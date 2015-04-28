package edu.banda.coel.server.dao.ac;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.chemistry.domain.AcTranslation;
import com.banda.chemistry.domain.AcTranslationItem;
import com.banda.chemistry.domain.AcTranslationVariable;
import com.banda.function.domain.AbstractFunction;
import com.banda.function.domain.Expression;
import com.banda.serverbase.persistence.GenericDAO;
import com.banda.serverbase.test.AbstractDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcTranslationItemDaoTest extends AbstractDaoTest<AcTranslationItem, Long> {

	@Autowired
	GenericDAO<AcTranslationItem, Long> acTranslationItemDAO;

	@Override
	protected GenericDAO<AcTranslationItem, Long> getDao() {
		return acTranslationItemDAO;
	}

	@Override
	protected Long getExistingTestKey() {
		return new Long(1);
	}

	@Override
	protected AcTranslationItem getTestObject() {
		AcTranslationItem newAcTranslationItem = new AcTranslationItem();
		newAcTranslationItem.setId(new Long(-1));

		AcTranslationVariable existingTranslationVariable = new AcTranslationVariable();
		existingTranslationVariable.setId(new Long(4));
		newAcTranslationItem.setVariable(existingTranslationVariable);

		AcTranslation existingTranslation = new AcTranslation();
		existingTranslation.setId(new Long(1));
		existingTranslation.addTranslationItem(newAcTranslationItem);

		AbstractFunction<Double[], Double> newFunction = new Expression("x1 + 2");
		newAcTranslationItem.setTranslationFunction(newFunction);

		return newAcTranslationItem;
	}
}