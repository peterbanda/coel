package edu.banda.coel.business.evo.fitness

import java.{lang => jl}
import com.banda.chemistry.business.AcEvaluationBOFactory
import com.banda.chemistry.business.ChemistryRunnableFactory
import com.banda.chemistry.business.ChemistryInterpretationFactory
import com.banda.chemistry.domain.AcReaction.ReactionDirection
import com.banda.chemistry.domain.AcReactionSet
import edu.banda.coel.task.chemistry.AcRunAndTranslateTask
import com.banda.math.domain.evo.ArrayChromosome
import com.banda.chemistry.business.AcRateConstantUtil
import edu.banda.coel.domain.evo.EvoAcRateConstantTask

class EvoAcRateConstantFitnessEvaluatorBO(
	evoTask : EvoAcRateConstantTask,
	chemistryRunnableFactory : ChemistryRunnableFactory,
	chemInterpretationFactory : ChemistryInterpretationFactory,
	acEvaluationBOFactory : AcEvaluationBOFactory
	) extends EvoAcFitnessEvaluatorBO[ArrayChromosome[jl.Double]](
	evoTask, chemistryRunnableFactory, chemInterpretationFactory, acEvaluationBOFactory) {

	override def adaptTask(task : AcRunAndTranslateTask, chromosome : ArrayChromosome[jl.Double]) {
        AcRateConstantUtil.getInstance.setRateConstants(
		        task.getCompartment, chromosome.getCode(), ReactionDirection.Both,
		        evoTask.getFixedRateReactions, evoTask.getFixedRateReactionGroups)
	}
}