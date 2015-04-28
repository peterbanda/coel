package edu.banda.coel.server.dao.evo;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.math.domain.evo.EvoRun;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.domain.evo.EvoAcRateConstantTask;
import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
@SuppressWarnings("rawtypes")
public class EvoRunDaoTest extends CoelBasicDaoTest<EvoRun, Long> {

	private static final Long EXISTING_EVO_RUN_ID = 1000l;

	@Autowired
	GenericDAO<EvoRun, Long> evoRunDAO;

	@Override
	public void setUpTestData() {
		setUp(evoRunDAO, EvoRun.class, EXISTING_EVO_RUN_ID);
	}

	@Override
	protected EvoRun getTestObject() {
		EvoRun evoRun = super.getTestObject();

		EvoAcRateConstantTask existingEvoTask = new EvoAcRateConstantTask();
		existingEvoTask.setId(1000l);
		existingEvoTask.addEvolutionRun(evoRun);

		return evoRun;
	}
}