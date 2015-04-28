package edu.banda.coel.domain.service;

import com.banda.core.domain.ComponentRunTrace;
import com.banda.network.domain.TopologicalNode;
import edu.banda.coel.task.network.NetworkPerformanceEvaluateTask;
import edu.banda.coel.task.network.NetworkRunTask;
import edu.banda.coel.task.network.SpatialNetworkPerformanceEvaluateTask;

import java.util.Collection;

/**
 * Title: NetworkService
 *
 * @author Peter Banda
 * @since 2013
 */
public interface NetworkService {

    /**
     * Runs the network for a given number of steps.
     *
     * @param task
     * @return Component traces (configurations)
     */
    <T> Collection<ComponentRunTrace<T, TopologicalNode>> runSimulation(NetworkRunTask<T> task);

    /**
     * Runs the network, evaluates the resulting configuration traces, and stores the results.
     *
     * @param task The task definition
     */
    <T> void runPerformanceEvaluation(NetworkPerformanceEvaluateTask<T> task);

    /**
     * Runs the spatial network, evaluates the resulting configuration traces, and stores the results.
     *
     * @param task The task definition
     */
    <T> void runSpatialPerformanceEvaluation(SpatialNetworkPerformanceEvaluateTask<T> task);

    /**
     * Run a Derrida perturbation analysis for the network
     *
     * @param task The task definition
     */
    void runBooleanDerridaAnalysis(NetworkRunTask<Boolean> task);

    /**
     * Run a damage spreading analysis for the network
     *
     * @param task The task definition
     */
    void runBooleanDamageSpreadingAnalysis(NetworkRunTask<Boolean> task);
}