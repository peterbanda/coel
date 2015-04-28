package edu.banda.coel.domain.rc;

import com.banda.function.domain.BooleanFunction.BooleanFunctionType;
import edu.banda.coel.task.RcTaskParts.RcMachineTemplateHolder;

import java.io.Serializable;

/**
 * @author © Peter Banda
 * @since 2013
 */
public class RcSetting<T extends RcMachineTemplate> implements RcMachineTemplateHolder<T>, Serializable {

    private T rcMachineTemplate;
    private BooleanFunctionType taskType;
    private boolean useRandomTask;
    private Integer timeWindowLength;
    private Integer inputsNumPerInputStream;

    @Override
    public T getRcMachineTemplate() {
        return rcMachineTemplate;
    }

    public Long getRcMachineTemplateId() {
        return rcMachineTemplate.getId();
    }

    @Override
    public boolean isRcMachineTemplateComplete() {
        return isRcMachineTemplateDefined() && rcMachineTemplate.getReservoirNodesNum() != null;
    }

    @Override
    public boolean isRcMachineTemplateDefined() {
        return rcMachineTemplate != null;
    }

    @Override
    public void setRcMachineTemplate(T rcMachineTemplate) {
        this.rcMachineTemplate = rcMachineTemplate;
    }

    public Integer getInputsNumPerInputStream() {
        return inputsNumPerInputStream;
    }

    public void setInputsNumPerInputStream(Integer inputsNumPerInputStream) {
        this.inputsNumPerInputStream = inputsNumPerInputStream;
    }

    public BooleanFunctionType getTaskType() {
        return taskType;
    }

    public void setTaskType(BooleanFunctionType taskType) {
        this.taskType = taskType;
    }

    public Integer getTimeWindowLength() {
        return timeWindowLength;
    }

    public void setTimeWindowLength(Integer timeWindowLength) {
        this.timeWindowLength = timeWindowLength;
    }

    public boolean isUseRandomTask() {
        return useRandomTask;
    }

    public void setUseRandomTask(boolean useRandomTask) {
        this.useRandomTask = useRandomTask;
    }
}