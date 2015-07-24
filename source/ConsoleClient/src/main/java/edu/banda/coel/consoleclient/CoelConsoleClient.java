package edu.banda.coel.consoleclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.banda.chemistry.domain.AcEvaluationItemState;
import com.banda.core.consoleclient.ConsoleClient;
import com.banda.core.consoleclient.ConsoleClientException;
import com.banda.core.consoleclient.ConsoleClientOption;
import com.banda.math.domain.evo.ArrayChromosome;
import com.banda.math.task.EvoPopulationContentStoreOption;
import com.banda.math.task.EvoPopulationSelection;
import com.banda.math.task.EvoRunTask;

import edu.banda.coel.CoelRuntimeException;
import edu.banda.coel.domain.service.ArtificialChemistryService;
import edu.banda.coel.domain.service.EvolutionService;
import edu.banda.coel.task.chemistry.AcPerturbateTask;
import edu.banda.coel.task.chemistry.AcRunAndTranslateTask;
import edu.banda.coel.task.chemistry.AcRunTask;
import edu.banda.coel.task.chemistry.AcRunTranslateAndEvaluateTask;

/**
 * @author © Peter Banda
 * @since 2011   
 */
@Deprecated
public class CoelConsoleClient extends ConsoleClient {

	private static final String APP_NAME = "COEL Console client";
	private static final String JAR_NAME = "coel-consoleclient.jar";
	private static final String VERSION = "0.8.1";

	private static final String PERTURBATION_EVAL_FILE_NAME_PREFIX = "ac_perturb_eval_";
	private static final int CORRECT_RATE_LABEL_LENGTH = AcEvaluationItemState.Correct.toString().length();
	
	/**
	 * The locations of Spring configurations.
	 */
	private static final String[] SPRING_CONF_LOCATIONS = new String[] {"classpath:/remote_service-config.xml"};

	private ArtificialChemistryService artificialChemistryService;
	private EvolutionService evolutionService;

	private enum ConsoleJobType {
		acRun, acEvaluation, acRunTranslationAndEvaluation,
		acPerturbation, acPerturbationResultsMerge, evoAcEvolution
	}

	public CoelConsoleClient(String[] args) {
		super(args);
	}

	@Override
	protected ConsoleClientOption<?>[] createOptions() {
		return new ConsoleClientOption<?>[] {
			Options.jobType,
			Options.artificialChemistryId,
			Options.actionSeriesId,
			Options.evaluatedActionSeriesId,
			Options.translationSeriesId,
			Options.acEvaluationId,
			Options.runIds,
			Options.translatedRunIds,
			Options.runTime,
			Options.translationSteps,
			Options.evaluationSteps,
			Options.translateFullRunFlag,
			Options.evaluateFullTranslationFlag,
			Options.repetitions,
			Options.acZeroThresholdViolationHandling,
			Options.acUpperThresholdViolationHandling,
			Options.acNotANumberConcentrationHandling,
			Options.perturbationStrength,
			Options.perturbationRepetitions,
			Options.evoTaskId,
			Options.evoPopulationContentStoreOption,
			Options.evoPopulationSelection,
			Options.initChromosomeCode
		};
	}

	@Override
	protected void execute() {
		switch (getJobType()) {
			case acRun: executeAcRunTask(); break;
//			case acEvaluation: executeAcEvaluationTask(); break;
			case acRunTranslationAndEvaluation: executeAcRunTranslationAndEvaluationTask(); break;
			case acPerturbation: executeAcPerturbationTask(); break;
			case acPerturbationResultsMerge: mergeAcPerturbationResults(); break;
			case evoAcEvolution: executeEvoAcTask(); break;
			default: throw new ConsoleClientException("Job type '" + getJobType() + "' does not have a handler specified.");
		}
	}

	private void executeAcRunTask() {
		println("Submiting AC run task with following parameters:");
		println("------------------------------------------------");
		println("AC id                     :" + getArtificialChemistryId());
		println("Action series id          :" + getActionSeriesId());
		println("Evaluated interaction series id:" + getEvalutedActionSeriesId());
		println("Run time                  :" + getRunTime());
		println("Repetitions               :" + getRepetitions());
		println("");
		println("...");
		Date startTime = new Date();

		AcRunTask acTask = new AcRunTask();
		acTask.setCompartmentId(getCompartmentId());
		acTask.setSimulationConfigId(getSimulationConfigId());
		acTask.setInteractionSeriesId(getActionSeriesId());
		acTask.setRunTime(getRunTime());
		acTask.setRepetitions(getRepetitions());
		artificialChemistryService.runSimulation(acTask);

		printlnExecutionTimeInfo(startTime);
	}

	private void executeAcRunTranslationAndEvaluationTask() {
		println("Submiting AC run, translation and evaluation (aka all in one) task with following parameters:");
		println("----------------------------------------------------------------------------");
		println("AC id                       :" + getArtificialChemistryId());
		println("AC interaction series id         :" + getActionSeriesId());
		println("Evaluated interaction series id  :" + getEvalutedActionSeriesId());
		println("Repetitions                 :" + getRepetitions());
		println("AC task id                  :" + getAcTaskId());
		println("Run time                    :" + getRunTime());
		println("AC translation series id    :" + getTranslationSeriesId());
		println("Number of translation steps :" + getTranslationSteps());
		println("Translate full run flag     :" + isTranslateFullRunFlag());
		println("AC evaluation id            :" + getAcEvaluationId());
		println("Number of evaluation steps  :" + getEvaluationSteps());
		println("Evaluate full transl. flag  :" + isEvaluateFullRunFlag());
		println("");
		println("...");
		Date startTime = new Date();

		AcRunTranslateAndEvaluateTask task = getAcRunTranslationAndEvaluationTask();
		artificialChemistryService.runSimulationTranslateAndEvaluate(task);

		printlnExecutionTimeInfo(startTime);
	}

	private void executeAcPerturbationTask() {
		println("Submiting AC perturbation performance task:");
		println("-------------------------------------------");
		println("AC id                       :" + getArtificialChemistryId());
		println("AC interaction series id         :" + getActionSeriesId());
		println("Evaluated interaction series id  :" + getEvalutedActionSeriesId());
		println("Repetitions                 :" + getRepetitions());
		println("AC task id                  :" + getAcTaskId());
		println("Run time                    :" + getRunTime());
		println("AC translation series id    :" + getTranslationSeriesId());
		println("Number of translation steps :" + getTranslationSteps());
		println("Translate full run flag     :" + isTranslateFullRunFlag());
		println("AC evaluation id            :" + getAcEvaluationId());
		println("Number of evaluation steps  :" + getEvaluationSteps());
		println("Evaluate full transl. flag  :" + isEvaluateFullRunFlag());
		println("Perturbation repetitions    :" + getPerturbationRepetitions());
		println("Perturbation strength       :" + getPerturbationStrength());
		println("");
		println("...");
		Date startTime = new Date();

		Double[] acEvaluationAverages = artificialChemistryService.runPerturbationAnalysis(getAcPerturbationTask());
		String fileName = getAcPerturbationEvalutionFileName(getActionSeriesId()); 
		writeToFile(fileName, asString(acEvaluationAverages));

		printlnExecutionTimeInfo(startTime);
	}

	private void mergeAcPerturbationResults() {
		println("Merging AC perturbation results:");
		println("--------------------------------");
		println("AC id                       :" + getArtificialChemistryId());
		println("AC evaluation id            :" + getAcEvaluationId());
		println("Perturbation strength       :" + getPerturbationStrength());
		println("");
		println("...");
		Date startTime = new Date();

		final int minActionSeriesId = 1003;
		final int maxActionSeriesId = 1300;
		Collection<Integer> actionSeriesFound = new ArrayList<Integer>();
		StringBuilder sb = new StringBuilder();
		for (int actionSeriesId = minActionSeriesId; actionSeriesId <= maxActionSeriesId; actionSeriesId++) {
			final String correctRate = getCorrectRateFromFile(actionSeriesId);
			if (correctRate != null) {
				actionSeriesFound.add(actionSeriesId);
				sb.append(actionSeriesId);
				sb.append(correctRate);
				sb.append("\n");
			}
		}
		final String fileName = getOverallAcPerturbationEvalutionFileName();
		writeToFile(fileName, sb.toString());

		printlnExecutionTimeInfo(startTime);
		println("Overall results written into the file '" + fileName + "'.");
		println("Following interaction series found:");
		println(actionSeriesFound.toString());
	}

	private void executeEvoAcTask() {
		println("Submiting evolution task with following parameters:");
		println("---------------------------------------------------");
		println("Evo AC task id            :" + getEvoTaskId());
		println("Save option               :" + getEvoPopulationContentStoreOption());
		println("Init. chromomosome chode  :" + getInitChromosomeCode());
		println("");
		println("...");
		Date startTime = new Date();

		EvoRunTask evoTask = new EvoRunTask();
		evoTask.setEvoTaskId(getEvoTaskId());
		evoTask.setPopulationContentStoreOption(getEvoPopulationContentStoreOption());
		evoTask.setPopulationSelection(getEvoPopulationSelectionStoreOption());

		// optional
		Collection<Double> initChromosomeCode = getInitChromosomeCode();
		if (initChromosomeCode != null) {
			ArrayChromosome<Double> arrayChromosome = new ArrayChromosome<Double>();
			arrayChromosome.setCode(getInitChromosomeCode().toArray(new Double[0]));
			evoTask.setInitChromosome(arrayChromosome);
		}
		Boolean autoSaveFlag = isEvoAutoSaveFlag();
		if (autoSaveFlag) {
			evoTask.setAutoSave(autoSaveFlag);
		}

		evolutionService.evolve(evoTask);

		printlnExecutionTimeInfo(startTime);
	}

	private String getCorrectRateFromFile(int actionSeriesId) {
		final String fileName = getAcPerturbationEvalutionFileName(actionSeriesId);
		if (!existsFile(fileName)) {
			return null;
		}
		String perturbationEvaluationString = readFromFile(fileName);
		String[] evalItemStateDatas = StringUtils.split(perturbationEvaluationString, '\n');
		for (String evalItemStateData : evalItemStateDatas) {
			if (evalItemStateData.contains(AcEvaluationItemState.Correct.toString())) {
				return evalItemStateData.substring(CORRECT_RATE_LABEL_LENGTH); 
			}
		}
		throw new CoelRuntimeException("AC pertrubation result file '" + fileName + "' corrupted.");
	}

	private String getAcPerturbationEvalutionFileName(long actionSeriesId) {
		StringBuilder sb = new StringBuilder();
		sb.append(PERTURBATION_EVAL_FILE_NAME_PREFIX);
		sb.append(getAcEvaluationId());
		sb.append("_");
		sb.append(actionSeriesId);
		sb.append("_");
		sb.append(getArtificialChemistryId());
		sb.append("_");
		sb.append(getPerturbationStrength());
		return sb.toString();
	}

	private String getOverallAcPerturbationEvalutionFileName() {
		StringBuilder sb = new StringBuilder();
		sb.append(PERTURBATION_EVAL_FILE_NAME_PREFIX);
		sb.append(getAcEvaluationId());
		sb.append("_");
		sb.append(getArtificialChemistryId());
		sb.append("_");
		sb.append(getPerturbationStrength());
		return sb.toString();
	}

	private String asString(Double[] acEvaluationAverages) {
		final int evaluatedRunLength = acEvaluationAverages.length;

		StringBuffer sb = new StringBuffer();
		// header
		sb.append("Time, ");
		for (int i = 0; i < evaluatedRunLength; i++) {
			sb.append(i);
			if (i < evaluatedRunLength - 1) {
				sb.append(", ");
			}
		}
		sb.append("\n");

		// rest
		String average = StringUtils.join(acEvaluationAverages, ", ");
		sb.append(average);
		sb.append("\n");

		return sb.toString();
	}

	@Override
	protected void validate() {
		super.validate();
		switch (getJobType()) {
			case acRun:
				checkOptionsNotNull(
					new ConsoleClientOption<?>[] {Options.artificialChemistryId, Options.runTime}
				);
				checkExactlyOneOptionNotNull(
					new ConsoleClientOption<?>[] {Options.actionSeriesId, Options.evaluatedActionSeriesId},
					"For AC translation exactly one of the following options is expected:"
				);
				break;

			case acPerturbation:
				checkOptionsNotNull(
					new ConsoleClientOption<?>[] {Options.perturbationStrength, Options.perturbationRepetitions}
				);

			case acRunTranslationAndEvaluation:
				checkAtLeastOneOfOptionsNotNull(
					new ConsoleClientOption<?>[] {Options.acEvaluationId, Options.acTaskId},
					"For AC run, translation, and evaluation (aka all in one) at least one of the following options is expected:"
				);

			case acPerturbationResultsMerge:
				checkOptionsNotNull(
					new ConsoleClientOption<?>[] {Options.artificialChemistryId, Options.acEvaluationId, Options.perturbationStrength}
				);
				break;

			case acEvaluation:
				checkAtLeastOneOfOptionsNotNull(
					new ConsoleClientOption<?>[] {Options.acEvaluationId, Options.acTaskId},
					"For AC evaluation at least one of the following options is expected:"
				);
				break;
				
		}
	}

	@Override
	protected String getAppName() {
		return APP_NAME;
	}

	@Override
	protected String getJarName() {
		return JAR_NAME;
	}

	@Override
	protected String getVersion() {
		return VERSION;
	}

	private ConsoleJobType getJobType() {
		Byte jobType = getOptionValue(Options.jobType);
		if (jobType == null) {
			return null;
		}
		return ConsoleJobType.values()[jobType];
	}

	private EvoPopulationContentStoreOption getEvoPopulationContentStoreOption() {
		Byte evoStoreOption = getOptionValue(Options.evoPopulationContentStoreOption);
		if (evoStoreOption == null) {
			return null;
		}
		return EvoPopulationContentStoreOption.values()[evoStoreOption];
	}

	private EvoPopulationSelection getEvoPopulationSelectionStoreOption() {
		Byte evoStoreOption = getOptionValue(Options.evoPopulationSelection);
		if (evoStoreOption == null) {
			return null;
		}
		return EvoPopulationSelection.values()[evoStoreOption];
	}

	private Long getArtificialChemistryId() {
		return getOptionValue(Options.artificialChemistryId);
	}

	private Long getCompartmentId() {
		return getOptionValue(Options.compartmentId);
	}

	private Long getSimulationConfigId() {
		return getOptionValue(Options.simulationConfigId);
	}

	private Long getActionSeriesId() {
		return getOptionValue(Options.actionSeriesId);
	}

	private Long getEvalutedActionSeriesId() {
		return getOptionValue(Options.evaluatedActionSeriesId);
	}

	private Long getAcEvaluationId() {
		return getOptionValue(Options.acEvaluationId);
	}

	private Integer getRunTime() {
		return getOptionValue(Options.runTime);
	}

	private Integer getTranslationSteps() {
		return getOptionValue(Options.translationSteps);
	}

	private Integer getEvaluationSteps() {
		return getOptionValue(Options.evaluationSteps);
	}

	private Integer getRepetitions() {
		return getOptionValue(Options.repetitions);
	}

	private Long getTranslationSeriesId() {
		return getOptionValue(Options.translationSeriesId);
	}

	private Long getAcTaskId() {
		return getOptionValue(Options.acTaskId);
	}

	private Collection<Long> getRunIds() {
		return (Collection<Long>) getOptionValue(Options.runIds);
	}

	private Collection<Long> getTranslatedRunIds() {
		return (Collection<Long>) getOptionValue(Options.translatedRunIds);
	}

	private Collection<Double> getInitChromosomeCode() {
		return (Collection<Double>) getOptionValue(Options.initChromosomeCode);
	}

	private Boolean isTranslateFullRunFlag() {
		return getOptionValue(Options.translateFullRunFlag);
	}

	private Boolean isEvaluateFullRunFlag() {
		return getOptionValue(Options.evaluateFullTranslationFlag);
	}

	private Boolean isEvoAutoSaveFlag() {
		return getOptionValue(Options.evoAutoSaveFlag);
	}

	private Long getEvoTaskId() {
		return getOptionValue(Options.evoTaskId);
	}

	private Integer getPerturbationRepetitions() {
		return getOptionValue(Options.perturbationRepetitions);
	}

	private Double getPerturbationStrength() {
		return getOptionValue(Options.perturbationStrength);
	}

	public ArtificialChemistryService getArtificialChemistryService() {
		return artificialChemistryService;
	}

	public void setArtificialChemistryService(ArtificialChemistryService artificialChemistryService) {
		this.artificialChemistryService = artificialChemistryService;
	}

	public EvolutionService getEvolutionService() {
		return evolutionService;
	}

	public void setEvolutionService(EvolutionService evolutionService) {
		this.evolutionService = evolutionService;
	}

	public AcPerturbateTask getAcPerturbationTask() {
		AcPerturbateTask task = new AcPerturbateTask();
		copyTo(task);
		task.setRepetitions(getPerturbationRepetitions());
		task.setPerturbationStrength(getPerturbationStrength());
		return task;
	}

	public AcRunTranslateAndEvaluateTask getAcRunTranslationAndEvaluationTask() {
		AcRunTranslateAndEvaluateTask task = new AcRunTranslateAndEvaluateTask();
		copyTo(task);
		return task;
	}

	public AcRunAndTranslateTask getAcRunAndTranslationTask() {
		AcRunAndTranslateTask task = new AcRunAndTranslateTask();
		copyTo(task);
		return task;
	}

	public void copyTo(AcRunTranslateAndEvaluateTask acEvaluationTask) {
		AcRunAndTranslateTask acTranslationTask = new AcRunAndTranslateTask();
		copyTo(acTranslationTask);

		acEvaluationTask.setRunAndTranslationTaskDefinition(acTranslationTask);
		acEvaluationTask.setAcEvaluationId(getAcEvaluationId());
		acEvaluationTask.setEvaluationSteps(getEvaluationSteps());
		acEvaluationTask.setEvaluateFullFlag(isEvaluateFullRunFlag());
	}

	public void copyTo(AcRunAndTranslateTask acTranslationTask) {
		AcRunTask acRunTask = new AcRunTask();

		acRunTask.setCompartmentId(getCompartmentId());
		acRunTask.setSimulationConfigId(getSimulationConfigId());
		acRunTask.setInteractionSeriesId(getActionSeriesId());
		acRunTask.setRunTime(getRunTime());
		acRunTask.setRepetitions(getRepetitions());

		acTranslationTask.setRunTaskDefinition(acRunTask);
		acTranslationTask.setTranslationSeriesId(getTranslationSeriesId());
	}

	public static void main(String args[]) {
		CoelConsoleClient rcc = new CoelConsoleClient(args);
		// Set artificial chemistry service interface
		ApplicationContext ctx = new ClassPathXmlApplicationContext(SPRING_CONF_LOCATIONS); 
		rcc.setArtificialChemistryService((ArtificialChemistryService) ctx.getBean("artificialChemistryService"));
		rcc.setEvolutionService((EvolutionService) ctx.getBean("evolutionService"));
		// Run console client
		rcc.run();
	}
}