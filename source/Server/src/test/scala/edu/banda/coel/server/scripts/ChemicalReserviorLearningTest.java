package edu.banda.coel.server.scripts;

import java.util.*;
import java.util.Map.Entry;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.banda.chemistry.business.ArtificialChemistryUtil;
import com.banda.chemistry.domain.*;
import com.banda.core.domain.MultiStateUpdateType;
import com.banda.core.dynamics.StateAlternationType;
import com.banda.core.metrics.MetricsFactory;
import com.banda.core.plotter.JavaPlotter;
import com.banda.function.domain.Expression;
import com.banda.function.domain.Function;
import com.banda.math.business.learning.IOStream;
import com.banda.math.business.learning.IOStreamFactory;
import com.banda.math.business.rand.RandomDistributionProvider;
import com.banda.math.business.rand.RandomDistributionProviderFactory;
import com.banda.math.domain.learning.ReservoirLearningSetting;
import com.banda.math.domain.rand.UniformDistribution;
import com.banda.network.business.*;
import com.banda.network.domain.*;

import edu.banda.coel.server.CoelTest;
import edu.banda.coel.server.dao.ArtificialChemistryDAO;

@Transactional
public class ChemicalReserviorLearningTest extends CoelTest {

	private static final Long AC_ID = 1025l; // 1110, 1105

	@Autowired
	private ArtificialChemistryDAO artificialChemistryDAO;

	@Autowired
	private NetworkBOFactory<Double> doubleNetworkFactory;

	@Autowired
	private MetricsFactory<Double> doubleMetricsFactory;

	@Autowired
	private IOStreamFactory ioStreamFactory;

	private ArtificialChemistryUtil acUtil = ArtificialChemistryUtil.getInstance();

	private final JavaPlotter plotter = JavaPlotter.createDisplayInstance();

//	@Test
	public void testReservoir() {
//		final AcIORunBO reservoir = createAcRunBO(AC_ID, 2, 2, StateAlternationType.Replacement, 20d);
//		final TimeRunnableStateCollectorAdapter<Double> stateCollectorAdapter = new TimeRunnableStateCollectorAdapter<Double>(reservoir, BigDecimal.valueOf(1d));
//		stateCollectorAdapter.runFor(BigDecimal.valueOf(20000d));
//
//		plotter.plotTimeSeriesTransposed(stateCollectorAdapter.getCollectedStates(), "States", true);
	}

	@Test
	public void testChemicalReservoirLMSLearning() {
		testReservoirLMSLearning(
			AC_ID,
			Expression.Double("2 * x0 + x1"),
			0.1,
			12,
			StateAlternationType.Addition,
			20d);
	}

	private ReservoirLearningSetting createReservoirLearningSetting() {
		ReservoirLearningSetting setting = new ReservoirLearningSetting();
		setting.setIterationNum(300);
		setting.setSingleIterationLength(100D);
		setting.setReadoutRunTime(1D);
		setting.setInitialLearningRate(0.001);
		setting.setInitialDelay(100D);
		return setting;
	}

	private void testReservoirLMSLearning(
		Long acId,
		Function<Double, Double> fun,
		double inputUpperBound,
		int reserviorNodesToMap,
		StateAlternationType inputInjectionType,
		Double inputApplicationTypeLength
	) {
//		final ReservoirLearningSetting reservoirSetting = createReservoirLearningSetting();
//		final IOStream<Double> trainingStream = createTrainingStream(fun, inputUpperBound);
//
//		final AcIORunBO reservoir = createAcRunBO(acId, trainingStream.inputDim(), reserviorNodesToMap, inputInjectionType, inputApplicationTypeLength);
//		final IOTimeRunnableStateCollectorAdapter<Double> reservoirStateCollectorAdapter = new IOTimeRunnableStateCollectorAdapter<Double>(reservoir, BigDecimal.valueOf(1));
//		final Network<Double> readoutNetwork = createLayeredDoubleNetworkTestData(reserviorNodesToMap, trainingStream.outputDim());
//
//		final LayeredNetworkBO<Double> readoutNetworkBO = (LayeredNetworkBO<Double>) doubleNetworkFactory.createNetworkBO(readoutNetwork);
//		final LMSNetworkLearningBO lmsReadout = new LMSNetworkLearningBO(readoutNetworkBO, doubleMetricsFactory, reservoirSetting);

//		ReservoirLearner<Double> reservoirLearning = new ReservoirLearner<Double>(reservoirStateCollectorAdapter, lmsReadout, reservoirSetting);
//		reportLayeredNetwork(readoutNetworkBO);

//		((Trainable<Double>) reservoirLearning).train(trainingStream);
//
//		plotter.plotSingleTimeSeries(reservoirLearning.errors(), "Errors", true);
//		plotter.plotTimeSeriesTransposed(reservoirStateCollectorAdapter.getCollectedStates(), "States", true);
//
//		for (Double error : reservoirLearning.errors()) {
//			System.out.println(error);
//		}

		RandomDistributionProvider<Double> rdp = RandomDistributionProviderFactory.apply(new UniformDistribution<Double>(0D, inputUpperBound));
		Collection<List<Double>> outputs = new ArrayList<List<Double>>();
		for (int i = 0; i < 100; i++) {
//			final List<Double> input = rdp.nextList(trainingStream.inputDim());
//			System.out.print(input + ",");
//			reservoirLearning.setInput(input);
//			reservoirLearning.runFor(BigDecimal.valueOf(reservoirSetting.getSingleIterationRunTime()));
//			outputs.add(reservoirLearning.getOutput());
		}
		System.out.println();

		for (List<Double> output : outputs) {
			System.out.print(output + ",");
		}
	}

//	private AcIORunBO createAcRunBO(Long acId, int inputsNum, int outputsNum, StateAlternationType inputInjectionType, Double inputApplicationTypeLength) {
//		ArtificialChemistry ac = artificialChemistryDAO.get(acId);
//
//		Collection<AcReaction> nonInfluxReactions = filterOutInfluces(ac.getReactionSet());
//		Map<Integer, Collection<AcSpecies>> rankSpeciesMap = rankSpecies(nonInfluxReactions, ac.getSpecies(), AcSpeciesAssociationType.Product);
//		List<Integer> ranks = new ArrayList<Integer>(rankSpeciesMap.keySet());
//		Collections.sort(ranks);
//		Collection<AcSpecies> inputSpecies = new ArrayList<AcSpecies>();
//		for (Integer rank : ranks) {
//			Collection<AcSpecies> rankedSpecies = rankSpeciesMap.get(rank);
//			final int selectNum = Math.min(inputsNum - inputSpecies.size(), rankedSpecies.size());
//			inputSpecies.addAll(RandomUtil.nextElementsWithoutRepetitions(rankedSpecies, selectNum));
//			if (inputSpecies.size() == inputsNum) {
//				break;
//			}
//		}
////		Set<AcSpecies> nonProductSpecies = acUtil.filterOutSpecies(nonInfluxReactions, ac.getSpecies(), AcSpeciesAssociationType.Product);
////		final Collection<AcSpecies> inputSpecies = RandomUtil.nextElementsWithoutRepetitions(nonProductSpecies, inputsNum);
//
//		System.out.println(inputSpecies);
//
//		final Collection<AcSpecies> outputSpecies = RandomUtil.nextElementsWithoutRepetitions(ac.getSpecies(), outputsNum);
//		for (AcSpecies oneInputSpecies : inputSpecies) {
//			ac.getSpeciesSet().addSpeciesToGroup(AcSpeciesType.Input, oneInputSpecies);
//		}
//		for (AcSpecies oneOutputSpecies : outputSpecies) {
//			ac.getSpeciesSet().addSpeciesToGroup(AcSpeciesType.Output, oneOutputSpecies);
//		}
//
//		ChemistryRunSetting setting = new ChemistryRunSetting();
//		AcIORunBO acRunBO = acRunBOFactory.createIOFlatInstance(ac.getSkinCompartment(), ac.getSimulationConfig(), setting, AcRunStoreOption.None, inputInjectionType, inputApplicationTypeLength);
//		RandomDistributionProvider<Double> rdp = RandomDistributionProviderFactory.createInstance(new UniformDistribution<Double>(0D, 1D));
//		acRunBO.setStates(rdp.nextList(acRunBO.getStates().size()));
//		return acRunBO;
//	}

	private Collection<AcReaction> filterOutInfluces(AcReactionSet reactionSet) {
		Collection<AcReaction> filteredReactions = new ArrayList<AcReaction>(reactionSet.getReactions());
		for (AcReaction reaction : reactionSet.getReactions()) {
			if (reaction.getSpeciesAssociations().size() == 1 && reaction.getSpeciesAssociationsNum(AcSpeciesAssociationType.Product) == 1) {
				filteredReactions.remove(reaction);
			}
		}
		return filteredReactions;
	}

	private Map<Integer, Collection<AcSpecies>> rankSpecies(
		Collection<AcReaction> reactions,
		Collection<AcSpecies> species,
		AcSpeciesAssociationType type
	) {
		Map<AcSpecies, Integer> speciesRankMap = new HashMap<AcSpecies, Integer>();
		for (AcSpecies oneSpecies : species) {
			speciesRankMap.put(oneSpecies, 0);
		}
		for (AcReaction reaction : reactions) {
			Collection<AcSpecies> reactionSpecies = acUtil.getSpecies(reaction.getSpeciesAssociations(type));
			for (AcSpecies oneSpecies : reactionSpecies) {
				speciesRankMap.put(oneSpecies, speciesRankMap.get(oneSpecies) + 1);
			}
		}
		Map<Integer, Collection<AcSpecies>> rankSpeciesMap = new HashMap<Integer, Collection<AcSpecies>>();
		for (Entry<AcSpecies, Integer> speciesRankPair : speciesRankMap.entrySet()) {
			Collection<AcSpecies> rankedSpecies = rankSpeciesMap.get(speciesRankPair.getValue());
			if (rankedSpecies == null) {
				rankedSpecies = new ArrayList<AcSpecies>();
				rankSpeciesMap.put(speciesRankPair.getValue(), rankedSpecies);
			}
			rankedSpecies.add(speciesRankPair.getKey());
		}
		return rankSpeciesMap;
	}

	private IOStream<Double> createTrainingStream(
		Function<Double, Double> fun,
		double inputUpperBound
	) {
		int order = fun.getReferencedVariables().size();
		return ioStreamFactory.createInstance1DFun(fun).apply(0d, order, order).apply(
				new UniformDistribution<Double>(0D, inputUpperBound));
	}

	private Network<Double> createLayeredDoubleNetworkTestData(int inputsNum, int outputsNum) {
		TemplateNetworkWeightSetting<Double> networkWeightSetting = new TemplateNetworkWeightSetting<Double>();
		networkWeightSetting.setRandomDistribution(new UniformDistribution<Double>(-2.0, 2.0));

		Network<Double> network = new Network<Double>();
		network.setDefaultBiasState(1D);
		network.setTopology(createLayeredTopologyTestData(inputsNum, outputsNum));
		network.setFunction(createDoubleNetworkFunctionTestData());
		network.setWeightSetting(networkWeightSetting);

		return network;
	}
	
	private NetworkFunction<Double> createDoubleNetworkFunctionTestData() {
		NetworkFunction<Double> layer1Function = new NetworkFunction<Double>();
		layer1Function.setMultiComponentUpdaterType(MultiStateUpdateType.Sync);
		layer1Function.setStatesWeightsIntegratorType(StatesWeightsIntegratorType.LinearSum);

		NetworkFunction<Double> layer2Function = new NetworkFunction<Double>();
		layer2Function.setMultiComponentUpdaterType(MultiStateUpdateType.Sync);
		layer2Function.setStatesWeightsIntegratorType(StatesWeightsIntegratorType.LinearSum);

		NetworkFunction<Double> topLevelFunction = new NetworkFunction<Double>();
		topLevelFunction.setMultiComponentUpdaterType(MultiStateUpdateType.AsyncFixedOrder);
		topLevelFunction.addLayerFunction(layer1Function);
		topLevelFunction.addLayerFunction(layer2Function);

		return topLevelFunction;
	}
	
	private TemplateTopology createLayeredTopologyTestData(int inputsNum, int outputsNum) {
		// layer 1
		TemplateTopology layer1 = new TemplateTopology();
		layer1.setIndex(1);
		layer1.setNodesNum(inputsNum);

		// layer 2
		TemplateTopology layer2 = new TemplateTopology();
		layer2.setIndex(2);
		layer2.setNodesNum(outputsNum);
		layer2.setGenerateBias(true);

		// top-level topology
		TemplateTopology topLevelTopology = new TemplateTopology();
		topLevelTopology.setIntraLayerAllEdges(true);
		topLevelTopology.addLayer(layer1);
		topLevelTopology.addLayer(layer2);

		return topLevelTopology;
	}

	private void reportLayeredNetwork(LayeredNetworkBO<?> layeredNetworkBO) {
		int index = 0;
		for (NetworkBO<?> layer : layeredNetworkBO.getLayers()) {
			System.out.println("Layer :" + index);
			System.out.println("--------------");
			for (NodeBO<?> node : layer.getNodes()) {
				if (node.isImmutable()) {
					System.out.println("Node (immutable):" + node.getIndex());
				} else {
					System.out.println("Node:" + node.getIndex());
				}
				for (NodeBOEdge<?> edge : node.getInEdges()) {
					System.out.println("Edge from :" + edge.getStart().getTopologicalNode().getTopology().getIndex() + "," + edge.getStart().getIndex());					
				}
			}
			System.out.println("--------------");
			System.out.println();
			index++;
		}
	}
}