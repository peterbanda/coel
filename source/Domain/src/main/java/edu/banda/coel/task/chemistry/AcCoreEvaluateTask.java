package edu.banda.coel.task.chemistry;

import com.banda.chemistry.BndChemistryException;
import com.banda.chemistry.domain.AcEvaluation;
import com.banda.core.domain.task.Task;

import edu.banda.coel.task.chemistry.AcTaskParts.AcEvaluationHolder;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcCoreEvaluateTask extends Task implements AcEvaluationHolder {

	private AcEvaluation acEvaluation;
	private Integer evaluationSteps;
	private boolean evaluateFullFlag;

	public AcCoreEvaluateTask() {
		super();
	}

	@Override
	public AcEvaluation getAcEvaluation() {
		return acEvaluation;
	}

	@Override
	public void setAcEvaluation(AcEvaluation acEvaluation) {
		this.acEvaluation = acEvaluation;
	}

	@Override
	public boolean isAcEvaluationDefined() {
		return acEvaluation != null;
	}

	@Override
	public boolean isAcEvaluationComplete() {
		return acEvaluation.getName() != null;
	}
	
	public void setAcEvaluationId(Long acEvaluationId) {
		if (acEvaluationId == null) {
			return;
		}
		if (acEvaluation != null) {
			throw new BndChemistryException("AC evaluation already set for AC evaluation task.");
		}
		acEvaluation = new AcEvaluation();
		acEvaluation.setId(acEvaluationId);
	}

	public Long getAcEvaluationId() {
		return isAcEvaluationDefined() ? acEvaluation.getId() : null;
	}

	public Integer getEvaluationSteps() {
		return evaluationSteps;
	}

	public void setEvaluationSteps(Integer evaluationSteps) {
		this.evaluationSteps = evaluationSteps;
	}

	public boolean isEvaluateFullFlag() {
		return evaluateFullFlag;
	}

	public void setEvaluateFullFlag(boolean evaluateFullFlag) {
		this.evaluateFullFlag = evaluateFullFlag;
	}

	public void copyFrom(AcCoreEvaluateTask coreEvalTaskDef) {
		acEvaluation = coreEvalTaskDef.acEvaluation;
		evaluationSteps = coreEvalTaskDef.evaluationSteps;
		evaluateFullFlag = coreEvalTaskDef.evaluateFullFlag;
	}
}