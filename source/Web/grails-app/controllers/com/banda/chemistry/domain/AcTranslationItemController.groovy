package com.banda.chemistry.domain

import edu.banda.coel.web.BaseDomainController

import com.banda.chemistry.business.ArtificialChemistryUtil
import com.banda.chemistry.domain.AcTranslation
import com.banda.chemistry.domain.AcTranslationItem
import com.banda.chemistry.domain.AcTranslationVariable
import com.banda.function.BndFunctionException

class AcTranslationItemController extends BaseDomainController {

	def acUtil = ArtificialChemistryUtil.instance

	def create() {
		def instance =  new AcTranslationItem()
		instance.properties = params

		if (params.'translation.id') {
			def rangeTranslation = AcTranslation.get(params.'translation.id')
			rangeTranslation.addTranslationItem(instance)
			[instance: instance]
		} else {
			flash.message = " translation expected for a new translation item"
			redirect(controller: "acTranslationSeries", action: "list")
			return
		}
	}

	def save() {
		// AC translation reference
		def translationId = request.getParameter('translation.id')
		AcTranslation rangeTranslation = AcTranslation.get(translationId)
		def translationSeries = rangeTranslation.translationSeries

		if (!params.variableLabel)
			params.variableLabel = params.translationFunction.formula

		def variable = translationSeries.variables.find { it.label.equals(params.variableLabel) }

		if (!variable) {
			// create a new translation variable
			variable = new AcTranslationVariable()
			if (params.variableLabel)
				variable.label = params.variableLabel
			else
				variable.label = params.translationFunction.formula
			// TODO: Is sort order needed?
			variable.sortOrder = variable.variableIndex
			rangeTranslation.translationSeries.addVariable(variable)
			if (!variable.save(flush: true)) {
				redirect(action: "create")
				return
			}
		}

		def acTranslationItemInstance = new AcTranslationItem(params)
		acTranslationItemInstance.variable = variable
		rangeTranslation.addTranslationItem(acTranslationItemInstance)

		// Translation Function		
		try {
			acUtil.setTranslationFunctionFromString(params.translationFunction.formula, acTranslationItemInstance)
		} catch (BndFunctionException e) {
			acTranslationItemInstance.errors.rejectValue("translationFunction", "Translation Function '" + params.translationFunction.formula + "' is invalid. " + e.getMessage())
		}

		if (acTranslationItemInstance.hasErrors() || !acTranslationItemInstance.save(flush: true)) {
			render(view: "create", model: [instance: acTranslationItemInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, acTranslationItemInstance.id])
		redirect(controller: "acTranslation", action: "show", id: rangeTranslation.id)
	}
	
	def update(Long id, Long version) {
		def acTranslationItemInstance = getSafe(id)
		if (!acTranslationItemInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (acTranslationItemInstance.version > version) {
				setOlcFailureMessage(acTranslationItemInstance)
				render(view: "edit", model: [instance: acTranslationItemInstance])
				return
			}

		acTranslationItemInstance.properties = params

		acTranslationItemInstance.variable.label = params.variableLabel

		// Translation Function		
		try {
			acUtil.setTranslationFunctionFromString(params.translationFunction.formula, acTranslationItemInstance)
		} catch (BndFunctionException e) {
			acTranslationItemInstance.errors.rejectValue("translationFunction", "Translation Function '" + params.translationFunction.formula + "' is invalid. " + e.getMessage())
		}

		if (acTranslationItemInstance.hasErrors() || !acTranslationItemInstance.save(flush: true)) {
			render(view: "edit", model: [instance: acTranslationItemInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acTranslationItemInstance.id])
		redirect(controller: "acTranslation", action: "show", id: acTranslationItemInstance.translation.id)
	}

	protected def deleteInstance(instance) {
		def acTranslationVariableInstance = instance.variable
		def translation = instance.translation
		def translationSeries = translation.translationSeries

		AcTranslationItem.withTransaction{ status ->
			translation.removeTranslationItem(instance)
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
		instance.translation.translationSeries
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

	def getTranslationVariable(Long id) {
		def selectedTranslation = AcTranslation.get(id)
		if (selectedTranslation) {
			render (template:"selectTranslationVariable", model: ["translationVariables": selectedTranslation.translationSeries.variables])
		}
	}
}