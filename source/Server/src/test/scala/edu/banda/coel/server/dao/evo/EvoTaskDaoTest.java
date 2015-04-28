package edu.banda.coel.server.dao.evo;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.chemistry.domain.AcEvaluation;
import com.banda.chemistry.domain.ArtificialChemistry;
import com.banda.core.domain.um.User;
import com.banda.serverbase.persistence.GenericDAO;
import com.banda.math.domain.evo.EvoGaSetting;
import com.banda.math.domain.evo.EvoTask;

import edu.banda.coel.domain.evo.EvoAcRateConstantTask;
import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class EvoTaskDaoTest extends CoelBasicDaoTest<EvoTask, Long> {

	private static final Long EXISTING_EVO_TASK_ID = 1l;

	@Autowired
	GenericDAO<EvoTask, Long> evoTaskDAO;

	@Override
	public void setUpTestData() {
		setUp(evoTaskDAO, EvoAcRateConstantTask.class, EXISTING_EVO_TASK_ID);
	}

	@Override
	protected EvoTask getTestObject() {
		EvoAcRateConstantTask newEvoAcTask = (EvoAcRateConstantTask) super.getTestObject();

		User existingUser = new User();
		existingUser.setId(new Long(1));
		newEvoAcTask.setCreatedBy(existingUser);

		EvoGaSetting existingGaSetting = new EvoGaSetting();
		existingGaSetting.setId(new Long(1));
		newEvoAcTask.setGaSetting(existingGaSetting);

		ArtificialChemistry existingAc = new ArtificialChemistry();
		existingAc.setId(new Long(1));
		newEvoAcTask.setAc(existingAc);

		AcEvaluation existingAcEvaluation = new AcEvaluation();
		existingAcEvaluation.setId(new Long(1));
		newEvoAcTask.setAcEvaluation(existingAcEvaluation);

		return newEvoAcTask;
	}
}