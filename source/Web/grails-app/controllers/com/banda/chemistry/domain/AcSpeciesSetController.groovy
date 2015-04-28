package com.banda.chemistry.domain

import edu.banda.coel.web.BaseDomainController

import com.banda.chemistry.domain.AcParameterSet
import com.banda.chemistry.domain.AcSpeciesSet
import com.banda.core.domain.um.User
import com.banda.core.util.ObjectUtil
import com.banda.chemistry.business.AcReplicator


class AcSpeciesSetController extends BaseDomainController {

	def replicator = AcReplicator.instance

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','name','createTime','createdBy']
		super.list(max)
	}

	def create() {
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','name']
		params.sort = 'id'
		params.order = 'desc'
		[instance: new AcSpeciesSet(params), acSpeciesSets : AcSpeciesSet.listWithParamsAndProjections(params)]
	}
	
	def save() {
		def acSpeciesSetInstance = new AcSpeciesSet(params)
		acSpeciesSetInstance.createdBy = currentUserOrError
		acSpeciesSetInstance.initVarSequenceNum()

		AcParameterSet parameterSet = new AcParameterSet()
		parameterSet.name = acSpeciesSetInstance.name
		parameterSet.createTime = acSpeciesSetInstance.createTime
		parameterSet.createdBy = acSpeciesSetInstance.createdBy

		acSpeciesSetInstance.parameterSet = parameterSet
		parameterSet.speciesSet = acSpeciesSetInstance

		if (!acSpeciesSetInstance.save(flush: true)) {
			if (!isAdmin())
				params.createdBy = getCurrentUserOrError()

			params.projections = ['id','name']
			params.sort = 'id'
			params.order = 'desc'
			render(view: "create", model: [instance: acSpeciesSetInstance, acSpeciesSets : AcSpeciesSet.listWithParamsAndProjections(params)])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, acSpeciesSetInstance.id])
		redirect(action: "show", id: acSpeciesSetInstance.id)
	}
	
	def update(Long id, Long version) {
		def acSpeciesSetInstance = getSafe(id)
		if (!acSpeciesSetInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (acSpeciesSetInstance.version > version) {
				setOlcFailureMessage(acSpeciesSetInstance)
				render(view: "edit", model: [instance: acSpeciesSetInstance])
				return
			}

		acSpeciesSetInstance.properties = params

		if (!acSpeciesSetInstance.save(flush: true)) {
			render(view: "edit", model: [instance: acSpeciesSetInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acSpeciesSetInstance.id])
		redirect(action: "show", id: acSpeciesSetInstance.id)
	}

	protected def copyInstance(instance) {
		def newInstance = replicator.cloneSpeciesSetWithParameterSet(instance)
		ObjectUtil.nullIdAndVersion(newInstance)
		ObjectUtil.nullIdAndVersion(newInstance.parameterSet)
		ObjectUtil.nullIdAndVersion(newInstance.variables)
		ObjectUtil.nullIdAndVersion(newInstance.parameterSet.variables)

		def name = newInstance.name + " copy"
		if (name.size() > 50) name = name.substring(0, 50)

		newInstance.name = name
		newInstance.parameterSet.name = name
		newInstance.createTime = new Date()
		newInstance.createdBy = currentUserOrError

		newInstance.save(flush: true)
		newInstance
	}
}