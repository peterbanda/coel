package edu.banda.coel.server.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.banda.chemistry.domain.AcEvaluation;
import com.banda.chemistry.domain.AcInteractionSeries;
import com.banda.chemistry.domain.AcTranslationSeries;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.CoelRuntimeException;
import edu.banda.coel.domain.service.ArtificialChemistryService;
import edu.banda.coel.task.chemistry.AcPerformanceEvaluateTask;
import edu.banda.coel.task.chemistry.AcTaskParts.AcMultipleEvaluationHolder;

/**
 * Title: ArtificialChemistryServiceImpl
 *
 * @see edu.banda.coel.domain.service.ArtificialChemistryService
 * @author Peter Banda
 * @since 2011
 */
public class ArtificialChemistryServiceAsyncHelper extends AbstractService {

	@Autowired
	private GenericDAO<AcEvaluation, Long> acEvaluationDAO;
	
	// ac service ref needed for Async... remove once AspectJ is proxying is activated
	@Autowired
	private ArtificialChemistryService artificialChemistryService;

	@Transactional
	public void runPerformanceEvaluation(AcPerformanceEvaluateTask task) {
		log.info("AC performance evaluation for AC compartment '" + task.getCompartmentId() + "' and evaluations '" + task.getAcEvaluationIds() + "' is about to be executed.");
		initMultipleAcEvaluationsIfNeeded(task);
    	if (task.getRepetitions() == null) {
    		throw new CoelRuntimeException("Repetitions missing for ac random rate performance evaluation task.");
    	}

		List<AcInteractionSeries> sortedActionSeries = new ArrayList<AcInteractionSeries>(task.getActionSeries());
		Collections.sort(sortedActionSeries, new AcInteractionSeriesIdComparator());
		List<AcEvaluation> sortedEvaluations = new ArrayList<AcEvaluation>(task.getAcEvaluations());
		Collections.sort(sortedEvaluations, new AcEvaluationIdComparator());

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
				artificialChemistryService.runPerformanceEvaluation(task, actionSeries, sharedTranslationEvaluations);
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

	private void loadLazyPropsOfTranslationSeries(AcTranslationSeries translationSeries) {
		translationSeries.getTranslations().size();
		translationSeries.getSpeciesSet().getName();
	}
}