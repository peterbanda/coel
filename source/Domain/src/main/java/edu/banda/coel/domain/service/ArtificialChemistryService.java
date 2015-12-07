package edu.banda.coel.domain.service;

import com.banda.chemistry.domain.*;
import com.banda.core.Pair;
import com.banda.core.domain.ComponentRunTrace;
import com.banda.math.domain.Stats;
import com.banda.math.domain.StatsType;
import com.banda.math.domain.dynamics.SingleRunAnalysisResultType;
import edu.banda.coel.task.chemistry.*;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Title: ArtificialChemistryService
 *
 * @author Peter Banda
 * @since 2011
 */
public interface ArtificialChemistryService {

    /**
     * Runs the compartmentalized chemistry against the interaction series (e.g., initial concentration setting) asynchronously.
     *
     * @param task The task definition containing a compartment, an interaction series, the number of steps, etc.
     * @return Concentration traces
     */
    Future<Collection<ComponentRunTrace<Double, Pair<AcCompartment, AcSpecies>>>> runSimulationAsync(AcRunTask task);

    /**
     * Runs the compartmentalized chemistry against the interaction series (e.g., initial concentration setting) synchronously.
     *
     * @param task The task definition containing a compartment, an interaction series, the number of steps, etc.
     * @return Concentration traces
     */
    Collection<ComponentRunTrace<Double, Pair<AcCompartment, AcSpecies>>> runSimulation(AcRunTask task);

    /**
     * Runs the compartmentalized chemistry and translates/interprets the resulting concentration traces asynchronously.
     *
     * @param task The task definition
     */
    Future<Collection<AcTranslatedRun>> runAndTranslateAsync(AcRunAndTranslateTask task);

    /**
     * Runs the compartmentalized chemistry and translates/interprets the resulting concentration traces synchronously.
     *
     * @param task The task definition
     */
    Collection<AcTranslatedRun> runAndTranslate(AcRunAndTranslateTask task);

    /**
     * Evaluates the translated chemistry run(s) and returns the results.
     *
     * @param task The task definition
     */
    Collection<AcEvaluatedRun> evaluateRunWithResult(AcEvaluateTask task);

    /**
     * Runs a chemistry, and translates and evaluates the resulting concentration.
     *
     * @param task The task definition
     */
    Collection<AcEvaluatedRun> runSimulationTranslateAndEvaluateWithResult(AcRunTranslateAndEvaluateTask task);

    /**
     * Runs, translates, and evaluates a given chemistry.
     *
     * @param task The task definition
     */
    void runSimulationTranslateAndEvaluate(AcRunTranslateAndEvaluateTask task);

    /**
     * Runs a perturbation analysis of a given compartmentalized chemistry with an interaction series and an evaluation.
     *
     * @param task The task definition
     */
    Double[] runPerturbationAnalysis(AcPerturbateTask task);

    /**
     * Runs and translates AC simulations on the fly, and stores the result.
     *
     * @param task The task definition
     */
    void runPerformanceEvaluation(AcPerformanceEvaluateTask task);

    /**
     * Aux method for Async execution. Removed once AspectJ proxying is activated (Spring > 3.2)
     */
    void runPerformanceEvaluation(
            AcPerformanceEvaluateTask task,
            AcInteractionSeries actionSeries,
            List<AcEvaluation> sharedTranslationEvaluations
    );

    /**
     * Runs and translates AC simulations on the fly, and stores the result.
     *
     * @param task The task definition
     */
    void runRandomRatePerformanceEvaluation(AcRandomRatePerformanceEvaluateTask task);

    /**
     * Evaluates perturbation performance.
     *
     * @param task The task definition
     */
    void runPerturbationPerformanceEvaluation(AcPerturbationPerformanceEvaluateTask task);

    /**
     * Saves SBML model represented as XML String to database as Artificial Chemistry.
     *
     * @param sbmlString The SBML model represented as String
     */
    void saveSbmlModelAsArtificialChemistry(String sbmlString, String acName);

    /**
     * Saves SBML model represented as XML String to database as compartment.
     *
     * @param sbmlString The SBML model represented as String
     */
    void saveSbmlModelAsCompartment(String sbmlString, String compartmentName);

    /**
     * Analyzes dynamics of given artificial chemistry
     *
     * @param task
     */
    void analyzeDynamics(AcMultiRunAnalysisTask task);

    /**
     * Reruns Derrida analysis for given artificial chemistries
     *
     * @param task
     */
    void rerunDerridaAnalysis(AcMultiRunAnalysisTask task);

    /**
     * Creates new artificial chemistries
     *
     * @param spec
     * @param config
     * @param acNum
     */
    void generateArtificialChemistries(ArtificialChemistrySpec spec, AcSimulationConfig config, int acNum);

    /**
     * @param property
     * @return
     */
    Collection<Stats> getMergedMultiRunStats(
            SingleRunAnalysisResultType property,
            StatsType statsType,
            Collection<Long> multiRunIds);

    Collection<Stats> getMergedAcStats(
            SingleRunAnalysisResultType property,
            StatsType statsType,
            Collection<Long> acIds);

    Collection<Collection<Stats>> getMultiMergedAcStats(
            SingleRunAnalysisResultType property,
            StatsType statsType,
            Collection<Long> acIds);
}