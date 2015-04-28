package edu.banda.coel.domain.evo;

import com.banda.chemistry.domain.AcSimulationConfig;
import com.banda.chemistry.domain.ArtificialChemistrySpecBound;
import com.banda.math.domain.dynamics.MultiRunAnalysisSpec;
import com.banda.math.domain.dynamics.SingleRunAnalysisSpec;
import com.banda.math.domain.evo.EvoTask;

/**
 * @author © Peter Banda
 * @since 2013
 */
public class EvoAcSpecTask extends EvoTask {

	private ArtificialChemistrySpecBound acSpecBound;
	private AcSimulationConfig simConfig;
	private MultiRunAnalysisSpec<Double> multiRunAnalysisSpec;

	public EvoAcSpecTask() {
		super();
	}

	public AcSimulationConfig getSimConfig() {
		return simConfig;
	}

	public void setSimConfig(AcSimulationConfig simConfig) {
		this.simConfig = simConfig;
	}

	public MultiRunAnalysisSpec<Double> getMultiRunAnalysisSpec() {
		return multiRunAnalysisSpec;
	}

	public void setMultiRunAnalysisSpec(MultiRunAnalysisSpec<Double> multiRunAnalysisSpec) {
		this.multiRunAnalysisSpec = multiRunAnalysisSpec;
	}

	public SingleRunAnalysisSpec getSingleRunAnalysisSpec() {
		return multiRunAnalysisSpec.getSingleRunSpec();
	}

	public ArtificialChemistrySpecBound getAcSpecBound() {
		return acSpecBound;
	}

	public void setAcSpecBound(ArtificialChemistrySpecBound acSpecBound) {
		this.acSpecBound = acSpecBound;
	}
}