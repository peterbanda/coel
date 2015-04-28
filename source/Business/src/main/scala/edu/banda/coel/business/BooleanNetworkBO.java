package edu.banda.coel.business;

import java.util.*;

import com.banda.core.domain.Point;
import com.banda.core.util.GeometryUtil;
import com.banda.core.util.RandomUtil;
import com.banda.function.business.FunctionFactory;
import com.banda.function.domain.Function;
import com.banda.function.evaluator.FunctionEvaluator;
import com.banda.function.evaluator.FunctionEvaluatorFactory;

import edu.banda.coel.CoelRuntimeException;
import edu.banda.coel.domain.bn.BooleanNetwork;
import edu.banda.coel.domain.net.FunctionNode;
import edu.banda.coel.domain.net.Node;
import edu.banda.coel.domain.util.GeneralUtil;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class BooleanNetworkBO {

	private final BooleanNetwork bn;
	private final FunctionEvaluatorFactory functionEvaluatorFactor;
	private final FunctionFactory functionFactory = new FunctionFactory();

    private boolean storePreviousNodeStateFlag = true;  // TODO
    private Map<FunctionNode<Boolean>, FunctionEvaluator<Boolean, Boolean>> nodeEvaluatorMap = 
    		new HashMap<FunctionNode<Boolean>, FunctionEvaluator<Boolean, Boolean>>();

	public BooleanNetworkBO(BooleanNetwork bn, FunctionEvaluatorFactory functionEvaluatorFactor) {
		this.bn = bn;
		this.functionEvaluatorFactor = functionEvaluatorFactor;
		initNodeEvaluatorMap();
	}

	private void initNodeEvaluatorMap() {
		for (FunctionNode<Boolean> functionNode : bn.getNodes()) {
			nodeEvaluatorMap.put(functionNode, functionEvaluatorFactor.createInstance(functionNode.getFunction()));
		}
	}

	public void setRandomStates() {
		bn.setNodesStates(
				RandomUtil.nextBooleanList(bn.getNumberOfNodes()));
	}

	public void setStatesToSingleActiveNode() {
		bn.setNodesStates(
				GeneralUtil.getBooleanListWithSingle1(bn.getNumberOfNodes()));
	}

	public void setStatesToZero() {
		bn.setNodesStates(
				GeneralUtil.getBooleanListWith0s(bn.getNumberOfNodes()));
	}

	public void setStatesToOne() {
		bn.setNodesStates(
				GeneralUtil.getBooleanListWith1s(bn.getNumberOfNodes()));
	}

	public void updateNodesStates() {
		List<Boolean> newStates = new ArrayList<Boolean>();
		for (FunctionNode<Boolean> node : bn.getNodes()) {
			if (storePreviousNodeStateFlag) {
				node.setPreviousState(new Boolean(node.getState()));
			}
			newStates.add(getNewState(node));
		}
		bn.setNodesStates(newStates);
	}

    private Boolean getNewState(FunctionNode<Boolean> node) {
    	List<Boolean> neighborstates = node.getNeighborstates();
    	FunctionEvaluator<Boolean, Boolean> evaluator = nodeEvaluatorMap.get(node);
    	if (evaluator == null) {
    		throw new CoelRuntimeException("Function evaluator not defined for '" + node.getFunction() + "'");
    	}
    	return evaluator.evaluate(neighborstates);
    }

	public void set2dTopology() {
        int numberOfNodes = bn.getNumberOfNodes();
        Point<Integer> rectangleSizes = GeometryUtil.getRectangleSizes(numberOfNodes);
        int sizex = rectangleSizes.getCoordinate(0);
        int x = 0;
        int y = 0;
        for (Node<?> node : bn.getNodes()) {
        	node.set2dLocation(x, y);
        	x++;
            if (x == sizex) {
            	y++;
            	x = 0;
            }
        }
	}

	public Collection<Boolean> getNodesStates() {
		return bn.getNodesStates();
 	}

	public void setNodesStates(Collection<Boolean> nodeStates) {
		List<Boolean> states = new ArrayList<Boolean>();
		states.addAll(nodeStates);
		bn.setNodesStates(states);
 	}

	public Collection<Boolean> getChangedNodesStateFlags() {
		Collection<Boolean> changedNodesStateFlags = new ArrayList<Boolean>();
		for (Node<Boolean> node : bn.getNodes()) {
			node.getChangedNodeStateFlag();
		}
		return changedNodesStateFlags;
 	}

	public int getNumberOfNodes() {
		return bn.getNumberOfNodes();
 	}

	public Collection<FunctionNode<Boolean>> getNodes() {
		return bn.getNodes();
 	}

	public void assignIdentifiers() {
		int identifier = 0;
		for (Node<Boolean> node : bn.getNodes()) {
			node.setIdentifier(new Integer(identifier));
			identifier++;
		}	
 	}

	public Node<Boolean> findNode(Integer nodeId) {
		for (Node<Boolean> node : bn.getNodes()) {
			if (nodeId.equals(node.getIdentifier())) {
				return node;
			}
		}
		return null;
	}

	public Boolean[] getNodeNeighborhoodFlags(Node<Boolean> node) {
		Boolean[] neighborhoodFlags = new Boolean[bn.getNumberOfNodes()];
		for (int i = 0; i < neighborhoodFlags.length; i++) {
			neighborhoodFlags[i] = Boolean.FALSE;
		}
		if (node != null) {
			for (Node<Boolean> neighbor : node.getNeighbors()) {
				Integer identifier = neighbor.getIdentifier();
				if (identifier == null) {
					new RuntimeException(
							"Node does not have identifier. Something went wrong!");
				}
				neighborhoodFlags[identifier] = true;
			}
		}
		return neighborhoodFlags;
 	}

	public Boolean[] getNodeNeighborhoodFlagsWithReflexivity(Node<Boolean> node) {
		Boolean[] neighborhoodFlags = new Boolean[bn.getNumberOfNodes()];
		for (int i = 0; i < neighborhoodFlags.length; i++) {
			neighborhoodFlags[i] = i == node.getIdentifier() ? Boolean.TRUE : Boolean.FALSE;
		}
		if (node != null) {
			for (Node<Boolean> neighbor : node.getNeighbors()) {
				Integer identifier = neighbor.getIdentifier();
				if (identifier == null) {
					new RuntimeException(
							"Node does not have an identifier. Something went wrong!");
				}
				neighborhoodFlags[identifier] = Boolean.TRUE;
			}
		}
		return neighborhoodFlags;
 	}

	public Boolean[] getNodeAdjacencyMatrixWithReflexivity(Node<Boolean> node) {
		int numberOfNodes = bn.getNumberOfNodes();
		Integer nodeId = node.getIdentifier();
		Boolean[] adjacencyMatrix = new Boolean[numberOfNodes * numberOfNodes];
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			adjacencyMatrix[i] = Boolean.FALSE;
		}
		for (int i = 0; i < numberOfNodes; i++) {
			adjacencyMatrix[nodeId + i * numberOfNodes] = Boolean.TRUE;
		}
		for (Node<Boolean> neighbor : node.getNeighbors()) {
			Integer neighborId = neighbor.getIdentifier();
			if (neighborId == null) {
				new RuntimeException("Node does not have identifier. Something went wrong!");
			}
			adjacencyMatrix[neighborId + nodeId * numberOfNodes] = Boolean.TRUE;
		}
		return adjacencyMatrix;
 	}

	public Collection<Boolean[]> getAllNodeNeighborhoodFlags() {
		Collection<Boolean[]> neighborhoodFlags = new ArrayList<Boolean[]>();
		for (Node<Boolean> node : bn.getNodes()) {
			neighborhoodFlags.add(getNodeNeighborhoodFlags(node));
		}		
		return neighborhoodFlags;
 	}

	public Collection<Boolean[]> getAllNodeNeighborhoodFlagsForUndirectedGraph() {
		List<Boolean[]> neighborhoodFlags = new ArrayList<Boolean[]>();
		for (int i = 0; i < bn.getNumberOfNodes(); i++) {
			Boolean[] nodeNeighborhoodFlags = new Boolean[bn.getNumberOfNodes()];
			for (int j = 0; j < bn.getNumberOfNodes(); j++) {
				nodeNeighborhoodFlags[j] = Boolean.FALSE;
			}
			neighborhoodFlags.add(nodeNeighborhoodFlags);
		}
		for (Node<Boolean> node : bn.getNodes()) {
			Boolean[] nodeNeighborhoodFlags = getNodeNeighborhoodFlags(node);
			Integer id = node.getIdentifier();
			Boolean[] resultNodeNeigborhoodFlags = neighborhoodFlags.get(id);
			for (int i = 0; i < nodeNeighborhoodFlags.length; i++) {
				if (nodeNeighborhoodFlags[i]) {
					resultNodeNeigborhoodFlags[i] = Boolean.TRUE;
					neighborhoodFlags.get(i)[id] = Boolean.TRUE;
				}
			}
		}		
		return neighborhoodFlags;
 	}

	public Collection<Boolean[]> getAllNodeNeighborhoodFlagsWithReflexivity() {
		Collection<Boolean[]> neighborhoodFlags = new ArrayList<Boolean[]>();
		for (Node<Boolean> node : bn.getNodes()) {
			neighborhoodFlags.add(getNodeNeighborhoodFlagsWithReflexivity(node));
		}		
		return neighborhoodFlags;
 	}

	public int getTotalNeighborDistance() {
		int totalDistance = 0;
		for (Node<?> node : bn.getNodes()) {
			totalDistance += getDistanceToNeighbors(node);
		}
		return totalDistance;
	}

	public List<Point<Integer>> getNodeLocations() {
		List<Point<Integer>> nodeLocations = new ArrayList<Point<Integer>>();
		for (Node<Boolean> node : bn.getNodes()) {
			nodeLocations.add(node.getLocation());
		}
		return nodeLocations;
	}

	public void setNodeLocations(Collection<Point<Integer>> locations) {
		Iterator<Point<Integer>> locationsIterator = locations.iterator();
		for (Node<Boolean> node : bn.getNodes()) {
			node.setLocation(locationsIterator.next());
		}
	}

	public void setFunctions(Collection<Function<Boolean, Boolean>> functions) {
		Iterator<Function<Boolean, Boolean>> functionsIterator = functions.iterator();
		for (FunctionNode<Boolean> node : bn.getNodes()) {
			node.setFunction(functionsIterator.next());
		}
		if (functionsIterator.hasNext()) {
			throw new RuntimeException("More functions than nodes in the network!");
		}
	}

	public void setRandomFunctions() {
		for (FunctionNode<Boolean> node : bn.getNodes()) {
			Function<Boolean, Boolean> randomFunction = functionFactory.createRandomBoolTransitionTable(
					node.getNeighbors().size());
			node.setFunction(randomFunction);
		}
	}

	public Collection<Boolean[]> getFunctionsOutputs() {
		Collection<Boolean[]> functionsOutputs = new ArrayList<Boolean[]>();
		for (FunctionNode<Boolean> node : bn.getNodes()) {
			BooleanFunctionBO bfBO = new BooleanFunctionBO(functionEvaluatorFactor.createInstance(node.getFunction()));
			functionsOutputs.add(
					bfBO.getAllOutputs(node.getNeighborsNumber()));
		}
		return functionsOutputs;
	}

	private int getDistanceToNeighbors(Node<?> node) {
		int totalDistance = 0;
		for (Node<?> neighbor : node.getNeighbors()) {
			totalDistance += bn.isTorusFlag() ?
					GeometryUtil.getTorusManhattanDistance(node.getLocation(), neighbor.getLocation(), bn.getDimensionSizes()) :
			        GeometryUtil.getManhattanDistance(node.getLocation(), neighbor.getLocation());
		}
		return totalDistance;
	}

	public BooleanNetwork getBn() {
		return bn;
	}	
}