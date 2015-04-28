package edu.banda.coel.server.grid.callable;

import com.banda.chemistry.business.AcEvaluator;
import com.banda.chemistry.business.AcEvaluationBOFactory;
import com.banda.chemistry.domain.AcEvaluatedRun;
import com.banda.core.domain.um.User;

import com.banda.serverbase.grid.ArgumentCallable;
import edu.banda.coel.task.chemistry.AcTranslatedRunEvaluationTask;

/**
 * @author © Peter Banda
 * @since 2012
 */
public class GridAcEvaluateCall implements ArgumentCallable<AcTranslatedRunEvaluationTask, AcEvaluatedRun> {

	private final AcEvaluationBOFactory acEvaluationBOFactory;

	public GridAcEvaluateCall(AcEvaluationBOFactory acEvaluationBOFactory) {
		this.acEvaluationBOFactory = acEvaluationBOFactory;
	}

	@Override
	public AcEvaluatedRun call(AcTranslatedRunEvaluationTask task) {
		// Create BO
		AcEvaluator acEvaluationBO = acEvaluationBOFactory.createInstance(
			task.getAcEvaluation(),
			task.getTranslatedRun());

		// Execute task
		if (task.isEvaluateFullFlag()) {
			acEvaluationBO.evaluateFull();
		} else {
			acEvaluationBO.evaluateMultiple(task.getEvaluationSteps());
		}
		// Collect results
		AcEvaluatedRun evaluatedRun = acEvaluationBO.getEvaluatedRun();
		User user = new User();
		user.setId(new Long(1));
		evaluatedRun.setCreatedBy(user);
		return evaluatedRun;
	}
}