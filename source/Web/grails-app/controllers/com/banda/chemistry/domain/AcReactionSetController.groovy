package com.banda.chemistry.domain

import com.banda.chemistry.business.*
import com.banda.chemistry.domain.AcReaction.ReactionDirection
import com.banda.core.util.ParseUtil
import com.banda.function.domain.Expression
import edu.banda.coel.web.BaseDomainController
import edu.banda.coel.web.ChemistryCommonService
import edu.banda.coel.business.chemistry.AcDNAStrandDisplacementConverter
import grails.converters.JSON

class AcReactionSetController extends BaseDomainController {

	def acUtil = ArtificialChemistryUtil.instance
	def acRateUtil = AcRateConstantUtil.instance
	def replicator = AcReplicator.instance
	def ChemistryCommonService chemistryCommonService

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
			acSpeciesSets = chemistryCommonService.getThisAndDerivedSpeciesSets(result.instance.speciesSet)
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

    protected def deleteInstance(instance) {
        def singleRefReactionSet = AcReactionSet.countBySpeciesSet(instance.speciesSet) == 1

        doClazz.withTransaction{ status ->
            instance.delete(flush: true)
        }

        if (singleRefReactionSet) {
            def noRefInteractionSeries = AcInteractionSeries.countBySpeciesSet(instance.speciesSet) == 0
            def noRefTranslationSeries = AcTranslationSeries.countBySpeciesSet(instance.speciesSet) == 0
            if (noRefInteractionSeries && noRefTranslationSeries)
                AcSpeciesSet.withTransaction { status ->
                    instance.speciesSet.delete(flush: true)
                }
        }
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
        def interactionSeriesIds = params.interactionSeriesId.collect{ if (it.value == "on") it.key else null } - null

        def newReactionSet = new AcReactionSet()
		def stats
        try {
            def converter = new AcDNAStrandDisplacementConverter(acReactionSetInstance, params.double('CMax'), true)
            newReactionSet = converter.convert()
            stats = converter.getStats()
            def newSpeciesSet = newReactionSet.speciesSet
            acReactionSetInstance.speciesSet.save(flush: true)

            if (newSpeciesSet != acReactionSetInstance.speciesSet) {
                newSpeciesSet.name = acReactionSetInstance.speciesSet.name + "-DNA SD"
                newSpeciesSet.createTime = new Date()
                newSpeciesSet.createdBy = currentUserOrError

                def newParameterSet = newSpeciesSet.parameterSet
                newParameterSet.name = newSpeciesSet.name
                newParameterSet.createTime = newSpeciesSet.createTime
                newParameterSet.createdBy = newSpeciesSet.createdBy
                newSpeciesSet.save(flush: true)
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

        flash.message = message(code: 'reactionSet.dnasd.message', args: [acReactionSetInstance.id, Double.toString(stats.cMax), Double.toString(stats.qMax), Double.toString(stats.sigmaMax), Double.toString(stats.gammaInverse)]) + "</br>" +
                        message(code: 'default.created.message', args: [doClazzMessageLabel, newReactionSet.id])

        if (params.createNewCompartment) {
            def newCompartment = new AcCompartment()
            newCompartment.label = acReactionSetInstance.label + "-DNA SD"
            newCompartment.createTime = new Date()
            newCompartment.createdBy = currentUserOrError
            newCompartment.reactionSet = newReactionSet
            if (!newCompartment.save(flush: true)) {
                render(view: "show", model: [instance: acReactionSetInstance])
                return
            }
            flash.message = flash.message + "</br>" + message(code: 'default.created.message', args: [message(code: 'acCompartment.label', default: 'Compartment'), newCompartment.id])
        }

        if (!interactionSeriesIds.isEmpty()) {
            def newSpeciesSet = newReactionSet.speciesSet
            def fuelSpecies = newSpeciesSet.variables.findAll{ it.label.matches("^(G_|L_|T_|B_|LS_|BS_).*\$") }

            interactionSeriesIds.each{
                def interactionSeries = AcInteractionSeries.get(it)
                def newInteractionSeries = transformInteractionSeriesToDNASD(interactionSeries, newSpeciesSet, stats.gammaInverse, stats.cMax, fuelSpecies)
                if (!newInteractionSeries.save(flush: true)) {
                    render(view: "show", model: [instance: acReactionSetInstance])
                    return
                }
                flash.message = flash.message + "</br>" + message(code: 'default.created.message', args: [message(code: 'acInteractionSeries.label', default: 'Interaction Series'), newInteractionSeries.id])
            }
        }

        render(view: "show", model: [instance: newReactionSet])
	}

    private def transformInteractionSeriesToDNASD(
        AcInteractionSeries interactionSeries,
        AcSpeciesSet dnaSdSpeciesSet,
        Double gammaInverse,
        Double cMax,
        Collection<AcSpecies> fuelGates
    ) {
        def newInteractionSeries = replicator.cloneActionSeriesWithActions(interactionSeries)

        replicator.nullIdAndVersionRecursively(newInteractionSeries)
        newInteractionSeries.name = interactionSeries.name + "-DNA SD"
        newInteractionSeries.timeCreated = new Date()
        newInteractionSeries.createdBy = currentUserOrError
        newInteractionSeries.speciesSet = dnaSdSpeciesSet

        def initialInteraction = newInteractionSeries.actions.find{it.startTime == 0}
        if (initialInteraction) {
            // transform the initial concentration of the original species as γ^{−1} * c_j
            initialInteraction.speciesActions.each{speciesAction ->
                def expression = (Expression) speciesAction.settingFunction
                if (expression.getReferencedVariables().isEmpty()) {
                    def oldValue = expression.formula.toDouble()
                    expression.formula = (oldValue * gammaInverse).toString()
                }
            }

            // set the initial concentration of the fuel gates (G,L,T,B,HS,and LS) to C_max
            def newFuelSettings = fuelGates.collect{ fuelGate ->
                def speciesAction = new AcSpeciesInteraction()
                speciesAction.species = fuelGate
                speciesAction.settingFunction = Expression.Double(cMax.toString())
                speciesAction
            }
            initialInteraction.addSpeciesActions(newFuelSettings)
        }
        newInteractionSeries
    }

    def getInteractionSeriesData(Long id) {
        def acReactionSetInstance = getSafe(id)
        def interactionSeries = AcInteractionSeries.findAllBySpeciesSet(acReactionSetInstance.speciesSet)
        def interactionSeriesMap = [ : ]

        interactionSeries.each{ interactionSeriesMap.putAt(it.id, it.id + " : " + it.name) }
        render interactionSeriesMap as JSON
    }

	def exportAsOctaveMatlab(Long id) {
		def acReactionSetInstance = getSafe(id)
		def octaveOutput = OctaveGenerator.apply(acReactionSetInstance)
		response.setHeader("Content-disposition", "attachment; filename=" + "reactionSet_${id}_ode.m");
		render(contentType: "text", text: octaveOutput.toString());
	}

	def getReactionStructureImages(Long id) {
		def instance = getSafe(id)
		def images = chemistryCommonService.getReactionStructureImages(instance, true)
		render images as JSON
	}

	def getSpeciesStructureImages(Long id) {
		def instance = getSafe(id)
		def images = chemistryCommonService.getSpeciesStructureImages(instance, true)
		render images as JSON
	}

	def scaleForwardRateConstants(Long id) {
		def instance = getSafe(id)
		def uniScale = params.double('uniScale')
		def biScale = params.double('biScale')
        if (params.uniScaleInverse)
            uniScale = 1 / uniScale

        if (params.biScaleInverse)
            biScale = 1/ biScale

        def instanceToScale = instance

		if (params.createNewReactionSet) {
            instanceToScale = replicator.cloneReactionSetWithReactionsAndGroups(instance)
            replicator.nullIdAndVersionRecursively(instanceToScale)

            def label = instanceToScale.label + " scaled"
            if (label.size() > 100) label = label.substring(0, 100)

            instanceToScale.label = label
            instanceToScale.createTime = new Date()
            instanceToScale.createdBy = currentUserOrError
        }

        instanceToScale.reactions.each{ reaction ->
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
		if (instanceToScale.save(flush: true)) {
            if (instanceToScale == instance)
			    flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, instanceToScale.id])
            else
                flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, instanceToScale.id])
		}
		redirect(action: "show", id: instanceToScale.id)
	}
}