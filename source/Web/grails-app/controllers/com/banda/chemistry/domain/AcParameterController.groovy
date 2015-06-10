package com.banda.chemistry.domain

import com.banda.chemistry.business.ArtificialChemistryUtil
import edu.banda.coel.web.BaseDomainController

class AcParameterController extends BaseDomainController {

	def create() {
		def instance =  new AcParameter(params)

		if (params.parentSet.id) {
			def parameterSet = AcParameterSet.get(params.parentSet.id)
			parameterSet.addVariable(instance)
			[instance: instance]
		} else {
			flash.message = "Parameter set expected for a new AC parameter"
			redirect(controller: "acSpeciesSet", action: "list")
			return
		}
	}

	def save() {
		def acParameterInstance = new AcParameter(params)

		def acUtil = ArtificialChemistryUtil.instance
		// Parameter Set
		AcParameterSet parameterSet = AcParameterSet.get(params.parentSet.id)
		parameterSet.addVariable(acParameterInstance)
		// TODO: Is sort order needed?
		acParameterInstance.sortOrder = acParameterInstance.variableIndex
		// Evolution Function
		acUtil.setEvolFunctionFromString(params.evolFunction.formula, acParameterInstance)

		if (!acParameterInstance.save(flush: true)) {
			render(view: "create", model: [instance: acParameterInstance])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [message(code: 'acParameter.label', default: 'AcParameter'), acParameterInstance.id])
		redirect(action: "show", id: acParameterInstance.id)
	}
	
	def update(Long id, Long version) {
		def acParameterInstance = getSafe(id)
		if (!acParameterInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (acParameterInstance.version > version) {
				setOlcFailureMessage(acParameterInstance)
				render(view: "edit", model: [instance: acParameterInstance])
				return
			}

		// Parameter set
//		def oldParameterSetId = acParameterInstance.parentSet.id
//		def newParameterSetId = params.parentSet.id
//		if (oldParameterSetId != newParameterSetId) {
//			AcParameterSet newParameterSet = AcParameterSet.get(newParameterSetId)
//			newParameterSet.addVariable(acParameterInstance)
//		}

		// Setting Function
		def acUtil = ArtificialChemistryUtil.instance
		acUtil.setEvolFunctionFromString(params.evolFunction.formula, acParameterInstance)

		acParameterInstance.properties = params
	
		if (!acParameterInstance.save(flush: true)) {
			render(view: "edit", model: [acParameterInstance: acParameterInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acParameterInstance.id])
		redirect(action: "show", id: acParameterInstance.id)
	}

	protected def deleteInstance(instance) {
		def parameterSet = instance.parentSet
		AcParameter.withTransaction{ status ->
			parameterSet.removeVariable(instance)
			instance.delete(flush: true)
		}
	}

	protected def notFoundRedirect(id) {
		redirect(controller: "acParameterSet", action: "list")
	}

	protected def deleteSuccessfulRedirect(instance, owner) {
		redirect(controller: "acParameterSet", action: "show", id: owner.id)
	}

	protected def deleteMultiSuccessfulRedirect(instancesWithOwners) {
		redirect(controller: "acParameterSet", action: "show", id: instancesWithOwners.first().value.id)
	}

	protected def getOwner(instance) {
		instance.parentSet
	}
}