package com.banda.math.domain.evo

import java.util.HashSet

import org.apache.commons.lang.StringUtils
import org.springframework.dao.DataIntegrityViolationException

import com.banda.core.util.ObjectUtil
import com.banda.math.task.EvoPopulationContentStoreOption
import com.banda.math.task.EvoPopulationSelection
import com.banda.math.task.EvoRunTask
import com.banda.chemistry.domain.AcCompartment
import com.banda.chemistry.domain.AcInteractionSeries
import com.banda.chemistry.domain.AcReactionSet
import com.banda.chemistry.domain.AcReaction.ReactionDirection
import com.banda.chemistry.business.AcRateConstantUtil
import com.banda.chemistry.business.AcReplicator
import com.banda.math.business.evo.EvolutionUtil
import com.banda.network.domain.NetworkFunction
import com.banda.network.domain.Network

import edu.banda.coel.domain.evo.EvoNetworkTask
import edu.banda.coel.domain.evo.EvoAcRateConstantTask
import edu.banda.coel.domain.evo.EvoAcInteractionSeriesTask

import com.banda.function.business.FunctionFactory

import edu.banda.coel.domain.service.EvolutionService

import edu.banda.coel.business.evo.fitness.EvoChromosomeAcInteractionSeriesConverter
import edu.banda.coel.web.BaseDomainController
import edu.banda.coel.web.ChartData
import edu.banda.coel.web.ChartData.SeriesGroup
import grails.converters.JSON

import com.banda.core.util.ParseUtil
import com.banda.math.domain.StatsType
import com.banda.math.business.JavaMathUtil

class EvoRunController extends BaseDomainController {

	static allowedMethods = [edit: "", save: "", update: ""]  // edit, save, and update not allowed

	def acRateUtil = AcRateConstantUtil.instance
	def functionFactory = new FunctionFactory()
	def replicator = AcReplicator.instance

	def EvolutionService evolutionService

	def index() {
		redirect(action: "list", params: params)
	}

//	def list(Integer max) {
//		params.projections = ['id','timeCreated','evoTask','populations']
//		super.list(max)
//	}

	def filter() {
		if (!getCurrentUser().id.equals(1l)) {
			params.filter.evoTask.taskType = "Network"
			params.filter.op.evoTask.taskType = "NotInList"
		}
		super.filter()
	}

	def create() {
		def evoRunTaskInstance = new EvoRunTask(params)

		if (!evoRunTaskInstance.populationContentStoreOption)
			evoRunTaskInstance.populationContentStoreOption = EvoPopulationContentStoreOption.BestChromosome
		if (!evoRunTaskInstance.populationSelection)
			evoRunTaskInstance.populationSelection = EvoPopulationSelection.All
		if (!evoRunTaskInstance.autoSave)
			evoRunTaskInstance.autoSave = true
		if (!evoRunTaskInstance.jobsInSequenceNum)
			evoRunTaskInstance.jobsInSequenceNum = 4
		if (!evoRunTaskInstance.maxJobsInParallelNum)
			evoRunTaskInstance.maxJobsInParallelNum = 20000

		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'

		def evoTasks = EvoTask.listWithParamsAndProjections(params)
		params.projections = ['id','timeCreated']
		def evoRuns = EvoRun.listWithParamsAndProjections(params)

		[instance: evoRunTaskInstance, evoTasks : evoTasks, evoRuns : evoRuns, initChromosomeId : params.initChromosomeId]
	}

	def show(Long id) {
		def result = super.show(id)
		result << [chartData : getChartData(result.instance)]
	}

	def runEvolution() {
		def evoRunTaskInstance = new EvoRunTask()
		bindData(evoRunTaskInstance, params, [exclude: '_autoSave'])
		evoRunTaskInstance.createdBy = currentUserOrError

		if (params.initChromosomeId) {
			def initChromosome = Chromosome.get(params.long('initChromosomeId'))
			if (!initChromosome) {
				flash.message = "Chromosome with id " + params.initChromosomeId + " doesn't exist."
				render "failure"
				return
			}
			evoRunTaskInstance.initChromosome = initChromosome
		}

		evolutionService.evolve(evoRunTaskInstance)
		render "success"
	}

	def exportFitnessOnly(Long id) {
		def evoRunInstance = getSafe(id)
		if (!evoRunInstance) {
			flash.message = message(code: 'default.not.found.message', args: [doClazzMessageLabel, id])
			redirect(action: "list")
			return
		}
	
		def generations = new ArrayList<Integer>()
		def minFitnesses = new ArrayList<Double>()
		def meanFitnesses = new ArrayList<Double>()
		def maxFitnesses = new ArrayList<Double>()

		evoRunInstance.populations.each{
			generations.add(it.generation)
			minFitnesses.add(it.minFitness)
			meanFitnesses.add(it.meanFitness)
			if (it.maxFitness)
				maxFitnesses.add(it.maxFitness)
			else 
				maxFitnesses.add(it.getBestOrLastChromosome().fitness)}

		def StringBuilder sb = new StringBuilder()

		sb.append(StringUtils.join(generations, ','))
		sb.append('\n')
		sb.append(StringUtils.join(minFitnesses, ','))
		sb.append('\n')
		sb.append(StringUtils.join(meanFitnesses, ','))
		sb.append('\n')
		sb.append(StringUtils.join(maxFitnesses, ','))
		sb.append('\n')

		response.setHeader("Content-disposition", "attachment; filename=" + "evo_" + id + ".csv");
		render(contentType: "text/csv", text: sb.toString());
	}

	def exportWithBestChromosomes(Long id) {
		def evoRunInstance = getSafe(id)
		if (!evoRunInstance) {
			flash.message = message(code: 'default.not.found.message', args: [doClazzMessageLabel, id])
			redirect(action: "list")
			return
		}
	
		def generations = new ArrayList<Integer>()
		def minFitnesses = new ArrayList<Double>()
		def meanFitnesses = new ArrayList<Double>()
		def maxFitnesses = new ArrayList<Double>()
		def codes = new ArrayList<ArrayList<Double>>()

		def codeSize = ObjectUtil.getFirst(evoRunInstance.populations).getBestOrLastChromosome().getCodeSize()
		for (int i = 0; i < codeSize; i++) {
			codes.add(new ArrayList<Double>());
		}

		evoRunInstance.populations.each{
			generations.add(it.generation)
			minFitnesses.add(it.minFitness)
			meanFitnesses.add(it.meanFitness)
			if (it.maxFitness)
				maxFitnesses.add(it.maxFitness)
			else
				maxFitnesses.add(it.getBestOrLastChromosome().fitness)
			int index = 0
			for (Double codeElement : it.getBestOrLastChromosome().code) {
				codes.get(index).add(codeElement)
				index++
			}}

		def StringBuilder sb = new StringBuilder()

		sb.append(StringUtils.join(generations, ','))
		sb.append('\n')
		sb.append(StringUtils.join(minFitnesses, ','))
		sb.append('\n')
		sb.append(StringUtils.join(meanFitnesses, ','))
		sb.append('\n')
		sb.append(StringUtils.join(maxFitnesses, ','))
		sb.append('\n')
		codes.each{
			sb.append(StringUtils.join(it, ','))
			sb.append('\n')
		}

		response.setHeader("Content-disposition", "attachment; filename=" + "evo_with_code_" + id + ".csv");
		render(contentType: "text/csv", text: sb.toString());
	}

	def importLastBestChromosome(Long id) {
		def evoRunInstance = getSafe(id)
		def code = evoRunInstance.lastPopulation?.bestOrLastChromosome?.code
		def evoTask = evoRunInstance.evoTask
		if (evoTask instanceof EvoAcRateConstantTask) {
			importRateConstants(code, evoTask)
		} else if (evoTask instanceof EvoAcInteractionSeriesTask) {
			if (params.createNewInteractionSeries)
				importAssignmentValuesToClone(code, evoTask, id)
			else
				importAssignmentValues(code, evoTask)
		} else if (evoTask instanceof EvoNetworkTask) {
			def networkFunction = evoTask.network.function

			if (params.actionSpec == "Import")
				importTableOutputs(code, networkFunction)
			else if (params.actionSpec == "New function") {
				def newNetworkFunction = createNewNetworkFunction(code, networkFunction, evoRunInstance)
				if (newNetworkFunction.save(flush: true)) {
					flash.message = message(code: 'default.created.message', args: [message(code: 'networkFunction.label', default: 'Network Function'), newNetworkFunction.id])
				}
			} else if (params.actionSpec == "New network") {
				def newNetworkFunction = createNewNetworkFunction(code, networkFunction, evoRunInstance)
				def newNetwork = createNewNetwork(evoTask.network, evoRunInstance)
				newNetwork.function = newNetworkFunction
				if (newNetworkFunction.save())
					if (newNetwork.save(flush : true))
						flash.message = message(code: 'default.created.message', args: [message(code: 'network.label', default: 'Network'), newNetwork.id])
			}
		}
		redirect(action: "show", id: id)
	}

	def showMulti() {
		if (!params.ids) {
			flash.message = "No rows selected"
			redirect(action: "list")
			return
		}

		def StatsType statsType = params.statsType
		def evoRunIds = ParseUtil.parseArray(params.ids, Long.class, "Evo Run id", ",")

		def chartData = new ChartData()
		chartData.title = "Evolution (" + statsType.toString() + ")"
		chartData.xAxisCaption = "Generation"
		chartData.yAxisCaption = "Fitness"

		def seriesGroup = new SeriesGroup()
		seriesGroup.groupLabel = "All"
		chartData.seriesGroups.add(seriesGroup)

		def ids = []

		evoRunIds.each { id ->
			def evoRun = getSafe(id)

			if (!chartData.xSteps)
				evoRun.populations.each{
					chartData.xSteps.add(it.generation.toDouble())
				}

			def fitnesses = new ArrayList<Double>()
			chartData.series.add(fitnesses)

			evoRun.populations.each{
				if (statsType == StatsType.Mean)
					fitnesses.add(EvolutionUtil.getMeanFitness(it))
				if (statsType == StatsType.Min)
					fitnesses.add(EvolutionUtil.getWorstFitness(it))
				if (statsType == StatsType.Max)
					fitnesses.add(EvolutionUtil.getBestFitness(it))
			}
	
			seriesGroup.labels.add("" + evoRun.id)
			ids.add(id)
		}

		JSON.use("deep") {
			def chartDataJSON = chartData as JSON
			[ids : ids, chartData : chartDataJSON]
		}
	}

	private def importTableOutputs(Boolean[] tableOutputs, NetworkFunction networkFunction) {
		def transitionTable = functionFactory.createBoolTransitionTable(Arrays.asList(tableOutputs))
		networkFunction.setFunction(transitionTable)
		if (networkFunction.save(flush: true)) {
			flash.message = message(code: 'default.updated.message', args: [message(code: 'networkFunction.label', default: 'Network Function'), networkFunction.id])
		}
	}

	private def createNewNetworkFunction(Boolean[] tableOutputs, NetworkFunction networkFunction, EvoRun evoRun) {
		def transitionTable = functionFactory.createBoolTransitionTable(Arrays.asList(tableOutputs))
		def newNetworkFunction = new NetworkFunction()
		newNetworkFunction.name = networkFunction.name + ", EvoRun " + evoRun.id
		newNetworkFunction.multiComponentUpdaterType = networkFunction.multiComponentUpdaterType
		newNetworkFunction.function = transitionTable
		newNetworkFunction.createdBy = currentUserOrError
		newNetworkFunction
	}

	private def createNewNetwork(Network network, EvoRun evoRun) {
		def newNetwork = new Network()
		newNetwork.name = network.name + ", EvoRun " + evoRun.id
		newNetwork.createdBy = currentUserOrError
		newNetwork.topology = network.topology
		newNetwork.weightSetting = network.weightSetting
		newNetwork
	}

	private def importRateConstants(Double[] rateConstants, EvoAcRateConstantTask evoAcTask) {
		def acCompartmentInstance = evoAcTask.ac.skinCompartment
		acRateUtil.setRateConstants(
			acCompartmentInstance,
			rateConstants,
			ReactionDirection.Both,
			evoAcTask.fixedRateReactions,
			evoAcTask.fixedRateReactionGroups)

		def reactionSets = getReactionSets(acCompartmentInstance)
		reactionSets.each{ reactionSet ->
			// just to make it dirty
			reactionSet.reactions.each{ reaction -> reaction.setForwardRateConstants(reaction.getForwardRateConstants().clone()) }
		}
		if (acCompartmentInstance.save(flush: true)) {
			flash.message = message(code: 'default.updated.message', args: [message(code: 'acCompartment.label', default: 'AcCompartment'), acCompartmentInstance.id])
		}
	}

	private def importAssignmentValues(Double[] assignmentValues, EvoAcInteractionSeriesTask evoTask) {
		def converter = new EvoChromosomeAcInteractionSeriesConverter(evoTask)
		evoTask.getActionSeries().each{ interactionSeries ->
			converter.modify(interactionSeries, assignmentValues)

			if (interactionSeries.save(flush: true)) {
				flash.message = message(code: 'default.updated.message', args: [message(code: 'acInteractionSeries.label', default: 'AcInteractionSeries'), interactionSeries.id])
			}
		}
	}

	private def importAssignmentValuesToClone(Double[] assignmentValues, EvoAcInteractionSeriesTask evoTask, Long evoRunId) {
		def converter = new EvoChromosomeAcInteractionSeriesConverter(evoTask)
		def topMultiActionSeries = evoTask.getActionSeries()
		def actionSeriesCloneMap = [ : ]
		topMultiActionSeries.each{ interactionSeries ->
			def newInteractionSeriesCloneMap = converter.toClone(interactionSeries, assignmentValues)
//			def newInteractionSeriesCloneMap = [ : ]
//			replicator.cloneActionSeriesWithActionsRecursively(interactionSeries, newInteractionSeriesCloneMap)
			newInteractionSeriesCloneMap.values().each{replicator.nullIdAndVersionRecursively(it)}

			def newInteractionSeriesClone = newInteractionSeriesCloneMap.get(interactionSeries)
			resetMetaDataRecursively(newInteractionSeriesClone, evoRunId)
			actionSeriesCloneMap += newInteractionSeriesCloneMap
		}

		topMultiActionSeries.each{saveActionSeriesRecursively(it, actionSeriesCloneMap)}
		def newIds = actionSeriesCloneMap.values()*.id
	
		if (newIds.size() == 1)
			flash.message = message(code: 'default.created.message', args: [message(code: 'acInteractionSeries.label', default: 'AcInteractionSeries'), newIds.get(0)])
		else
			flash.message = "Multiple interaction series created: ${newIds}"
	}

	private def resetMetaDataRecursively(AcInteractionSeries interactionSeries, Long evoRunId) {					
		interactionSeries.name = interactionSeries.name + " (EvoRun " + evoRunId + ")"			
		interactionSeries.timeCreated = new Date()
		interactionSeries.createdBy = currentUserOrError
		interactionSeries.getSubActionSeries().each{ resetMetaDataRecursively(it, evoRunId) }
	}

	private def getReactionSets(AcCompartment compartment) {
		def reactionSets = new HashSet<AcReactionSet>()
		reactionSets.add(compartment.getReactionSet())
		for (AcCompartment subCompartment : compartment.getSubCompartments()) {
			reactionSets.addAll(getReactionSets(subCompartment));
		}
		reactionSets
	}

	private def getChartData(EvoRun evoRunInstance) {
		def chartData = new ChartData()
		chartData.title = "Evolution"
		chartData.xAxisCaption = "Generation"
		chartData.yAxisCaption = "Fitness"

		def seriesGroup = new SeriesGroup()
		seriesGroup.groupLabel = "All"
		chartData.seriesGroups.add(seriesGroup)

		seriesGroup.labels.add("Worst")
		seriesGroup.labels.add("Mean")
		seriesGroup.labels.add("Best")

		def minFitnesses = new ArrayList<Double>()
		def meanFitnesses = new ArrayList<Double>()
		def maxFitnesses = new ArrayList<Double>()
		chartData.series.add(minFitnesses)
		chartData.series.add(meanFitnesses)
		chartData.series.add(maxFitnesses)

		evoRunInstance.populations.each{
			chartData.xSteps.add(it.generation.toDouble())
			minFitnesses.add(EvolutionUtil.getWorstFitness(it))
			meanFitnesses.add(EvolutionUtil.getMeanFitness(it))
			maxFitnesses.add(EvolutionUtil.getBestFitness(it))
		}

		JSON.use("deep") {
			chartData as JSON
		}
	}
}