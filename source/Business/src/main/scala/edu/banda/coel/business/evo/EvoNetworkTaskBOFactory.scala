package edu.banda.coel.business.evo

import com.banda.math.domain.evo.EvoTask
import java.{lang=>jl}
import edu.banda.coel.domain.evo.EvoNetworkTask
import com.banda.math.domain.evo.EvoGaSetting
import com.banda.math.business.evo.EvoTaskBO
import com.banda.network.business.MetaNetworkRunnableFactory
import com.banda.network.business.NetworkEvaluator
import com.banda.math.domain.evo.ArrayChromosome
import com.banda.network.domain.NetworkActionSeries
import org.springframework.beans.factory.annotation.Autowired
import com.banda.core.reflection.ReflectionProvider
import com.banda.math.domain.evo.Chromosome
import com.banda.math.business.evo.EvoArrayChromManipulatorBO
import com.banda.math.business.evo.EvoEnumeratedTestSampleGeneratorBO
import com.banda.function.domain.TransitionTable
import com.banda.math.business.evo.EvoChromManipulatorBO
import com.banda.math.domain.evo.EvoTaskType
import com.banda.core.reflection.GenericReflectionProvider
import com.banda.function.evaluator.FunctionEvaluatorFactory
import com.banda.core.DoubleConvertible.JavaBooleanAsDoubleConvertible
import edu.banda.coel.CoelRuntimeException
import java.io.Serializable
import edu.banda.coel.business.evo.fitness.EvoTTableNetworkFitnessEvaluator

final private class EvoNetworkTaskBOFactory[T](
	metaNetworkRunnableFactory : MetaNetworkRunnableFactory,
	funEvaluatorFactory : FunctionEvaluatorFactory
	) extends EvoTaskBOFactory[EvoNetworkTask[T]] with Serializable {

    @Autowired
	private val chromosomeRF : ReflectionProvider[_ <: Chromosome[_]] = null

	@Autowired
	private val genericReflectionProvider : GenericReflectionProvider = null

	val javaBooleanClazz = classOf[jl.Boolean]

    override def createInstance(evoTask : EvoNetworkTask[T], gaSetting : EvoGaSetting) : EvoTaskBO[_, _, _] = {
        val network = evoTask.getNetwork
        val stateClazz = network.getStateClazz
	    val networkRunnableFactory = metaNetworkRunnableFactory.createInstanceFromClass(stateClazz)

	    //transition table assumed
	    val transitionTable = network.getFunction.getFunction.asInstanceOf[TransitionTable[T, T]]

	    val chromManipulator = new EvoArrayChromManipulatorBO[T](
				chromosomeRF.asInstanceOf[ReflectionProvider[ArrayChromosome[T]]],
				transitionTable.getOutputs.size(),
				gaSetting.getBitMutationType,
				gaSetting.getPertrubMutationStrength,
				stateClazz).asInstanceOf[EvoChromManipulatorBO[ArrayChromosome[T], Array[T with Object]]]

		val testSampleGenerator = new EvoEnumeratedTestSampleGeneratorBO[NetworkActionSeries[T]](
				evoTask.getActionSeries,
				evoTask.getAsRepetitions)

		val fitnessEvaluator = EvoTTableNetworkFitnessEvaluator[T](
		        funEvaluatorFactory,
		        networkRunnableFactory,
		        genericReflectionProvider)(
				evoTask.getNetwork,
				evoTask.getSimConfig,
				evoTask.getEvaluation,
				evoTask.getRunTime,
				evoTask.getAsRepetitions.toDouble)

	    new EvoTaskBO[ArrayChromosome[T], Array[T with Object], NetworkActionSeries[T]] (
	    		chromManipulator, fitnessEvaluator, testSampleGenerator)
    }
}