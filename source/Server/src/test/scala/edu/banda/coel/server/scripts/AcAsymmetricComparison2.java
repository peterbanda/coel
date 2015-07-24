package edu.banda.coel.server.scripts;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.banda.chemistry.business.ArtificialChemistryUtil;
import com.banda.chemistry.domain.*;
import com.banda.core.Pair;
import com.banda.core.domain.ComponentRunTrace;
import com.banda.core.dynamics.StateAlternationType;
import com.banda.core.util.FileUtil;
import com.banda.core.util.ObjectUtil;
import com.banda.function.domain.Expression;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.domain.service.ArtificialChemistryService;
import edu.banda.coel.server.CoelTest;
import edu.banda.coel.task.chemistry.AcRunTask;

@Transactional
public class AcAsymmetricComparison2 extends CoelTest {

	private static final long AC_ID = 1035l;
	private static final int RUN_TIME = 100;
	private static final double CATALIST_CONC = 1d;
	private static final double SUBSTRATE_CONC = 1d;

	@Autowired
	GenericDAO<ArtificialChemistry, Long> artificialChemistryDAO;

	@Autowired
	ArtificialChemistryService artificialChemistryService;

	ArtificialChemistryUtil acUtil = ArtificialChemistryUtil.getInstance();

	@Test
	public void runEnzymeAndProductConcentrationRelation() {
		final ArtificialChemistry ac = artificialChemistryDAO.get(AC_ID);
		final AcReactionSet reactionSet = ac.getReactionSet();
		final AcReaction catalyticReaction = reactionSet.getReactions().get(0);
		final AcReaction decayReaction = reactionSet.getReactions().get(1);

		final Map<String, AcSpecies> acLabelSpeciesMap = acUtil.getLabelVariableMap(ac.getSpeciesSet());
		final AcSpecies substrate = acLabelSpeciesMap.get("S");
		final AcSpecies enzyme = acLabelSpeciesMap.get("E");
		final AcSpecies product = acLabelSpeciesMap.get("P");

		StringBuffer sb = new StringBuffer();
		double K_m = 0.1;
		for (double k2 = 1; k2 >= 0; k2 -= 0.02)
		for (double k_cat = 0; k_cat <= 1; k_cat += 0.02) {
			final AcInteractionSeries actionSeries = createActionSeries(substrate, enzyme, SUBSTRATE_CONC, CATALIST_CONC);
			catalyticReaction.setForwardRateConstants(new Double[] {k_cat, K_m});
			decayReaction.setForwardRateConstant(k2);
			actionSeries.setSpeciesSet(ac.getSpeciesSet());

			AcRunTask task = createAcRunTask(ac, actionSeries);
			Collection<ComponentRunTrace<Double, Pair<AcCompartment, AcSpecies>>> runs = artificialChemistryService.runSimulation(task);
			Map<Pair<AcCompartment, AcSpecies>, List<Double>> componentHistoryMap  = ObjectUtil.getFirst(runs).componentHistoryMap();
			final AcCompartment compartment = ObjectUtil.getFirst(componentHistoryMap.keySet()).getFirst();
			List<Double> productHistory = componentHistoryMap.get(new Pair<AcCompartment, AcSpecies>(compartment, product));
			double Pfinal = productHistory.get(productHistory.size() - 1);
			sb.append(K_m + "," + k_cat + "," + k2 + "," + Pfinal);
			sb.append('\n');
			System.out.println(K_m + "," + k_cat + "," + k2 + "," + Pfinal);
		}
		FileUtil.getInstance().overwriteStringToFileSafe(sb.toString(), "asymmetric_comparison_kcat_k_anih.out");
	}

	private AcRunTask createAcRunTask(ArtificialChemistry ac, AcInteractionSeries actionSeries) {
		AcRunTask acTask = new AcRunTask();
		acTask.setCompartment(ac.getSkinCompartment());
		acTask.setSimulationConfig(ac.getSimulationConfig());
		acTask.setInteractionSeries(actionSeries);
		acTask.setRunTime(RUN_TIME);
		acTask.setRepetitions(1);
		return acTask;
	}

	private AcInteractionSeries createActionSeries(AcSpecies substrate, AcSpecies enzyme, double substrateConc, double enzymeConc) {
		AcInteractionSeries as = new AcInteractionSeries();
		AcInteraction action = new AcInteraction();
		action.setStartTime(0);
		action.setTimeLength(0d);
		action.setAlternationType(StateAlternationType.Replacement);
		as.addAction(action);

		AcSpeciesInteraction speciesAction1 = new AcSpeciesInteraction();
		speciesAction1.setSettingFunction(new Expression<Double, Double>("" + substrateConc));
		speciesAction1.setSpecies(substrate);
		action.addToSpeciesActions(speciesAction1);

		AcSpeciesInteraction speciesAction2 = new AcSpeciesInteraction();
		speciesAction2.setSettingFunction(new Expression<Double, Double>("" + enzymeConc));
		speciesAction2.setSpecies(enzyme);
		action.addToSpeciesActions(speciesAction2);

		return as;
	}
}