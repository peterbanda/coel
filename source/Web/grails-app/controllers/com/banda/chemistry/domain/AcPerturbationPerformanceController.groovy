package com.banda.chemistry.domain

import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils
import org.hibernate.transform.Transformers
import org.springframework.dao.DataIntegrityViolationException

import edu.banda.coel.task.chemistry.AcPerturbationPerformanceEvaluateTask

import com.banda.math.business.JavaMathUtil
import com.banda.math.domain.StatsType
import com.banda.core.util.ConversionUtil
import com.banda.core.util.ParseUtil

import edu.banda.coel.domain.service.ArtificialChemistryService
import edu.banda.coel.web.BaseDomainController
import grails.converters.JSON
import edu.banda.coel.web.AcDependentPerformanceData

class AcPerturbationPerformanceController extends BaseDomainController {

	static allowedMethods = [edit: "", save: "", update: ""]  // can't edit, save or update

	def ArtificialChemistryService artificialChemistryService

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','timeCreated','compartment','actionSeries','perturbationStrength']
		def listMap = super.list(max)

		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','label']
		params.sort = 'id'
		params.order = 'desc'

		def compartments = AcCompartment.listWithParamsAndProjections(params)
		params.projections = ['id','name']
		def actionSeries = AcInteractionSeries.listWithParamsAndProjections(params)

		listMap << [compartments : compartments, actionSeries : actionSeries]
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

		def instance = new AcPerturbationPerformanceEvaluateTask()
		instance.repetitions = 10
		instance.perturbationNum = 1000
		instance.runTime = 200100
		instance.simulationConfig =  AcSimulationConfig.get(defaultSimConfig)
		[instance: instance, compartments : compartments, simulationConfigs : simulationConfigs, actionSeries : actionSeries, evaluations : evaluations]
	}

	def runPerformanceEvaluation() {
		def acPerturbationPerformanceTaskInstance = new AcPerturbationPerformanceEvaluateTask()
		bindData(acPerturbationPerformanceTaskInstance, params, [])
		if (params.actionSeriesIds) {
			def actionSeriesIds = ConversionUtil.convertCollection(Long.class, params.list('actionSeriesIds'), "Action series id")
			acPerturbationPerformanceTaskInstance.setActionSeriesIds(actionSeriesIds)
		}
		if (params.evaluationIds) {
			def evaluationIds = ConversionUtil.convertCollection(Long.class, params.list('evaluationIds'), "AC evaluation id")
			acPerturbationPerformanceTaskInstance.setAcEvaluationIds(evaluationIds)
		}
		def perturbationStrengths = ParseUtil.parseArray(params.perturbationStrengths, Double.class, "Perturbation Strength", ",")

		acPerturbationPerformanceTaskInstance.setPerturbationStrengths(perturbationStrengths)
		artificialChemistryService.runPerturbationPerformanceEvaluation(acPerturbationPerformanceTaskInstance)
		render "success"
	}

	def exportAsCSV() {
		if (!params.perturbationPerformanceIds && !params.actionSeriesIds) {
			flash.message = "No perturbation performances, nor interaction series (with ac) defined!"
			render "failure"
			return
		}
		
		def perturbationPerformances = null
		if (params.actionSeriesIds) {
			def actionSeriesIds = ConversionUtil.convertCollection(Long.class, params.list('actionSeriesIds'), "Action series id")
			def acId = params.long('acId')

			def actionSeries = new ArrayList<AcInteractionSeries>()
			actionSeriesIds.each { actionSeriesStringId ->
				actionSeries.add(new AcInteractionSeries(actionSeriesStringId))
			}
			def ac = new ArtificialChemistry()
			ac.id = acId

			def c = AcPerturbationPerformance.createCriteria()
			perturbationPerformances = c.listDistinct {
				and {
					'in'("actionSeries", actionSeries)
					eq("ac",ac)
				}
				order("id", "asc")
			}
		} else {
			def perturbationPerformanceIds = ConversionUtil.convertCollection(Long.class, params.list('perturbationPerformanceIds'), "Perturbation performance id")
			perturbationPerformances = getSafe(perturbationPerformanceIds)
		}

		def StringBuilder sb = new StringBuilder()
		def performanceMinId = perturbationPerformances[0].id
		def performanceMaxId = perturbationPerformances[perturbationPerformances.size - 1].id
		def filename = "acPerturbationPerformance_${performanceMinId}-${performanceMaxId}.csv"
		if (!params.statsType) {
			perturbationPerformances.each { acPerturbationPerformanceInstance ->
				sb.append(acPerturbationPerformanceInstance.id)
				sb.append(',')
				sb.append(acPerturbationPerformanceInstance.actionSeries.id)
				sb.append(',')
				sb.append(acPerturbationPerformanceInstance.perturbationStrength)
				sb.append(',')
				sb.append(StringUtils.join(acPerturbationPerformanceInstance.getAveragedCorrectRates(), ','))
				sb.append('\n')
			}
		} else {
			def StatsType statsType = params.statsType 
			def Map<Double, Collection<Collection<Double>>> perturbationStrengthResultsMaps = new HashMap<Double, Collection<Collection<Double>>>()			
			perturbationPerformances.each { acPerturbationPerformanceInstance ->
				def oneResults = Arrays.asList(acPerturbationPerformanceInstance.getAveragedCorrectRates())
				def results = perturbationStrengthResultsMaps.get(acPerturbationPerformanceInstance.perturbationStrength)
				if (!results) {
					results = new ArrayList<Collection<Double>>()
					perturbationStrengthResultsMaps.put(acPerturbationPerformanceInstance.perturbationStrength, results)
				}
				results.add(oneResults)
			}
			List<Double> sortedPerturbationStrengths = new ArrayList(perturbationStrengthResultsMaps.keySet())
			Collections.sort(sortedPerturbationStrengths);
			for (Double perturbationStrength : sortedPerturbationStrengths) {
				def results = JavaMathUtil.calcStatsBySecond(perturbationStrengthResultsMaps.get(perturbationStrength))
				def projResults = JavaMathUtil.projStats(statsType, results)

				sb.append(perturbationStrength)
				sb.append(',')
				sb.append(StringUtils.join(projResults, ','))
				sb.append('\n')
			}
			filename = "acPerturbationPerformance_${statsType}_${performanceMinId}-${performanceMaxId}.csv"
		}

        response.setHeader("Content-disposition", "attachment; filename=" + filename);
        render(contentType: "text/csv", text: sb.toString());
	}

	def getAcDependentData(Long id) {
		def ac = ArtificialChemistry.get(id)

		def speciesSets = getThisAndParentSpeciesSets(ac.speciesSet)
		def interactionSeries = AcInteractionSeries.findAll("from AcInteractionSeries as a where a.speciesSet IN (:speciesSets) order by a.id DESC", [speciesSets : speciesSets])
		def evaluations = AcEvaluation.findAll("from AcEvaluation as a where a.translationSeries.speciesSet IN (:speciesSets) order by a.id DESC", [speciesSets : speciesSets])

		def data = new AcDependentPerformanceData()
		data.interactionSeries = interactionSeries.collectEntries{ [it.id, it.id + " : " + it.name] }
		data.evaluations = evaluations.collectEntries{ [it.id, it.id + " : " + it.name] }
		render data as JSON
	}
}