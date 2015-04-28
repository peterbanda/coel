package edu.banda.coel.server.grid.callable

import com.banda.serverbase.grid.ArgumentCallable

import scala.math.BigDecimal
import java.{lang => jl, util => ju}
import scala.collection.JavaConversions._
import com.banda.network.business.NetworkRunnableFactory
import com.banda.network.business.NetworkEvaluator
import com.banda.network.business.NetworkRunnableFactory
import edu.banda.coel.task.network.NetworkRunTask
import com.banda.network.business.MetaNetworkRunnableFactory
import com.banda.core.runnable.ComponentStateCollector
import com.banda.network.domain.TopologicalNode
import com.banda.core.domain.ComponentRunTrace
import edu.banda.coel.CoelRuntimeException
import com.banda.core.DoubleConvertible
import com.banda.core.DoubleConvertible.JavaBooleanAsDoubleConvertible
import com.banda.function.evaluator.FunctionEvaluatorFactory
import com.banda.network.business.NetworkDamageSpreadingAnalyser
import com.banda.math.domain.rand.DiscreteDistribution
import com.banda.math.business.rand.RandomDistributionProviderFactory

/**
 * @author © Peter Banda
 * @since 2014
 */
private class GridNetworkBooleanDamageSpreadingAnalysisCall[T](
    networkRunnableFactory : NetworkRunnableFactory[jl.Boolean]) extends ArgumentCallable[NetworkRunTask[jl.Boolean], Iterable[(Double,Double)]] {

	val javaBooleanClazz = classOf[jl.Boolean]

	override def call(task : NetworkRunTask[jl.Boolean]) = {
	    def randomDistribution = new DiscreteDistribution[jl.Boolean](Array(0.5, 0.5), Array(true, false))
	    def randomDistributionProvider = RandomDistributionProviderFactory.apply(randomDistribution)

	    val analyser = NetworkDamageSpreadingAnalyser[jl.Boolean](
	    	networkRunnableFactory,
	    	randomDistributionProvider,
	    	{(a: Seq[jl.Boolean], b : Seq[jl.Boolean]) => (a,b).zipped.map((x,y) => if (x == y) 0 else 1).sum},
	    	{a : jl.Boolean => !a})

	    analyser.run(
	    	task.getNetwork,
	    	task.getSimulationConfig,
	    	task.getRunTime.toDouble,
	    	1d,
	    	task.getRepetitions)
	}
}