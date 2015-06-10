package com.banda.chemistry.domain

import com.banda.chemistry.business.AcReplicator
import com.banda.chemistry.business.ArtificialChemistryUtil
import com.banda.core.dynamics.StateAlternationType
import com.banda.core.util.ParseUtil
import edu.banda.coel.web.BaseDomainController
import edu.banda.coel.web.ChemistryCommonService
import grails.converters.JSON

class AcInteractionSeriesController extends BaseDomainController {

	def acUtil = ArtificialChemistryUtil.instance
	def replicator = AcReplicator.instance
	def ChemistryCommonService chemistryCommonService
	  
	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','name','timeCreated','speciesSet']
		super.list(max)
	}

	def create() {
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()
		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'

		def acSpeciesSets = AcSpeciesSet.listWithParamsAndProjections(params)
		def acInteractionSeries = AcInteractionSeries.listWithParamsAndProjections(params)

		def actionSeriesInstance = new AcInteractionSeries(params)

		[instance: actionSeriesInstance, acSpeciesSets : acSpeciesSets, acInteractionSeries : acInteractionSeries]
	}

    def save() {
		def acInteractionSeriesInstance = new AcInteractionSeries(params)
		if (params["speciesSet.id"]) {
			AcSpeciesSet speciesSet = AcSpeciesSet.get(params.speciesSet.id)
			acInteractionSeriesInstance.speciesSet = speciesSet
		}
		acInteractionSeriesInstance.createdBy = currentUserOrError

		def interaction = new AcInteraction()
		interaction.startTime = 0
		interaction.timeLength = 0
		interaction.alternationType = StateAlternationType.Replacement
		acInteractionSeriesInstance.addAction(interaction)

        if (!acInteractionSeriesInstance.save(flush: true)) {
			if (!isAdmin())
				params.createdBy = currentUserOrError
			params.projections = ['id','name']
			params.sort = 'id'
			params.order = 'desc'

			def acSpeciesSets = AcSpeciesSet.listWithParamsAndProjections(params)
			def acInteractionSeries = AcInteractionSeries.listWithParamsAndProjections(params)
            render(view: "create", model: [instance: acInteractionSeriesInstance, acSpeciesSets : acSpeciesSets, acInteractionSeries : acInteractionSeries])
			return
		}

		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, acInteractionSeriesInstance.id])
		redirect(action: "show", id: acInteractionSeriesInstance.id)
    }

	def edit(Long id) {
		def result = super.edit(id)

		if (!isAdmin())
		params.createdBy = getCurrentUserOrError()
		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'

		def acSpeciesSets
		if (result.instance.actions.isEmpty()) {
			acSpeciesSets = AcSpeciesSet.listWithParamsAndProjections(params)
		} else
			acSpeciesSets = chemistryCommonService.getThisAndDerivedSpeciesSets(result.instance.speciesSet)

		def acInteractionSeries = AcInteractionSeries.listWithParamsAndProjections(params)
		result << [acSpeciesSets : acSpeciesSets, acInteractionSeries : acInteractionSeries]
	}

	def update(Long id, Long version) {
        def acInteractionSeriesInstance = getSafe(id)
		if (!acInteractionSeriesInstance) {
			handleObjectNotFound(id)
			return
		}

		if (version != null)
			if (acInteractionSeriesInstance.version > version) {
				setOlcFailureMessage(acInteractionSeriesInstance)
				render(view: "edit", model: [instance : acInteractionSeriesInstance])
				return
			}

		// handling of species set
		def oldAcSpeciesSetId = acInteractionSeriesInstance.speciesSet.id
		def newAcSpeciesSetId = params.speciesSet.id
		if (oldAcSpeciesSetId != newAcSpeciesSetId) {
			AcSpeciesSet newAcSpeciesSet = AcSpeciesSet.get(newAcSpeciesSetId)
			acInteractionSeriesInstance.speciesSet = newAcSpeciesSet
		}

		// parent interaction series
		def oldParentActionSeries = acInteractionSeriesInstance.parent
		def newParentActionSeriesId = params.parent.id
		if (oldParentActionSeries) {
			oldParentActionSeries = getSafe(oldParentActionSeries.id)
			if (oldParentActionSeries.id != newParentActionSeriesId) {
				oldParentActionSeries.removeSubActionSeries(acInteractionSeriesInstance)
			}
		}

        acInteractionSeriesInstance.properties = params

		if (!params.immutableSpecies) {
			acInteractionSeriesInstance.immutableSpecies = []
		}

		if (!acInteractionSeriesInstance.save(flush: true)) {
			render(view: "edit", model: [instance: acInteractionSeriesInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acInteractionSeriesInstance.id])
		redirect(action: "show", id: acInteractionSeriesInstance.id)
    }

	def replaceSpeciesSet(Long id, Long speciesSetId) {
		def interactionSeriesInstance = getSafe(id)
		def acSpeciesSetInstance = AcSpeciesSet.get(speciesSetId)

		acUtil.replaceSpeciesSet(interactionSeriesInstance, acSpeciesSetInstance)

		if (interactionSeriesInstance.save(flush: true)) {
			flash.message = "Species set of the interaction series " +  id + " replaced with " + speciesSetId + " ."
		}
		redirect(action: "show", id: id)
	}

	protected def copyInstance(instance) {
		if (params.recursively) {
			def actionSeriesCloneMap = cloneActionSeriesRecursively(instance)
            chemistryCommonService.saveActionSeriesRecursively(instance, actionSeriesCloneMap)
			actionSeriesCloneMap.get(instance)
		} else {
			def acInteractionSeriesCloneInstance = cloneActionSeries(instance)
            chemistryCommonService.saveActionSeries(instance, acInteractionSeriesCloneInstance)
			acInteractionSeriesCloneInstance
		}
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

	def getInteractionSeriesData() {
		def acInteractionSeriesData = [ : ]
		def acInteractionSeries = AcInteractionSeries.listWithProjections(['id', 'name'])
		acInteractionSeries.each{ acInteractionSeriesData.putAt(it.id, it.id + ' : ' + it.name) }
		render acInteractionSeriesData as JSON
	}

	def addInteractions(Long id, Long additionalInteractionSeriesId) {
		def override = params.override != null
		if (addInteractionsAux(id, additionalInteractionSeriesId, override)) {
			flash.message = "Interactions from " + additionalInteractionSeriesId + " added to " + id
		}
		redirect(action: "show", id: id)
	}

	def addInteractionsMultiple() {
		if (!params.ids) {
			flash.message = "No rows selected"
			redirect(action: "list")
			return
		}

		def actionSeriesIds = ParseUtil.parseArray(params.ids, Long.class, "Action series id", ",")
		Collections.sort(actionSeriesIds)

		def additionalInteractionSeriesId = params.long('additionalInteractionSeriesId')
		def override = params.override != null

		actionSeriesIds.each{
			addInteractionsAux(it, additionalInteractionSeriesId, override)}

		flash.message = "Interactions from " + additionalInteractionSeriesId + " added to " + actionSeriesIds

		redirect(action: "list")
	}

	private def cloneActionSeries(actionSeriesInstance) {
		def acInteractionSeriesInstanceClone = replicator.cloneActionSeriesWithActions(actionSeriesInstance)
		resetActionSeriesClone(acInteractionSeriesInstanceClone)
		acInteractionSeriesInstanceClone
	}

	private def cloneActionSeriesRecursively(actionSeriesInstance) {
		def actionSeriesCloneMap = [ : ]
		replicator.cloneActionSeriesWithActionsRecursively(actionSeriesInstance, actionSeriesCloneMap)
		actionSeriesCloneMap.values().each{resetActionSeriesClone(it)}
		actionSeriesCloneMap
	}

	private def resetActionSeriesClone(acInteractionSeriesInstanceClone) {
		def name = acInteractionSeriesInstanceClone.name + " copy"
		if (name.size() > 200) name = name.substring(0, 200)
		acInteractionSeriesInstanceClone.name = name

		acInteractionSeriesInstanceClone.timeCreated = new Date()
		acInteractionSeriesInstanceClone.createdBy = currentUserOrError
		replicator.nullIdAndVersionRecursively(acInteractionSeriesInstanceClone)
	}

	private def addInteractionsAux(Long id, Long additionalInteractionSeriesId, boolean override) {
		def interactionSeries = getSafe(id)
		def additionalInteractionSeries = getSafe(additionalInteractionSeriesId)

		def newVariables = acUtil.getNewInteractionVariables(interactionSeries, additionalInteractionSeries)
		if (!newVariables.isEmpty()) {
			interactionSeries.addVariables(newVariables)
			if (!interactionSeries.save(flush: true)) {
				return
			}
		}

		if (override) {		
			def itemsToRemove = acUtil.getSpeciesAndInteractionVariablesToRemove(interactionSeries, additionalInteractionSeries)

			itemsToRemove.getFirst().each{
				AcSpeciesInteraction.withTransaction{status ->
					it.action.removeFromSpeciesActions(it)
					it.delete()}
				}

			itemsToRemove.getSecond().each{
				AcInteractionVariableAssignment.withTransaction{ status ->
					it.action.removeVariableAssignment(it)
					it.delete()}
				}
		}

		acUtil.addInteractions(interactionSeries, additionalInteractionSeries)

		return interactionSeries.save(flush: true)
	}
}