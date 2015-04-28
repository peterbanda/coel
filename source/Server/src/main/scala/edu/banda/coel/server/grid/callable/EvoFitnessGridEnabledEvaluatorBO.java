package edu.banda.coel.server.grid.callable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.banda.core.Pair;
import com.banda.math.business.evo.EvoFitnessEvaluator;
import com.banda.math.domain.evo.Chromosome;

import com.banda.serverbase.grid.ArgumentCallable;
import com.banda.serverbase.grid.ComputationalGrid;
import edu.banda.coel.CoelRuntimeException;

/**
 * @author © Peter Banda
 * @since 2012
 */
public class EvoFitnessGridEnabledEvaluatorBO<H extends Chromosome<?>, T> implements EvoFitnessEvaluator<H, T>{

	private final ComputationalGrid grid;
	private final EvoFitnessEvaluator<H, T> fitnessEvaluator;
	private final Integer maxJobsInParallelNum;
	private final Integer jobsInSequenceNumber;

	public EvoFitnessGridEnabledEvaluatorBO(
		ComputationalGrid grid,
		EvoFitnessEvaluator<H, T> fitnessEvaluator,
		Integer maxJobsInParallelNum,
		Integer jobsInSequenceNumber
	) {
		this.grid = grid;
		this.fitnessEvaluator = fitnessEvaluator;
		this.maxJobsInParallelNum = maxJobsInParallelNum;
		this.jobsInSequenceNumber = jobsInSequenceNumber != null ? jobsInSequenceNumber : 1;
	}

	@Override
	public Double evaluateScore(H chromosome, T testSample) {
		return fitnessEvaluator.evaluateScore(chromosome, testSample);
	}

	@Override
	public Double calcFitness(Double score) {
		return fitnessEvaluator.calcFitness(score);
	}

	@Override
	public void evaluateScoreAndFitness(Collection<H> chromosomes, Collection<T> testSamples) {
		// First null score
		for (final H chromosome : chromosomes) {
			chromosome.nullScore();
		}
		// Run fitness evaluation on the grid
		ArgumentCallable<GridFitnessEvaluationEntry<H, T>, Pair<Integer, Double>> evoEvaluateFitnessCall = new GridFitnessEvaluatorBOTaskAdapter<H, T>(fitnessEvaluator);

		final Collection<GridFitnessEvaluationEntry<H, T>> jobInputs = createGridJobInputs(chromosomes, testSamples);
		final Collection<Pair<Integer, Double>> chromosomeOrderScores = grid.runOnGridSync(evoEvaluateFitnessCall, jobInputs, jobsInSequenceNumber, maxJobsInParallelNum); 

		//	log.info("Fitness evaluation successfully run on the grid for " + chromosomes.size() + " chromosomes and " + testSamples.size() + " test samples.");
		if (chromosomeOrderScores.size() != chromosomes.size() * testSamples.size()) {
			throw new CoelRuntimeException("The number of chromosomes '" + chromosomes.size() + "' times the number of test samples '" + testSamples.size()
         					                + "' is not equal to the number of fitness scores '" + chromosomeOrderScores.size() + "' obtained from the grid.");
		}

		// Set score to chromosomes		
		Map<Integer, Collection<Double>> chromosomeOrderScoresMap = createChromosomeOrderScoresMap(chromosomeOrderScores);
		int order = 0;
		for (final H chromosome : chromosomes) {
			Collection<Double> scores = chromosomeOrderScoresMap.get(order);
			for (Double score : scores) {
				chromosome.addToScore(score);
			}
			order++;
		}

		// Calculate and set fitness
		for (final H chromosome : chromosomes) {
			chromosome.setFitness(calcFitness(chromosome.getScore()));
		}
	}

	private Collection<GridFitnessEvaluationEntry<H, T>> createGridJobInputs(Collection<H> chromosomes, Collection<T> testSamples) {
		Collection<GridFitnessEvaluationEntry<H, T>> jobInputs = new ArrayList<GridFitnessEvaluationEntry<H, T>>();
		int order = 0;
		for (H chromosome : chromosomes) {
			for (T testSample : testSamples)
				jobInputs.add(new GridFitnessEvaluationEntry<H, T>(order, chromosome, testSample));
			order++;
		}
		return jobInputs;
	}

	private static Map<Integer, Collection<Double>> createChromosomeOrderScoresMap(Collection<Pair<Integer, Double>> chromosomeOrderScores) {
		 Map<Integer, Collection<Double>> chromosomeOrderScoresMap = new HashMap<Integer, Collection<Double>>();
		 for (Pair<Integer, Double> orderScorePair : chromosomeOrderScores) {
			 Collection<Double> scores = chromosomeOrderScoresMap.get(orderScorePair.getFirst());
			 if (scores == null) {
				 scores = new ArrayList<Double>();
				 chromosomeOrderScoresMap.put(orderScorePair.getFirst(), scores);
			 }
			 scores.add(orderScorePair.getSecond());
		 }
		 return chromosomeOrderScoresMap;
	}
}