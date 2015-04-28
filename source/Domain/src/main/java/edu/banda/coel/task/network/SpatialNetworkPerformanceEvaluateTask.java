package edu.banda.coel.task.network;

import java.util.ArrayList;
import java.util.Collection;

import com.banda.network.BndNetworkException;
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
public class SpatialNetworkPerformanceEvaluateTask<T> extends NetworkPerformanceEvaluateTask<T> {

	private Integer sizeFrom;
	private Integer sizeTo;

	public SpatialNetworkPerformanceEvaluateTask() {
		super();
	}

	public Integer getSizeFrom() {
		return sizeFrom;
	}

	public void setSizeFrom(Integer sizeFrom) {
		this.sizeFrom = sizeFrom;
	}

	public Integer getSizeTo() {
		return sizeTo;
	}

	public void setSizeTo(Integer sizeTo) {
		this.sizeTo = sizeTo;
	}
}