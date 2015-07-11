package com.banda.network.domain

import com.banda.core.util.ConversionUtil
import edu.banda.coel.web.BaseDomainController
import grails.converters.JSON
import org.apache.commons.lang.StringUtils

import java.util.regex.Matcher
import java.util.regex.Pattern

class SpatialNeighborController extends BaseDomainController {
	
	def index() {
		redirect(action: "show", params: params)
	}

	def saveAjax() {
        SpatialNeighborhood spatialNeighborhood = SpatialNeighborhood.get(params.parent.id)

        def index = 0
        if (!spatialNeighborhood.neighbors.isEmpty())
            index = 1 + spatialNeighborhood.neighbors.max{it.index}.index

		def neighborCoordinateDiffs = StringUtils.split(params.coordinateDiffs, '(')
		def ids = []
		def neighborInstances = []
		neighborCoordinateDiffs.each{ item ->
			def trimmedItem = item.replace(')','').trim()
            Matcher m = Pattern.compile("[\\-]?\\d+").matcher(trimmedItem);
            def coordinateDiffs = []
            while (m.find()) {
                coordinateDiffs += ConversionUtil.convertToInteger(m.group(0), "Coordinate")
            }
			if (!coordinateDiffs.isEmpty()) {
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

    def updateAjax(String value, Long id) {
        def instance = getSafe(id)
        if (!instance) {
            handleObjectNotFoundAjax(id)
            return
        }

        Matcher m = Pattern.compile("[\\-]?\\d+").matcher(value);
        def coordinateDiffs = []
        while (m.find()) {
            coordinateDiffs += ConversionUtil.convertToInteger(m.group(0), "Coordinate")
        }
        if (!coordinateDiffs.isEmpty()) {
            instance.coordinateDiffs = coordinateDiffs
        }

        saveFromAjax(instance, "coordinateDiffs")
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
        instance.parent
    }
}