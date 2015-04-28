package edu.banda.coel.consoleclient;

import java.util.Collection;

import com.banda.chemistry.domain.IllegalStateEventHandling;
import com.banda.core.consoleclient.ConsoleClientOption;

/**
 * @author © Peter Banda
 * @since 2011
 */
public interface Options {

	ConsoleClientOption<Byte> jobType = new ConsoleClientOption<Byte>(
			"job", "job to perform",
			Byte.class, null,
			"the type of job to perform, out of the following ones: 0 - AC run\n" +
			"                                                                    1 - AC translation\n" +
			"                                                                    2 - AC run and translation\n" +
			"                                                                    3 - AC evaluation\n" +
			"                                                                    4 - AC run, translation and evaluation\n" +
			"                                                                    5 - AC evaluation average\n" +			
			"                                                                    6 - AC perturbation\n" +
			"                                                                    7 - AC perturbation results merge\n" +
			"                                                                    8 - Evo AC evalution",
			true);

	ConsoleClientOption<Byte> evoPopulationContentStoreOption = new ConsoleClientOption<Byte>(
			"eps", "evolution population content save option",
			Byte.class, null,
			"the population content to save after (while) evolution: 0 - Score and Fitness Only\n" +
			"                                                                  1 - Best Chromosome\n" +
			"                                                                  2 - Full");

	ConsoleClientOption<Byte> evoPopulationSelection = new ConsoleClientOption<Byte>(
			"eps", "evolution population selection save option",
			Byte.class, null,
			"the population to save after (while) evolution: 0 - Last population\n" +
			"                                                                1 - All populations");

	ConsoleClientOption<Byte> acUpperThresholdViolationHandling = new ConsoleClientOption<Byte>(
			"uth", "ac upper threshold violation handling",
			Byte.class, null,
			"type specifying what to do in case of upper threshold violation during AC run: " + IllegalStateEventHandling.Ignore.ordinal() + " - Ignore\n" +
			"                                                                     " + IllegalStateEventHandling.FixSilently.ordinal() + " - Fix silently\n" +
			"                                                                     " + IllegalStateEventHandling.FixAndCreateEvent.ordinal() + " - Fix and create event (not supported yet)\n" +
			"                                                                     " + IllegalStateEventHandling.Stop.ordinal() + " - Stop (not supported yet)\n" +
			"                                                                     " + IllegalStateEventHandling.ThrowException.ordinal() + " - Throw exception (not supported yet)\n");

	ConsoleClientOption<Byte> acZeroThresholdViolationHandling = new ConsoleClientOption<Byte>(
			"zth", "ac zero threshold violation handling",
			Byte.class, null,
			"type specifying what to do in case of zero threshold violation during AC run: " + IllegalStateEventHandling.Ignore.ordinal() + " - Ignore\n" +
			"                                                                     " + IllegalStateEventHandling.FixSilently.ordinal() + " - Fix silently\n" +
			"                                                                     " + IllegalStateEventHandling.FixAndCreateEvent.ordinal() + " - Fix and create event (not supported yet)\n" +
			"                                                                     " + IllegalStateEventHandling.Stop.ordinal() + " - Stop (not supported yet)\n" +
			"                                                                     " + IllegalStateEventHandling.ThrowException.ordinal() + " - Throw exception (not supported yet)\n");

	ConsoleClientOption<Byte> acNotANumberConcentrationHandling = new ConsoleClientOption<Byte>(
			"nnch", "ac not-a-number concentration event handling",
			Byte.class, null,
			"type specifying what to do in case of NaN concentration during AC run: " + IllegalStateEventHandling.Ignore.ordinal() + " - Ignore\n" +
			"                                                                     " + IllegalStateEventHandling.FixSilently.ordinal() + " - Fix silently\n" +
			"                                                                     " + IllegalStateEventHandling.FixAndCreateEvent.ordinal() + " - Fix and create event (not supported yet)\n" +
			"                                                                     " + IllegalStateEventHandling.Stop.ordinal() + " - Stop (not supported yet)\n" +
			"                                                                     " + IllegalStateEventHandling.ThrowException.ordinal() + " - Throw exception (not supported yet)\n");

	ConsoleClientOption<Long> artificialChemistryId = new ConsoleClientOption<Long>(
			"ac", "artificial chemistry id",
			Long.class, null,
			"the id of the artificial chemistry to run");

	ConsoleClientOption<Long> compartmentId = new ConsoleClientOption<Long>(
			"co", "compartment id",
			Long.class, null,
			"the id of the compartment to run");

	ConsoleClientOption<Long> simulationConfigId = new ConsoleClientOption<Long>(
			"sc", "simulation config id",
			Long.class, null,
			"the id of the simulation config to use");

	ConsoleClientOption<Long> actionSeriesId = new ConsoleClientOption<Long>(
			"as", "interaction series id",
			Long.class, null,
			"the id of interaction series defining simulation setup");

	ConsoleClientOption<Long> acEvaluationId = new ConsoleClientOption<Long>(
			"ae", "ac evaluation id",
			Long.class, null,
			"the id of evaluation defining the interpretation of AC simulation / translation");

	ConsoleClientOption<Long> evaluatedActionSeriesId = new ConsoleClientOption<Long>(
			"eas", "evaluted interaction series id",
			Long.class, null,
			"the id of evaluted interaction series defining simulation setup");

	ConsoleClientOption<Long> translationSeriesId = new ConsoleClientOption<Long>(
			"ts", "translation series id",
			Long.class, null,
			"the id of translation series defining interpretations of AC run");

	ConsoleClientOption<Long> acTaskId = new ConsoleClientOption<Long>(
			"at", "AC task id",
			Long.class, null,
			"the id of AC task that contains interaction series, translation series and expected run translation");

	ConsoleClientOption<Collection> runIds = new ConsoleClientOption<Collection>(
			"ru", "AC run id(s)",
			Collection.class, Long.class, null,
			"the ids of AC runs (AC simulation histories).");

	ConsoleClientOption<Collection> translatedRunIds = new ConsoleClientOption<Collection>(
			"tru", "AC translated run id(s)",
			Collection.class, Long.class, null,
			"the ids of AC translated runs.");

	ConsoleClientOption<Long> gaSettingId = new ConsoleClientOption<Long>(
			"ga", "GA setting id",
			Long.class, null,
			"the id of genetic algorithm");

	ConsoleClientOption<Long> evoTaskId = new ConsoleClientOption<Long>(
			"et", "Evolution task id",
			Long.class, null,
			"the id of evolution task");

	ConsoleClientOption<Collection> initChromosomeCode = new ConsoleClientOption<Collection>(
			"ich", "Initial chromosome code",
			Collection.class, Double.class, null,
			"Code of a chromosome to start evalution from.");

	ConsoleClientOption<Boolean> evoAutoSaveFlag = new ConsoleClientOption<Boolean>(
			"eas", "Auto save evolution flag",
			Boolean.class, Boolean.TRUE,
			"if set, runing evolution is autosaved after each generation");

	ConsoleClientOption<Integer> runTime = new ConsoleClientOption<Integer>(
			"sr", "run time",
			Integer.class, null,
			"the total time of a single AC run");

	ConsoleClientOption<Integer> translationSteps = new ConsoleClientOption<Integer>(
			"st", "translation steps",
			Integer.class, null,
			"the number of steps of a single AC translation");
	
	ConsoleClientOption<Integer> evaluationSteps = new ConsoleClientOption<Integer>(
			"se", "evaluation steps",
			Integer.class, null,
			"the number of steps of a single AC evaluation");

	ConsoleClientOption<Boolean> translateFullRunFlag = new ConsoleClientOption<Boolean>(
			"tf", "Translate full run flag",
			Boolean.class, Boolean.TRUE,
			"if set, the translation series will be applied to the whole run in case it's possible");

	ConsoleClientOption<Boolean> evaluateFullTranslationFlag = new ConsoleClientOption<Boolean>(
			"ef", "Evaluate full translation flag",
			Boolean.class, Boolean.TRUE,
			"if set, AC evaluation will be applied to the whole translation");

	ConsoleClientOption<Integer> repetitions = new ConsoleClientOption<Integer>(
			"r", "repetitions",
			Integer.class, new Integer(1),
			"the number of runs");

	ConsoleClientOption<Double> perturbationStrength = new ConsoleClientOption<Double>(
			"ps", "perturbation strength",
			Double.class, new Double(1),
			"the strength of perturbation defined as double from (0,1>");

	ConsoleClientOption<Integer> perturbationRepetitions = new ConsoleClientOption<Integer>(
			"pr", "perturbation repetitions",
			Integer.class, new Integer(1),
			"the number of perturbations");
}