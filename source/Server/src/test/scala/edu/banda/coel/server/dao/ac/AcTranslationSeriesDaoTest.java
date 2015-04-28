package edu.banda.coel.server.dao.ac;

import org.springframework.beans.factory.annotation.Autowired;
import com.banda.chemistry.domain.AcSpeciesSet;
import com.banda.chemistry.domain.AcTranslationSeries;
import com.banda.core.domain.um.User;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcTranslationSeriesDaoTest extends CoelBasicDaoTest<AcTranslationSeries, Long> {

	private static final Long EXISTING_AC_TRANSLATION_SERIES_ID = 1l;

	@Autowired
	GenericDAO<AcTranslationSeries, Long> acTranslationSeriesDAO;

	@Override
	public void setUpTestData() {
		setUp(acTranslationSeriesDAO, AcTranslationSeries.class, EXISTING_AC_TRANSLATION_SERIES_ID);
	}

	@Override
	protected AcTranslationSeries getTestObject() {
		AcTranslationSeries newAcTranslationSeries = super.getTestObject();

		User existingUser = new User();
		existingUser.setId(new Long(1));
		newAcTranslationSeries.setCreatedBy(existingUser);

		AcSpeciesSet existingSpeciesSet = new AcSpeciesSet();
		existingSpeciesSet.setId(new Long(1));
		newAcTranslationSeries.setSpeciesSet(existingSpeciesSet);

		return newAcTranslationSeries;
	}
}