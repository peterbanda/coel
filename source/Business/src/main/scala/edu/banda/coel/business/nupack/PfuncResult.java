package edu.banda.coel.business.nupack;

/**
 * @author © Peter Banda
 * @since 2012
 */
public class PfuncResult {

	private Double freeEnergy;
	private Double partitionFunction;

	public PfuncResult(Double freeEnergy, Double partitionFunction) {
		this.freeEnergy = freeEnergy;
		this.partitionFunction = partitionFunction;
	}

	public Double getFreeEnergy() {
		return freeEnergy;
	}

	public Double getPartitionFunction() {
		return partitionFunction;
	}
}

