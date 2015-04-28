package edu.banda.coel.task;

import com.banda.core.domain.task.Task;

/**
 * @author Peter Banda
 * @since 2012
 */
public abstract class RcRunTask extends Task {

    public enum RcRunStoreOption {
        StoreStatesAllTime, StoreStatesJustLastStep;
    }

    private RcRunStoreOption storeOption;
    private Integer repetitions;

    public RcRunTask() {
        super();
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public RcRunStoreOption getStoreOption() {
        return storeOption;
    }

    public void setStoreOption(RcRunStoreOption storeOption) {
        this.storeOption = storeOption;
    }
}