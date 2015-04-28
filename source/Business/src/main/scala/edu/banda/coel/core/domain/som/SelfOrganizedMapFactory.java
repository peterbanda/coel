package edu.banda.coel.core.domain.som;

import java.util.ArrayList;
import java.util.List;

import com.banda.core.domain.Point;
import com.banda.core.util.ConversionUtil;
import com.banda.core.util.GeometryUtil;

import edu.banda.coel.business.BooleanNetworkBO;
import edu.banda.coel.business.SelfOrganizedMapBO;
import edu.banda.coel.domain.net.Node;
import edu.banda.coel.domain.som.SelfOrganizedMap;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class SelfOrganizedMapFactory {

	private static final int ITERATIONS_PER_NODE = 600;
	private static final double INITIAL_LEARNING_RATE = 1;
	private static SelfOrganizedMapFactory instance = new SelfOrganizedMapFactory();

	private SelfOrganizedMapFactory() {
		// Nothing to do
	}

	public static SelfOrganizedMapFactory getInstance() {
		return instance;
	}

	public <IN_TYPE extends Number & Comparable<IN_TYPE>> SelfOrganizedMap<IN_TYPE> createSelfOrganizedMap(
		List<IN_TYPE[]> inputs,
		Point<Integer> dimensionSizes,
		boolean torusFlag,
		double initialLearningRate,
		int numberOfIterations) {

		SelfOrganizedMap<IN_TYPE> som = new SelfOrganizedMap<IN_TYPE>(
			inputs,
			dimensionSizes,
			numberOfIterations,
			initialLearningRate,
			torusFlag);

		new SelfOrganizedMapBO<IN_TYPE>(som).initializeTopology();
		return som;
	}

	public <IN_TYPE extends Number & Comparable<IN_TYPE>> SelfOrganizedMap<IN_TYPE> createSelfOrganizedMap(
		List<IN_TYPE[]> inputs,
		Point<Integer> dimensionSizes,
		boolean torusFlag) {

		return createSelfOrganizedMap(inputs, dimensionSizes, torusFlag, INITIAL_LEARNING_RATE, ITERATIONS_PER_NODE * inputs.size());
	}

	public <IN_TYPE extends Number & Comparable<IN_TYPE>> SelfOrganizedMap<IN_TYPE> createRectangularSelfOrganizedMap(
		List<IN_TYPE[]> inputs,
		boolean torusFlag) {

		Point<Integer> rectangleSizes = GeometryUtil.getRectangleSizes(inputs.size());
		return createSelfOrganizedMap(inputs, rectangleSizes, torusFlag);
	}

	public SelfOrganizedMap<Double> createSelfOrganizedMapBO(
		BooleanNetworkBO bnBO,
		Point<Integer> dimensionSizes) {

		SelfOrganizedMap<Double> som = null;

		List<Double[]> inputs = new ArrayList<Double[]>();
		for (Node<Boolean> bnNode : bnBO.getNodes()) {
			inputs.add(ConversionUtil.convertToDouble(bnBO.getNodeNeighborhoodFlagsWithReflexivity(bnNode)));
		}
		if (dimensionSizes == null) {
			som = createRectangularSelfOrganizedMap(inputs, true);
		} else {
			som = createSelfOrganizedMap(inputs, dimensionSizes, true);
		}
		return som;
	}

	public SelfOrganizedMap<Double> createSelfOrganizedMapBOAdvanced(
		BooleanNetworkBO bnBO,
		Point<Integer> dimensionSizes) {

		SelfOrganizedMap<Double> som = null;

		List<Double[]> inputs = new ArrayList<Double[]>();
		for (Node<Boolean> bnNode : bnBO.getNodes()) {
			inputs.add(ConversionUtil.convertToDouble(bnBO.getNodeAdjacencyMatrixWithReflexivity(bnNode)));
		}
		if (dimensionSizes == null) {
			som = createRectangularSelfOrganizedMap(inputs, true);
		} else {
			som = createSelfOrganizedMap(inputs, dimensionSizes, true);
		}
		return som;
	}
}