package edu.banda.coel.server.grid.callable

import com.banda.serverbase.grid.ArgumentCallable

import scala.math.BigDecimal
import java.{lang => jl}
import scala.collection.JavaConversions
import com.banda.network.business.NetworkRunnableFactory
import com.banda.core.domain.um.User
import com.banda.core.runnable.TimeRunnable
import com.banda.network.business.NetworkRunnableFactory
import edu.banda.coel.task.network.NetworkRunTask
import com.banda.network.business.MetaNetworkRunnableFactory
import com.banda.core.runnable.ComponentStateCollector
import com.banda.network.domain.TopologicalNode
import com.banda.core.domain.ComponentRunTrace
import edu.banda.coel.CoelRuntimeException
import com.banda.math.business.rand.RandomDistributionProviderFactory

/**
 * @author © Peter Banda
 * @since 2014
 */
private class GridNetworkRunCall[T](
	metaNetworkRunnableFactory : MetaNetworkRunnableFactory) extends ArgumentCallable[NetworkRunTask[T], ComponentRunTrace[T, TopologicalNode]] {

	override def call(task : NetworkRunTask[T]) = {
	    val network = task.getNetwork
	    val networkRunnableFactory = metaNetworkRunnableFactory.createInstanceFromClass(network.getStateClazz)
	    val networkTraceRunnable = networkRunnableFactory.createInteractiveWithTrace(network, task.getSimulationConfig, task.getActionSeries)

	    networkTraceRunnable.runFor(task.getRunTime.toDouble)
	    networkTraceRunnable.getRunTrace
	}
}