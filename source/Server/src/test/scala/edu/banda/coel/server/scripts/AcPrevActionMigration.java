package edu.banda.coel.server.scripts;

import java.util.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.banda.chemistry.business.ArtificialChemistryUtil;
import com.banda.chemistry.domain.*;
import com.banda.core.util.ObjectUtil;
import com.banda.function.business.FunctionUtility;
import com.banda.function.domain.Expression;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.CoelTest;

@Transactional
public class AcPrevActionMigration extends CoelTest {

	private static final Long FROM = 1207l;
	private static final Long TO = 1222l;

	@Autowired
	GenericDAO<AcInteractionSeries, Long> acInteractionSeriesDAO;

	@Autowired
	GenericDAO<AcInteractionVariableAssignment, Long> acInteractionVariableAssignmentDAO;

	private final ArtificialChemistryUtil acUtil = ArtificialChemistryUtil.getInstance();
	private final FunctionUtility functionUtility = new FunctionUtility(); 

//	@Test
//	@Rollback(false)
	public void migrate() {
		Collection<AcInteractionSeries> actionSeries = acInteractionSeriesDAO.get(range(FROM, TO));		
		for (AcInteractionSeries oneActionSeries : actionSeries) {
			if (isInteractionVariableAssignmentNeeded(oneActionSeries)) {
				final AcInteractionVariable firstVariable = ObjectUtil.getFirst(oneActionSeries.getVariables());
				final AcInteractionVariable secondVariable = ObjectUtil.getSecond(oneActionSeries.getVariables());

				final AcInteractionVariable x1Inj = firstVariable.getLabel().equals("X1_inj") ? firstVariable : secondVariable;
				final AcInteractionVariable x2Inj = firstVariable.getLabel().equals("X2_inj") ? firstVariable : secondVariable;

				for (AcInteraction action : oneActionSeries.getActions()) {
					if (action.getStartTime().equals(100)) {
						// X1 inj write
						AcInteractionVariableAssignment x1InteractionVariableAssignment = new AcInteractionVariableAssignment();
						x1InteractionVariableAssignment.setVariable(x1Inj);
						action.addVariableAssignment(x1InteractionVariableAssignment);
						acUtil.setSettingFunctionFromString("rand() < 0.5", x1InteractionVariableAssignment);

						// X2 inj write
						AcInteractionVariableAssignment x2InteractionVariableAssignment = new AcInteractionVariableAssignment();
						x2InteractionVariableAssignment.setVariable(x2Inj);
						action.addVariableAssignment(x2InteractionVariableAssignment);
						acUtil.setSettingFunctionFromString("rand() < 0.5", x2InteractionVariableAssignment);

						for (AcSpeciesInteraction speciesAction : action.getSpeciesActions()) {
							if (speciesAction.getSpecies().getLabel().equals("X1")) {
								((Expression<?,?>) speciesAction.getFunction()).setFormula("x1");
							}
							if (speciesAction.getSpecies().getLabel().equals("X2")) {
								((Expression<?,?>) speciesAction.getFunction()).setFormula("x3");
							}
						}
					}

					if (action.getStartTime().equals(200)) {
						for (AcSpeciesInteraction speciesAction : action.getSpeciesActions()) {
							final Expression<?, ?> settingFunction = (Expression<?, ?>) speciesAction.getSettingFunction();
							Map<Integer, Integer> migrationMap = new HashMap<Integer, Integer>();
							migrationMap.put(20, 1);
							migrationMap.put(30, 3);
							final String newFormula = functionUtility.getFormulaWithReplacedIndexPlaceholders(settingFunction, migrationMap);
							settingFunction.setFormula(newFormula);
						}
					}
				}

//				acInteractionSeriesDAO.save(oneActionSeries);
			}
		}
	}

	@Test
	@Rollback(false)
	public void removeDuplicateInteractionVariableAssignments() {
		Collection<AcInteractionSeries> actionSeries = acInteractionSeriesDAO.get(range(FROM, TO));		
		for (AcInteractionSeries oneActionSeries : actionSeries) {
			if (isInteractionVariableAssignmentNeeded(oneActionSeries)) {
				for (AcInteraction action : oneActionSeries.getActions()) {
					if (action.getStartTime().equals(100)) {
						List<AcInteractionVariableAssignment> x1InjWrites = new ArrayList<AcInteractionVariableAssignment>();
						List<AcInteractionVariableAssignment> x2InjWrites = new ArrayList<AcInteractionVariableAssignment>();
						for (AcInteractionVariableAssignment assignment : action.getVariableAssignments()) {
							if (assignment.getVariable().getLabel().equals("X1_inj")) {
								x1InjWrites.add(assignment);
							}
							if (assignment.getVariable().getLabel().equals("X2_inj")) {
								x2InjWrites.add(assignment);
							}
						}

						if (x1InjWrites.size() > 1) {
							action.removeVariableAssignment(x1InjWrites.get(0));
						}
						if (x2InjWrites.size() > 1) {
							action.removeVariableAssignment(x2InjWrites.get(0));
						}
					}
				}
			}
		}
	}


	private Collection<Long> range(Long from, Long to) {
		Collection<Long> ids = new ArrayList<Long>();
		for (long id = from; id <= to; id++) {
			ids.add(id);
		}
		return ids;
	}

	private boolean isInteractionVariableAssignmentNeeded(AcInteractionSeries actionSeries) {
//		for (AcInteraction action : actionSeries.getActions()) {
//			if (action.isUseAllPreviousActionMagnitudes() || action.hasPreviousActionSpecies()) {
//				return true;
//			}
//		}
		return false;
	}
}