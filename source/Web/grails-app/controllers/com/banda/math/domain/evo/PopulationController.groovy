package com.banda.math.domain.evo

import com.banda.chemistry.business.AcRateConstantUtil
import com.banda.chemistry.domain.AcCompartment
import com.banda.chemistry.domain.AcReaction.ReactionDirection
import com.banda.chemistry.domain.AcReactionSet
import edu.banda.coel.web.BaseDomainController

class PopulationController extends BaseDomainController {

	static allowedMethods = [create:"", edit: "", save: "", update: "", delete: ""]  // only show is allowed

	def acRateUtil = AcRateConstantUtil.instance

	def importBestChromosome(Long id) {
		def populationInstance = getSafe(id)
		def evoRunInstance = populationInstance.evolutionRun
		def lastBestChromosomeCode = populationInstance?.bestOrLastChromosome?.code
		def evoAcTask = evoRunInstance.evoTask
		def acCompartmentInstance = evoAcTask.ac.skinCompartment
		acRateUtil.setRateConstants(
			acCompartmentInstance,
			lastBestChromosomeCode,
			ReactionDirection.Both,
			evoAcTask.fixedRateReactions,
			evoAcTask.fixedRateReactionGroups)

		def reactionSets = getReactionSets(acCompartmentInstance)
		reactionSets.each{ reactionSet ->
			// just to make it dirty
			reactionSet.reactions.each{ reaction -> reaction.setForwardRateConstants(reaction.getForwardRateConstants().clone()) }
		}
		if (acCompartmentInstance.save(flush: true)) {
			flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, acCompartmentInstance.id])
		}
		redirect(action: "show", id: id)
	}

	private def getReactionSets(AcCompartment compartment) {
		def reactionSets = new HashSet<AcReactionSet>()
		reactionSets.add(compartment.getReactionSet())
		for (AcCompartment subCompartment : compartment.getSubCompartments()) {
			reactionSets.addAll(getReactionSets(subCompartment));
		}
		reactionSets
	}
}