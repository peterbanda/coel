package com.banda.chemistry.domain

import edu.banda.coel.web.BaseDomainController
import grails.converters.JSON
import org.apache.commons.lang.StringUtils

class AcInteractionVariableController extends BaseDomainController {

	def saveAjax() {
		AcInteractionSeries actionSeries = AcInteractionSeries.get(params.parentSet.id)

		def labels = StringUtils.split(params.labels, ', ')
		def ids = []
		def acInteractionVariableInstances = []
		labels.each{ label ->
			def acInteractionVariableInstance = new AcInteractionVariable()
			acInteractionVariableInstance.label = label.trim()
			actionSeries.addVariable(acInteractionVariableInstance)
			// TODO: Is sort order needed?
			acInteractionVariableInstance.sortOrder = acInteractionVariableInstance.variableIndex

			if (!acInteractionVariableInstance.save(flush: true)) {
				render(view: "create", model: [instance: acInteractionVariableInstance])
				return
			}
			ids.add(acInteractionVariableInstance.id)
			acInteractionVariableInstances.add(acInteractionVariableInstance)
		}

		def message = message(code: 'default.created.message', args: [doClazzMessageLabel, ids])
		render ([message: message, acInteractionVariableInstances : acInteractionVariableInstances] as JSON)
	}

	def update(Long id, Long version) {
		def acInteractionVariableInstance = getSafe(id)
		if (!acInteractionVariableInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (acInteractionVariableInstance.version > version) {
				setOlcFailureMessage(acInteractionVariableInstance)
				render(view: "edit", model: [instance: acInteractionVariableInstance])
				return
			}

		acInteractionVariableInstance.properties = params
	
		if (!acInteractionVariableInstance.save(flush: true)) {
			render(view: "edit", model: [instance: acInteractionVariableInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acInteractionVariableInstance.id])
		redirect(controller: "acInteractionSeries", action: "show", id: acInteractionVariableInstance.parentSet.id)
	}

	protected def deleteInstance(instance) {
		def actionSeries = instance.parentSet
		AcInteractionVariable.withTransaction{ status ->
			actionSeries.removeVariable(instance)
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
		instance.parentSet
	}
}