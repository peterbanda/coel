package com.banda.network.domain

import com.banda.function.BndFunctionException
import com.banda.function.business.FunctionUtility
import edu.banda.coel.web.BaseDomainController
import org.springframework.dao.DataIntegrityViolationException

class NetworkEvaluationItemController extends BaseDomainController {

	def functionUtility = new FunctionUtility()

	def create() {
		def instance =  new NetworkEvaluationItem()
		instance.properties = params

		if (params.'evaluation.id') {
			def evaluation = NetworkEvaluation.get(params.'evaluation.id')
			evaluation.addEvaluationItem(instance)
			[instance: instance]
		} else {
			flash.message = "Network evaluation expected for a new evaluation item"
			redirect(controller: "networkEvaluation", action: "list")
			return
		}
	}

	def save() {
		// AC evaluation reference
		def evaluationId = request.getParameter('evaluation.id')
		NetworkEvaluation evaluation = NetworkEvaluation.get(evaluationId)

		def variable = evaluation.variables.find { it.label.equals(params.variableLabel) }

		if (!variable) {
			// create a new evaluation variable
			variable = new NetworkEvaluationVariable()
			variable.label = params.variableLabel
			evaluation.addVariable(variable)
			if (!variable.save(flush: true)) {
				redirect(action: "create")
				return
			}
		}

		def networkEvaluationItemInstance = new NetworkEvaluationItem(params)
		networkEvaluationItemInstance.variable = variable
		evaluation.addEvaluationItem(networkEvaluationItemInstance)

		// Evaluation Function		
		try {
			setEvaluationFunctionFromString(params.evalFunction.formula, networkEvaluationItemInstance)
		} catch (BndFunctionException e) {
			networkEvaluationItemInstance.errors.rejectValue("evalFunction", "Evaluation function '" + params.evalFunction.formula + "' is invalid. " + e.getMessage())
		}

		if (networkEvaluationItemInstance.hasErrors() || !networkEvaluationItemInstance.save(flush: true)) {
			render(view: "create", model: [instance: networkEvaluationItemInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'networkEvaluationItem.label', default: 'Network Evaluation Item'), networkEvaluationItemInstance.id])
		redirect(controller: "networkEvaluation", action: "show", id: evaluation.id)
	}
	
	def update(Long id, Long version) {
		def networkEvaluationItemInstance = getSafe(id)
		if (!networkEvaluationItemInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'networkEvaluationItem.label', default: 'Network Evaluation Item'), id])
			redirect(action: "list")
			return
		}
	
		if (version != null) {
			if (networkEvaluationItemInstance.version > version) {
				networkEvaluationItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: 'networkEvaluationItem.label', default: 'NetworkEvaluationItem')] as Object[],
						  "Another user has updated this NetworkEvaluationItem while you were editing")
					render(view: "edit", model: [networkEvaluationItemInstance: networkEvaluationItemInstance])
					return
			}
		}

		networkEvaluationItemInstance.properties = params

		networkEvaluationItemInstance.variable.label = params.variableLabel

		// Evaluation Function		
		try {
			setEvaluationFunctionFromString(params.evalFunction.formula, networkEvaluationItemInstance)
		} catch (BndFunctionException e) {
			networkEvaluationItemInstance.errors.rejectValue("evalFunction", "Evaluation function '" + params.evalFunction.formula + "' is invalid. " + e.getMessage())
		}

		if (networkEvaluationItemInstance.hasErrors() || !networkEvaluationItemInstance.save(flush: true)) {
			render(view: "edit", model: [instance: networkEvaluationItemInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [message(code: 'networkEvaluationItem.label', default: 'NetworkEvaluationItem'), networkEvaluationItemInstance.id])
		redirect(controller: "networkEvaluation", action: "show", id: networkEvaluationItemInstance.evaluation.id)
	}

	def delete(Long id) {
		def networkEvaluationItemInstance = getSafe(id)
		if (!networkEvaluationItemInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'networkEvaluationItem.label', default: 'NetworkEvaluationItem'), id])
			redirect(action: "list")
			return
		}
	
		try {
			def variable = networkEvaluationItemInstance.variable
			def evaluation = networkEvaluationItemInstance.evaluation

			NetworkEvaluationItem.withTransaction{ status ->
				evaluation.removeEvaluationItem(networkEvaluationItemInstance)
				networkEvaluationItemInstance.delete(flush: true)
			}

			NetworkEvaluationVariable.withTransaction{ status ->
				evaluation.removeVariable(variable)
				variable.delete(flush: true)
			}

			flash.message = message(code: 'default.deleted.message', args: [message(code: 'networkEvaluationItem.label', default: 'NetworkEvaluationItem'), id])
			redirect(controller: "networkEvaluation", action: "show", id: evaluation.id)
		} catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'networkEvaluationItem.label', default: 'NetworkEvaluationItem'), id])
			redirect(action: "show", id: id)
		}
	}

	def getEvaluationVariable(Long id) {
		def evaluation = NetworkEvaluation.get(id)
		if (evaluation) {
			render (template:"selectEvaluationVariable", model: ["evaluationVariables": evaluation.variables])
		}
	}

	def setEvaluationFunctionFromString(String formula, NetworkEvaluationItem evaluationItem) {
		def variableIndexLabelMap = [0 : 'firstState', 1 : 'lastButOneState', 2 : 'lastState']
		functionUtility.validateExpression(formula, variableIndexLabelMap.values())
		String convertedFormula = functionUtility.getFormulaVariableNamesReplacedWithIndexPlaceholders(formula, variableIndexLabelMap.keySet(), variableIndexLabelMap)
		functionUtility.setExpressionFunctionFromString(convertedFormula, evaluationItem)
	}
}