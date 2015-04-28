package com.banda.network.domain

import org.springframework.dao.DataIntegrityViolationException

import com.banda.core.util.ConversionUtil

import edu.banda.coel.web.BaseDomainController

class TopologyController extends BaseDomainController {
	
	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','timeCreated','name']
		super.list(max)
	}

	def createTemplate() {
		def topologyInstance =  new TemplateTopology()
		topologyInstance.properties = params
		
		def parentId = ""
		if (params.'parent.id') {
			parentId = params.'parent.id'
		}

		render(view: "create", model: [instance: topologyInstance, parentId : parentId])
	}

	def createLayered() {
		def topologyInstance =  new LayeredTopology()
		topologyInstance.properties = params

		def parentId = ""
		if (params.'parent.id') {
			parentId = params.'parent.id'
		}

		render(view: "create", model: [instance: topologyInstance, parentId : parentId])
	}

	def createSpatial() {
		def topologyInstance =  new SpatialTopology()
		topologyInstance.properties = params

		def parentId = ""
		if (params.'parent.id') {
			parentId = params.'parent.id'
		}

		def spatialNeighborhoods = SpatialNeighborhood.listWithProjections(['id', 'name'])

		render(view: "create", model: [instance: topologyInstance, spatialNeighborhoods : spatialNeighborhoods, parentId : parentId])
	}

	def save() {
		def topologyInstance
		if (params.type == "template") {
			topologyInstance = new TemplateTopology(params)
		} else if (params.type == "spatial") {
			topologyInstance = new SpatialTopology(params)
		} else {
			topologyInstance = new LayeredTopology(params)
		}

		topologyInstance.createdBy = currentUserOrError
		if (params.sizes) {
			def sizes = ConversionUtil.convertToList(Integer.class, params.sizes, "Spatial Topology Sizes")
			topologyInstance.setSizes(sizes)
		}

		if (!topologyInstance.save(flush: true)) {
			render(view: "create", model: [instance: topologyInstance])
			return
		}

		if (params.parentId) {
			def parentTopology = Topology.get(params.parentId)
			parentTopology.addLayer(topologyInstance)
			if (!parentTopology.save(flush: true)) {
				render(view: "create", model: [topologyInstance: topologyInstance])
				return
			}
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'topology.label', default: 'Topology'), topologyInstance.id])
		redirect(action: "show", id: topologyInstance.id)
	}

	def show(Long id) {
		def result = super.show(id)
		def topologies = Topology.listWithProjections(['id', 'name'])
		result << [topologies : topologies]
	}

	def edit(Long id) {
		def result = super.edit(id)	
		def spatialNeighborhoods = SpatialNeighborhood.listWithProjections(['id', 'name'])
		result << [spatialNeighborhoods : spatialNeighborhoods]
	}

	def update(Long id, Long version) {
		def topologyInstance = getSafe(id)
		if (!topologyInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'topology.label', default: 'Topology'), id])
			redirect(action: "list")
			return
		}

		if (version != null) {
			if (topologyInstance.version > version) {
				topologyInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: 'topology.label', default: 'Topology')] as Object[],
						  "Another user has updated this Topology while you were editing")
					render(view: "edit", model: [topologyInstance: topologyInstance])
					return
			}
		}

		topologyInstance.properties = params

		if (params.sizes) {
			def sizes = ConversionUtil.convertToList(Integer.class, params.sizes, "Spatial Topology Sizes")
			topologyInstance.setSizes(sizes)
		}

		if (!topologyInstance.save(flush: true)) {
			render(view: "edit", model: [topologyInstance: topologyInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [message(code: 'topology.label', default: 'Topology'), topologyInstance.id])
		redirect(action: "show", id: topologyInstance.id)
	}

	def addLayer(Long id, Long layerId) {
		def parentTopology = Topology.get(id)
		def layerTopology = Topology.get(layerId)
		parentTopology.addLayer(layerTopology)

		parentTopology.save(flush: true)

		redirect(action: "show", id: id)
	}
}