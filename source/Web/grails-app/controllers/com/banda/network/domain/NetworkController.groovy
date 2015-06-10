package com.banda.network.domain

import edu.banda.coel.web.BaseDomainController

class NetworkController extends BaseDomainController {

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','timeCreated','name']
		super.list(max)
	}

	def create() {
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'

		def topologies = Topology.listWithParamsAndProjections(params)
		def functions = NetworkFunction.listWithParamsAndProjections(params)
		def networkWeightSettings = NetworkWeightSetting.listWithParamsAndProjections(params)

		[instance: new Network(params), topologies : topologies, functions : functions, networkWeightSettings : networkWeightSettings]
	}
	
	def save() {
		def networkInstance = new Network(params)

		networkInstance.createdBy = currentUserOrError

		if (!networkInstance.save(flush: true)) {
			render(view: "create", model: [instance: networkInstance])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [message(code: 'network.label', default: 'Network'), networkInstance.id])
		redirect(action: "show", id: networkInstance.id)
	}

	def edit(Long id) {
		def result = super.edit(id)
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'

		def topologies = Topology.listWithParamsAndProjections(params)
		def functions = NetworkFunction.listWithParamsAndProjections(params)
		def networkWeightSettings = NetworkWeightSetting.listWithParamsAndProjections(params)

		result << [topologies : topologies, functions : functions, networkWeightSettings : networkWeightSettings]
	}

	def update(Long id, Long version) {
		def networkInstance = getSafe(id)
		if (!networkInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'network.label', default: 'Network'), id])
			redirect(action: "list")
			return
		}
	
		if (version != null) {
			if (networkInstance.version > version) {
				networkInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: 'network.label', default: 'Network')] as Object[],
						  "Another user has updated this Network while you were editing")
					render(view: "edit", model: [networkInstance: networkInstance])
					return
			}
		}

		networkInstance.properties = params
	
		if (!networkInstance.save(flush: true)) {
			render(view: "edit", model: [instance: networkInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [message(code: 'network.label', default: 'Network'), networkInstance.id])
		redirect(action: "show", id: networkInstance.id)
	}
}