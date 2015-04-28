package edu.banda.coel.domain.rc;

import com.banda.core.domain.TechnicalDomainObject;
import com.banda.math.domain.rand.RandomDistribution;

/**
 * @author © Peter Banda
 * @since 2013
 */
public abstract class RcMachineTemplate extends TechnicalDomainObject {

    public enum RcMachineType {
        NeuralNetwork, ArtificialChemistry
    }

    private Integer reservoirNodesNum;
    private Integer inputNodesNum;
    private Integer readoutNodesNum;
    private boolean useFullInputToReservoirConnections;
    private boolean useFullReservoirToReadoutConnections;
    private boolean useFullReadoutToReservoirConnections;
    private RandomDistribution<Double> initReservoirWeightDistribution;

    public Integer getReservoirNodesNum() {
        return reservoirNodesNum;
    }

    public void setReservoirNodesNum(Integer reservoirNodesNum) {
        this.reservoirNodesNum = reservoirNodesNum;
    }

    public Integer getInputNodesNum() {
        return inputNodesNum;
    }

    public void setInputNodesNum(Integer inputNodesNum) {
        this.inputNodesNum = inputNodesNum;
    }

    public Integer getReadoutNodesNum() {
        return readoutNodesNum;
    }

    public void setReadoutNodesNum(Integer readoutNodesNum) {
        this.readoutNodesNum = readoutNodesNum;
    }

    public Integer getTotalNodesNum() {
        return inputNodesNum + reservoirNodesNum + readoutNodesNum;
    }

    public abstract Integer getReservoirFixedInDegree();

    public abstract void setReservoirFixedInDegree(Integer reservoirFixedInDegree);

    // input to reservoir connections are not counted to the reservoir in-degree
    public abstract Integer getInputToReservoirFixedOutDegree();

    public abstract void setInputToReservoirFixedOutDegree(Integer inputToReservoirFixedOutDegree);

    public abstract Integer getReservoirToReadoutFixedOutDegree();

    public abstract void setReservoirToReadoutFixedOutDegree(Integer reservoirToReadoutFixedOutDegree);

    public abstract Integer getReadoutToReservoirFixedOutDegree();

    public abstract void setReadoutToReservoirFixedOutDegree(Integer readoutToReservoirFixedOutDegree);

    public boolean isUseFullInputToReservoirConnections() {
        return useFullInputToReservoirConnections;
    }

    public void setUseFullInputToReservoirConnections(boolean useFullInputToReservoirConnections) {
        this.useFullInputToReservoirConnections = useFullInputToReservoirConnections;
    }

    public boolean isUseFullReservoirToReadoutConnections() {
        return useFullReservoirToReadoutConnections;
    }

    public void setUseFullReservoirToReadoutConnections(boolean useFullReservoirToReadoutConnections) {
        this.useFullReservoirToReadoutConnections = useFullReservoirToReadoutConnections;
    }

    public boolean isUseFullReadoutToReservoirConnections() {
        return useFullReadoutToReservoirConnections;
    }

    public void setUseFullReadoutToReservoirConnections(boolean useFullReadoutToReservoirConnections) {
        this.useFullReadoutToReservoirConnections = useFullReadoutToReservoirConnections;
    }

    public RandomDistribution<Double> getInitReservoirWeightDistribution() {
        return initReservoirWeightDistribution;
    }

    public void setInitReservoirWeightDistribution(RandomDistribution<Double> initReservoirWeightDistribution) {
        this.initReservoirWeightDistribution = initReservoirWeightDistribution;
    }

    public abstract RcMachineType getType();
}