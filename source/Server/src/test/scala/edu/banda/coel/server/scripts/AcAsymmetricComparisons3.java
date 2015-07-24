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
public class AcAsymmetricComparisons3 extends CoelTest {

	private static final long AC_ID = 1121l;
	private static final int RUN_TIME = 200;

	private static final double K_m = 0.1;
	private static final double k1_cat = 1d;
	private static final double k2_cat = 1d;

	private static final String FILE_NAME = "asymmetric_comparisons_mm_anih";

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
		final AcReaction anih1Reaction = reactionSet.getReactions().get(2);
		final AcReaction anih2Reaction = reactionSet.getReactions().get(3);

		final Map<String, AcSpecies> acLabelSpeciesMap = acUtil.getLabelVariableMap(ac.getSpeciesSet());
		final AcSpecies substrate1 = acLabelSpeciesMap.get("S1");
		final AcSpecies enzyme1 = acLabelSpeciesMap.get("E1");
		final AcSpecies substrate2 = acLabelSpeciesMap.get("S2");
		final AcSpecies enzyme2 = acLabelSpeciesMap.get("E2");
		final AcSpecies product = acLabelSpeciesMap.get("P");

		for (double anih_k = 1; anih_k >= 0; anih_k -= 0.2)
		for (double sub1_conc = 0; sub1_conc <= 1; sub1_conc += 0.2)
		for (double sub2_conc = 0; sub2_conc <= 1; sub2_conc += 0.2) {
			StringBuffer sb = new StringBuffer();
			sb.append("[W1],[W2],[Y]/([X1]+[X2])\n");

			for (double cat1_conc = 0; cat1_conc <= 2; cat1_conc += 0.25)
			for (double cat2_conc = 0; cat2_conc <= 2; cat2_conc += 0.25) {
				final AcInteractionSeries actionSeries = createActionSeries(
					substrate1, enzyme1, substrate2, enzyme2,
					sub1_conc, cat1_conc, sub2_conc, cat2_conc);

				catalytic1Reaction.setForwardRateConstants(new Double[] {k1_cat, K_m});
				catalytic2Reaction.setForwardRateConstants(new Double[] {k2_cat, K_m});
				anih1Reaction.setForwardRateConstant(anih_k);
				anih2Reaction.setForwardRateConstant(anih_k);

				actionSeries.setSpeciesSet(ac.getSpeciesSet());

				AcRunTask task = createAcRunTask(ac, actionSeries);
				Collection<ComponentRunTrace<Double, Pair<AcCompartment, AcSpecies>>> runs = artificialChemistryService.runSimulation(task);
				Map<Pair<AcCompartment, AcSpecies>, List<Double>> componentHistoryMap  = ObjectUtil.getFirst(runs).componentHistoryMap();
				final AcCompartment compartment = ObjectUtil.getFirst(componentHistoryMap.keySet()).getFirst();
				List<Double> productHistory = componentHistoryMap.get(new Pair<AcCompartment, AcSpecies>(compartment, product));
				double Pfinal = productHistory.get(productHistory.size() - 1);
				sb.append(cat1_conc + "," + cat2_conc + ","  + (Pfinal / (sub1_conc + sub2_conc)));
				sb.append('\n');
				System.out.println(cat1_conc + "," + cat2_conc + ","  + (Pfinal / (sub1_conc + sub2_conc)));
			}
			FileUtil.getInstance().overwriteStringToFileSafe(sb.toString(), FILE_NAME + "_anih_" + round2(anih_k) + "_x1_" + round2(sub1_conc) + "_x2_" + round2(sub2_conc) +".csv");
		}
	}

	private String round2(double value) {
		double val = ((double) Math.round(100 * value)) / 100;
		return String.valueOf(val).replace('.', '_');
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