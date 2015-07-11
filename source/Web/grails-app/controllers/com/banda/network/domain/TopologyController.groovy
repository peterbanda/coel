package com.banda.network.domain

import com.banda.core.util.ConversionUtil
import com.banda.core.util.ObjectUtil
import edu.banda.coel.business.Replicator
import edu.banda.coel.web.BaseDomainController

class TopologyController extends BaseDomainController {

    def replicator = Replicator.instance

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

		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'

		def spatialNeighborhoods = SpatialNeighborhood.listWithParamsAndProjections(params)

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

		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'

		def spatialNeighborhoods = SpatialNeighborhood.listWithParamsAndProjections(params)

		result << [spatialNeighborhoods : spatialNeighborhoods]
	}

	def update(Long id, Long version) {
		def topologyInstance = getSafe(id)
		if (!topologyInstance) {
			handleObjectNotFound(id)
			return
		}

		if (version != null) {
			if (topologyInstance.version > version) {
				setOlcFailureMessage(topologyInstance)
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

    protected def copyInstance(instance) {
        def newInstance = replicator.cloneTopology(instance)
        ObjectUtil.nullIdAndVersion(newInstance)

        def name = newInstance.name + " copy"
        if (name.size() > 30) name = name.substring(0, 30)

        newInstance.name = name
        newInstance.timeCreated = new Date()
        newInstance.createdBy = currentUserOrError

        newInstance.save(flush: true)
        newInstance
    }
}