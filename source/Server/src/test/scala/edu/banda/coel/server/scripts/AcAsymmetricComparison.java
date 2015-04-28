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
import com.banda.core.util.ObjectUtil;
import com.banda.function.domain.Expression;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.domain.service.ArtificialChemistryService;
import edu.banda.coel.server.CoelTest;
import edu.banda.coel.task.chemistry.AcRunTask;

@Transactional
public class AcAsymmetricComparison extends CoelTest {

	private static final long AC_COMPARTMENT_ID = 1035l;
	private static final long AC_SIMULATION_CONFIG_ID = 1010l;
	private static final int RUN_TIME = 1000;

	@Autowired
	GenericDAO<AcCompartment, Long> acCompartmentDAO;

	@Autowired
	ArtificialChemistryService artificialChemistryService;

	ArtificialChemistryUtil acUtil = ArtificialChemistryUtil.getInstance();

	@Test
	public void runEnzymeAndProductConcentrationRelation() {
		AcCompartment compartment = acCompartmentDAO.get(AC_COMPARTMENT_ID);
		Map<String, AcSpecies> acLabelSpeciesMap = acUtil.getLabelVariableMap(compartment.getSpeciesSet());
		final AcSpecies substrate = acLabelSpeciesMap.get("S");
		final AcSpecies enzyme = acLabelSpeciesMap.get("E");
		final AcSpecies product = acLabelSpeciesMap.get("P");

		StringBuffer sb = new StringBuffer();
		for (double E0 = 0.05; E0 < 5; E0+=0.01) {
			AcInteractionSeries actionSeries = createActionSeries(substrate, enzyme, E0);
			actionSeries.setSpeciesSet(compartment.getSpeciesSet());
			AcRunTask task = createAcRunTask(actionSeries);
			Collection<ComponentRunTrace<Double, Pair<AcCompartment, AcSpecies>>> runs = artificialChemistryService.runSimulation(task);

			Map<Pair<AcCompartment, AcSpecies>, List<Double>> componentHistoryMap  = ObjectUtil.getFirst(runs).componentHistoryMap();
			final AcCompartment aCompartment = ObjectUtil.getFirst(componentHistoryMap.keySet()).getFirst();
			List<Double> productHistory = componentHistoryMap.get(new Pair<AcCompartment, AcSpecies>(aCompartment, product));
			double Pfinal = productHistory.get(productHistory.size() - 1);
			sb.append(E0 + "," + Pfinal);
			sb.append(Pfinal);
			sb.append('\n');
		}
		System.out.println(sb.toString());
	}

	private AcRunTask createAcRunTask(AcInteractionSeries actionSeries) {
		AcRunTask acTask = new AcRunTask();
		acTask.setCompartmentId(new Long(AC_COMPARTMENT_ID));
		acTask.setSimulationConfigId(new Long(AC_SIMULATION_CONFIG_ID));
		acTask.setActionSeries(actionSeries);
		acTask.setRunTime(RUN_TIME);
		acTask.setRepetitions(1);
		return acTask;
	}

	private AcInteractionSeries createActionSeries(AcSpecies substrate, AcSpecies enzyme, double enzymeConc) {
		AcInteractionSeries as = new AcInteractionSeries();
		AcInteraction action = new AcInteraction();
		action.setStartTime(0);
		action.setTimeLength(0d);
		action.setAlternationType(StateAlternationType.Replacement);
		as.addAction(action);

		AcSpeciesInteraction speciesAction1 = new AcSpeciesInteraction();
		speciesAction1.setSettingFunction(new Expression<Double, Double>("1"));
		speciesAction1.setSpecies(substrate);
		action.addToSpeciesActions(speciesAction1);

		AcSpeciesInteraction speciesAction2 = new AcSpeciesInteraction();
		speciesAction2.setSettingFunction(new Expression<Double, Double>("" + enzymeConc));
		speciesAction2.setSpecies(enzyme);
		action.addToSpeciesActions(speciesAction2);

		return as;
	}
}