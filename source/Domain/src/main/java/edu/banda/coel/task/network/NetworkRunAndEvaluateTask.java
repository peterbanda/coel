package edu.banda.coel.task.network;

import com.banda.core.domain.task.Task;
import com.banda.core.util.ObjectUtil;
import com.banda.network.domain.Network;
import com.banda.network.domain.NetworkActionSeries;
import com.banda.network.domain.NetworkEvaluation;
import com.banda.network.domain.NetworkSimulationConfig;
import edu.banda.coel.task.network.NetworkTaskParts.NetworkMultipleEvaluationHolder;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Peter Banda
 * @since 2013
 */
public class NetworkRunAndEvaluateTask<T> extends Task implements NetworkMultipleEvaluationHolder {

    private NetworkRunTask<T> runTask;
    private Collection<NetworkEvaluation> evaluations;

    public NetworkRunAndEvaluateTask() {
        super();
    }

    @Override
    public Collection<NetworkEvaluation> getNetworkEvaluations() {
        return evaluations;
    }

    @Override
    public void setNetworkEvaluations(Collection<NetworkEvaluation> evaluations) {
        this.evaluations = evaluations;
    }

    @Override
    public boolean areNetworkEvaluationsDefined() {
        return evaluations != null;
    }

    @Override
    public boolean areNetworkEvaluationsComplete() {
        return ObjectUtil.getFirst(evaluations).getName() != null;
    }

    public void setNetworkEvaluationIds(Collection<Long> evaluationIds) {
        if (evaluationIds == null) {
            return;
        }
        evaluations = new ArrayList<NetworkEvaluation>();
        for (Long networkActionSeriesId : evaluationIds) {
            final NetworkEvaluation evaluation = new NetworkEvaluation();
            evaluation.setId(networkActionSeriesId);
            evaluations.add(evaluation);
        }
    }

    public Collection<Long> getNetworkEvaluationIds() {
        if (!areNetworkEvaluationsDefined()) {
            return null;
        }
        Collection<Long> networkEvaluationIds = new ArrayList<Long>();
        for (NetworkEvaluation oneNetworkEvaluation : evaluations) {
            networkEvaluationIds.add(oneNetworkEvaluation.getId());
        }
        return networkEvaluationIds;
    }

    public NetworkRunTask<T> getRunTask() {
        return runTask;
    }

    public void setRunTask(NetworkRunTask<T> runTask) {
        this.runTask = runTask;
    }

    public Network<T> getNetwork() {
        return runTask.getNetwork();
    }

    public void setNetwork(Network<T> network) {
        runTask.setNetwork(network);
    }

    public Long getNetworkId() {
        return runTask.getNetworkId();
    }

    public NetworkActionSeries<T> getActionSeries() {
        return runTask.getActionSeries();
    }

    public Long getActionSeriesId() {
        return runTask.getActionSeries().getId();
    }

    public NetworkSimulationConfig getSimulationConfig() {
        return runTask.getSimulationConfig();
    }
}