package edu.banda.coel.server.dao.ac;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.chemistry.domain.AcReactionSet;
import com.banda.chemistry.domain.AcSpeciesSet;
import com.banda.core.domain.um.User;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcReactionSetDaoTest extends CoelBasicDaoTest<AcReactionSet, Long> {

	private static final Long EXISTING_AC_REACTION_SET_ID = 1l;

	@Autowired
	GenericDAO<AcReactionSet, Long> acReactionSetDAO;

	@Override
	public void setUpTestData() {
		setUp(acReactionSetDAO, AcReactionSet.class, EXISTING_AC_REACTION_SET_ID);
	}

	@Override
	protected AcReactionSet getTestObject() {
		AcReactionSet newAcReactionSet = super.getTestObject();

		User existingUser = new User();
		existingUser.setId(new Long(1));
		newAcReactionSet.setCreatedBy(existingUser);

		AcSpeciesSet existingSpeciesSet = new AcSpeciesSet();
		existingSpeciesSet.setId(new Long(1));
		newAcReactionSet.setSpeciesSet(existingSpeciesSet);

		return newAcReactionSet;
	}
}