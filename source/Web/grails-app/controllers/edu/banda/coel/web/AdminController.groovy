package edu.banda.coel.web

import com.banda.core.grid.GridMetrics
import edu.banda.coel.domain.service.TaskManager
import grails.converters.JSON

class AdminController {

	static navigationScope = "none"

	def TaskManager taskManager

	def grid = {
		def metrics = new GridMetrics()
		[metrics : metrics]
	}

	def getGridMetrics() {
		def metrics = taskManager.getGridMetrics()
		render metrics as JSON
	}

	def cancelTask() {
		try {
			taskManager.cancelTask(params.taskId)
		} catch (e) {
			render e.message
			return
		}
		render "Task successfully cancelled."
	}
}