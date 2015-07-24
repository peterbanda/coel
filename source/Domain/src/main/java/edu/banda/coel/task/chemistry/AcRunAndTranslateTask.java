package edu.banda.coel.task.chemistry;

import com.banda.chemistry.BndChemistryException;
import com.banda.chemistry.domain.AcCompartment;
import com.banda.chemistry.domain.AcInteractionSeries;
import com.banda.chemistry.domain.AcTranslationSeries;
import com.banda.core.domain.task.Task;
import edu.banda.coel.task.chemistry.AcTaskParts.AcTranslationSeriesHolder;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcRunAndTranslateTask extends Task implements AcTranslationSeriesHolder {

    private AcTranslationSeries translationSeries;
    private AcRunTask runTaskDefinition;
    private boolean storeRunTrace;

    public AcRunAndTranslateTask() {
        super();
    }

    @Override
    public AcTranslationSeries getTranslationSeries() {
        return translationSeries;
    }

    @Override
    public void setTranslationSeries(AcTranslationSeries translationSeries) {
        this.translationSeries = translationSeries;
    }

    @Override
    public boolean isTranslationSeriesDefined() {
        return translationSeries != null;
    }

    @Override
    public boolean isTranslationSeriesComplete() {
        return isTranslationSeriesDefined() && !translationSeries.getTranslations().isEmpty();
    }

    public void setTranslationSeriesId(Long actionSeriesId) {
        if (translationSeries != null) {
            throw new BndChemistryException("AC translation series already set for AC translation task.");
        }
        translationSeries = new AcTranslationSeries();
        translationSeries.setId(actionSeriesId);
    }

    public Long getTranslationSeriesId() {
        if (isTranslationSeriesDefined()) {
            return translationSeries.getId();
        }
        return null;
    }

    public AcRunTask getRunTaskDefinition() {
        return runTaskDefinition;
    }

    public void setRunTaskDefinition(AcRunTask runTaskDefinition) {
        this.runTaskDefinition = runTaskDefinition;
    }

    public AcInteractionSeries getActionSeries() {
        return runTaskDefinition.getInteractionSeries();
    }

    public Long getActionSeriesId() {
        return runTaskDefinition.getInteractionSeriesId();
    }

    public boolean isStoreRunTrace() {
        return storeRunTrace;
    }

    public void setStoreRunTrace(boolean storeRunTrace) {
        this.storeRunTrace = storeRunTrace;
    }

    public AcCompartment getCompartment() {
        return runTaskDefinition != null ? runTaskDefinition.getCompartment() : null;
    }

    public Long getCompartmentId() {
        return getCompartment() != null ? getCompartment().getId() : null;
    }

    public void copyFrom(AcRunAndTranslateTask taskDef) {
        AcRunTask runTaskDef = new AcRunTask();
        setRunTaskDefinition(runTaskDef);
        runTaskDef.copyFrom(taskDef.getRunTaskDefinition());
        setTranslationSeries(taskDef.getTranslationSeries());
        setStoreRunTrace(taskDef.isStoreRunTrace());
    }
}