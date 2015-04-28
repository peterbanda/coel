package com.banda.math.domain.dynamics

import edu.banda.coel.web.BaseDomainController
import com.banda.chemistry.business.AcReplicator

import org.springframework.dao.DataIntegrityViolationException
import java.util.Date

class SingleRunAnalysisSpecController extends BaseDomainController {

	def replicator = AcReplicator.instance

	def index() {
		redirect(action: "list", params: params)
	}

	def create() {
		[instance: new SingleRunAnalysisSpec(params)]
	}

	def save() {
		def singleRunAnalysisSpecInstance = new SingleRunAnalysisSpec(params)
		singleRunAnalysisSpecInstance.createdBy = currentUserOrError
		if (!singleRunAnalysisSpecInstance.save(flush: true)) {
			render(view: "create", model: [instance: singleRunAnalysisSpecInstance])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [message(code: 'singleRunAnalysisSpec.label', default: 'Single Run Analysis Spec'), singleRunAnalysisSpecInstance.id])
		redirect(action: "show", id: singleRunAnalysisSpecInstance.id)
	}
	
	def update(Long id, Long version) {
		def singleRunAnalysisSpecInstance = SingleRunAnalysisSpec.get(id)
		if (!singleRunAnalysisSpecInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'singleRunAnalysisSpec.label', default: 'Single Run Analysis Spec'), id])
			redirect(action: "list")
			return
		}
	
		if (version != null) {
			if (singleRunAnalysisSpecInstance.version > version) {
				singleRunAnalysisSpecInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: 'singleRunAnalysisSpec.label', default: 'Single Run Analysis Spec')] as Object[],
						  "Another user has updated this SingleRunAnalysisSpec while you were editing")
					render(view: "edit", model: [instance: singleRunAnalysisSpecInstance])
					return
			}
		}

		singleRunAnalysisSpecInstance.properties = params
	
		if (!singleRunAnalysisSpecInstance.save(flush: true)) {
			render(view: "edit", model: [instance: singleRunAnalysisSpecInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [message(code: 'singleRunAnalysisSpec.label', default: 'Single Run Analysis Spec'), singleRunAnalysisSpecInstance.id])
		redirect(action: "show", id: singleRunAnalysisSpecInstance.id)
	}

	def delete(Long id) {
		def singleRunAnalysisSpecInstance = SingleRunAnalysisSpec.get(id)
		if (!singleRunAnalysisSpecInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'singleRunAnalysisSpec.label', default: 'Single Run Analysis Spec'), id])
			redirect(action: "list")
			return
		}
	
		try {
			SingleRunAnalysisSpec.withTransaction{ status ->
				singleRunAnalysisSpecInstance.delete(flush: true)
			}
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'singleRunAnalysisSpec.label', default: 'Single Run Analysis Spec'), id])
			redirect(action: "list")
		} catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'singleRunAnalysisSpec.label', default: 'Single Run Analysis Spec'), id])
			redirect(action: "show", id: id)
		}
	}

	def copy(Long id) {
		def singleRunAnalysisSpecInstance = SingleRunAnalysisSpec.get(id)

		def singleRunAnalysisSpecInstanceClone = replicator.cloneSingleRunAnalysisSpec(singleRunAnalysisSpecInstance)
		singleRunAnalysisSpecInstanceClone.timeCreated = new Date()
		if (singleRunAnalysisSpecInstanceClone.save(flush: true)) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'singleRunAnalysisSpec.label', default: 'Single Run Analysis Spec'), id])

		}
		redirect(action: "list")
	}
}