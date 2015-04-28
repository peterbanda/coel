package edu.banda.coel.task.chemistry;

import com.banda.chemistry.domain.AcTranslatedRun;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcTranslatedRunEvaluationTask extends AcCoreEvaluateTask {

    private AcTranslatedRun translatedRun;

    public AcTranslatedRun getTranslatedRun() {
        return translatedRun;
    }

    public void setTranslatedRun(AcTranslatedRun translatedRun) {
        this.translatedRun = translatedRun;
    }

    public static Collection<AcTranslatedRunEvaluationTask> createInstances(AcEvaluateTask containerTaskDef) {
        Collection<AcTranslatedRunEvaluationTask> instances = new ArrayList<AcTranslatedRunEvaluationTask>();
        for (AcTranslatedRun translatedRun : containerTaskDef.getTranslatedRuns()) {
            AcTranslatedRunEvaluationTask instance = new AcTranslatedRunEvaluationTask();
            instance.setTranslatedRun(translatedRun);
            instance.setAcEvaluation(containerTaskDef.getAcEvaluation());
//			instance.setSaveEvaluationFlag(containerTaskDef.isSaveEvaluationFlag());
            instance.setEvaluationSteps(containerTaskDef.getEvaluationSteps());
            instance.setEvaluateFullFlag(containerTaskDef.isEvaluateFullFlag());
            instances.add(instance);
        }
        return instances;
    }
}