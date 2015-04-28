package edu.banda.coel.server.grid.callable;

import java.io.Serializable;

import com.banda.math.domain.evo.Chromosome;

/**
 * @author © Peter Banda
 * @since 2012
 */
public class GridFitnessEvaluationEntry<H extends Chromosome<?>, T> implements Serializable {

	private final Integer order;
	private final H chromosome;
	private final T testSample;

	public GridFitnessEvaluationEntry(Integer order, H chromosome, T testSample) {
		this.order = order;
		this.chromosome = chromosome;
		this.testSample = testSample;
	}

	public Integer getOrder() {
		return order;
	}

	public H getChromosome() {
		return chromosome;
	}

	public T getTestSample() {
		return testSample;
	}
}
