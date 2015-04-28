package edu.banda.coel.server.dao.ac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.banda.chemistry.domain.AcTranslationSeries;
import com.banda.chemistry.domain.AcTranslationVariable;
import com.banda.serverbase.persistence.GenericDAO;
import com.banda.serverbase.test.AbstractDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcTranslationVariableDaoTest extends AbstractDaoTest<AcTranslationVariable, Long> {

	@Autowired
	@Qualifier(value="acTranslationVariableDAO")
	GenericDAO<AcTranslationVariable, Long> acTranslationVariableDAO;

	@Override
	protected GenericDAO<AcTranslationVariable, Long> getDao() {
		return acTranslationVariableDAO;
	}

	@Override
	protected Long getExistingTestKey() {
		return new Long(1);
	}

	@Override
	protected AcTranslationVariable getTestObject() {
		AcTranslationVariable newAcTranslationVariable = new AcTranslationVariable();
		newAcTranslationVariable.setId(new Long(-1));
		newAcTranslationVariable.setLabel("XXX");
		newAcTranslationVariable.setVariableIndex(new Integer(8));
		newAcTranslationVariable.setSortOrder(new Integer(8));

		AcTranslationSeries existingAcTranslationSeries = new AcTranslationSeries();
		existingAcTranslationSeries.setId(new Long(1));
		existingAcTranslationSeries.addVariable(newAcTranslationVariable);

		return newAcTranslationVariable;
	}
}