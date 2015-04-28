package com.banda.network.domain

import java.util.Collection

import java.util.Arrays

import org.apache.commons.lang.StringUtils
import org.springframework.dao.DataIntegrityViolationException

import edu.banda.coel.domain.service.NetworkService
import edu.banda.coel.web.BaseDomainController

import com.banda.network.domain.NetworkPerformance
import com.banda.network.domain.NetworkSimulationConfig

import edu.banda.coel.task.network.NetworkPerformanceEvaluateTask

import com.banda.core.util.ConversionUtil
import com.banda.network.domain.Network

import edu.banda.coel.web.ChartData
import edu.banda.coel.web.ChartData.SeriesGroup
import grails.converters.JSON

import com.banda.core.util.ParseUtil
	
class NetworkPerformanceController extends BaseDomainController {

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

		def networkPerformanceEvaluationTaskInstance = new NetworkPerformanceEvaluateTask()
		networkPerformanceEvaluationTaskInstance.repetitions = 10000
		networkPerformanceEvaluationTaskInstance.runTime = 300
		[instance: networkPerformanceEvaluationTaskInstance, networks : networks, actionSeries : actionSeries, evaluations : evaluations]
	}

	def show(Long id) {
		def networkPerformanceInstance = getSafe(id)
		if (!networkPerformanceInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'networkPerformance.label', default: 'Network Performance'), id])
			redirect(action: "list")
			return
		}

		[instance: networkPerformanceInstance]
	}

	def runPerformanceEvaluation() {
		def networkPerformanceTaskInstance = new NetworkPerformanceEvaluateTask()
		bindData(networkPerformanceTaskInstance, params, [])
		if (params.actionSeriesIds) {
			def actionSeriesIds = ConversionUtil.convertCollection(Long.class, params.list('actionSeriesIds'), "Action series id")
			networkPerformanceTaskInstance.setNetworkActionSeriesIds(actionSeriesIds)
		}
		if (params.evaluationIds) {
			def evaluationIds = ConversionUtil.convertCollection(Long.class, params.list('evaluationIds'), "Network evaluation id")
			networkPerformanceTaskInstance.setNetworkEvaluationIds(evaluationIds)
		}

		// sim config
		if (params.fpDetectorPeriodicity) {
			def simConfig = new NetworkSimulationConfig()
			simConfig.fixedPointDetectionPeriodicity = params.double('fpDetectorPeriodicity') 
			networkPerformanceTaskInstance.simulationConfig = simConfig
		}

		networkService.runPerformanceEvaluation(networkPerformanceTaskInstance)
		render "success"
	}

	def exportAsCSV() {
		if (!params.ids) {
			flash.message = "No rows selected"
			redirect(action: "list")
		}
		def evaluatedPerformanceIds = ParseUtil.parseArray(params.ids, Long.class, "Network performance id", ",")
		StringBuilder sb = new StringBuilder()
		evaluatedPerformanceIds.each { id ->
			def networkPerformanceInstance = getSafe(id)

			sb.append(networkPerformanceInstance.interactionSeries.name)
			sb.append(',')
			sb.append(id)
			sb.append(',')
			sb.append(networkPerformanceInstance.interactionSeries.id)
			sb.append(',')
			sb.append(StringUtils.join(networkPerformanceInstance.result.mean, ','))
			sb.append(',')
			sb.append(StringUtils.join(networkPerformanceInstance.result.min, ','))
			sb.append(',')
			sb.append(StringUtils.join(networkPerformanceInstance.result.max, ','))
			sb.append(',')
			sb.append(StringUtils.join(networkPerformanceInstance.result.standardDeviation, ','))
			sb.append('\n')
		}
		def evaluatedPerformanceMinId = evaluatedPerformanceIds[evaluatedPerformanceIds.size - 1]
		def evaluatedPerformanceMaxId = evaluatedPerformanceIds[0]
        response.setHeader("Content-disposition", "attachment; filename=" + "netPerformance_${evaluatedPerformanceMinId}-${evaluatedPerformanceMaxId}.csv");
        render(contentType: "text/csv", text: sb.toString());
	}
}