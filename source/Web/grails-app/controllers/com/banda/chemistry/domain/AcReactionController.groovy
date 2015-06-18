package com.banda.chemistry.domain

import com.banda.chemistry.business.AcKineticsBO
import com.banda.chemistry.business.AcRateConstantUtil
import com.banda.chemistry.business.AcReplicator
import com.banda.chemistry.business.ArtificialChemistryUtil
import com.banda.chemistry.domain.AcReaction.ReactionDirection
import com.banda.core.util.ConversionUtil
import com.banda.core.util.ParseUtil
import com.banda.function.BndFunctionException
import edu.banda.coel.business.chempic.ChemistryPicGeneratorImpl
import edu.banda.coel.domain.service.ChemistryPicGenerator
import edu.banda.coel.domain.util.GeneralUtil
import edu.banda.coel.web.BaseDomainController
import java.lang.NumberFormatException

class AcReactionController extends BaseDomainController {

	def acUtil = ArtificialChemistryUtil.instance
	def acRateUtil = AcRateConstantUtil.instance
	def replicator = AcReplicator.instance
	def ChemistryPicGenerator chemPicGenerator = new ChemistryPicGeneratorImpl(true);

	def create() {
		def instance =  new AcReaction()
		instance.properties = params

		if (params.reactionSet.id) {
			def reactionSet = AcReactionSet.get(params.reactionSet.id)
			reactionSet.addReaction(instance)
			[instance: instance]
		} else {
			flash.message = "Reaction set expected for a new reaction"
			redirect(controller: "acReactionSet", action: "list")
			return
		}
	}

	def save() {
		def acReactionInstance = new AcReaction(params)
		acReactionInstance.enabled = true

		// Reaction set
		AcReactionSet reactionSet = AcReactionSet.get(params.reactionSet.id)
		reactionSet.addReaction(acReactionInstance)

		// Label
		setLabelIfNotDefined(acReactionInstance)
		checkLabelUniqueness(acReactionInstance)

		// Reactants, products, catalysts and inhibitors
		try {
			acUtil.setSpeciesAssociationsFromString(params.reactants, AcSpeciesAssociationType.Reactant, acReactionInstance)
		} catch (e) {
			acReactionInstance.errors.rejectValue("speciesAssociations","Reactants: " + e.message)
		}

		try {
			acUtil.setSpeciesAssociationsFromString(params.products, AcSpeciesAssociationType.Product, acReactionInstance)
		} catch (e) {
			acReactionInstance.errors.rejectValue("speciesAssociations","Products: " + e.message)
		}

		if (!acReactionInstance.hasSpeciesAssociations(AcSpeciesAssociationType.Reactant)
			&& !acReactionInstance.hasSpeciesAssociations(AcSpeciesAssociationType.Product))
			acReactionInstance.errors.rejectValue("speciesAssociations","No reactants nor products defined.")

		if (params.catalystIds) {
			def catalystIds = ConversionUtil.convertCollection(Long.class, params.list('catalystIds'), "Catalyst id")
			catalystIds.each{ catalystId ->
				acReactionInstance.addSpeciesAssociation(new AcSpeciesReactionAssociation(new AcSpecies(catalystId), AcSpeciesAssociationType.Catalyst))
			}
		}
		if (params.inhibitorIds) {
			def inhibitorIds = ConversionUtil.convertCollection(Long.class, params.list('inhibitorIds'), "Inhibitor id")
			inhibitorIds.each{ inhibitorId ->
				acReactionInstance.addSpeciesAssociation(new AcSpeciesReactionAssociation(new AcSpecies(inhibitorId), AcSpeciesAssociationType.Inhibitor))
			}
		}

		// Rate constants and/or functions
		setRateConstantsAndFunctions(acReactionInstance)

		if (acReactionInstance.hasErrors() || !acReactionInstance.save(flush: true)) {
			render(view: "create", model: [instance: acReactionInstance])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, acReactionInstance.id])
		redirect(controller: "acReactionSet", action: "show", id: reactionSet.id)
	}

	def update(Long id, Long version) {
		def acReactionInstance = getSafe(id)
		if (!acReactionInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (acReactionInstance.version > version) {
				setOlcFailureMessage(acReactionInstance)
				render(view: "edit", model: [instance: acReactionInstance])
				return
			}

		// Reaction group
		def oldGroupId = acReactionInstance.group?.id
		def newGroupId = params.long('group.id')
		if (oldGroupId != newGroupId) {
			if (newGroupId != null) {
				AcReactionGroup group = AcReactionGroup.get(newGroupId)
				group.addReaction(acReactionInstance)
			} else {
				AcReactionGroup oldGroup = AcReactionGroup.get(oldGroupId)
				oldGroup.removeReaction(acReactionInstance)
			}
		}

		acReactionInstance.properties = params

		checkLabelUniqueness(acReactionInstance)

	    // Reactants, products, catalysts and inhibitors
		try {
			acUtil.updateSpeciesAssociations(params.reactants, AcSpeciesAssociationType.Reactant, acReactionInstance)
		} catch (e) {
			acReactionInstance.errors.rejectValue("speciesAssociations","Reactants: " + e.message)
		}

		try {
			acUtil.updateSpeciesAssociations(params.products, AcSpeciesAssociationType.Product, acReactionInstance)
		} catch (e) {
			acReactionInstance.errors.rejectValue("speciesAssociations","Products: " + e.message)
		}

		if (!acReactionInstance.hasSpeciesAssociations(AcSpeciesAssociationType.Reactant)
			&& !acReactionInstance.hasSpeciesAssociations(AcSpeciesAssociationType.Product))
			acReactionInstance.errors.rejectValue("speciesAssociations","No reactants nor products defined.")

		def newCatalystAssocs = []
		if (params.catalystIds) {
			def catalystIds = ConversionUtil.convertCollection(Long.class, params.list('catalystIds'), "Catalyst id")
			catalystIds.each{ catalystId ->
				newCatalystAssocs.add(new AcSpeciesReactionAssociation(new AcSpecies(catalystId), AcSpeciesAssociationType.Catalyst))
			}
		}
		acUtil.updateSpeciesAssociations(newCatalystAssocs, AcSpeciesAssociationType.Catalyst, acReactionInstance)

		def newInhibitorAssocs = []
		if (params.inhibitorIds) {
			def inhibitorIds = ConversionUtil.convertCollection(Long.class, params.list('inhibitorIds'), "Inhibitor id")
			inhibitorIds.each{ inhibitorId ->
				newInhibitorAssocs.add(new AcSpeciesReactionAssociation(new AcSpecies(inhibitorId), AcSpeciesAssociationType.Inhibitor))
			}
		}
		acUtil.updateSpeciesAssociations(newInhibitorAssocs, AcSpeciesAssociationType.Inhibitor, acReactionInstance)

		// Rate constants and/or functions
		setRateConstantsAndFunctions(acReactionInstance)

		if (acReactionInstance.hasErrors() || !acReactionInstance.save(flush: true)) {
			render(view: "edit", model: [instance: acReactionInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acReactionInstance.id])
		redirect(controller: "acReactionSet", action: "show", id: acReactionInstance.reactionSet.id)
	}

	def updateLabelAjax(String value, Long id) {
		def instance = getSafe(id)
		if (!instance) {
			handleObjectNotFoundAjax(id)
			return
		}

		instance.label = value
		checkLabelUniqueness(instance)

		saveFromAjax(instance, "label")
	}

	def updateReactantsAndProductsAjax(String value, Long id) {
		def instance = getSafe(id)
		if (!instance) {
			handleObjectNotFoundAjax(id)
			return
		}

		def parts = value.trim().split("→")
		if (!value.contains("→"))
			instance.errors.rejectValue("speciesAssociations","The reactions/products delimeter expected.".encodeAsHTML())
		else if (parts.length == 0)
			instance.errors.rejectValue("speciesAssociations","No reactans or products defined.".encodeAsHTML())
		else if (parts.length > 2)
			instance.errors.rejectValue("speciesAssociations","Multiple reactions/products delimeters found.".encodeAsHTML())
		else {
			def reactants = ""
			def products = ""
			if (parts.length == 1) {
				if (value.trim().indexOf("→") == 0)
					products = parts[0]
				else
					reactants = parts[0]
			} else {
				reactants = parts[0]
				products = parts[1]
			}

			try {
				acUtil.updateSpeciesAssociations(reactants, AcSpeciesAssociationType.Reactant, instance)
			} catch (e) {
				instance.errors.rejectValue("speciesAssociations","Reactants: " + e.message)
			}
			try {
				acUtil.updateSpeciesAssociations(products, AcSpeciesAssociationType.Product, instance)
			} catch (e) {
				instance.errors.rejectValue("speciesAssociations","Products: " + e.message)
			}
		}
		if (instance.hasForwardRateConstants())
			resetForwardRateConstants(instance)
		if (instance.hasReverseRateConstants())
			resetForwardRateConstants(instance)
		saveFromAjax(instance, "speciesAssociations")
	}

	def updateCatalystsAjax(String value, Long id) {
		def instance = getSafe(id)
		if (!instance) {
			handleObjectNotFoundAjax(id)
			return
		}

		def speciesMap = acUtil.getLabelVariableMap(instance.speciesSet)
		def newCatalystAssocs = []
		if (!value.trim().isEmpty()) {
			def catalystLabels = value.split(",")
			catalystLabels.each{ catalystLabel ->
				def catalyst = speciesMap.get(catalystLabel)
				if (catalyst == null)
					instance.errors.rejectValue("speciesAssociations","Species $catalystLabel not found.")
				else 
					newCatalystAssocs.add(new AcSpeciesReactionAssociation(catalyst, AcSpeciesAssociationType.Catalyst))
			}
		}
		if (!instance.hasErrors()) {
			acUtil.updateSpeciesAssociations(newCatalystAssocs, AcSpeciesAssociationType.Catalyst, instance)
			if (instance.hasForwardRateConstants())
				resetForwardRateConstants(instance)
			if (instance.hasReverseRateConstants())
				resetForwardRateConstants(instance)
		}

		saveFromAjax(instance, "speciesAssociations")
	}

	def show(Long id) {
		def result = super.show(id)
		def structureImage = getStructureImage(result.instance)
		result << [structureImage : structureImage]
	}
 
	def edit(Long id) {
		def result = super.edit(id)
		def structureImage = getStructureImage(result.instance)
		result << [structureImage : structureImage]
	}

	def copyMultiple() {
		if (!params.ids) {
			flash.message = "No rows selected"
			redirect(controller: "acReactionSet", action: "list")
			return
		}

		def ids = ParseUtil.parseArray(params.ids, Long.class, "Reaction id", ",")
		Collections.sort(ids)
		def newIds = ids.collect{copyInstance(it)}.flatten()

		if (newIds.size() == 1)
			flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, newIds.get(0)])
		else
			flash.message = "Multiple reactions created: ${newIds}"

		def reactionSet = getSafe(ids.get(0)).reactionSet
		redirect(controller: "acReactionSet", action: "show", id: reactionSet.id)
	}

	private def copyInstance(Long id) {
		def acReactionInstance = getSafe(id)
		def acReactionInstanceClone = replicator.cloneReaction(acReactionInstance)

		// introduce new label
		def label = acReactionInstanceClone.label
		def coreLabel = label.replaceAll("copy[0-9]*", "")
		if (!label.equals(coreLabel) && label.startsWith(coreLabel)) {
			def reactionIndex = 1
			def reactionLabels = acReactionInstance.reactionSet.reactions*.(label)

			label = coreLabel + "copy" + reactionIndex
			while (reactionLabels.contains(label)) {
				reactionIndex++
				label = coreLabel + "copy" + reactionIndex
			}
		} else {
			label = label + " copy"
		}

		if (label.size() > 30) label = label.substring(0, 30)
		acReactionInstanceClone.label = label
		acReactionInstanceClone.reactionSet = null
		acReactionInstanceClone.group = null
		acReactionInstance.reactionSet.addReaction(acReactionInstanceClone)
		if (acReactionInstance.group)
			acReactionInstance.group.addReaction(acReactionInstanceClone)
		replicator.nullIdAndVersion(acReactionInstanceClone)
		acReactionInstanceClone.save(flush: true)
		acReactionInstanceClone.id
	}

	def associateOrDisassociateFromGroup = {
		def acReactionInstance = getSafe(params.id)
		def acReactionGroupInstance = acReactionInstance.group
		if (acReactionGroupInstance) {
			// reamove if exists
			acReactionGroupInstance.removeReaction(acReactionInstance)
		} else {
			// add to group
		    def reactionGroupId = request.getParameter('reactionGroupId')
			acReactionGroupInstance = AcReactionGroup.get(reactionGroupId)
			acReactionGroupInstance.addReaction(acReactionInstance)
		}
		if (acReactionInstance.save(flush: true)) {
			flash.message = "AC reaction successfully removed from the group"
		} else {
			flash.message = "Problems occured while removing reaction from the group"
		}
		redirect(controller: "acReactionGroup", action: "edit", params:[instance : acReactionGroupInstance])
	}

	def importRatesFromText = {
		def acReactionInstance = getSafe(params.id)
		acRateUtil.setRateConstantsFromString(params.ratesInput, acReactionInstance, ReactionDirection.Both)
	}

   /**
	* Sets the label if not defined.
	*/
   private def setLabelIfNotDefined(reaction) {
	   if (!reaction.label || reaction.label.isEmpty()) {
		   def reactionIndex = reaction.reactionSet.reactionsNum
		   def reactionLabels = reaction.reactionSet.reactions*.(label)

		   def label = "R" + (reactionIndex < 10 ? "0" : "") + reactionIndex
		   
		   while (reactionLabels.contains(label)) {
			   reactionIndex++
			   label = "R" + (reactionIndex < 10 ? "0" : "") + reactionIndex
		   }

		   reaction.label = label
	   }
   }

   private def checkLabelUniqueness(acReactionInstance) {
	   def matchedReaction = acReactionInstance.reactionSet.reactions.find{ it != acReactionInstance && it.label.equals(acReactionInstance.label) }
	   if (matchedReaction) {
		   acReactionInstance.errors.rejectValue(
			   "label",
			   message(code: 'default.not.unique.message', args: ['label', 'Reaction', acReactionInstance.label]))
	   }
   }

   private def setRateConstantsAndFunctions(acReactionInstance) {
	   if (params.forwardRateConstants && params.forwardRateFunction?.formula)
		   acReactionInstance.errors.rejectValue("forwardRateConstants", "Forward rate constants and function defined at the same time.")
	   else
		   if (params.forwardRateFunction?.formula) {
			   try {
				   acUtil.setRateFunctionFromString(params.forwardRateFunction.formula, acReactionInstance, true)
				   acReactionInstance.setForwardRateConstants(null)
			   } catch (BndFunctionException e) {
			   		acReactionInstance.errors.rejectValue("forwardRateFunction", "Rate Function '" + params.forwardRateFunction.formula + "' is invalid. " + e.getMessage())
			   }
		   } else {
			   setForwardRateConstants(params.forwardRateConstants, acReactionInstance)
			   acReactionInstance.setForwardRateFunction(null)
		   }

	   if (params.reverseRateConstants && params.reverseRateFunction?.formula)
		   acReactionInstance.errors.rejectValue("reverseRateConstants", "Reverse rate constants and function defined at the same time.")
	   else
		   if (params.reverseRateFunction?.formula) {
			   try {
				   acUtil.setRateFunctionFromString(params.reverseRateFunction.formula, acReactionInstance, false)
				   acReactionInstance.setReverseRateConstants(null)
			   } catch (BndFunctionException e) {
			   		acReactionInstance.errors.rejectValue("reverseRateFunction", "Rate Function '" + params.reverseRateFunction.formula + "' is invalid. " + e.getMessage())
			   }
		   } else if (params.reverseRateConstants) {
               setReverseRateConstants(params.reverseRateConstants, acReactionInstance)
			   acReactionInstance.setReverseRateFunction(null)
		   } else {
		   	   acReactionInstance.setReverseRateConstants(null)
			   acReactionInstance.setReverseRateFunction(null)
		   }
   }

   protected def deleteInstance(instance) {
	   def reactionSet = instance.reactionSet
	   AcReaction.withTransaction{ status ->
		   reactionSet.removeReaction(instance)
		   instance.removeFromGroup()
		   instance.delete(flush: true)
	   }
   }

   protected def notFoundRedirect(id) {
	   redirect(controller: "acReactionSet", action: "list")
   }

   protected def deleteSuccessfulRedirect(instance, owner) {
	   redirect(controller: "acReactionSet", action: "show", id: owner.id)
   }

   protected def deleteMultiSuccessfulRedirect(instancesWithOwners) {
	   redirect(controller: "acReactionSet", action: "show", id: instancesWithOwners.first().value.id)
   }

   protected def getOwner(instance) {
	   instance.reactionSet
   }

   /**
	* Sets the rate constants from String (user's input).
	*/
   private def setForwardRateConstants(rateConstants, reaction) {
       try {
           acRateUtil.setRateConstantsFromString(rateConstants, reaction, ReactionDirection.Forward)
           int requiredRateConstantsNum = AcKineticsBO.createInstance(reaction, true).getRequiredRateConstantsNum()
           def adaptedRateConstants = GeneralUtil.expandOrShrinkArrayIfNeeded(reaction.forwardRateConstants, requiredRateConstantsNum)
           GeneralUtil.setNullToZero(adaptedRateConstants, Double.class)
           reaction.setForwardRateConstants(adaptedRateConstants)
       } catch (e) {
           reaction.errors.rejectValue("forwardRateConstants", "Rate constant(s) '" + rateConstants + "' are invalid.")
       }
   }

   /**
   	* Sets the rate constants from String (user's input).
   	*/
   private def setReverseRateConstants(rateConstants, reaction) {
      try {
          acRateUtil.setRateConstantsFromString(rateConstants, reaction, ReactionDirection.Reverse)
          int requiredRateConstantsNum = AcKineticsBO.createInstance(reaction, false).getRequiredRateConstantsNum()
          def adaptedRateConstants = GeneralUtil.expandOrShrinkArrayIfNeeded(reaction.reverseRateConstants, requiredRateConstantsNum)
          GeneralUtil.setNullToZero(adaptedRateConstants, Double.class)
          reaction.setReverseRateConstants(adaptedRateConstants)
      } catch (e) {
          reaction.errors.rejectValue("reverseRateConstants", "Rate constant(s) '" + rateConstants + "' are invalid.")
      }
   }

   private def resetForwardRateConstants(reaction) {
	   int requiredRateConstantsNum = AcKineticsBO.createInstance(reaction, true).getRequiredRateConstantsNum()
	   def adaptedRateConstants = GeneralUtil.expandOrShrinkArrayIfNeeded(reaction.forwardRateConstants, requiredRateConstantsNum)
	   GeneralUtil.setNullToZero(adaptedRateConstants, Double.class)
	   reaction.setForwardRateConstants(adaptedRateConstants)
   }

   private def resetReverseRateConstants(reaction) {
	   int requiredRateConstantsNum = AcKineticsBO.createInstance(reaction, false).getRequiredRateConstantsNum()
	   def adaptedRateConstants = GeneralUtil.expandOrShrinkArrayIfNeeded(reaction.reverseRateConstants, requiredRateConstantsNum)
	   GeneralUtil.setNullToZero(adaptedRateConstants, Double.class)
	   reaction.setReverseRateConstants(adaptedRateConstants)
	}
 
   def getAllReactionSets = {
	   AcReactionSet.listWithProjections(['id', 'label'])
   }

   private def getStructureImage(acReaction) {
	   def reactantStructures = acReaction.getSpeciesAssociations(AcSpeciesAssociationType.Reactant).collect{ assoc ->
	   		def structure = assoc.species.structure
   			int intStoichiometry = assoc.getStoichiometricFactor().intValue()
   			def structureList = []
			for (i in 1..intStoichiometry) {
   				structureList.add(structure)
			}
			structureList
	   }.flatten()

	   def productStructures = acReaction.getSpeciesAssociations(AcSpeciesAssociationType.Product).collect{ assoc ->
	   		def structure = assoc.species.structure
   			int intStoichiometry = assoc.getStoichiometricFactor().intValue()
   			def structureList = []
			for (i in 1..intStoichiometry) {
   				structureList.add(structure)
			}
			structureList
	   }.flatten()

	   reactantStructures = reactantStructures.findAll{ it != null && !it.isEmpty() }
	   productStructures = productStructures.findAll{ it != null && !it.isEmpty() }

	   if (!reactantStructures.isEmpty() || !productStructures.isEmpty()) {
		   chemPicGenerator.createDNAReactionSVG(reactantStructures, productStructures, acReaction.hasReverseRateConstants())
	   } else {
		   null
	   }
   }
}