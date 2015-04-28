package com.banda.core.domain.task

import org.apache.commons.lang.StringUtils
import org.springframework.dao.DataIntegrityViolationException

import com.banda.core.util.ObjectUtil;

import edu.banda.coel.domain.service.EvolutionService
import edu.banda.coel.web.BaseDomainController

import org.hibernate.transform.Transformers
import org.hibernate.criterion.CriteriaSpecification
	
class TaskController extends BaseDomainController {

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','timeCreated','repeat']
		super.list(max)
	}

	def update(Long id, Long version) {
		def taskInstance = (id)
		if (!taskInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'task.label', default: 'Task'), id])
			redirect(action: "list")
			return
		}
	
		if (version != null) {
			if (taskInstance.version > version) {
				taskInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: 'task.label', default: 'Task')] as Object[],
						  "Another user has updated this Task while you were editing")
					render(view: "edit", model: [taskInstance: taskInstance])
					return
			}
		}

		taskInstance.properties = params
	
		if (!taskInstance.save(flush: true)) {
			render(view: "edit", model: [taskInstance: taskInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [message(code: 'task.label', default: 'Task'), taskInstance.id])
		redirect(action: "show", id: taskInstance.id)
	}
}