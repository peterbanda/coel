package edu.banda.coel.business;

import java.util.*;

import com.banda.core.domain.Point;
import com.banda.core.util.ConversionUtil;
import com.banda.core.util.GeometryUtil;
import com.banda.core.util.RandomUtil;

import edu.banda.coel.domain.som.SelfOrganizedMap;
import edu.banda.coel.domain.som.SomNode;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class SelfOrganizedMapBO<IN_TYPE extends Number & Comparable<IN_TYPE>> {
	
	private static class SomNodeDistance<IN_TYPE> implements Comparable<SomNodeDistance<IN_TYPE>> {
		private SomNode<IN_TYPE> somNode;
		private Double distance;

		private SomNodeDistance(SomNode<IN_TYPE> somNode, Double distance) {
			this.somNode = somNode;
			this.distance = distance;
		}

		@Override
		public int compareTo(SomNodeDistance<IN_TYPE> o) {
			return new Double(distance).compareTo(o.distance);
		}
	}

	private static class InputSampleDistance implements Comparable<InputSampleDistance> {
		private int inputIndex;
		private Double distance;

		private InputSampleDistance(int inputIndex, Double distance) {
			this.inputIndex = inputIndex;
			this.distance = distance;
		}

		@Override
		public int compareTo(InputSampleDistance o) {
			return new Double(distance).compareTo(o.distance);
		}
	}

	private SelfOrganizedMap<IN_TYPE> som;
	private int iteration = 0;

	public SelfOrganizedMapBO(SelfOrganizedMap<IN_TYPE> som) {
		this.som = som;
	}

	public void initializeWeights() {
		IN_TYPE[] mins = som.createBlankWeightsArray();
		IN_TYPE[] maxs = som.createBlankWeightsArray();
		for (IN_TYPE[] input : som.getInputs()) {
			for (int i = 0; i < input.length; i++) {
				if (mins[i] == null) {
					mins[i] = input[i];
				} else if (input[i].compareTo(mins[i]) == -1) { 
					mins[i] = input[i];
				}
				if (maxs[i] == null) {
					maxs[i] = input[i];
				} else if (input[i].compareTo(maxs[i]) == 1) { 
					maxs[i] = input[i];
				}
			}
		}

		Class<IN_TYPE> inTypeClazz = (Class<IN_TYPE>) mins[0].getClass();
		for (SomNode<IN_TYPE> node : som.getNodes()) {
			IN_TYPE[] nodeWeights = som.createBlankWeightsArray();
			for (int i = 0; i < nodeWeights.length; i++) {
				nodeWeights[i] = RandomUtil.next(inTypeClazz, mins[i], maxs[i]);
			}
			node.setWeights(nodeWeights);
		}
	}

	public void initializeTopology() {
		Map<Point<Integer>, SomNode<IN_TYPE>> pointNodeMap = new HashMap<Point<Integer>, SomNode<IN_TYPE>>();
		List<SomNode<IN_TYPE>> nodesToHandle = new ArrayList<SomNode<IN_TYPE>>();
		Iterator<SomNode<IN_TYPE>> nodesReserviorIterator = som.getNodes().iterator();

		SomNode<IN_TYPE> firstNode = nodesReserviorIterator.next();
		firstNode.setLocation(GeometryUtil.getZeroPoint(som.getNumberOfDimensions()));
		pointNodeMap.put(firstNode.getLocation(), firstNode);
		nodesToHandle.add(firstNode);

		while (!nodesToHandle.isEmpty()) {
			SomNode<IN_TYPE> nodeToHandle = nodesToHandle.remove(0);
			Collection<Point<Integer>> neighborLocations = 
				GeometryUtil.getSurroundingPoints(nodeToHandle.getLocation(), som.getDimensionSizes(), som.isTorusFlag());
			for (Point<Integer> neighborLocation : neighborLocations) {
				SomNode<IN_TYPE> neighbor = pointNodeMap.get(neighborLocation);
				if (neighbor == null) {
					// node at given location has not been found,
					// thus we take first available node from reservoir and attach it to that location
					while ((neighbor == null || neighbor.hasLocation())
							&& nodesReserviorIterator.hasNext()) {
						neighbor = nodesReserviorIterator.next();
					}
					if (neighbor.hasLocation()) {
						// all nodes already have locations...bad! - this should never happen
						new RuntimeException("Node with location " + neighborLocation + " has not been found, but all nodes already have locations!");
					} else {
						neighbor.setLocation(neighborLocation);
						pointNodeMap.put(neighbor.getLocation(), neighbor);
						nodesToHandle.add(neighbor);
					}
				}
				nodeToHandle.addNeighbor(neighbor);
			}
		}
	}

	public List<IN_TYPE[]> getInputs() {
		return som.getInputs();
	}

	public List<Point<Integer>> getDistanceBasedBmuLocations() {
		List<Point<Integer>> locations = new ArrayList<Point<Integer>>();
		List<SomNode<IN_TYPE>> bmuNodes = getDistanceBasedBmuNodes(som.getInputs());
		for (SomNode<?> bmuNode : bmuNodes) {
			locations.add(bmuNode.getLocation());			
		}
		return locations;
	}

	public void learnAndInitWeights() {
		initializeWeights();
		iteration = 0;
		while (learnOneStep());
    }

	public boolean learnOneStep() {
		List<IN_TYPE[]> inputs = som.getInputs();

		if (iteration < som.getMaxNumberOfInterations()) {
			IN_TYPE[] inputSample = inputs.get(RandomUtil.nextInt(0, inputs.size()));
	        //Find its best matching11 unit
			SomNode<IN_TYPE> bestMatchingUnit = getBestMatchingUnit(inputSample);

			//Scale the neighbors according to t
			adjustNeighborhoodWeights(bestMatchingUnit, inputSample);				
			iteration++;
			return true;
        }
		return false;
    }

	protected void adjustNeighborhoodWeights(
		SomNode<IN_TYPE> bmu,
		IN_TYPE[] inputSample
	) {
		Map<SomNode<IN_TYPE>, Double> nodeDistancesMap = new HashMap<SomNode<IN_TYPE>, Double>();
		nodeDistancesMap.put(bmu, new Double(0));
		adjustNeighborhoodWeights(bmu, nodeDistancesMap, inputSample, getLearningRate());
	}

	private void adjustNeighborhoodWeights(
		SomNode<IN_TYPE> node,
		Map<SomNode<IN_TYPE>, Double> nodeDistancesMap,
		IN_TYPE[] inputSample,
		double learningRate
	) {
		double neighborhoodRadius = getNeighborhoodRadius();
		double neighborhoodRadiusSq = neighborhoodRadius * neighborhoodRadius;
		double distanceToBmu = nodeDistancesMap.get(node);
		if (distanceToBmu <= neighborhoodRadius) {
//			double influence = getInfluence(distanceToBmu, neighborhoodRadius);
			double influence = getInfluence(distanceToBmu * distanceToBmu, neighborhoodRadiusSq);
			adjustWeightsForNode(node, inputSample, learningRate, influence);
			Collection<SomNode<IN_TYPE>> neighborsToHandle = new ArrayList<SomNode<IN_TYPE>>();
			for (SomNode<IN_TYPE> neighbor : node.getTypedNeighbors()) { 
				if ((!nodeDistancesMap.containsKey(neighbor))) {
					nodeDistancesMap.put(neighbor, new Double(distanceToBmu + 1));
					neighborsToHandle.add(neighbor);
				}
			}
			for (SomNode<IN_TYPE> neighborToHandle : neighborsToHandle) {
				adjustNeighborhoodWeights(neighborToHandle, nodeDistancesMap, inputSample, learningRate);	
			}
		}
	}

	public void adjustWeightsForNode(
		SomNode<IN_TYPE> node,
		IN_TYPE[] inputSample,
		double learningRate,
		double influence
	) {
		int weightIndex = 0;
		Class<IN_TYPE> inTypeClass = getInTypeClass();
		for (IN_TYPE weight : node.getWeights()) {
			double oldWeight = weight.doubleValue();
			double inputWeight = inputSample[weightIndex].doubleValue();
			double newWeight = oldWeight + influence * learningRate * (inputWeight - oldWeight);
			node.setWeight(weightIndex, ConversionUtil.convert(newWeight, inTypeClass));
			weightIndex++;
		}
	}

	public SomNode<IN_TYPE> getBestMatchingUnit(IN_TYPE[] inputSample) {
		SomNode<IN_TYPE> bmu = null;
		double minDistance = Double.MAX_VALUE;
		List<SomNode<IN_TYPE>> nodes = new ArrayList<SomNode<IN_TYPE>>();
		nodes.addAll(som.getNodes());
		Collections.shuffle(nodes);
		for (SomNode<IN_TYPE> node : nodes) {
			double distance = getWeightDistance(node, inputSample);
			if (distance < minDistance) {
				bmu = node;
				minDistance = distance;
			}
		}
		return bmu;
	}

	public List<SomNode<IN_TYPE>> getBestMatchingUnitsReverse(List<IN_TYPE[]> inputSamples) {
		List<SomNode<IN_TYPE>> bmuReverseNodes = new ArrayList<SomNode<IN_TYPE>>();
		for (int inputIndex = 0; inputIndex < inputSamples.size(); inputIndex++) {
			bmuReverseNodes.add(null);
		}

		Map<SomNode<IN_TYPE>, Integer[]> nodeInputMatchRanks = new HashMap<SomNode<IN_TYPE>, Integer[]>(); 
		for (SomNode<IN_TYPE> node : som.getNodes()) {
			nodeInputMatchRanks.put(node, getInputMatchRanks(node, inputSamples));
		}
		int rank = 0;
		int resolvedInputs = 0;
		while (resolvedInputs < inputSamples.size()) {
			for (SomNode<IN_TYPE> somNode : nodeInputMatchRanks.keySet()) {
				Integer[] inputMatchRanks = nodeInputMatchRanks.get(somNode);
				if (inputMatchRanks != null && inputMatchRanks[rank] != null) {
					Integer matchedInputIndex = inputMatchRanks[rank];
					bmuReverseNodes.set(matchedInputIndex, somNode);
					// now null found input index for other nodes
					for (SomNode<IN_TYPE> otherNode : nodeInputMatchRanks.keySet()) {
						Integer[] otherInputMatchRanks = nodeInputMatchRanks.get(otherNode);
						if (otherInputMatchRanks != null) {
							for (int rankRest = rank; rankRest < inputSamples.size(); rankRest++) {
								if (otherInputMatchRanks[rankRest] == matchedInputIndex) {
									otherInputMatchRanks[rankRest] = null;
									break;
								}
							}
						}
					}
					resolvedInputs++;
					nodeInputMatchRanks.put(somNode, null);
				}
			}
			rank++;
		}
		return bmuReverseNodes;
	}

	public List<SomNode<IN_TYPE>> getTradeOffRankBmuNodes(List<IN_TYPE[]> inputSamples) {
		List<SomNode<IN_TYPE>> tradeOffRankBmuNodes = new ArrayList<SomNode<IN_TYPE>>();
		for (int inputIndex = 0; inputIndex < inputSamples.size(); inputIndex++) {
			tradeOffRankBmuNodes.add(null);
		}
		List<SomNode<IN_TYPE>[]> bmuNodes = new ArrayList<SomNode<IN_TYPE>[]>();
		for (IN_TYPE[] input : inputSamples) {
			bmuNodes.add((SomNode<IN_TYPE>[]) getDistanceBasedSortedSomNodes(input).toArray(new SomNode<?>[som.getNumberOfNodes()]));
		}
		int foundBmuNumber = 0;
		int rank = 0;
		while (foundBmuNumber < inputSamples.size()) {
			for (int inputIndex = 0; inputIndex < inputSamples.size(); inputIndex++) {
				SomNode<IN_TYPE>[] rankBmuNodes = bmuNodes.get(inputIndex);
				if (rankBmuNodes != null && rankBmuNodes[rank] != null) {
					SomNode<IN_TYPE> somNodeMatch = rankBmuNodes[rank];
					foundBmuNumber++;
					tradeOffRankBmuNodes.set(inputIndex, somNodeMatch);
					// now null found bmu node for other inputs
					for (SomNode<IN_TYPE>[] someBmuNodes : bmuNodes) {
						if (someBmuNodes != null) {
							for (int rankRest = rank; rankRest < som.getNumberOfNodes(); rankRest++) {
								if (someBmuNodes[rankRest] == somNodeMatch) {
									someBmuNodes[rankRest] = null;
								}
							}
						}
					}
					// and remove rank bmu array for handled input totally
					bmuNodes.set(inputIndex, null);
				}
			}
			rank++;
		}
		return tradeOffRankBmuNodes;
	}

	public List<SomNode<IN_TYPE>> getDistanceBasedBmuNodes(List<IN_TYPE[]> inputSamples) {
		List<SomNode<IN_TYPE>> distanceBasedBmuNodes = new ArrayList<SomNode<IN_TYPE>>();
		for (int inputIndex = 0; inputIndex < inputSamples.size(); inputIndex++) {
			distanceBasedBmuNodes.add(null);
		}
		List<List<SomNodeDistance<IN_TYPE>>> inputAssocSomNodeDistances = new ArrayList<List<SomNodeDistance<IN_TYPE>>>();
		for (IN_TYPE[] input : inputSamples) {
			inputAssocSomNodeDistances.add(getSortedSomNodeDistances(input));
		}
		for (int i = 0; i < inputSamples.size(); i++) {
			SomNodeDistance<IN_TYPE> bestNodeDistance = null;
			int bestInputIndex = -1;
			for (int inputIndex = 0; inputIndex < inputAssocSomNodeDistances.size(); inputIndex++) {
				List<SomNodeDistance<IN_TYPE>> inputSomNodeDistances = inputAssocSomNodeDistances.get(inputIndex);
				if (inputSomNodeDistances != null) {
					SomNodeDistance<IN_TYPE> nodeDistance = inputSomNodeDistances.get(0);
					if (bestNodeDistance == null || bestNodeDistance.compareTo(nodeDistance) == 1)
					{
						bestNodeDistance = nodeDistance;
						bestInputIndex = inputIndex;
					}
				}
			}
			distanceBasedBmuNodes.set(bestInputIndex, bestNodeDistance.somNode);
			inputAssocSomNodeDistances.set(bestInputIndex, null);
			for (List<SomNodeDistance<IN_TYPE>> inputSomNodeDistances : inputAssocSomNodeDistances) {
				if (inputSomNodeDistances != null) {
					for (SomNodeDistance<IN_TYPE> somNodeDistance : inputSomNodeDistances) {
						if (somNodeDistance.somNode == bestNodeDistance.somNode) {
							inputSomNodeDistances.remove(somNodeDistance);
							break;
						}
					}
				}
			}
		}
		return distanceBasedBmuNodes;
	}

	public List<SomNode<IN_TYPE>> getInputDistanceBasedBmuNodes(List<IN_TYPE[]> inputSamples) {
		List<SomNode<IN_TYPE>> inputDistanceBasedBmuNodes = new ArrayList<SomNode<IN_TYPE>>();
		for (int inputIndex = 0; inputIndex < inputSamples.size(); inputIndex++) {
			inputDistanceBasedBmuNodes.add(null);
		}
		Map<SomNode<IN_TYPE>, List<InputSampleDistance>> nodeAssocSortedInputSampleDistances = new HashMap<SomNode<IN_TYPE>, List<InputSampleDistance>>();
		for (SomNode<IN_TYPE> somNode : som.getNodes()) {
			nodeAssocSortedInputSampleDistances.put(somNode, getSortedInputSampleDistances(somNode, inputSamples));
		}
		for (int i = 0; i < som.getNumberOfNodes(); i++) {
			InputSampleDistance bestInputSampleDistance = null;
			SomNode<IN_TYPE> bestNode = null;
			for (SomNode<IN_TYPE> somNode : nodeAssocSortedInputSampleDistances.keySet()) {
				List<InputSampleDistance> nodeInputSampleDistances = nodeAssocSortedInputSampleDistances.get(somNode);
				InputSampleDistance inputSampleDistance = nodeInputSampleDistances.get(0);
				if (bestInputSampleDistance == null || bestInputSampleDistance.compareTo(inputSampleDistance) == 1)
				{
					bestInputSampleDistance = inputSampleDistance;
					bestNode = somNode;
				}
			}
			inputDistanceBasedBmuNodes.set(bestInputSampleDistance.inputIndex, bestNode);
			nodeAssocSortedInputSampleDistances.remove(bestNode);
			for (SomNode<IN_TYPE> somNode : nodeAssocSortedInputSampleDistances.keySet()) {
				List<InputSampleDistance> nodeInputSampleDistances = nodeAssocSortedInputSampleDistances.get(somNode);
				for (InputSampleDistance inputSampleDistance : nodeInputSampleDistances) {
					if (inputSampleDistance.inputIndex == bestInputSampleDistance.inputIndex) {
						nodeInputSampleDistances.remove(inputSampleDistance);
						break;
					}
				}
			}
		}
		return inputDistanceBasedBmuNodes;
	}

	private Integer[] getInputMatchRanks(SomNode<IN_TYPE> somNode, List<IN_TYPE[]> inputSamples) {
		Integer[] inputMatchRanks = new Integer[inputSamples.size()];
		List<InputSampleDistance> inputDistances = getSortedInputSampleDistances(somNode, inputSamples);
		int  i = 0;
		for (InputSampleDistance inputDistance : inputDistances) {
			inputMatchRanks[i] = inputDistance.inputIndex;
			i++;
		}
		return inputMatchRanks;
	}

	private List<InputSampleDistance> getSortedInputSampleDistances(SomNode<IN_TYPE> somNode, List<IN_TYPE[]> inputSamples) {
		List<InputSampleDistance> inputDistances = new ArrayList<InputSampleDistance>();
		int inputIndex = 0;
		for (IN_TYPE[] inputSample : inputSamples) {
			inputDistances.add(new InputSampleDistance(inputIndex, getWeightDistance(somNode, inputSample)));
			inputIndex++;
		}
		Collections.sort(inputDistances);
		return inputDistances;
	}

	private List<SomNode<IN_TYPE>> getDistanceBasedSortedSomNodes(IN_TYPE[] inputSample) {
		List<SomNodeDistance<IN_TYPE>> somNodeDistances = getSortedSomNodeDistances(inputSample);
		List<SomNode<IN_TYPE>> somNodes = new ArrayList<SomNode<IN_TYPE>>();
		for (SomNodeDistance<IN_TYPE> somNodeDistance : somNodeDistances) {
			somNodes.add(somNodeDistance.somNode);
		}
		return somNodes;
	}

	private List<SomNodeDistance<IN_TYPE>> getSortedSomNodeDistances(IN_TYPE[] inputSample) {
		List<SomNodeDistance<IN_TYPE>> somNodeDistances = new ArrayList<SomNodeDistance<IN_TYPE>>();
		for (SomNode<IN_TYPE> node : som.getNodes()) {
			SomNodeDistance<IN_TYPE> somNodeDistance = new SomNodeDistance<IN_TYPE>(node, getWeightDistance(node, inputSample));
			somNodeDistances.add(somNodeDistance);
		}
		Collections.sort(somNodeDistances);
		return somNodeDistances;
	}

	private double getWeightDistance(SomNode<IN_TYPE> somNode, IN_TYPE[] inputSample) {
		IN_TYPE[] weights = somNode.getWeights();
//		IN_TYPE[] roundedWeights = som.createBlankWeightsArray();
//		Class<IN_TYPE> inTypeClass = getInTypeClass();

//		for (int i = 0; i < weights.length; i++) {
//			Byte roundedWeight = GeneralUtils.convertAndRound(weights[i].doubleValue(), Byte.class);
//			roundedWeights[i] = GeneralUtils.convert(roundedWeight.doubleValue(), inTypeClass);
//		}
		return getEuclideanDistance(weights, inputSample);
//		return getManhattanDistance(roundedWeights, inputSample);
	}

	protected double getEuclideanDistance(IN_TYPE[] vector1, IN_TYPE[] vector2) {
		Point<?> numericalPoint1 = new Point<IN_TYPE>(vector1);
		Point<?> numericalPoint2 = new Point<IN_TYPE>(vector2);
		return GeometryUtil.getEuclideanDistance(numericalPoint1, numericalPoint2);
	}

	protected double getManhattanDistance(IN_TYPE[] vector1, IN_TYPE[] vector2) {
		Point<?> numericalPoint1 = new Point<IN_TYPE>(vector1);
		Point<?> numericalPoint2 = new Point<IN_TYPE>(vector2);
		return GeometryUtil.getManhattanDistance(numericalPoint1, numericalPoint2);
	}

	private double getInitialRadius() {
		double dimensionSizesSum = 0;
		for (Integer dimensionSize : som.getDimensionSizes().getCoordinates()) {
			dimensionSizesSum += dimensionSize;
		}
		return (dimensionSizesSum /  som.getDimensionSizes().getNumberOfCoordinates()) - 1;
	}

	public void removeNode(SomNode<IN_TYPE> somNode) {
		som.removeNode(somNode);
	}

	private double getRadiusTimeConstant() {
		return getTuningPhaseStart() / Math.log(getInitialRadius());
	}

	public double getNeighborhoodRadius() {
		if (iteration < getTuningPhaseStart()) {
			if (iteration == 0) {
				return getInitialRadius();
			}
			return 1 + Math.floor(1 + (getInitialRadius() - 1) * (1 - ((double)iteration / getTuningPhaseStart())));
//			return 1 + Math.floor(getInitialRadius() * Math.exp(-iteration / getRadiusTimeConstant()));
		}
		return 1.0;
	}

	private double getInfluence(double distSq, double radiusSq) {
		return Math.exp(-distSq / (1.5 * radiusSq));
	}

//	public double getLearningRate() {
//		return som.getInitialLearningRate() * Math.exp(-(double)iteration / som.getMaxNumberOfInterations());
////      som.getInitialLearningRate() * iteration / som.getMaxNumberOfInterations();
//	}

	private int getTuningPhaseStart() {
		return som.getMaxNumberOfInterations() / 2;
	}

	private double getTuningPhaseInitialLearningRate() {
		return 0.1;
	}

//	private double getLearningRateTimeConstant() {
//		return getTuningPhaseStart() / Math.log(1 + som.getInitialLearningRate());
//	}

	public double getLearningRate() {
		if (iteration < getTuningPhaseStart()) {
//			return getTuningPhaseInitialLearningRate() + 
//			(som.getInitialLearningRate() - getTuningPhaseInitialLearningRate()) * Math.exp(-(double)iteration / getLearningRateTimeConstant());		
//			return  -1 + (1 + som.getInitialLearningRate()) * Math.exp(-(double)iteration / getLearningRateTimeConstant());
//			return  getTuningPhaseInitialLearningRate() -1 + (1 + som.getInitialLearningRate() - getTuningPhaseInitialLearningRate()) * Math.exp(-(double)iteration / getLearningRateTimeConstant());
			return getTuningPhaseInitialLearningRate() + (som.getInitialLearningRate() - getTuningPhaseInitialLearningRate())* (1 - ((double)iteration / getTuningPhaseStart()));
		}
		return getTuningPhaseInitialLearningRate() *
			(1 - ((double) (iteration -  getTuningPhaseStart()) / (som.getMaxNumberOfInterations() - getTuningPhaseStart())));
//               som.getInitialLearningRate() * iteration / som.getMaxNumberOfInterations();
	}

	// nasty
	private Class<IN_TYPE> getInTypeClass() {
		return (Class<IN_TYPE>) som.getInputs().iterator().next()[0].getClass();
	}
}