package edu.banda.coel.server.dao.evo;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.core.domain.um.User;
import com.banda.math.domain.evo.EvoGaSetting;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class EvoGaSettingDaoTest extends CoelBasicDaoTest<EvoGaSetting, Long> {

	private static final Long EXISTING_GA_SETTING_ID = 1l;

	@Autowired
	GenericDAO<EvoGaSetting, Long> evoGaSettingDAO;

	@Override
	public void setUpTestData() {
		setUp(evoGaSettingDAO, EvoGaSetting.class, EXISTING_GA_SETTING_ID);
	}

	@Override
	protected GenericDAO<EvoGaSetting, Long> getDao() {
		return evoGaSettingDAO;
	}

	@Override
	protected EvoGaSetting getTestObject() {
		EvoGaSetting newGaSetting = super.getTestObject();

		User existingUser = new User();
		existingUser.setId(1l);
		newGaSetting.setCreatedBy(existingUser);

		return newGaSetting;
	}
}