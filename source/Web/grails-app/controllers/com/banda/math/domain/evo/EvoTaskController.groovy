package com.banda.math.domain.evo

import java.util.Collection

import com.banda.core.Pair

import edu.banda.coel.web.BaseDomainController
import edu.banda.coel.web.AcAssignmentBoundData
import edu.banda.coel.web.AcAssignmentData
import edu.banda.coel.business.Replicator
import edu.banda.coel.domain.evo.EvoAcSpecTask
import edu.banda.coel.domain.evo.EvoAcRateConstantTask
import edu.banda.coel.domain.evo.EvoAcInteractionSeriesTask
import edu.banda.coel.domain.evo.EvoNetworkTask
import edu.banda.coel.domain.evo.AcSpeciesAssignmentBound
import edu.banda.coel.domain.evo.AcInteractionVariableAssignmentBound

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataIntegrityViolationException

import com.banda.core.plotter.JavaPlotter
import com.banda.math.business.JavaMathUtil
import com.banda.core.plotter.Plotter
import com.banda.math.domain.Stats
import com.banda.math.domain.StatsType
import com.banda.core.util.ObjectUtil
import com.banda.math.business.dynamics.JavaStatsPlotter
import com.banda.chemistry.domain.ArtificialChemistry
import com.banda.chemistry.domain.AcInteractionSeries
import com.banda.chemistry.domain.AcInteraction
import com.banda.chemistry.domain.AcEvaluation
import com.banda.chemistry.domain.AcSpeciesInteraction
import com.banda.chemistry.domain.AcInteractionVariableAssignment
import com.banda.network.domain.Network
import com.banda.network.domain.NetworkActionSeries
import com.banda.network.domain.NetworkEvaluation
import com.banda.chemistry.business.ArtificialChemistryUtil

import edu.banda.coel.domain.service.EvolutionService
import edu.banda.coel.web.AcDependentEvoRateConstantTaskData
import edu.banda.coel.web.AcDependentEvoInteractionSeriesTaskData
import grails.converters.JSON

class EvoTaskController extends BaseDomainController {

	def EvolutionService evolutionService
	def acUtil = ArtificialChemistryUtil.getInstance()

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','createTime','name','taskType']
		super.list(max)
	}

	def createAcRateConstantTaskInstance() {
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'

		def acs = ArtificialChemistry.listWithParamsAndProjections(params)
		def gaSettings = EvoGaSetting.listWithParamsAndProjections(params)
		def interactionSeries = []
		def evaluations = []
		def reactions = []
		def reactionGroups = []

		render(view: "create", model: [instance: new EvoAcRateConstantTask(params), acs : acs, gaSettings : gaSettings, interactionSeries : interactionSeries, evaluations : evaluations, reactions : reactions, reactionGroups : reactionGroups])
	}

	def createAcInteractionSeriesTaskInstance() {
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'

		def acs = ArtificialChemistry.listWithParamsAndProjections(params)
		def gaSettings = EvoGaSetting.listWithParamsAndProjections(params)
		def interactionSeries = []
		def allInteractionSeries = []
		def evaluations = []

		render(view: "create", model: [instance: new EvoAcInteractionSeriesTask(params), acs : acs, gaSettings : gaSettings, interactionSeries : interactionSeries, allInteractionSeries : allInteractionSeries, evaluations : evaluations])
	}

	def createAcSpecInstance() {
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'
		def gaSettings = EvoGaSetting.listWithParamsAndProjections(params)
		render(view: "create", model: [instance: new EvoAcSpecTask(params), gaSettings : gaSettings])
	}

	def createNetworkInstance() {
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'

		def networks = Network.listWithParamsAndProjections(params)
		def networkActionSeries = NetworkActionSeries.listWithParamsAndProjections(params)
		def evaluations =  NetworkEvaluation.listWithParamsAndProjections(params)
		def gaSettings = EvoGaSetting.listWithParamsAndProjections(params)

		render(view: "create", model: [instance: new EvoNetworkTask(params), networks : networks, networkActionSeries : networkActionSeries, evaluations : evaluations, gaSettings : gaSettings])
	}

	def save() {
		def evoTaskInstance = null
		if (params.acSpecBound)
			evoTaskInstance = new EvoAcSpecTask(params)
		else if (params.type == 'AcRateConstant')
			evoTaskInstance = new EvoAcRateConstantTask(params)
		else if (params.type == 'AcInteractionSeries')
			evoTaskInstance = new EvoAcInteractionSeriesTask(params)
		else 
			evoTaskInstance = new EvoNetworkTask(params)

		evoTaskInstance.createdBy = currentUserOrError

		if (evoTaskInstance instanceof EvoAcInteractionSeriesTask)
			setAssignmentBounds(evoTaskInstance)

		if (!evoTaskInstance.save(flush: true)) {
			render(view: "create", model: [instance: evoTaskInstance])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, evoTaskInstance.id])
		redirect(action: "show", id: evoTaskInstance.id)
	}

	def setAssignmentBounds(EvoAcInteractionSeriesTask evoTaskInstance) {
		def assignmentBounds = JSON.parse(params.assignmentBoundTable)

		def speciesAssignmentBounds = assignmentBounds.findAll{ it.type == 0 }.collect{
			def bound = new AcSpeciesAssignmentBound(it)
			def assignments = it.assignments.collect{
				def assignment = new AcSpeciesInteraction()
				assignment.id = it as long
				assignment
			} as Set
			bound.assignments = assignments
			bound
		} as Set

		def variableAssignmentBounds = assignmentBounds.findAll{ it.type == 1 }.collect{
			def bound = new AcInteractionVariableAssignmentBound(it)
			def assignments = it.assignments.collect{
			def assignment = new AcInteractionVariableAssignment()
				assignment.id = it as long
				assignment
			} as Set
			bound.assignments = assignments
			bound
		} as Set
		
		evoTaskInstance.speciesAssignmentBounds.clear()
		evoTaskInstance.speciesAssignmentBounds.addAll(speciesAssignmentBounds)
		
		evoTaskInstance.variableAssignmentBounds.clear()
		evoTaskInstance.variableAssignmentBounds.addAll(variableAssignmentBounds)		
	}

	def edit(Long id) {
		def evoTaskInstance = getSafe(id)
		if (!evoTaskInstance) {
			handleObjectNotFound(id)
			return
		}
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'
		def gaSettings = EvoGaSetting.listWithParamsAndProjections(params)

		// ac stuff
		def acs = []
		def interactionSeries = []
		def allInteractionSeries = []
		def evaluations = []
		def reactions = []
		def reactionGroups = []

		// network stuff
		def networks = []
		def networkActionSeries = []

		if (evoTaskInstance instanceof EvoAcRateConstantTask || evoTaskInstance instanceof EvoAcInteractionSeriesTask) {
			acs = ArtificialChemistry.listWithParamsAndProjections(params)

			def ac = evoTaskInstance.ac 
			def speciesSets = getThisAndParentSpeciesSets(evoTaskInstance.ac.speciesSet)

			interactionSeries = AcInteractionSeries.findAll("from AcInteractionSeries as a where a.speciesSet IN (:speciesSets) order by a.id DESC", [speciesSets : speciesSets])
			evaluations = AcEvaluation.findAll("from AcEvaluation as a where a.translationSeries.speciesSet IN (:speciesSets) order by a.id DESC", [speciesSets : speciesSets])
			if (evoTaskInstance instanceof EvoAcRateConstantTask) {
				def reactionSets = ([ac.skinCompartment.reactionSet] as Set) + getReactionSetsRecursively(ac.skinCompartment)
				reactions = reactionSets*.reactions.flatten()
				reactionGroups = reactionSets*.groups.flatten()
			} else {
				allInteractionSeries = getInteractionSeriesRecursively(interactionSeries)
			}
		} else if (evoTaskInstance instanceof EvoNetworkTask) {
			networks = Network.listWithParamsAndProjections(params)
			networkActionSeries = NetworkActionSeries.listWithParamsAndProjections(params)
			evaluations =  NetworkEvaluation.listWithParamsAndProjections(params)
		}

		[instance: evoTaskInstance, acs : acs, networks : networks, networkActionSeries : networkActionSeries, gaSettings : gaSettings, interactionSeries : interactionSeries, allInteractionSeries : allInteractionSeries, evaluations : evaluations, reactions : reactions, reactionGroups : reactionGroups]
	}
	
	def update(Long id, Long version) {
		def evoTaskInstance = getSafe(id)
		if (!evoTaskInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (evoTaskInstance.version > version) {
				setOlcFailureMessage(evoTaskInstance)
				render(view: "edit", model: [instance: evoTaskInstance])
				return
			}

		evoTaskInstance.properties = params

		if (evoTaskInstance instanceof EvoAcRateConstantTask) {
			if (!params.fixedRateReactions) {
				evoTaskInstance.fixedRateReactions = []
			}
			if (!params.fixedRateReactionGroups) {
				evoTaskInstance.fixedRateReactionGroups = []
			}
		}

		if (evoTaskInstance instanceof EvoAcInteractionSeriesTask) {
			setAssignmentBounds(evoTaskInstance)
		}

		if (!evoTaskInstance.save(flush: true)) {
			render(view: "edit", model: [instance: evoTaskInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, evoTaskInstance.id])
		redirect(action: "show", id: evoTaskInstance.id)
	}

	protected def copyInstance(instance) {
		def replicator = Replicator.instance

		def newInstance = null
		if (instance instanceof EvoAcRateConstantTask) {
			newInstance = replicator.cloneEvoAcRateConstantTask(instance)
			ObjectUtil.nullIdAndVersion(newInstance)
		} else if (instance instanceof EvoAcInteractionSeriesTask) {
			newInstance = replicator.cloneEvoAcInteractionSeriesTask(instance)
			replicator.nullIdAndVersion(newInstance)
		} else if (instance instanceof EvoNetworkTask) {
			newInstance = replicator.cloneEvoNetworkTask(instance)
			ObjectUtil.nullIdAndVersion(newInstance)
		}

		def name = newInstance.name + " copy"
		if (name.size() > 100) name = name.substring(0, 100)

		newInstance.name = name
		newInstance.createTime = new Date()
		newInstance.createdBy = currentUserOrError

		newInstance.save(flush: true)
		newInstance
	}

	def displayFitness(Long id) {
		def StatsType statsType = params.statsType
		def boolean export = params.export == "true"
		def EvoTask evoTask = getSafe(id)

		def multiStats = new ArrayList<List<Stats>>()

		for (EvoRun evoRunInstance : evoTask.getEvolutionRuns()) {
			if (evoRunInstance.isDone()) {
				def stats = new ArrayList<Stats>()
				multiStats.add(stats)
				evoRunInstance.populations.each{ stats.add(it.getFitnessStats()) }
			}
		}

		def plotter = Plotter.createExportInstance("svg")
		def statsPlotter = new JavaStatsPlotter(plotter)
		def xlabel = "Generation"
		def ylabel = "Fitness"
		def title = "Fitness Evo Task " + id + " (" + statsType + ")"

		def results = JavaMathUtil.calcStatsBySecond(statsType, multiStats)

		statsPlotter.plotStats(results, title, xlabel, ylabel, null)
		def svgXML = plotter.getOutput()

		if (export) {
			response.setHeader("Content-disposition", "attachment; filename=" + "fitness_evo_task_" + id + "_(" + statsType + ").svg")
			render(contentType: "image/svg+xml", text: svgXML)
		} else {
			def plotImageURI = URLEncoder.encode(svgXML, "UTF-8")
			plotImageURI = StringUtils.replace(plotImageURI, "+", "%20")
			render(template:"refreshPlotPart", model: ["plotImage": plotImageURI])
		}
	}

	def displayFitnessCorrelation(Long id) {
		def int chromElementIndex = params.int('chromElementIndex')
		def boolean export = params.export2 == "true"
		def EvoTask evoTask = getSafe(id)

		def fitnessChromElementPairs = new ArrayList<List<Double>>()

		for (EvoRun evoRunInstance : evoTask.getEvolutionRuns()) {
			if (evoRunInstance.isDone()) {
				evoRunInstance.populations.each{
					def pair = new ArrayList<Double>()
					pair.add(it.getBestOrLastChromosome().fitness)
					pair.add(it.getBestOrLastChromosome().getCodeAt(chromElementIndex))
					fitnessChromElementPairs.add(pair)
				}
			}
		}

		def plotter = JavaPlotter.createExportInstance("svg")
//		def xlabel = "Fitness"
//		def ylabel = "Val"
		def title = "Correlations - task " + id + ", index " + chromElementIndex

		plotter.plotXY(fitnessChromElementPairs, title)
		def svgXML = plotter.getOutput()

		if (export) {
			response.setHeader("Content-disposition", "attachment; filename=" + "fitness_evo_task_" + id + "_(" + statsType + ").svg")
			render(contentType: "image/svg+xml", text: svgXML)
		} else {
			def plotImageURI = URLEncoder.encode(svgXML, "UTF-8")
			plotImageURI = StringUtils.replace(plotImageURI, "+", "%20")
			render(template:"refreshPlotPart", model: ["plotImage": plotImageURI])
		}
	}

	def exportBestChromDensityFitnessRelations(Long id) {
		def EvoTask evoTask = getSafe(id)

		StringBuilder sb = new StringBuilder()
		evolutionService.calcFitnessToBestChromDensity(evoTask).each { densityFitnessPair ->
			sb.append(densityFitnessPair.getFirst())
			sb.append(',')
			sb.append(densityFitnessPair.getSecond())
			sb.append('\n')
		}
		response.setHeader("Content-disposition", "attachment; filename=" + "evoTaskFitnessChromDensity_${id}.csv");
		render(contentType: "text/csv", text: sb.toString());
	}

	def getAcRateConstantDependentData(Long id) {
		def ac = ArtificialChemistry.get(id)
		def reactionSets = ([ac.skinCompartment.reactionSet] as Set) + getReactionSetsRecursively(ac.skinCompartment)

		def speciesSets = getThisAndParentSpeciesSets(ac.speciesSet)
		def interactionSeries = AcInteractionSeries.findAll("from AcInteractionSeries as a where a.speciesSet IN (:speciesSets) order by a.id DESC", [speciesSets : speciesSets])
		def evaluations = AcEvaluation.findAll("from AcEvaluation as a where a.translationSeries.speciesSet IN (:speciesSets) order by a.id DESC", [speciesSets : speciesSets])

		def data = new AcDependentEvoRateConstantTaskData()
		data.reactions = reactionSets*.reactions.flatten().collectEntries{ [it.id, it.label] }
		data.reactionGroups = reactionSets*.groups.flatten().collectEntries{ [it.id, it.label] }
		data.interactionSeries = interactionSeries.collectEntries{ [it.id, it.id + " : " + it.name] }
		data.evaluations = evaluations.collectEntries{ [it.id, it.id + " : " + it.name] }
		render data as JSON
	}

	def getAcInteractionSeriesDependentData(Long id) {
		def ac = ArtificialChemistry.get(id)

		def speciesSets = getThisAndParentSpeciesSets(ac.speciesSet)
		def topInteractionSeries = AcInteractionSeries.findAll("from AcInteractionSeries as a where a.speciesSet IN (:speciesSets) order by a.id DESC", [speciesSets : speciesSets])
		def evaluations = AcEvaluation.findAll("from AcEvaluation as a where a.translationSeries.speciesSet IN (:speciesSets) order by a.id DESC", [speciesSets : speciesSets])
		def allInteractionSeries = getInteractionSeriesRecursively(topInteractionSeries)

		def data = new AcDependentEvoInteractionSeriesTaskData()
		data.topInteractionSeries = topInteractionSeries.collectEntries{ [it.id, it.id + " : " + it.name] }
		data.interactionSeries = allInteractionSeries.collectEntries{ [it.id, it.id + " : " + it.name] }
		data.evaluations = evaluations.collectEntries{ [it.id, it.id + " : " + it.name] }
		render data as JSON
	}

	def getAcInteractions(Long id) {
		def acInteractionSeries = AcInteractionSeries.get(id)

		def interactions = acInteractionSeries.actions.collectEntries{ [it.id, it.startTime] }
		render interactions as JSON
	}

	def getAcSpeciesAssignments(Long id) {
		def acInteraction = AcInteraction.get(id)

		def speciesAssignments = acInteraction.speciesActions.collectEntries{ [it.id, it.species.label + " &larr; " + acUtil.getSettingFunctionAsString(it)] }
		render speciesAssignments as JSON
	}

	def getAcVariableAssignments(Long id) {
		def acInteraction = AcInteraction.get(id)

		def variableAssignments = acInteraction.variableAssignments.collectEntries{ [it.id, it.variable.label + " &rarr; " + acUtil.getSettingFunctionAsString(it)] }
		render variableAssignments as JSON
	}

	def getAcAssignmentBounds(Long id) {
		def evoAcInteractionSeriesTask = getSafe(id)

		def speciesBounds = evoAcInteractionSeriesTask.speciesAssignmentBounds.collect{
			def data = new AcAssignmentBoundData()
			data.type = 0
			data.from = it.from
			data.to = it.to
			data.assignments = it.assignments.collect{
				def assignment = new AcAssignmentData()
				assignment.interactionSeriesId = it.action.actionSeries.id
				assignment.interactionId = it.action.id
				assignment.assignmentId = it.id
			
				assignment.interactionSeriesLabel = it.action.actionSeries.id + " : " + it.action.actionSeries.name
				assignment.interactionLabel = it.action.startTime
				assignment.assignmentLabel = it.species.label + " &larr; " + acUtil.getSettingFunctionAsString(it)
				assignment 
			}
			data
		};

		def variableBounds = evoAcInteractionSeriesTask.variableAssignmentBounds.collect{
			def data = new AcAssignmentBoundData()
			data.type = 1
			data.from = it.from
			data.to = it.to
			data.assignments = it.assignments.collect{
				def assignment = new AcAssignmentData()
				assignment.interactionSeriesId = it.action.actionSeries.id
				assignment.interactionId = it.action.id
				assignment.assignmentId = it.id
		
				assignment.interactionSeriesLabel = it.action.actionSeries.id + " : " + it.action.actionSeries.name 
				assignment.interactionLabel = it.action.startTime
				assignment.assignmentLabel = it.variable.label + " &rarr; " + acUtil.getSettingFunctionAsString(it)
				assignment
			}
			data
		};

		def bounds = speciesBounds + variableBounds
		def boundsJSON = bounds as JSON 
		render boundsJSON
	}

	private def getReactionSetsRecursively(compartment) {
		def reactionSets = compartment.getSubCompartments()*.reactionSet as Set
		reactionSets << compartment.getSubCompartments().collect{ getReactionSetsRecursively(it) }.flatten() as Set
	}

	private def getInteractionSeriesRecursively(List<AcInteractionSeries> interactionSeries) {
		def allInteractionSeries = []
		allInteractionSeries += interactionSeries
		allInteractionSeries += interactionSeries.collect{ getInteractionSeriesRecursively(it.getSubActionSeries()) }.flatten()
		allInteractionSeries
	}
}