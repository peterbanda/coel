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
            handleObjectNotFound(id)
			return
		}
	
		if (version != null) {
			if (networkInstance.version > version) {
                setOlcFailureMessage(networkInstance)
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

	protected def copyInstance(instance) {
        def newInstance = genericReflectionProvider.clone(instance)
        newInstance.id = null
        newInstance.version = null

		def name = newInstance.name + " copy"
		if (name.size() > 50) name = name.substring(0, 50)

		newInstance.name = name
		newInstance.timeCreated = new Date()
		newInstance.createdBy = currentUserOrError

		newInstance.save(flush: true)
		newInstance
	}
}