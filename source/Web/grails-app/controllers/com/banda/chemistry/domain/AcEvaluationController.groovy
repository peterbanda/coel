package com.banda.chemistry.domain

import edu.banda.coel.web.BaseDomainController

import com.banda.chemistry.business.ArtificialChemistryUtil
import com.banda.chemistry.domain.AcEvaluation
import com.banda.chemistry.domain.ArtificialChemistry
import com.banda.chemistry.business.AcReplicator

import com.banda.function.BndFunctionException

class AcEvaluationController extends BaseDomainController {

	def acUtil = ArtificialChemistryUtil.instance
	def replicator = AcReplicator.instance

	def list(Integer max) {
		params.projections = ['id','name','createTime','createdBy']
		super.list(max)
	}

	def index() {
		redirect(action: "list", params: params)
	}

	def create() {
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'

		def translationSeries = AcTranslationSeries.listWithParamsAndProjections(params)
		[instance: new AcEvaluation(params), translationSeries : translationSeries]
	}

	def edit(Long id) {
		def result = super.edit(id)
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'

		def translationSeries = AcTranslationSeries.listWithParamsAndProjections(params)
		result << [translationSeries : translationSeries]
	}

	def save() {
		def acEvaluationInstance = new AcEvaluation(params)
		acEvaluationInstance.createdBy = currentUserOrError

		// Evaluation Function
		try {
			acUtil.setEvaluationFunctionFromString(params.evalFunction.formula, acEvaluationInstance)
		} catch (BndFunctionException e) {
			acEvaluationInstance.errors.rejectValue("evalFunction", "Evaluation Function '" + params.evalFunction.formula + "' is invalid. " + e.getMessage())
		}

		if (acEvaluationInstance.hasErrors() || !acEvaluationInstance.save(flush: true)) {
			if (!isAdmin())
				params.createdBy = getCurrentUserOrError()

			params.projections = ['id','name']
			params.sort = 'id'
			params.order = 'desc'

			def translationSeries = AcTranslationSeries.listWithParamsAndProjections(params)
			render(view: "create", model: [instance: acEvaluationInstance, translationSeries : translationSeries])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, acEvaluationInstance.id])
		redirect(action: "show", id: acEvaluationInstance.id)
	}
	
	def update(Long id, Long version) {
		def acEvaluationInstance = getSafe(id)
		if (!acEvaluationInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (acEvaluationInstance.version > version) {
				setOlcFailureMessage(acEvaluationInstance)
				render(view: "edit", model: [instance: acEvaluationInstance])
				return
			}

		acEvaluationInstance.properties = params

		// Evaluation Function
		try {
			acUtil.setEvaluationFunctionFromString(params.evalFunction.formula, acEvaluationInstance)
		} catch (BndFunctionException e) {
			acEvaluationInstance.errors.rejectValue("evalFunction", "Evaluation Function '" + params.evalFunction.formula + "' is invalid. " + e.getMessage())
		}

		if (acEvaluationInstance.hasErrors() || !acEvaluationInstance.save(flush: true)) {
			render(view: "edit", model: [instance: acEvaluationInstance, translationSeries : AcTranslationSeries.listWithProjections(['id', 'name'])])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acEvaluationInstance.id])
		redirect(action: "show", id: acEvaluationInstance.id)
	}

	protected def copyInstance(instance) {
		def newInstance = replicator.cloneEvaluation(instance)
		replicator.nullIdAndVersion(newInstance)

		def name = newInstance.name + " copy"
		if (label.size() > 100) name = label.substring(0, 100)

		newInstance.name = name
		newInstance.createTime = new Date()
		newInstance.createdBy = currentUserOrError

		newInstance.save(flush: true)
		newInstance
	}
}
