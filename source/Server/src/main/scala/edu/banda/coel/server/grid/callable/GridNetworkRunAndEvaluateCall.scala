package edu.banda.coel.server.grid.callable

import com.banda.serverbase.grid.ArgumentCallable

import scala.math.BigDecimal

import java.{lang => jl, util => ju}
import scala.collection.JavaConversions._
import com.banda.network.business.NetworkRunnableFactory
import com.banda.network.business.NetworkEvaluator
import com.banda.network.business.NetworkRunnableFactory
import edu.banda.coel.task.network.NetworkRunAndEvaluateTask
import com.banda.network.business.MetaNetworkRunnableFactory
import com.banda.core.runnable.ComponentStateCollector
import com.banda.network.domain.TopologicalNode
import com.banda.core.domain.ComponentRunTrace
import edu.banda.coel.CoelRuntimeException
import com.banda.core.DoubleConvertible
import com.banda.core.DoubleConvertible.JavaBooleanAsDoubleConvertible
import com.banda.function.evaluator.FunctionEvaluatorFactory

/**
 * @author © Peter Banda
 * @since 2014
 */
private class GridNetworkRunAndEvaluateCall[T](
    networkRunnableFactory : NetworkRunnableFactory[T],
	functionEvaluatorFactory : FunctionEvaluatorFactory) extends ArgumentCallable[NetworkRunAndEvaluateTask[T], Iterable[Double]] {

	val javaBooleanClazz = classOf[jl.Boolean]

	override def call(task : NetworkRunAndEvaluateTask[T]) = {
	    val networkRunnable = networkRunnableFactory.createInteractive(task.getNetwork, task.getSimulationConfig, task.getActionSeries)


	    val evaluators = task.getNetworkEvaluations.map{ evaluation =>
	    	// TODO: Boolean expected
	    	val evaluator = (task.getNetwork.getStateClazz) match {
		    	case `javaBooleanClazz` => {
		    		val jbdc = JavaBooleanAsDoubleConvertible
		    			NetworkEvaluator[jl.Boolean](
		    				functionEvaluatorFactory,
		    				evaluation,
		    				task.getRunTask.getRunTime).asInstanceOf[NetworkEvaluator[T]]
		    	}
		    	case _ => throw new CoelRuntimeException("Network evaluator value type " + task.getNetwork.getStateClazz + " not recognized.");
	    	}
	    	networkRunnable.subscribe(evaluator)
	    	evaluator
	    }

	    networkRunnable.runFor(task.getRunTask.getRunTime.toDouble)

	    evaluators.map(_.result)
	}
}

trait GridNetworkRunAndEvaluateCallFactory {

    def apply[T](stateClazz : Class[T]) : ArgumentCallable[NetworkRunAndEvaluateTask[T], Iterable[Double]]
}

private class GridNetworkRunAndEvaluateCallFactoryImpl(
	metaNetworkRunnableFactory : MetaNetworkRunnableFactory,
    functionEvaluatorFactory : FunctionEvaluatorFactory) extends GridNetworkRunAndEvaluateCallFactory {

    def apply[T](stateClazz : Class[T]) : ArgumentCallable[NetworkRunAndEvaluateTask[T], Iterable[Double]] =
        new GridNetworkRunAndEvaluateCall(metaNetworkRunnableFactory.createInstanceFromClass(stateClazz), functionEvaluatorFactory)
}