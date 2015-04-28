package com.banda.chemistry.domain

import edu.banda.coel.business.chempic.ChemistryPicGeneratorImpl
import edu.banda.coel.domain.service.ChemistryPicGenerator

import edu.banda.coel.web.BaseDomainController
import grails.converters.JSON

import com.banda.core.util.ParseUtil
import com.banda.chemistry.business.*
import com.banda.chemistry.domain.AcReaction.ReactionDirection
import com.banda.chemistry.business.OctaveGenerator
import edu.banda.coel.web.SvgStructureData

class AcReactionSetController extends BaseDomainController {

	def acUtil = ArtificialChemistryUtil.instance
	def acRateUtil = AcRateConstantUtil.instance
	def replicator = AcReplicator.instance
	def ChemistryPicGenerator chemPicGenerator = new ChemistryPicGeneratorImpl(true);

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','label','createTime','createdBy']
		super.list(max)
	}

	def create() {
		params.createdBy = getCurrentUserOrError()

		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'

		def acSpeciesSets = AcSpeciesSet.listWithParamsAndProjections(params)

		[instance: new AcReactionSet(params), acSpeciesSets : acSpeciesSets]
	}
	
	def save() {
		def acReactionSetInstance = new AcReactionSet(params)
		acReactionSetInstance.createdBy = currentUserOrError

		def saved = false
		if (params.speciesSetChoice == '1')
			saved = saveWithNewSpeciesSet(acReactionSetInstance)
		else
			saved = acReactionSetInstance.save(flush: true)

		if (!saved) {
			if (!isAdmin())
				params.createdBy = getCurrentUserOrError()

			params.projections = ['id','name']
			params.sort = 'id'
			params.order = 'desc'

			def acSpeciesSets = AcSpeciesSet.listWithParamsAndProjections(params)
			render(view: "create", model: [instance: acReactionSetInstance, acSpeciesSets : acSpeciesSets])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, acReactionSetInstance.id])
		redirect(action: "show", id: acReactionSetInstance.id)
	}

	private def saveWithNewSpeciesSet(instance) {
		def label = instance.label
		def speciesSet = createSpeciesSet(label)
		instance.speciesSet = speciesSet

		def saved = false;
		if (speciesSet.save())
			if (instance.save(flush: true))
				saved = true

		saved
	}

	private def createSpeciesSet(name) {
		def acSpeciesSetInstance = new AcSpeciesSet()
		acSpeciesSetInstance.name = name
		acSpeciesSetInstance.createdBy = currentUserOrError
		acSpeciesSetInstance.initVarSequenceNum()

		AcParameterSet parameterSet = new AcParameterSet()
		parameterSet.name = acSpeciesSetInstance.name
		parameterSet.createTime = acSpeciesSetInstance.createTime
		parameterSet.createdBy = acSpeciesSetInstance.createdBy

		acSpeciesSetInstance.parameterSet = parameterSet
		parameterSet.speciesSet = acSpeciesSetInstance
		acSpeciesSetInstance
	}

	def edit(Long id) {
		def result = super.edit(id)
		def acSpeciesSets
		if (result.instance.reactions.isEmpty()) {
			if (!isAdmin())
				params.createdBy = getCurrentUserOrError()
			params.projections = ['id','name']
			params.sort = 'id'
			params.order = 'desc'
			acSpeciesSets = AcSpeciesSet.listWithParamsAndProjections(params)
		} else
			acSpeciesSets = getThisAndDerivedSpeciesSets(result.instance.speciesSet)
		result << [acSpeciesSets : acSpeciesSets]
	}

	def update(Long id, Long version) {
		def acReactionSetInstance = getSafe(id)
		if (!acReactionSetInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (acReactionSetInstance.version > version) {
				setOlcFailureMessage(acReactionSetInstance)
				render(view: "edit", model: [instance: acReactionSetInstance])
				return
			}

		acReactionSetInstance.properties = params

		if (!updateSafe(acReactionSetInstance)) {
			render(view: "edit", model: [instance: acReactionSetInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acReactionSetInstance.id])
		redirect(action: "show", id: acReactionSetInstance.id)
	}

	protected def copyInstance(instance) {
		def newInstance = replicator.cloneReactionSetWithReactionsAndGroups(instance)
		replicator.nullIdAndVersionRecursively(newInstance)

		def label = newInstance.label + " copy"
		if (label.size() > 100) label = label.substring(0, 100)

		newInstance.label = label
		newInstance.createTime = new Date()
		newInstance.createdBy = currentUserOrError

		newInstance.save(flush: true)
		newInstance
	}

	def replaceSpeciesSet(Long id, Long speciesSetId) {
		def acReactionSetInstance = getSafe(id)
		def acSpeciesSetInstance = AcSpeciesSet.get(speciesSetId)

		try {
			acUtil.replaceSpeciesSet(acReactionSetInstance, acSpeciesSetInstance)
			if (acReactionSetInstance.save(flush: true)) {
				flash.message = "Species set of the reaction set " +  id + " replaced with " + speciesSetId + " ."
			}
		}  catch (e) {
			flash.message = "Species set replacement failed! " + e.message
		}

		redirect(action: "show", id: id)
	}

	def addReactions(Long id, Long additionalReactionSetId) {
		def reactionSet = getSafe(id)
		def additionalReactionSet = getSafe(additionalReactionSetId)

		addReactionsAux(reactionSet, additionalReactionSet, params.createNewSpeciesSet)
		flash.message = "Reactions from " + additionalReactionSetId + " added to " + id
		redirect(action: "show", id: id)
	}

	def addReactionsMultiple() {
		if (!params.ids) {
			flash.message = "No rows selected!"
			notFoundRedirect(null)
			return
		}
		def ids = ParseUtil.parseArray(params.ids, Long.class, doClazzVariableName + " id", ",")
		Collections.sort(ids)

		def additionalReactionSet = getSafe(params.long('additionalReactionSetId'))
		if (!additionalReactionSet) {
			handleObjectNotFound(params.additionalReactionSetId)
			return
		}
		def error = false
		ids.collect{ id ->
			def instance = getSafe(id)
			if (!instance) {
				error = true
				handleObjectNotFound(id)
				return
			}
			addReactionsAux(instance, additionalReactionSet, params.createNewSpeciesSet)
		}
		if (!error) {
			def size = ids.size()
			if (size == 1)
				flash.message = "Reactions from " + params.additionalReactionSetId + " added to " + ids.get(0)
			else
				flash.message = "Reactions from " + params.additionalReactionSetId + " added to " + ids
			redirect(action: "list")
		}
	}

	def addReactionsAux(reactionSet, additionalReactionSet, createNewSpeciesSet) {
		if (createNewSpeciesSet) {
			def oldSpeciesSet = reactionSet.speciesSet
			def newSpeciesSet = new AcSpeciesSet()
			newSpeciesSet.name = oldSpeciesSet.name + '-ext'
			newSpeciesSet.createdBy = currentUserOrError
	
			AcParameterSet parameterSet = new AcParameterSet()
			parameterSet.name = newSpeciesSet.name
			parameterSet.createTime = newSpeciesSet.createTime
			parameterSet.createdBy = newSpeciesSet.createdBy
	
			newSpeciesSet.parameterSet = parameterSet
			parameterSet.speciesSet = newSpeciesSet

			newSpeciesSet.parentSpeciesSet = oldSpeciesSet
			newSpeciesSet.initVarSequenceNum()

			if (!newSpeciesSet.save(flush: true)) {
				return
			}
			reactionSet.speciesSet = newSpeciesSet
		}

		def newSpecies = acUtil.getNewSpecies(reactionSet, additionalReactionSet)
		if (!newSpecies.isEmpty()) {
			reactionSet.speciesSet.addVariables(newSpecies)
			if (!reactionSet.speciesSet.save(flush: true)) {
				return
			}
		}

		acUtil.addReactions(reactionSet, additionalReactionSet)
		reactionSet.save(flush: true)
	}

	def getSpeciesSetsData() {
		def speciesSetsData = [ : ]
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'

		def speciesSets = AcSpeciesSet.listWithParamsAndProjections(params)
		speciesSets.each{ speciesSetsData.putAt(it.id, it.id + " : " + it.name) }

		render speciesSetsData as JSON
	}

	def getReactionSetsData() {
		def reactionSetsData = [ : ]
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','label']
		params.sort = 'id'
		params.order = 'desc'

		def reactionSets = AcReactionSet.listWithParamsAndProjections(params)
		reactionSets.each{ reactionSetsData.putAt(it.id, it.id + " : " + it.label) }

		render reactionSetsData as JSON
	}

	def importRatesFromText(Long id) {
		def acReactionSetInstance = getSafe(id)
		acRateUtil.setRateConstantsFromString(params.ratesInput, acReactionSetInstance, ReactionDirection.Both, null, null)
		acReactionSetInstance.reactions.each{ reaction ->
			// just to make it dirty
			reaction.setForwardRateConstants(reaction.getForwardRateConstants().clone())
		}
		if (acReactionSetInstance.save(flush: true)) {
			flash.message = "${message(code: 'default.updated.message', args: [message(code: 'acReactionSet.label', default: 'AcReactionSet'), acReactionSetInstance.id])}"	
		}
		redirect(action: "show", id: acReactionSetInstance.id)
	}

	def exportAsLatexTable(Long id) {
		def acReactionSetInstance = getSafe(id)

		def includeRate = params.includeRates
		def latexTable = acUtil.getLatexTable(acReactionSetInstance, includeRate != null)
		response.setHeader("Content-disposition", "attachment; filename=" + "reactionSet_${id}_latex");
		render(contentType: "text", text: latexTable);
	}

	def createReport() {
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','label']
		params.sort = 'id'
		params.order = 'desc'

		def reactionSets = AcReactionSet.listWithParamsAndProjections(params)

		chain(controller:'jasper',action:'index',model:[data:reactionSets],params:params)
	}

	def createCatalysisMassActionSubstReactionSet(Long id) {
		def acReactionSetInstance = getSafe(id)
		def massActionType = MassActionType.fromString(params.massActionType)

		AcMichaelisMentenToMassActionRSConverter converter = new AcMichaelisMentenToMassActionRSConverter(acReactionSetInstance, massActionType)
		
		def newReactionSet = converter.convert()
		def newSpeciesSet = newReactionSet.speciesSet
		if (newSpeciesSet != acReactionSetInstance.speciesSet) {
			newSpeciesSet.name = acReactionSetInstance.speciesSet.name + "-ext"
			newSpeciesSet.createTime = new Date()
			newSpeciesSet.createdBy = currentUserOrError

			def newParameterSet = newSpeciesSet.parameterSet
			newParameterSet.name = newSpeciesSet.name
			newParameterSet.createTime = newSpeciesSet.createTime
			newParameterSet.createdBy = newSpeciesSet.createdBy
			newSpeciesSet.save(flush: true)		
		} else {
//			flash.message = "No catalysis to replace, hence no action taken. Try basic copy function, if you want to clone existing reaction set."
		}
		newReactionSet.createTime = new Date()
		newReactionSet.createdBy = currentUserOrError
		newReactionSet.label = acReactionSetInstance.label + "-MA"
		replicator.nullIdAndVersionRecursively(newReactionSet)
		if (newReactionSet.save(flush: true)) {
			flash.message = "${message(code: 'default.created.message', args: [message(code: 'acReactionSet.label', default: 'AcReactionSet'), newReactionSet.id])}"
		}
		redirect(action: "list")
	}

	def copyAsDNASD(Long id) {
		def acReactionSetInstance = getSafe(id)

        def newReactionSet = new AcReactionSet()
        try {
            def converter = new AcDNAStrandDisplacementConverter(acReactionSetInstance, params.double('CMax'), true)
            newReactionSet = converter.convert()
            def newSpeciesSet = newReactionSet.speciesSet
            acReactionSetInstance.speciesSet.save(flush: true)

            if (newSpeciesSet != acReactionSetInstance.speciesSet) {
                newSpeciesSet.name = acReactionSetInstance.speciesSet.name + "-ext"
                newSpeciesSet.createTime = new Date()
                newSpeciesSet.createdBy = currentUserOrError

                def newParameterSet = newSpeciesSet.parameterSet
                newParameterSet.name = newSpeciesSet.name
                newParameterSet.createTime = newSpeciesSet.createTime
                newParameterSet.createdBy = newSpeciesSet.createdBy
                newSpeciesSet.save(flush: true)
            } else {
//			flash.message = "No catalysis to replace, hence no action taken. Try basic copy function, if you want to clone existing reaction set."
            }
        } catch (e) {
            acReactionSetInstance.errors.rejectValue("reactions", e.message)
        }

		newReactionSet.createTime = new Date()
		newReactionSet.createdBy = currentUserOrError
		newReactionSet.label = acReactionSetInstance.label + "-DNA SD"
		replicator.nullIdAndVersionRecursively(newReactionSet)
		if (newReactionSet.hasErrors() || !newReactionSet.save(flush: true)) {
            render(view: "show", model: [instance: acReactionSetInstance])
            return
		}
        flash.message = "${message(code: 'default.created.message', args: [message(code: 'acReactionSet.label', default: 'AcReactionSet'), newReactionSet.id])}"
        render(view: "show", model: [instance: newReactionSet])
	}

	def exportAsOctaveMatlab(Long id) {
		// TODO not safe
		def acReactionSetInstance = AcReactionSet.get(id)
		def octaveOutput = OctaveGenerator.apply(acReactionSetInstance)
		response.setHeader("Content-disposition", "attachment; filename=" + "reactionSet_${id}_ode.m");
		render(contentType: "text", text: octaveOutput.toString());
	}

	def getReactionStructureImages(Long id) {
		def instance = getSafe(id)
		def images = instance.reactions.collect{ reaction ->
			def structData = new SvgStructureData()
			structData.label = reaction.label
			structData.image = getStructureImage(reaction)
			structData
		}
		render images as JSON
	}

	def getSpeciesStructureImages(Long id) {
		def instance = getSafe(id)
		def refSpecies = acUtil.getReferencedSpecies(instance).toList()

		def images = refSpecies.sort{ it.label }.collect{ species -> 
			def structData = new SvgStructureData()
			structData.label = species.label
			if (species.structure && !species.structure.isEmpty()) {
				structData.image = chemPicGenerator.createDNAStrandSVG(species.structure)
			}			
			structData
		}

		render images as JSON
	}

	def scaleForwardRateConstants(Long id) {
		def instance = getSafe(id)
		def uniScale = params.double('uniScale')
		def biScale = params.double('biScale')

		def images = instance.reactions.each{ reaction -> 
			def overallStoichiometry = 0
			reaction.getSpeciesAssociations(AcSpeciesAssociationType.Reactant).each { assoc ->
				if (assoc.stoichiometricFactor)
					overallStoichiometry += assoc.stoichiometricFactor.intValue()
				else 
					overallStoichiometry++
			}
			for (index in 0..(reaction.getForwardRateConstantsNum() - 1)) {
				def oldConstant = reaction.getForwardRateConstants()[index]
				if (overallStoichiometry == 1)
					reaction.getForwardRateConstants()[index] = oldConstant * uniScale
				else if (overallStoichiometry == 2)
					reaction.getForwardRateConstants()[index] = oldConstant * biScale
			}
			// just to make it dirty
			reaction.setForwardRateConstants(reaction.getForwardRateConstants().clone())
		}
		if (instance.save(flush: true)) {
			flash.message = "${message(code: 'default.updated.message', args: [message(code: 'acReactionSet.label', default: 'Reaction Set'), instance.id])}"	
		}
		redirect(action: "show", id: instance.id)
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