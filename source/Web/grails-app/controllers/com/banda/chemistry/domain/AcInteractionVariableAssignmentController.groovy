package com.banda.chemistry.domain

import edu.banda.coel.web.BaseDomainController

import com.banda.chemistry.business.ArtificialChemistryUtil
import com.banda.chemistry.domain.AcInteraction
import com.banda.chemistry.domain.AcInteractionVariableAssignment
import com.banda.function.domain.Expression

import com.banda.function.BndFunctionException

class AcInteractionVariableAssignmentController extends BaseDomainController {

	def acUtil = ArtificialChemistryUtil.instance

	def index() {
		redirect(action: "list", params: params)
	}

	def create() {
		def instance =  new AcInteractionVariableAssignment()
		instance.properties = params

		if (params.'action.id') {
			def action = AcInteraction.get(params.'action.id')
			def interactionVariables = getAvailableVariables(action)
			if (interactionVariables.isEmpty()) {
				flash.message = "No interaction variables available."
				redirect(controller: "acInteractionSeries", action: "show", id: action.actionSeries.id)
				return
			} else {
				action.addVariableAssignment(instance)
				[instance: instance, interactionVariables : interactionVariables]
			}
		} else {
			flash.message = "Interaction expected for a new interaction variable assignment"
			redirect(controller: "acInteractionSeries", action: "list")
			return
		}
	}

	def save() {
		def acInteractionVariableAssignmentInstance = new AcInteractionVariableAssignment(params)

		// AC action
		def actionId = request.getParameter('action.id')
		AcInteraction action = AcInteraction.get(actionId)
		action.addVariableAssignment(acInteractionVariableAssignmentInstance)

		// Setting Function
		try {
			acUtil.setSettingFunctionFromString(params.settingFunction.formula, acInteractionVariableAssignmentInstance)
		} catch (BndFunctionException e) {
			acInteractionVariableAssignmentInstance.errors.rejectValue("settingFunction", "Setting Function '" + params.settingFunction.formula + "' is invalid. " + e.getMessage())
		}

		if (acInteractionVariableAssignmentInstance.hasErrors() || !acInteractionVariableAssignmentInstance.save(flush: true)) {
			def interactionVariables = getAvailableVariables(action)
			render(view: "create", model: [instance: acInteractionVariableAssignmentInstance, interactionVariables : interactionVariables])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, acInteractionVariableAssignmentInstance.id])
		redirect(controller: "acInteractionSeries", action: "show", id: action.actionSeries.id)
	}

	def edit(Long id) {
		def result = super.edit(id)
		def interactionVariables = getAvailableVariables(result.instance.action)
		interactionVariables += result.instance.variable
		result << [interactionVariables : interactionVariables]
	}

	def update(Long id, Long version) {
		def acInteractionVariableAssignmentInstance = getSafe(id)
		if (!acInteractionVariableAssignmentInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (acInteractionVariableAssignmentInstance.version > version) {
				setOlcFailureMessage(acInteractionVariableAssignmentInstance)
				def interactionVariables = getAvailableVariables(acInteractionVariableAssignmentInstance.action)
				interactionVariables += acInteractionVariableAssignmentInstance.variable
				render(view: "edit", model: [instance: acInteractionVariableAssignmentInstance, interactionVariables : interactionVariables])
				return
			}

		acInteractionVariableAssignmentInstance.properties = params

		// Setting Function
		try {
			acUtil.setSettingFunctionFromString(params.settingFunction.formula, acInteractionVariableAssignmentInstance)
		} catch (BndFunctionException e) {
			acInteractionVariableAssignmentInstance.errors.rejectValue("settingFunction", "Setting Function '" + params.settingFunction.formula + "' is invalid. " + e.getMessage())
		}

		if (acInteractionVariableAssignmentInstance.hasErrors() || !updateSafe(acInteractionVariableAssignmentInstance)) {
			def interactionVariables = getAvailableVariables(acInteractionVariableAssignmentInstance.action)
			interactionVariables += acInteractionVariableAssignmentInstance.variable
			render(view: "edit", model: [instance: acInteractionVariableAssignmentInstance, interactionVariables : interactionVariables])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acInteractionVariableAssignmentInstance.id])
		redirect(controller: "acInteractionSeries", action: "show", id: acInteractionVariableAssignmentInstance.action.actionSeries.id)
	}

	private def getAvailableVariables(action) {
		def variables = action.actionSeries.variables.sort{a,b -> a.label <=> b.label}
		def voidVariable = variables.find{ it.label.equals("void") }
		def refVariables = action.variableAssignments*.variable
		variables.removeAll(refVariables)

		if (voidVariable && refVariables.contains(voidVariable))
			variables += voidVariable

		variables
	}

	protected deleteInstance(instance) {
		def action = instance.action
		AcInteractionVariableAssignment.withTransaction{ status ->
			action.removeVariableAssignment(instance)
			instance.delete(flush: true)
		}
	}

	protected def notFoundRedirect(id) {
		redirect(controller: "acInteractionSeries", action: "list")
	}

	protected def deleteSuccessfulRedirect(instance, owner) {
		redirect(controller: "acInteractionSeries", action: "show", id: owner.id)
	}

	protected def deleteMultiSuccessfulRedirect(instancesWithOwners) {
		redirect(controller: "acInteractionSeries", action: "show", id: instancesWithOwners.first().value.id)
	}

	protected def getOwner(instance) {
		instance.action.actionSeries
	}
}