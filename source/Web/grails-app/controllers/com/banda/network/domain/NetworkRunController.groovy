package com.banda.network.domain

import com.banda.core.dynamics.StateAlternationType
import com.banda.core.util.ConversionUtil
import com.banda.core.util.ObjectUtil
import com.banda.math.domain.rand.DiscreteDistribution
import edu.banda.coel.business.netpic.JavaNetworkRunSpatialPicGenerator
import edu.banda.coel.domain.service.NetworkService
import edu.banda.coel.task.network.NetworkRunTask
import edu.banda.coel.web.BaseController
import edu.banda.coel.web.NetworkRunSpatialImageData
import grails.converters.JSON

class NetworkRunController extends BaseController {

	static navigationScope = "none"

	def NetworkService networkService

	def index = {
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'

		def networks = Network.listWithParamsAndProjections(params)
		if (networks.isEmpty()) {
			flash.message = "No networks found. You must first define one."
		}

		def instance = new NetworkRunTask()
        instance.properties = params
        if (!instance.runTime)
            instance.runTime = 100

        [instance: instance, networks : networks, oneZeroRatio : params.oneZeroProbability ?: 0.5, format : params.format, runnow : params.runnow != null]
	}

	def runSimulation() {
		NetworkRunTask networkRunTask = new NetworkRunTask()
		networkRunTask.properties = params

		if (params.topologySizes) {
			def sizes = ConversionUtil.convertToList(Integer.class, params.topologySizes, "Spatial Topology Sizes")
			def network = Network.get(networkRunTask.network.id)
			network.topology.sizes = sizes
			networkRunTask.network = network
		}

		def ratio = params.double('oneZeroRatio')
		
		def actionSeries = new NetworkActionSeries<Boolean>()
				
		def initAction = new NetworkAction<Boolean>()
		initAction.setStartTime(0)
		initAction.setTimeLength(0d)
		initAction.setAlternationType(StateAlternationType.Replacement)
		initAction.setStateDistribution(new DiscreteDistribution<Boolean>([ratio, 1 - ratio] as double[], [true, false] as Boolean[]))
		actionSeries.addAction(initAction)

		networkRunTask.setActionSeries(actionSeries)

		def runTraces = networkService.runSimulation(networkRunTask)
		def runTrace = ObjectUtil.getFirst(runTraces)

		def timeRunTrace = runTrace.transpose()
	
		def firstNode = timeRunTrace.components().get(0)
		def zoom = 2
		if (params.format == "svg") zoom = 1

		def spatialNetworkRunPicGenerator = (firstNode.hasLocation()) ? 
				(firstNode.location.size() == 1) ?
					JavaNetworkRunSpatialPicGenerator.createSpatialJavaBoolean1D(timeRunTrace.components().size(), zoom)
				:
					JavaNetworkRunSpatialPicGenerator.createSpatialJavaBoolean2D(zoom)
			: JavaNetworkRunSpatialPicGenerator.createNonSpatialJavaBoolean2D(zoom)

		def data = spatialNetworkRunPicGenerator.generateImageData(timeRunTrace, params.format, true)
		def data2 = new NetworkRunSpatialImageData()
		data2.format = data.format
		data2.width = data.width
		data2.height = data.height
		data2.images = data.images
//		if (data.format == "svg")
//			data2.images = data.images.collect{
//				def utf8Svg = URLEncoder.encode(it, "UTF-8")
//				StringUtils.replace(utf8Svg, "+", "%20")
//			}

		JSON.use("deep") {
			def dataJSON = data2 as JSON
			render dataJSON 
		}
//
//		def svgs = spatialNetworkRunPicGenerator.generateSVGStrings(timeRunTrace)
//		def svgXMLURIs = []
//		svgs.each{
//			def utf8Svg = URLEncoder.encode(it, "UTF-8")
//			svgXMLURIs.add(StringUtils.replace(utf8Svg, "+", "%20"))
//		} 
//		render svgXMLURIs as JSON
	}

	def getTopologySizes(Long id) {
		def network = Network.get(id)
		if (network.topology instanceof SpatialTopology)
			render network.topology.sizes as JSON
		else
			render ""
	}
}