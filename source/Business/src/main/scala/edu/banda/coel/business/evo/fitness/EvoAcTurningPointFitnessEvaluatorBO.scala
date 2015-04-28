package edu.banda.coel.business.evo.fitness

import java.{lang => jl}
import scala.collection.JavaConversions._
import com.banda.math.business.dynamics.JavaTurningPointCountProcessor
import edu.banda.coel.domain.evo.EvoAcSpecTask
import com.banda.chemistry.business.ChemistryRunnableFactory
import com.banda.chemistry.business.factory.AcCompartmentFactory

class EvoAcTurningPointFitnessEvaluatorBO(
	chemistryRunnableFactory : ChemistryRunnableFactory,
	evoAcSpecTask : EvoAcSpecTask,
	compartmentFactory : AcCompartmentFactory,
	detectionPrecision : jl.Double,
	extremeDiff : jl.Double,
	upperBound : jl.Double) extends EvoAcSpecFitnessEvaluatorBO[jl.Iterable[jl.Integer]](
	       chemistryRunnableFactory, evoAcSpecTask, compartmentFactory,
	       new JavaTurningPointCountProcessor(detectionPrecision, extremeDiff, upperBound),
	       evoAcSpecTask.getSingleRunAnalysisSpec.getIterations.doubleValue * evoAcSpecTask.getMultiRunAnalysisSpec.getRunNum) {

	override protected def resultToScore(results : jl.Iterable[jl.Integer]) = {
		var sum = 0d
		var speciesNum : Int = 0
		for (r <- results) {
			sum += r
			speciesNum += 1
		}
		sum / speciesNum
	}
}