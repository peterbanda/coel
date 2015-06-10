package com.banda.chemistry.domain

import com.banda.chemistry.business.ArtificialChemistryUtil
import edu.banda.coel.web.BaseDomainController

class AcTranslationController extends BaseDomainController {

	def acUtil = ArtificialChemistryUtil.instance

	def create() {
		def instance =  new AcTranslation()
		instance.properties = params
		if (params.translationSeries.id) {
			def translationSeries = AcTranslationSeries.get(params.translationSeries.id)
			translationSeries.addTranslation(instance)
			[instance: instance]
		} else {
			flash.message = "Translation series expected for a new translation"
			redirect(controller: "acTranslationSeries", action: "list")
			return
		}
	}

	def save() {
		def acTranslationInstance = new AcTranslation(params)
		AcTranslationSeries translationSeries = AcTranslationSeries.get(params.translationSeries.id)
		translationSeries.addTranslation(acTranslationInstance)

		if (!acTranslationInstance.save(flush: true)) {
			render(view: "create", model: [instance: acTranslationInstance])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, acTranslationInstance.id])
		redirect(action: "show", id: acTranslationInstance.id)
	}

	def update(Long id, Long version) {
		def acTranslationInstance = getSafe(id)
		if (!acTranslationInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (acTranslationInstance.version > version) {
				setOlcFailureMessage(acTranslationInstance)
				render(view: "edit", model: [instance: acTranslationInstance])
				return
			}

		acTranslationInstance.properties = params
	
		if (!acTranslationInstance.save(flush: true)) {
			render(view: "edit", model: [instance: acTranslationInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acTranslationInstance.id])
		redirect(controller: "acTranslationSeries", action: "show", id: acTranslationInstance.translationSeries.id)
	}

	protected def deleteInstance(instance) {
		def translationSeries = instance.translationSeries
		AcTranslation.withTransaction{ status ->
			translationSeries.removeTranslation(instance)
			instance.delete(flush: true)
		}
		def variables = []
		translationSeries.variables.each {variables.add(it)}
		removeUnrefVariables(variables, translationSeries)
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
		instance.translationSeries
	}

	private def removeUnrefVariables(variables, translationSeries) {
		def refVariables = translationSeries.translations*.translationItems*.variable.flatten()

		if (translationSeries.variablesReferenced) {
			def refVariables2 = translationSeries.translations*.translationItems.flatten().collect {
				acUtil.getTranslationFunctionReferencedVariables(it)
			}.flatten()
			refVariables.addAll(refVariables2)
		}

		variables.removeAll(refVariables)
		AcTranslationVariable.withTransaction{ status ->
			variables.each { variable ->
				translationSeries.removeVariable(variable)
				variable.delete(flush: true)
			}
		}
	}
}