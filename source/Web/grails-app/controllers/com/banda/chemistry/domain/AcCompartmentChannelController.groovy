package com.banda.chemistry.domain

import edu.banda.coel.web.BaseDomainController

import com.banda.chemistry.domain.AcCompartmentChannel

class AcCompartmentChannelController extends BaseDomainController {

	def create() {
		def instance = new AcCompartmentChannel(params)
		instance.direction = AcChannelDirection.In

		if (params.compartment.id) {
			def compartment = AcCompartment.get(params.compartment.id)
			compartment.addChannel(instance)
			[instance: instance]
		} else {
			flash.message = "Compartment expected for a new channel"
			redirect(controller: "acCompartment", action: "list")
			return
		}
	}

	def save() {
		def acCompartmentChannelInstance = new AcCompartmentChannel(params)

		def innerSpecies = AcSpecies.get(params.long('innerSpeciesId'))
		def parentSpecies = AcSpecies.get(params.long('parentSpeciesId'))

		if (acCompartmentChannelInstance.direction == AcChannelDirection.In) {
			acCompartmentChannelInstance.sourceSpecies = parentSpecies
			acCompartmentChannelInstance.targetSpecies = innerSpecies
		} else {
			acCompartmentChannelInstance.sourceSpecies = innerSpecies
			acCompartmentChannelInstance.targetSpecies = parentSpecies
		}

		if (!acCompartmentChannelInstance.save(flush: true)) {
			render(view: "create", model: [instance: acCompartmentChannelInstance])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, acCompartmentChannelInstance.id])
		redirect(controller: "acCompartment", action: "show", id: acCompartmentChannelInstance.compartment.id)
	}

	def update(Long id, Long version) {
		def acCompartmentChannelInstance = getSafe(id)
		if (!acCompartmentChannelInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (acCompartmentChannelInstance.version > version) {
				setOlcFailureMessage(acCompartmentChannelInstance)
				render(view: "edit", model: [instance: acCompartmentChannelInstance])
				return
			}

		acCompartmentChannelInstance.properties = params
	
		def innerSpecies = AcSpecies.get(params.long('innerSpeciesId'))
		def parentSpecies = AcSpecies.get(params.long('parentSpeciesId'))

		if (acCompartmentChannelInstance.direction == AcChannelDirection.In) {
			acCompartmentChannelInstance.sourceSpecies = parentSpecies
			acCompartmentChannelInstance.targetSpecies = innerSpecies
		} else {
			acCompartmentChannelInstance.sourceSpecies = innerSpecies
			acCompartmentChannelInstance.targetSpecies = parentSpecies
		}

		if (!acCompartmentChannelInstance.save(flush: true)) {
			render(view: "edit", model: [instance: acCompartmentChannelInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acCompartmentChannelInstance.id])
		redirect(controller: "acCompartment", action: "show", id: acCompartmentChannelInstance.compartment.id)
	}

	protected def deleteInstance(instance) {
		def compartment = instance.compartment
		AcCompartmentChannel.withTransaction{ status ->
			compartment.removeChannel(instance)
			instance.delete(flush: true)
		}
	}

	protected def notFoundRedirect(id) {
		redirect(controller: "acCompartment", action: "list")
	}

	protected def deleteSuccessfulRedirect(instance, owner) {
		redirect(controller: "acCompartment", action: "show", id: owner.id)
	}

	protected def deleteMultiSuccessfulRedirect(instancesWithOwners) {
		def compartment = instancesWithOwners.first().value
		redirect(controller: "acCompartment", action: "show", id: compartment.id)
	}

	protected def getOwner(instance) {
		instance.compartment
	}
}