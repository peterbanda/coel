package com.banda.network.domain

import edu.banda.coel.web.BaseDomainController
import com.banda.core.util.ConversionUtil

class SpatialNeighborController extends BaseDomainController {
	
	def index() {
		redirect(action: "show", params: params)
	}

	def create() {
		def instance = new SpatialNeighbor(params)

		if (params.parent.id) {
			def spatialNeighborhood = SpatialNeighborhood.get(params.parent.id)
			spatialNeighborhood.addNeighbor(instance)
			[instance: instance]
		} else {
			flash.message = "Spatial neighborhood expected for a spatial neighbor"
			redirect(controller: "spatialNeighborhood", action: "list")
			return
		}
	}

	def save() {
		def spatialNeighborInstance = new SpatialNeighbor(params)

		if (params.coordinateDiffs) {
			def coordinateDiffs = ConversionUtil.convertToList(Integer.class, params.coordinateDiffs, "Spatial Topology Sizes")
			spatialNeighborInstance.setCoordinateDiffs(coordinateDiffs)
		}

		if (!spatialNeighborInstance.save(flush: true)) {
			render(view: "create", model: [instance: spatialNeighborInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'spatialNeighbor.label', default: 'Spatial Neighbor'), spatialNeighborInstance.id])
		redirect(action: "show", id: spatialNeighborInstance.id)
	}

	def update(Long id, Long version) {
		def spatialNeighborInstance = getSafe(id)
		if (!spatialNeighborInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'spatialNeighbor.label', default: 'Network Weight Setting'), id])
			redirect(action: "list")
			return
		}
	
		if (version != null) {
			if (spatialNeighborInstance.version > version) {
				spatialNeighborInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: 'spatialNeighbor.label', default: 'Spatial Neighbor')] as Object[],
						  "Another user has updated this spatial neighbor while you were editing")
					render(view: "edit", model: [spatialNeighborInstance: spatialNeighborInstance])
					return
			}
		}

		spatialNeighborInstance.properties = params
		if (params.coordinateDiffs) {
			def coordinateDiffs = ConversionUtil.convertToList(Integer.class, params.coordinateDiffs, "Spatial Topology Sizes")
			spatialNeighborInstance.setCoordinateDiffs(coordinateDiffs)
		}

		if (!spatialNeighborInstance.save(flush: true)) {
			render(view: "edit", model: [instance: spatialNeighborInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [message(code: 'spatialNeighbor.label', default: 'Network Weight Setting'), spatialNeighborInstance.id])
		redirect(action: "show", id: spatialNeighborInstance.id)
	}
}