package edu.banda.coel.business.evo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.chemistry.business.AcEvaluationBOFactory;
import com.banda.chemistry.business.ChemistryInterpretationFactory;
import com.banda.chemistry.business.ChemistryRunnableFactory;
import com.banda.chemistry.domain.AcInteraction;
import com.banda.chemistry.domain.AcInteractionSeries;
import com.banda.chemistry.domain.AcInteractionVariableAssignment;
import com.banda.chemistry.domain.AcSpeciesInteraction;
import com.banda.core.domain.DomainObject.DomainObjectKeyComparator;
import com.banda.core.domain.ValueBound;
import com.banda.core.reflection.ReflectionProvider;
import com.banda.math.business.evo.EvoArrayChromManipulatorBO;
import com.banda.math.business.evo.EvoEnumeratedTestSampleGeneratorBO;
import com.banda.math.business.evo.EvoTaskBO;
import com.banda.math.domain.evo.ArrayChromosome;
import com.banda.math.domain.evo.Chromosome;
import com.banda.math.domain.evo.EvoGaSetting;

import edu.banda.coel.business.evo.fitness.EvoAcInteractionSeriesFitnessEvaluatorBO;
import edu.banda.coel.domain.evo.EvoAcInteractionSeriesTask;

final class EvoAcInteractionSeriesTaskBOFactory implements EvoTaskBOFactory<EvoAcInteractionSeriesTask>, Serializable {

	private final ChemistryRunnableFactory chemistryRunnableFactory;
	private final ChemistryInterpretationFactory chemInterpretationFactory;
	private final AcEvaluationBOFactory acEvaluationBOFactory;

	@Autowired
	private ReflectionProvider<? extends Chromosome<?>> chromosomeRF;
	
	public EvoAcInteractionSeriesTaskBOFactory(
		ChemistryRunnableFactory chemistryRunnableFactory,
		ChemistryInterpretationFactory chemInterpretationFactory,
		AcEvaluationBOFactory acEvaluationBOFactory
	) {
		this.chemistryRunnableFactory = chemistryRunnableFactory;
		this.chemInterpretationFactory = chemInterpretationFactory;
		this.acEvaluationBOFactory = acEvaluationBOFactory;
	}

	@Override 
	public EvoTaskBO<?, ?, ?> createInstance(EvoAcInteractionSeriesTask evoTask, EvoGaSetting gaSetting) {
		Collection<ValueBound<Double>> bounds = new ArrayList<ValueBound<Double>>();

		final Map<AcInteractionVariableAssignment, ValueBound<Double>> variableAssignmentMap = evoTask.getVariableAssignmentBoundMap();
		final Map<AcSpeciesInteraction, ValueBound<Double>> speciesAssignmentMap = evoTask.getSpeciesAssignmentBoundMap();

	    List<AcInteractionSeries> interactionSeries = collectInteractionSeries(evoTask.getActionSeries());
	    Collections.sort(interactionSeries, new DomainObjectKeyComparator<Long>());

	    for (AcInteractionSeries actionSeries : interactionSeries)
	    	for (AcInteraction interaction : actionSeries.getActions()) {

	    		// collect variable assignment bounds
	    		List<AcInteractionVariableAssignment> variableAssignments = new ArrayList<AcInteractionVariableAssignment>(interaction.getVariableAssignments());
	    		Collections.sort(variableAssignments, new DomainObjectKeyComparator<Long>());
	    		for (AcInteractionVariableAssignment assignment : variableAssignments)
	    			if (variableAssignmentMap.containsKey(assignment)) {
	    				final ValueBound<Double> bound = variableAssignmentMap.get(assignment);
	    				if (!bounds.contains(bound)) bounds.add(bound);
	    			}

	    		// collect species assignment bounds
	    		List<AcSpeciesInteraction> speciesAssignments = new ArrayList<AcSpeciesInteraction>(interaction.getSpeciesActions());
	    		Collections.sort(speciesAssignments, new DomainObjectKeyComparator<Long>());
	    		for (AcSpeciesInteraction assignment : speciesAssignments)
	    			if (speciesAssignmentMap.containsKey(assignment)) {
	    				final ValueBound<Double> bound = speciesAssignmentMap.get(assignment);
	    				if (!bounds.contains(bound)) bounds.add(bound);
	    			}
	    	}

		EvoTaskBO<?, ?, ?> evoTaskBO = new EvoTaskBO<ArrayChromosome<Double>, Double[], AcInteractionSeries> (
			new EvoArrayChromManipulatorBO<Double>(
				(ReflectionProvider<ArrayChromosome<Double>>) chromosomeRF,
				gaSetting.getBitMutationType(),
				gaSetting.getPertrubMutationStrength(),
				Double.class,
				bounds.toArray(new ValueBound[0])),

			new EvoAcInteractionSeriesFitnessEvaluatorBO(
				evoTask,
				chemistryRunnableFactory,
				chemInterpretationFactory,
				acEvaluationBOFactory),

			new EvoEnumeratedTestSampleGeneratorBO<AcInteractionSeries>(
				evoTask.getActionSeries(),
				evoTask.getAsRepetitions()));

		return evoTaskBO;
	}

	private List<AcInteractionSeries> collectInteractionSeries(Collection<AcInteractionSeries> interactionSeries) {
		List<AcInteractionSeries> allInteractionSeries = new ArrayList<AcInteractionSeries>(interactionSeries);
		for (AcInteractionSeries singleInteractionSeries : interactionSeries)
			allInteractionSeries.addAll(
					collectInteractionSeries(singleInteractionSeries.getSubActionSeries()));
		return allInteractionSeries;
	}
}