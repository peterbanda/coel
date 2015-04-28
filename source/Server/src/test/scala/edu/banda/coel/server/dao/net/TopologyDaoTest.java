package edu.banda.coel.server.dao.net;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.network.domain.TemplateTopology;
import com.banda.network.domain.Topology;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2014
 */
public class TopologyDaoTest extends CoelBasicDaoTest<Topology, Long> {

	private static final Long EXISTING_TOPOLOGY_ID = 1l;

	@Autowired
	GenericDAO<Topology, Long> topologyDAO;

	@Override
	public void setUpTestData() {
		
		setUp(topologyDAO, TemplateTopology.class, EXISTING_TOPOLOGY_ID);
	}

	@Override
	protected Topology getTestObject() {
		Topology newTopology = super.getTestObject();

		return newTopology;
	}
}