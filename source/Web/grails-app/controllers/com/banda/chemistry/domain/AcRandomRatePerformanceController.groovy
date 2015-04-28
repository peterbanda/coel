package com.banda.chemistry.domain

import java.util.Collection;

import org.apache.commons.lang.StringUtils
import org.springframework.dao.DataIntegrityViolationException

import edu.banda.coel.domain.service.ArtificialChemistryService
import edu.banda.coel.web.BaseDomainController

import com.banda.chemistry.domain.AcRandomRatePerformance;

import edu.banda.coel.task.chemistry.AcRandomRatePerformanceEvaluateTask;

import com.banda.core.util.ConversionUtil

import grails.converters.JSON
import edu.banda.coel.web.AcDependentPerformanceData
	
class AcRandomRatePerformanceController extends BaseDomainController {

	static allowedMethods = [edit: "", save: "", update: ""]  // can't edit, save or update

	def ArtificialChemistryService artificialChemistryService

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
		def actionSeries = AcInteractionSeries.listWithParamsAndProjections(params)
		def evaluations = AcEvaluation.listWithParamsAndProjections(params)
		def simulationConfigs = AcSimulationConfig.listWithParamsAndProjections(params)		

		def instance = new AcRandomRatePerformanceEvaluateTask()
		instance.repetitions = 10
		instance.randomRateGenerationNum = 10000
		instance.runTime = 120100
		instance.simulationConfig =  AcSimulationConfig.get(defaultSimConfig)
		[instance: instance, compartments : compartments, simulationConfigs : simulationConfigs, actionSeries : actionSeries, evaluations : evaluations]
	}

	def runPerformanceEvaluation() {
		def acRandomRatePerformanceTaskInstance = new AcRandomRatePerformanceEvaluateTask()
		bindData(acRandomRatePerformanceTaskInstance, params, [])
		if (params.actionSeriesIds) {
			def actionSeriesIds = new HashSet()
			params.actionSeriesIds.each { actionSeriesStringId -> 
				actionSeriesIds.add(ConversionUtil.convertToLong(actionSeriesStringId, "Action series id"))
			}
			acRandomRatePerformanceTaskInstance.setActionSeriesIds(actionSeriesIds)
		}
		if (params.evaluationIds) {
			def evaluationIds = ConversionUtil.convertCollection(Long.class, params.list('evaluationIds'), "AC evaluation id")
			acRandomRatePerformanceTaskInstance.setAcEvaluationIds(evaluationIds)
		}
		if (params.rateConstantTypeBoundIds) {
			def rateConstantTypeBoundIds = new HashSet()
			params.rateConstantTypeBoundIds.each { rateConstantTypeBoundId ->
				rateConstantTypeBoundIds.add(ConversionUtil.convertToLong(rateConstantTypeBoundId, "Rate Constant Type Bound"))
			}
			acRandomRatePerformanceTaskInstance.setRateConstantTypeBoundIds(rateConstantTypeBoundIds)
		}
		artificialChemistryService.runRandomRatePerformanceEvaluation(acRandomRatePerformanceTaskInstance)
		render "success"
	}

	def exportAsCSV() {
		if (!params.randomRatePerformanceIds) {
			flash.message = "No random rate performances defined!"
			render "failure"
			return
		}
		def randomRatePerformanceIds = ConversionUtil.convertArray(Long.class, params.randomRatePerformanceIds, "Random rate performance id")
		StringBuilder sb = new StringBuilder()
		randomRatePerformanceIds.each { id ->
			def acRandomRatePerformanceInstance = getSafe(id)
			sb.append(id)
			sb.append(',')
			sb.append(acRandomRatePerformanceInstance.actionSeries.id)
			sb.append(',')
			sb.append(StringUtils.join(acRandomRatePerformanceInstance.getAveragedCorrectRates(), ','))
			sb.append('\n')
		}
		def performanceMinId = randomRatePerformanceIds[0]
		def performanceMaxId = randomRatePerformanceIds[randomRatePerformanceIds.length - 1]
        response.setHeader("Content-disposition", "attachment; filename=" + "acRandomRatePerformance_${performanceMinId}-${performanceMaxId}.csv");
        render(contentType: "text/csv", text: sb.toString());
	}

	def getCompartmentDependentData(Long id) {
		def compartment = AcCompartment.get(id)

		def speciesSets = getThisAndParentSpeciesSets(compartment.speciesSet)
		def interactionSeries = AcInteractionSeries.findAll("from AcInteractionSeries as a where a.speciesSet IN (:speciesSets) order by a.id DESC", [speciesSets : speciesSets])
		def evaluations = AcEvaluation.findAll("from AcEvaluation as a where a.translationSeries.speciesSet IN (:speciesSets) order by a.id DESC", [speciesSets : speciesSets])

		def data = new AcDependentPerformanceData()
		data.interactionSeries = interactionSeries.collectEntries{ [it.id, it.id + " : " + it.name] }
		data.evaluations = evaluations.collectEntries{ [it.id, it.id + " : " + it.name] }
		render data as JSON
	}
}