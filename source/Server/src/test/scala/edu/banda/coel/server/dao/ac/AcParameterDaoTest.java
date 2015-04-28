package edu.banda.coel.server.dao.ac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.banda.chemistry.domain.AcParameter;
import com.banda.chemistry.domain.AcParameterSet;
import com.banda.function.domain.AbstractFunction;
import com.banda.function.domain.Expression;
import com.banda.serverbase.persistence.GenericDAO;
import com.banda.serverbase.test.AbstractDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcParameterDaoTest extends AbstractDaoTest<AcParameter, Long> {

	@Autowired
	@Qualifier(value="acParameterDAO")
	GenericDAO<AcParameter, Long> acParameterDAO;

	@Override
	protected GenericDAO<AcParameter, Long> getDao() {
		return acParameterDAO;
	}

	@Override
	protected Long getExistingTestKey() {
		return new Long(1);
	}

	@Override
	protected AcParameter getTestObject() {
		AcParameter newAcParameter = new AcParameter();
		newAcParameter.setId(new Long(-1));
		newAcParameter.setVariableIndex(new Integer(8));
		newAcParameter.setSortOrder(new Integer(8));
		newAcParameter.setLabel("XXX");

		AcParameterSet existingAcParameterSet = new AcParameterSet();
		existingAcParameterSet.setId(new Long(1));
		existingAcParameterSet.addVariable(newAcParameter);

		AbstractFunction<Double, Double> newFunction = Expression.Double("x1 + 2");
		newAcParameter.setEvolFunction(newFunction);

		return newAcParameter;
	}
}