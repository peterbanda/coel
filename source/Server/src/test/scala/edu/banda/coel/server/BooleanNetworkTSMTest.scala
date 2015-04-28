package edu.banda.coel.server

import java.{util=>ju,lang=>jl}
import com.banda.function.{domain => fd}
import edu.banda.coel.business.netpic.{JavaNetworkRunSpatialPicGenerator, NetworkRunSpatialPicGenerator}
import scala.collection.JavaConversions._
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import com.banda.core.CollectionElementsConversions._
import com.banda.core.util.ConversionUtil
import com.banda.core.util.RandomUtil
import com.banda.function.business.FunctionFactory
import com.banda.function.domain.TransitionTable
import com.banda.network.domain.Network
import com.banda.network.domain.NetworkFunction
import com.banda.network.domain.SpatialTopology
import junit.framework.TestCase._
import com.banda.core.metrics.MetricsType
import com.banda.function.BndFunctionException
import com.banda.function.evaluator.FunctionEvaluatorFactory
import com.banda.function.domain.AbstractFunction
import com.banda.core.runnable.TimeStateManager
import com.banda.core.domain.MultiStateUpdateType
import com.banda.core.runnable.StateCollector
import org.junit.BeforeClass
import com.banda.network.domain.Topology
import scala.util.Random
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import java.util.Collections
import com.banda.network.domain.TopologicalNodeLocationComparator
import com.banda.core.runnable.ComponentStateCollector
import com.banda.network.domain.TopologicalNode
import com.banda.network.business.integrator.StatesWeightsIntegratorDef.StatesWeightsIntegrator
import com.banda.core.runnable.FixedPointDetector
import com.banda.core.runnable.StrictFixedPointDetector
import com.banda.core.runnable.SeqIndexAccessible
import com.banda.core.runnable.SeqIndexAccessible._
import com.banda.core.runnable.ComposedStateProducer
import com.banda.core.runnable.StateProducer
import com.banda.core.runnable.TimeRunnable
import scala.collection.mutable.Publisher
import com.banda.core.runnable.StateEvent
import com.banda.core.runnable.StateAccessible
import com.banda.core.runnable.FullStateAccessible
import com.banda.network.business.NetworkRunnableFactory
import com.banda.network.domain.StatesWeightsIntegratorType
import com.banda.network.domain.NetworkSimulationConfig
import com.banda.core.plotter.Plotter
import com.banda.core.plotter.TimeSeriesPlotSetting
import edu.banda.coel.core.client.gui.BNFrame
import edu.banda.coel.core.svg.SVGUtil
import java.awt.Color
import java.awt.BasicStroke
import BooleanNetworkTSMTest._

/**
 * @author © Peter Banda
 * @since 2013  
 */
object BooleanNetworkTSMTest {

    val repetitions = 2
    val runTime = 200
    val nodesNum = 100
    val fixedPointPeriodicity = 20
    val svgUtil = new SVGUtil

    var topologies : Iterable[Topology] = _
    var functions : Iterable[fd.AbstractFunction[jl.Boolean, jl.Boolean]] = _
    var initialStates: Iterable[Seq[jl.Boolean]] = _

    val funFactory = new FunctionFactory
    val config = new NetworkSimulationConfig

    @BeforeClass
    def initialize {
        topologies = for (_ <- 1 to repetitions) yield createSpatialTopologyTestData(nodesNum)

        functions = for (topology <- topologies) yield funFactory.createRandomBoolTransitionTable(3)

        initialStates = for (topology <- topologies) yield createInitialStates(nodesNum)
    }

	def createSpatialTopologyTestData(nodesNum : Int) = new SpatialTopology {
			setMetricsType(MetricsType.Manhattan)
			setTorusFlag(true)
			setItsOwnNeighor(true)
			addSize(nodesNum)
			setRadius(1)
		}

	def createInitialStates(nodeNum : Int) : ju.List[jl.Boolean] = {
		var configuration = new ju.ArrayList[jl.Boolean]
		val booleans : Array[Boolean] = Array(true, false)
		for (i <- 1 to nodeNum) {
			configuration.add(RandomUtil.nextBoolean)
		}
		configuration
	}
}

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class BooleanNetworkTSMTest extends CoelTest {

    val plotter = Plotter.createDisplayInstance
    
	@Autowired
	val booleanNetworkRunnableFactory : NetworkRunnableFactory[jl.Boolean] = null

	@Test
	def test1() = {
        
    }

	@Test
	def test3BooleanNetworkTSM = {
	    val multiRuns = (topologies, functions, initialStates).zipped.map(getBooleanNetworkTSMComponentTimeStates(MultiStateUpdateType.Sync, config, None))

	    val zoom = 10
	    var counter = 0
	    multiRuns.foreach{ run =>
	       	val picGenerator = JavaNetworkRunSpatialPicGenerator.createSpatialJavaBoolean1D(run._1.size, zoom)
	       	val canvases = picGenerator.generateSVGImages(run._1, run._2)
	       	canvases.zipWithIndex.foreach{ case (canvas, index) => svgUtil.exportToFile(canvas, "boolean-network" + counter + "_" + index + ".svg", true)}
	       	counter += 1
	    }
	}

	def runBooleanNetworkTSM(
	    multiStateUpdaterType : MultiStateUpdateType,
	    config : NetworkSimulationConfig,
		statesWeightsIntegratorType : Option[StatesWeightsIntegratorType] = None)(
	    topology : Topology,
	    function : fd.AbstractFunction[jl.Boolean, jl.Boolean],
	    initialStates : Seq[jl.Boolean]
	) {
	    val networkRunnable = createBooleanNetwork(multiStateUpdaterType, config, statesWeightsIntegratorType)(topology, function)
	    networkRunnable.setStates(initialStates)
        networkRunnable.runFor(runTime)
	}

	def getBooleanNetworkTSMComponentTimeStates(
	    multiStateUpdaterType : MultiStateUpdateType,
	    config : NetworkSimulationConfig,
		statesWeightsIntegratorType : Option[StatesWeightsIntegratorType] = None)(
	    topology : Topology,
	    function : fd.AbstractFunction[jl.Boolean, jl.Boolean],
	    initialStates : Seq[jl.Boolean]
	) = {
	    val networkRunnable = createBooleanNetwork(multiStateUpdaterType, config, statesWeightsIntegratorType)(topology, function)
	    networkRunnable.setStates(initialStates)
	    val collector = new ComponentStateCollector[jl.Boolean, TopologicalNode, ju.List]
	    networkRunnable.subscribe(collector)
        networkRunnable.runFor(runTime)

        val orderedComponents = new ju.ArrayList[TopologicalNode](collector.components)
        Collections.sort(orderedComponents, new TopologicalNodeLocationComparator)
        val orderedComponentIndexMap = orderedComponents.zipWithIndex.toMap 
        val timeStates = collector.collected.map{ case (time, states) => {
        	val sortedComponentStates = (collector.components zip states).toList.sortBy{ case (node, _) => orderedComponentIndexMap.get(node).get}
        	(time, sortedComponentStates.map(_._2))
        }}
	    (orderedComponents, timeStates)
	}

	def createBooleanNetwork(
	    multiStateUpdaterType : MultiStateUpdateType,
	    config : NetworkSimulationConfig,
		statesWeightsIntegratorType : Option[StatesWeightsIntegratorType] = None)(
	    topology : Topology,
	    function : fd.AbstractFunction[jl.Boolean, jl.Boolean]
	) = {
	    val networkFunction = new NetworkFunction[jl.Boolean]
	    networkFunction.setMultiComponentUpdaterType(multiStateUpdaterType)
	    if (statesWeightsIntegratorType.isDefined)
	    	networkFunction.setStatesWeightsIntegratorType(statesWeightsIntegratorType.get)
	    networkFunction.setFunction(function)

	    val network = new Network[jl.Boolean]
	    network.setTopology(topology)
	    network.setFunction(networkFunction)

	    booleanNetworkRunnableFactory.createNonInteractive(network, config)
	}
}