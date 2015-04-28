package com.banda.core.domain.um

import edu.banda.coel.web.BaseDomainController
import com.banda.core.domain.um.Role

class RoleController extends BaseDomainController {
	
	def index() {
		redirect(action: "list", params: params)
	}

	def create() {
		[instance: new Role(params)]
	}

	def save() {
		def roleInstance = new Role(params)
		if (!roleInstance.save(flush: true)) {
			render(view: "create", model: [instance: roleInstance])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, roleInstance.id])
		redirect(action: "show", id: roleInstance.id)
	}

	def update(Long id, Long version) {
		def roleInstance = getSafe(id)
		if (!roleInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (roleInstance.version > version) {
				setOlcFailureMessage(roleInstance)
				render(view: "edit", model: [instance: roleInstance])
				return
			}

		roleInstance.properties = params
	
		if (!roleInstance.save(flush: true)) {
			render(view: "edit", model: [instance: roleInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, roleInstance.id])
		redirect(action: "show", id: roleInstance.id)
	}
}