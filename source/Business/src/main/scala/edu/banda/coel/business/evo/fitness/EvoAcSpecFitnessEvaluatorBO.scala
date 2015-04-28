package edu.banda.coel.business.evo.fitness

import java.{lang => jl, util => ju}
import com.banda.chemistry.business._
import com.banda.chemistry.domain._
import com.banda.core.UnboundValueException
import com.banda.math.domain.dynamics.SingleRunAnalysisSpec
import com.banda.math.business.dynamics.DoubleSingleRunProcessor
import com.banda.math.business.dynamics.JavaDoubleSingleRunProcessorExec
import com.banda.math.domain.evo.ArrayChromosome
import edu.banda.coel.domain.evo.EvoAcSpecTask
import java.io.Serializable
import com.banda.math.business.evo.EvoFitnessEvaluator
import com.banda.math.business.evo.EvoFitnessEvaluatorAdapter
import com.banda.math.domain.rand.RandomDistribution
import com.banda.chemistry.business.factory.AcCompartmentFactory

abstract class EvoAcSpecFitnessEvaluatorBO[O](
	chemistryRunnableFactory : ChemistryRunnableFactory, 
	evoAcSpecTask : EvoAcSpecTask, 
	compartmentFactory : AcCompartmentFactory, 
	singleRunProcessor : DoubleSingleRunProcessor[O], 
	maxScore : jl.Double
	) extends EvoFitnessEvaluatorAdapter[ArrayChromosome[jl.Number], ju.List[jl.Double]] {

	private val acUtil = ArtificialChemistryUtil.getInstance
	private val exec : JavaDoubleSingleRunProcessorExec[O] = new JavaDoubleSingleRunProcessorExec[O](singleRunProcessor)

	override def evaluateScore(chromosome : ArrayChromosome[jl.Number], initialState : ju.List[jl.Double]) : jl.Double = {
		val newAcSpec = evoAcSpecTask.getAcSpecBound() match {
		    case _ : AcSymmetricSpecBound => new AcSymmetricSpec
		    case bound : AcDNAStrandSpecBound => new AcDNAStrandSpec() {
		    	setMirrorComplementarity(true)
		    	setUseGlobalOrder(bound.isUseGlobalOrder)
		    } 
		}

		newAcSpec.setIncludeReverseReactions(false)
		newAcSpec.setSpeciesForbiddenRedundancy(AcReactionSpeciesForbiddenRedundancy.None)
		newAcSpec.setOutfluxAll(false)

		EvoChromosomeAcSpecConverter.modify(newAcSpec, chromosome.getCode)

		val compartment = compartmentFactory.createInstance(newAcSpec, evoAcSpecTask.getSimConfig)
		val chemistry = chemistryRunnableFactory.createNonInteractive(compartment, evoAcSpecTask.getSimConfig, None)
		try {
			val result : O = exec.run(chemistry, initialState, getSingleRunAnalysisSpec.getTimeStepLength, getSingleRunAnalysisSpec.getIterations)
			resultToScore(result)
		} catch {
		    case _ : UnboundValueException => 0 : jl.Double 
		}
	}

	override def calcFitness(score : jl.Double) : jl.Double = score / maxScore

	protected def getSingleRunAnalysisSpec() = evoAcSpecTask.getMultiRunAnalysisSpec.getSingleRunSpec

	protected def resultToScore(result : O) : jl.Double
}

object EvoChromosomeAcSpecConverter {

	def modify(
        spec : ArtificialChemistrySpec,
        code : Array[jl.Number]
    ) = {
		spec.setInfluxRatio(code(0).doubleValue);
		spec.setOutfluxRatio(code(1).doubleValue);
		spec.setConstantSpeciesRatio(code(2).doubleValue);

		spec.setRateConstantDistribution(
				RandomDistribution.createPositiveNormalDistribution(code(3).doubleValue, code(4).doubleValue));
		spec.setInfluxRateConstantDistribution(
				RandomDistribution.createPositiveNormalDistribution(code(5).doubleValue, code(6).doubleValue));
		spec.setOutfluxRateConstantDistribution(
				RandomDistribution.createPositiveNormalDistribution(code(7).doubleValue, code(8).doubleValue));

		var lastDataIndex = 0;
		if (spec.isInstanceOf[AcSymmetricSpec]) {
			var acSymmetricSpec = spec.asInstanceOf[AcSymmetricSpec];
			acSymmetricSpec.setSpeciesNum(code(9).intValue);
			acSymmetricSpec.setReactionNum(code(10).intValue);
			acSymmetricSpec.setReactantsPerReactionNumber(code(11).intValue);
			acSymmetricSpec.setProductsPerReactionNumber(code(12).intValue);
			acSymmetricSpec.setCatalystsPerReactionNumber(code(13).intValue);
			acSymmetricSpec.setInhibitorsPerReactionNumber(code(14).intValue);
			lastDataIndex = 14;
		}

		if (spec.isInstanceOf[AcDNAStrandSpec]) {
			var acDNAStrandSpec = spec.asInstanceOf[AcDNAStrandSpec];
			acDNAStrandSpec.setSingleStrandsNum(code(9).intValue);
			acDNAStrandSpec.setUpperToLowerStrandRatio(code(10).doubleValue);
			acDNAStrandSpec.setComplementaryStrandsRatio(code(11).doubleValue);
			acDNAStrandSpec.setUpperStrandPartialBindingDistribution(
					RandomDistribution.createPositiveNormalDistribution(classOf[Integer], code(12).doubleValue, code(13).doubleValue));
			lastDataIndex = 13;
		}

		// new stuff (old ac specs do not have it)
		if (code.length > lastDataIndex + 1) {
			spec.setOutfluxNonReactiveRateConstantDistribution(
					RandomDistribution.createPositiveNormalDistribution(code(lastDataIndex + 1).doubleValue, code(lastDataIndex + 2).doubleValue));	
		}
	}
}