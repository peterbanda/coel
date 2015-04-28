package edu.banda.coel.domain.net;

import java.util.Collection;

import com.banda.core.domain.Point;
import com.banda.core.util.ObjectUtil;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class SpatialNetwork<TYPE, NODE_TYPE extends Node<TYPE>> extends Network<TYPE, NODE_TYPE>{

	private Point<Integer> dimensionSizes;
	private boolean torusFlag;            

	public SpatialNetwork() {
		super();
	}

	public SpatialNetwork(Collection<NODE_TYPE> nodes) {
		super(nodes);
	}

	public SpatialNetwork(Collection<NODE_TYPE> nodes, boolean torusFlag) {
		this(nodes);
		this.torusFlag = torusFlag;
	}

	public SpatialNetwork(Collection<NODE_TYPE> nodes, boolean torusFlag, Point<Integer> dimensionSizes) {
		this(nodes, torusFlag);
		this.dimensionSizes = dimensionSizes;
		int spaceSize = getSpaceSize();
		if (nodes.size() != spaceSize) {
			throw new RuntimeException("The number of nodes in spatial network (" + nodes.size() + ") is not the same as the space size (" + spaceSize + ")!");
		}
	}
 
	protected void setDimensionSizes(Point<Integer> dimensionSizes) {
		this.dimensionSizes = dimensionSizes;
	}

	public int getNumberOfDimensions() {
		return dimensionSizes.getNumberOfCoordinates();
	}

	public Point<Integer> getDimensionSizes() {
		if (dimensionSizes == null) {
			initDimensionSizes();
		}
		return dimensionSizes; 
	}

	private NODE_TYPE getFirstNode() {
		if (!hasNodes()) {
			return null;
		}
		return getNodes().iterator().next();
	}

	protected void initDimensionSizes() {
		NODE_TYPE firstNode = getFirstNode();
		if (firstNode == null) {
			return;
		}
		Integer[] dimensionMaxs = new Integer[firstNode.getLocation().getNumberOfCoordinates()];
		for (NODE_TYPE node : getNodes()) {
			Point<Integer> location = node.getLocation();
			if (location != null) {
				int i = 0;
				for (Integer coordinate : location.getCoordinates()) {
					if (ObjectUtil.compareObjects(dimensionMaxs[i], coordinate) == 1) {
						dimensionMaxs[i] = coordinate;
					}
					i++;
				}				
			}
		}
		for (int i = 0; i < dimensionMaxs.length; i ++) {
			dimensionMaxs[i]++;
		}
		dimensionSizes = new Point<Integer>(dimensionMaxs);
	}

	// Must be the same as the number of nodes
	public int getSpaceSize() {
		if (dimensionSizes == null) {
			return 0;
		}
		int spaceSize = 1;
		for (Integer dimension : dimensionSizes.getCoordinates()) {
			spaceSize *= dimension.intValue();
		}
		return spaceSize;
	}

	protected void setTorusFlag(boolean torusFlag) {
		this.torusFlag = torusFlag;
	}

	public boolean isTorusFlag() {
		return torusFlag;
	}
}