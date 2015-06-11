package com.banda.chemistry.domain

import com.banda.chemistry.business.ArtificialChemistryUtil
import com.banda.chemistry.domain.AcItemHistory.AcItemHistoryLabelComparator
import com.banda.core.domain.ComponentRunTrace
import com.banda.core.util.ObjectUtil
import edu.banda.coel.domain.service.ArtificialChemistryService
import edu.banda.coel.task.chemistry.AcRunAndTranslateTask
import edu.banda.coel.task.chemistry.AcRunTask
import edu.banda.coel.web.AcDependentRunData
import edu.banda.coel.web.BaseController
import edu.banda.coel.web.ChartData
import edu.banda.coel.web.ChartData.SeriesGroup
import edu.banda.coel.web.ChemistryCommonService
import grails.converters.JSON

class AcRunController extends BaseController {

	static navigationScope = "none"

	def ArtificialChemistryService artificialChemistryService
	def acUtil = ArtificialChemistryUtil.getInstance()
	def ChemistryCommonService chemistryCommonService

	def index = {
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','label']
		params.sort = 'id'
		params.order = 'desc'

		def compartments = AcCompartment.listWithParamsAndProjections(params)
		params.projections = ['id','name']
		def translationSeries = AcTranslationSeries.listWithParamsAndProjections(params)

		def instance = new AcRunTask()
		instance.properties = params
		if (!instance.runTime)
			instance.runTime = 1000

		if (compartments.isEmpty()) {
			flash.message = "No compartments found. You must first define one."
		}

		def simulationConfigs = AcSimulationConfig.listWithProjections(['id', 'name'])
		instance.simulationConfig =  AcSimulationConfig.get(defaultSimConfig)
		[instance: instance, compartments : compartments, simulationConfigs : simulationConfigs, translationSeries : translationSeries]
	}

	def getCompartmentDependentData(Long id) {
		def selectedCompartment = AcCompartment.get(id)

		if (selectedCompartment) {
			def speciesSets = chemistryCommonService.getThisAndParentSpeciesSets(selectedCompartment.speciesSet)
			def actionSeries = AcInteractionSeries.findAll("from AcInteractionSeries as a where a.speciesSet IN (:speciesSets) order by a.id DESC", [speciesSets : speciesSets])
			def translationSeries = AcTranslationSeries.findAll("from AcTranslationSeries as a where a.speciesSet IN (:speciesSets) order by a.id DESC", [speciesSets : speciesSets])

			def acDependentRunData = new AcDependentRunData()
			acDependentRunData.speciesSetId = selectedCompartment.speciesSet.id
			acDependentRunData.actionSeries = [ : ]
			acDependentRunData.translationSeries = [ : ]

//			acDependentRunData.actionSeries = new LinkedHashMap()
//			acDependentRunData.translationSeries = new LinkedHashMap()

			actionSeries.each{ acDependentRunData.actionSeries.putAt(it.id, it.id + " : " + it.name) }
			translationSeries.each{  acDependentRunData.translationSeries.putAt(it.id, it.id + " : " + it.name) }

//			acDependentRunData.actionSeries = acDependentRunData.actionSeries.sort { -it.key }
//			acDependentRunData.translationSeries = acDependentRunData.translationSeries.sort { -it.key }

			render acDependentRunData as JSON
		}
	}

	def runSimulationAsync() {
		AcRunTask acRunTask = new AcRunTask()
		acRunTask.properties = params
		acRunTask.runOnGrid = false   // run locally

		if (!acRunTask.compartment?.id) {
			def message = message(code: 'default.null.message', args: ['Compartment', 'AC Run Task'])
			acRunTask.errors.rejectValue("compartment",null,message)
		}
		if (!acRunTask.simulationConfig?.id) {
			def message = message(code: 'default.null.message', args: ['Simulation Config', 'AC Run Task'])
			acRunTask.errors.rejectValue("simulationConfig",null,message)
		}
		if (!acRunTask.runTime) {
			def message = message(code: 'default.null.message', args: ['Run Time', 'AC Run Task'])
			acRunTask.errors.rejectValue("runTime",null,message)
		}
		if (!acRunTask.actionSeries?.id) {
			def message = message(code: 'default.null.message', args: ['Interaction Series', 'AC Run Task'])
			acRunTask.errors.rejectValue("actionSeries",null,message)
		}

		if (acRunTask.hasErrors()) {
			def errorsJSON = acRunTask.errors as JSON
			render errorsJSON
			return
		}

		def runTrace;
		def translatedRun;
		UUID token = UUID.randomUUID();
		if (params.translationSeriesId) {
			AcRunAndTranslateTask acRunAndTranslateTask = new AcRunAndTranslateTask()
			acRunAndTranslateTask.runOnGrid = false   // run locally
			acRunAndTranslateTask.setRunTaskDefinition(acRunTask)
			acRunAndTranslateTask.setTranslationSeriesId(params.long('translationSeriesId'))
			acRunAndTranslateTask.setStoreRunTrace(params.storeRunTrace != null)
			session["acRunResponse.$token"] = artificialChemistryService.runAndTranslateAsync(acRunAndTranslateTask)
		} else {
			session["acRunResponse.$token"] = artificialChemistryService.runSimulationAsync(acRunTask)
		}

		render token
	}

	def checkRunResponse() {
		def token = params.token
		if (!token) {
			response.status = 404
			render "No token"
			return
		}

		def acRunResponse = session["acRunResponse.$token"]
		if (!acRunResponse || !acRunResponse.isDone()) {
			response.status = 404
			render "Need to wait"
			return
		}

		session["acRunResponse.$token"] = null
		def runObject = ObjectUtil.getFirst(acRunResponse.get())
		def chartDatas = []

		if (runObject instanceof AcTranslatedRun) {
			if (runObject.acRunTrace)
				chartDatas.add(
					createAcRunChartData(runObject.acRunTrace))
 
			chartDatas.add(
				createAcTranslatedRunChartData(runObject))			
		} else
			chartDatas.add(
				createAcRunChartData(runObject))

		JSON.use("deep") {
			def chartDatasJSON = chartDatas as JSON
			println "AC run chart(s) sent to client as JSON"
			response.status = 200
			render chartDatasJSON
		}
	}

	private def createAcRunChartData(ComponentRunTrace runTrace) {
		def chartData = new ChartData()
		chartData.title = "Chemistry"
		chartData.xAxisCaption = "Time"
		chartData.yAxisCaption = "Concentration"
		chartData.xSteps = runTrace.timeSteps

		runTrace.sortComponentHistoriesBy(acUtil.compartmentSpeciesComparator)
		def lastCompartment = null
		def lastSeriesGroup = null

		runTrace.componentHistories().each { compartmentHistory ->
			def compartment = compartmentHistory.component().getFirst()

			def species = compartmentHistory.component().getSecond()
			if (lastCompartment != compartment) {
				def seriesGroup = new SeriesGroup()
				seriesGroup.groupLabel = compartment.label
				chartData.seriesGroups.add(seriesGroup)

				lastCompartment = compartment
				lastSeriesGroup = seriesGroup
			}
			lastSeriesGroup.labels.add(species.label)
//	   		def history = compartmentHistory.history().takeWhile {!it.isNaN() && !it.isInfinite()}
// 			chartData.series.add(history)
			chartData.series.add(compartmentHistory.history())
		}
		chartData
	}

	private def createAcTranslatedRunChartData(AcTranslatedRun acTranslatedRun) {
		def chartData = new ChartData()
		chartData.title = "Translated Chemistry"
		chartData.xAxisCaption = "Step"
		chartData.yAxisCaption = "Value"
		chartData.xSteps = 0..ObjectUtil.getFirst(acTranslatedRun.itemHistories).sequence.size() - 1

		def compartmentSpeciesHistoriesMap = [ : ]

		def itemHistories = new ArrayList(acTranslatedRun.itemHistories)
		Collections.sort(itemHistories, new AcItemHistoryLabelComparator())

		def seriesGroup = new SeriesGroup()
		seriesGroup.groupLabel = "All"
		chartData.seriesGroups.add(seriesGroup)

		for (itemHistory in itemHistories) {
			seriesGroup.labels.add(itemHistory.label)
			def sequence = itemHistory.sequence.takeWhile {!it.isNaN() && !it.isInfinite()}
			chartData.series.add(sequence)
		}
		chartData
	}
}