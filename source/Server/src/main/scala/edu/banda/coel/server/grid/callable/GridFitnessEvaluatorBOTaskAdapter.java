package edu.banda.coel.server.grid.callable;

import com.banda.core.Pair;
import com.banda.math.business.evo.EvoFitnessEvaluator;
import com.banda.math.domain.evo.Chromosome;
import com.banda.serverbase.grid.ArgumentCallable;

/**
 * @author © Peter Banda
 * @since 2012
 */
public class GridFitnessEvaluatorBOTaskAdapter<H extends Chromosome<?>, T> implements ArgumentCallable<GridFitnessEvaluationEntry<H, T>, Pair<Integer, Double>> {

	private final EvoFitnessEvaluator<H, T> fitnessEvaluator;

	public GridFitnessEvaluatorBOTaskAdapter(EvoFitnessEvaluator<H, T> fitnessEvaluator) {
		this.fitnessEvaluator = fitnessEvaluator;
	}

	@Override
	public Pair<Integer, Double> call(GridFitnessEvaluationEntry<H, T> gridFitnessEntry) {
		Double score = fitnessEvaluator.evaluateScore(gridFitnessEntry.getChromosome(), gridFitnessEntry.getTestSample());
		return new Pair<Integer, Double>(gridFitnessEntry.getOrder(), score);
	}
}