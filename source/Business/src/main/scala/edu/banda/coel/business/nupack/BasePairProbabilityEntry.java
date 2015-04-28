package edu.banda.coel.business.nupack;

/**
 * @author © Peter Banda
 * @since 2012
 */
public class BasePairProbabilityEntry {

	private int baseIndex1;
	private int baseIndex2;
	private double pairProbability;
	private Double nestedPairProbability;
	private Double nonnestedPairProbability;

	public BasePairProbabilityEntry(
		int baseIndex1,
		int baseIndex2,
		double pairProbability
	) {
		this.baseIndex1 = baseIndex1;
		this.baseIndex2 = baseIndex2;
		this.pairProbability = pairProbability;
	}

	public BasePairProbabilityEntry(
		int baseIndex1,
		int baseIndex2,
		double pairProbability,
		Double nestedPairProbability,
		Double nonnestedPairProbability
	) {
		this(baseIndex1, baseIndex2, pairProbability);
		this.nestedPairProbability = nestedPairProbability;
		this.nonnestedPairProbability = nonnestedPairProbability;
	}

	public int getBaseIndex1() {
		return baseIndex1;
	}

	public int getBaseIndex2() {
		return baseIndex2;
	}

	public double getPairProbability() {
		return pairProbability;
	}

	public Double getNestedPairProbability() {
		return nestedPairProbability;
	}

	public Double getNonnestedPairProbability() {
		return nonnestedPairProbability;
	}

	public boolean hasNestedPairProbability() {
		return nestedPairProbability != null;
	}

	public boolean hasNonnestedPairProbability() {
		return nonnestedPairProbability != null;
	}
}