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
public class AcAsymmetricComparisons2 extends CoelTest {

	private static final long AC_ID = 1124l;
	private static final int RUN_TIME = 100;

	private static final double S1_CONC = 0;
	private static final double S2_CONC = 1.5;
	private static final double K_cat = 1d;
	private static final double K_m = 0.1;
	private static final double ANIH_DECAY_k = 1;

	private static final String FILE_NAME = "asymmetric_comparisons_ma_decay_" + ANIH_DECAY_k + "(0_1).out";

	@Autowired
	GenericDAO<ArtificialChemistry, Long> artificialChemistryDAO;

	@Autowired
	ArtificialChemistryService artificialChemistryService;

	ArtificialChemistryUtil acUtil = ArtificialChemistryUtil.getInstance();

	@Test
	public void runEnzymeAndProductConcentrationRelation() {
		final ArtificialChemistry ac = artificialChemistryDAO.get(AC_ID);
		final AcReactionSet reactionSet = ac.getReactionSet();
		final AcReaction catalytic1Reaction = reactionSet.getReactions().get(0);
		final AcReaction catalytic2Reaction = reactionSet.getReactions().get(1);
		final AcReaction anihDecay1Reaction = reactionSet.getReactions().get(2);
		final AcReaction anihDecay2Reaction = reactionSet.getReactions().get(3);

		final Map<String, AcSpecies> acLabelSpeciesMap = acUtil.getLabelVariableMap(ac.getSpeciesSet());
		final AcSpecies substrate1 = acLabelSpeciesMap.get("S1");
		final AcSpecies enzyme1 = acLabelSpeciesMap.get("E1");
		final AcSpecies substrate2 = acLabelSpeciesMap.get("S2");
		final AcSpecies enzyme2 = acLabelSpeciesMap.get("E2");
		final AcSpecies product = acLabelSpeciesMap.get("P");

		StringBuffer sb = new StringBuffer();
		for (double e1_conc = 0; e1_conc <= 5; e1_conc += 0.1)
		for (double e2_conc = 0; e2_conc <= 5; e2_conc += 0.1) {
			final AcInteractionSeries actionSeries = createActionSeries(
					substrate1, enzyme1, substrate2, enzyme2,
					S1_CONC, e1_conc, S2_CONC, e2_conc);

//			catalytic1Reaction.setForwardRateConstants(new Double[] {K_cat, K_m});
//			catalytic2Reaction.setForwardRateConstants(new Double[] {K_cat, K_m});
			catalytic1Reaction.setForwardRateConstant(K_cat);
			catalytic2Reaction.setForwardRateConstant(K_cat);
			anihDecay1Reaction.setForwardRateConstant(ANIH_DECAY_k);
			anihDecay2Reaction.setForwardRateConstant(ANIH_DECAY_k);

			actionSeries.setSpeciesSet(ac.getSpeciesSet());

			AcRunTask task = createAcRunTask(ac, actionSeries);
			Collection<ComponentRunTrace<Double, Pair<AcCompartment, AcSpecies>>> runs = artificialChemistryService.runSimulation(task);
			Map<Pair<AcCompartment, AcSpecies>, List<Double>> componentHistoryMap  = ObjectUtil.getFirst(runs).componentHistoryMap();
			final AcCompartment compartment = ObjectUtil.getFirst(componentHistoryMap.keySet()).getFirst();
			List<Double> productHistory = componentHistoryMap.get(new Pair<AcCompartment, AcSpecies>(compartment, product));
			double Pfinal = productHistory.get(productHistory.size() - 1);
			sb.append(e1_conc + ","  + e2_conc + "," + Pfinal);
			sb.append('\n');
			System.out.println(e1_conc + ","  + e2_conc + "," + Pfinal);
		}
		FileUtil.getInstance().overwriteStringToFileSafe(sb.toString(), FILE_NAME);
	}

	private AcRunTask createAcRunTask(ArtificialChemistry ac, AcInteractionSeries actionSeries) {
		AcRunTask acTask = new AcRunTask();
		acTask.setCompartment(ac.getSkinCompartment());
		acTask.setSimulationConfig(ac.getSimulationConfig());
		acTask.setActionSeries(actionSeries);
		acTask.setRunTime(RUN_TIME);
		acTask.setRepetitions(1);
		return acTask;
	}

	private AcInteractionSeries createActionSeries(
		AcSpecies substrate1,
		AcSpecies enzyme1,
		AcSpecies substrate2,
		AcSpecies enzyme2,
		double substrate1Conc,
		double enzyme1Conc,
		double substrate2Conc,
		double enzyme2Conc
	) {
		AcInteractionSeries as = new AcInteractionSeries();
		AcInteraction action = new AcInteraction();
		action.setStartTime(0);
		action.setTimeLength(0d);
		action.setAlternationType(StateAlternationType.Replacement);
		as.addAction(action);

		final AcSpeciesInteraction speciesAction1 = new AcSpeciesInteraction();
		speciesAction1.setSettingFunction(new Expression<Double, Double>("" + substrate1Conc));
		speciesAction1.setSpecies(substrate1);
		action.addToSpeciesActions(speciesAction1);

		final AcSpeciesInteraction speciesAction2 = new AcSpeciesInteraction();
		speciesAction2.setSettingFunction(new Expression<Double, Double>("" + enzyme1Conc));
		speciesAction2.setSpecies(enzyme1);
		action.addToSpeciesActions(speciesAction2);

		final AcSpeciesInteraction speciesAction3 = new AcSpeciesInteraction();
		speciesAction3.setSettingFunction(new Expression<Double, Double>("" + substrate2Conc));
		speciesAction3.setSpecies(substrate2);
		action.addToSpeciesActions(speciesAction3);

		final AcSpeciesInteraction speciesAction4 = new AcSpeciesInteraction();
		speciesAction4.setSettingFunction(new Expression<Double, Double>("" + enzyme2Conc));
		speciesAction4.setSpecies(enzyme2);
		action.addToSpeciesActions(speciesAction4);

		return as;
	}
}