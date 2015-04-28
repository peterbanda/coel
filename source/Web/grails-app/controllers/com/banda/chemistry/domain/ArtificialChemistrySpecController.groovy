package com.banda.chemistry.domain

import java.util.Date

import com.banda.core.plotter.JavaPlotter
import com.banda.math.business.MathUtil
import com.banda.core.plotter.Plotter
import com.banda.core.util.ConversionUtil
import com.banda.core.util.ObjectUtil

import edu.banda.coel.core.util.ImageUtils
import edu.banda.coel.domain.service.ArtificialChemistryService
import edu.banda.coel.web.BaseDomainController

import com.banda.chemistry.business.*
import com.banda.chemistry.domain.ArtificialChemistrySpec
import edu.banda.coel.business.evo.fitness.EvoChromosomeAcSpecConverter
import com.banda.math.domain.dynamics.SingleRunAnalysisResultType
import com.banda.math.domain.StatsType
import com.banda.core.domain.um.User
import com.banda.chemistry.domain.AcReaction.ReactionDirection
import com.banda.math.business.dynamics.DynamicsAnalysisResultProcessor
import com.banda.math.business.dynamics.JavaStatsPlotter
import com.banda.math.business.MathUtil

import org.apache.commons.lang.StringUtils
import org.springframework.dao.DataIntegrityViolationException
import org.hibernate.transform.Transformers
import org.hibernate.criterion.CriteriaSpecification

class ArtificialChemistrySpecController extends BaseDomainController {
	
	def replicator = AcReplicator.instance	
	def ArtificialChemistryService artificialChemistryService
	def acUtil = ArtificialChemistryUtil.instance

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','timeCreated','name']
		super.list(max)
	}

	def createSymmetricInstance() {
		render(view: "create", model: [artificialChemistrySpecInstance: new AcSymmetricSpec(params)])
	}

	def createDNAStrandInstance() {
		render(view: "create", model: [artificialChemistrySpecInstance: new AcDNAStrandSpec(params)])
	}

	def save() {
		def artificialChemistrySpecInstance = null
		if (params.singleStrandsNum) {
			artificialChemistrySpecInstance = new AcDNAStrandSpec(params)
			artificialChemistrySpecInstance.upperStrandPartialBindingDistribution = bindRandomDistributionInt("upperStrandPartialBinding")
		} else {
			artificialChemistrySpecInstance = new AcSymmetricSpec(params)
		}
		artificialChemistrySpecInstance.rateConstantDistribution = bindRandomDistribution("rate")
		artificialChemistrySpecInstance.influxRateConstantDistribution = bindRandomDistribution("influxRate")
		artificialChemistrySpecInstance.outfluxRateConstantDistribution = bindRandomDistribution("outfluxRate")
		artificialChemistrySpecInstance.outfluxNonReactiveRateConstantDistribution = bindRandomDistribution("outfluxNonReactiveRate")
		artificialChemistrySpecInstance.createdBy = currentUserOrError
		if (!artificialChemistrySpecInstance.save(flush: true)) {
			render(view: "create", model: [instance: artificialChemistrySpecInstance])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, artificialChemistrySpecInstance.id])
		redirect(action: "show", id: artificialChemistrySpecInstance.id)
	}
	
	def show(Long id) {
		def result = super.show(id)
		def instance = result.find{ it.key == "instance" }.value
		def c = ArtificialChemistry.createCriteria()
		def acs = c.list {
			eq("generatedBySpec", instance)
			projections {
				property('id','id')
				property('name','name')
				property('createTime','createTime')
			}
			order("id", "desc")
			resultTransformer Transformers.aliasToBean(ArtificialChemistry.class)
		}			
		result << [acs : acs]
	}

	def update(Long id, Long version) {
		def artificialChemistrySpecInstance = getSafe(id)
		if (!artificialChemistrySpecInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (artificialChemistrySpecInstance.version > version) {
				setOlcFailureMessage(artificialChemistrySpecInstance)
				render(view: "edit", model: [instance: artificialChemistrySpecInstance])
				return
			}

		artificialChemistrySpecInstance.properties = params

		def newRateConstantDistribution = bindRandomDistribution("rate")
		def newInfluxRateConstantDistribution = bindRandomDistribution("influxRate")
		def newOutfluxRateConstantDistribution = bindRandomDistribution("outfluxRate")
		def newOutfluxNonReactiveRateConstantDistribution = bindRandomDistribution("outfluxNonReactiveRate")

		if (!isRandomDistributionEqual(artificialChemistrySpecInstance.rateConstantDistribution, newRateConstantDistribution)) {
			artificialChemistrySpecInstance.rateConstantDistribution = newRateConstantDistribution 
		}
		if (!isRandomDistributionEqual(artificialChemistrySpecInstance.influxRateConstantDistribution, newInfluxRateConstantDistribution)) {
			artificialChemistrySpecInstance.influxRateConstantDistribution = newInfluxRateConstantDistribution
		}
		if (!isRandomDistributionEqual(artificialChemistrySpecInstance.outfluxRateConstantDistribution, newOutfluxRateConstantDistribution)) {
			artificialChemistrySpecInstance.outfluxRateConstantDistribution = newOutfluxRateConstantDistribution
		}
		if (!isRandomDistributionEqual(artificialChemistrySpecInstance.outfluxNonReactiveRateConstantDistribution, newOutfluxNonReactiveRateConstantDistribution)) {
			artificialChemistrySpecInstance.outfluxNonReactiveRateConstantDistribution = newOutfluxNonReactiveRateConstantDistribution
		}

		if (params.singleStrandsNum) {
			def newUpperStrandPartialBindingDistribution = bindRandomDistributionInt("upperStrandPartialBinding")
			if (!isRandomDistributionEqual(artificialChemistrySpecInstance.upperStrandPartialBindingDistribution, newUpperStrandPartialBindingDistribution)) {
				artificialChemistrySpecInstance.upperStrandPartialBindingDistribution = newUpperStrandPartialBindingDistribution
			}
		}

		if (!artificialChemistrySpecInstance.save(flush: true)) {
			render(view: "edit", model: [instance: artificialChemistrySpecInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, artificialChemistrySpecInstance.id])
		redirect(action: "show", id: artificialChemistrySpecInstance.id)
	}

	def copy(Long id) {
		def artificialChemistrySpecInstance = getSafe(id)
		if (!artificialChemistrySpecInstance) {
			handleObjectNotFound(id)
			return
		}

		def artificialChemistrySpecInstanceClone = replicator.cloneArtificialChemistrySpec(artificialChemistrySpecInstance)
		artificialChemistrySpecInstanceClone.timeCreated = new Date()
		artificialChemistrySpecInstanceClone.createdBy = currentUserOrError
		if (!artificialChemistrySpecInstanceClone.save(flush: true)) {
			redirect(action: "show", id: artificialChemistrySpecInstance.id)
			return
		}
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, id])
		redirect(action: "list")
	}

	def displayAnalysisResults(Long id) {
		def StatsType firstStatsType = params.firstStatsType
		def StatsType secondStatsType = params.secondStatsType
		def SingleRunAnalysisResultType singleRunAnalysisResultType = params.singleRunAnalysisResultType
		def boolean mergeFlag = params.mergeFlag
		def yRangeMax = null
		if (params.yRangeMax) {
			yRangeMax = params.double('yRangeMax')
		}

		def artificialChemistrySpecInstance = getSafe(id)		
		def c = ArtificialChemistry.createCriteria()
		def acIds = c.list {
			eq("generatedBySpec", artificialChemistrySpecInstance)
			projections {
				property('id')
			}
		}

		def plotter = Plotter.createExportInstance("svg")
		def statsPlotter = new JavaStatsPlotter(plotter)
		def xlabel = statsPlotter.getXLabel(singleRunAnalysisResultType)
		def ylabel = "val"
		def title = singleRunAnalysisResultType.toString() + " (" + firstStatsType.toString() + ")"
 
		if (mergeFlag) {
			def results = artificialChemistryService.getMergedAcStats(singleRunAnalysisResultType, firstStatsType, acIds)
			statsPlotter.plotStats(results, title, xlabel, ylabel, yRangeMax)
		} else {
			def results = artificialChemistryService.getMultiMergedAcStats(singleRunAnalysisResultType, firstStatsType, acIds)
			statsPlotter.plotStats(results, secondStatsType, title + " (" + secondStatsType.toString() + ")",  xlabel, ylabel, yRangeMax, null)
		}

		def plotImageURI = URLEncoder.encode(plotter.getOutput(), "UTF-8")
		plotImageURI = StringUtils.replace(plotImageURI, "+", "%20")

		render(template:"refreshPlotPart", model: ["plotImage": plotImageURI])
	}

	def exportAnalysisResults(Long id) {
		def StatsType firstStatsType = params.firstStatsType
		def StatsType secondStatsType = params.secondStatsType
		def SingleRunAnalysisResultType singleRunAnalysisResultType = params.singleRunAnalysisResultType
		def boolean mergeFlag = params.mergeFlag
		def yRangeMax = null
		if (params.yRangeMax) {
			yRangeMax = params.double('yRangeMax')
		}

		def artificialChemistrySpecInstance = getSafe(id)
		def c = ArtificialChemistry.createCriteria()
		def acIds = c.list {
			eq("generatedBySpec", artificialChemistrySpecInstance)
			projections {
				property('id')
			}
		}

		def results = artificialChemistryService.getMergedAcStats(singleRunAnalysisResultType, firstStatsType, acIds)
		def indeces = new ArrayList<Double>()
		def means = new ArrayList<Double>()
		def maxs = new ArrayList<Double>()
		def mins = new ArrayList<Double>()
		results.each{
			indeces.add(it.getPos())
			means.add(MathUtil.projStats(StatsType.Mean, it))
			maxs.add(MathUtil.projStats(StatsType.Max, it))
			mins.add(MathUtil.projStats(StatsType.Min, it))
		}

		def StringBuilder sb = new StringBuilder()
		sb.append(StringUtils.join(indeces, ','))
		sb.append('\n')
		sb.append(StringUtils.join(means, ','))
		sb.append('\n')
		sb.append(StringUtils.join(maxs, ','))
		sb.append('\n')
		sb.append(StringUtils.join(mins, ','))
		sb.append('\n')

		response.setHeader("Content-disposition", "attachment filename=" + "stats_" + singleRunAnalysisResultType + " " + new Date().toString() + ".csv")
		render(contentType: "text/csv", text: sb.toString())
	}

	def importFromText = {
		def artificialChemistrySpecInstance = null
		if (params.type.equals('Symmetric')) {
			artificialChemistrySpecInstance = new AcSymmetricSpec()
		} else {
			artificialChemistrySpecInstance = new AcDNAStrandSpec()
			artificialChemistrySpecInstance.mirrorComplementarity = true
		}
		artificialChemistrySpecInstance.name = 'Imported ' + (new Date()).toString()
		artificialChemistrySpecInstance.includeReverseReactions = false
		artificialChemistrySpecInstance.outfluxAll = false
		artificialChemistrySpecInstance.speciesForbiddenRedundancy = AcReactionSpeciesForbiddenRedundancy.None

		def dataAsList = ConversionUtil.convertToList(Double.class, params.data, "Ac Spec data")
		EvoChromosomeAcSpecConverter.modify(artificialChemistrySpecInstance, dataAsList.toArray(new Number[0]))
		if (artificialChemistrySpecInstance.save(flush: true)) {
			flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, artificialChemistrySpecInstance.id])
		}
		redirect(action: "show", id: artificialChemistrySpecInstance.id)
	}
}