package edu.banda.coel.server.dao.ac;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.chemistry.domain.AcCompartment;
import com.banda.chemistry.domain.AcCompartmentChannelGroup;
import com.banda.chemistry.domain.AcReactionSet;
import com.banda.core.domain.um.User;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcCompartmentDaoTest extends CoelBasicDaoTest<AcCompartment, Long> {

	private static final Long EXISTING_AC_COMPARTMENT_ID = 1000l;

	@Autowired
	GenericDAO<AcCompartment, Long> acCompartmentDAO;

	@Override
	public void setUpTestData() {
		setUp(acCompartmentDAO, AcCompartment.class, EXISTING_AC_COMPARTMENT_ID);
	}

	@Override
	protected AcCompartment getTestObject() {
		AcCompartment newAcCompartment = super.getTestObject();

		User existingUser = new User();
		existingUser.setId(new Long(1));
		newAcCompartment.setCreatedBy(existingUser);
		
		AcReactionSet existingReactionSet = new AcReactionSet();
		existingReactionSet.setId(new Long(1));
		newAcCompartment.setReactionSet(existingReactionSet);
		
		AcCompartment existingSubCompartment = new AcCompartment();
		existingSubCompartment.setId(new Long(1));
		newAcCompartment.addSubCompartment(existingSubCompartment);

		AcCompartmentChannelGroup newSubCompartmentChannelGroup = new AcCompartmentChannelGroup();
		newAcCompartment.addSubChannelGroup(newSubCompartmentChannelGroup);

		return newAcCompartment;
	}
}