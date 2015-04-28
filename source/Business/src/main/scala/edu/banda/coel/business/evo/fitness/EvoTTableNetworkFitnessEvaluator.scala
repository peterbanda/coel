package edu.banda.coel.business.evo.fitness

import scala.collection.JavaConversions._
import java.{lang => jl, util => ju}
import com.banda.math.domain.evo.Chromosome
import edu.banda.coel.domain.evo.EvoAcTask
import com.banda.network.domain.NetworkSimulationConfig
import com.banda.network.business.NetworkRunnableFactory
import com.banda.network.business.NetworkEvaluator
import com.banda.core.DoubleConvertible.Implicits.addAsDouble
import com.banda.network.domain.Network
import com.banda.math.domain.evo.ArrayChromosome
import com.banda.function.domain.TransitionTable
import com.banda.core.CollectionElementsConversions._
import com.banda.network.domain.NetworkActionSeries
import scala.collection.mutable.Subscriber
import com.banda.core.runnable.StateEvent
import scala.collection.mutable.Publisher
import com.banda.core.runnable.StateUpdatedEvent
import com.banda.network.domain.TopologicalNode
import com.banda.network.domain.NetworkEvaluation
import com.banda.network.domain.NetworkFunction
import com.banda.core.reflection.GenericReflectionProvider
import java.util.Date
import com.banda.core.DoubleConvertible.JavaBooleanAsDoubleConvertible
import edu.banda.coel.CoelRuntimeException
import com.banda.function.evaluator.FunctionEvaluatorFactory
import com.banda.math.business.evo.EvoFitnessEvaluator
import com.banda.math.business.evo.EvoFitnessEvaluatorAdapter

final private class EvoTTableNetworkFitnessEvaluator[T](
    functionEvaluatorFactory : FunctionEvaluatorFactory,
    networkRunnableFactory : NetworkRunnableFactory[T],
	genericReflectionProvider : GenericReflectionProvider)(
	network : Network[T], 
	config : NetworkSimulationConfig,
	evaluation : NetworkEvaluation, 
	runTime : Int, 
	maxScore : Double
	) extends EvoFitnessEvaluatorAdapter[ArrayChromosome[T], NetworkActionSeries[T]] {

	private[fitness] def getNetwork = network

	val javaBooleanClazz = classOf[jl.Boolean]

	override def evaluateScore(chromosome : ArrayChromosome[T], actionSeries : NetworkActionSeries[T]) = {
	    // transition table expected
	    val transitionTable = network.getFunction.getFunction.asInstanceOf[TransitionTable[T, T]]
	    val chromosomeCode = arrayToJavaList(chromosome.getCode).asInstanceOf[ju.List[T]]

	    // clone network for thread safety
	    val startTime = new Date
	    val newNetwork = genericReflectionProvider.clone(network)
	    val newNetworkFunction = genericReflectionProvider.clone(network.getFunction)
	    val newTrasitionTable = genericReflectionProvider.clone(transitionTable)
	    newTrasitionTable.setOutputs(chromosomeCode)
	    newNetworkFunction.setFunction(newTrasitionTable)
	    newNetwork.setFunction(newNetworkFunction)
//	    println(new Date().getTime() - startTime.getTime)

	    // set transition table outputs
	    val networkRunnable = networkRunnableFactory.createInteractive(newNetwork, config, actionSeries)

	    // TODO: not sure if we have to do this
	    val newEvaluation = genericReflectionProvider.clone(evaluation)
	    newEvaluation.setEvaluationItems(new ju.HashSet(evaluation.getEvaluationItems))
	    // TODO: Boolean expected
		val evaluator = (newNetwork.getStateClazz) match {
		    case `javaBooleanClazz` => {
		        val jbdc = JavaBooleanAsDoubleConvertible
		        NetworkEvaluator[jl.Boolean](
		                functionEvaluatorFactory,
		                newEvaluation,
		                runTime).asInstanceOf[NetworkEvaluator[T]]
		    }
		    case _ => throw new CoelRuntimeException("Network evaluator value type " + newNetwork.getStateClazz + " not recognized.");
		}

	    networkRunnable.subscribe(evaluator)
	    networkRunnable.runFor(runTime)

	    evaluator.result
	}

	override def calcFitness(score : jl.Double) = score / maxScore
}

object EvoTTableNetworkFitnessEvaluator {

    def apply[T](
        functionEvaluatorFactory : FunctionEvaluatorFactory,
        networkRunnableFactory : NetworkRunnableFactory[T],
        genericReflectionProvider : GenericReflectionProvider)(
    	network : Network[T], 
    	config : NetworkSimulationConfig,
    	evaluation : NetworkEvaluation, 
    	runTime : Int,
    	maxScore : Double
    ) : EvoFitnessEvaluator[ArrayChromosome[T], NetworkActionSeries[T]] =
        new EvoTTableNetworkFitnessEvaluator(functionEvaluatorFactory, networkRunnableFactory, genericReflectionProvider)(network, config, evaluation, runTime, maxScore)
}