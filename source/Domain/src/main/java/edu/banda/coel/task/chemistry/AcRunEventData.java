package edu.banda.coel.task.chemistry;

import java.io.Serializable;
import java.util.Collection;

import com.banda.chemistry.domain.AcEvaluatedAction;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcRunEventData implements Serializable {

	private Collection<Collection<Double>> speciesRunHistorySequences;
	private Collection<AcEvaluatedAction> evaluatedActions;

	public AcRunEventData(Collection<Collection<Double>> speciesRunHistorySequences, Collection<AcEvaluatedAction> evaluatedActions) {
		this.speciesRunHistorySequences = speciesRunHistorySequences;
		this.evaluatedActions = evaluatedActions;
	}

	public Collection<Collection<Double>> getSpeciesRunHistorySequences() {
		return speciesRunHistorySequences;
	}

	public Collection<AcEvaluatedAction> getEvaluatedActions() {
		return evaluatedActions;
	}
}