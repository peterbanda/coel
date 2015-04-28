package com.banda.network.domain

import java.util.Collection
import java.util.Arrays

import org.apache.commons.lang.StringUtils
import org.springframework.dao.DataIntegrityViolationException

import edu.banda.coel.domain.service.NetworkService
import edu.banda.coel.web.BaseDomainController

import com.banda.network.domain.NetworkDamageSpreading
import com.banda.network.domain.NetworkSimulationConfig

import edu.banda.coel.task.network.NetworkRunTask

import com.banda.core.util.ConversionUtil
import com.banda.network.domain.Network

import edu.banda.coel.web.ChartData
import edu.banda.coel.web.ChartData.SeriesGroup

import grails.converters.JSON
import edu.banda.coel.web.ChartData
import edu.banda.coel.web.ChartData.SeriesGroup

import com.banda.core.util.ParseUtil
import com.banda.math.domain.StatsSequence
import com.banda.math.domain.StatsType
import com.banda.math.business.JavaMathUtil
	
class NetworkDamageSpreadingController extends BaseDomainController {

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
		networkRunTaskInstance.runTime = 300
		[instance: networkRunTaskInstance, networks : networks]
	}

	def show(Long id) {
		def networkDamageSpreadingInstance = getSafe(id)
		if (!networkDamageSpreadingInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'networkDamageSpreading.label', default: 'Network Damage Spreading'), id])
			redirect(action: "list")
			return
		}
		def chartData = getChartData(
			networkDamageSpreadingInstance.result,
			 "Network Damage Spreading",
			 "Time",
			 "Distance")

		[instance: networkDamageSpreadingInstance, chartData : chartData]
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

		networkService.runBooleanDamageSpreadingAnalysis(networkRunTaskInstance)
		render "success"
	}

	def exportAsCSV() {
		if (!params.ids) {
			flash.message = "No rows selected"
			redirect(action: "list")
		}
		def StatsType statsType = params.statsType

		def networkDamageSpreadingIds = ParseUtil.parseArray(params.ids, Long.class, "Network Damage Spreding id", ",")
		StringBuilder sb = new StringBuilder()
		networkDamageSpreadingIds.each { id ->
			def networkDamageSpreadingInstance = getSafe(id)
			def projResults = JavaMathUtil.projStats(statsType, networkDamageSpreadingInstance.result.stats)

			sb.append(id)
			sb.append(',')
			sb.append(networkDamageSpreadingInstance.network.id)
			sb.append(',')
			sb.append(networkDamageSpreadingInstance.network.name)
			sb.append(',')
			sb.append(StringUtils.join(projResults, ','))
			sb.append('\n')
		}
		def networkDamageSpreadingMinId = networkDamageSpreadingIds[networkDamageSpreadingIds.size - 1]
		def networkDamageSpreadingMaxId = networkDamageSpreadingIds[0]
        response.setHeader("Content-disposition", "attachment; filename=" + "netDerrida_${networkDamageSpreadingMinId}-${networkDamageSpreadingMaxId}.csv");
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
		def derridaAnalysisIds = ParseUtil.parseArray(params.ids, Long.class, "Network Damage Spreading id", ",")

		def chartData = new ChartData()
		chartData.title = "Network Damage Spreading (" + statsType.toString() + ")"
		chartData.xAxisCaption = "Time"
		chartData.yAxisCaption = "Distance"

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