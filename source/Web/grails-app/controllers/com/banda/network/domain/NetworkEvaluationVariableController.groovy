package com.banda.network.domain

import edu.banda.coel.web.BaseDomainController
import org.springframework.dao.DataIntegrityViolationException

class NetworkEvaluationVariableController extends BaseDomainController {

	def create() {
		def instance =  new NetworkEvaluationVariable()
		instance.properties = params

		if (params.parentSet.id) {
			def evaluation = NetworkEvaluation.get(params.parentSet.id)
			evaluation.addVariable(instance)
			[instance: instance]
		} else {
			flash.message = "Network evaluation expected for a new evaluation Variable"
			redirect(controller: "networkEvaluation", action: "list")
			return
		}
	}

	def save() {
		def networkEvaluationVariableInstance = new NetworkEvaluationVariable(params)
		def evaluation = NetworkEvaluation.get(params.parentSet.id)
		evaluation.addVariable(networkEvaluationVariableInstance)

		if (!networkEvaluationVariableInstance.save(flush: true)) {
			render(view: "create", model: [instance: networkEvaluationVariableInstance])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [message(code: 'networkEvaluationVariable.label', default: 'Network Evaluation Variable'), networkEvaluationVariableInstance.id])
		redirect(controller: "networkEvaluation", action: "show", id: evaluation.id)
	}
	
	def update(Long id, Long version) {
		def networkEvaluationVariableInstance = getSafe(id)
		if (!networkEvaluationVariableInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'networkEvaluationVariable.label', default: 'Network Evaluation Variable'), id])
			redirect(action: "list")
			return
		}
	
		if (version != null) {
			if (networkEvaluationVariableInstance.version > version) {
				networkEvaluationVariableInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: 'networkEvaluationVariable.label', default: 'NetworkEvaluationVariable')] as Object[],
						  "Another user has updated this NetworkEvaluationVariable while you were editing")
					render(view: "edit", model: [instance: networkEvaluationVariableInstance])
					return
			}
		}

		networkEvaluationVariableInstance.properties = params

		if (!networkEvaluationVariableInstance.save(flush: true)) {
			render(view: "edit", model: [instance: networkEvaluationVariableInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [message(code: 'networkEvaluationVariable.label', default: 'Network Evaluation Variable'), networkEvaluationVariableInstance.id])
		redirect(controller: "networkEvaluation", action: "show", id: networkEvaluationVariableInstance.parentSet.id)
	}

	def delete(Long id) {
		def networkEvaluationVariableInstance = getSafe(id)
		if (!networkEvaluationVariableInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'networkEvaluationVariable.label', default: 'NetworkEvaluationVariable'), id])
			redirect(action: "list")
			return
		}
	
		try {
			def evaluation = networkEvaluationVariableInstance.parentSet
			NetworkEvaluationVariable.withTransaction{ status ->
				evaluation.removeVariable(networkEvaluationVariableInstance)
				networkEvaluationVariableInstance.delete(flush: true)
			}
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'networkEvaluationVariable.label', default: 'NetworkEvaluationVariable'), id])
			redirect(controller: "networkEvaluation", action: "show", id: evaluation.id)
		} catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'networkEvaluationVariable.label', default: 'NetworkEvaluationVariable'), id])
			redirect(action: "show", id: id)
		}
	}
}