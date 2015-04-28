package com.banda.chemistry.domain

import edu.banda.coel.web.BaseDomainController

import com.banda.chemistry.domain.AcInteraction;
import com.banda.chemistry.domain.AcInteractionSeries;

class AcInteractionController extends BaseDomainController {
	
	def create() {
		def instance =  new AcInteraction(params)

		if (params.actionSeries.id) {
			AcInteractionSeries actionSeries = AcInteractionSeries.get(params.actionSeries.id)
			actionSeries.addAction(instance)
			[instance: instance]
		} else {
			flash.message = "Interaction series expected for a new interaction"
			redirect(controller: "acInteractionSeries", action: "list")
			return
		}
	}

	def save() {
		def acInteractionInstance = new AcInteraction(params)
		AcInteractionSeries actionSeries = AcInteractionSeries.get(params.actionSeries.id)
		actionSeries.addAction(acInteractionInstance)

		if (!acInteractionInstance.save(flush: true)) {
			render(view: "create", model: [instance: acInteractionInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, acInteractionInstance.id])
		redirect(controller: "acInteractionSeries", action: "show", id: acInteractionInstance.actionSeries.id)
	}

	def update(Long id, Long version) {
        def acInteractionInstance = getSafe(id)
		if (!acInteractionInstance) {
			handleObjectNotFound(id)
			return
		}

		if (version != null)
			if (acInteractionInstance.version > version) {
				setOlcFailureMessage(acInteractionInstance)
				render(view: "edit", model: [instance: acInteractionInstance])
				return
			}

		acInteractionInstance.properties = params

		if (!acInteractionInstance.save(flush: true)) {
			render(view: "edit", model: [instance: acInteractionInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acInteractionInstance.id])
		redirect(controller: "acInteractionSeries", action: "show", id: acInteractionInstance.actionSeries.id)
    }

	protected deleteInstance(instance) {
		def actionSeries = instance.actionSeries
		AcInteraction.withTransaction{ status ->
			actionSeries.removeAction(instance)
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
		instance.actionSeries
	}
}