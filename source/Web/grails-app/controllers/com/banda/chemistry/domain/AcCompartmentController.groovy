package com.banda.chemistry.domain

import com.banda.chemistry.business.AcRateConstantUtil
import com.banda.chemistry.business.AcReplicator
import com.banda.chemistry.domain.AcReaction.ReactionDirection
import edu.banda.coel.web.BaseDomainController
import edu.banda.coel.domain.service.ArtificialChemistryService

class AcCompartmentController extends BaseDomainController {

    def ArtificialChemistryService artificialChemistryService
	def acRateUtil = AcRateConstantUtil.instance
	def replicator = AcReplicator.instance

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','label','reactionSet']
		super.list(max)
	}

	def create() {
		def instance = new AcCompartment(params)

		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','label']
		params.sort = 'id'
		params.order = 'desc'

		def acReactionSets = AcReactionSet.listWithParamsAndProjections(params)

		def parentId
		if (params.'parent.id') {
			parentId = params.long('parent.id')
			def parent = getSafe(parentId)
			instance.reactionSet = parent.reactionSet
		}

		[instance: instance, acReactionSets : acReactionSets, parentId : parentId]
	}

	def save() {
		def acCompartmentInstance = new AcCompartment(params)
		acCompartmentInstance.createdBy = currentUserOrError

		def saved = false
		if (params.reactionSetChoice == '1')
			saved = saveWithNewReactionSet(acCompartmentInstance)
		else
			saved = acCompartmentInstance.save(flush: true)

		if (!saved) {
			if (!isAdmin())
				params.createdBy = getCurrentUserOrError()

			params.projections = ['id','label']
			params.sort = 'id'
			params.order = 'desc'

			def acReactionSets = AcReactionSet.listWithParamsAndProjections(params)
			render(view: "create", model: [instance: acCompartmentInstance, acReactionSets : acReactionSets, parentId : params.parentId])
			return
		}

		if (params.parentId) {
			def parentCompartment = getSafe(params.long('parentId'))
			parentCompartment.addSubCompartment(acCompartmentInstance)
			if (!parentCompartment.save(flush: true)) {
				render(view: "create", model: [instance: acCompartmentInstance])
				return
			}
		}

		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, acCompartmentInstance.id])
		if (params.reactionSetChoice == '1')
			redirect(controller: "acReactionSet", action: "show", id: acCompartmentInstance.reactionSet.id)
		else
			redirect(action: "show", id: acCompartmentInstance.id)
	}

	private def saveWithNewReactionSet(instance) {
		def label = instance.label
		def speciesSet = createSpeciesSet(label)

		def reactionSet = new AcReactionSet()
		reactionSet.label = label
		reactionSet.createdBy = currentUserOrError
		reactionSet.speciesSet = speciesSet

		instance.reactionSet = reactionSet

		def saved = false;
		if (speciesSet.save())
			if (reactionSet.save())
				if (instance.save(flush: true))
					saved = true

		saved
	}

	private def createSpeciesSet(name) {
		def acSpeciesSetInstance = new AcSpeciesSet()
		acSpeciesSetInstance.name = name
		acSpeciesSetInstance.createdBy = currentUserOrError
		acSpeciesSetInstance.initVarSequenceNum()

		AcParameterSet parameterSet = new AcParameterSet()
		parameterSet.name = acSpeciesSetInstance.name
		parameterSet.createTime = acSpeciesSetInstance.createTime
		parameterSet.createdBy = acSpeciesSetInstance.createdBy

		acSpeciesSetInstance.parameterSet = parameterSet
		parameterSet.speciesSet = acSpeciesSetInstance
		acSpeciesSetInstance
	}

	def show(Long id) {
		def result = super.show(id)
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		params.projections = ['id','label']
		params.sort = 'id'
		params.order = 'desc'
		def acCompartments = AcCompartment.listWithParamsAndProjections(params)
		result << [acCompartments : acCompartments]
	}
	
	def edit(Long id) {
		def result = super.edit(id)
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()
			
		params.projections = ['id','label']
		params.sort = 'id'
		params.order = 'desc'
		def acReactionSets = AcReactionSet.listWithParamsAndProjections(params)
		def acCompartments = AcCompartment.listWithParamsAndProjections(params)
		result << [acCompartments : acCompartments, acReactionSets : acReactionSets]
	}

	def update(Long id, Long version) {
		def acCompartmentInstance = getSafe(id)
		if (!acCompartmentInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (acCompartmentInstance.version > version) {
				setOlcFailureMessage(acCompartmentInstance)
				render(view: "edit", model: [instance: acCompartmentInstance])
				return
			}

		acCompartmentInstance.properties = params

		if (!updateSafe(acCompartmentInstance)) {
			render(view: "edit", model: [instance: acCompartmentInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acCompartmentInstance.id])
		redirect(action: "show", id: acCompartmentInstance.id)
	}

	def importRatesFromText(Long id) {
		def acCompartmentInstance = getSafe(id)
		acRateUtil.setRateConstantsFromString(params.ratesInput, acCompartmentInstance, ReactionDirection.Both, null, null)
		def reactionSets = getReactionSets(acCompartmentInstance)
		reactionSets.each{ reactionSet ->
			// just to make it dirty
			reactionSet.reactions.each{ reaction -> reaction.setForwardRateConstants(reaction.getForwardRateConstants().clone()) }
		}
		if (acCompartmentInstance.save(flush: true)) {
			flash.message = "${message(code: 'default.updated.message', args: [doClazzMessageLabel, acCompartmentInstance.id])}"
		}
		redirect(action: "show", id: acCompartmentInstance.id)
	}

	def importSbml = {
		def sbmlFile = request.getFile('sbmlFile')
		if (!sbmlFile.empty) {
			def sbmlFileContent = sbmlFile.inputStream.text
			def fileName = sbmlFile.originalFilename
			artificialChemistryService.saveSbmlModelAsCompartment(sbmlFileContent, fileName[0..-5])
			flash.message = 'SBML model has been imported.'
		}
		else {
			flash.message = 'Filename cannot be empty.'
		}
		redirect(action: "list")
	}

	def addSubCompartment(Long id, Long subCompartmentId) {
		def parentCompartment = getSafe(id)
		def subCompartment = getSafe(subCompartmentId)
		parentCompartment.addSubCompartment(subCompartment)

		parentCompartment.save(flush: true)
		redirect(action: "show", id: id)
	}

	protected def deleteInstance(instance) {
		AcCompartment.withTransaction{ status ->
			// remove sub associations
			def subAssocs = []
			subAssocs.addAll(instance.subCompartmentAssociations)
			subAssocs.each{ removeCompartmentAssociation(it)}

			// remove parent associations
			def parentAssocs = []
			parentAssocs.addAll(instance.parentCompartmentAssociations)
			parentAssocs.each{ removeCompartmentAssociation(it)}

			instance.delete(flush: true)
		}
	}

	protected def copyInstance(instance) {
		def compartmentCloneMap = [ : ]
		def channelCloneMap = [ : ]
		replicator.cloneCompartmentWithChannelsRecursively(instance, compartmentCloneMap, channelCloneMap)
		compartmentCloneMap.values().each{resetCompartmentClone(it)}
		saveCompartmentRecursively(instance, compartmentCloneMap, channelCloneMap)
		compartmentCloneMap.get(instance)
	}

	def removeSubCompartmentAssocFromParent(Long id) {
		def compartmentAssociation = AcCompartmentAssociation.get(id)
		def parentCompartment = compartmentAssociation.parentCompartment
		def subCompartment = compartmentAssociation.subCompartment

		removeCompartmentAssociation(compartmentAssociation)

		flash.message = message(code: 'default.disassociated.message', args: [doClazzMessageLabel, subCompartment.id])
		redirect(action: "show", id: parentCompartment.id)
	}

	private def removeCompartmentAssociation(compartmentAssociation) {
		def parentCompartment = compartmentAssociation.parentCompartment
		def subCompartment = compartmentAssociation.subCompartment

		AcCompartmentAssociation.withTransaction{ status ->
			parentCompartment.removeSubCompartmentAssociation(compartmentAssociation)
			compartmentAssociation.delete(flush: true)
		}
	}

	private def getReactionSets(AcCompartment compartment) {
		def reactionSets = new HashSet<AcReactionSet>()
		reactionSets.add(compartment.getReactionSet())
		for (AcCompartment subCompartment : compartment.getSubCompartments()) {
			reactionSets.addAll(getReactionSets(subCompartment));
		}
		reactionSets
	}

	private def saveCompartmentRecursively(compartmentInstance, compartmentCloneMap, channelCloneMap) {
		def compartmentClone = compartmentCloneMap.get(compartmentInstance)
		compartmentClone.getSubCompartments().each{ compartmentClone.removeSubCompartment(it)}

		def newInstances = compartmentInstance.getSubCompartments().collect{
			saveCompartmentRecursively(it, compartmentCloneMap, channelCloneMap)
		}.flatten()

		compartmentInstance.getSubCompartments().each{
			def subCompartmentClone = compartmentCloneMap.get(it)
			compartmentClone.addSubCompartment(subCompartmentClone)
		}

		def newId = saveCompartment(compartmentInstance, compartmentClone)
		newInstances += compartmentClone

		compartmentClone.subChannelGroups.each{ subChannelGroupClone ->
			def channels = []
			channels.addAll(subChannelGroupClone.channels)
			subChannelGroupClone.channels = new ArrayList<AcCompartmentChannel>();
			subChannelGroupClone.save(flush: true)
			
			channels.each{ subChannelGroupClone.addChannel(it) }

			subChannelGroupClone.save(flush: true)
		}
		newInstances
	}

	private def saveCompartment(acCompartmentInstance, acCompartmentInstanceClone) {
		if (!acCompartmentInstanceClone.save(flush: true)) {
			render(view: "edit", model: [instance: acCompartmentInstance])
			return
		}
		acCompartmentInstanceClone.id
	}

	private def resetCompartmentClone(acCompartmentInstanceClone) {
		def label = acCompartmentInstanceClone.label + " copy"
		if (label.size() > 100) label = label.substring(0, 100)
		acCompartmentInstanceClone.label = label

		acCompartmentInstanceClone.createTime = new Date()
		acCompartmentInstanceClone.createdBy = currentUserOrError
		replicator.nullIdAndVersionRecursively(acCompartmentInstanceClone)
	}
}