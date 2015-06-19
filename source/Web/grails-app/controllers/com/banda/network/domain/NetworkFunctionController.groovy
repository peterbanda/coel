package com.banda.network.domain

import com.banda.core.util.ObjectUtil
import com.banda.function.business.FunctionFactory
import edu.banda.coel.business.Replicator
import edu.banda.coel.web.BaseDomainController
import edu.banda.coel.web.BinaryStringRepresentations
import edu.banda.coel.web.NetworkCommonService
import grails.converters.JSON

class NetworkFunctionController extends BaseDomainController {

    def NetworkCommonService networkCommonService
	def functionFactory = new FunctionFactory()
    def replicator = Replicator.instance

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

		if (params.binaryValue) {
            def binaryString = params.binaryValue
            if (!params.boolean('lsbFirst'))
                binaryString = binaryString.reverse()
			def tableOutputs = binaryString.findAll { it.equals("0") || it.equals("1") }.collect{ if (it.equals("1")) true else false}

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
			handleObjectNotFound(id)
			return
		}
	
		if (version != null) {
			if (networkFunctionInstance.version > version) {
				setOlcFailureMessage(networkFunctionInstance)
				render(view: "edit", model: [networkFunctionInstance: networkFunctionInstance])
				return
			}
		}

		networkFunctionInstance.properties = params
	
		if (params.binaryValue) {
            def binaryString = params.binaryValue
            if (!params.boolean('lsbFirst'))
                binaryString = binaryString.reverse()

            def tableOutputs = binaryString.findAll { it.equals("0") || it.equals("1") }.collect{ if (it.equals("1")) true else false}
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

	def exportTransitionTable(Long id) {
        def lsbFirst = params.lsbfirst
        def networkFunctionInstance = getSafe(id)
        def text = networkCommonService.getTransitionTableInfo(networkFunctionInstance, lsbFirst)

        response.setHeader("Content-disposition", "attachment; filename=" + "transition_table_${id}");
        render(contentType: "text", text: text);
	}

	def getRepresentationsForBinaryString(String string) {
		def repres = new BinaryStringRepresentations()
		repres.binaryString = string.replaceAll("[^0-1]", "")
        if (!repres.binaryString.isEmpty()) {
		    repres.decimalString = new BigInteger(repres.binaryString, 2).toString()
		    repres.hexadecimalString = new BigInteger(repres.binaryString, 2).toString(16)
        }
		render repres as JSON
	}

	def getRepresentationsForDecimalString(String string) {
		def repres = new BinaryStringRepresentations()
		repres.decimalString = string.replaceAll("[^0-9]", "")
        if (!repres.decimalString.isEmpty()) {
            repres.binaryString = new BigInteger(repres.decimalString, 10).toString(2)
            repres.hexadecimalString = new BigInteger(repres.binaryString, 2).toString(16)
        }
		render repres as JSON
	}

    def getRepresentationsForHexadecimalString(String string) {
        def repres = new BinaryStringRepresentations()
        repres.hexadecimalString = string.replaceAll("[^0-9a-fA-F]", "")
        if (!repres.hexadecimalString.isEmpty()) {
            repres.binaryString = new BigInteger(repres.hexadecimalString, 16).toString(2)
            repres.decimalString = new BigInteger(repres.binaryString, 2).toString()
        }
        render repres as JSON
    }

    protected def copyInstance(instance) {
        def newInstance = replicator.cloneNetworkFunction(instance)
        replicator.nullIdAndVersion(newInstance)

        def name = newInstance.name + " copy"
        if (name.size() > 50) name = name.substring(0, 50)

        newInstance.name = name
        newInstance.timeCreated = new Date()
        newInstance.createdBy = currentUserOrError

        newInstance.save(flush: true)
        newInstance
    }
}