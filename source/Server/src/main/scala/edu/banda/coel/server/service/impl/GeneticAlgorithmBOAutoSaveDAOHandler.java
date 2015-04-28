package edu.banda.coel.server.service.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.banda.serverbase.persistence.GenericDAO;
import com.banda.math.business.evo.GeneticAlgorithmBOAutoSaveHandler;
import com.banda.math.domain.evo.Chromosome;
import com.banda.math.domain.evo.EvoRun;
import com.banda.math.domain.evo.Population;

/**
 * @author Peter Banda
 * @since 2012
 */
@Deprecated
class GeneticAlgorithmBOAutoSaveDAOHandler<C> implements GeneticAlgorithmBOAutoSaveHandler<C> {

	private GenericDAO<EvoRun<C>, Long> evoRunDAO;
	private GenericDAO<Population<C>, Long> populationDAO;
	private GenericDAO<Chromosome<C>, Long> chromosomeDAO;

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public EvoRun<C> saveEvoRun(EvoRun<C> evoRun) {
		return evoRunDAO.save(evoRun);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Population<C> savePopulation(Population<C> population) {
		return populationDAO.save(population);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void replacePopulation(Long populationId, Population<C> newPopulation) {
		Population<C> population = populationDAO.get(populationId);
		// chromosomes
		Collection<Chromosome<C>> chromosomesToRemove = new ArrayList<Chromosome<C>>();
		if (newPopulation.hasChromosomes() && !population.hasChromosomes()) {
			population.addChromosomes(newPopulation.getChromosomes());
		} else if (!newPopulation.hasChromosomes() && population.hasChromosomes()) {
			chromosomesToRemove.addAll(population.getChromosomes());
			population.removeAllChromosomes();
		}

		// best chromosome
		if (newPopulation.hasBestChromosome() && !population.hasBestChromosome()) {
			Chromosome<C> newBestChromosome = newPopulation.getBestChromosome();
			if (newBestChromosome.getId() != null) {
				newBestChromosome = chromosomeDAO.get(newBestChromosome.getId());	
			}
			population.setBestChromosome(newBestChromosome);
		} else if (!newPopulation.hasBestChromosome() && population.hasBestChromosome()) {
			population.setBestChromosome(null);
			chromosomesToRemove.add(population.getBestChromosome());
		}

		if (population.hasBestChromosome()) {
			chromosomesToRemove.remove(population.getBestChromosome());
		}

		// remove obsolete chromosomes
		chromosomeDAO.remove(chromosomesToRemove);

		// explicit score 
		population.setMinScore(newPopulation.getMinScore());
		population.setMaxScore(newPopulation.getMaxScore());
		population.setMeanScore(newPopulation.getMeanScore());

		// explicit fitness
		population.setMinFitness(newPopulation.getMinFitness());
		population.setMaxFitness(newPopulation.getMaxFitness());
		population.setMeanFitness(newPopulation.getMeanFitness());
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void removePopulation(Long populationId) {
		Population<C> population = populationDAO.get(populationId);
		// just to trigger lazy initialization
		population.getChromosomes().isEmpty();
		// TODO: move to PopulationDAO
		if (population.hasChromosomes()) {
			Collection<Chromosome<C>> chromosomesToRemove = new ArrayList<Chromosome<C>>(population.getChromosomes());
			population.removeAllChromosomes();
			chromosomeDAO.remove(chromosomesToRemove);
		}
		if (population.hasBestChromosome()) {
			Chromosome<C> bestChromosome = population.getBestChromosome();
			population.setBestChromosome(null);
			chromosomeDAO.remove(bestChromosome);
		}
		populationDAO.remove(population);
	}

	// Injections

	public GenericDAO<EvoRun<C>, Long> getEvoRunDAO() {
		return evoRunDAO;
	}

	public void setEvoRunDAO(GenericDAO<EvoRun<C>, Long> evoRunDAO) {
		this.evoRunDAO = evoRunDAO;
	}

	public GenericDAO<Population<C>, Long> getPopulationDAO() {
		return populationDAO;
	}

	public void setPopulationDAO(GenericDAO<Population<C>, Long> populationDAO) {
		this.populationDAO = populationDAO;
	}

	public GenericDAO<Chromosome<C>, Long> getChromosomeDAO() {
		return chromosomeDAO;
	}

	public void setChromosomeDAO(GenericDAO<Chromosome<C>, Long> chromosomeDAO) {
		this.chromosomeDAO = chromosomeDAO;
	}
}