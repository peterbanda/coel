package edu.banda.coel.business.evo;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.chemistry.business.*;
import com.banda.chemistry.domain.*;
import com.banda.chemistry.domain.AcReaction.ReactionDirection;
import com.banda.core.domain.ValueBound;
import com.banda.core.reflection.ReflectionProvider;
import com.banda.math.business.evo.EvoArrayChromManipulatorBO;
import com.banda.math.business.evo.EvoEnumeratedTestSampleGeneratorBO;
import com.banda.math.business.evo.EvoTaskBO;
import com.banda.math.domain.evo.ArrayChromosome;
import com.banda.math.domain.evo.Chromosome;
import com.banda.math.domain.evo.EvoGaSetting;

import edu.banda.coel.business.evo.fitness.*;
import edu.banda.coel.domain.evo.EvoAcRateConstantTask;

final class EvoAcRateConstantTaskBOFactory implements EvoTaskBOFactory<EvoAcRateConstantTask>, Serializable {

	private final ChemistryRunnableFactory chemistryRunnableFactory;
	private final ChemistryInterpretationFactory chemInterpretationFactory;
	private final AcEvaluationBOFactory acEvaluationBOFactory;

	private final AcRateConstantUtil acRateUtil = AcRateConstantUtil.getInstance();

	@Autowired
	private ReflectionProvider<? extends Chromosome<?>> chromosomeRF;
	
	public EvoAcRateConstantTaskBOFactory(
		ChemistryRunnableFactory chemistryRunnableFactory,
		ChemistryInterpretationFactory chemInterpretationFactory,
		AcEvaluationBOFactory acEvaluationBOFactory
	) {
		this.chemistryRunnableFactory = chemistryRunnableFactory;
		this.chemInterpretationFactory = chemInterpretationFactory;
		this.acEvaluationBOFactory = acEvaluationBOFactory;
	}

	@Override 
	public EvoTaskBO<?, ?, ?> createInstance(EvoAcRateConstantTask evoAcTask, EvoGaSetting gaSetting) {
		Collection<ValueBound<Double>> rateConstantBounds = acRateUtil.getRateConstantBounds(
				evoAcTask.getAc().getSkinCompartment(), evoAcTask.getReactionRateConstantTypeBoundMap(), ReactionDirection.Both,
				evoAcTask.getFixedRateReactions(), evoAcTask.getFixedRateReactionGroups());

		EvoTaskBO<?, ?, ?> evoTaskBO = new EvoTaskBO<ArrayChromosome<Double>, Double[], AcInteractionSeries> (
			new EvoArrayChromManipulatorBO<Double>(
				(ReflectionProvider<ArrayChromosome<Double>>) chromosomeRF,
				gaSetting.getBitMutationType(),
				gaSetting.getPertrubMutationStrength(),
				Double.class,
				rateConstantBounds.toArray(new ValueBound[0])),

			new EvoAcRateConstantFitnessEvaluatorBO(
				evoAcTask,
				chemistryRunnableFactory,
				chemInterpretationFactory,
				acEvaluationBOFactory),

			new EvoEnumeratedTestSampleGeneratorBO<AcInteractionSeries>(
				evoAcTask.getActionSeries(),
				evoAcTask.getAsRepetitions()));

		return evoTaskBO;
	}
}