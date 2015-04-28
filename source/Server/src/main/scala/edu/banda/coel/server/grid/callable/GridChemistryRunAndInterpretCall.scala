package edu.banda.coel.server.grid.callable

import java.{lang => jl}
import com.banda.serverbase.grid.ArgumentCallable

import scala.collection.JavaConversions._
import com.banda.chemistry.business._
import com.banda.chemistry.domain.AcTranslatedRun
import edu.banda.coel.task.chemistry.AcRunAndTranslateTask
import edu.banda.coel.task.chemistry.AcRunTask
import com.banda.core.domain.um.User
import com.banda.core.runnable.TimeRunnable
import com.banda.core.runnable.RunTraceHolder
import com.banda.chemistry.domain.AcCompartment
import com.banda.chemistry.domain.AcSpecies
import com.banda.core.domain.RunTrace
import ChemistryRunTraceUtil._

/**
 * @author © Peter Banda
 * @since 2012
 */
class GridChemistryRunAndInterpretCall(
	chemistryRunnableFactory : ChemistryRunnableFactory,
	chemistryInterpretationFactory : ChemistryInterpretationFactory) extends ArgumentCallable[AcRunAndTranslateTask, AcTranslatedRun] {
 
	override def call(task : AcRunAndTranslateTask) = {
		val runTask = task.getRunTaskDefinition
		val setting = new ChemistryRunSetting {
			notANumberConcentrationHandling(runTask.getNotANumberConcentrationHandling)
			upperThresholdViolationHandling(runTask.getUpperThresholdViolationHandling)
			zeroThresholdViolationHandling(runTask.getZeroThresholdViolationHandling)
		}

		val skinCompartment = runTask.getCompartment
		var runTraceHolder : Option[RunTraceHolder[jl.Double, (AcCompartment, AcSpecies)]] = None

		val chemistryRunnableWithPublishers = if (task.isStoreRunTrace) { 
		    val chemistryRunnableTraceHolderWithPublishers = chemistryRunnableFactory.createInteractiveWithTraceAndPublishers(
				skinCompartment, runTask.getSimulationConfig, runTask.getActionSeries, Some(setting))
			runTraceHolder = Some(chemistryRunnableTraceHolderWithPublishers._1)
			chemistryRunnableTraceHolderWithPublishers
		} else 
		    chemistryRunnableFactory.createInteractiveWithPublishers(
				skinCompartment, runTask.getSimulationConfig, runTask.getActionSeries, Some(setting))

		val chemistryRunnable = chemistryRunnableWithPublishers._1
		val skinCompartmentPublisher = chemistryRunnableWithPublishers._2.find(_._1 == skinCompartment).get._2

		val chemistryInterpreter = chemistryInterpretationFactory.apply(chemistryRunnable, skinCompartmentPublisher, task.getTranslationSeries)
		chemistryInterpreter.runFor(runTask.getRunTime.doubleValue)

		val user = new User
		user.setId(1l)
		val translatedRun = chemistryInterpreter.getAcTranslatedRun
		translatedRun.setCreatedBy(user)
		if (runTraceHolder.isDefined) {
		    val runTrace = runTraceHolder.get.getRunTrace
		    translatedRun.setAcRunTrace(convertTupleToPair(runTrace))
		}
		translatedRun		    
	}
}