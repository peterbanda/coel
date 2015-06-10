package com.banda.chemistry.domain

import com.banda.chemistry.business.AcKineticsBO
import com.banda.chemistry.business.AcRateConstantUtil
import com.banda.core.util.ObjectUtil
import com.banda.core.util.ParseUtil
import edu.banda.coel.web.BaseDomainController

class AcReactionGroupController extends BaseDomainController {

	def acRateUtil = AcRateConstantUtil.instance

	def create() {
		def instance =  new AcReactionGroup()
		instance.properties = params

		if (params.reactionSet.id) {
			def reactionSet = AcReactionSet.get(params.reactionSet.id)
			reactionSet.addGroup(instance)
			[instance: instance]
		} else {
			flash.message = "Reaction set expected for a new reaction group"
			redirect(controller: "acReactionSet", action: "list")
			return
		}
	}

	def save() {
		def acReactionGroupInstance = new AcReactionGroup(params)

		acReactionGroupInstance.createdBy = currentUserOrError

		// Reaction set
		AcReactionSet reactionSet = AcReactionSet.get(params.reactionSet.id)
		reactionSet.addGroup(acReactionGroupInstance)

		// Reactions
		addOrRemoveReactions(acReactionGroupInstance)

		if (!acReactionGroupInstance.save(flush: true)) {
			render(view: "create", model: [instance: acReactionGroupInstance])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, acReactionGroupInstance.id])
		redirect(controller:"acReactionSet", action: "show", id: reactionSet.id)
	}

	def show(Long id) {
		def result = super.show(id)
		result << [rateConstants : getRateConstants(result.instance)]	
	}

	def edit(Long id) {
		def result = super.edit(id)
		result << [rateConstants : getRateConstants(result.instance)]
	}

	def update(Long id, Long version) {
		def acReactionGroupInstance = getSafe(id)
		if (!acReactionGroupInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (acReactionGroupInstance.version > version) {
				setOlcFailureMessage(acReactionGroupInstance)
				render(view: "edit", model: [instance: acReactionGroupInstance, rateConstants : getRateConstants(acReactionGroupInstance)])
				return
			}

		// Reactions
		addOrRemoveReactions(acReactionGroupInstance)

		acReactionGroupInstance.properties = params

		if (params.forwardRateConstants)
			if (acReactionGroupInstance.reactions.isEmpty()) {
				acReactionGroupInstance.errors.rejectValue("reactions", "Cannot set the rate constants, because the reaction group contains no reactions.")
			} else if (!isSameRateConstantsCount(acReactionGroupInstance)) {
				acReactionGroupInstance.errors.rejectValue("reactions", "Cannot set the rate constants, because the associated reactions are not compatible.")
			} else {
				int requiredRateConstantsNum = AcKineticsBO.createInstance(acReactionGroupInstance.reactions[0], true).getRequiredRateConstantsNum()
				def rateConstants = ParseUtil.parseArray(params.forwardRateConstants, Double.class, "Rate constants", ",")
				if (rateConstants.size() != requiredRateConstantsNum) {
					acReactionGroupInstance.errors.rejectValue("reactions", "The given number of rate constants '" + rateConstants.size() + "' do not match the expected one '" + requiredRateConstantsNum + "'.")
				} else {
					acReactionGroupInstance.reactions.each{ reaction ->
						reaction.setRateConstants(rateConstants.toArray(new Double[0]), true);
					}
				}
			}

		if (acReactionGroupInstance.hasErrors() || !acReactionGroupInstance.save(flush: true)) {
			render(view: "edit", model: [instance: acReactionGroupInstance, rateConstants : getRateConstants(acReactionGroupInstance)])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acReactionGroupInstance.id])
		redirect(controller:"acReactionSet", action: "show", id: acReactionGroupInstance.reactionSet.id)
	}

	protected def deleteInstance(instance) {
		def reactionSet = instance.reactionSet
		AcReactionGroup.withTransaction{ status ->
			reactionSet.removeGroup(instance)
			instance.removeAllReactions()
			instance.delete(flush: true)
		}
	}

	protected def notFoundRedirect(id) {
		redirect(controller: "acReactionSet", action: "list")
	}

	protected def deleteSuccessfulRedirect(instance, owner) {
		redirect(controller: "acReactionSet", action: "show", id: owner.id)
	}

	protected def deleteMultiSuccessfulRedirect(instancesWithOwners) {
		redirect(controller: "acReactionSet", action: "show", id: instancesWithOwners.first().value.id)
	}

	protected def getOwner(instance) {
		instance.reactionSet
	}

	private def addOrRemoveReactions(acReactionGroupInstance) {
		acReactionGroupInstance.allSetReactions.eachWithIndex() { reaction, i ->
			def contains = request.getParameter("contains${i}")
			def contained = ObjectUtil.areObjectsEqual(acReactionGroupInstance, reaction.group)
			if (contains) {
				if (!contained) {
					reaction.removeFromGroup()
					acReactionGroupInstance.addReaction(reaction)
				}
			} else {
				if (contained) {
					acReactionGroupInstance.removeReaction(reaction)
				}
			}
		}
	}

	private def getRateConstants(reactionGroup) {
		def prevReaction
		def allRatesEqual = reactionGroup.reactions.every { reaction ->
			def result = true
			if (prevReaction) {
				result = ObjectUtil.areObjectsEqual(prevReaction.getRateConstants(true), reaction.getRateConstants(true))
			}
			prevReaction = reaction
			result
		}
		if (prevReaction && allRatesEqual)
			prevReaction.getRateConstants(true)
		else {
			if (prevReaction)
				flash.message = 'The reaction group is not consistent, since its reactions do not share the same rate constants! Please fix.'
			[]
		}
	}

	private def isSameRateConstantsCount(reactionGroup) {
		def rateCount
		reactionGroup.reactions.every { reaction ->
			if (rateCount) {
				rateCount == reaction.getRateConstants(true).size()
			} else {
				rateCount = reaction.getRateConstants(true).size()
				true
			}
		}
	}
}