package com.banda.network.domain

import edu.banda.coel.web.BaseDomainController
import org.springframework.dao.DataIntegrityViolationException

class NetworkActionController extends BaseDomainController {

	def index() {
		redirect(action: "list", params: params)
	}

	def create() {
		def networkActionInstance =  new NetworkAction()
		networkActionInstance.properties = params
		def actionSeriesId = request.getParameter('actionSeries.id')
		if (actionSeriesId) {
			NetworkActionSeries actionSeries = NetworkActionSeries.get(actionSeriesId)
			actionSeries.addAction(networkActionInstance)
		}

		def networkActionSeries = NetworkActionSeries.listWithProjections(['id', 'name'])

		[instance: networkActionInstance, networkActionSeries : networkActionSeries]
	}

	def save() {
		def networkActionInstance = new NetworkAction(params)
		NetworkActionSeries actionSeries = NetworkActionSeries.get(params.actionSeries.id)
		actionSeries.addAction(networkActionInstance)
		
		networkActionInstance.stateDistribution = bindRandomDistribution("state")
		
		if (!networkActionInstance.save(flush: true)) {
			render(view: "create", model: [networkActionInstance: networkActionInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'networkAction.label', default: 'Network Action'), networkActionInstance.id])
		redirect(controller: "networkActionSeries", action: "show", id: actionSeries.id)
	}

	def edit(Long id) {
		def result = super.edit(id)	
		def networkActionSeries = NetworkActionSeries.listWithProjections(['id', 'name'])
		result << [networkActionSeries : networkActionSeries]
	}

	def update(Long id, Long version) {
        def networkActionInstance = getSafe(id)
		if (!networkActionInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'networkAction.label', default: 'Network Action'), params.id])}"
            redirect(action: "list")
			return
		}

		if (version != null) {
			if (networkActionInstance.version > version) {
				networkActionInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: 'networkAction.label', default: 'Network Action')] as Object[],
						  "Another user has updated this Role while you were editing")
					render(view: "edit", model: [networkActionInstance: networkActionInstance])
					return
			}
		}

		// Action series
		def oldActionSeriesId = networkActionInstance.actionSeries.id
		def newActionSeriesId = params.actionSeries.id

		if (oldActionSeriesId != newActionSeriesId) {
			NetworkActionSeries newActionSeries = NetworkActionSeries.get(newActionSeriesId)
			newActionSeries.addAction(networkActionInstance)
		}

		networkActionInstance.properties = params

		def newStateDistribution = bindRandomDistribution("state")

		if (!isRandomDistributionEqual(networkActionInstance.stateDistribution, newStateDistribution)) {
			networkActionInstance.stateDistribution = newStateDistribution
		}

		if (!networkActionInstance.save(flush: true)) {
			render(view: "edit", model: [networkActionInstance: networkActionInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [message(code: 'networkAction.label', default: 'Network Action'), networkActionInstance.id])
		redirect(controller: "networkActionSeries", action: "show", id: newActionSeriesId)
    }

	def delete(Long id) {
        def networkActionInstance = getSafe(id)
		if (!networkActionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'networkAction.label', default: 'Network Action'), id])
			redirect(action: "list")
			return
		}
	
		try {
			def actionSeries = networkActionInstance.actionSeries
			NetworkAction.withTransaction{ status ->
				actionSeries.removeAction(networkActionInstance)
				networkActionInstance.delete(flush: true)
			}
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'networkAction.label', default: 'Network Action'), id])
			redirect(controller: "networkActionSeries", action: "show", id: actionSeries.id)
		} catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'networkAction.label', default: 'Network Action'), id])
			redirect(action: "show", id: id)
		}
    }
}