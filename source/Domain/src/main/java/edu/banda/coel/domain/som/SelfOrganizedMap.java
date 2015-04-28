package edu.banda.coel.domain.som;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.banda.core.domain.Point;

import edu.banda.coel.domain.net.SpatialNetwork;

/**
 * @author Peter Banda
 * @since 2011
 */
public class SelfOrganizedMap<IN_TYPE extends Number & Comparable<IN_TYPE>> extends SpatialNetwork<IN_TYPE[], SomNode<IN_TYPE>> {

	private List<IN_TYPE[]> inputs;
	private int maxNumberOfInterations;
	private double initialLearningRate;

	// TODO: change to protected
	public SelfOrganizedMap(
		List<IN_TYPE[]> inputs,
		Point<Integer> dimensionSizes,
		int numberOfIterations,
		double initialLearningRate,
		boolean torusFlag
	) {
		super(null, torusFlag, dimensionSizes);
		this.inputs = inputs;
		checkInputs();
		this.maxNumberOfInterations = numberOfIterations;
		this.initialLearningRate = initialLearningRate;
	}

	protected SelfOrganizedMap(
		List<IN_TYPE[]> inputs,
		Point<Integer> dimensionSizes,
		int numberOfIterations,
		double initialLearningRate,
		boolean torusFlag,
		Collection<SomNode<IN_TYPE>> nodes
	) {
		this(inputs, dimensionSizes, numberOfIterations, initialLearningRate, torusFlag);
		setNodes(nodes);
	}

	private void initNodes() {
		Collection<SomNode<IN_TYPE>> nodes = new HashSet<SomNode<IN_TYPE>>();
		int spaceSize = getSpaceSize();
		for (int i = 0; i < spaceSize; i++) {
			nodes.add(new SomNode<IN_TYPE>(null));
		}
		setNodes(nodes);
	}

	@Override
	public Collection<SomNode<IN_TYPE>> getNodes() {
		if (!hasNodes()) {
			initNodes();
		}
		return super.getNodes();
	}

	public List<IN_TYPE[]> getInputs() {
		return inputs;
	}

	/**
	 * Gets the size of input (the same of all inputs).
	 * 
	 * @return The size of input
	 */
	public int getSizeOfInput() {
		return getFirstInput().length;			
	}

	private IN_TYPE[] getFirstInput() {
		return inputs.iterator().next();			
	}

	public IN_TYPE[] createBlankWeightsArray() {
		IN_TYPE[] blankWeights = getFirstInput().clone();
		for (int i = 0; i <  blankWeights.length; i++) {
			blankWeights[i] = null;
		}
		return blankWeights;
	}

	public int getMaxNumberOfInterations() {
		return maxNumberOfInterations;
	}

	public double getInitialLearningRate() {
		return initialLearningRate;
	}

	private void checkInputs() {
		if (inputs == null || inputs.isEmpty()) {
			throw new RuntimeException("Inputs not available!");
		}
		int sizeOfInput = -1; 
		for (IN_TYPE[] input : inputs) {
			if (sizeOfInput == -1) {
				sizeOfInput = input.length;
			} else if (sizeOfInput != input.length) {
				throw new RuntimeException("The size of input " + input.length + " not the same as " + sizeOfInput);
			}
		}
	}
}