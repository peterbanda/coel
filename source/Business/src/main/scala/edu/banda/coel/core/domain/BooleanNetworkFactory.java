package edu.banda.coel.core.domain;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.banda.core.util.RandomUtil;
import com.banda.function.business.FunctionFactory;
import com.banda.function.domain.BooleanFunction;
import com.banda.function.domain.BooleanFunction.BooleanFunctionType;
import com.banda.function.domain.Function;

import edu.banda.coel.domain.bn.BooleanNetwork;
import edu.banda.coel.domain.net.FunctionNode;
import edu.banda.coel.domain.net.Node;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class BooleanNetworkFactory {

	private final FunctionFactory functionFactory = new FunctionFactory();
	private static final BooleanNetworkFactory instance = new BooleanNetworkFactory();
	
	private BooleanNetworkFactory() {
		// nothing to do
	}

	public static BooleanNetworkFactory getInstance() {
		return instance;
	}

	// functions must be associated before
	private void createRandomConnections(List<FunctionNode<Boolean>> nodes) {
		for (FunctionNode<Boolean> node : nodes) {
			int functionsArity = node.getFunction().getArity();
			for (int i = 0; i < functionsArity; i++) {
				Node<Boolean> randomNode = null;
				do {
					randomNode = nodes.get(RandomUtil.nextInt(0, nodes.size()));
				} while (node.getNeighbors().contains(randomNode));
				node.addNeighbor(randomNode);
			}
		}
	}

	private void createRandomConnections(List<FunctionNode<Boolean>> nodes, int neighborsNum) {
		for (FunctionNode<Boolean> node : nodes) {
			for (int i = 0; i < neighborsNum; i++) {
				Node<Boolean> randomNode = null;
				do {
					randomNode = nodes.get(RandomUtil.nextInt(0, nodes.size()));
				} while (node.getNeighbors().contains(randomNode));
				node.addNeighbor(randomNode);
			}
		}
	}

	public BooleanNetwork createRandomBNWithANDFun(int numberOfNodes, int fixK) {
		List<FunctionNode<Boolean>> nodes = new ArrayList<FunctionNode<Boolean>>();
		// First we create nodes with random boolean functions
		for (int i = 0; i < numberOfNodes; i++) {
			BooleanFunction ANDFunction = new BooleanFunction(BooleanFunctionType.AND);
			nodes.add(new FunctionNode<Boolean>(ANDFunction));
		}

		// Now we handle connections (neighborhoods)
		createRandomConnections(nodes, fixK);
		return new BooleanNetwork(nodes, fixK);
	}

	public BooleanNetwork createRandomBNWithMixAND_ORFun(int numberOfNodes, int fixK, boolean torusFlag) {
		List<FunctionNode<Boolean>> nodes = new ArrayList<FunctionNode<Boolean>>();
		// First we create nodes with random boolean functions
		for (int i = 0; i < numberOfNodes; i++) {
			BooleanFunction binaryFunction = new BooleanFunction(RandomUtil.nextBoolean() ? BooleanFunctionType.AND : BooleanFunctionType.OR);
			nodes.add(new FunctionNode<Boolean>(binaryFunction));
		}

		// Now we handle connections (neighborhoods)
		createRandomConnections(nodes, fixK);
		return new BooleanNetwork(nodes, fixK, torusFlag);
	}

	public BooleanNetwork createRandomBNWithFixK(int numberOfNodes, int fixK, boolean torusFlag) {
		List<FunctionNode<Boolean>> nodes = new ArrayList<FunctionNode<Boolean>>();
		// First we create nodes with random boolean functions
		for (int i = 0; i < numberOfNodes; i++) {
			Function<Boolean, Boolean> randomFunction = functionFactory.createRandomBoolTransitionTable(fixK);
			nodes.add(new FunctionNode<Boolean>(randomFunction));
		}

		// Now we handle connections (neighborhoods)
		createRandomConnections(nodes);
		return new BooleanNetwork(nodes, fixK, torusFlag);
	}

	public BooleanNetwork create1dCycleBNWithRandomFunctions(int numberOfNodes) {
		List<FunctionNode<Boolean>> nodes = new ArrayList<FunctionNode<Boolean>>();
		// First we create nodes with random boolean functions
		for (int i = 0; i < numberOfNodes; i++) {
			Function<Boolean, Boolean> randomFunction = functionFactory.createRandomBoolTransitionTable(1);
			nodes.add(new FunctionNode<Boolean>(randomFunction));
		}

		// Now we handle connections (neighborhoods)
		int i = 1;
		for (Node<Boolean> node : nodes) {
			node.addNeighbor(nodes.get(i));
			i++;
			if (i == nodes.size()) {
				i = 0;
			}
		}
		return new BooleanNetwork(nodes, 1, true);
	}

	// Adjacency matrix supports multiple connections between same nodes
	// In that case associated matrix element holds the number of multiple connections (> 1)
	public BooleanNetwork createBNWithRandomFunctions(Collection<Byte[]> adjacencyMatrix) {
		List<FunctionNode<Boolean>> nodes = new ArrayList<FunctionNode<Boolean>>();
		// First we create nodes with random boolean functions 
		for (Byte[] adjacencyVector : adjacencyMatrix) {
			int neighborsNum = 0;
			for (Byte adjacencyFlag : adjacencyVector) {
				neighborsNum += adjacencyFlag;
			}
			Function<Boolean, Boolean> randomFunction = functionFactory.createRandomBoolTransitionTable(neighborsNum);
			nodes.add(new FunctionNode<Boolean>(randomFunction));
		}

		// Now we handle connections (neighborhoods)
		Iterator<FunctionNode<Boolean>> nodesIterator = nodes.iterator();
		for (Byte[] adjacencyVector : adjacencyMatrix) {
			FunctionNode<Boolean> node = nodesIterator.next();
			int neighborId = 0;
			for (Byte adjacencyFlag : adjacencyVector) {
				if (adjacencyFlag.compareTo(new Byte((byte) 0)) > 0) {
					Node<Boolean> neighbor = nodes.get(neighborId);
					for (int i = 0; i < adjacencyFlag; i++) {
						node.addNeighbor(neighbor);
					}
				}
				neighborId++;
			}
		}
		return new BooleanNetwork(nodes, null, true);
	}

//	public BooleanNetwork createRandomBNWithMedianK(int numberOfNodes, int medianK) {
//		List<Node<Boolean>> nodes = new ArrayList<Node<Boolean>>();
//		// First we create nodes with random boolean functions
//		for (int i = 0; i < numberOfNodes; i++) {
//			Function<Boolean, Boolean> randomFunction = getFunctionFactory().nextBoolTransitionTable(numberOfNodes);
//			nodes.add(new Node<Boolean>(randomFunction));
//		}
//
//		// Now we handle connections (neighborhoods)
//		Node<Boolean> randomNode = null;
//		for (Node<Boolean> node : nodes) {
//			for (int i = 0; i < fixK; i++) {
//				randomNode = nodes.get(GeneralUtils.nextInt(0, nodes.size() - 1));
//				node.addNeighbor(randomNode);
//			}
//		}
//		return new BooleanNetwork(nodes, fixK);
//	}
}