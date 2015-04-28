package edu.banda.coel.task.chemistry;

import java.util.Collection;

import com.banda.chemistry.domain.*;
import com.banda.math.domain.dynamics.MultiRunAnalysisSpec;

/**
 * @author Peter Banda
 * @since 2011
 */
public interface AcTaskParts {

	public interface AcCompartmentHolder {
		public AcCompartment getCompartment();
		public void setCompartment(AcCompartment compartment);
		public boolean isCompartmentDefined();
		public boolean isCompartmentComplete();
	}

	public interface AcMultipleCompartmentHolder {
		public Collection<AcCompartment> getCompartments();
		public void setCompartments(Collection<AcCompartment> compartments);
		public boolean areCompartmentsDefined();
		public boolean areCompartmentsComplete();
	}

	public interface AcSimulationConfigHolder {
		public AcSimulationConfig getSimulationConfig();
		public void setSimulationConfig(AcSimulationConfig simulationConfig);
		public boolean isSimulationConfigDefined();
		public boolean isSimulationConfigComplete();
	}

	public interface AcMultipleActionSeriesHolder {
		public Collection<AcInteractionSeries> getActionSeries();
		public void setActionSeries(Collection<AcInteractionSeries> actionSeries);
		public Collection<Long> getActionSeriesIds();
		public boolean areActionSeriesDefined();
		public boolean areActionSeriesComplete();
	}

	public interface AcInteractionSeriesHolder {
		public AcInteractionSeries getActionSeries();
		public void setActionSeries(AcInteractionSeries actionSeries);
		public boolean isActionSeriesDefined();
		public boolean isActionSeriesComplete();
	}

	public interface AcEvaluatedActionSeriesHolder {
		public AcEvaluatedActionSeries getEvaluatedActionSeries();
		public void setEvaluatedActionSeries(AcEvaluatedActionSeries evaluatedActionSeries);
		public boolean isEvaluatedActionSeriesDefined();
		public boolean isEvaluatedActionSeriesComplete();
	}

	public interface AcMultipleTranslationSeriesHolder {
		public Collection<AcTranslationSeries> getTranslationSeries();
		public void setTranslationSeries(Collection<AcTranslationSeries> translationSeries);
		public Collection<Long> getTranslationSeriesIds();
		public boolean areTranslationSeriesDefined();
		public boolean areTranslationSeriesComplete();
	}

	public interface AcTranslationSeriesHolder {
		public AcTranslationSeries getTranslationSeries();
		public void setTranslationSeries(AcTranslationSeries actionSeries);
		public boolean isTranslationSeriesDefined();
		public boolean isTranslationSeriesComplete();
	}

	public interface AcEvaluationHolder {
		public AcEvaluation getAcEvaluation();
		public void setAcEvaluation(AcEvaluation acEvaluation);
		public boolean isAcEvaluationDefined();
		public boolean isAcEvaluationComplete();
	}

	public interface AcMultipleEvaluationHolder {
		public Collection<AcEvaluation> getAcEvaluations();
		public void setAcEvaluations(Collection<AcEvaluation> acEvaluations);
		public Collection<Long> getAcEvaluationIds();
		public boolean areAcEvaluationsDefined();
		public boolean areAcEvaluationsComplete();
	}

	public interface AcTranslatedRunsHolder {
		public Collection<AcTranslatedRun> getTranslatedRuns();
		public void setTranslatedRuns(Collection<AcTranslatedRun> runs);
		public Collection<Long> getTranslatedRunIds();
		public boolean areTranslatedRunsDefined();
		public boolean areTranslatedRunsComplete();
	}

	public interface AcMultipleRateConstantTypeBoundHolder {
		public Collection<AcRateConstantTypeBound> getRateConstantTypeBounds();
		public void setRateConstantTypeBounds(Collection<AcRateConstantTypeBound> rateConstantTypeBounds);
		public Collection<Long> getRateConstantTypeBoundIds();
		public boolean areRateConstantTypeBoundDefined();
		public boolean areRateConstantTypeBoundComplete();
	}

	// Move to dyn part??
	
	public interface MultiRunAnalysisSpecHolder<T> {
		public MultiRunAnalysisSpec<T> getMultiRunAnalysisSpec();
		public void setMultiRunAnalysisSpec(MultiRunAnalysisSpec<T> multiRunAnalysisSpec);
		public boolean isMultiRunAnalysisSpecDefined();
		public boolean isMultiRunAnalysisSpecComplete();
	}
}