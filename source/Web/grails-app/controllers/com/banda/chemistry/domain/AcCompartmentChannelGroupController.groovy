package com.banda.chemistry.domain

import edu.banda.coel.web.BaseDomainController

import com.banda.chemistry.domain.AcCompartmentChannelGroup

class AcCompartmentChannelGroupController extends BaseDomainController {

	def create() {
		def instance = new AcCompartmentChannelGroup(params)

		if (params.compartment.id) {
			def compartment = AcCompartment.get(params.compartment.id)
			compartment.addSubChannelGroup(instance)
			[instance: instance]
		} else {
			flash.message = "Compartment expected for a new channel group"
			redirect(controller: "acCompartment", action: "list")
			return
		}

		[instance: instance]
	}

	def save() {
		def acCompartmentChannelGroupInstance = new AcCompartmentChannelGroup(params)
		def compartment = AcCompartment.get(params.compartment.id)
		// channels
		compartment.subCompartments*.channels.flatten().eachWithIndex() { channel, i ->
			def contains = request.getParameter("contains${i}")
			def contained = acCompartmentChannelGroupInstance.channels.contains(channel)
			if (contains) {
				if (!contained) {
//					channel.removeFromGroup()
					acCompartmentChannelGroupInstance.addChannel(channel)
				}
			} else {
				if (contained) {
					acCompartmentChannelGroupInstance.removeChannel(channel)
				}
			}
		}

		if (!acCompartmentChannelGroupInstance.save(flush: true)) {
			render(view: "create", model: [instance: acCompartmentChannelGroupInstance])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, acCompartmentChannelGroupInstance.id])
		redirect(controller: "acCompartment", action: "show", id: acCompartmentChannelGroupInstance.compartment.id)
	}

	def update(Long id, Long version) {
		def acCompartmentChannelGroupInstance = getSafe(id)
		if (!acCompartmentChannelGroupInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (acCompartmentChannelGroupInstance.version > version) {
				setOlcFailureMessage(acCompartmentChannelGroupInstance)
				render(view: "edit", model: [instance: acCompartmentChannelGroupInstance])
				return
			}

		// channels
		acCompartmentChannelGroupInstance.compartment.subCompartments*.channels.flatten().eachWithIndex() { channel, i ->
			def contains = request.getParameter("contains${i}")
			def contained = acCompartmentChannelGroupInstance.channels.contains(channel)
			if (contains) {
				if (!contained) {
//					channel.removeFromGroup()
					acCompartmentChannelGroupInstance.addChannel(channel)
				}
			} else {
				if (contained) {
					acCompartmentChannelGroupInstance.removeChannel(channel)
				}
			}
		}
		acCompartmentChannelGroupInstance.properties = params
	
		if (!acCompartmentChannelGroupInstance.save(flush: true)) {
			render(view: "edit", model: [instance: acCompartmentChannelGroupInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acCompartmentChannelGroupInstance.id])
		redirect(controller: "acCompartment", action: "show", id: acCompartmentChannelGroupInstance.compartment.id)
	}

	protected def deleteInstance(instance) {
		def compartment = instance.compartment
		AcCompartmentChannelGroup.withTransaction{ status ->
			compartment.removeSubChannelGroup(instance)
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
		redirect(controller: "acCompartment", action: "show", id: instancesWithOwners.first().value.id)
	}

	protected def getOwner(instance) {
		instance.compartment
	}
}