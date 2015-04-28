package com.banda.network.domain

import edu.banda.coel.web.BaseDomainController

import com.banda.function.business.FunctionFactory
import com.banda.function.domain.Expression
import com.banda.function.domain.FunctionHolder
import com.banda.core.util.ConversionUtil

import org.springframework.dao.DataIntegrityViolationException

class NetworkFunctionController extends BaseDomainController {

	def functionFactory = new FunctionFactory()

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','name','multiComponentUpdaterType']
		super.list(max)
	}

	def create() {
		def networkFunctionInstance = new NetworkFunction(params)

		def parentFunctionId = ""
		if (params.'parentFunction.id') {
			parentFunctionId = params.'parentFunction.id'
		}

		[instance: networkFunctionInstance, parentFunctionId : parentFunctionId]
	}

	def save() {
		def networkFunctionInstance = new NetworkFunction(params)

		if (params.functionInput) {
			def integerTableOutputs = ConversionUtil.convertToList(Integer.class, params.functionInput, "Table Output")
			def tableOutputs = integerTableOutputs.collect{ if (it.equals(new Integer(1))) true else false}
			def transitionTable = functionFactory.createBoolTransitionTable(tableOutputs)
			networkFunctionInstance.setFunction(transitionTable)
		}

		if (params.parentFunctionId) {
			def parentFunction = getSafe(params.parentFunctionId)
			parentFunction.addLayerFunction(networkFunctionInstance)
			networkFunctionInstance.setIndex(parentFunction.layerFunctions.size() + 1)
			if (!parentFunction.save(flush: true)) {
				render(view: "create", model: [networkFunctionInstance: networkFunctionInstance])
				return
			}
		}

		networkFunctionInstance.createdBy = currentUserOrError

		if (!networkFunctionInstance.save(flush: true)) {
			render(view: "create", model: [instance: networkFunctionInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'networkFunction.label', default: 'Network Function'), networkFunctionInstance.id])
		redirect(action: "show", id: networkFunctionInstance.id)
	}
	
	def update(Long id, Long version) {
		def networkFunctionInstance = getSafe(id)
		if (!networkFunctionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'networkFunction.label', default: 'Network Function'), id])
			redirect(action: "list")
			return
		}
	
		if (version != null) {
			if (networkFunctionInstance.version > version) {
				networkFunctionInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: 'networkFunction.label', default: 'Network Function')] as Object[],
						  "Another user has updated this Network Function while you were editing")
					render(view: "edit", model: [networkFunctionInstance: networkFunctionInstance])
					return
			}
		}

		networkFunctionInstance.properties = params
	
		if (params.functionInput) {
			def integerTableOutputs = ConversionUtil.convertToList(Integer.class, params.functionInput, "Table Output")
			def tableOutputs = integerTableOutputs.collect{ if (it.equals(new Integer(1))) true else false}
			def newTransitionTable = functionFactory.createBoolTransitionTable(tableOutputs)

			if (networkFunctionInstance.function == null) {
				networkFunctionInstance.function = newTransitionTable
			} else {
				networkFunctionInstance.function.outputs = tableOutputs
				networkFunctionInstance.function.arity = newTransitionTable.arity
			}
		}

		if (!networkFunctionInstance.save(flush: true)) {
			render(view: "edit", model: [instance: networkFunctionInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [message(code: 'networkFunction.label', default: 'Network Function'), networkFunctionInstance.id])
		redirect(action: "show", id: networkFunctionInstance.id)
	}
}