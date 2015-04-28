package com.banda.math.domain.dynamics

import com.banda.chemistry.domain.ArtificialChemistry
import edu.banda.coel.task.chemistry.AcMultiRunAnalysisTask
import com.banda.core.util.ConversionUtil

import edu.banda.coel.domain.service.ArtificialChemistryService
import edu.banda.coel.web.BaseDomainController

class MultiRunAnalysisResultController extends BaseDomainController {

	def ArtificialChemistryService artificialChemistryService

	def index() {
		redirect(action: "list", params: params)
	}

	def create() {
		def acMultiRunAnalysisTaskInstance = new AcMultiRunAnalysisTask()
		def acs = ArtificialChemistry.listWithProjections(['id', 'name'])

		[instance: acMultiRunAnalysisTaskInstance, acs : acs]
	}

	def launchAnalysis() {
		def acMultiRunAnalysisTaskInstance = new AcMultiRunAnalysisTask()
		bindData(acMultiRunAnalysisTaskInstance, params, [exclude: 'acIds'])
		if (params.acIds) {
			def acIds = new HashSet()
			params.list('acIds').each { acId ->
				acIds.add(ConversionUtil.convertToLong(acId, "AC id"))
			}
			acMultiRunAnalysisTaskInstance.setAcIds(acIds)
		}
		artificialChemistryService.analyzeDynamics(acMultiRunAnalysisTaskInstance)
		render "success"
	}

	def rerunDerridaAnalysis() {
		def acMultiRunAnalysisTaskInstance = new AcMultiRunAnalysisTask()
		bindData(acMultiRunAnalysisTaskInstance, params, [exclude: 'acIds'])
		if (params.acIds) {
			def acIds = new HashSet()
			params.list('acIds').each { acId ->
				acIds.add(ConversionUtil.convertToLong(acId, "AC id"))
			}
			acMultiRunAnalysisTaskInstance.setAcIds(acIds)
		}
		artificialChemistryService.rerunDerridaAnalysis(acMultiRunAnalysisTaskInstance)
		render "success"
	}
}