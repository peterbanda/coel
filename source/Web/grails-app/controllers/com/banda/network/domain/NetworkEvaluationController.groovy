package com.banda.network.domain

import com.banda.function.BndFunctionException
import com.banda.function.business.FunctionUtility
import edu.banda.coel.web.BaseDomainController
import org.springframework.dao.DataIntegrityViolationException

class NetworkEvaluationController extends BaseDomainController {

	def functionUtility = new FunctionUtility()

	def list(Integer max) {
		params.projections = ['id','name','timeCreated','createdBy']
		super.list(max)
	}

	def index() {
		redirect(action: "list", params: params)
	}

	def create() {
		[instance: new NetworkEvaluation(params)]
	}

	def save() {
		def networkEvaluationInstance = new NetworkEvaluation(params)
		networkEvaluationInstance.createdBy = currentUserOrError

		// Evaluation Function
		try {
			setEvaluationFunctionFromString(params.evalFunction.formula, networkEvaluationInstance)
		} catch (BndFunctionException e) {
			networkEvaluationInstance.errors.rejectValue("evalFunction", "Evaluation function '" + params.evalFunction.formula + "' is invalid. " + e.getMessage())
		}

		if (networkEvaluationInstance.hasErrors() || !networkEvaluationInstance.save(flush: true)) {
			render(view: "create", model: [instance: networkEvaluationInstance])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [message(code: 'networkEvaluation.label', default: 'Network Evaluation'), networkEvaluationInstance.id])
		redirect(action: "show", id: networkEvaluationInstance.id)
	}
	
	def update(Long id, Long version) {
		def networkEvaluationInstance = getSafe(id)
		if (!networkEvaluationInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'networkEvaluation.label', default: 'Network Evaluation'), id])
			redirect(action: "list")
			return
		}
	
		if (version != null) {
			if (networkEvaluationInstance.version > version) {
				networkEvaluationInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: 'networkEvaluation.label', default: 'Network Evaluation')] as Object[],
						  "Another user has updated this NetworkEvaluation while you were editing")
					render(view: "edit", model: [instance: networkEvaluationInstance])
					return
			}
		}

		networkEvaluationInstance.properties = params

		// Evaluation Function
		try {
			setEvaluationFunctionFromString(params.evalFunction.formula, networkEvaluationInstance)
		} catch (BndFunctionException e) {
			networkEvaluationInstance.errors.rejectValue("evalFunction", "Evaluation function '" + params.evalFunction.formula + "' is invalid. " + e.getMessage())
		}

		if (networkEvaluationInstance.hasErrors() || !networkEvaluationInstance.save(flush: true)) {
			render(view: "edit", model: [instance: networkEvaluationInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [message(code: 'networkEvaluation.label', default: 'NetworkEvaluation'), networkEvaluationInstance.id])
		redirect(action: "show", id: networkEvaluationInstance.id)
	}

	def delete(Long id) {
		def networkEvaluationInstance = getSafe(id)
		if (!networkEvaluationInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'networkEvaluation.label', default: 'Network Evaluation'), id])
			redirect(action: "list")
			return
		}
	
		try {
			NetworkEvaluation.withTransaction{ status ->
				networkEvaluationInstance.delete(flush: true)
			}
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'networkEvaluation.label', default: 'Network Evaluation'), id])
			redirect(action: "list")
		} catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'networkEvaluation.label', default: 'Network Evaluation'), id])
			redirect(action: "show", id: id)
		}
	}

	def setEvaluationFunctionFromString(String formula, NetworkEvaluation evaluation) {
		def variableIndexLabelMap = [0 : 'firstState', 2 : 'lastButOneState', 4 : 'lastState']
		evaluation.variables.each{ variableIndexLabelMap.put(2 * it.variableIndex + 1, it.label) }
		functionUtility.validateExpression(formula, variableIndexLabelMap.values())
		String convertedFormula = functionUtility.getFormulaVariableNamesReplacedWithIndexPlaceholders(formula, variableIndexLabelMap.keySet(), variableIndexLabelMap)
		functionUtility.setExpressionFunctionFromString(convertedFormula, evaluation)
	}
}