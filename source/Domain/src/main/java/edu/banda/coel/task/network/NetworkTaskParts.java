package edu.banda.coel.task.network;

import com.banda.network.domain.Network;
import com.banda.network.domain.NetworkActionSeries;
import com.banda.network.domain.NetworkEvaluation;
import com.banda.network.domain.NetworkSimulationConfig;

import java.util.Collection;

/**
 * @author Peter Banda
 * @since 2013
 */
public interface NetworkTaskParts {

    public interface NetworkSimulationConfigHolder {
        public NetworkSimulationConfig getSimulationConfig();

        public void setSimulationConfig(NetworkSimulationConfig simulationConfig);

        public boolean isSimulationConfigDefined();

        public boolean isSimulationConfigComplete();
    }

    public interface NetworkHolder<T> {
        public Network<T> getNetwork();

        public void setNetwork(Network<T> network);

        public boolean isNetworkDefined();

        public boolean isNetworkComplete();
    }

    public interface NetworkActionSeriesHolder<T> {
        public NetworkActionSeries<T> getActionSeries();

        public void setActionSeries(NetworkActionSeries<T> actionSeries);

        public boolean isActionSeriesDefined();

        public boolean isActionSeriesComplete();
    }

    public interface NetworkMultipleActionSeriesHolder<T> {
        public Collection<NetworkActionSeries<T>> getNetworkActionSeries();

        public void setNetworkActionSeries(Collection<NetworkActionSeries<T>> actionSeries);

        public Collection<Long> getNetworkActionSeriesIds();

        public boolean areNetworkActionSeriesDefined();

        public boolean areNetworkActionSeriesComplete();
    }

    public interface NetworkMultipleEvaluationHolder {
        public Collection<NetworkEvaluation> getNetworkEvaluations();

        public void setNetworkEvaluations(Collection<NetworkEvaluation> evaluations);

        public Collection<Long> getNetworkEvaluationIds();

        public boolean areNetworkEvaluationsDefined();

        public boolean areNetworkEvaluationsComplete();
    }

    public interface NetworkEvaluationHolder {
        public NetworkEvaluation getNetworkEvaluation();

        public void setNetworkEvaluation(NetworkEvaluation evaluation);

        public boolean isNetworkEvaluationDefined();

        public boolean isNetworkEvaluationComplete();
    }
}