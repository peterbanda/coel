package com.banda.chemistry.domain

import edu.banda.coel.web.BaseDomainController

import org.springframework.dao.DataIntegrityViolationException

class AcTranslationVariableController extends BaseDomainController {

	def create() {
		def instance =  new AcTranslationVariable()
		instance.properties = params

		if (params.parentSet.id) {
			def translationSeries = AcTranslationSeries.get(params.parentSet.id)
			translationSeries.addVariable(instance)
			[instance: instance]
		} else {
			flash.message = "Translation series expected for a new translation Variable"
			redirect(controller: "acTranslationSeries", action: "list")
			return
		}
	}

	def save() {
		def acTranslationVariableInstance = new AcTranslationVariable(params)
		AcTranslationSeries translationSeries = AcTranslationSeries.get(params.parentSet.id)
		translationSeries.addVariable(acTranslationVariableInstance)
		// TODO: Is sort order needed?
		acTranslationVariableInstance.sortOrder = acTranslationVariableInstance.variableIndex

		if (!acTranslationVariableInstance.save(flush: true)) {
			render(view: "create", model: [instance: acTranslationVariableInstance])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, acTranslationVariableInstance.id])
		redirect(controller: "acTranslationSeries", action: "show", id: translationSeries.id)
	}
	
	def update(Long id, Long version) {
		def acTranslationVariableInstance = getSafe(id)
		if (!acTranslationVariableInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (acTranslationVariableInstance.version > version) {
				setOlcFailureMessage(acTranslationVariableInstance)
				render(view: "edit", model: [instance: acTranslationVariableInstance])
				return
			}

		acTranslationVariableInstance.properties = params

		if (!acTranslationVariableInstance.save(flush: true)) {
			render(view: "edit", model: [instance: acTranslationVariableInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acTranslationVariableInstance.id])
		redirect(controller: "acTranslationSeries", action: "show", id: acTranslationVariableInstance.parentSet.id)
	}

	protected def deleteInstance(instance) {
		def translationSeries = instance.parentSet
		AcTranslationVariable.withTransaction{ status ->
			translationSeries.removeVariable(instance)
			instance.delete(flush: true)
		}
	}
 
	protected def notFoundRedirect(id) {
		redirect(controller: "acTranslationSeries", action: "list")
	}
 
	protected def deleteSuccessfulRedirect(instance, owner) {
		redirect(controller: "acTranslationSeries", action: "show", id: owner.id)
	}
 
	protected def deleteMultiSuccessfulRedirect(instancesWithOwners) {
		redirect(controller: "acTranslationSeries", action: "show", id: instancesWithOwners.first().value.id)
	}
 
	protected def getOwner(instance) {
		instance.parentSet
	}
}