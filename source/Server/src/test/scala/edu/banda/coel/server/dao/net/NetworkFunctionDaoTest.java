package edu.banda.coel.server.dao.net;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.network.domain.NetworkFunction;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2014
 */
@SuppressWarnings("rawtypes")
public class NetworkFunctionDaoTest extends CoelBasicDaoTest<NetworkFunction, Long> {

	private static final Long EXISTING_NETWORK_FUNCTION_ID = 1l;

	@Autowired
	GenericDAO<NetworkFunction, Long> networkFunctionDAO;

	@Override
	public void setUpTestData() {
		setUp(networkFunctionDAO, NetworkFunction.class, EXISTING_NETWORK_FUNCTION_ID);
	}

	@Override
	protected NetworkFunction getTestObject() {
		NetworkFunction networkFunction = super.getTestObject();

		return networkFunction;
	}
}