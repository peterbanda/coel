package edu.banda.coel.task.chemistry;

import java.util.ArrayList;
import java.util.Collection;

import com.banda.chemistry.domain.AcCompartment;
import com.banda.chemistry.domain.AcInteractionSeries;
import com.banda.chemistry.domain.AcEvaluation;
import com.banda.chemistry.domain.AcSimulationConfig;
import com.banda.core.domain.task.Task;
import com.banda.core.util.ObjectUtil;

import edu.banda.coel.task.chemistry.AcTaskParts.AcCompartmentHolder;
import edu.banda.coel.task.chemistry.AcTaskParts.AcMultipleEvaluationHolder;
import edu.banda.coel.task.chemistry.AcTaskParts.AcMultipleActionSeriesHolder;
import edu.banda.coel.task.chemistry.AcTaskParts.AcSimulationConfigHolder;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcPerformanceEvaluateTask extends Task implements AcCompartmentHolder, AcSimulationConfigHolder, AcMultipleActionSeriesHolder, AcMultipleEvaluationHolder {

	private AcCompartment compartment;
	private AcSimulationConfig simulationConfig;
	private Collection<AcInteractionSeries> actionSeries;
	private Collection<AcEvaluation> acEvaluations;
	private Integer runTime;
	private Integer repetitions;
	private boolean async;

	public AcPerformanceEvaluateTask() {
		super();
	}

	@Override
	public AcCompartment getCompartment() {
		return compartment;
	}

	@Override
	public void setCompartment(AcCompartment compartment) {
		this.compartment = compartment;
	}

	@Override
	public boolean isCompartmentDefined() {
		return compartment != null;
	}

	@Override
	public boolean isCompartmentComplete() {
		return isCompartmentDefined() && compartment.getReactionSet() != null;
	}

	public void setCompartmentId(Long compartmentId) {
		compartment = new AcCompartment();
		compartment.setId(compartmentId);
	}

	public Long getCompartmentId() {
		if (isCompartmentDefined()) {
			return compartment.getId();
		}
		return null;
	}
	
	@Override
	public AcSimulationConfig getSimulationConfig() {
		return simulationConfig;
	}

	@Override
	public void setSimulationConfig(AcSimulationConfig simulationConfig) {
		this.simulationConfig = simulationConfig;
	}

	public void setSimulationConfigId(Long simulationConfigId) {
		simulationConfig = new AcSimulationConfig();
		simulationConfig.setId(simulationConfigId);
	}

	@Override
	public boolean isSimulationConfigDefined() {
		return simulationConfig != null;
	}

	@Override
	public boolean isSimulationConfigComplete() {
		return simulationConfig.getOdeSolverType() != null;
	}

	@Override
	public Collection<AcEvaluation> getAcEvaluations() {
		return acEvaluations;
	}

	@Override
	public void setAcEvaluations(Collection<AcEvaluation> acEvaluations) {
		this.acEvaluations = acEvaluations;
	}

	@Override
	public boolean areAcEvaluationsDefined() {
		return acEvaluations != null;
	}

	@Override
	public boolean areAcEvaluationsComplete() {
		return ObjectUtil.getFirst(acEvaluations).getName() != null;
	}

	public void setAcEvaluationIds(Collection<Long> evaluationIds) {
		if (evaluationIds == null) {
			return;
		}
		acEvaluations = new ArrayList<AcEvaluation>();
		for (Long actionSeriesId : evaluationIds) {
			final AcEvaluation evaluation = new AcEvaluation();
			evaluation.setId(actionSeriesId);
			acEvaluations.add(evaluation);
		}
	}

	public Collection<Long> getAcEvaluationIds() {
		if (!areAcEvaluationsDefined()) {
			return null;
		}
		Collection<Long> acEvaluationIds = new ArrayList<Long>();
		for (AcEvaluation oneAcEvaluation : acEvaluations) {
			acEvaluationIds.add(oneAcEvaluation.getId());
		}
		return acEvaluationIds;
	}

	@Override
	public Collection<AcInteractionSeries> getActionSeries() {
		return actionSeries;
	}

	@Override
	public void setActionSeries(Collection<AcInteractionSeries> actionSeries) {
		this.actionSeries = actionSeries;
	}

	@Override
	public boolean areActionSeriesDefined() {
		return actionSeries != null;
	}

	@Override
	public boolean areActionSeriesComplete() {
		return ObjectUtil.getFirst(actionSeries).getName() != null;
	}

	public void setActionSeriesIds(Collection<Long> actionSeriesIds) {
		if (actionSeriesIds == null) {
			return;
		}
//		if (actionSeries != null) {
//			throw new BndChemistryException("AC interaction series already set for AC action task.");
//		}
		actionSeries = new ArrayList<AcInteractionSeries>();
		for (Long actionSeriesId : actionSeriesIds) {
			actionSeries.add(new AcInteractionSeries(actionSeriesId));
		}
	}

	public Collection<Long> getActionSeriesIds() {
		if (!areActionSeriesDefined()) {
			return null;
		}
		Collection<Long> actionSeriesIds = new ArrayList<Long>();
		for (AcInteractionSeries oneActionSeries : actionSeries) {
			actionSeriesIds.add(oneActionSeries.getId());
		}
		return actionSeriesIds;
	}



	public Integer getRunTime() {
		return runTime;
	}

	public void setRunTime(Integer runTime) {
		this.runTime = runTime;
	}

	public Integer getRepetitions() {
		return repetitions;
	}

	public void setRepetitions(Integer repetitions) {
		this.repetitions = repetitions;
	}

	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}
}