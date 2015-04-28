package edu.banda.coel.task;

import edu.banda.coel.task.chemistry.AcRunAndTranslateTask;

/**
 * @author Peter Banda
 * @since 2012
 */
public class RcAcRunTask extends RcRunTask {

    private AcRunAndTranslateTask runAndTranslationTask;

    public AcRunAndTranslateTask getRunAndTranslationTaskDef() {
        return runAndTranslationTask;
    }

    public void setRunAndTranslationTaskDef(AcRunAndTranslateTask runAndTranslationTask) {
        this.runAndTranslationTask = runAndTranslationTask;
    }
}