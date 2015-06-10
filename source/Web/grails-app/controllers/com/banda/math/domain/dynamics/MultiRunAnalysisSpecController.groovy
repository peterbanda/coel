package com.banda.math.domain.dynamics

import com.banda.chemistry.business.AcReplicator
import edu.banda.coel.web.BaseDomainController
import org.springframework.dao.DataIntegrityViolationException

class MultiRunAnalysisSpecController extends BaseDomainController {

	def replicator = AcReplicator.instance
	
	def index() {
		redirect(action: "list", params: params)
	}

	def create() {
		[instance: new MultiRunAnalysisSpec(params)]
	}

	def save() {
		def multiRunAnalysisSpecInstance = new MultiRunAnalysisSpec(params)
		multiRunAnalysisSpecInstance.initialStateDistribution = bindRandomDistribution('initialState')
		multiRunAnalysisSpecInstance.createdBy = currentUserOrError

		if (!multiRunAnalysisSpecInstance.save(flush: true)) {
			render(view: "create", model: [instance: multiRunAnalysisSpecInstance])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [message(code: 'multiRunAnalysisSpec.label', default: 'Multi Run Analysis Spec'), multiRunAnalysisSpecInstance.id])
		redirect(action: "show", id: multiRunAnalysisSpecInstance.id)
	}
	
	def update(Long id, Long version) {
		def multiRunAnalysisSpecInstance = MultiRunAnalysisSpec.get(id)
		if (!multiRunAnalysisSpecInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'multiRunAnalysisSpec.label', default: 'Multi Run Analysis Spec'), id])
			redirect(action: "list")
			return
		}
	
		if (version != null) {
			if (multiRunAnalysisSpecInstance.version > version) {
				multiRunAnalysisSpecInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: 'multiRunAnalysisSpec.label', default: 'Multi Run Analysis Spec')] as Object[],
						  "Another user has updated this MultiRunAnalysisSpec while you were editing")
					render(view: "edit", model: [instance: multiRunAnalysisSpecInstance])
					return
			}
		}

		multiRunAnalysisSpecInstance.properties = params
		def newInitialStateDistribution = bindRandomDistribution('initialState')
		if (!isRandomDistributionEqual(multiRunAnalysisSpecInstance.initialStateDistribution, newInitialStateDistribution)) {
			multiRunAnalysisSpecInstance.initialStateDistribution = newInitialStateDistribution
		}

		if (!multiRunAnalysisSpecInstance.save(flush: true)) {
			render(view: "edit", model: [instance: multiRunAnalysisSpecInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [message(code: 'multiRunAnalysisSpec.label', default: 'Multi Run Analysis Spec'), multiRunAnalysisSpecInstance.id])
		redirect(action: "show", id: multiRunAnalysisSpecInstance.id)
	}

	def delete(Long id) {
		def multiRunAnalysisSpecInstance = MultiRunAnalysisSpec.get(id)
		if (!multiRunAnalysisSpecInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'multiRunAnalysisSpec.label', default: 'Multi Run Analysis Spec'), id])
			redirect(action: "list")
			return
		}
	
		try {
			multiRunAnalysisSpecInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'multiRunAnalysisSpec.label', default: 'Multi Run Analysis Spec'), id])
			redirect(action: "list")
		} catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'multiRunAnalysisSpec.label', default: 'Multi Run Analysis Spec'), id])
			redirect(action: "show", id: id)
		}
	}

	def copy(Long id) {
		def multiRunAnalysisSpecInstance = MultiRunAnalysisSpec.get(id)

		def multiRunAnalysisSpecInstanceClone = replicator.cloneMultiRunAnalysisSpec(multiRunAnalysisSpecInstance)
		multiRunAnalysisSpecInstanceClone.timeCreated = new Date()
		if (multiRunAnalysisSpecInstanceClone.save(flush: true)) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'multiRunAnalysisSpec.label', default: 'Multi Run Analysis Spec'), id])

		}
		redirect(action: "list")
	}
}