package edu.banda.coel.server.dao.ac;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.banda.chemistry.domain.AcInteraction;
import com.banda.chemistry.domain.AcInteractionSeries;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcInteractionDaoTest extends CoelBasicDaoTest<AcInteraction, Long> {

	private static final Long EXISTING_AC_INTERACTION_ID = 1000l;

	@Autowired
	GenericDAO<AcInteraction, Long> acInteractionDAO;

	@Override
	public void setUpTestData() {
		setUp(acInteractionDAO, AcInteraction.class, EXISTING_AC_INTERACTION_ID);
	}

	@Override
	protected AcInteraction getTestObject() {
		AcInteraction newAcInteraction = super.getTestObject();

		AcInteractionSeries existingActionSeries = new AcInteractionSeries();
		existingActionSeries.setId(new Long(1));
		existingActionSeries.addAction(newAcInteraction);

		return newAcInteraction;
	}

	@Test
	public void testGetPreviousPK() {
		Long previousPK = acInteractionDAO.getPreviousPK(1007l);
		assertNotNull(previousPK);
		System.out.println("Previous " + previousPK);
	}

	@Test
	public void testGetNextPK() {
		Long nextPK = acInteractionDAO.getNextPK(1007l);
		assertNotNull(nextPK);
		System.out.println("Next " + nextPK);
	}
}