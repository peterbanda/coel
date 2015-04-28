package edu.banda.coel.domain.service;

import com.banda.core.Pair;
import com.banda.math.domain.evo.EvoTask;
import com.banda.math.task.EvoRunTask;

import java.util.Collection;


/**
 * Title: EvolutionService
 *
 * @author Peter Banda
 * @since 2011
 */
public interface EvolutionService {

    void evolve(EvoRunTask taskDef);

    Collection<Pair<Double, Double>> calcFitnessToBestChromDensity(EvoTask evoTask);
}