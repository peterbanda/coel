package edu.banda.coel.task.network;

import java.util.ArrayList;
import java.util.Collection;

import com.banda.network.BndNetworkException;
import com.banda.network.domain.NetworkActionSeries;
import com.banda.network.domain.NetworkEvaluation;
import com.banda.network.domain.Network;
import com.banda.network.domain.NetworkSimulationConfig;
import com.banda.core.domain.task.Task;
import com.banda.core.util.ObjectUtil;

import edu.banda.coel.task.network.NetworkTaskParts;
import edu.banda.coel.task.network.NetworkTaskParts.NetworkMultipleActionSeriesHolder;
import edu.banda.coel.task.network.NetworkTaskParts.NetworkHolder;
import edu.banda.coel.task.network.NetworkTaskParts.NetworkMultipleEvaluationHolder;
import edu.banda.coel.task.network.NetworkTaskParts.NetworkSimulationConfigHolder;

/**
 * @author Peter Banda
 * @since 2013
 */
public class NetworkPerformanceEvaluateTask<T> extends Task implements NetworkMultipleActionSeriesHolder<T>, NetworkHolder<T>, NetworkMultipleEvaluationHolder, NetworkSimulationConfigHolder {

	private Network<T> network;
	private NetworkSimulationConfig simulationConfig;
	private Collection<NetworkActionSeries<T>> networkActionSeries;
	private Collection<NetworkEvaluation> evaluations;
	private Integer runTime;
	private Integer repetitions;

	public NetworkPerformanceEvaluateTask() {
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

	@Override
	public Collection<NetworkActionSeries<T>> getNetworkActionSeries() {
		return networkActionSeries;
	}

	@Override
	public void setNetworkActionSeries(Collection<NetworkActionSeries<T>> networkActionSeries) {
		this.networkActionSeries = networkActionSeries;
	}

	@Override
	public boolean areNetworkActionSeriesDefined() {
		return networkActionSeries != null;
	}

	@Override
	public boolean areNetworkActionSeriesComplete() {
		return ObjectUtil.getFirst(networkActionSeries).getName() != null;
	}

	public void setNetworkActionSeriesIds(Collection<Long> networkActionSeriesIds) {
		if (networkActionSeriesIds == null) {
			return;
		}
		networkActionSeries = new ArrayList<NetworkActionSeries<T>>();
		for (Long networkActionSeriesId : networkActionSeriesIds) {
			final NetworkActionSeries<T> oneNetworkActionSeries = new NetworkActionSeries<T>();
			oneNetworkActionSeries.setId(networkActionSeriesId);
			networkActionSeries.add(oneNetworkActionSeries);
		}
	}

	public Collection<Long> getNetworkActionSeriesIds() {
		if (!areNetworkActionSeriesDefined()) {
			return null;
		}
		Collection<Long> networkActionSeriesIds = new ArrayList<Long>();
		for (NetworkActionSeries<T> oneNetworkActionSeries : networkActionSeries) {
			networkActionSeriesIds.add(oneNetworkActionSeries.getId());
		}
		return networkActionSeriesIds;
	}

	@Override
	public Network<T> getNetwork() {
		return network;
	}

	@Override
	public void setNetwork(Network<T> network) {
		this.network = network;
	}

	@Override
	public boolean isNetworkDefined() {
		return network != null;
	}

	@Override
	public boolean isNetworkComplete() {
		return isNetworkDefined() && network.getName() != null;
	}

	public void setNetworkId(Long networkId) {
		network = new Network<T>();
		network.setId(networkId);
	}

	public Long getNetworkId() {
		if (isNetworkDefined()) {
			return network.getId();
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

	@Override
	public NetworkSimulationConfig getSimulationConfig() {
		return simulationConfig;
	}

	@Override
	public void setSimulationConfig(NetworkSimulationConfig simulationConfig) {
		this.simulationConfig = simulationConfig;
	}

	@Override
	public boolean isSimulationConfigDefined() {
		return simulationConfig == null;
	}

	@Override
	public boolean isSimulationConfigComplete() {
		return isSimulationConfigDefined();
	}
}