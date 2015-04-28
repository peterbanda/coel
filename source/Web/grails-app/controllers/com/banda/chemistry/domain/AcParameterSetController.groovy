package com.banda.chemistry.domain

import edu.banda.coel.web.BaseDomainController

import com.banda.chemistry.domain.AcParameterSet;

class AcParameterSetController extends BaseDomainController {

	static allowedMethods = [create: "", delete: ""]  // can't create or delete

	def update(Long id, Long version) {
		def acParameterSetInstance = getSafe(id)
		if (!acParameterSetInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (acParameterSetInstance.version > version) {
				setOlcFailureMessage(acParameterSetInstance)
				render(view: "edit", model: [instance: acParameterSetInstance])
				return
			}

		acParameterSetInstance.properties = params
	
		if (!acParameterSetInstance.save(flush: true)) {
			render(view: "edit", model: [instance: acParameterSetInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acParameterSetInstance.id])
		redirect(action: "show", id: acParameterSetInstance.id)
	}
}