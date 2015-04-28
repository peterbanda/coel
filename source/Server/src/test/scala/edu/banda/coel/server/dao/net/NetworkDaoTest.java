package edu.banda.coel.server.dao.net;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.network.domain.Network;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2014
 */
public class NetworkDaoTest extends CoelBasicDaoTest<Network, Long> {

	private static final Long EXISTING_NETWORK_ID = 1001l;

	@Autowired
	GenericDAO<Network, Long> networkDAO;

	@Override
	public void setUpTestData() {		
		setUp(networkDAO, Network.class, EXISTING_NETWORK_ID);
	}

	@Override
	protected Network getTestObject() {
		Network newNetwork = super.getTestObject();

		return newNetwork;
	}
}