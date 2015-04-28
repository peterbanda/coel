package com.banda.network.domain

import edu.banda.coel.web.BaseDomainController

class NetworkWeightSettingController extends BaseDomainController {
	
	def index() {
		redirect(action: "show", params: params)
	}

	def update(Long id, Long version) {
		def networkWeightSettingInstance = getNetworkWeightSetting(id)
		if (!networkWeightSettingInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'fixedNetworkWeightSetting.label', default: 'Network Weight Setting'), id])
			redirect(action: "list")
			return
		}
	
		if (version != null) {
			if (networkWeightSettingInstance.version > version) {
				networkWeightSettingInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: 'fixedNetworkWeightSetting.label', default: 'FixedNetworkWeightSetting')] as Object[],
						  "Another user has updated this FixedNetworkWeightSetting while you were editing")
					render(view: "edit", model: [instance: networkWeightSettingInstance])
					return
			}
		}

		networkWeightSettingInstance.properties = params
	
		if (!networkWeightSettingInstance.save(flush: true)) {
			render(view: "edit", model: [instance: networkWeightSettingInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [message(code: 'fixedNetworkWeightSetting.label', default: 'Network Weight Setting'), networkWeightSettingInstance.id])
		redirect(action: "show", id: networkWeightSettingInstance.id)
	}

	private NetworkWeightSetting getNetworkWeightSetting(id) {
		// Grails can't handle polymorphic queries on abstract classes, such as, NetworkWeightSetting (that's clearly a bug)
		// workaround is to get all objects by each subtype
		def weightSettingInstance = TemplateNetworkWeightSetting.get(id)
		if (!weightSettingInstance) {
			weightSettingInstance = LayeredNetworkWeightSetting.get(id)
		}
		if (!weightSettingInstance) {
			weightSettingInstance = FixedNetworkWeightSetting.get(id)
		}
		weightSettingInstance
	}
}