package edu.banda.coel.server.dao.net;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.banda.function.business.FunctionFactory;
import com.banda.function.domain.AbstractFunction;
import com.banda.function.domain.TransitionTable;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2014
 */
@SuppressWarnings("rawtypes")
public class FunctionDaoTest extends CoelBasicDaoTest<AbstractFunction, Long> {

	private static final Long EXISTING_FUNCTION_ID = 1l;
	private final FunctionFactory functionFactory = new FunctionFactory();

	@Autowired
	GenericDAO<AbstractFunction, Long> functionDAO;

	@Override
	public void setUpTestData() {
		setUp(functionDAO, AbstractFunction.class, EXISTING_FUNCTION_ID);
	}

	@Override
	protected AbstractFunction getTestObject() {
		TransitionTable<Boolean, Boolean> newAbstractFunction = functionFactory.createRandomBoolTransitionTable(2);
		newAbstractFunction.setId(new Long(-1));

		return newAbstractFunction;
	}
}