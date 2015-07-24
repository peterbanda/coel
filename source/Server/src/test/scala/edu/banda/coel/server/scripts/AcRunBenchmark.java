package edu.banda.coel.server.scripts;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.banda.coel.domain.service.ArtificialChemistryService;
import edu.banda.coel.server.CoelTest;
import edu.banda.coel.task.chemistry.AcRunAndTranslateTask;
import edu.banda.coel.task.chemistry.AcRunTask;

@Transactional
//@Ignore
public class AcRunBenchmark extends CoelTest {

	private final static int NUM_OF_RUNS = 100;
	private final static int STEPS = 50000;

	private final static int AC_COMPARTMENT_ID = 1018;
	private final static int AC_SIMULATION_CONFIG_ID = 1010;
	private final static int ACTION_SERIES_ID = 1035;
	private final static int TRANSLATION_SERIES_ID = 1002;

//	private final static int AC_ID = 1021;
//	private final static int ACTION_SERIES_ID = 1013;
//	private final static int TRANSLATION_SERIES_ID = 1003;
//	private final static int AC_ID = 1017;
//	private final static int ACTION_SERIES_ID = 1009;
//	private final static int TRANSLATION_SERIES_ID = 1003;
//	private final static int AC_ID = 1015;
//	private final static int ACTION_SERIES_ID = 1002;
//	private final static int TRANSLATION_SERIES_ID = 1000;

	@Autowired
	ArtificialChemistryService artificialChemistryService;

	@Test
	public void testAcRun() {
		StringBuilder sb = new StringBuilder();
		long minTime = Long.MAX_VALUE;
		long maxTime = Long.MIN_VALUE;
		long totalTime = 0;
		AcRunTask acTaskDef = createAcRunTask();
		for (int i = 0; i < NUM_OF_RUNS; i++) {
			Date startTime = new Date();
			System.out.println("Executing " + i + " AC run.");
			artificialChemistryService.runSimulation(acTaskDef);
			Date endTime = new Date();
			final long runTime = endTime.getTime() - startTime.getTime();
			if (runTime < minTime) {
				minTime = runTime;
			}
			if (runTime > maxTime) {
				maxTime = runTime;
			}
			totalTime += runTime;
		}
		sb.append(": Average time - ");
		sb.append(totalTime / NUM_OF_RUNS);
		sb.append(", Min time - ");
		sb.append(minTime);
		sb.append(", Max time - ");
		sb.append(maxTime);
		sb.append("\n");
		System.out.print(sb.toString());
		assertTrue(true);
	}

	@Test
	@Ignore
	public void testAcRunAndTranslation() {
		StringBuilder sb = new StringBuilder();
		long minTime = Long.MAX_VALUE;
		long maxTime = Long.MIN_VALUE;
		long totalTime = 0;
		AcRunAndTranslateTask acTask = createAcRunAndTranslationTask();
		for (int i = 0; i < NUM_OF_RUNS; i++) {
			Date startTime = new Date();
			System.out.println("Executing " + i + " AC run and translation.");
			artificialChemistryService.runAndTranslate(acTask);
			Date endTime = new Date();
			final long runTime = endTime.getTime() - startTime.getTime();
			if (runTime < minTime) {
				minTime = runTime;
			}
			if (runTime > maxTime) {
				maxTime = runTime;
			}
			totalTime += runTime;
		}
		sb.append(": Average time - ");
		sb.append(totalTime / NUM_OF_RUNS);
		sb.append(", Min time - ");
		sb.append(minTime);
		sb.append(", Max time - ");
		sb.append(maxTime);
		sb.append("\n");
		System.out.print(sb.toString());
		assertTrue(true);
	}

	private AcRunTask createAcRunTask() {
		AcRunTask acTask = new AcRunTask();
		acTask.setCompartmentId(new Long(AC_COMPARTMENT_ID));
		acTask.setSimulationConfigId(new Long(AC_SIMULATION_CONFIG_ID));
		acTask.setInteractionSeriesId(new Long(ACTION_SERIES_ID));
		acTask.setRunTime(STEPS);
		acTask.setRepetitions(1);
		return acTask;
	}

	private AcRunAndTranslateTask createAcRunAndTranslationTask() {
		AcRunAndTranslateTask acRunAndTranslationTask = new AcRunAndTranslateTask();
		acRunAndTranslationTask.setRunOnGrid(false);
		acRunAndTranslationTask.setRunTaskDefinition(createAcRunTask());
		acRunAndTranslationTask.setTranslationSeriesId(new Long(TRANSLATION_SERIES_ID));
		return acRunAndTranslationTask;
	}
}