package com.banda.network.domain

import edu.banda.coel.web.BaseDomainController

class SpatialNeighborhoodController extends BaseDomainController {
	
	def index() {
		redirect(action: "show", params: params)
	}

	def create() {
		def instance = new SpatialNeighborhood(params)

		[instance: instance]
	}

	def save() {
		def spatialNeighborhoodInstance = new SpatialNeighborhood(params)

		spatialNeighborhoodInstance.createdBy = currentUserOrError

		if (!spatialNeighborhoodInstance.save(flush: true)) {
			render(view: "create", model: [instance: spatialNeighborhoodInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'spatialNeighborhood.label', default: 'Spatial Neighborhood'), spatialNeighborhoodInstance.id])
		redirect(action: "show", id: spatialNeighborhoodInstance.id)
	}
	
	def update(Long id, Long version) {
		def spatialNeighborhoodInstance = getSafe(id)
		if (!spatialNeighborhoodInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'spatialNeighborhood.label', default: 'Spatial Neighborhood'), id])
			redirect(action: "list")
			return
		}
	
		if (version != null) {
			if (spatialNeighborhoodInstance.version > version) {
				spatialNeighborhoodInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: 'spatialNeighborhood.label', default: 'Spatial Neighborhood')] as Object[],
						  "Another user has updated this spatial neighborhood while you were editing")
					render(view: "edit", model: [instance: spatialNeighborhoodInstance])
					return
			}
		}

		spatialNeighborhoodInstance.properties = params
	
		if (!spatialNeighborhoodInstance.save(flush: true)) {
			render(view: "edit", model: [instance: spatialNeighborhoodInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [message(code: 'spatialNeighborhood.label', default: 'Spatial Neighborhood'), spatialNeighborhoodInstance.id])
		redirect(action: "show", id: spatialNeighborhoodInstance.id)
	}
}