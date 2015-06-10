package com.banda.chemistry.domain

import com.banda.chemistry.business.ArtificialChemistryUtil
import com.banda.function.BndFunctionException
import edu.banda.coel.web.BaseDomainController

class AcSpeciesInteractionController extends BaseDomainController {

	def acUtil = ArtificialChemistryUtil.instance

	def create() {
		def instance =  new AcSpeciesInteraction()
		instance.properties = params

		if (params.'action.id') {
			def action = AcInteraction.get(params.'action.id')
			def species = getAvailableSpecies(action)
			if (species.isEmpty()) {
				flash.message = "No species available."
				redirect(controller: "acInteractionSeries", action: "show", id: action.actionSeries.id)
				return
			} else {
				action.addToSpeciesActions(instance)
				[instance: instance, species : species]
			}
		} else {
			flash.message = "Interaction expected for a new species interaction"
			redirect(controller: "acInteractionSeries", action: "list")
			return
		}
	}

	def getAvailableSpecies(action) {
		def species = action.species.sort{a,b -> a.label <=> b.label}
		def refSpecies = action.speciesActions*.species
		species.removeAll(refSpecies)
		species
	}

	def save() {
		def acSpeciesInteractionInstance = new AcSpeciesInteraction(params)

		// AC action
		def actionId = request.getParameter('action.id')
		AcInteraction action = AcInteraction.get(actionId)
		action.addToSpeciesActions(acSpeciesInteractionInstance)

		// Setting Function
		try {
			acUtil.setSettingFunctionFromString(params.settingFunction.formula, acSpeciesInteractionInstance)
		} catch (BndFunctionException e) {
			acSpeciesInteractionInstance.errors.rejectValue("settingFunction", "Setting Function '" + params.settingFunction.formula + "' is invalid. " + e.getMessage())
		}

		if (acSpeciesInteractionInstance.hasErrors() ||  !acSpeciesInteractionInstance.save(flush: true)) {
			def species = getAvailableSpecies(action)
			render(view: "create", model: [instance: acSpeciesInteractionInstance, species : species])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, acSpeciesInteractionInstance.id])
		redirect(controller: "acInteractionSeries", action: "show", id: action.actionSeries.id)
	}

	def edit(Long id) {
		def result = super.edit(id)
		def species = getAvailableSpecies(result.instance.action)
		species += result.instance.species
		result << [species : species]
	}

	def update(Long id, Long version) {
		def acSpeciesInteractionInstance = getSafe(id)
		if (!acSpeciesInteractionInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (acSpeciesInteractionInstance.version > version) {
				setOlcFailureMessage(acSpeciesInteractionInstance)
				render(view: "edit", model: [instance: acSpeciesInteractionInstance])
				return
			}

		acSpeciesInteractionInstance.properties = params

		// Setting Function
		try {
			acUtil.setSettingFunctionFromString(params.settingFunction.formula, acSpeciesInteractionInstance)
		} catch (BndFunctionException e) {
			acSpeciesInteractionInstance.errors.rejectValue("settingFunction", "Setting Function '" + params.settingFunction.formula + "' is invalid. " + e.getMessage())
		}

		if (acSpeciesInteractionInstance.hasErrors() || !acSpeciesInteractionInstance.save(flush: true)) {
			render(view: "edit", model: [instance: acSpeciesInteractionInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acSpeciesInteractionInstance.id])
		redirect(controller: "acInteractionSeries", action: "show", id: acSpeciesInteractionInstance.action.actionSeries.id)
	}

	protected def deleteInstance(instance) {
		def action = instance.action
		AcSpeciesInteraction.withTransaction{ status ->
			action.removeFromSpeciesActions(instance)
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

	def getSpecies(Long id) {
		def selectedAction = AcInteraction.get(id)
		if (selectedAction) {
			render (template:"selectSpecies", model: ["species": selectedAction.species])
		}
	}
}