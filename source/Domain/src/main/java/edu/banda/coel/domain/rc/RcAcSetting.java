package edu.banda.coel.domain.rc;

import com.banda.chemistry.domain.AcConcentrationLevel;
import com.banda.function.domain.AggregateFunction;

/**
 * @author © Peter Banda
 * @since 2013
 */
public class RcAcSetting extends RcSetting<RcAcMachineTemplate> {

    private AcConcentrationLevel inputConcentrationLevel;
    private AcConcentrationLevel outputConcentrationLevel;
    private AcConcentrationLevel feedbackConcentrationLevel;
    private Integer afterInitStartingTimeStep;
    private Integer actionInterval;
    private AggregateFunction inputSpeciesStateTransFunction;
    private AggregateFunction outputSpeciesStateTransFunction;
    private AggregateFunction feedbackSpeciesStateTransFunction;
    private AggregateFunction internalSpeciesStateTransFunction;
    private boolean reservoirTranslationMode;

    public AcConcentrationLevel getInputConcentrationLevel() {
        return inputConcentrationLevel;
    }

    public void setInputConcentrationLevel(AcConcentrationLevel inputConcentrationLevel) {
        this.inputConcentrationLevel = inputConcentrationLevel;
    }

    public AcConcentrationLevel getFeedbackConcentrationLevel() {
        return feedbackConcentrationLevel;
    }

    public void setFeedbackConcentrationLevel(AcConcentrationLevel feedbackConcentrationLevel) {
        this.feedbackConcentrationLevel = feedbackConcentrationLevel;
    }

    public AcConcentrationLevel getOutputConcentrationLevel() {
        return outputConcentrationLevel;
    }

    public void setOutputConcentrationLevel(AcConcentrationLevel outputConcentrationLevel) {
        this.outputConcentrationLevel = outputConcentrationLevel;
    }

    public Integer getAfterInitStartingTimeStep() {
        return afterInitStartingTimeStep;
    }

    public void setAfterInitStartingTimeStep(Integer afterInitStartingTimeStep) {
        this.afterInitStartingTimeStep = afterInitStartingTimeStep;
    }

    public Integer getActionInterval() {
        return actionInterval;
    }

    public void setActionInterval(Integer actionInterval) {
        this.actionInterval = actionInterval;
    }

    public boolean isFeedbackMode() {
        return feedbackConcentrationLevel != null;
    }

    public AggregateFunction getInternalSpeciesStateTransFunction() {
        return internalSpeciesStateTransFunction;
    }

    public void setInternalSpeciesStateTransFunction(AggregateFunction internalSpeciesStateTransFunction) {
        this.internalSpeciesStateTransFunction = internalSpeciesStateTransFunction;
    }

    public AggregateFunction getInputSpeciesStateTransFunction() {
        return inputSpeciesStateTransFunction;
    }

    public void setInputSpeciesStateTransFunction(
            AggregateFunction inputSpeciesStateTransFunction) {
        this.inputSpeciesStateTransFunction = inputSpeciesStateTransFunction;
    }

    public AggregateFunction getOutputSpeciesStateTransFunction() {
        return outputSpeciesStateTransFunction;
    }

    public void setOutputSpeciesStateTransFunction(
            AggregateFunction outputSpeciesStateTransFunction) {
        this.outputSpeciesStateTransFunction = outputSpeciesStateTransFunction;
    }

    public AggregateFunction getFeedbackSpeciesStateTransFunction() {
        return feedbackSpeciesStateTransFunction;
    }

    public void setFeedbackSpeciesStateTransFunction(
            AggregateFunction feedbackSpeciesStateTransFunction) {
        this.feedbackSpeciesStateTransFunction = feedbackSpeciesStateTransFunction;
    }

    public boolean isReservoirTranslationMode() {
        return reservoirTranslationMode;
    }

    public void setReservoirTranslationMode(boolean reservoirTranslationMode) {
        this.reservoirTranslationMode = reservoirTranslationMode;
    }

    public Integer getTotalRunSteps() {
        return getInputsNumPerInputStream() * actionInterval + afterInitStartingTimeStep;
    }
}