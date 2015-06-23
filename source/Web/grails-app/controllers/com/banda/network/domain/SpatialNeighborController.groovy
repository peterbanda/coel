package com.banda.network.domain

import com.banda.chemistry.domain.AcSpecies
import com.banda.chemistry.domain.AcSpeciesSet
import com.banda.core.util.ConversionUtil
import com.banda.core.util.ParseUtil
import edu.banda.coel.web.BaseDomainController
import grails.converters.JSON
import org.apache.commons.lang.StringUtils

class SpatialNeighborController extends BaseDomainController {
	
	def index() {
		redirect(action: "show", params: params)
	}

	def saveAjax() {
        SpatialNeighborhood spatialNeighborhood = SpatialNeighborhood.get(params.parent.id)

        def index = 0
        if (!spatialNeighborhood.neighbors.isEmpty())
            index = 1 + spatialNeighborhood.neighbors.max {it.index}

		def neighborCoordinateDiffs = StringUtils.split(params.coordinateDiffs, '(')
		def ids = []
		def neighborInstances = []
		neighborCoordinateDiffs.each{ item ->
			def trimmedItem = item.replace(')','').trim()
			if (trimmedItem.matches("[0-9, ]*")) {
                def coordinateDiffs = ConversionUtil.convertToList(Integer.class, trimmedItem, "Spatial Neighbor Coordinate Diffs")

				def spatialNeighbor = new SpatialNeighbor()
                spatialNeighbor.index = index
                spatialNeighbor.coordinateDiffs = coordinateDiffs
                spatialNeighborhood.addNeighbor(spatialNeighbor)

				if (!spatialNeighbor.save(flush: true)) {
					return
				}
                ids.add(spatialNeighbor.id)
                neighborInstances.add(spatialNeighbor)
                index++
			}
		}

		def size = ids.size()
		def text = null
		if (size == 0)
			text = message(code: 'species.notcreated.message')
		else if (size == 1)
			text = message(code: 'default.created.message', args: [doClazzMessageLabel, ids.get(0)])
		else
			text = message(code: 'default.multicreated.message', args: [doClazzMessageLabel, ids.join(", "), size])
		render ([message: text, neighborInstances : neighborInstances] as JSON)
	}

	def save() {
		def spatialNeighborInstance = new SpatialNeighbor(params)

		if (params.coordinateDiffs) {
			def coordinateDiffs = ConversionUtil.convertToList(Integer.class, params.coordinateDiffs, "Spatial Neighbor Coordinate Diffs")
			spatialNeighborInstance.setCoordinateDiffs(coordinateDiffs)
		}

		if (!spatialNeighborInstance.save(flush: true)) {
			render(view: "create", model: [instance: spatialNeighborInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'spatialNeighbor.label', default: 'Spatial Neighbor'), spatialNeighborInstance.id])
		redirect(action: "show", id: spatialNeighborInstance.id)
	}

    protected def deleteInstance(instance) {
        def spatialNeighborhood = instance.parent
        SpatialNeighbor.withTransaction{ status ->
            spatialNeighborhood.removeNeighbor(instance)
            instance.delete(flush: true)
        }
    }

    protected def notFoundRedirect(id) {
        redirect(controller: "SpatialNeighborhood", action: "list")
    }

    protected def deleteSuccessfulRedirect(instance, owner) {
        redirect(controller: "SpatialNeighborhood", action: "show", id: owner.id)
    }

    protected def deleteMultiSuccessfulRedirect(instancesWithOwners) {
        redirect(controller: "SpatialNeighborhood", action: "show", id: instancesWithOwners.first().value.id)
    }

    protected def getOwner(instance) {
        instance.parentSet
    }
}