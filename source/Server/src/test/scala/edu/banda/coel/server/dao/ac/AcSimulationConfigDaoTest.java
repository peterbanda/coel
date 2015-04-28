package edu.banda.coel.server.dao.ac;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.chemistry.domain.AcSimulationConfig;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcSimulationConfigDaoTest extends CoelBasicDaoTest<AcSimulationConfig, Long> {

	private static final Long EXISTING_AC_SIMULATION_CONFIG_ID = 1l;

	@Autowired
	GenericDAO<AcSimulationConfig, Long> acSimulationConfigDAO;

	@Override
	public void setUpTestData() {
		setUp(acSimulationConfigDAO, AcSimulationConfig.class, EXISTING_AC_SIMULATION_CONFIG_ID);
	}
}