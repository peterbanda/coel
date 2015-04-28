package edu.banda.coel.business.evo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.chemistry.business.*;
import com.banda.chemistry.business.factory.AcCompartmentFactory;
import com.banda.chemistry.business.factory.AcReactionSetFactory;
import com.banda.chemistry.business.factory.AcSpeciesSetFactory;
import com.banda.chemistry.domain.*;
import com.banda.core.domain.ValueBound;
import com.banda.core.reflection.ReflectionProvider;
import com.banda.math.business.evo.EvoArrayChromManipulatorBO;
import com.banda.math.business.evo.EvoListTestSampleGeneratorBO;
import com.banda.math.business.evo.EvoTaskBO;
import com.banda.math.business.rand.RandomDistributionProvider;
import com.banda.math.business.rand.RandomDistributionProviderFactory;
import com.banda.math.domain.dynamics.MultiRunAnalysisSpec;
import com.banda.math.domain.evo.ArrayChromosome;
import com.banda.math.domain.evo.Chromosome;
import com.banda.math.domain.evo.EvoGaSetting;

import edu.banda.coel.CoelRuntimeException;
import edu.banda.coel.business.evo.fitness.*;
import edu.banda.coel.domain.evo.EvoAcSpecTask;

final class EvoAcSpecTaskBOFactory implements EvoTaskBOFactory<EvoAcSpecTask>, Serializable {

	private static final double STATIONARY_POINT_DETECTION_PRECISION = 0.0000001;
	private static final double TURNING_POINT_DETECTION_PRECISION = 0.0000001;
	private static final double TURNING_POINT_EXTREME_DIFF = 0.002;

	private final ChemistryRunnableFactory chemistryRunnableFactory;

	private final AcCompartmentFactory compartmentFactory = new AcCompartmentFactory(AcReactionSetFactory.getInstance(), AcSpeciesSetFactory.getInstance());

	@Autowired
	private ReflectionProvider<? extends Chromosome<?>> chromosomeRF;

	public EvoAcSpecTaskBOFactory(ChemistryRunnableFactory chemistryRunnableFactory) {
		this.chemistryRunnableFactory = chemistryRunnableFactory;
	}

	@Override
	public EvoTaskBO<?, ?, ?> createInstance(EvoAcSpecTask evoAcSpecTask, EvoGaSetting gaSetting) {
		final ArtificialChemistrySpecBound acSpecBound = evoAcSpecTask.getAcSpecBound();
		final MultiRunAnalysisSpec<Double> analysisSpec = evoAcSpecTask.getMultiRunAnalysisSpec();
		final RandomDistributionProvider<Double> initialStateDP =  
				RandomDistributionProviderFactory.apply(analysisSpec.getInitialStateDistribution());

		final EvoArrayChromManipulatorBO<Number> chromManipulator = new EvoArrayChromManipulatorBO<Number>(
				(ReflectionProvider<ArrayChromosome<Number>>) chromosomeRF,
				Number.class,
				gaSetting.getBitMutationType(),
				gaSetting.getPertrubMutationStrength(),
				createAcSpecBounds(acSpecBound));

		EvoAcSpecFitnessEvaluatorBO<?> fitnessEvaluatorBO = null;
		switch (evoAcSpecTask.getTaskType()) {
			case AcSpecStationaryPoint :
				fitnessEvaluatorBO = new EvoAcStationaryPointFitnessEvaluatorBO(
					chemistryRunnableFactory,
					evoAcSpecTask,
					compartmentFactory,
					STATIONARY_POINT_DETECTION_PRECISION);
				break;
			case AcSpecTurningPoint :
				fitnessEvaluatorBO = new EvoAcTurningPointFitnessEvaluatorBO(
					chemistryRunnableFactory,
					evoAcSpecTask,
					compartmentFactory,
					TURNING_POINT_DETECTION_PRECISION,
					TURNING_POINT_EXTREME_DIFF,
					evoAcSpecTask.getSimConfig().getUpperThreshold());
				break;
			case AcSpecNonlinearityErrors :
				fitnessEvaluatorBO = new EvoAcNonlinearityErrorsFitnessEvaluatorBO(
					chemistryRunnableFactory,
					evoAcSpecTask,
					compartmentFactory,
					evoAcSpecTask.getSimConfig().getUpperThreshold());
				break;
			default:
				throw new CoelRuntimeException("Eo task not '" + evoAcSpecTask.getTaskType() + "' allowed for AC spec instance.");
		}

		final EvoListTestSampleGeneratorBO<Double> testSampleGeneratorBO = new EvoListTestSampleGeneratorBO<Double>(
				initialStateDP,
				analysisSpec.getRunNum(),
				200);

		EvoTaskBO<?, ?, ?> evoTaskBO = new EvoTaskBO<ArrayChromosome<Number>, Number[], List<Double>> (
				chromManipulator, fitnessEvaluatorBO, testSampleGeneratorBO);

		return evoTaskBO;
	}

	private List<ValueBound<? extends Number>> createAcSpecBounds(ArtificialChemistrySpecBound acSpecBound) {
		List<ValueBound<? extends Number>> bounds = new ArrayList<ValueBound<? extends Number>>();
		bounds.add(acSpecBound.getInfluxRatio());
		bounds.add(acSpecBound.getOutfluxRatio());
		bounds.add(acSpecBound.getConstantSpeciesRatio());
		bounds.add(acSpecBound.getRateConstantDistributionLocation());
		bounds.add(acSpecBound.getRateConstantDistributionShape());
		bounds.add(acSpecBound.getInfluxRateConstantDistributionLocation());
		bounds.add(acSpecBound.getInfluxRateConstantDistributionShape());
		bounds.add(acSpecBound.getOutfluxRateConstantDistributionLocation());
		bounds.add(acSpecBound.getOutfluxRateConstantDistributionShape());

		if (acSpecBound instanceof AcSymmetricSpecBound) {
			AcSymmetricSpecBound acSymmetricSpecBound = (AcSymmetricSpecBound) acSpecBound;
			bounds.add(acSymmetricSpecBound.getSpeciesNum());
			bounds.add(acSymmetricSpecBound.getReactionNum());
			bounds.add(acSymmetricSpecBound.getReactantsPerReactionNumber());
			bounds.add(acSymmetricSpecBound.getProductsPerReactionNumber());
			bounds.add(acSymmetricSpecBound.getCatalystsPerReactionNumber());
			bounds.add(acSymmetricSpecBound.getInhibitorsPerReactionNumber());
		}

		if (acSpecBound instanceof AcDNAStrandSpecBound) {
			AcDNAStrandSpecBound acDNAStrandSpecBound = (AcDNAStrandSpecBound) acSpecBound;
			bounds.add(acDNAStrandSpecBound.getSingleStrandsNum());
			bounds.add(acDNAStrandSpecBound.getUpperToLowerStrandRatio());
			bounds.add(acDNAStrandSpecBound.getComplementaryStrandsRatio());
			bounds.add(acDNAStrandSpecBound.getUpperStrandPartialBindingDistributionLocation());
			bounds.add(acDNAStrandSpecBound.getUpperStrandPartialBindingDistributionShape());
		}

		bounds.add(acSpecBound.getOutfluxNonReactiveRateConstantDistributionLocation());
		bounds.add(acSpecBound.getOutfluxNonReactiveRateConstantDistributionShape());

		return bounds;
	}
}