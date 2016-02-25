package edu.banda.coel.server.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.banda.chemistry.domain.AcCompartment;
import com.banda.chemistry.domain.AcInteractionSeries;
import com.banda.chemistry.domain.AcRateConstantTypeBound;
import com.banda.chemistry.domain.AcSimulationConfig;
import com.banda.chemistry.domain.AcSpecies;
import com.banda.chemistry.domain.ArtificialChemistry;
import com.banda.chemistry.domain.ArtificialChemistrySpec;
import com.banda.core.Pair;
import com.banda.core.domain.ComponentRunTrace;
import com.banda.core.util.FileUtil;
import com.banda.math.domain.Stats;
import com.banda.math.domain.StatsType;
import com.banda.math.domain.dynamics.MultiRunAnalysisSpec;
import com.banda.math.domain.dynamics.SingleRunAnalysisResultType;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.domain.service.ArtificialChemistryService;
import edu.banda.coel.server.CoelTest;
import edu.banda.coel.task.chemistry.AcMultiRunAnalysisTask;
import edu.banda.coel.task.chemistry.AcPerformanceEvaluateTask;
import edu.banda.coel.task.chemistry.AcPerturbateTask;
import edu.banda.coel.task.chemistry.AcPerturbationPerformanceEvaluateTask;
import edu.banda.coel.task.chemistry.AcRandomRatePerformanceEvaluateTask;
import edu.banda.coel.task.chemistry.AcRunAndTranslateTask;
import edu.banda.coel.task.chemistry.AcRunTask;
import edu.banda.coel.task.chemistry.AcRunTranslateAndEvaluateTask;
import com.banda.core.domain.um.User;

/**
 * @author Peter Banda
 * @since 2012
 */
@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ArtificialChemistryServiceTest extends CoelTest {

	@Autowired
	ArtificialChemistryService artificialChemistryService;

	@Autowired
	GenericDAO<ArtificialChemistry, Long> artificialChemistryDAO;

	@Autowired
	GenericDAO<AcInteractionSeries, Long> acInteractionSeriesDAO;

	@Autowired
	GenericDAO<AcRateConstantTypeBound, Long> acRateConstantTypeBoundDAO;

	@Test
	@Ignore
	public void testRunSimulationWithResult() {
		AcRunTask acTask = new AcRunTask();
		acTask.setCompartmentId(3484l);
		acTask.setSimulationConfigId(1010l);
		acTask.setInteractionSeriesId(1406l);
		acTask.setRunTime(10);

		Collection<ComponentRunTrace<Double, Pair<AcCompartment, AcSpecies>>> acRuns = artificialChemistryService.runSimulation(acTask);
		assertNotEmpty(acRuns);
		assertEquals(acRuns.size(), 1);
	}

	@Test
	@Ignore
	public void testRunPerturbationPerformanceEvaluation() {
		AcPerturbationPerformanceEvaluateTask taskDef = new AcPerturbationPerformanceEvaluateTask();

		taskDef.setCompartmentId(new Long(1015));
		taskDef.setSimulationConfigId(1010l);
		taskDef.setActionSeriesIds(Collections.singleton(1002l));
		taskDef.setAcEvaluationIds(Collections.singleton(1000l));
		taskDef.setRunTime(120100);
		taskDef.setRepetitions(10);
		taskDef.setPerturbationNum(2);
		taskDef.addPerturbationStrength(0.5);
		artificialChemistryService.runPerturbationPerformanceEvaluation(taskDef);
	}

	@Test
	@Ignore
	public void testGetMergedAcStats() {
		Collection<Long> acIds = new ArrayList<Long>();
		acIds.add(1022l);
		acIds.add(1023l);
		acIds.add(1025l);

		Collection<Stats> stats = artificialChemistryService.getMergedAcStats(SingleRunAnalysisResultType.DerridaResults, StatsType.Mean, acIds);
		checkStats(stats);
	}

	@Test
//	@Rollback(false)
	@Ignore
	public void testRerunDerrida() {
		Collection<Long> acIds = new ArrayList<Long>();
//		for (ArtificialChemistry ac : artificialChemistryDAO.getAll()) {
//			acIds.add(ac.getId());
//		}
		acIds.add(1016l);
		acIds.add(1017l);
		acIds.add(1018l);

		AcMultiRunAnalysisTask taskDef = new AcMultiRunAnalysisTask();
		taskDef.setCompartmentIds(acIds);
		taskDef.setSimulationConfigId(1010l);

		MultiRunAnalysisSpec<Double> multiSpec = new MultiRunAnalysisSpec<Double>();
		multiSpec.setId(1000l);
		taskDef.setMultiRunAnalysisSpec(multiSpec);

		artificialChemistryService.rerunDerridaAnalysis(taskDef);
	}	
	
	@Test
	@Ignore
	public void testgetMergedMultiRunStats1() {
		Collection<Stats> results = artificialChemistryService.getMergedMultiRunStats(SingleRunAnalysisResultType.SpatialCorrelations, StatsType.Mean,
				Arrays.asList(new Long[] {1000l, 1001l}));
		checkStats(results);
	}

	@Test
	@Ignore
	public void testgetMergedMultiRunStats2() {
		Collection<Stats> results = artificialChemistryService.getMergedMultiRunStats(SingleRunAnalysisResultType.FinalLyapunovExponents, StatsType.Mean,
				Arrays.asList(new Long[] {1000l}));
		checkStats(results);
	}

	private void checkStats(Collection<Stats> multipleStats) {
		assertNotNull(multipleStats);
		for (Stats stats : multipleStats) {
			assertNotNull(stats);
			assertNotNull(stats.getPos());
			assertNotNull(stats.getMin());
			assertNotNull(stats.getMax());
			assertNotNull(stats.getMean());
			assertNotNull(stats.getStandardDeviation());
			System.out.println(stats.getPos() + ": " + stats.getMean() + ", " + stats.getMin() + ", " + stats.getMax());
		}		
	}

	@Test
	@Ignore
	public void testAnalyzeDynamics() {
		AcMultiRunAnalysisTask taskDef = new AcMultiRunAnalysisTask();
		taskDef.setCompartmentIds(Arrays.asList(new Long[] {1016l, 1017l, 1018l, 1019l, 1022l, 1023l}));
		taskDef.setSimulationConfigId(1010l);

//		SingleRunAnalysisSpec spec = new SingleRunAnalysisSpec();
//		spec.setTimeStepLength(1D);
//		spec.setLyapunovPerturbationStrength(0.00001);
//		spec.setDerridaPerturbationStrength(1D);
//		spec.setDerridaTimeLength(10D);
//		spec.setIterations(2000);
//		spec.setTimeStepToFilter(50);
//		spec.setFixedPointDetectionPrecision(0.0000001);
//		spec.setDerridaResolution(0.05);
//
//		MultiRunAnalysisSpec<Double> multiSpec = new MultiRunAnalysisSpec<Double>();
//		multiSpec.setName("Test");
//		multiSpec.setSingleRunSpec(spec);
//		multiSpec.setInitialStateDistribution(new UniformDistribution<Double>(0d, 0.1));
//		multiSpec.setRunNum(10);

		MultiRunAnalysisSpec<Double> multiSpec = new MultiRunAnalysisSpec<Double>();
		multiSpec.setId(1000l);
		taskDef.setMultiRunAnalysisSpec(multiSpec);

		artificialChemistryService.analyzeDynamics(taskDef);
	}

	@Test
	@Ignore
	public void testGenerateAcs() {
		ArtificialChemistrySpec acSpec = new ArtificialChemistrySpec();
		acSpec.setId(1011l);

		AcSimulationConfig acSimConfig = new AcSimulationConfig();
		acSimConfig.setId(1030l);
		artificialChemistryService.generateArtificialChemistries(acSpec, acSimConfig, 5);
	}

	@Test
	@Ignore
	public void testRunSimulation() {
		ArtificialChemistry ac = artificialChemistryDAO.get(new Long(1));
		AcInteractionSeries actionSeries = acInteractionSeriesDAO.get(new Long(1));

		AcRunTask acTask = new AcRunTask();
		acTask.setCompartment(ac.getSkinCompartment());
		acTask.setSimulationConfig(ac.getSimulationConfig());
		acTask.setInteractionSeries(actionSeries);
		acTask.setRunTime(3);

		artificialChemistryService.runSimulation(acTask);
		assertTrue(true);
	}

	@Test
	@Ignore
	public void testRunSimulation2() {
		AcRunTask acTask = new AcRunTask();
		acTask.setCompartmentId(new Long(1022));
		acTask.setSimulationConfigId(1010l);
		acTask.setInteractionSeriesId(new Long(1015));
		acTask.setRunTime(10000);

		artificialChemistryService.runSimulation(acTask);
		assertTrue(true);
	}

	@Test
	@Ignore
	public void testSaveSbmlModelAsCompartment() throws FileNotFoundException, IOException {
		String fileName = "perceptron_v06.xml";
		String sbmlString = FileUtil.getInstance().readStringFromFile(fileName);
		User user = new User();
		user.setId(new Long(1));
		artificialChemistryService.saveSbmlModelAsCompartment(sbmlString, fileName.substring(0, fileName.indexOf('.')), user, false);
		assertTrue(true);
	}

	@Test
	@Ignore
	public void testRunTranslateAndEvaluateRun() {
		AcRunAndTranslateTask acRunAndTranslationTask = new AcRunAndTranslateTask();
		AcRunTask acRunTask = new AcRunTask();

		acRunTask.setCompartmentId(new Long(1016));
		acRunTask.setSimulationConfigId(1010l);
		acRunTask.setInteractionSeriesId(new Long(1006));
		acRunTask.setRunTime(500100);
		acRunTask.setRepetitions(1);

		acRunAndTranslationTask.setRunTaskDefinition(acRunTask);
		acRunAndTranslationTask.setTranslationSeriesId(new Long(1003));

		AcRunTranslateAndEvaluateTask acEvaluationTask = new AcRunTranslateAndEvaluateTask();
		acEvaluationTask.setRunAndTranslationTaskDefinition(acRunAndTranslationTask);
		acEvaluationTask.setAcEvaluationId(new Long(1003));
		acEvaluationTask.setEvaluateFullFlag(true);

		artificialChemistryService.runSimulationTranslateAndEvaluate(acEvaluationTask);
		assertTrue(true);
	}

	@Test
	@Ignore
	public void testPerturbationAnalysis() {
		AcRunAndTranslateTask acRunAndTranslationTask = new AcRunAndTranslateTask();
		AcRunTask acRunTask = new AcRunTask();

		acRunTask.setCompartmentId(new Long(1015));
		acRunTask.setSimulationConfigId(1010l);
		acRunTask.setInteractionSeriesId(new Long(1002));
		acRunTask.setRunTime(20100);
		acRunTask.setRepetitions(4);

		acRunAndTranslationTask.setRunTaskDefinition(acRunTask);
		acRunAndTranslationTask.setTranslationSeriesId(new Long(1000));

		AcPerturbateTask perturbationTask = new AcPerturbateTask();
		perturbationTask.setRunAndTranslationTaskDefinition(acRunAndTranslationTask);
		perturbationTask.setAcEvaluationId(new Long(1000));
		perturbationTask.setEvaluateFullFlag(true);
		perturbationTask.setRepetitions(10);
		perturbationTask.setPerturbationStrength(2.00);

		Double[] overallAveragedEvaluatedRun = artificialChemistryService.runPerturbationAnalysis(perturbationTask);
		assertNotNull(overallAveragedEvaluatedRun);
		assertTrue(overallAveragedEvaluatedRun.length > 0);
	}

	@Test
//	@Rollback(false)
	public void testRunPerformanceEvaluation() {
		AcPerformanceEvaluateTask taskDef = new AcPerformanceEvaluateTask();

		taskDef.setCompartmentId(new Long(1126));
		taskDef.setSimulationConfigId(1010l);
		taskDef.setActionSeriesIds(Arrays.asList(new Long[] {1097l, 1216l}));
		taskDef.setAcEvaluationIds(Arrays.asList(new Long[] {1007l, 1012l, 1008l}));
		taskDef.setRunTime(120100);
		taskDef.setRepetitions(20);
		artificialChemistryService.runPerformanceEvaluation(taskDef);
	}

	@Test
	@Ignore
//	@Transactional
//	@Rollback(false)
	public void testRunRandomRatePerformanceEvaluation() {
		AcRandomRatePerformanceEvaluateTask taskDef = new AcRandomRatePerformanceEvaluateTask();

		taskDef.setCompartmentId(new Long(1015));
		taskDef.setSimulationConfigId(1010l);
		Collection<Long> actionSeriesIds = new ArrayList<Long>();
		actionSeriesIds.add(1002l);
		actionSeriesIds.add(1002l);

		taskDef.setActionSeriesIds(actionSeriesIds);
		taskDef.setAcEvaluationIds(Collections.singleton(1000l));
		taskDef.setRunTime(120100);
		taskDef.setRepetitions(2);
		taskDef.setRandomRateGenerationNum(10);

		Collection<Long> rateConstantTypeBoundIds = new ArrayList<Long>();
		rateConstantTypeBoundIds.add(1000l);
		rateConstantTypeBoundIds.add(1001l);
		rateConstantTypeBoundIds.add(1002l);
		rateConstantTypeBoundIds.add(1003l);
		taskDef.setRateConstantTypeBoundIds(rateConstantTypeBoundIds);

		artificialChemistryService.runRandomRatePerformanceEvaluation(taskDef);
	}
}