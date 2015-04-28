package com.banda.chemistry.domain

import org.apache.commons.lang.StringUtils
import grails.plugins.springsecurity.Secured
import org.springframework.security.access.prepost.PostFilter
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.dao.DataIntegrityViolationException

import com.banda.chemistry.business.AcReplicator
import com.banda.chemistry.business.ArtificialChemistryUtil
import edu.banda.coel.task.chemistry.AcRunTask
import com.banda.core.plotter.JavaPlotter
import com.banda.core.plotter.Plotter
import com.banda.math.domain.dynamics.SingleRunAnalysisResult
import com.banda.math.domain.dynamics.SingleRunAnalysisResultType
import com.banda.math.domain.StatsType
import com.banda.core.domain.um.User
import com.banda.core.util.ObjectUtil
import com.banda.math.business.dynamics.JavaStatsPlotter

import edu.banda.coel.core.util.ImageUtils
import edu.banda.coel.domain.service.ArtificialChemistryService
import edu.banda.coel.web.BaseDomainController

import org.hibernate.transform.Transformers

class ArtificialChemistryController extends BaseDomainController {

	def replicator = AcReplicator.instance
	def ArtificialChemistryService artificialChemistryService
	def ArtificialChemistryUtil acUtil = ArtificialChemistryUtil.instance

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','name','createTime','createdBy']
		super.list(max)
	}

	def create() {
		def acSimulationConfigs = AcSimulationConfig.listWithProjections(['id', 'name'])
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','label']
		params.sort = 'id'
		params.order = 'desc'

		def acCompartments = AcCompartment.listWithParamsAndProjections(params)

		def instance = new ArtificialChemistry(params)
		instance.simulationConfig =  AcSimulationConfig.get(1010l)
		[instance: instance, acCompartments : acCompartments, acSimulationConfigs : acSimulationConfigs]
	}

	def createQuick() {
		def acSimulationConfigs = AcSimulationConfig.listWithProjections(['id', 'name'])

		def instance = new ArtificialChemistry(params)
		instance.simulationConfig =  AcSimulationConfig.get(1010l)

		[instance: instance, acSimulationConfigs : acSimulationConfigs]
	}

	def save() {
        def artificialChemistryInstance = new ArtificialChemistry(params)
		artificialChemistryInstance.createdBy = currentUserOrError

		if (!artificialChemistryInstance.save(flush: true)) {
			def acSimulationConfigs = AcSimulationConfig.listWithProjections(['id', 'name'])
			if (!isAdmin())
				params.createdBy = getCurrentUserOrError()

			params.projections = ['id','label']
			params.sort = 'id'
			params.order = 'desc'

			def acCompartments = AcCompartment.listWithParamsAndProjections(params)
			render(view: "create", model: [instance: artificialChemistryInstance, acCompartments : acCompartments, acSimulationConfigs : acSimulationConfigs])
			return
		}

		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, artificialChemistryInstance.id])
		redirect(action: "show", id: artificialChemistryInstance.id)
	}

	def saveQuick() {
		def artificialChemistryInstance = new ArtificialChemistry(params)
		artificialChemistryInstance.createdBy = currentUserOrError

		def name = artificialChemistryInstance.name
		def speciesSet = createSpeciesSet(name)

		def reactionSet = new AcReactionSet()
		reactionSet.label = name
		reactionSet.createdBy = currentUserOrError
		reactionSet.speciesSet = speciesSet

		def compartment = new AcCompartment()
		compartment.label = name
		compartment.createdBy = currentUserOrError
		compartment.reactionSet = reactionSet

		artificialChemistryInstance.skinCompartment = compartment

		def saved = false;
		if (speciesSet.save())
			if (reactionSet.save())
				if (compartment.save())
					if (artificialChemistryInstance.save(flush: true))
						saved = true

		if (!saved) {
			def acSimulationConfigs = AcSimulationConfig.listWithProjections(['id', 'name'])
			render(view: "createQuick", model: [instance: artificialChemistryInstance, acSimulationConfigs : acSimulationConfigs])
			return
		}

		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, artificialChemistryInstance.id])
		redirect(controller: 'acReactionSet', action: "show", id: reactionSet.id)
	}

	private def createSpeciesSet(name) {
		def acSpeciesSetInstance = new AcSpeciesSet()
		acSpeciesSetInstance.name = name
		acSpeciesSetInstance.createdBy = currentUserOrError
		acSpeciesSetInstance.initVarSequenceNum()

		AcParameterSet parameterSet = new AcParameterSet()
		parameterSet.name = acSpeciesSetInstance.name
		parameterSet.createTime = acSpeciesSetInstance.createTime
		parameterSet.createdBy = acSpeciesSetInstance.createdBy

		acSpeciesSetInstance.parameterSet = parameterSet
		parameterSet.speciesSet = acSpeciesSetInstance
		acSpeciesSetInstance
	}

	def show(Long id) {
		def result = super.show(id)

		def singleRunResults = []
		result.instance?.multiRunAnalysisResults.each { singleRunResults.addAll(it.singleRunResults) }

		result <<  [singleRunResults : singleRunResults]
	}

	def edit(Long id) {
		def result = super.edit(id)

		def acSimulationConfigs = AcSimulationConfig.listWithProjections(['id', 'name'])
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()
		params.projections = ['id','label']
		params.sort = 'id'
		params.order = 'desc'

		def acCompartments = AcCompartment.listWithParamsAndProjections(params)
		result << [acCompartments : acCompartments, acSimulationConfigs : acSimulationConfigs]
	}

	def update(Long id, Long version) {
		def artificialChemistryInstance = getSafe(id)

		if (!artificialChemistryInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (artificialChemistryInstance.version > version) {
				setOlcFailureMessage(artificialChemistryInstance)
				render(view: "edit", model: [instance: artificialChemistryInstance])
				return
			}

		artificialChemistryInstance.properties = params
		if (!updateSafe(artificialChemistryInstance)) {
			render(view: "edit", model: [instance: artificialChemistryInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, artificialChemistryInstance.id])
		redirect(action: "show", id: artificialChemistryInstance.id)
	}

	def importSbml = {
		def sbmlFile = request.getFile('sbmlFile')
		if (!sbmlFile.empty) {
			def sbmlFileContent = sbmlFile.inputStream.text
			def fileName = sbmlFile.originalFilename
			artificialChemistryService.saveSbmlModelAsArtificialChemistry(sbmlFileContent, fileName[0..-5])
			flash.message = 'SBML model has been imported.'
		}
		else {
			flash.message = 'Filename cannot be empty.'
		}
		redirect(action:list)
	}

	protected def copyInstance(instance) {
		def newInstance = genericReflectionProvider.clone(instance)
		ObjectUtil.nullIdAndVersion(newInstance)

		def name = newInstance.name + " copy"
		if (name.size() > 100) name = name.substring(0, 100)

		newInstance.name = name
		newInstance.createTime = new Date()
		newInstance.createdBy = currentUserOrError
		newInstance.multiRunAnalysisResults = null

		newInstance.save(flush:true)
		newInstance
	}

	def displayAnalysisResults(Long id) {
		def StatsType statsType = params.statsType
		def SingleRunAnalysisResultType singleRunAnalysisResultType = params.singleRunAnalysisResultType
		def boolean export = params.export == "true"

		def ArtificialChemistry acInstance = getSafe(id)

		def multiRunIds = []
		acInstance.getMultiRunAnalysisResults().each{ multiRunResult -> multiRunIds.add(multiRunResult.id) }
		def results = artificialChemistryService.getMergedMultiRunStats(singleRunAnalysisResultType, statsType, multiRunIds)

		def plotter = Plotter.createExportInstance("svg")
		def statsPlotter = new JavaStatsPlotter(plotter)
		def xlabel = statsPlotter.getXLabel(singleRunAnalysisResultType)
		def ylabel = "val"
		def title = singleRunAnalysisResultType.toString() + " (" + statsType.toString() + ")"

		statsPlotter.plotStats(results, title, xlabel, ylabel, null)
		def svgXML = plotter.getOutput()

		if (export) {
			response.setHeader("Content-disposition", "attachment filename=" + "ac_" + id + "_" + StringUtils.replace(title, " ", "_") + ".svg")
			render(contentType: "image/svg+xml", text: svgXML)
		} else {
			def plotImageURI = URLEncoder.encode(svgXML, "UTF-8")
			plotImageURI = StringUtils.replace(plotImageURI, "+", "%20")
			render(template:"refreshPlotPart", model: ["plotImage": plotImageURI])
		}
	}

	def runAcForAnalysisResult(Long id) {
		def singleRunResultId = params.long('singleRunResult.id')
		def singleRunAnalysisResult = SingleRunAnalysisResult.get(singleRunResultId)		
		def runTime = singleRunAnalysisResult.multiRunResult.spec.singleRunSpec.iterations

		AcRunTask acRunTask = new AcRunTask()
		acRunTask.setAcId(id)
		acRunTask.setRunTime(runTime)
//		acRunTask.setInitialConcentrations(singleRunAnalysisResult.initialState)

		def runTraces = artificialChemistryService.runSimulation(acRunTask)
		def runTrace = ObjectUtil.getFirst(runTraces)
		runTrace.sortComponentHistoriesBy(acUtil.compartmentSpeciesComparator)

//		runTrace.timeSteps
	
		def sequences = []
		runTrace.componentHistories().each { compartmentHistory ->
//			def compartment = compartmentHistory.component().getFirst()
//			def species = compartmentHistory.component().getSecond()	
			sequences.add(compartmentHistory.history())
		}

		def plotter = JavaPlotter.createExportInstance("svg")
		plotter.plotTimeSeries(sequences, "Concentrations", false)

		def plotImageURI = URLEncoder.encode(plotter.getOutput(), "UTF-8")
		plotImageURI = StringUtils.replace(plotImageURI, "+", "%20")

		render(template:"refreshPlotPart", model: ["plotImage": plotImageURI])		
	}

	def generateACs(Long artificialChemistrySpecId, Long acSimulationConfigId, Integer acNum) {
		def spec = new ArtificialChemistrySpec()
		spec.setId(artificialChemistrySpecId)
		def config = new AcSimulationConfig()
		config.setId(acSimulationConfigId)
		artificialChemistryService.generateArtificialChemistries(spec, config, acNum)
		redirect(action: "list")
	}

	def exportRunForAnalysisResult(Long id) {
		def singleRunResultId = params.long('singleRunResult.id')
		def singleRunAnalysisResult = SingleRunAnalysisResult.get(singleRunResultId)		
		def runTime = singleRunAnalysisResult.multiRunResult.spec.singleRunSpec.iterations

		AcRunTask acRunTask = new AcRunTask()
		acRunTask.setAcId(id)
		acRunTask.setRunTime(runTime)
		acRunTask.setInitialConcentrations(singleRunAnalysisResult.initialState)

		def runTraces = artificialChemistryService.runSimulation(acRunTask)
		def runTrace = ObjectUtil.getFirst(runTraces)

//		def tableString = acUtil.getAcRunAsTable(acRunInstance, 10)
		def tableString = "TODO"
		
		response.setHeader("Content-disposition", "attachment filename=" + "acRun_for_ac_" + id + ".csv")
		render(contentType: "text/csv", text: tableString)
	}
}