package com.banda.chemistry.domain

import edu.banda.coel.web.BaseDomainController

class AcRateConstantTypeBoundController extends BaseDomainController {

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','rateConstantType','from','to']
		super.list(max)
	}

	def create() {
		def acRateConstantTypeBoundInstance =  new AcRateConstantTypeBound()
		acRateConstantTypeBoundInstance.properties = params
		[instance: acRateConstantTypeBoundInstance]
	}

	def save() {
		def acRateConstantTypeBoundInstance = new AcRateConstantTypeBound(params)
		acRateConstantTypeBoundInstance.createdBy = currentUserOrError

		if (!acRateConstantTypeBoundInstance.save(flush: true)) {
			render(view: "create", model: [instance: acRateConstantTypeBoundInstance])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, acRateConstantTypeBoundInstance.id])
		redirect(action: "show", id: acRateConstantTypeBoundInstance.id)
	}

	def update(Long id, Long version) {
		def acRateConstantTypeBoundInstance = getSafe(id)
		if (!acRateConstantTypeBoundInstance) {
			handleObjectNotFound(id)
			return
		}

		if (version != null)
			if (acRateConstantTypeBoundInstance.version > version) {
				setOlcFailureMessage(acRateConstantTypeBoundInstance)
				render(view: "edit", model: [instance: acRateConstantTypeBoundInstance])
				return
			}

		acRateConstantTypeBoundInstance.properties = params

		if (!acRateConstantTypeBoundInstance.save(flush: true)) {
			render(view: "edit", model: [instance: acRateConstantTypeBoundInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acRateConstantTypeBoundInstance.id])
		redirect(action: "show", id: acRateConstantTypeBoundInstance.id)
	}
}