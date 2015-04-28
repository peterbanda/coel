package com.banda.network.domain

import java.util.Collection
import java.util.Arrays

import org.apache.commons.lang.StringUtils
import org.springframework.dao.DataIntegrityViolationException

import edu.banda.coel.domain.service.NetworkService
import edu.banda.coel.web.BaseDomainController

import com.banda.network.domain.SpatialNetworkPerformance
import com.banda.network.domain.NetworkSimulationConfig
import com.banda.network.domain.SpatialNetworkPerformance

import edu.banda.coel.task.network.SpatialNetworkPerformanceEvaluateTask

import com.banda.core.util.ConversionUtil
import com.banda.network.domain.Network

import edu.banda.coel.web.ChartData
import edu.banda.coel.web.ChartData.SeriesGroup
import grails.converters.JSON

import com.banda.core.util.ParseUtil
import com.banda.math.domain.StatsSequence
import com.banda.math.domain.StatsType
import com.banda.math.business.JavaMathUtil
	
class SpatialNetworkPerformanceController extends BaseDomainController {

	def NetworkService networkService

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','timeCreated','network','interactionSeries','evaluation']
		super.list(max)
	}

	def create() {
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'

		def networks = Network.listWithParamsAndProjections(params)
		def actionSeries = NetworkActionSeries.listWithParamsAndProjections(params)
		def evaluations = NetworkEvaluation.listWithParamsAndProjections(params)

		def networkPerformanceEvaluationTaskInstance = new SpatialNetworkPerformanceEvaluateTask()
		networkPerformanceEvaluationTaskInstance.repetitions = 10000
		networkPerformanceEvaluationTaskInstance.runTime = 300
		[instance: networkPerformanceEvaluationTaskInstance, networks : networks, actionSeries : actionSeries, evaluations : evaluations]
	}

	def show(Long id) {
		def networkPerformanceInstance = getSafe(id)
		if (!networkPerformanceInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'spatialNetworkPerformance.label', default: 'Spatial Network Performance'), id])
			redirect(action: "list")
			return
		}
		def chartData = getChartData(
			networkPerformanceInstance.results,
			 "Spatial Network Performance",
			 "Size",
			 "Performance",
			 networkPerformanceInstance.sizeFrom,
			 networkPerformanceInstance.sizeTo)

		[instance: networkPerformanceInstance, chartData : chartData]
	}

	def runPerformanceEvaluation() {
		def spatialNetworkPerformanceTaskInstance = new SpatialNetworkPerformanceEvaluateTask()
		bindData(spatialNetworkPerformanceTaskInstance, params, [])
		if (params.actionSeriesIds) {
			def actionSeriesIds = ConversionUtil.convertCollection(Long.class, params.list('actionSeriesIds'), "Action series id")
			spatialNetworkPerformanceTaskInstance.setNetworkActionSeriesIds(actionSeriesIds)
		}
		if (params.evaluationIds) {
			def evaluationIds = ConversionUtil.convertCollection(Long.class, params.list('evaluationIds'), "Network evaluation id")
			spatialNetworkPerformanceTaskInstance.setNetworkEvaluationIds(evaluationIds)
		}

		// sim config
		if (params.fpDetectorPeriodicity) {
			def simConfig = new NetworkSimulationConfig()
			simConfig.fixedPointDetectionPeriodicity = params.double('fpDetectorPeriodicity') 
			spatialNetworkPerformanceTaskInstance.simulationConfig = simConfig
		}

		networkService.runSpatialPerformanceEvaluation(spatialNetworkPerformanceTaskInstance)
		render "success"
	}

	def exportAsCSV() {
		if (!params.ids) {
			flash.message = "No rows selected"
			redirect(action: "list")
		}
		def evaluatedPerformanceIds = ParseUtil.parseArray(params.ids, Long.class, "Spatial Network performance id", ",")
		def StatsType statsType = params.statsType

		StringBuilder sb = new StringBuilder()
		evaluatedPerformanceIds.each { id ->
			def networkPerformanceInstance = getSafe(id)

			sb.append('Network')
			sb.append(',')
			sb.append('Interaction Series')
			sb.append(',')
			sb.append('Evaluation')
			sb.append(',')
			sb.append('Id')
			sb.append(',')
			sb.append(StringUtils.join(networkPerformanceInstance.sizeFrom..networkPerformanceInstance.sizeTo, ','))
			sb.append('\n')

			def projResults = JavaMathUtil.projStats(statsType, networkPerformanceInstance.results.stats)
			sb.append(networkPerformanceInstance.network.name)
			sb.append(',')
			sb.append(networkPerformanceInstance.interactionSeries.name)
			sb.append(',')
			sb.append(networkPerformanceInstance.evaluation.name)
			sb.append(',')
			sb.append(id)
			sb.append(',')
			sb.append(StringUtils.join(projResults, ','))
			sb.append('\n')
		}
		def evaluatedPerformanceMinId = evaluatedPerformanceIds[evaluatedPerformanceIds.size - 1]
		def evaluatedPerformanceMaxId = evaluatedPerformanceIds[0]
        response.setHeader("Content-disposition", "attachment; filename=" + "netSpatialEvaluatedPerformance_${evaluatedPerformanceMinId}-${evaluatedPerformanceMaxId}.csv");
        render(contentType: "text/csv", text: sb.toString());
	}

	def getChartData(StatsSequence statsSeq, title, xCaption, yCaption, xFrom, xTo) {
		def chartData = new ChartData()
		chartData.title = title
		chartData.xAxisCaption = xCaption
		chartData.yAxisCaption = yCaption

		def seriesGroup = new SeriesGroup()
		seriesGroup.groupLabel = "All"
		chartData.seriesGroups.add(seriesGroup)
		chartData.xSteps = xFrom..xTo

		StatsType.values().each{ statsType ->
			def projResults = JavaMathUtil.projStats(statsType, statsSeq.stats)
			seriesGroup.labels.add(statsType.toString())
			chartData.series.add(projResults)
		}

		JSON.use("deep") {
			def chartDataJSON = chartData as JSON
			chartDataJSON
		}
	}

	def showMulti() {
		if (!params.ids) {
			flash.message = "No rows selected"
			redirect(action: "list")
			return
		}

		def StatsType statsType = params.statsType
		def evaluatedPerformanceIds = ParseUtil.parseArray(params.ids, Long.class, "Spatial network performance id", ",")
		
		def chartData = new ChartData()
		chartData.title = "Spatial Network Performance (" + statsType.toString() + ")"
		chartData.xAxisCaption = "Size"
		chartData.yAxisCaption = "Performance"

		def seriesGroup = new SeriesGroup()
		seriesGroup.groupLabel = "All"
		chartData.seriesGroups.add(seriesGroup)

		def ids = []

		evaluatedPerformanceIds.each { id ->
			def performance = getSafe(id)
			def stats = performance.results.stats

			if (!chartData.xSteps) {
				chartData.xSteps = performance.sizeFrom..performance.sizeTo
			}

			def projResults = JavaMathUtil.projStats(statsType, stats)
			seriesGroup.labels.add(StringUtils.abbreviate(performance.network.name, 15) + "-" + StringUtils.abbreviate(performance.evaluation.name, 15))
			chartData.series.add(projResults)
			ids.add(id)
		}

		JSON.use("deep") {
			def chartDataJSON = chartData as JSON
			[ids : ids, chartData : chartDataJSON]
		}
	}
}