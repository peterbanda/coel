package com.banda.chemistry.domain

import edu.banda.coel.web.BaseDomainController

import com.banda.chemistry.business.AcReplicator

import com.banda.chemistry.domain.AcSpeciesSet
import com.banda.chemistry.domain.AcTranslationSeries
import com.banda.chemistry.domain.ArtificialChemistry

class AcTranslationSeriesController extends BaseDomainController {

	def replicator = AcReplicator.instance

	def list(Integer max) {
		params.projections = ['id','name','timeCreated','createdBy','speciesSet']
		super.list(max)
	}

	def index() {
		redirect(action: "list", params: params)
	}

	def create() {
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'

		def acSpeciesSets = AcSpeciesSet.listWithParamsAndProjections(params)
		[instance: new AcTranslationSeries(params), acSpeciesSets : acSpeciesSets]
	}
	
	def save() {
		def acTranslationSeriesInstance = new AcTranslationSeries(params)
		acTranslationSeriesInstance.createdBy = currentUserOrError

		// AC Species Set
		AcSpeciesSet newSpeciesSet = AcSpeciesSet.get(params.speciesSet.id)
		acTranslationSeriesInstance.speciesSet = newSpeciesSet

		if (!acTranslationSeriesInstance.save(flush: true)) {
			if (!isAdmin())
				params.createdBy = getCurrentUserOrError()

			params.projections = ['id','name']
			params.sort = 'id'
			params.order = 'desc'

			def acSpeciesSets = AcSpeciesSet.listWithParamsAndProjections(params)
			render(view: "create", model: [instance: acTranslationSeriesInstance, acSpeciesSets : acSpeciesSets])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, acTranslationSeriesInstance.id])
		redirect(action: "show", id: acTranslationSeriesInstance.id)
	}

	def edit(Long id) {
		def result = super.edit(id)

		def acSpeciesSets
		if (result.instance.translations.isEmpty()) {
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
		def acTranslationSeriesInstance = getSafe(id)
		if (!acTranslationSeriesInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (acTranslationSeriesInstance.version > version) {
				setOlcFailureMessage(acTranslationSeriesInstance)
				render(view: "edit", model: [instance: acTranslationSeriesInstance])
				return
			}

		// Species set
		def oldSpeciesSetId = acTranslationSeriesInstance.speciesSet.id
		def newSpeciesSetId = params.speciesSet.id
		if (oldSpeciesSetId != newSpeciesSetId) {
			AcSpeciesSet newSpeciesSet = AcSpeciesSet.get(newSpeciesSetId)
			acTranslationSeriesInstance.speciesSet = newSpeciesSet
		}

		acTranslationSeriesInstance.properties = params
	
		if (!acTranslationSeriesInstance.save(flush: true)) {
			render(view: "edit", model: [instance: acTranslationSeriesInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acTranslationSeriesInstance.id])
		redirect(action: "show", id: acTranslationSeriesInstance.id)
	}

	protected def copyInstance(instance) {
		def newInstance = replicator.cloneTranslationSeriesWithTranslations(instance)
		replicator.nullIdAndVersion(newInstance)

		def name = newInstance.name + " copy"
		if (name.size() > 50) name = name.substring(0, 50)
		newInstance.name = name
		newInstance.timeCreated = new Date()
		newInstance.createdBy = currentUserOrError

		newInstance.save(flush: true)
		newInstance
	}

	protected def deleteInstance(instance) {
		AcTranslationSeries.withTransaction{ status ->
			def rangeTranslations = []
			instance.translations.each { it ->
				rangeTranslations.add(it)
			}
			rangeTranslations.each { it ->
				instance.removeTranslation(it)
				it.delete(flush : true)
			}
			instance.delete(flush: true)
		}
	}
}