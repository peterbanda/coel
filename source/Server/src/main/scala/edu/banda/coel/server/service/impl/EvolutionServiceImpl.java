package edu.banda.coel.server.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.banda.chemistry.domain.AcCompartment;
import com.banda.chemistry.domain.AcCompartmentChannelGroup;
import com.banda.chemistry.domain.AcEvaluation;
import com.banda.chemistry.domain.AcInteractionSeries;
import com.banda.chemistry.domain.AcTranslationSeries;
import com.banda.core.Pair;
import com.banda.core.domain.um.User;
import com.banda.core.util.ObjectUtil;
import com.banda.math.business.evo.EvoChromManipulatorBO;
import com.banda.math.business.evo.EvoTaskBO;
import com.banda.math.business.evo.GeneticAlgorithmBO;
import com.banda.math.business.evo.GeneticAlgorithmBOAutoSaveHandler;
import com.banda.math.business.evo.GeneticAlgorithmBOFactory;
import com.banda.math.domain.evo.Chromosome;
import com.banda.math.domain.evo.CompositeChromosome;
import com.banda.math.domain.evo.EvoRun;
import com.banda.math.domain.evo.EvoTask;
import com.banda.math.domain.evo.Population;
import com.banda.math.task.EvoRunTask;
import com.banda.math.task.EvoTaskParts.EvoRunHolder;
import com.banda.math.task.EvoTaskParts.EvoTaskHolder;
import com.banda.network.domain.NetworkActionSeries;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.CoelRuntimeException;
import edu.banda.coel.business.evo.EvoTaskBOFactorySwitch;
import edu.banda.coel.domain.evo.EvoAcInteractionSeriesTask;
import edu.banda.coel.domain.evo.EvoAcRateConstantTask;
import edu.banda.coel.domain.evo.EvoAcSpecTask;
import edu.banda.coel.domain.evo.EvoNetworkTask;
import edu.banda.coel.domain.service.EvolutionService;
import edu.banda.coel.server.dao.TaskDAO;
import edu.banda.coel.server.grid.callable.EvoFitnessGridEnabledEvaluatorBO;

/**
 * Title: EvolutionServiceImpl
 *
 * @see edu.banda.coel.domain.service.EvolutionService
 * @author Peter Banda
 * @since 2011
 */
class EvolutionServiceImpl extends AbstractService implements EvolutionService {

	/**
	 * Data Access Objects used by the service methods in this class to access
	 * persistent layer.
	 */
	private GenericDAO<EvoTask, Long> evoTaskDAO;
	private GenericDAO<EvoRun<?>, Long> evoRunDAO;

	@Autowired
	private TaskDAO taskDAO;
	private GeneticAlgorithmBOAutoSaveHandler geneticAlgorithmBOAutoSaveHandler;

	@Autowired
	private EvoTaskBOFactorySwitch evoTaskBOFactory;

	@Autowired
	private GeneticAlgorithmBOFactory geneticAlgorithmBOFactory;

	private Integer defaultMaxJobsInParallelNum;
	private Integer defaultJobsInSequenceNum;

	//////////////
	// SERVICES //
	//////////////

	@Override
	@Transactional
	public void evolve(EvoRunTask task) {
		EvoRunTask taskDef = initEvoRunTaskIfNeeded(task);
		if (taskDef.hasInitChromosome()) {
			log.info("Evolution for evo task '" + taskDef.getEvoTaskId() + "' with an init chromosome '" + taskDef.getInitChromosome().getId() + "' is about to be executed.");
		} else if (taskDef.getEvoRunId() != null) {
			log.info("Evolution for evo run '" + taskDef.getEvoRunId() + "' is about to be executed.");
		} else {
			log.info("Evolution for evo task '" + taskDef.getEvoTaskId() + "' is about to be executed.");
		}

		// initialize evo task, evo run and chromosomes
		initEvoTaskIfNeeded(taskDef);
		initEvoRunIfNeeded(taskDef);
		if (taskDef.isEvoRunDefined()) {
			initChromosomesIfResume(taskDef);
		}

		if (!taskDef.isEvoRunDefined() && !taskDef.isEvoTaskDefined()) {
			throw new CoelRuntimeException("Evolution task or evolution run required for the evolution run task.");	
		}

		if (taskDef.isEvoTaskDefined()) {
			// if evo task is defined, new evo run needs to be launched, so don't pass (old) evo run
			taskDef.setEvoRun(null);
		} else {
			// otherwise, resume evo run using associated evo task
			taskDef.setEvoTask(taskDef.getEvoRun().getEvoTask());
		}

		EvoTaskBO<?, ?, ?> evoTaskBO = evoTaskBOFactory.createInstance(taskDef);
		evoTaskBO = createGridBasedEvoTaskBO(evoTaskBO, task);
		if (taskDef.hasInitChromosome()) {
			initChromosomesIfGiven(taskDef, evoTaskBO.getChromManipulator());
		}

		GeneticAlgorithmBO<?, ?, ?> gaBO = geneticAlgorithmBOFactory.createInstance(taskDef, evoTaskBO);
		gaBO.setAutoSaveHandler(geneticAlgorithmBOAutoSaveHandler);
		if (taskDef.isEvoRunDefined()) {
			log.info("Resuming the genetic algorithm for evo run '" + taskDef.getEvoRunId() + "' with generation limit '" + taskDef.getGaSetting().getGenerationLimit() + "'.");			
		} else {
			log.info("Running the genetic algorithm for evo task '" + taskDef.getEvoTaskId() + "' with generation limit '" + taskDef.getGaSetting().getGenerationLimit() + "'.");
		}

		try {
			gaBO.evolve();
			EvoRun<?> evoRun = gaBO.getEvoRun();
			if (!taskDef.isAutoSave()) {
				evoRun = evoRunDAO.save(gaBO.getEvoRun());
			}
			postAndLogSaveMessage(evoRun, "Evolution Run");
		} catch (Exception e) {
			log.error("Evolution run failed!", e);
		}
	}

	@Override
	@Transactional(readOnly = true)
    public Collection<Pair<Double, Double>> calcFitnessToBestChromDensity(EvoTask evoTask) {
		if (evoTask.getEvolutionRuns().isEmpty()) {
			evoTask = evoTaskDAO.get(evoTask.getId());
			evoTask.getEvolutionRuns().size();
		}
		Collection<Pair<Double, Double>> fitnessChromDensities = new ArrayList<Pair<Double, Double>>();
		for (EvoRun<?> evoRun : evoTask.getEvolutionRuns()) {
			for (Population<?> population : evoRun.getPopulations()) {
				Chromosome<?> bestChromosome = population.getBestOrLastChromosome();
				try {
					CompositeChromosome<?,Boolean> bestCompositeChromosome = (CompositeChromosome<?,Boolean>) bestChromosome;
					int count = 0;
					for (int i = 0; i < bestCompositeChromosome.getCodeSize(); i++)
						if (bestCompositeChromosome.getCodeAt(i)) count++;
					fitnessChromDensities.add(
							new Pair<Double, Double>(bestChromosome.getFitness(),
									(double) count / bestChromosome.getCodeSize()));
				} catch (ClassCastException e) {
					log.error("Chromosome '" + bestChromosome.getId() + "' from the population '" + population.getId() + "' is not a Boolean composite chromosome.", e);
				}
			}
		}
		return fitnessChromDensities;
    }

	private void initChromosomesIfResume(EvoRunTask taskDef) {
		EvoRun<?> evoRun = taskDef.getEvoRun();

		Population<?> lastPopulation = ObjectUtil.getLast(evoRun.getPopulations());
		if (lastPopulation.hasChromosomes()) {
			List<Chromosome<?>> chromsomes = new ArrayList<Chromosome<?>>(lastPopulation.getChromosomes());
			for (Chromosome<?> chromosome : chromsomes) { 
				ObjectUtil.nullIdAndVersion(chromosome);
			}
			lastPopulation.removeAllChromosomes();
			taskDef.setInitChromosomes(chromsomes);
		} else {
			throw new CoelRuntimeException("In order to resume evolution run, the last population has to contain all chromosomes.");
		}
	}

	private <H extends Chromosome<C>, C> void initChromosomesIfGiven(
		EvoRunTask taskDef,
		EvoChromManipulatorBO<H, C> chromManipulator
	) {
		if (taskDef.getInitChromosome() == null) {
			return;
		}
		final int populationSize = taskDef.getGaSetting().getPopulationSize();
		List<Chromosome<?>> chromosomes = new ArrayList<Chromosome<?>>();
		for (int i = 0; i < populationSize; i++) {
			final H chromosome = chromManipulator.cloneChromosome((H) taskDef.getInitChromosome());
			ObjectUtil.nullIdAndVersion(chromosome);
			chromManipulator.mutatePerBit(chromosome, i / populationSize);
			chromosomes.add(chromosome);
		}
		taskDef.setInitChromosomes(chromosomes);
	}

	private <H extends Chromosome<C>, C, T> EvoTaskBO<H, C, T> createGridBasedEvoTaskBO(
		EvoTaskBO<H, C, T> evoTaskBO,
		EvoRunTask task
	) { 
		return new EvoTaskBO<H, C, T>(
				evoTaskBO.getChromManipulator(),
				new EvoFitnessGridEnabledEvaluatorBO<H, T>(
						computationalGrid,
						evoTaskBO.getFitnessEvaluator(),
						task.getMaxJobsInParallelNum() != null ? task.getMaxJobsInParallelNum() : defaultMaxJobsInParallelNum,
						task.getJobsInSequenceNum() != null ? task.getJobsInSequenceNum() : defaultJobsInSequenceNum),
				evoTaskBO.getTestSampleGenerator());
	}

	private EvoRunTask initEvoRunTaskIfNeeded(EvoRunTask task) {
		EvoRunTask evoRunTask = task;
		if (task.getId() != null && task.getGaSetting() == null) {
			evoRunTask = (EvoRunTask) taskDAO.get(task.getId());
		} else if (task.getEvoTask() != null) {
			if (task.getCreatedBy() == null) {
				User user = new User();
				user.setId(1l);
				task.setCreatedBy(user);
			}
			evoRunTask = (EvoRunTask) taskDAO.save(task);
		}

		evoRunTask = copyWithoutHibernate(evoRunTask);
		return evoRunTask;
	}

	private void initEvoTaskIfNeeded(EvoTaskHolder holder) {
		if (holder.isEvoTaskDefined()) {
			EvoTask evoTask = null;
			if (holder.isEvoTaskComplete()) {
				evoTask = holder.getEvoTask();
			} else {
				evoTask = evoTaskDAO.get(holder.getEvoTask().getId());
			}
			lazyInitEvoTask(evoTask);
			evoTask = copyWithoutHibernate(evoTask);
			evoTask.setEvolutionRuns(null);
			holder.setEvoTask(evoTask);
		}
	}

	private void lazyInitEvoTask(EvoTask evoTask) {
		evoTask.getCreatedBy().getUsername();
		if (evoTask instanceof EvoAcRateConstantTask) {
			EvoAcRateConstantTask evoAcTask = (EvoAcRateConstantTask) evoTask;
			// lazy loading of actions
			for (AcInteractionSeries actionSeries : evoAcTask.getActionSeries()) {
				initActionSeriesRecursively(actionSeries);
			}

			// lazy loading of fixed rate reactions
			evoAcTask.getFixedRateReactions().size();

			// lazy loading of fixed rate reaction groups
			evoAcTask.getFixedRateReactionGroups().size();

			evoAcTask.getRateConstantTypeBounds().size();
			evoAcTask.getAc().getName();
			loadLazyPropsOfCompartment(evoAcTask.getAc().getSkinCompartment());
			// lazy loading of translations
			AcEvaluation evaluation = evoAcTask.getAcEvaluation();
			evaluation.getTranslationSeries().getTranslations().size();
			evaluation.getTranslationSeries().getSpeciesSet().getVariables().size();
		}
		if (evoTask instanceof EvoAcInteractionSeriesTask) {
			EvoAcInteractionSeriesTask evoAcTask = (EvoAcInteractionSeriesTask) evoTask;
			// lazy loading of actions
			for (AcInteractionSeries actionSeries : evoAcTask.getActionSeries()) {
				initActionSeriesRecursively(actionSeries);
			}

			// lazy loading of species assignment bounds
			evoAcTask.getSpeciesAssignmentBounds().size();

			// lazy loading of variable assignment bounds
			evoAcTask.getVariableAssignmentBounds().size();

			evoAcTask.getAc().getName();
			loadLazyPropsOfCompartment(evoAcTask.getAc().getSkinCompartment());
			// lazy loading of translations
			AcEvaluation evaluation = evoAcTask.getAcEvaluation();
			evaluation.getTranslationSeries().getTranslations().size();
			evaluation.getTranslationSeries().getSpeciesSet().getVariables().size();
		}
		if (evoTask instanceof EvoAcSpecTask) {
			EvoAcSpecTask evoAcSpecTask = (EvoAcSpecTask) evoTask;
			evoAcSpecTask.getAcSpecBound().getName();
			evoAcSpecTask.getMultiRunAnalysisSpec().getName();
			evoAcSpecTask.getSimConfig().getName();
		}
		if (evoTask instanceof EvoNetworkTask) {
			EvoNetworkTask<?> evoNetworkTask = (EvoNetworkTask<?>) evoTask;
			// lazy loading of actions
			for (NetworkActionSeries<?> actionSeries : evoNetworkTask.getActionSeries()) {
				actionSeries.getActions().size();
			}
			evoNetworkTask.getNetwork().getName();
			evoNetworkTask.getSimConfig();

			evoNetworkTask.getEvaluation().getEvaluationItems().size();
		}
	}

	private void loadLazyPropsOfCompartment(AcCompartment compartment) {
		compartment.getChannels();
		compartment.getReactionSet().getReactions().size();
		for (AcCompartment subCompartment : compartment.getSubCompartments()) {
			loadLazyPropsOfCompartment(subCompartment);
		}
		for (AcCompartmentChannelGroup channelGroup : compartment.getSubChannelGroups()) {
			channelGroup.getChannels().size();
		}		
	}

	private void initActionSeriesRecursively(AcInteractionSeries actionSeries) {
		actionSeries.getActions().size();
		actionSeries.getVariables().size();
		actionSeries.getImmutableSpecies().size();
		for (AcInteractionSeries subActionSeries : actionSeries.getSubActionSeries()) {
			initActionSeriesRecursively(subActionSeries);
		}
	}

	private void loadLazyPropsOfTranslationSeries(AcTranslationSeries translationSeries) {
		translationSeries.getTranslations().size();
		translationSeries.getSpeciesSet().getName();
	}

	private void initEvoRunIfNeeded(EvoRunHolder holder) {
		if (holder.isEvoRunDefined()) {
			EvoRun<?> evoRun = null;
			if (holder.isEvoRunComplete()) {
				evoRun = holder.getEvoRun();
			} else {
				evoRun = evoRunDAO.get(holder.getEvoRun().getId());
			}
			// lazy loading of populations and chromosomes
			evoRun.getPopulations().size();
			for (Population<?> population : evoRun.getPopulations()) {
				population.getChromosomes().size();
			}
			lazyInitEvoTask(evoRun.getEvoTask());
			evoRun = copyWithoutHibernate(evoRun);
			evoRun.getEvoTask().setEvolutionRuns(null);
			holder.setEvoRun(evoRun);
		}
	}

	////////////////
	// INJECTIONS //
	////////////////

	public GenericDAO<EvoTask, Long> getEvoTaskDAO() {
		return evoTaskDAO;
	}

	public void setEvoTaskDAO(GenericDAO<EvoTask, Long> evoTaskDAO) {
		this.evoTaskDAO = evoTaskDAO;
	}
	
	public GenericDAO<EvoRun<?>, Long> getEvoRunDAO() {
		return evoRunDAO;
	}

	public void setEvoRunDAO(GenericDAO<EvoRun<?>, Long> evoRunDAO) {
		this.evoRunDAO = evoRunDAO;
	}

	public GeneticAlgorithmBOAutoSaveHandler getGeneticAlgorithmBOAutoSaveHandler() {
		return geneticAlgorithmBOAutoSaveHandler;
	}

	public void setGeneticAlgorithmBOAutoSaveHandler(GeneticAlgorithmBOAutoSaveHandler geneticAlgorithmBOAutoSaveHandler) {
		this.geneticAlgorithmBOAutoSaveHandler = geneticAlgorithmBOAutoSaveHandler;
	}

	public Integer getDefaultMaxJobsInParallelNum() {
		return defaultMaxJobsInParallelNum;
	}

	public void setDefaultMaxJobsInParallelNum(Integer defaultMaxJobsInParallelNum) {
		this.defaultMaxJobsInParallelNum = defaultMaxJobsInParallelNum;
	}

	public Integer getDefaultJobsInSequenceNum() {
		return defaultJobsInSequenceNum;
	}

	public void setDefaultJobsInSequenceNum(Integer defaultJobsInSequenceNum) {
		this.defaultJobsInSequenceNum = defaultJobsInSequenceNum;
	}
}