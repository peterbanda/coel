package edu.banda.coel.business.evo.fitness

import com.banda.math.business.dynamics.JavaNonlinearityErrorProcessor
import java.{lang => jl, util => ju}
import scala.collection.JavaConversions._
import edu.banda.coel.domain.evo.EvoAcSpecTask
import com.banda.chemistry.business.ChemistryRunnableFactory
import com.banda.chemistry.business.factory.AcCompartmentFactory

class EvoAcNonlinearityErrorsFitnessEvaluatorBO(
	chemistryRunnableFactory : ChemistryRunnableFactory,
	evoAcSpecTask : EvoAcSpecTask,
	compartmentFactory : AcCompartmentFactory,
	upperBound : jl.Double
	) extends EvoAcSpecFitnessEvaluatorBO[jl.Iterable[jl.Double]](
	       chemistryRunnableFactory, evoAcSpecTask, compartmentFactory,
	       new JavaNonlinearityErrorProcessor(upperBound),
	       evoAcSpecTask.getMultiRunAnalysisSpec.getRunNum.doubleValue) {

	override protected def resultToScore(results : jl.Iterable[jl.Double]) = {
		var sum = 0d
		var speciesNum : Int = 0
		for (r <- results) {
			sum += r
			speciesNum += 1
		}
		sum / speciesNum
	}
}