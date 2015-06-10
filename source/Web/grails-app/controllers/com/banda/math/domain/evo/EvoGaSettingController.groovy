package com.banda.math.domain.evo

import com.banda.core.util.ObjectUtil
import edu.banda.coel.web.BaseDomainController

class EvoGaSettingController extends BaseDomainController {

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','createTime','name','eliteNumber','populationSize']
		super.list(max)
	}

	def create() {
		[instance: new EvoGaSetting(params)]
	}
	
	def save() {
		def gaSettingInstance = new EvoGaSetting(params)
		gaSettingInstance.createTime = new Date()
		gaSettingInstance.createdBy = currentUserOrError

		if (!gaSettingInstance.save(flush: true)) {
			render(view: "create", model: [instance: gaSettingInstance])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, gaSettingInstance.id])
		redirect(action: "show", id: gaSettingInstance.id)
	}
	
	def update(Long id, Long version) {
		def gaSettingInstance = getSafe(id)
		if (!gaSettingInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (gaSettingInstance.version > version) {
				setOlcFailureMessage(gaSettingInstance)
				render(view: "edit", model: [instance: gaSettingInstance])
				return
			}

		gaSettingInstance.properties = params
	
		if (!gaSettingInstance.save(flush: true)) {
			render(view: "edit", model: [instance: gaSettingInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, gaSettingInstance.id])
		redirect(action: "show", id: gaSettingInstance.id)
	}

	protected def copyInstance(instance) {
		def newInstance = genericReflectionProvider.clone(instance)
		ObjectUtil.nullIdAndVersion(newInstance)

		def name = newInstance.name + " copy"
		if (name.size() > 100) name = name.substring(0, 100)

		newInstance.name = name
		newInstance.createTime = new Date()
		newInstance.createdBy = currentUserOrError

		newInstance.createTime = new Date()
		newInstance.createdBy = currentUserOrError

		newInstance.save(flush: true)
		newInstance
	}
}