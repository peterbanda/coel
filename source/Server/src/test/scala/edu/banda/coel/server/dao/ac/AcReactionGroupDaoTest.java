package edu.banda.coel.server.dao.ac;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.banda.chemistry.domain.AcReactionGroup;
import com.banda.chemistry.domain.AcReactionSet;
import com.banda.core.domain.um.User;
import com.banda.serverbase.persistence.GenericDAO;
import com.banda.serverbase.test.AbstractDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcReactionGroupDaoTest extends AbstractDaoTest<AcReactionGroup, Long> {

	@Autowired
	@Qualifier(value="acReactionGroupDAO")
	GenericDAO<AcReactionGroup, Long> acReactionGroupDAO;

	@Override
	protected GenericDAO<AcReactionGroup, Long> getDao() {
		return acReactionGroupDAO;
	}

	@Override
	protected Long getExistingTestKey() {
		return new Long(1);
	}

	@Override
	protected AcReactionGroup getTestObject() {
		AcReactionGroup newAcReactionGroup = new AcReactionGroup();
		newAcReactionGroup.setId(new Long(-1));
		newAcReactionGroup.setLabel("LALA");
		newAcReactionGroup.setCreateTime(new Date());

		User existingUser = new User();
		existingUser.setId(new Long(1));
		newAcReactionGroup.setCreatedBy(existingUser);

		AcReactionSet existingAcReactionSet = new AcReactionSet();
		existingAcReactionSet.setId(new Long(1));
		existingAcReactionSet.addGroup(newAcReactionGroup);

		return newAcReactionGroup;
	}
}