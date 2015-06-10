package com.banda.chemistry.domain

import edu.banda.coel.web.BaseDomainController

class ArtificialChemistrySpecBoundController extends BaseDomainController {

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','name', 'timeCreated']
		super.list(max)
	}

	def createSymmetricInstance() {
		render(view: "create", model: [artificialChemistrySpecBoundInstance: new AcSymmetricSpecBound(params)])
	}

	def createDNAStrandInstance() {
		render(view: "create", model: [artificialChemistrySpecBoundInstance: new AcDNAStrandSpecBound(params)])
	}

	def save() {
		def artificialChemistrySpecBoundInstance = null
		if (params.singleStrandsNumFrom) {
			artificialChemistrySpecBoundInstance = new AcDNAStrandSpecBound(params)
		} else {
			artificialChemistrySpecBoundInstance = new AcSymmetricSpecBound(params)
		}
		artificialChemistrySpecBoundInstance.createdBy = currentUserOrError
		if (!artificialChemistrySpecBoundInstance.save(flush: true)) {
			render(view: "create", model: [instance: artificialChemistrySpecBoundInstance])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, artificialChemistrySpecBoundInstance.id])
		redirect(action: "show", id: artificialChemistrySpecBoundInstance.id)
	}
	
	def update(Long id, Long version) {
		def artificialChemistrySpecBoundInstance = getSafe(id)
		if (!artificialChemistrySpecBoundInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (artificialChemistrySpecBoundInstance.version > version) {
				setOlcFailureMessage(artificialChemistrySpecBoundInstance)
				render(view: "edit", model: [instance: artificialChemistrySpecBoundInstance])
				return
			}

		artificialChemistrySpecBoundInstance.properties = params
	
		if (!artificialChemistrySpecBoundInstance.save(flush: true)) {
			render(view: "edit", model: [instance: artificialChemistrySpecBoundInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, artificialChemistrySpecBoundInstance.id])
		redirect(action: "show", id: artificialChemistrySpecBoundInstance.id)
	}
}