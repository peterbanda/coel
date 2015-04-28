package edu.banda.coel.business.nupack;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author © Peter Banda
 * @since 2012
 */
public class PpairsResult {

	private Integer basesNumber;
	private Integer distinctBasesNumber;
	private Collection<BasePairProbabilityEntry> pairProbabilityEntries = new ArrayList<BasePairProbabilityEntry>();
	private Collection<DistinctBasePairNumberEntry> distinctPairNumberEntries = new ArrayList<DistinctBasePairNumberEntry>();
	
	public Integer getBasesNumber() {
		return basesNumber;
	}

	public void setBasesNumber(Integer basesNumber) {
		this.basesNumber = basesNumber;
	}

	public Integer getDistinctBasesNumber() {
		return distinctBasesNumber;
	}

	public void setDistinctBasesNumber(Integer distinctBasesNumber) {
		this.distinctBasesNumber = distinctBasesNumber;
	}

	public boolean hasBasesNumber() {
		return basesNumber != null;
	}

	public boolean hasDistinctBasesNumber() {
		return distinctBasesNumber != null;
	}
	
	public Collection<BasePairProbabilityEntry> getPairProbabilityEntries() {
		return pairProbabilityEntries;
	}

	public void addPairProbabilityEntry(BasePairProbabilityEntry entry) {
		pairProbabilityEntries.add(entry);
	}

	public Collection<DistinctBasePairNumberEntry> getDistinctPairNumberEntries() {
		return distinctPairNumberEntries;
	}

	public void addDistinctBasePairNumberEntry(DistinctBasePairNumberEntry entry) {
		distinctPairNumberEntries.add(entry);
	}	
}