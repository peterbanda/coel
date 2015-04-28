package edu.banda.coel.domain.evo;

import com.banda.math.domain.evo.EvoTask;
import com.banda.math.domain.evo.EvoTaskType;
import com.banda.network.domain.Network;
import com.banda.network.domain.NetworkActionSeries;
import com.banda.network.domain.NetworkEvaluation;
import com.banda.network.domain.NetworkSimulationConfig;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author © Peter Banda
 * @since 2013
 */
public class EvoNetworkTask<T> extends EvoTask {

    private Network<T> network;
    private NetworkSimulationConfig simConfig;
    private Collection<NetworkActionSeries<T>> actionSeries = new HashSet<NetworkActionSeries<T>>();
    private Integer asRepetitions;
    private NetworkEvaluation evaluation;
    private Integer runTime;

    public EvoNetworkTask() {
        super();
        setTaskType(EvoTaskType.Network);
    }

    public Network<T> getNetwork() {
        return network;
    }

    public void setNetwork(Network<T> network) {
        this.network = network;
    }

    public NetworkSimulationConfig getSimConfig() {
        return simConfig;
    }

    public void setSimConfig(NetworkSimulationConfig simConfig) {
        this.simConfig = simConfig;
    }

    public Collection<NetworkActionSeries<T>> getActionSeries() {
        return actionSeries;
    }

    public void setActionSeries(Collection<NetworkActionSeries<T>> actionSeries) {
        this.actionSeries = actionSeries;
    }

    public void addActionSeries(NetworkActionSeries<T> actionSeries) {
        this.actionSeries.add(actionSeries);
    }

    public Integer getAsRepetitions() {
        return asRepetitions;
    }

    public void setAsRepetitions(Integer asRepetitions) {
        this.asRepetitions = asRepetitions;
    }

    public Integer getRunTime() {
        return runTime;
    }

    public void setRunTime(Integer runTime) {
        this.runTime = runTime;
    }

    public NetworkEvaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(NetworkEvaluation evaluation) {
        this.evaluation = evaluation;
    }

    public void setFixedPointDetectionPeriodicity(Double fixedPointDetectionPeriodicity) {
        if (simConfig == null)
            simConfig = new NetworkSimulationConfig();
        simConfig.setFixedPointDetectionPeriodicity(fixedPointDetectionPeriodicity);
    }

    public Double getFixedPointDetectionPeriodicity() {
        return simConfig != null ? simConfig.getFixedPointDetectionPeriodicity() : null;
    }
}