package com.banda.network.domain

import edu.banda.coel.web.BaseDomainController

class NetworkActionSeriesController extends BaseDomainController {

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','name','timeCreated']
		super.list(max)
	}

	def create() {
		[instance: new NetworkActionSeries(params)]
	}

    def save() {
		def networkActionSeriesInstance = new NetworkActionSeries(params)

		networkActionSeriesInstance.createdBy = currentUserOrError

        if (!networkActionSeriesInstance.save(flush: true)) {
            render(view: "create", model: [instance: networkActionSeriesInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'networkActionSeries.label', default: 'Network Interaction Series'), networkActionSeriesInstance.id])
		redirect(action: "show", id: networkActionSeriesInstance.id)
    }

	def update(Long id, Long version) {
        def networkActionSeriesInstance = getSafe(id)
		if (!networkActionSeriesInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'networkActionSeries.label', default: 'Network Interaction Series'), id])
			redirect(action: "list")
			return
		}

		if (version != null) {
			if (networkActionSeriesInstance.version > version) {
				networkActionSeriesInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: 'networkActionSeries.label', default: 'Network Interaction Series')] as Object[],
						  "Another user has updated this NetworkActionSeries while you were editing")
					render(view: "edit", model: [networkActionSeriesInstance: networkActionSeriesInstance])
					return
			}
		}

        networkActionSeriesInstance.properties = params

		if (!networkActionSeriesInstance.save(flush: true)) {
			render(view: "edit", model: [networkActionSeriesInstance: networkActionSeriesInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [message(code: 'networkActionSeries.label', default: 'Network Interaction Series'), networkActionSeriesInstance.id])
		redirect(action: "show", id: networkActionSeriesInstance.id)
    }
}