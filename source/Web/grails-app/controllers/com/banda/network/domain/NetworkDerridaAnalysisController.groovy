package com.banda.network.domain

import com.banda.core.util.ParseUtil
import com.banda.math.business.JavaMathUtil
import com.banda.math.domain.StatsSequence
import com.banda.math.domain.StatsType
import edu.banda.coel.domain.service.NetworkService
import edu.banda.coel.task.network.NetworkRunTask
import edu.banda.coel.web.BaseDomainController
import edu.banda.coel.web.ChartData
import edu.banda.coel.web.ChartData.SeriesGroup
import grails.converters.JSON
import org.apache.commons.lang.StringUtils

class NetworkDerridaAnalysisController extends BaseDomainController {

	def NetworkService networkService

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','timeCreated','network']
		super.list(max)
	}

	def create() {
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'

		def networks = Network.listWithParamsAndProjections(params)

		def networkRunTaskInstance = new NetworkRunTask()
		networkRunTaskInstance.repetitions = 100
		networkRunTaskInstance.runTime = 1
		[instance: networkRunTaskInstance, networks : networks]
	}

	def show(Long id) {
		def networkDerridaAnalysisInstance = getSafe(id)
		if (!networkDerridaAnalysisInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'networkDerridaAnalysis.label', default: 'Network Derrida Analysis'), id])
			redirect(action: "list")
			return
		}
		def chartData = getChartData(
			networkDerridaAnalysisInstance.result,
			 "Network Derrida Analysis",
			 "d_t",
			 "d_t+1")

		[instance: networkDerridaAnalysisInstance, chartData : chartData]
	}

	def runAnalysis() {
		def networkRunTaskInstance = new NetworkRunTask()
		bindData(networkRunTaskInstance, params, [])

		// sim config
		if (params.fpDetectorPeriodicity) {
			def simConfig = new NetworkSimulationConfig()
			simConfig.fixedPointDetectionPeriodicity = params.double('fpDetectorPeriodicity') 
			networkRunTaskInstance.simulationConfig = simConfig
		}

		networkService.runBooleanDerridaAnalysis(networkRunTaskInstance)
		render "success"
	}

	def exportAsCSV() {
		if (!params.ids) {
			flash.message = "No rows selected"
			redirect(action: "list")
		}
		def StatsType statsType = params.statsType

		def networkDerridaAnalysisIds = ParseUtil.parseArray(params.ids, Long.class, "Network Derrida Analysis id", ",")
		StringBuilder sb = new StringBuilder()
		networkDerridaAnalysisIds.each { id ->
			def networkDerridaAnalysisInstance = getSafe(id)
			def projResults = JavaMathUtil.projStats(statsType, networkDerridaAnalysisInstance.result.stats)

			sb.append(id)
			sb.append(',')
			sb.append(networkDerridaAnalysisInstance.network.id)
			sb.append(',')
			sb.append(networkDerridaAnalysisInstance.network.name)
			sb.append(',')
			sb.append(StringUtils.join(projResults, ','))
			sb.append('\n')
		}
		def networkDerridaAnalysisMinId = networkDerridaAnalysisIds[networkDerridaAnalysisIds.size - 1]
		def networkDerridaAnalysisMaxId = networkDerridaAnalysisIds[0]
        response.setHeader("Content-disposition", "attachment; filename=" + "netDerrida_${networkDerridaAnalysisMinId}-${networkDerridaAnalysisMaxId}.csv");
        render(contentType: "text/csv", text: sb.toString());
	}

	def getChartData(StatsSequence statsSeq, title, xCaption, yCaption) {
		def chartData = new ChartData()
		chartData.title = title
		chartData.xAxisCaption = xCaption
		chartData.yAxisCaption = yCaption

		def seriesGroup = new SeriesGroup()
		seriesGroup.groupLabel = "All"
		chartData.seriesGroups.add(seriesGroup)
		chartData.xSteps = statsSeq.stats*.pos

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
		def derridaAnalysisIds = ParseUtil.parseArray(params.ids, Long.class, "Network Derrida Analysis id", ",")

		def chartData = new ChartData()
		chartData.title = "Network Derrida Analysis (" + statsType.toString() + ")"
		chartData.xAxisCaption = "d_t"
		chartData.yAxisCaption = "d_t+1"

		def seriesGroup = new SeriesGroup()
		seriesGroup.groupLabel = "All"
		chartData.seriesGroups.add(seriesGroup)

		def ids = []

		derridaAnalysisIds.each { id ->
			def derridaAnalysis = getSafe(id)
			def stats = derridaAnalysis.result.stats

			if (!chartData.xSteps) {
				chartData.xSteps = stats*.pos
			}

			def projResults = JavaMathUtil.projStats(statsType, stats)
			seriesGroup.labels.add(derridaAnalysis.network.name)
			chartData.series.add(projResults)
			ids.add(id)
		}

		JSON.use("deep") {
			def chartDataJSON = chartData as JSON
			[ids : ids, chartData : chartDataJSON]
		}
	}
}