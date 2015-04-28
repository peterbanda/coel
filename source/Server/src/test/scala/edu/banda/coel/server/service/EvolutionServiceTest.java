package edu.banda.coel.server.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.banda.chemistry.domain.AcInteractionSeries;
import com.banda.chemistry.domain.AcEvaluation;
import com.banda.chemistry.domain.ArtificialChemistry;
import com.banda.serverbase.persistence.GenericDAO;
import com.banda.math.domain.dynamics.MultiRunAnalysisSpec;
import com.banda.math.domain.evo.ArrayChromosome;
import com.banda.math.domain.evo.EvoGaSetting;
import com.banda.math.domain.evo.MutationType;
import com.banda.math.domain.evo.SelectionType;
import com.banda.math.task.EvoPopulationContentStoreOption;
import com.banda.math.task.EvoPopulationSelection;
import com.banda.math.task.EvoRunTask;

import edu.banda.coel.domain.evo.EvoAcRateConstantTask;
import edu.banda.coel.domain.service.EvolutionService;
import edu.banda.coel.server.CoelTest;

/**
 * @author Peter Banda
 * @since 2012
 */
@Transactional
public class EvolutionServiceTest extends CoelTest {

	@Autowired
	EvolutionService evolutionService;

	@Autowired
	GenericDAO<ArtificialChemistry, Long> artificialChemistryDAO;

	@Autowired
	GenericDAO<AcEvaluation, Long> acEvaluationDAO;

	@Autowired
	GenericDAO<AcInteractionSeries, Long> acInteractionSeriesDAO;

	@Autowired
	GenericDAO<MultiRunAnalysisSpec<?>, Long> multiRunAnalysisSpecDAO;
	
	@Test
	@Ignore
	public void testEvolution1() {
		final Long AC_ID = 1015l;
		final Long AC_EVALUATION_ID = 1000l;
		final Long[] ACTION_SERIES_IDs = new Long[] {1002l};
		ArtificialChemistry ac = artificialChemistryDAO.get(AC_ID);
		AcEvaluation acEvaluation = acEvaluationDAO.get(AC_EVALUATION_ID);
		List<AcInteractionSeries> actionSeries = new ArrayList<AcInteractionSeries>();
		actionSeries.addAll(acInteractionSeriesDAO.get(Arrays.asList(ACTION_SERIES_IDs)));

		EvoGaSetting gaSetting = new EvoGaSetting();
		gaSetting.setName("Test GA");
		gaSetting.setEliteNumber(2);
		gaSetting.setPopulationSize(10);
		gaSetting.setConditionalMutationFlag(false);
		gaSetting.setConditionalCrossOverFlag(false);
		gaSetting.setCrossOverProbability(0.1);
		gaSetting.setMutationProbability(0.016);
//		gaSetting.setMutationPerBitProbability(0.016);
		gaSetting.setMutationType(MutationType.OneBit);
		gaSetting.setSelectionType(SelectionType.Elite);
		gaSetting.setFitnessRenormalizationType(null);
		gaSetting.setGenerationLimit(100);
		gaSetting.setMaxValueFlag(true);

		EvoAcRateConstantTask evoAcTask = new EvoAcRateConstantTask();
		evoAcTask.setName("Evo Test Task");
		evoAcTask.setAc(ac);
		evoAcTask.setAcEvaluation(acEvaluation);
		evoAcTask.setActionSeries(actionSeries);
		evoAcTask.setRunSteps(100100);
		evoAcTask.setLastEvaluationStepsToCount(5);
		evoAcTask.setAsRepetitions(2);
		evoAcTask.setGaSetting(gaSetting);

		EvoRunTask evoTaskDef = new EvoRunTask();
		evoTaskDef.setPopulationSelection(EvoPopulationSelection.All);
		evoTaskDef.setPopulationContentStoreOption(EvoPopulationContentStoreOption.BestChromosome);
		evoTaskDef.setEvoTask(evoAcTask);
	
		evolutionService.evolve(evoTaskDef);
		assertTrue(true);
	}

	@Test
	@Rollback(false)
	public void testEvolution1x() {
		EvoRunTask evoTaskDef = new EvoRunTask();
		evoTaskDef.setEvoTaskId(1030l);
		evoTaskDef.setAutoSave(false);
		evoTaskDef.setPopulationSelection(EvoPopulationSelection.All);
		evoTaskDef.setPopulationContentStoreOption(EvoPopulationContentStoreOption.BestChromosome);

		evolutionService.evolve(evoTaskDef);
		assertTrue(true);
	}

	@Test
	@Ignore
	public void testEvolution2() {
		EvoRunTask evoTaskDef = new EvoRunTask();
		evoTaskDef.setEvoRunId(new Long(1029));
		evoTaskDef.setAutoSave(true);
		evoTaskDef.setPopulationSelection(EvoPopulationSelection.All);
		evoTaskDef.setPopulationContentStoreOption(EvoPopulationContentStoreOption.BestChromosome);

		evolutionService.evolve(evoTaskDef);
		assertTrue(true);
	}

	@Test
	@Ignore
	public void testEvolution3() {
		EvoRunTask evoTaskDef = new EvoRunTask();
		evoTaskDef.setEvoRunId(new Long(1004));
		evoTaskDef.setAutoSave(true);
		evoTaskDef.setPopulationSelection(EvoPopulationSelection.All);
		evoTaskDef.setPopulationContentStoreOption(EvoPopulationContentStoreOption.BestChromosome);

		evolutionService.evolve(evoTaskDef);
		assertTrue(true);
	}

	@Test
	@Ignore
	public void testEvolution4() {
		EvoRunTask evoTaskDef = new EvoRunTask();
		evoTaskDef.setEvoTaskId(new Long(1002));
		evoTaskDef.setPopulationSelection(EvoPopulationSelection.Last);
		evoTaskDef.setPopulationContentStoreOption(EvoPopulationContentStoreOption.Full);

		final Double[] code = new Double[] {0.08333046626309222, 4.2870599715282856, 0.13642189391798992, 0.9687735234207717,
				                            0.22648242858903087, 0.055268600570232036, 0.07983507675700369, 0.1258381004888828,
				                            0.006856545607068409, 4.51480531191773, 0.05509405213906124, 3.923291278151393};

		ArrayChromosome<Double> initChromosome = new ArrayChromosome<Double>();
		initChromosome.setCode(code);
		evoTaskDef.setInitChromosome(initChromosome);
		evolutionService.evolve(evoTaskDef);
		assertTrue(true);

	}
}