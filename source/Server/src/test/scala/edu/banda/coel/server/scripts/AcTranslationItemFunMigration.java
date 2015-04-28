package edu.banda.coel.server.scripts;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.banda.chemistry.domain.AcTranslationItem;
import com.banda.function.business.FunctionUtility;
import com.banda.function.domain.AbstractFunction;
import com.banda.function.domain.Expression;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.CoelTest;

@Transactional
public class AcTranslationItemFunMigration extends CoelTest {

	@Autowired
	GenericDAO<AcTranslationItem, Long> acTranslationItemDAO;

	@Autowired
	GenericDAO<AbstractFunction, Long> functionDAO;

	private final FunctionUtility functionUtility = new FunctionUtility(); 

	@Test
	@Rollback(false)
	public void migrate() {
		Collection<AcTranslationItem> translationItems = acTranslationItemDAO.getAll();		
		for (AcTranslationItem translationItem : translationItems) {
			final Expression<?, ?> function = (Expression<?, ?>) translationItem.getTranslationFunction();
			Map<Integer, Integer> migrationMap = new HashMap<Integer, Integer>();
			for (Integer oldIndex : function.getReferencedVariableIndeces()) {
				migrationMap.put(oldIndex, 2 * oldIndex);
			}
			final String newFormula = functionUtility.getFormulaWithReplacedIndexPlaceholders(function, migrationMap);
			function.setFormula(newFormula);
			functionDAO.save(function);
		}
	}
}