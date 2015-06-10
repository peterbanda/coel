package com.banda.chemistry.domain

import com.banda.core.util.ConversionUtil
import com.banda.core.util.ParseUtil
import edu.banda.coel.domain.service.ArtificialChemistryService
import edu.banda.coel.server.service.impl.ArtificialChemistryServiceAsyncHelper
import edu.banda.coel.task.chemistry.AcPerformanceEvaluateTask
import edu.banda.coel.web.AcDependentPerformanceData
import edu.banda.coel.web.BaseDomainController
import edu.banda.coel.web.ChartData
import edu.banda.coel.web.ChartData.SeriesGroup
import edu.banda.coel.web.ChemistryCommonService
import grails.converters.JSON
import org.apache.commons.lang.StringUtils

class AcEvaluatedPerformanceController extends BaseDomainController {

	static allowedMethods = [edit: "", save: "", update: ""]  // can't edit, save or update

	def ArtificialChemistryServiceAsyncHelper artificialChemistryServiceAsyncHelper
	def ArtificialChemistryService artificialChemistryService
	def ChemistryCommonService chemistryCommonService

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','timeCreated','compartment','actionSeries','evaluation']
		super.list(max)
	}

	def create() {
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','label']
		params.sort = 'id'
		params.order = 'desc'

		def compartments = AcCompartment.listWithParamsAndProjections(params)

		params.projections = ['id','name']
		def simulationConfigs = AcSimulationConfig.listWithParamsAndProjections(params)

		def instance = new AcPerformanceEvaluateTask()
		instance.repetitions = 10000
		instance.runTime = 200100
		instance.simulationConfig =  AcSimulationConfig.get(defaultSimConfig)
		[instance: instance, compartments : compartments, simulationConfigs : simulationConfigs]
	}

	def show(Long id) {
		def acEvaluatedPerformanceInstance = getWithProjections(id, ['id','createdBy','timeCreated','compartment','simulationConfig','actionSeries','evaluation','repetitions','length'])
		if (!acEvaluatedPerformanceInstance) {
			handleObjectNotFound(id)
			return
		}

		[instance: acEvaluatedPerformanceInstance, chartData : ''] // getChartData(acEvaluatedPerformanceInstance)
	}

	def runPerformanceEvaluation() {
		def acEvaluatedPerformanceTaskInstance = new AcPerformanceEvaluateTask()
		bindData(acEvaluatedPerformanceTaskInstance, params, [])
		if (params.actionSeriesIds) {
			def actionSeriesIds = ConversionUtil.convertCollection(Long.class, params.list('actionSeriesIds'), "Action series id")
			acEvaluatedPerformanceTaskInstance.setActionSeriesIds(actionSeriesIds)
		}
		if (params.evaluationIds) {
			def evaluationIds = ConversionUtil.convertCollection(Long.class, params.list('evaluationIds'), "AC evaluation id")
			acEvaluatedPerformanceTaskInstance.setAcEvaluationIds(evaluationIds)
		}
		if (acEvaluatedPerformanceTaskInstance.async)
			artificialChemistryServiceAsyncHelper.runPerformanceEvaluation(acEvaluatedPerformanceTaskInstance)
		else
			artificialChemistryService.runPerformanceEvaluation(acEvaluatedPerformanceTaskInstance)
		render "success"
	}

	def exportAsCSV() {
		if (!params.ids) {
			flash.message = "No rows selected"
			redirect(action: "list")
		}
		def evaluatedPerformanceIds = ParseUtil.parseArray(params.ids, Long.class, "Evaluated performance id", ",")
		StringBuilder sb = new StringBuilder()
		evaluatedPerformanceIds.each { id ->
			def acEvaluatedPerformanceInstance = getSafe(id)

			sb.append(acEvaluatedPerformanceInstance.actionSeries.name)
			sb.append(',')
			sb.append(id)
			sb.append(',')
			sb.append(acEvaluatedPerformanceInstance.actionSeries.id)
			sb.append(',')
			sb.append(StringUtils.join(acEvaluatedPerformanceInstance.getAveragedCorrectRates(), ','))
			sb.append('\n')
		}
		def evaluatedPerformanceMinId = evaluatedPerformanceIds[evaluatedPerformanceIds.size - 1]
		def evaluatedPerformanceMaxId = evaluatedPerformanceIds[0]
        response.setHeader("Content-disposition", "attachment; filename=" + "acEvaluatedPerformance_${evaluatedPerformanceMinId}-${evaluatedPerformanceMaxId}.csv");
        render(contentType: "text/csv", text: sb.toString());
	}

	def getChartDataForId() {
		def acEvaluatedPerformanceInstance = getWithProjections(params.long('id'), ['averagedCorrectRates','actionSeries'])
		def averages = acEvaluatedPerformanceInstance.getAveragedCorrectRates().takeWhile {!it.isNaN() && !it.isInfinite()}

		def chartData = new ChartData()
		chartData.title = "Performance"
		chartData.xAxisCaption = "Step"
		chartData.yAxisCaption = "Value"

		def seriesGroup = new SeriesGroup()
		seriesGroup.groupLabel = "All"
		chartData.seriesGroups.add(seriesGroup)
		chartData.xSteps = 0..averages.size() - 1

		def label = StringUtils.abbreviate(acEvaluatedPerformanceInstance.actionSeries.name, 50)
		seriesGroup.labels.add(label)
		chartData.series.add(Arrays.asList(averages))

		JSON.use("deep") {
			def chartDataJSON = chartData as JSON
			render chartDataJSON
		}
	}

	def getChartData(AcEvaluatedPerformance acEvaluatedPerformanceInstance) {
		def averages = acEvaluatedPerformanceInstance.getAveragedCorrectRates().takeWhile {!it.isNaN() && !it.isInfinite()}

		def chartData = new ChartData()
		chartData.title = "Performance"
		chartData.xAxisCaption = "Step"
		chartData.yAxisCaption = "Value"

		def seriesGroup = new SeriesGroup()
		seriesGroup.groupLabel = "All"
		chartData.seriesGroups.add(seriesGroup)
		chartData.xSteps = 0..averages.size() - 1

		def label = StringUtils.abbreviate(acEvaluatedPerformanceInstance.actionSeries.name, 65)
		seriesGroup.labels.add(label)
		chartData.series.add(Arrays.asList(averages))

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

		def evaluatedPerformanceIds = ParseUtil.parseArray(params.ids, Long.class, "Evaluated performance id", ",") 

		def chartData = new ChartData()
		chartData.title = "Performance"
		chartData.xAxisCaption = "Step"
		chartData.yAxisCaption = "Success Rate"

		def seriesGroup = new SeriesGroup()
		seriesGroup.groupLabel = "All"
		chartData.seriesGroups.add(seriesGroup)

		def ids = []

		evaluatedPerformanceIds.each { id ->
			def acEvaluatedPerformanceInstance = getSafe(id)
			def averages = acEvaluatedPerformanceInstance.getAveragedCorrectRates().takeWhile {!it.isNaN() && !it.isInfinite()}
			if (!chartData.xSteps) {
				chartData.xSteps = 0..averages.size() - 1
			}

			def label = StringUtils.abbreviate(acEvaluatedPerformanceInstance.actionSeries.name, 65) + " - " +  acEvaluatedPerformanceInstance.evaluation.name 
			seriesGroup.labels.add(label)
			chartData.series.add(Arrays.asList(averages))
			ids.add(id)
		}

		JSON.use("deep") {
			def chartDataJSON = chartData as JSON
			[ids : ids, chartData : chartDataJSON]
		}
	}

	def getCompartmentDependentData(Long id) {
		def compartment = AcCompartment.get(id)

		def speciesSets = chemistryCommonService.getThisAndParentSpeciesSets(compartment.speciesSet)
		def interactionSeries = AcInteractionSeries.findAll("from AcInteractionSeries as a where a.speciesSet IN (:speciesSets) order by a.id DESC", [speciesSets : speciesSets])
		def evaluations = AcEvaluation.findAll("from AcEvaluation as a where a.translationSeries.speciesSet IN (:speciesSets) order by a.id DESC", [speciesSets : speciesSets])

		def data = new AcDependentPerformanceData()
		data.interactionSeries = interactionSeries.collectEntries{ [it.id, it.id + " : " + it.name] }
		data.evaluations = evaluations.collectEntries{ [it.id, it.id + " : " + it.name] }

		data.interactionSeries = data.interactionSeries.sort { -it.key }
		data.evaluations = data.evaluations.sort { -it.key }

		render data as JSON
	}
}