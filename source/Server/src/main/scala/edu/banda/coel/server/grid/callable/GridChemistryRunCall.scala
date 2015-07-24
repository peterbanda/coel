package edu.banda.coel.server.grid.callable

import java.{lang => jl}
import com.banda.serverbase.grid.ArgumentCallable

import scala.math.BigDecimal
import scala.collection.JavaConversions._
import com.banda.chemistry.business.ChemistryRunnableFactory
import com.banda.chemistry.business.ChemistryRunSetting
import edu.banda.coel.task.chemistry.AcRunTask
import com.banda.core.domain.um.User
import com.banda.core.runnable.TimeRunnable
import com.banda.core.domain.RunTrace
import com.banda.chemistry.domain.AcSpecies
import com.banda.chemistry.domain.AcCompartment
import com.banda.core.domain.ComponentHistory
import com.banda.core.{Pair => CPair}
import ChemistryRunTraceUtil._
import com.banda.core.domain.ComponentRunTrace

/**
 * @author © Peter Banda
 * @since 2012
 */
class GridChemistryRunCall(chemistryRunnableFactory : ChemistryRunnableFactory) extends ArgumentCallable[AcRunTask, ComponentRunTrace[jl.Double, CPair[AcCompartment, AcSpecies]]] {
    
	override def call(task : AcRunTask) = {
		val setting = new ChemistryRunSetting {
			notANumberConcentrationHandling(task.getNotANumberConcentrationHandling)
			upperThresholdViolationHandling(task.getUpperThresholdViolationHandling)
			zeroThresholdViolationHandling(task.getZeroThresholdViolationHandling)
		}

		val chemistryRunnable = chemistryRunnableFactory.createInteractiveWithTrace(
				task.getCompartment, task.getSimulationConfig, task.getInteractionSeries, Some(setting))
		chemistryRunnable.runFor(task.getRunTime.doubleValue)

		convertTupleToPair(chemistryRunnable.getRunTrace)
	}
}

object ChemistryRunTraceUtil{

    def convertTupleToPair(trace : ComponentRunTrace[jl.Double, (AcCompartment, AcSpecies)]) = {
        val newComponentHistories = trace.componentHistories.map{
		    c => new ComponentHistory(new CPair(c.component._1, c.component._2), c.history) }

    	val newTrace = new ComponentRunTrace[jl.Double, CPair[AcCompartment, AcSpecies]]
		newTrace.runTime(trace.runTime)
		newTrace.timeSteps(trace.timeSteps)
		newTrace.componentHistories(newComponentHistories)
		newTrace
    }
}