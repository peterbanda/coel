package edu.banda.coel.server.scripts;

import java.util.*;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.banda.chemistry.business.ArtificialChemistryUtil;
import com.banda.chemistry.domain.*;
import com.banda.function.business.FunctionUtility;
import com.banda.function.domain.Expression;
import com.banda.function.domain.FunctionHolder;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.CoelRuntimeException;
import edu.banda.coel.server.CoelTest;

@Transactional
@Ignore
public class AcHierarchicalSpeciesSetMigration extends CoelTest {

	private final static Set<Long> SPECIAL_SPECIES_SET_IDS = new HashSet<Long>();
	static {
		SPECIAL_SPECIES_SET_IDS.add(new Long(1013));
		SPECIAL_SPECIES_SET_IDS.add(new Long(1015));
		SPECIAL_SPECIES_SET_IDS.add(new Long(1016));
	}
	private final static int[][] SPECIAL_SPECIES_INDEX_MIGRATION_PAIRS = new int[][]{
		{105, 27},
		{104, 22},
		{103, 17},
		{102, 12},
		{101, 7},
		{100, 2},
		{28, 70},
		{21, 65},
		{20, 6},
		{19, 21},
		{18, 60},
		{17, 11},
		{16, 55},
		{15, 50},
		{14, 45},
		{13, 40},
		{12, 35},
		{11, 36},
		{10, 1},
		{9, 26},
		{8, 16},
		{7, 31},
		{6, 30},
		{5, 25},
		{4, 20},
		{3, 15},
		{2, 10},
		{1, 5},
		{0, 0}
	};

	private final static Map<Integer, Integer> SPECIES_INDEX_SIMPLE_5X_MAP = createMapWith5Mult(100);
	private final static Map<Integer, Integer> SPECIAL_SPECIES_INDEX_MIGRATION_MAP = createMap(SPECIAL_SPECIES_INDEX_MIGRATION_PAIRS);
	
	private static Map<Integer, Integer> createMap(int[][] speciesIndexMigrationPairs) {
		Map<Integer, Integer> speciesIndexMigrationMap = new HashMap<Integer, Integer>();
		for (int[] speciesIdPair : speciesIndexMigrationPairs) {
			if (speciesIdPair.length != 2) {
				throw new CoelRuntimeException("Pair of species ids expected.");
			}
			speciesIndexMigrationMap.put(new Integer(speciesIdPair[0]), new Integer(speciesIdPair[1]));
		}
		return speciesIndexMigrationMap;
	}

	private static Map<Integer, Integer> createMapWith5Mult(int count) {
		Map<Integer, Integer> speciesIndexMigrationMap = new HashMap<Integer, Integer>();
		for (int originalIndex = 0; originalIndex < count; originalIndex++) {
			speciesIndexMigrationMap.put(new Integer(originalIndex), new Integer(5 * originalIndex));
		}
		return speciesIndexMigrationMap;
	}

	@Autowired
	@Qualifier(value="acReactionSetDAO")
	GenericDAO<AcReactionSet, Long> acReactionSetDAO;

	@Autowired
	@Qualifier(value="acParameterSetDAO")
	GenericDAO<AcParameterSet, Long> acParameterSetDAO;

	@Autowired
	@Qualifier(value="acInteractionSeriesDAO")
	GenericDAO<AcInteractionSeries, Long> acInteractionSeriesDAO;

	@Autowired
	@Qualifier(value="acTranslationSeriesDAO")
	GenericDAO<AcTranslationSeries, Long> acTranslationSeriesDAO;

	ArtificialChemistryUtil acUtil = ArtificialChemistryUtil.getInstance();

	@Test
	@Rollback(false)
	public void migrateReactions() {
		Collection<AcReactionSet> reactionSets = acReactionSetDAO.getAll();
		for (AcReactionSet reactionSet : reactionSets) {
			if (SPECIAL_SPECIES_SET_IDS.contains(reactionSet.getSpeciesSet().getId())) {
				replaceIndexPlaceholders(reactionSet.getReactions(), SPECIAL_SPECIES_INDEX_MIGRATION_MAP);				
			} else {
				replaceIndexPlaceholders(reactionSet.getReactions(), SPECIES_INDEX_SIMPLE_5X_MAP);
			}
		}

		assertTrue(true);
	}

	@Test
 	@Rollback(false)
	public void migrateParameters() {
		Collection<AcParameterSet> parameterSets = acParameterSetDAO.getAll();
		for (AcParameterSet parameterSet : parameterSets) {
			if (SPECIAL_SPECIES_SET_IDS.contains(parameterSet.getSpeciesSet().getId())) {
				replaceIndexPlaceholders(parameterSet.getVariables(), SPECIAL_SPECIES_INDEX_MIGRATION_MAP);				
			} else {
				replaceIndexPlaceholders(parameterSet.getVariables(), SPECIES_INDEX_SIMPLE_5X_MAP);
			}
		}

		assertTrue(true);
	}

	@Test
    @Rollback(false)
	public void migrateSpeciesActions() {
		Collection<AcInteractionSeries> actionSeries = acInteractionSeriesDAO.getAll();
		for (AcInteractionSeries oneActionSeries : actionSeries) {
			Collection<AcSpeciesInteraction> speciesActions = new ArrayList<AcSpeciesInteraction>();
			for (AcInteraction action : oneActionSeries.getActions()) {
				speciesActions.addAll(action.getSpeciesActions());
			}
			if (SPECIAL_SPECIES_SET_IDS.contains(oneActionSeries.getSpeciesSet().getId())) {
				replaceIndexPlaceholders(speciesActions, SPECIAL_SPECIES_INDEX_MIGRATION_MAP);				
			} else {
				replaceIndexPlaceholders(speciesActions, SPECIES_INDEX_SIMPLE_5X_MAP);
			}
		}

		assertTrue(true);
	}

	@Test
    @Rollback(false)
	public void migrateTranslationItems() {
		Collection<AcTranslationSeries> translationSeries = acTranslationSeriesDAO.getAll();
		for (AcTranslationSeries oneTranslationSeries : translationSeries) {
			Collection<AcTranslationItem<?>> speciesTranslations = new ArrayList<AcTranslationItem<?>>();
			for (AcTranslation rangeTranslation : oneTranslationSeries.getTranslations()) {
                for (AcTranslationItem<?> translationItem : rangeTranslation.getTranslationItems()) {
                    speciesTranslations.add(translationItem);
                }
			}
			if (SPECIAL_SPECIES_SET_IDS.contains(oneTranslationSeries.getSpeciesSet().getId())) {
				replaceIndexPlaceholders(speciesTranslations, SPECIAL_SPECIES_INDEX_MIGRATION_MAP);				
			} else {
				replaceIndexPlaceholders(speciesTranslations, SPECIES_INDEX_SIMPLE_5X_MAP);
			}
		}

		assertTrue(true);
	}

	private static void replaceIndexPlaceholders(
		Collection<? extends FunctionHolder<?, ?>> functionHolders,
		Map<Integer, Integer> variableIndexMap
	) {
		FunctionUtility funUtil = new FunctionUtility();
		for (FunctionHolder<?, ?> functionHolder : functionHolders) {
			final String newFormula = funUtil.getFormulaWithReplacedIndexPlaceholders(functionHolder.getFunction(), variableIndexMap);
			if (newFormula != null) {
				((Expression<?,?>) functionHolder.getFunction()).setFormula(newFormula);
			}
		}
	}
}