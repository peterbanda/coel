package edu.banda.coel.task.chemistry;

import com.banda.chemistry.BndChemistryException;
import com.banda.chemistry.domain.*;
import com.banda.core.domain.task.Task;
import edu.banda.coel.task.chemistry.AcTaskParts.AcCompartmentHolder;
import edu.banda.coel.task.chemistry.AcTaskParts.AcInteractionSeriesHolder;
import edu.banda.coel.task.chemistry.AcTaskParts.AcSimulationConfigHolder;

import java.util.Collection;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcRunTask extends Task implements AcCompartmentHolder, AcSimulationConfigHolder, AcInteractionSeriesHolder {

    private AcCompartment compartment;
    private AcSimulationConfig simulationConfig;
    private AcInteractionSeries interactionSeries;

    private Integer runTime;
    private Integer repetitions = 1;
    private IllegalStateEventHandling upperThresholdViolationHandling;
    private IllegalStateEventHandling zeroThresholdViolationHandling;
    private IllegalStateEventHandling notANumberConcentrationHandling;
    private Collection<AcSpecies> storeSpeciesHistorySelectionGroup;

    public AcRunTask() {
        super();
    }

    @Override
    public AcCompartment getCompartment() {
        return compartment;
    }

    @Override
    public void setCompartment(AcCompartment compartment) {
        this.compartment = compartment;
    }

    @Override
    public boolean isCompartmentDefined() {
        return compartment != null;
    }

    @Override
    public boolean isCompartmentComplete() {
        return isCompartmentDefined() && compartment.getReactionSet() != null;
    }

    public void setCompartmentId(Long compartmentId) {
        compartment = new AcCompartment();
        compartment.setId(compartmentId);
    }

    public Long getCompartmentId() {
        if (isCompartmentDefined()) {
            return compartment.getId();
        }
        return null;
    }

    @Override
    public AcSimulationConfig getSimulationConfig() {
        return simulationConfig;
    }

    @Override
    public void setSimulationConfig(AcSimulationConfig simulationConfig) {
        this.simulationConfig = simulationConfig;
    }

    public void setSimulationConfigId(Long simulationConfigId) {
        simulationConfig = new AcSimulationConfig();
        simulationConfig.setId(simulationConfigId);
    }

    @Override
    public boolean isSimulationConfigDefined() {
        return simulationConfig != null;
    }

    @Override
    public boolean isSimulationConfigComplete() {
        return simulationConfig.getOdeSolverType() != null;
    }

    @Override
    public AcInteractionSeries getInteractionSeries() {
        return interactionSeries;
    }

    @Override
    public void setInteractionSeries(AcInteractionSeries interactionSeries) {
        this.interactionSeries = interactionSeries;
    }

    @Override
    public boolean isInteractionSeriesDefined() {
        return interactionSeries != null;
    }

    @Override
    public boolean isInteractionSeriesComplete() {
        return isInteractionSeriesDefined() && !interactionSeries.getActions().isEmpty();
    }

    public void setInteractionSeriesId(Long interactionSeriesId) {
        if (interactionSeries != null) {
            throw new BndChemistryException("AC interaction series already set for AC run task.");
        }
        interactionSeries = new AcInteractionSeries();
        interactionSeries.setId(interactionSeriesId);
    }

    public Long getInteractionSeriesId() {
        if (isInteractionSeriesDefined()) {
            return interactionSeries.getId();
        }
        return null;
    }

    public Integer getRunTime() {
        return runTime;
    }

    public void setRunTime(Integer runTime) {
        this.runTime = runTime;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public boolean hasUpperThresholdViolationHandling() {
        return upperThresholdViolationHandling != null;
    }

    public IllegalStateEventHandling getUpperThresholdViolationHandling() {
        return upperThresholdViolationHandling;
    }

    public void setUpperThresholdViolationHandling(
            IllegalStateEventHandling upperThresholdViolationHandling) {
        this.upperThresholdViolationHandling = upperThresholdViolationHandling;
    }

    public IllegalStateEventHandling getZeroThresholdViolationHandling() {
        return zeroThresholdViolationHandling;
    }

    public void setZeroThresholdViolationHandling(
            IllegalStateEventHandling zeroThresholdViolationHandling) {
        this.zeroThresholdViolationHandling = zeroThresholdViolationHandling;
    }

    public IllegalStateEventHandling getNotANumberConcentrationHandling() {
        return notANumberConcentrationHandling;
    }

    public void setNotANumberConcentrationHandling(
            IllegalStateEventHandling notANumberConcentrationHandling) {
        this.notANumberConcentrationHandling = notANumberConcentrationHandling;
    }

    public Collection<AcSpecies> getStoreSpeciesHistorySelectionGroup() {
        return storeSpeciesHistorySelectionGroup;
    }

    public void setStoreSpeciesHistorySelectionGroup(Collection<AcSpecies> storeSpeciesHistorySelectionGroup) {
        this.storeSpeciesHistorySelectionGroup = storeSpeciesHistorySelectionGroup;
    }

    public void copyFrom(AcRunTask runTask) {
        setRunOnGrid(runTask.isRunOnGrid());
        setCompartment(runTask.getCompartment());
        setSimulationConfig(runTask.getSimulationConfig());
        setInteractionSeries(runTask.getInteractionSeries());
        setRunTime(runTask.getRunTime());
        setRepetitions(runTask.getRepetitions());
        setUpperThresholdViolationHandling(runTask.getUpperThresholdViolationHandling());
        setZeroThresholdViolationHandling(runTask.getZeroThresholdViolationHandling());
        setNotANumberConcentrationHandling(runTask.getNotANumberConcentrationHandling());
        setStoreSpeciesHistorySelectionGroup(runTask.getStoreSpeciesHistorySelectionGroup());
    }
}