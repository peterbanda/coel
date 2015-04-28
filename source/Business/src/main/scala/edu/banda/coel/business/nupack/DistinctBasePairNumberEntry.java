package edu.banda.coel.business.nupack;

/**
 * @author © Peter Banda
 * @since 2012
 */
public class DistinctBasePairNumberEntry {

	private int baseIndex1;
	private int baseIndex2;
	private double pairNumber;

	public DistinctBasePairNumberEntry(
		int baseIndex1,
		int baseIndex2,
		double pairNumber
	) {
		this.baseIndex1 = baseIndex1;
		this.baseIndex2 = baseIndex2;
		this.pairNumber = pairNumber;
	}

	public int getBaseIndex1() {
		return baseIndex1;
	}

	public int getBaseIndex2() {
		return baseIndex2;
	}

	public double getPairNumber() {
		return pairNumber;
	}
}