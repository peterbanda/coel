package edu.banda.coel.business.evo.fitness

import scala.collection.JavaConversions._
import java.{lang => jl}
import com.banda.chemistry.business.AcEvaluationBOFactory
import com.banda.chemistry.business.ChemistryInterpretationFactory
import com.banda.chemistry.domain.AcInteractionSeries
import com.banda.chemistry.domain.AcEvaluation
import com.banda.chemistry.domain.AcTranslationSeries
import com.banda.chemistry.domain.ArtificialChemistry
import edu.banda.coel.task.chemistry.AcRunAndTranslateTask
import edu.banda.coel.task.chemistry.AcRunTask
import com.banda.math.domain.evo.Chromosome
import edu.banda.coel.domain.evo.EvoAcTask
import com.banda.chemistry.business.ChemistryRunSetting
import com.banda.chemistry.business.ChemistryRunnableFactory
import com.banda.chemistry.domain.AcEvaluationItemState
import com.banda.core.DoubleConvertible.Implicits.addAsDouble
import java.io.Serializable
import com.banda.math.business.evo.EvoFitnessEvaluator
import com.banda.math.business.evo.EvoFitnessEvaluatorAdapter
import com.banda.chemistry.domain.AcCompartment
import com.banda.chemistry.domain.AcSimulationConfig

abstract class EvoAcFitnessEvaluatorBO[H <: Chromosome[_]](
	compartment : AcCompartment,
    simulationConfig : AcSimulationConfig,
	taskEvaluation : AcEvaluation,
	runSteps : Integer,
	lastEvaluationStepsToCount : Integer,
	maxScore : Integer,
	chemistryRunnableFactory : ChemistryRunnableFactory,
	chemistryInterpretationFactory : ChemistryInterpretationFactory,
	acEvaluationBOFactory : AcEvaluationBOFactory
	) extends EvoFitnessEvaluatorAdapter[H, AcInteractionSeries] {

	// TODO: what if lastEvaluationStepsToCount is null??
	def this(
		evoAcTask : EvoAcTask,
		chemistryRunnableFactory : ChemistryRunnableFactory,
		chemistryInterpretationFactory : ChemistryInterpretationFactory,
		acEvaluationBOFactory : AcEvaluationBOFactory
	) = this(evoAcTask.getCompartment(),
            evoAcTask.getSimulationConfig(),
			evoAcTask.getAcEvaluation(),
			evoAcTask.getRunSteps(),
			evoAcTask.getLastEvaluationStepsToCount(),
			evoAcTask.getLastEvaluationStepsToCount() * evoAcTask.getAsRepetitions() * evoAcTask.getActionSeries().size(),
			chemistryRunnableFactory,
			chemistryInterpretationFactory,
			acEvaluationBOFactory)

	private[fitness] def getCompartment() = compartment

    private[fitness] def getSimulationConfig() = simulationConfig

	private[fitness] def getTaskEvaluation() = taskEvaluation

	private[fitness] def createTask() : AcRunAndTranslateTask = 
	    new AcRunAndTranslateTask {
			setTranslationSeries(taskEvaluation.getTranslationSeries)
			setRunTaskDefinition(new AcRunTask {
				setCompartment(compartment)
                setSimulationConfig(simulationConfig)
				setRunTime(runSteps)
				setRepetitions(1)
			})
		}

	private[fitness] def adaptTask(task : AcRunAndTranslateTask, chromosome : H)

	override def evaluateScore(chromosome : H, actionSeries : AcInteractionSeries) = {
		// Prepare task definition
		val task = createTask
		val runTask = task.getRunTaskDefinition
		runTask.setActionSeries(actionSeries)
		adaptTask(task, chromosome)

		val setting = new ChemistryRunSetting {
			notANumberConcentrationHandling(runTask.getNotANumberConcentrationHandling)
			upperThresholdViolationHandling(runTask.getUpperThresholdViolationHandling)
			zeroThresholdViolationHandling(runTask.getZeroThresholdViolationHandling)
		}
		val skinCompartment = runTask.getCompartment

		val chemistryRunnableWithPublishers = chemistryRunnableFactory.createInteractiveWithPublishers(
				skinCompartment, runTask.getSimulationConfig, runTask.getActionSeries, Some(setting))
		val chemistryRunnable = chemistryRunnableWithPublishers._1

		val skinCompartmentPublisher = chemistryRunnableWithPublishers._2.find(_._1 == skinCompartment).get._2

		val chemistryInterpreter = chemistryInterpretationFactory.apply(chemistryRunnable, skinCompartmentPublisher, task.getTranslationSeries)

		// Translation
		chemistryInterpreter.runFor(runTask.getRunTime.doubleValue)
		val translatedRun = chemistryInterpreter.getAcTranslatedRun

		// Evaluation
		val acTaskEvaluationBO = acEvaluationBOFactory.createInstance(taskEvaluation, translatedRun)

		// Execute task
		acTaskEvaluationBO.evaluateFull

		// Collection results
		val evaluatedValues = acTaskEvaluationBO.getEvaluatedRun.getEvaluatedValues
		val sum = 0
		if (lastEvaluationStepsToCount != null) 
		    evaluatedValues.drop(evaluatedValues.size - lastEvaluationStepsToCount).foldLeft(0 : jl.Double)(addAsDouble)
		else
		    evaluatedValues.foldLeft(0 : jl.Double)(addAsDouble)
	}

	override def calcFitness(score : jl.Double) = score / maxScore
}