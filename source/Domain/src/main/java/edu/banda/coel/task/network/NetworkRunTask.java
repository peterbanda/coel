package edu.banda.coel.task.network;

import com.banda.core.domain.task.Task;

import com.banda.network.domain.Network;
import com.banda.network.domain.NetworkActionSeries;
import com.banda.network.domain.NetworkSimulationConfig;

import edu.banda.coel.task.network.NetworkTaskParts.NetworkActionSeriesHolder;
import edu.banda.coel.task.network.NetworkTaskParts.NetworkHolder;
import edu.banda.coel.task.network.NetworkTaskParts.NetworkSimulationConfigHolder;

/**
 * @author Peter Banda
 * @since 2013
 */
public class NetworkRunTask<T> extends Task implements NetworkHolder<T>, NetworkActionSeriesHolder<T>, NetworkSimulationConfigHolder {

	private Network<T> network;
	private NetworkSimulationConfig simulationConfig;
	private NetworkActionSeries<T> actionSeries;

	private Integer runTime;
	private Integer repetitions = 1;

	public NetworkRunTask() {
		super();
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

	@Override
	public Network<T> getNetwork() {
		return network;
	}

	@Override
	public void setNetwork(Network<T> network) {
		this.network = network;
	}

	public void setNetworkId(Long networkId) {
		network = new Network<T>();
		network.setId(networkId);
	}

	public Long getNetworkId() {
		if (isNetworkDefined())
			return network.getId();
		return null;
	}

	@Override
	public boolean isNetworkDefined() {
		return network != null;
	}

	@Override
	public boolean isNetworkComplete() {
		return network.getTopology() != null;
	}

	@Override
	public NetworkActionSeries<T> getActionSeries() {
		return actionSeries;
	}

	@Override
	public void setActionSeries(NetworkActionSeries<T> actionSeries) {
		this.actionSeries = actionSeries;
	}

	@Override
	public boolean isActionSeriesDefined() {
		return actionSeries != null;
	}

	@Override
	public boolean isActionSeriesComplete() {
		return isActionSeriesDefined() && actionSeries.getName() != null;
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
}