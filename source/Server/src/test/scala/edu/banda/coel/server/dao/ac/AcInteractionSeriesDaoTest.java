package edu.banda.coel.server.dao.ac;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.chemistry.domain.AcInteractionSeries;
import com.banda.chemistry.domain.AcSpeciesSet;
import com.banda.core.domain.um.User;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcInteractionSeriesDaoTest extends CoelBasicDaoTest<AcInteractionSeries, Long> {

	private static final Long EXISTING_AC_INTERACTION_SERIES_ID = 1l;

	@Autowired
	GenericDAO<AcInteractionSeries, Long> acInteractionSeriesDAO;

	@Override
	public void setUpTestData() {
		setUp(acInteractionSeriesDAO, AcInteractionSeries.class, EXISTING_AC_INTERACTION_SERIES_ID);
	}

	@Override
	protected AcInteractionSeries getTestObject() {
		AcInteractionSeries newAcInteractionSeries = super.getTestObject();

		User existingUser = new User();
		existingUser.setId(new Long(1));
		newAcInteractionSeries.setCreatedBy(existingUser);
		
		AcSpeciesSet existingSpeciesSet = new AcSpeciesSet();
		existingSpeciesSet.setId(new Long(1));
		newAcInteractionSeries.setSpeciesSet(existingSpeciesSet);

		return newAcInteractionSeries;
	}
}