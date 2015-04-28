package edu.banda.coel.domain.evo;

import java.util.Collection;
import java.util.HashSet;

import com.banda.chemistry.domain.AcCompartment;
import com.banda.chemistry.domain.AcEvaluation;
import com.banda.chemistry.domain.AcInteractionSeries;
import com.banda.chemistry.domain.AcSimulationConfig;
import com.banda.chemistry.domain.ArtificialChemistry;
import com.banda.math.domain.evo.EvoTask;

/**
 * @author © Peter Banda
 * @since 2013
 */
public abstract class EvoAcTask extends EvoTask {

	private AcCompartment compartment;
	private AcSimulationConfig simulationConfig;
	@Deprecated
	private ArtificialChemistry ac;
	private AcEvaluation acEvaluation;
	private Collection<AcInteractionSeries> actionSeries = new HashSet<AcInteractionSeries>();
	private Integer asRepetitions;
	private Integer runSteps;
	private Integer lastEvaluationStepsToCount;

	public EvoAcTask() {
		super();
	}

	@Deprecated
	public ArtificialChemistry getAc() {
		return ac;
	}

	@Deprecated
	public void setAc(ArtificialChemistry ac) {
		this.ac = ac;
	}

	public AcCompartment getCompartment() {
		return compartment;
	}

	public void setCompartment(AcCompartment compartment) {
		this.compartment = compartment;
	}

	public AcSimulationConfig getSimulationConfig() {
		return simulationConfig;
	}

	public void setSimulationConfig(AcSimulationConfig simulationConfig) {
		this.simulationConfig = simulationConfig;
	}

	public AcEvaluation getAcEvaluation() {
		return acEvaluation;
	}

	public void setAcEvaluation(AcEvaluation acEvaluation) {
		this.acEvaluation = acEvaluation;
	}

	public Collection<AcInteractionSeries> getActionSeries() {
		return actionSeries;
	}

	public void setActionSeries(Collection<AcInteractionSeries> actionSeries) {
		this.actionSeries = actionSeries;
	}

	public void addActionSeries(AcInteractionSeries oneActionSeries) {
		actionSeries.add(oneActionSeries);
	}

	public void initActionSeries() {
		actionSeries = new HashSet<AcInteractionSeries>();
	}

	public Integer getAsRepetitions() {
		return asRepetitions;
	}

	public void setAsRepetitions(Integer asRepetitions) {
		this.asRepetitions = asRepetitions;
	}

	public Integer getRunSteps() {
		return runSteps;
	}

	public void setRunSteps(Integer runSteps) {
		this.runSteps = runSteps;
	}

	public Integer getLastEvaluationStepsToCount() {
		return lastEvaluationStepsToCount;
	}

	public void setLastEvaluationStepsToCount(Integer lastEvaluationStepsToCount) {
		this.lastEvaluationStepsToCount = lastEvaluationStepsToCount;
	}
}