package edu.banda.coel.server.scripts;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.banda.chemistry.business.ArtificialChemistryUtil;
import com.banda.chemistry.domain.*;
import com.banda.core.domain.um.User;
import com.banda.core.dynamics.StateAlternationType;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.CoelTest;

@Transactional
public class AcNarmaActionSeriesCreation extends CoelTest {
 
//	private static final long SPECIES_SET_ID = 1142;
	private static final long SPECIES_SET_ID = 3476;
	private static final int NARMA_ORDER_FROM = 2;
	private static final int NARMA_ORDER_TO = 10;

	@Autowired
	GenericDAO<AcInteractionSeries, Long> acInteractionSeriesDAO;

	@Autowired
	GenericDAO<AcSpeciesSet, Long> acSpeciesSetDAO;

	private final ArtificialChemistryUtil acUtil = ArtificialChemistryUtil.getInstance(); 

	@Test
	@Rollback(false)
	public void createAndSaveNarmaActionSeries() {
		final AcSpeciesSet speciesSet = acSpeciesSetDAO.get(SPECIES_SET_ID);
		for (int narmaOrder = NARMA_ORDER_FROM; narmaOrder <= NARMA_ORDER_TO; narmaOrder++) {
			for (int inputOrder = 2; inputOrder <= 5; inputOrder++) {
				final AcInteractionSeries actionSeries1 = createNarmaActionSeries(speciesSet, narmaOrder, inputOrder, 0.1, 3);
				acInteractionSeriesDAO.save(actionSeries1);
//				final AcInteractionSeries actionSeries1 = createNarmaActionSeries(speciesSet, narmaOrder, inputOrder, 0.1, 1);
//				acInteractionSeriesDAO.save(actionSeries1);
//				final AcInteractionSeries actionSeries2 = createNarmaActionSeries(speciesSet, narmaOrder, inputOrder, 0.1, 2);
//				acInteractionSeriesDAO.save(actionSeries2);
//				final AcInteractionSeries actionSeries3 = createNarmaActionSeries(speciesSet, narmaOrder, inputOrder, 1, 1);
//				acInteractionSeriesDAO.save(actionSeries3);
//				final AcInteractionSeries actionSeries4 = createNarmaActionSeries(speciesSet, narmaOrder, inputOrder, 1, 2);
//				acInteractionSeriesDAO.save(actionSeries4);
			}
		}
	}

	private AcInteractionSeries createNarmaActionSeries(
		AcSpeciesSet speciesSet,
		int narmaOrder,
		int inputOrder,
		double sin,
		double outputScale
	) {
		AcInteractionSeries interactionSeries = new AcInteractionSeries();
		interactionSeries.setPeriodicity(500);
		interactionSeries.setRepeatFromElement(1);
		interactionSeries.setSpeciesSet(speciesSet);
		String scaleString = outputScale == 1 ? "" : outputScale == Math.floor(outputScale) ? "" + ((int) outputScale) + " *" : "" + outputScale + " *";
		interactionSeries.setName("AASP-DL-" + inputOrder + " " + scaleString + " NARMA" + narmaOrder + " IN (x0 = " + sin + ", 0 < x < 0.5) (50/500)");
		User user = new User();
		user.setId(1l);
		interactionSeries.setCreatedBy(user);

		interactionSeries.addVariable("alpha");
		interactionSeries.addVariable("beta");
		interactionSeries.addVariable("gamma");
		interactionSeries.addVariable("delta");
		interactionSeries.addVariable("Xt");
		interactionSeries.addVariable("YEt");

		acInteractionSeriesDAO.save(interactionSeries);

		for (int index = 1; index <= narmaOrder; index++) {
			interactionSeries.addVariable("Xt" + index);
			interactionSeries.addVariable("YEt" + index);
		}

		final Map<String, AcInteractionVariable> variableMap = acUtil.getLabelVariableMap(interactionSeries);
		final Map<String, AcSpecies> speciesMap = acUtil.getLabelVariableMap(speciesSet);

		// interaction 0
		final AcInteraction interaction0 = createInteraction(0);
		interactionSeries.addAction(interaction0);
		addVariableAssignment(interaction0, variableMap.get("alpha"), "0.3");
		addVariableAssignment(interaction0, variableMap.get("beta"), "0.05");
		addVariableAssignment(interaction0, variableMap.get("gamma"), "1.5");
		addVariableAssignment(interaction0, variableMap.get("delta"), "0.1");

		for (int index = 1; index <= narmaOrder; index++) {
			addVariableAssignment(interaction0, variableMap.get("Xt" + index), "0");
			addVariableAssignment(interaction0, variableMap.get("YEt" + index), "0");
		}

		for (int index = 0; index <= inputOrder; index++) {
			addSpeciesAction(interaction0, speciesMap.get("W" + index), "random(0.5,1.5)");
		}

		// interaction 1
		final AcInteraction interaction1 = createInteraction(1);
		interactionSeries.addAction(interaction1);
		addVariableAssignment(interaction1, variableMap.get("Xt"), "random(0,0.5)");
		addSpeciesAction(interaction1, speciesMap.get("B"), "0");
		addSpeciesAction(interaction1, speciesMap.get("Y"), "0");
		addSpeciesAction(interaction1, speciesMap.get("S1"), "1");
		addSpeciesAction(interaction1, speciesMap.get("SL"), "0");
		addSpeciesAction(interaction1, speciesMap.get("Sin"), "" + sin);
		addSpeciesAction(interaction1, speciesMap.get("SinY"), "0");
//		addSpeciesAction(interaction1, speciesMap.get("X"), "YEt1");
		addSpeciesAction(interaction1, speciesMap.get("X"), "Xt1");

		for (int index = 1; index <= inputOrder; index++) {
			addSpeciesAction(interaction1, speciesMap.get("X" + index), "0");
			addSpeciesAction(interaction1, speciesMap.get("X" + index + "Y"), "0");
		}

		// interaction 26
		final AcInteraction interaction26 = createInteraction(26);
		interactionSeries.addAction(interaction26);
		addSpeciesAction(interaction26, speciesMap.get("S2"), "1");

		// interaction 51
		final AcInteraction interaction51 = createInteraction(51);
		interactionSeries.addAction(interaction51);

		List<String> yets = new ArrayList<String>();
		for (int index = 1; index <= narmaOrder; index++) {
			yets.add("YEt" + index);
		}
		String yetExpression = "alpha * YEt1 + beta * YEt1 * (" + StringUtils.join(yets, " + ") + ") + gamma * Xt1 * Xt" + narmaOrder + " + delta";
//		System.out.println(yetExpression);
		addVariableAssignment(interaction51, variableMap.get("YEt"), yetExpression);

		if (outputScale == 1) {
			addSpeciesAction(interaction51, speciesMap.get("B"), "YEt");
		} else {
			addSpeciesAction(interaction51, speciesMap.get("B"), outputScale + " * YEt");
		}
		addSpeciesAction(interaction51, speciesMap.get("SL"), "1");

		// interactions 52+
		for (int index = 1; index < narmaOrder; index++) {
			final AcInteraction interaction5x = createInteraction(51 + index);
			interactionSeries.addAction(interaction5x);

			addVariableAssignment(interaction5x, variableMap.get("Xt" + (narmaOrder - index + 1)), "Xt" + (narmaOrder - index));
			addVariableAssignment(interaction5x, variableMap.get("YEt" + (narmaOrder - index + 1)), "YEt" + (narmaOrder - index));
		}

		final AcInteraction interaction5x = createInteraction(51 + narmaOrder);
		interactionSeries.addAction(interaction5x);

		addVariableAssignment(interaction5x, variableMap.get("Xt1"), "Xt");
		addVariableAssignment(interaction5x, variableMap.get("YEt1"), "YEt");

		return interactionSeries;
	}

	private void addVariableAssignment(
		AcInteraction interaction,
		AcInteractionVariable variable,
		String expression
	) {
		AcInteractionVariableAssignment variableAssignment = new AcInteractionVariableAssignment(variable);
		interaction.addVariableAssignment(variableAssignment);
		acUtil.setSettingFunctionFromString(expression, variableAssignment);		
	}

	private void addSpeciesAction(
		AcInteraction interaction,
		AcSpecies species,
		String expression
	) {
		AcSpeciesInteraction speciesAction = new AcSpeciesInteraction();
		speciesAction.setSpecies(species);
		interaction.addToSpeciesActions(speciesAction);
		acUtil.setSettingFunctionFromString(expression, speciesAction);		
	}

	private AcInteraction createInteraction(int startTime) {
		final AcInteraction interaction = new AcInteraction();
		interaction.setStartTime(startTime);
		interaction.setTimeLength(0d);
		interaction.setAlternationType(StateAlternationType.Replacement);
		return interaction;
	}
}