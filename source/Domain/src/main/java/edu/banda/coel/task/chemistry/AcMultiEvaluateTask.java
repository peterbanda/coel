package edu.banda.coel.task.chemistry;

import com.banda.chemistry.domain.AcEvaluation;
import com.banda.core.domain.task.Task;
import com.banda.core.util.ObjectUtil;
import edu.banda.coel.task.chemistry.AcTaskParts.AcMultipleEvaluationHolder;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcMultiEvaluateTask extends Task implements AcMultipleEvaluationHolder {

    private Collection<AcEvaluation> acEvaluations;
    private Integer evaluationSteps;
    private boolean evaluateFullFlag;

    public AcMultiEvaluateTask() {
        super();
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

    public void copyFrom(AcMultiEvaluateTask task) {
        acEvaluations = new ArrayList<AcEvaluation>(task.getAcEvaluations());
        evaluationSteps = task.evaluationSteps;
        evaluateFullFlag = task.evaluateFullFlag;
    }
}