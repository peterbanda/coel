package com.banda.chemistry.domain

import com.banda.function.domain.ODESolverType
import edu.banda.coel.web.BaseDomainController

class AcSimulationConfigController extends BaseDomainController {

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','timeCreated','name']
		super.list(max)
	}

	def create() {
		def instance = new AcSimulationConfig(params)
		instance.odeSolverType = ODESolverType.RungeKutta4
		[instance: instance]
	}

	def save() {
		def acSimulationConfigInstance = new AcSimulationConfig(params)
		acSimulationConfigInstance.createdBy = currentUserOrError
		if (!acSimulationConfigInstance.save(flush: true)) {
			render(view: "create", model: [instance: acSimulationConfigInstance])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, acSimulationConfigInstance.id])
		redirect(action: "show", id: acSimulationConfigInstance.id)
	}
	
	def update(Long id, Long version) {
		def acSimulationConfigInstance = getSafe(id)
		if (!acSimulationConfigInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (acSimulationConfigInstance.version > version) {
				setOlcFailureMessage(acSimulationConfigInstance)
				render(view: "edit", model: [instance: acSimulationConfigInstance])
				return
			}

		acSimulationConfigInstance.properties = params
	
		if (!acSimulationConfigInstance.save(flush: true)) {
			render(view: "edit", model: [instance: acSimulationConfigInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acSimulationConfigInstance.id])
		redirect(action: "show", id: acSimulationConfigInstance.id)
	}

	protected def copyInstance(instance) {
		def newInstance = genericReflectionProvider.clone(instance)
		newInstance.id = null
		newInstance.version = 1l

		def name = newInstance.name + " copy"
		if (name.size() > 100) name = name.substring(0, 100)

		newInstance.name = name
		newInstance.timeCreated = new Date()
		newInstance.createdBy = currentUserOrError

		newInstance.save(flush: true)
		newInstance
	}
}