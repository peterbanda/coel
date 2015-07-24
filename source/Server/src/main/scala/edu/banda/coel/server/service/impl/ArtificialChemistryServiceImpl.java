package edu.banda.coel.server.service.impl;

import java.lang.InterruptedException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.banda.serverbase.grid.ArgumentCallable;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.banda.core.domain.DomainObject.DomainObjectKeyComparator;

import com.banda.chemistry.business.AcRateConstantUtil;
import com.banda.chemistry.business.AcReactionRatePerturbationBO;
import com.banda.chemistry.business.AcReplicator;
import com.banda.chemistry.business.ArtificialChemistryUtil;
import com.banda.chemistry.business.ChemistryRunnableFactory;
import com.banda.chemistry.business.factory.AcCompartmentFactory;
import com.banda.chemistry.business.factory.AcReactionSetFactory;
import com.banda.chemistry.business.factory.AcSpeciesSetFactory;
import com.banda.chemistry.domain.AcCompartment;
import com.banda.chemistry.domain.AcDNAStrandSpeciesSet;
import com.banda.chemistry.domain.AcEvaluatedPerformance;
import com.banda.chemistry.domain.AcEvaluatedRun;
import com.banda.chemistry.domain.AcEvaluation;
import com.banda.chemistry.domain.AcInteractionSeries;
import com.banda.chemistry.domain.AcMultiRunAnalysisResult;
import com.banda.chemistry.domain.AcParameter;
import com.banda.chemistry.domain.AcParameterSet;
import com.banda.chemistry.domain.AcPerturbationPerformance;
import com.banda.chemistry.domain.AcRandomRatePerformance;
import com.banda.chemistry.domain.AcRateConstantTypeBound;
import com.banda.chemistry.domain.AcReaction;
import com.banda.chemistry.domain.AcReactionSet;
import com.banda.chemistry.domain.AcSimulationConfig;
import com.banda.chemistry.domain.AcSpecies;
import com.banda.chemistry.domain.AcSpeciesSet;
import com.banda.chemistry.domain.AcTranslatedRun;
import com.banda.chemistry.domain.AcTranslationSeries;
import com.banda.chemistry.domain.ArtificialChemistry;
import com.banda.chemistry.domain.ArtificialChemistrySpec;
import com.banda.core.Pair;
import com.banda.core.domain.ComponentRunTrace;
import com.banda.core.domain.um.User;
import com.banda.core.reflection.GenericReflectionProvider;
import com.banda.core.util.ObjectUtil;
import com.banda.function.domain.Function;
import com.banda.math.business.JavaMathUtil;
import com.banda.math.business.rand.RandomDistributionProvider;
import com.banda.math.business.rand.RandomDistributionProviderFactory;
import com.banda.math.domain.Stats;
import com.banda.math.domain.StatsSequence;
import com.banda.math.domain.StatsType;
import com.banda.math.domain.dynamics.MultiRunAnalysisSpec;
import com.banda.math.domain.dynamics.SingleRunAnalysisResult;
import com.banda.math.domain.dynamics.SingleRunAnalysisResultType;
import com.banda.math.domain.dynamics.SingleRunAnalysisSpec;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.CoelRuntimeException;
import edu.banda.coel.core.util.SBMLConverter;
import edu.banda.coel.domain.service.ArtificialChemistryService;
import edu.banda.coel.server.dao.AcMultiRunAnalysisResultDAO;
import edu.banda.coel.server.dao.ArtificialChemistryDAO;
import edu.banda.coel.server.dao.SingleRunAnalysisResultDAO;
import edu.banda.coel.server.grid.callable.GridDynamicsAnalysisCall;
import edu.banda.coel.server.grid.callable.GridRerunDerridaAnalysisCall;
import edu.banda.coel.task.chemistry.AcEvaluateTask;
import edu.banda.coel.task.chemistry.AcMultiRunAnalysisTask;
import edu.banda.coel.task.chemistry.AcPerformanceEvaluateTask;
import edu.banda.coel.task.chemistry.AcPerturbateTask;
import edu.banda.coel.task.chemistry.AcPerturbationPerformanceEvaluateTask;
import edu.banda.coel.task.chemistry.AcRandomRatePerformanceEvaluateTask;
import edu.banda.coel.task.chemistry.AcRunAndTranslateTask;
import edu.banda.coel.task.chemistry.AcRunTask;
import edu.banda.coel.task.chemistry.AcRunTranslateAndEvaluateTask;
import edu.banda.coel.task.chemistry.AcRunTranslateAndMultiEvaluateTask;
import edu.banda.coel.task.chemistry.AcTaskParts.AcCompartmentHolder;
import edu.banda.coel.task.chemistry.AcTaskParts.AcEvaluationHolder;
import edu.banda.coel.task.chemistry.AcTaskParts.AcInteractionSeriesHolder;
import edu.banda.coel.task.chemistry.AcTaskParts.AcMultipleCompartmentHolder;
import edu.banda.coel.task.chemistry.AcTaskParts.AcMultipleEvaluationHolder;
import edu.banda.coel.task.chemistry.AcTaskParts.AcMultipleRateConstantTypeBoundHolder;
import edu.banda.coel.task.chemistry.AcTaskParts.AcMultipleTranslationSeriesHolder;
import edu.banda.coel.task.chemistry.AcTaskParts.AcSimulationConfigHolder;
import edu.banda.coel.task.chemistry.AcTaskParts.AcTranslationSeriesHolder;
import edu.banda.coel.task.chemistry.AcTaskParts.MultiRunAnalysisSpecHolder;
import edu.banda.coel.task.chemistry.AcTranslatedRunEvaluationTask;

/**
 * Title: ArtificialChemistryServiceImpl
 *
 * @see edu.banda.coel.domain.service.ArtificialChemistryService
 * @author Peter Banda
 * @since 2011
 */
class ArtificialChemistryServiceImpl extends AbstractService implements ArtificialChemistryService {

	/**
	 * Data Access Objects used by the service methods in this class to access
	 * persistent layer.
	 */
	@Autowired
	private ArtificialChemistryDAO artificialChemistryDAO;
	@Autowired
	private GenericDAO<AcSpeciesSet, Long> acSpeciesSetDAO;
	@Autowired
	private GenericDAO<AcParameterSet, Long> acParameterSetDAO;
	@Autowired
	private GenericDAO<AcParameter, Long> acParameterDAO;
	@Autowired
	private GenericDAO<AcCompartment, Long> acCompartmentDAO;
	@Autowired
	private GenericDAO<AcInteractionSeries, Long> acInteractionSeriesDAO;
	@Autowired
	private GenericDAO<AcReactionSet, Long> acReactionSetDAO;
	@Autowired
	private GenericDAO<Function<?,?>, Long> functionDAO;
	@Autowired
	private GenericDAO<AcTranslationSeries, Long> acTranslationSeriesDAO;
	@Autowired
	private GenericDAO<AcEvaluation, Long> acEvaluationDAO;
	@Autowired
	private GenericDAO<User, Long> userDAO;
	@Autowired
	private GenericDAO<AcEvaluatedPerformance, Long> acEvaluatedPerformanceDAO;
	@Autowired
	private GenericDAO<AcRateConstantTypeBound, Long> acRateConstantTypeBoundDAO;
	@Autowired
	private GenericDAO<SingleRunAnalysisSpec, Long> singleRunAnalysisSpecDAO;
	@Autowired
	private GenericDAO<MultiRunAnalysisSpec<Double>, Long> multiRunAnalysisSpecDAO;
	@Autowired
	private SingleRunAnalysisResultDAO singleRunAnalysisResultDAO;
	@Autowired
	private AcMultiRunAnalysisResultDAO acMultiRunAnalysisResultDAO;
	@Autowired
	private GenericDAO<ArtificialChemistrySpec, Long> artificialChemistrySpecDAO;
	@Autowired
	private GenericDAO<StatsSequence, Long> statsSequenceDAO;
	@Autowired
	private GenericDAO<AcSimulationConfig, Long> acSimulationConfigDAO;

	@Autowired
	private ChemistryRunnableFactory chemistryRunnableFactory;

	@Autowired
	private GenericReflectionProvider genericReflectionProvider;

	// tasks
	private ArgumentCallable<AcRunTask, ComponentRunTrace<Double, Pair<AcCompartment, AcSpecies>>> chemistryRunCall;
	private ArgumentCallable<AcRunAndTranslateTask, AcTranslatedRun> chemistryRunAndInterpretCall;

	private ArgumentCallable<AcTranslatedRunEvaluationTask, AcEvaluatedRun> acEvaluateCall;

	/**
	 * Grid wrapper provides executions of predefined jobs on the computational grid.  
	 */
	private Integer dynamicsAnalysisTasksInParallel;

	private AcReplicator replicator = AcReplicator.getInstance();
	private ArtificialChemistryUtil acUtil = ArtificialChemistryUtil.getInstance();
	private AcRateConstantUtil acRateUtil = AcRateConstantUtil.getInstance();
	private AcCompartmentFactory compartmentFactory = new AcCompartmentFactory(
			AcReactionSetFactory.getInstance(), AcSpeciesSetFactory.getInstance());

	//////////////
	// SERVICES //
	//////////////

	/** 
	 * @see edu.banda.coel.domain.service.ArtificialChemistryService#runSimulation(AcRunTask)
	 */
	@Override
	@Transactional(readOnly = true)
	@Async
	public Future<Collection<ComponentRunTrace<Double, Pair<AcCompartment, AcSpecies>>>> runSimulationAsync(AcRunTask task) {
		return new AsyncResult<Collection<ComponentRunTrace<Double,Pair<AcCompartment,AcSpecies>>>>(runSimulation(task));
	}

	@Override
	@Transactional(readOnly = true)
	@Async
	public Future<Collection<AcTranslatedRun>> runAndTranslateAsync(AcRunAndTranslateTask task) {
		return new AsyncResult<Collection<AcTranslatedRun>>(runAndTranslate(task));
	}

	/** 
	 * @see edu.banda.coel.domain.service.ArtificialChemistryService#runSimulation(AcRunTask)
	 */
	@Override
	@Transactional(readOnly = true)
	public Collection<ComponentRunTrace<Double, Pair<AcCompartment, AcSpecies>>> runSimulation(AcRunTask task) {
		log.info("Simulation for AC compartment '" + task.getCompartmentId() + "', AC interaction series '" + task.getInteractionSeriesId() + "' is about to be executed.");

		// Initialize task definition
		initCompartmentIfNeeded(task);
		initSimulationConfigIfNeeded(task);
		initActionSeriesIfNeeded(task);
		if (!task.isCompartmentDefined()) {
    		throw new CoelRuntimeException("Compartment missing for AC simulation task.");
		}

		Collection<ComponentRunTrace<Double, Pair<AcCompartment, AcSpecies>>> runTraces = null;
		try {
    		// Run on computational grid or locally
    		final int repetitions = task.getRepetitions();
			runTraces = task.isRunOnGrid() ?
    				computationalGrid.runOnGridSync(chemistryRunCall, task, repetitions, task.getJobsInSequenceNum(), task.getMaxJobsInParallelNum()) :
        			computationalGrid.runSerialLocal(chemistryRunCall, task, repetitions);

		} catch (Exception exception) {
			handleAndWrapSqlException(exception, "Problem occured in ArtificialChemistryService.runSimulation method.");
		}
		log.info("Simulation for AC compartment '" + task.getCompartmentId() + "', AC interaction series '" + task.getInteractionSeriesId() + "' finished.");
		return runTraces;
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<AcTranslatedRun> runAndTranslate(AcRunAndTranslateTask task) {
		log.info("AC run and translation for AC compartment '" + task.getCompartmentId() + "', interaction series '" + task.getActionSeriesId() + "', and translation series '" + task.getTranslationSeriesId() + "' is about to be executed.");
		Collection<AcTranslatedRun> translatedRuns = null;
	
		// Initialize task definition
		initRunAndTranslationTaskIfNeeded(task);

    	try {
    		// Run on computational grid or locally
    		final int repetitions = task.getRunTaskDefinition().getRepetitions();
    		translatedRuns = task.isRunOnGrid() ?
    				computationalGrid.runOnGridSync(chemistryRunAndInterpretCall, task, repetitions, task.getJobsInSequenceNum(), task.getMaxJobsInParallelNum()) :
        			computationalGrid.runSerialLocal(chemistryRunAndInterpretCall, task, repetitions);

		} catch (Exception exception) {
			handleAndWrapSqlException(exception, "Problem occured in ArtificialChemistryService.runSimulationAndTranslate method.");
		}
		log.info("AC run and translation for AC compartment '" + task.getCompartmentId() + "', interaction series '" + task.getActionSeriesId() + "', and translation series '" + task.getTranslationSeriesId() + "' finished.");
		return translatedRuns;
	}

	/**
	 * @see edu.banda.coel.domain.service.ArtificialChemistryService#evaluateRunWithResult(AcEvaluateTask)
	 */
	@Override
	@Transactional(readOnly = true)
	public Collection<AcEvaluatedRun> evaluateRunWithResult(AcEvaluateTask task) {
		log.info("AC evaluation for AC compartment '" + task.getCompartmentId() + "' and AC evaluation '" + task.getAcEvaluationId() + "' is about to be executed.");
		Collection<AcEvaluatedRun> evaluatedRuns = null;
		Set<AcTranslatedRun> translatedRuns = new HashSet<AcTranslatedRun>();

		// Initialize task definition	
		initAcEvaluationIfNeeded(task);

		if (task.areTranslatedRunsDefined()) {
			translatedRuns.addAll(task.getTranslatedRuns());
		}
		initCompartmentIfNeeded(task);
		initSimulationConfigIfNeeded(task);
		initTranslationSeriesIfNeeded(task);

		task.setTranslatedRuns(translatedRuns);

		if (task.getTranslatedRuns().isEmpty()) {
			throw new CoelRuntimeException("At least one AC translated run is required for AC evaluation task.");
    	}
    	if (!task.isAcEvaluationDefined()) {
    		throw new CoelRuntimeException("AC evaluation missing for AC evaluation task.");
    	}

    	try {
    		// Run on computationalGrid
    		evaluatedRuns = computationalGrid.runOnGridSync(
    				acEvaluateCall, AcTranslatedRunEvaluationTask.createInstances(task),
    				task.getJobsInSequenceNum(), task.getMaxJobsInParallelNum());
		} catch (Exception exception) {
			handleAndWrapSqlException(exception, "Problem occured in ArtificialChemistryService.evaluateRunWithResult method.");
		}
		return evaluatedRuns;
	}

	private void initRunAndTranslationTaskIfNeeded(AcRunAndTranslateTask task) {
		AcRunTask runTask = task.getRunTaskDefinition();
		
		initCompartmentIfNeeded(runTask);
		initSimulationConfigIfNeeded(runTask);
		initActionSeriesIfNeeded(runTask);
		initTranslationSeriesIfNeeded(task);
    	if (!runTask.isCompartmentDefined()) {
        	throw new CoelRuntimeException("AC compartment missing for AC run and translation task.");
    	}
    	if (!task.isTranslationSeriesDefined()) {
    		throw new CoelRuntimeException("Translation series missing for AC run and translation task.");
    	}
	}

	/**
	 * @see edu.banda.coel.domain.service.ArtificialChemistryService#runSimulationTranslateAndEvaluate(AcRunTranslateAndEvaluateTask)
	 */
	@Override
	@Transactional(readOnly = false)
	public void runSimulationTranslateAndEvaluate(AcRunTranslateAndEvaluateTask task) {
		runSimulationTranslateAndEvaluateWithResult(task);
	}

	/**
	 * @see edu.banda.coel.domain.service.ArtificialChemistryService#runSimulationTranslateAndEvaluateWithResult(AcRunTranslateAndEvaluateTask)
	 */
	@Override
	@Transactional(readOnly = false)
	public Collection<AcEvaluatedRun> runSimulationTranslateAndEvaluateWithResult(AcRunTranslateAndEvaluateTask task) {
		AcRunAndTranslateTask runAndTranslationTask = task.getRunAndTranslationTaskDefinition();

		// run AC simulations and obtain translated runs
		Collection<AcTranslatedRun> translatedRuns = runAndTranslate(runAndTranslationTask);

		// prepare AC evaluation task
		AcEvaluateTask evaluationTask = new AcEvaluateTask();
		evaluationTask.copyFrom(task);
		evaluationTask.setTranslatedRuns(translatedRuns);

		// run evaluation
		return evaluateRunWithResult(evaluationTask);		
	}

	@Transactional(readOnly = false)
	public Map<AcEvaluation, Collection<AcEvaluatedRun>> runSimulationTranslateAndEvaluateWithResult(AcRunTranslateAndMultiEvaluateTask task) {
		AcRunAndTranslateTask runAndTranslationTask = task.getRunAndTranslationTaskDefinition();

		// run AC simulations and obtain translated runs
		final Collection<AcTranslatedRun> translatedRuns = runAndTranslate(runAndTranslationTask);

		Map<AcEvaluation, Collection<AcEvaluatedRun>> evaluatedRunsMap = new HashMap<AcEvaluation, Collection<AcEvaluatedRun>>();

		for (AcEvaluation evaluation : task.getAcEvaluations()) {
			// prepare AC evaluation task
			AcEvaluateTask evaluationTask = new AcEvaluateTask();
			evaluationTask.setAcEvaluation(evaluation);
			evaluationTask.setEvaluationSteps(task.getEvaluationSteps());
			evaluationTask.setEvaluateFullFlag(task.isEvaluateFullFlag());
			evaluationTask.setJobsInSequenceNum(task.getJobsInSequenceNum());
			evaluationTask.setMaxJobsInParallelNum(task.getMaxJobsInParallelNum());

			evaluationTask.setTranslatedRuns(translatedRuns);
		
			// run evaluation
			Collection<AcEvaluatedRun> evaluatedRuns = evaluateRunWithResult(evaluationTask);
			evaluatedRunsMap.put(evaluation, evaluatedRuns);
		}
		return evaluatedRunsMap;
	}

	/**
	 * @see edu.banda.coel.domain.service.ArtificialChemistryService#saveSbmlModelAsArtificialChemistry(String, String)
	 */
	@Override
	@Transactional(readOnly = false)
    public void saveSbmlModelAsArtificialChemistry(String sbmlString, String acName) {
		SBMLConverter sbmlConverter = SBMLConverter.getInstance();
		ArtificialChemistry ac = sbmlConverter.convertSBMLStringToAlChemistry(sbmlString);

		User user = userDAO.get(new Long(1));
		saveCompartmentWithAllData(ac.getSkinCompartment(), user);

//		for (AcReaction reaction : reactionSet.getReactions()) {
//			reaction.setForwardRateFunction((Function<Double, Double>) functionDAO.save(reaction.getForwardRateFunction()));
//		}
//
//		for (AcParameter parameter : ac.getSkinCompartment().getParameters()) {
//			parameter.setEvolFunction((Function<Double, Double>) functionDAO.save(parameter.getEvolFunction()));
//		}
    }


	private Double[] getOverallAveragedEvaluatedRun(Collection<AcEvaluatedRun> acEvaluatedRuns) {
		final int evaluatedRunsNum = acEvaluatedRuns.size();
		log.info("Overall average for '" + evaluatedRunsNum + "' evaluated runs is about to be executed.");

		if (acEvaluatedRuns.isEmpty()) {
			return null;
		}
			
		// initialization
		final int evaluatedValuesNum = ObjectUtil.getFirst(acEvaluatedRuns).getEvaluatedValues().length;
		Double[] averages = new Double[evaluatedValuesNum];
		Arrays.fill(averages, new Double(0d));

		int[] counts = new int[evaluatedValuesNum];

		// summing
		for (AcEvaluatedRun evaluatedRun : acEvaluatedRuns) {
			final Double[] values = evaluatedRun.getEvaluatedValues();
			if (values.length != evaluatedValuesNum) {
				throw new CoelRuntimeException("AC evaluation of different size found, but uniform expected - '" + evaluatedValuesNum + "' vs '" + values.length);
			}
			for (int index = 0; index < evaluatedValuesNum; index++)
				if (!values[index].isNaN() && !values[index].isInfinite()) {
					averages[index] += values[index];
					counts[index]++;
				}
		}

		// average
		for (int index = 0; index < evaluatedValuesNum; index++) {
			averages[index] /= counts[index];
			if (counts[index] < evaluatedRunsNum) {
				log.warn("NaN or infinite eval value found at index " + index + ". " + counts[index] + " out of " + evaluatedRunsNum + " correct.");
			}
		}

		return averages;	
	}

	@Override  
	@Transactional(readOnly = true)
	public Double[] runPerturbationAnalysis(AcPerturbateTask task) {
		AcRunAndTranslateTask runAndTranslationTask = task.getRunAndTranslationTaskDefinition();
		log.info("AC perturbation analysis for AC compartment '" + runAndTranslationTask.getCompartmentId() + "' and perturbation strength '" + task.getPerturbationStrength() + "' is about to be executed.");

		// initialize task definition
		initRunAndTranslationTaskIfNeeded(runAndTranslationTask);
		if (task.getPerturbationStrength() == null) {
			throw new CoelRuntimeException("Perturbation strength must be defined for AC perturbation task.");
		}
		if (task.getRepetitions() == null) {
			throw new CoelRuntimeException("The number of repetitions must be defined for AC perturbation task.");
		}

		final AcCompartment originalCompartment = runAndTranslationTask.getCompartment();
		Collection<AcCompartment> compartments = new ArrayList<AcCompartment>();
		for (int i = 0; i < task.getRepetitions(); i++) {
			// perturbate reaction rates of compartment
			AcCompartment perturbatedCompartment = replicator.cloneCompartmentWithReactionSet(originalCompartment);
			new AcReactionRatePerturbationBO(perturbatedCompartment.getReactionSet(), task.getPerturbationStrength()).perturbate();
			compartments.add(perturbatedCompartment);
		}

		Collection<AcEvaluatedRun> evaluatedRuns = runSimulationTranslateAndEvaluateWithResult(compartments, task);
		return getOverallAveragedEvaluatedRun(evaluatedRuns);
	}

	private Collection<AcEvaluatedRun> runSimulationTranslateAndEvaluateWithResult(
		Collection<AcCompartment> compartments,
		AcRunTranslateAndEvaluateTask allInOneTask
	) {
		final AcRunAndTranslateTask runAndTranslationTask = allInOneTask.getRunAndTranslationTaskDefinition();
		initRunAndTranslationTaskIfNeeded(runAndTranslationTask);

		Collection<Future<Collection<AcTranslatedRun>>> translatedRunsGgs = new ArrayList<Future<Collection<AcTranslatedRun>>>();
		for (AcCompartment compartment : compartments) {
			// run asynchronously on computationalGrid
			AcRunAndTranslateTask runAndTranslationTaskCopy = new AcRunAndTranslateTask();
			runAndTranslationTaskCopy.copyFrom(allInOneTask.getRunAndTranslationTaskDefinition());
			runAndTranslationTaskCopy.getRunTaskDefinition().setCompartment(compartment);
			// collect results
			final int repetitions = runAndTranslationTaskCopy.getRunTaskDefinition().getRepetitions();
			final Future<Collection<AcTranslatedRun>> results = computationalGrid.runOnGridAsync(
					chemistryRunAndInterpretCall, runAndTranslationTaskCopy, repetitions, 1);
			translatedRunsGgs.add(results);
		}

		// unpack async results
		Collection<AcTranslatedRun> translatedRuns = new ArrayList<AcTranslatedRun>();
		try {
			for (Future<Collection<AcTranslatedRun>> translatedRunsGf : translatedRunsGgs) {
				translatedRuns.addAll(translatedRunsGf.get());
			}
		} catch (ExecutionException e) {
			throw new CoelRuntimeException("Asynchronous 'AC Run and Translation' job failed on grid.", e);
		} catch (InterruptedException e) {
            throw new CoelRuntimeException("Asynchronous 'AC Run and Translation' job failed on grid.", e);
        }

		// prepare AC evaluation task
		AcEvaluateTask evaluationTask = new AcEvaluateTask();
		evaluationTask.copyFrom(allInOneTask);
		evaluationTask.setTranslatedRuns(translatedRuns);

		return evaluateRunWithResult(evaluationTask);
	}

	@Override
	public void runPerformanceEvaluation(AcPerformanceEvaluateTask task) {
		log.info("AC performance evaluation for AC compartment '" + task.getCompartmentId() + "' and evaluations '" + task.getAcEvaluationIds() + "' is about to be executed.");
		initMultipleAcEvaluationsIfNeeded(task);
    	if (task.getRepetitions() == null) {
    		throw new CoelRuntimeException("Repetitions missing for ac random rate performance evaluation task.");
    	}

		List<AcInteractionSeries> sortedActionSeries = new ArrayList<AcInteractionSeries>(task.getActionSeries());
		Collections.sort(sortedActionSeries, new AcInteractionSeriesIdComparator());
		List<AcEvaluation> sortedEvaluations = new ArrayList<AcEvaluation>(task.getAcEvaluations());
		Collections.sort(sortedEvaluations, new DomainObjectKeyComparator<Long>());

		Map<AcTranslationSeries, List<AcEvaluation>> translationSeriesEvaluationMap = new HashMap<AcTranslationSeries, List<AcEvaluation>>();
		for (AcEvaluation evaluation : sortedEvaluations) {
			List<AcEvaluation> evaluations = translationSeriesEvaluationMap.get(evaluation.getTranslationSeries());
			if (evaluations == null) {
				evaluations = new ArrayList<AcEvaluation>();
				translationSeriesEvaluationMap.put(evaluation.getTranslationSeries(), evaluations);
			}
			evaluations.add(evaluation);
		}

		for (final AcInteractionSeries actionSeries : sortedActionSeries)
			for (List<AcEvaluation> sharedTranslationEvaluations : translationSeriesEvaluationMap.values())
				try {
					runPerformanceEvaluation(task, actionSeries, sharedTranslationEvaluations);
				} catch (Exception e) {
					log.error("Performance evaluation for AC compartment '" + task.getCompartmentId() + "' and interaction series '" + actionSeries.getId() + "' failed!", e);
				}
	}

	@Override
	public void runRandomRatePerformanceEvaluation(AcRandomRatePerformanceEvaluateTask task) {
		log.info("AC random rate performance evaluation for AC compartment '" + task.getCompartmentId() + "' and evaluations '" + task.getAcEvaluationIds() + "' is about to be executed.");
		initCompartmentIfNeeded(task);
		initSimulationConfigIfNeeded(task);
		initMultipleRateConstantTypeBoundIfNeeded(task);

    	if (task.getRepetitions() == null) {
    		throw new CoelRuntimeException("Repetitions missing for ac random rate performance evaluation task.");
    	}
    	if (task.getRandomRateGenerationNum() == null) {
    		throw new CoelRuntimeException("Random rate generation number missing for ac random rate performance evaluation task.");
    	}

		List<AcInteractionSeries> sortedActionSeries = new ArrayList<AcInteractionSeries>(task.getActionSeries());
		Collections.sort(sortedActionSeries, new AcInteractionSeriesIdComparator());
		List<AcEvaluation> sortedEvaluations = new ArrayList<AcEvaluation>(task.getAcEvaluations());
		Collections.sort(sortedEvaluations, new DomainObjectKeyComparator<Long>());

		for (final AcInteractionSeries actionSeries : sortedActionSeries)
			for (final AcEvaluation evaluation : sortedEvaluations)
				runRandomRatePerformanceEvaluation(task, actionSeries, evaluation);
	}

	@Override
	public void runPerturbationPerformanceEvaluation(AcPerturbationPerformanceEvaluateTask task) {
		log.info("AC performance evaluation for AC compartment '" + task.getCompartmentId() + "', evaluations '" + task.getAcEvaluationIds() + "', and perturbation stengths '" + task.getPerturbationStrengths() + "' is about to be executed.");
		initMultipleAcEvaluationsIfNeeded(task);
		initCompartmentIfNeeded(task);
		initSimulationConfigIfNeeded(task);

    	if (task.getRepetitions() == null) {
    		throw new CoelRuntimeException("Repetitions missing for ac perturbation performance evaluation task.");
    	}
    	if (task.getPerturbationNum() == null) {
    		throw new CoelRuntimeException("The number of perturbation missing for ac perturbation performance evaluation task.");
    	}

		List<AcInteractionSeries> sortedActionSeries = new ArrayList<AcInteractionSeries>(task.getActionSeries());
		Collections.sort(sortedActionSeries, new AcInteractionSeriesIdComparator());
		List<AcEvaluation> sortedEvaluations = new ArrayList<AcEvaluation>(task.getAcEvaluations());
		Collections.sort(sortedEvaluations, new DomainObjectKeyComparator<Long>());

		for (Double perturbationStrength : task.getPerturbationStrengths())
			for (final AcInteractionSeries actionSeries : sortedActionSeries)
				for (final AcEvaluation evaluation : sortedEvaluations)
					runPerturbationEvaluation(task, actionSeries, evaluation, perturbationStrength);
	}

	@Async
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Override
	public void runPerformanceEvaluation(
		AcPerformanceEvaluateTask task,
		AcInteractionSeries actionSeries,
		List<AcEvaluation> sharedTranslationEvaluations
	) {
		// create a 'run, translate and evaluate task'
		final AcRunTranslateAndMultiEvaluateTask acEvaluationTask = createAllInOneTask(task, actionSeries, sharedTranslationEvaluations);

		// run the task and get evaluated runs
		Map<AcEvaluation, Collection<AcEvaluatedRun>> evaluatedRuns = runSimulationTranslateAndEvaluateWithResult(acEvaluationTask);

		for (Entry<AcEvaluation, Collection<AcEvaluatedRun>> evaluationRunsPairs : evaluatedRuns.entrySet()) {
			// calculate average
			Double[] overallAveragedEvaluatedRun = getOverallAveragedEvaluatedRun(evaluationRunsPairs.getValue());

			// create a new evaluated performance and fill it with data
			AcEvaluatedPerformance performance = new AcEvaluatedPerformance();
			performance.setTimeCreated(new Date()); 
			performance.setCompartment(task.getCompartment());
			performance.setSimulationConfig(task.getSimulationConfig());
			performance.setActionSeries(actionSeries);
			performance.setEvaluation(evaluationRunsPairs.getKey());
			performance.setRepetitions(task.getRepetitions());
			performance.setAveragedCorrectRates(overallAveragedEvaluatedRun);
			performance.setLength(overallAveragedEvaluatedRun.length);

			User user = new User();
			user.setId(new Long(1));
			performance.setCreatedBy(user);

			final AcEvaluatedPerformance savedPerformance = acEvaluatedPerformanceDAO.save(performance);
			postAndLogSaveMessage(savedPerformance, "AC evaluated performance");
		}
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private void runRandomRatePerformanceEvaluation(
		AcRandomRatePerformanceEvaluateTask task,
		AcInteractionSeries actionSeries,
		AcEvaluation evaluation
	) {
		final AcCompartment originalCompartment = task.getCompartment();

		Collection<AcCompartment> compartments = new ArrayList<AcCompartment>();
		for (int i = 0; i < task.getRandomRateGenerationNum(); i++) {
			AcCompartment compartmentClone = replicator.cloneCompartmentWithReactionSet(originalCompartment);
			acRateUtil.setRandomRateConstants(compartmentClone.getReactionSet(), task.getReactionRateConstantTypeBoundMap());

			compartments.add(compartmentClone);
		}

		// create a 'run, translate and evaluate task'
		final AcRunTranslateAndEvaluateTask acEvaluationTask = createAllInOneTask(task, actionSeries, evaluation);

		// run the task for ACs with randomly generated rates, and get evaluated runs
		Collection<AcEvaluatedRun> evaluatedRuns = runSimulationTranslateAndEvaluateWithResult(compartments, acEvaluationTask);

		// calculate average
		Double[] overallAveragedEvaluatedRun = getOverallAveragedEvaluatedRun(evaluatedRuns);

		// create a new random rate performance holder and fill it with data
		AcRandomRatePerformance performance = new AcRandomRatePerformance();
		performance.setTimeCreated(new Date());
		performance.setCompartment(task.getCompartment());
		performance.setSimulationConfig(task.getSimulationConfig());
		performance.setActionSeries(actionSeries);
		performance.setEvaluation(evaluation);
		performance.setRepetitions(task.getRepetitions());
		performance.setRandomRateGenerationNum(task.getRandomRateGenerationNum());
		performance.setAveragedCorrectRates(overallAveragedEvaluatedRun);
		performance.setLength(overallAveragedEvaluatedRun.length);
		performance.setRateConstantTypeBounds(task.getRateConstantTypeBounds());

		User user = new User();
		user.setId(new Long(1));
		performance.setCreatedBy(user);

		final AcRandomRatePerformance savedPerformance = (AcRandomRatePerformance) acEvaluatedPerformanceDAO.save(performance);
		postAndLogSaveMessage(savedPerformance, "AC random rate performance");
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private void runPerturbationEvaluation(
		AcPerturbationPerformanceEvaluateTask task,
		AcInteractionSeries actionSeries,
		AcEvaluation evaluation,
		Double perturbationStrength
	) {
		final AcCompartment originalCompartment = task.getCompartment();

		Collection<AcCompartment> compartments = new ArrayList<AcCompartment>();
		for (int i = 0; i < task.getPerturbationNum(); i++) {
			AcCompartment compartmentClone = replicator.cloneCompartmentWithReactionSet(originalCompartment);
			new AcReactionRatePerturbationBO(compartmentClone.getReactionSet(), perturbationStrength).perturbate();
			compartments.add(compartmentClone);
		}

		// create a 'run, translate and evaluate task'
		final AcRunTranslateAndEvaluateTask acEvaluationTask = createAllInOneTask(task, actionSeries, evaluation);

		// run the task for ACs with randomly generated rates, and get evaluated runs
		Collection<AcEvaluatedRun> evaluatedRuns = runSimulationTranslateAndEvaluateWithResult(compartments, acEvaluationTask);

		// calculate average
		Double[] overallAveragedEvaluatedRun = getOverallAveragedEvaluatedRun(evaluatedRuns);

		// create a new random rate performance holder and fill it with data
		AcPerturbationPerformance performance = new AcPerturbationPerformance();
		performance.setTimeCreated(new Date());
		performance.setCompartment(task.getCompartment());
		performance.setSimulationConfig(task.getSimulationConfig());
		performance.setActionSeries(actionSeries);
		performance.setEvaluation(evaluation);
		performance.setRepetitions(task.getRepetitions());
		performance.setPerturbationNum(task.getPerturbationNum());
		performance.setAveragedCorrectRates(overallAveragedEvaluatedRun);
		performance.setLength(overallAveragedEvaluatedRun.length);
		performance.setPerturbationStrength(perturbationStrength);

		User user = new User();
		user.setId(new Long(1));
		performance.setCreatedBy(user);

		final AcPerturbationPerformance savedPerformance = (AcPerturbationPerformance) acEvaluatedPerformanceDAO.save(performance);
		postAndLogSaveMessage(savedPerformance, "AC perturbation performance");
	}

	private AcRunTranslateAndEvaluateTask createAllInOneTask(
		AcPerformanceEvaluateTask task,
		AcInteractionSeries actionSeries,
		AcEvaluation evaluation
	) {
//		initAcEvaluationIfNeeded(task);
		// AC run task
		AcRunTask acRunTask = new AcRunTask();
		acRunTask.setCompartment(task.getCompartment());
		acRunTask.setSimulationConfig(task.getSimulationConfig());
		acRunTask.setInteractionSeries(actionSeries);
		acRunTask.setRunTime(task.getRunTime());
		acRunTask.setRepetitions(task.getRepetitions());

		// AC translation task
		AcRunAndTranslateTask acTranslationTask = new AcRunAndTranslateTask();
		acTranslationTask.setRunTaskDefinition(acRunTask);
		acTranslationTask.setTranslationSeries(evaluation.getTranslationSeries());

		// AC evaluation task
		AcRunTranslateAndEvaluateTask acEvaluationTask = new AcRunTranslateAndEvaluateTask();
		acEvaluationTask.setRunAndTranslationTaskDefinition(acTranslationTask);
		acEvaluationTask.setAcEvaluation(evaluation);
		acEvaluationTask.setEvaluateFullFlag(true);

		return acEvaluationTask;
	}

	private AcRunTranslateAndMultiEvaluateTask createAllInOneTask(
		AcPerformanceEvaluateTask task,
		AcInteractionSeries actionSeries,
		Collection<AcEvaluation> evaluations
	) {
		// assume translation series is shared
		final AcTranslationSeries sharedTranslationSeries = ObjectUtil.getFirst(evaluations).getTranslationSeries();

		// AC run task
		AcRunTask acRunTask = new AcRunTask();
		acRunTask.setCompartment(task.getCompartment());
		acRunTask.setSimulationConfig(task.getSimulationConfig());
		acRunTask.setInteractionSeries(actionSeries);
		acRunTask.setRunTime(task.getRunTime());
		acRunTask.setRepetitions(task.getRepetitions());

		// AC translation task
		AcRunAndTranslateTask acTranslationTask = new AcRunAndTranslateTask();
		acTranslationTask.setRunTaskDefinition(acRunTask);
		acTranslationTask.setTranslationSeries(sharedTranslationSeries);
		acTranslationTask.setJobsInSequenceNum(task.getJobsInSequenceNum());
		acTranslationTask.setMaxJobsInParallelNum(task.getMaxJobsInParallelNum());

		// AC evaluation task
		AcRunTranslateAndMultiEvaluateTask acEvaluationTask = new AcRunTranslateAndMultiEvaluateTask();
		acEvaluationTask.setRunAndTranslationTaskDefinition(acTranslationTask);
		acEvaluationTask.setAcEvaluations(evaluations);
		acEvaluationTask.setEvaluateFullFlag(true);
		acEvaluationTask.setJobsInSequenceNum(task.getJobsInSequenceNum());
		acEvaluationTask.setMaxJobsInParallelNum(task.getMaxJobsInParallelNum());

		return acEvaluationTask;
	}

	@Override
	public void analyzeDynamics(AcMultiRunAnalysisTask task) {
		log.info("AC multi run analysis for AC compartments '" + task.getCompartmentIds() + "' and analysis spec '" + task.getMultiRunAnalysisSpec().getId() + "' is about to be executed.");
		initMultipleCompartmentsIfNeeded(task);
		initSimulationConfigIfNeeded(task);
		initMultiRunAnalysisSpecIfNeeded(task);

    	if (task.getMultiRunAnalysisSpec() == null) {
    		throw new CoelRuntimeException("Specificiation for ac multi run analysis task.");
    	}

		List<AcCompartment> sortedCompartments = new ArrayList<AcCompartment>(task.getCompartments());
		Collections.sort(sortedCompartments, new DomainObjectKeyComparator<Long>());
		Collection<Collection<AcCompartment>> compartmentGroups = ObjectUtil.splitIntoGroups(sortedCompartments, dynamicsAnalysisTasksInParallel);
		for (final Collection<AcCompartment> compartments : compartmentGroups) {
			analyzeDynamics(compartments, task.getSimulationConfig(), task.getMultiRunAnalysisSpec());
		}
	}

	@Override
	public void rerunDerridaAnalysis(AcMultiRunAnalysisTask task) {
		log.info("AC Derrida analysis (rerun) for AC compartment '" + task.getCompartmentIds() + "' and analysis spec '" + task.getMultiRunAnalysisSpec().getId() + "' is about to be executed.");
		
		// TODO: migrate analysis results
//		initMultipleAcsWithAnalysisResultsIfNeeded(task);
		initMultipleCompartmentsIfNeeded(task);
		initSimulationConfigIfNeeded(task);
		initMultiRunAnalysisSpecIfNeeded(task);

    	if (task.getMultiRunAnalysisSpec() == null) {
    		throw new CoelRuntimeException("Specificiation for ac multi run analysis task.");
    	}

		List<AcCompartment> sortedCompartments = new ArrayList<AcCompartment>(task.getCompartments());
		Collections.sort(sortedCompartments, new DomainObjectKeyComparator<Long>());
		Collection<Collection<AcCompartment>> compartmentGroups = ObjectUtil.splitIntoGroups(sortedCompartments, dynamicsAnalysisTasksInParallel);
		for (final Collection<AcCompartment> compartments : compartmentGroups) {
			rerunDerridaAnalysis(compartments, task.getSimulationConfig(), task.getMultiRunAnalysisSpec().getSingleRunSpec());
		}
		log.info("AC Derrida analysis (rerun) finished.");
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void analyzeDynamics(
		Collection<AcCompartment> compartments,
		AcSimulationConfig simulationConfig,
		MultiRunAnalysisSpec<Double> spec
	) {
		Map<AcCompartment, Future<Collection<SingleRunAnalysisResult>>> compartmentResultsMap = new HashMap<AcCompartment, Future<Collection<SingleRunAnalysisResult>>>();
		for (AcCompartment compartment : compartments) {

			final ArgumentCallable<List<Double>, SingleRunAnalysisResult> dynamicsAnalysisCall = 
				new GridDynamicsAnalysisCall(chemistryRunnableFactory, compartment, simulationConfig, spec.getSingleRunSpec());

			final RandomDistributionProvider<Double> distributionProvider = RandomDistributionProviderFactory.apply(spec.getInitialStateDistribution());

			int speciesNum = compartment.getSpecies().size();

			Collection<List<Double>> initialStates = new ArrayList<List<Double>>();
			for (int i = 0; i < spec.getRunNum(); i++) {
				initialStates.add(distributionProvider.nextList(speciesNum));
			}
			Future<Collection<SingleRunAnalysisResult>> results = computationalGrid.runOnGridAsync(dynamicsAnalysisCall, initialStates);
			compartmentResultsMap.put(compartment, results);			
		}

		Collection<AcMultiRunAnalysisResult> multiResults = new ArrayList<AcMultiRunAnalysisResult>();
		for (AcCompartment compartment : compartments) {
			Collection<SingleRunAnalysisResult> results;
			try {
				results = compartmentResultsMap.get(compartment).get();
			} catch (ExecutionException e) {
                throw new CoelRuntimeException("Asynchronous 'Dynamics Analysis' job failed on grid.", e);
            } catch (InterruptedException e) {
                throw new CoelRuntimeException("Asynchronous 'Dynamics Analysis' job failed on grid.", e);
            }
			AcMultiRunAnalysisResult acMultiRunAnalysisResult = new AcMultiRunAnalysisResult();
			User user = new User();
			user.setId(1l);
			acMultiRunAnalysisResult.setCreatedBy(user);
			// TODO: migrate to compartment and simulation config
			// acMultiRunAnalysisResult.setAc(compartment);
			acMultiRunAnalysisResult.setSpec(spec);

			for (SingleRunAnalysisResult singleResult : results) {
				acMultiRunAnalysisResult.addSingleRunResult(singleResult);
			}
			multiResults.add(acMultiRunAnalysisResult);
		}
		saveObjectsAndPostMessage(acMultiRunAnalysisResultDAO, multiResults, "AC multi run analysis result");
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void rerunDerridaAnalysis(
		Collection<AcCompartment> compartments,
		AcSimulationConfig simulationConfig,
		SingleRunAnalysisSpec singleRunSpec
	) {
		Set<Future<Collection<StatsSequence>>> derridaResults = new HashSet<Future<Collection<StatsSequence>>>();
		Set<Long> derridaResultStatsIds = new HashSet<Long>();
		for (AcCompartment compartment : compartments) {
			final ArgumentCallable<SingleRunAnalysisResult, StatsSequence> derridaRerunCall =
				new GridRerunDerridaAnalysisCall(chemistryRunnableFactory, compartment, simulationConfig, singleRunSpec);

			// TODO: migrate multi run analysis results to compartments

//			for (AcMultiRunAnalysisResult multiRunResult : ac.getMultiRunAnalysisResults()) {
//				Collection<SingleRunAnalysisResult> singleRunResults = singleRunAnalysisResultDAO.getListWithPropertyAndInitialState(
//						SingleRunAnalysisResultType.DerridaResults, multiRunResult.getId());
//
//				Future<Collection<StatsSequence>> results = computationalGrid.runOnGridAsync(derridaRerunCall, singleRunResults);
//				derridaResults.add(results);
//				for (SingleRunAnalysisResult singleRunResult : singleRunResults) {
//					derridaResultStatsIds.add(singleRunResult.getDerridaResults().getId());
//				}
//			}
		}

		Collection<StatsSequence> oldDerridaResults = statsSequenceDAO.get(derridaResultStatsIds);
		Map<Long, StatsSequence> oldDerridaResultMap = new HashMap<Long, StatsSequence>();
		for (StatsSequence oldDerridaResult : oldDerridaResults) {
			oldDerridaResultMap.put(oldDerridaResult.getId(), oldDerridaResult);
		}

		for (Future<Collection<StatsSequence>> derridaResultsFutures : derridaResults) {
			try {
				for (StatsSequence newDerridaResult : derridaResultsFutures.get()) {
					StatsSequence oldDerridaResult = oldDerridaResultMap.get(newDerridaResult.getId());
					oldDerridaResult.setStats(newDerridaResult.getStats());
				}
			} catch (ExecutionException e) {
                throw new CoelRuntimeException("Asynchronous 'Derrida Analysis Rerun' job failed on grid.", e);
            } catch (InterruptedException e) {
                throw new CoelRuntimeException("Asynchronous 'Derrida Analysis Rerun' job failed on grid.", e);
            }
		}

//		saveObjectsAndPostMessage(acMultiRunAnalysisResultDAO, multiResults, "AC multi run analysis result");
	}

	@Override
	@Transactional
	public void generateArtificialChemistries(
		ArtificialChemistrySpec spec,
		AcSimulationConfig config,
		int acNum
	) {
		spec = initAcSpecIfNeeded(spec);
		User user = userDAO.get(new Long(1));
		for (int i = 0; i < acNum; i++) {
			AcCompartment newAc = compartmentFactory.createInstance(spec, config);
			newAc.setLabel("Generated : " + spec.getName() + "(" + (i + 1) + ")");
			saveCompartmentWithAllData(newAc, user);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<Stats> getMergedMultiRunStats(
		SingleRunAnalysisResultType property,
		StatsType statsType,
		Collection<Long> multiRunIds
	) {
		Collection<Stats> mergedStats = null; 
		if (property.holdsStats()) {
			List<Stats> multipleStats =	acMultiRunAnalysisResultDAO.getStatsForPropertyOnly(property, multiRunIds);

			Collection<Collection<Stats>> statsPosGroups = new ArrayList<Collection<Stats>>();
			Double currentPos = null;
			Collection<Stats> currentStatsPosGroup = null;
			for (Stats stats : multipleStats) {
				if (!ObjectUtil.areObjectsEqual(stats.getPos(), currentPos)) {
					currentStatsPosGroup = new ArrayList<Stats>();
					statsPosGroups.add(currentStatsPosGroup);
				}
				currentStatsPosGroup.add(stats);
				currentPos = stats.getPos();
			}

	    	Collection<Double> indeces = new ArrayList<Double>();
	    	for (Collection<Stats> statsGroup : statsPosGroups) {
	    		indeces.add(ObjectUtil.getFirst(statsGroup).getPos());
	    	}

	    	mergedStats = (Collection<Stats>) JavaMathUtil.calcStatsByFirst(statsType, statsPosGroups, indeces);
		} else {
			Collection<List<Double>> nonStatsValues = (Collection<List<Double>>) acMultiRunAnalysisResultDAO.getForPropertyOnly(property, multiRunIds);
			mergedStats = (Collection<Stats>) JavaMathUtil.calcStatsBySecond(nonStatsValues);
		}

		return mergedStats;
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<Stats> getMergedAcStats(
		SingleRunAnalysisResultType property,
		StatsType statsType,
		Collection<Long> acIds
	) {
		Collection<Object[]> acAndMultiRunIds = artificialChemistryDAO.getAcAndMultiRunAnalysisResultIds(acIds);
		Collection<Long> multiRunIds = new ArrayList<Long>();
		for (Object[] acAndMultiRunId : acAndMultiRunIds) {
			multiRunIds.add((Long) acAndMultiRunId[1]);
		}
		return getMergedMultiRunStats(property, statsType, multiRunIds);
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<Collection<Stats>> getMultiMergedAcStats(
		SingleRunAnalysisResultType property,
		StatsType firstStatsType,
		Collection<Long> acIds
	) {
		Collection<Object[]> acAndMultiRunIds = artificialChemistryDAO.getAcAndMultiRunAnalysisResultIds(acIds);
		Map<Long, Collection<Long>> acMultiRunIdsMap = new HashMap<Long, Collection<Long>>();
		for (Object[] acAndMultiRunId : acAndMultiRunIds) {
			Collection<Long> acMultiRunIds = acMultiRunIdsMap.get(acAndMultiRunId[0]);
			if (acMultiRunIds == null) {
				acMultiRunIds = new ArrayList<Long>();
				acMultiRunIdsMap.put((Long) acAndMultiRunId[0], acMultiRunIds);
			}
			acMultiRunIds.add((Long) acAndMultiRunId[1]);
		}

		Collection<Collection<Stats>> multiMergedAcStats = new ArrayList<Collection<Stats>>();
		for (Collection<Long> acMultiRunIds : acMultiRunIdsMap.values()) {
			multiMergedAcStats.add(getMergedMultiRunStats(property, firstStatsType, acMultiRunIds));
		}

		return multiMergedAcStats;
	}

	@Transactional
	private void saveCompartmentWithAllData(AcCompartment skinCompartment, User user) {
		final AcReactionSet reactionSet = skinCompartment.getReactionSet();
		AcSpeciesSet speciesSet = skinCompartment.getSpeciesSet();
		final AcParameterSet parameterSet = skinCompartment.getSpeciesSet().getParameterSet();

		final String acName = skinCompartment.getLabel();

		speciesSet.setCreatedBy(user);
		parameterSet.setCreatedBy(user);
		speciesSet.setName(acName + " SS");
		parameterSet.setName(acName + " PS");

		if (speciesSet instanceof AcDNAStrandSpeciesSet) {
			AcSpeciesSet newSpeciesSet = new AcSpeciesSet();
			newSpeciesSet.setName(speciesSet.getName());
			newSpeciesSet.setCreateTime(speciesSet.getCreateTime());
			newSpeciesSet.setCreatedBy(speciesSet.getCreatedBy());
			newSpeciesSet.setVarSequenceNum(speciesSet.getVarSequenceNum());
			newSpeciesSet.setParentSpeciesSet(speciesSet.getParentSpeciesSet());
			newSpeciesSet.setParameterSet(speciesSet.getParameterSet());
			parameterSet.setSpeciesSet(newSpeciesSet);
			Collection<AcSpecies> species = new ArrayList<AcSpecies>(speciesSet.getVariables());
			for (AcSpecies oneSpecies : species) {
				Integer index = oneSpecies.getVariableIndex();
				speciesSet.removeVariable(oneSpecies);
				oneSpecies.setVariableIndex(index);
				newSpeciesSet.addVariable(oneSpecies);
			}
			speciesSet = newSpeciesSet;
		}

		AcSpeciesSet savedSpeciesSet = acSpeciesSetDAO.save(speciesSet);
	
		reactionSet.setSpeciesSet(savedSpeciesSet);
		reactionSet.setCreatedBy(user);	
		reactionSet.setLabel(acName);

		skinCompartment.setCreatedBy(user);
		skinCompartment.setReactionSet(acReactionSetDAO.save(reactionSet));

		acCompartmentDAO.save(skinCompartment);
	}

//////////////////////
// DB INITIALIZATIONS
//////////////////////

	@Transactional
	private void initCompartmentIfNeeded(AcCompartmentHolder holder) {
		if (holder.isCompartmentDefined()) {
			AcCompartment compartment = holder.getCompartment();
			if (!holder.isCompartmentComplete()) {
				compartment = acCompartmentDAO.get(compartment.getId());
				// lazy initialization
				loadLazyPropsOfCompartment(compartment);
			}
			holder.setCompartment(copyWithoutHibernate(compartment));
		}
	}

	@Transactional
	private void initMultipleCompartmentsIfNeeded(AcMultipleCompartmentHolder holder) {
		if (holder.areCompartmentsDefined()) {
			Collection<AcCompartment> compartments = null;
			if (!holder.areCompartmentsComplete()) {
				compartments = new ArrayList<AcCompartment>();
				for (AcCompartment compartment : holder.getCompartments()) {
					AcCompartment loadedCompartment = acCompartmentDAO.get(compartment.getId());
					// lazy initialization
					loadLazyPropsOfCompartment(loadedCompartment);
					compartments.add(loadedCompartment);
				}
			} else {
				compartments = holder.getCompartments();
			}
			holder.setCompartments(copyWithoutHibernate(compartments));
		}
	}

	@Transactional
	private void initSimulationConfigIfNeeded(AcSimulationConfigHolder holder) {
		if (holder.isSimulationConfigDefined()) {
			AcSimulationConfig simulationConfig = holder.getSimulationConfig();
			if (!holder.isSimulationConfigComplete()) {
				simulationConfig = acSimulationConfigDAO.get(simulationConfig.getId());
			}
			holder.setSimulationConfig(copyWithoutHibernate(simulationConfig));
		}
	}

	@Transactional
	private ArtificialChemistrySpec initAcSpecIfNeeded(ArtificialChemistrySpec acSpec) {
		ArtificialChemistrySpec initializedAcSpec = acSpec;
		if (acSpec != null) {
			if (acSpec.getRateConstantDistribution() == null) {
				initializedAcSpec = artificialChemistrySpecDAO.get(acSpec.getId());
			}
			initializedAcSpec = copyWithoutHibernate(initializedAcSpec);
		}
		return initializedAcSpec;
	}

//	@Transactional
//	private void initMultipleAcsWithAnalysisResultsIfNeeded(MultipleAcHolder holder) {
//		if (holder.areAcsDefined()) {
//			Collection<ArtificialChemistry> acs = null;
//			if (!holder.areAcsComplete()) {
//				acs = new ArrayList<ArtificialChemistry>();
//				for (ArtificialChemistry ac : holder.getAcs()) {
//					ArtificialChemistry loadedAc = artificialChemistryDAO.get(ac.getId());
//					// lazy initialization
//					loadLazyPropsOfAc(loadedAc);
//					loadedAc.getMultiRunAnalysisResults();
//					acs.add(loadedAc);
//				}
//			} else {
//				acs = holder.getAcs();
//			}
//			holder.setAcs(copyWithoutHibernate(acs));
//		}
//	}

	private void loadLazyPropsOfCompartment(AcCompartment compartment) {
		compartment.getChannels().size();
		compartment.getSubChannelGroups().size();
		compartment.getSpecies().size();
		for (AcReaction reaction : compartment.getReactionSet().getReactions()) {
			reaction.getSpeciesAssociations();
		}
		for (AcCompartment subCompartment : compartment.getSubCompartments()) {
			loadLazyPropsOfCompartment(subCompartment);
		}
	}

	private void loadLazyPropsOfAc(ArtificialChemistry ac) {
		loadLazyPropsOfCompartment(ac.getSkinCompartment());
	}

	@Transactional
	private void initActionSeriesIfNeeded(AcInteractionSeriesHolder holder) {
		if (holder.isInteractionSeriesDefined()) {
			AcInteractionSeries actionSeries = holder.getInteractionSeries();
			if (!holder.isInteractionSeriesComplete()) {
				actionSeries = acInteractionSeriesDAO.get(actionSeries.getId());
				// lazy loading
				initActionSeriesRecursively(actionSeries);
			}
			holder.setInteractionSeries(copyWithoutHibernate(actionSeries));
		}
	}

	private void initActionSeriesRecursively(AcInteractionSeries actionSeries) {
		actionSeries.getActions().size();
		actionSeries.getVariables().size();
		actionSeries.getImmutableSpecies().size();
		actionSeries.getSpecies().size();
		for (AcInteractionSeries subActionSeries : actionSeries.getSubActionSeries()) {
			initActionSeriesRecursively(subActionSeries);
		}
	}

	@Transactional
	private void initMultipleTranslationSeriesIfNeeded(AcMultipleTranslationSeriesHolder holder) {
		if (holder.areTranslationSeriesDefined()) {
			Collection<AcTranslationSeries> translationSeries = holder.getTranslationSeries();
			if (!holder.areTranslationSeriesComplete()) {
				translationSeries = acTranslationSeriesDAO.get(holder.getTranslationSeriesIds());
				for (AcTranslationSeries oneTranslationSeries : translationSeries) {
					loadLazyPropsOfTranslationSeries(oneTranslationSeries);
				}
			}
			holder.setTranslationSeries(copyWithoutHibernate(translationSeries));
		}
	}

	@Transactional
	private void initTranslationSeriesIfNeeded(AcTranslationSeriesHolder holder) {
		if (holder.isTranslationSeriesDefined()) {
			AcTranslationSeries translationSeries = holder.getTranslationSeries();
			if (!holder.isTranslationSeriesComplete()) {
				translationSeries = acTranslationSeriesDAO.get(translationSeries.getId());
				loadLazyPropsOfTranslationSeries(translationSeries);
			}
			holder.setTranslationSeries(copyWithoutHibernate(translationSeries));
		}
	}

	private void loadLazyPropsOfTranslationSeries(AcTranslationSeries translationSeries) {
		translationSeries.getTranslations().size();
		translationSeries.getSpeciesSet().getName();
	}

	@Transactional
	private void initAcEvaluationIfNeeded(AcEvaluationHolder holder) {
		if (holder.isAcEvaluationDefined()) {
			AcEvaluation acEvaluation = holder.getAcEvaluation();
			if (!holder.isAcEvaluationComplete()) {
				acEvaluation = acEvaluationDAO.get(acEvaluation.getId());
				log.info("AC evaluation '" + acEvaluation.getId() + "' loaded from database.");
			}
			loadLazyPropsOfTranslationSeries(acEvaluation.getTranslationSeries());
			holder.setAcEvaluation(copyWithoutHibernate(acEvaluation));
		}
	}

	@Transactional
	private void initMultipleAcEvaluationsIfNeeded(AcMultipleEvaluationHolder holder) {
		if (holder.areAcEvaluationsDefined()) {
			Collection<AcEvaluation> acEvaluations = holder.getAcEvaluations();
			if (!holder.areAcEvaluationsComplete()) {
				acEvaluations = new ArrayList<AcEvaluation>();
				for (Long evaluationId : holder.getAcEvaluationIds()) {
					final AcEvaluation evaluation = acEvaluationDAO.get(evaluationId);
					loadLazyPropsOfTranslationSeries(evaluation.getTranslationSeries());
					acEvaluations.add(evaluation);
				}
			}
			holder.setAcEvaluations(copyWithoutHibernate(acEvaluations));
		}
	}

	@Transactional
	private void initMultipleRateConstantTypeBoundIfNeeded(AcMultipleRateConstantTypeBoundHolder holder) {
		if (holder.areRateConstantTypeBoundDefined()) {
			Collection<AcRateConstantTypeBound> acRateConstantTypeBounds = holder.getRateConstantTypeBounds();
			if (!holder.areRateConstantTypeBoundComplete()) {
				acRateConstantTypeBounds = acRateConstantTypeBoundDAO.get(holder.getRateConstantTypeBoundIds());
			}
			holder.setRateConstantTypeBounds(copyWithoutHibernate(acRateConstantTypeBounds));
		}
	}

	@Transactional
	private void initMultiRunAnalysisSpecIfNeeded(MultiRunAnalysisSpecHolder<?> holder) {
		if (holder.isMultiRunAnalysisSpecDefined()) {
			MultiRunAnalysisSpec multiRunAnalysisSpec = holder.getMultiRunAnalysisSpec();
			if (!holder.isMultiRunAnalysisSpecComplete()) {
				multiRunAnalysisSpec = multiRunAnalysisSpecDAO.get(multiRunAnalysisSpec.getId());
			}
			holder.setMultiRunAnalysisSpec(copyWithoutHibernate(multiRunAnalysisSpec));
		}
	}

	private void handleAndWrapSqlException(Exception exception, String errorMessage) {
		if (exception instanceof SQLException) {
			while (exception != null) {
				log.error("SQL related error occured while accessing database for AC run task.", exception); // Log the exception
				// Get cause if present
				Throwable t = exception.getCause();
				while (t != null) {
					log.error("Cause:" + t);
					t = t.getCause();
				}
				// proceed to the next exception
				exception = ((SQLException) exception).getNextException();
			}
		}
		log.error(errorMessage, exception);
		throw new CoelRuntimeException(errorMessage, exception);
	}

	////////////////
	// INJECTIONS //
	////////////////

	protected ArtificialChemistryUtil getAcUtil() {
		return acUtil;
	}

	public ArgumentCallable<AcTranslatedRunEvaluationTask, AcEvaluatedRun> getAcEvaluateCall() {
		return acEvaluateCall;
	}

	public void setAcEvaluateCall(ArgumentCallable<AcTranslatedRunEvaluationTask, AcEvaluatedRun> acEvaluateCall) {
		this.acEvaluateCall = acEvaluateCall;
	}

	public Integer getDynamicsAnalysisTasksInParallel() {
		return dynamicsAnalysisTasksInParallel;
	}

	public void setDynamicsAnalysisTasksInParallel(Integer dynamicsAnalysisTasksInParallel) {
		this.dynamicsAnalysisTasksInParallel = dynamicsAnalysisTasksInParallel;
	}

	public ArgumentCallable<AcRunTask, ComponentRunTrace<Double, Pair<AcCompartment, AcSpecies>>> getChemistryRunCall() {
		return chemistryRunCall;
	}

	public void setChemistryRunCall(ArgumentCallable<AcRunTask, ComponentRunTrace<Double, Pair<AcCompartment, AcSpecies>>> chemistryRunCall) {
		this.chemistryRunCall = chemistryRunCall;
	}

	public AcReplicator getReplicator() {
		return replicator;
	}

	public void setReplicator(AcReplicator replicator) {
		this.replicator = replicator;
	}

	public GenericDAO<Function<?, ?>, Long> getFunctionDAO() {
		return functionDAO;
	}

	public Log getLog() {
		return log;
	}

	public void setAcUtil(ArtificialChemistryUtil acUtil) {
		this.acUtil = acUtil;
	}

	public ArgumentCallable<AcRunAndTranslateTask, AcTranslatedRun> getChemistryRunAndInterpretCall() {
		return chemistryRunAndInterpretCall;
	}

	public void setChemistryRunAndInterpretCall(ArgumentCallable<AcRunAndTranslateTask, AcTranslatedRun> chemistryRunAndInterpretCall) {
		this.chemistryRunAndInterpretCall = chemistryRunAndInterpretCall;
	}
}