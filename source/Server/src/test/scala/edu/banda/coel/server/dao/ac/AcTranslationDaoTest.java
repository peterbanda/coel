package edu.banda.coel.server.dao.ac;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.chemistry.domain.AcTranslation;
import com.banda.chemistry.domain.AcTranslationSeries;
import com.banda.serverbase.persistence.GenericDAO;
import com.banda.serverbase.test.AbstractDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcTranslationDaoTest extends AbstractDaoTest<AcTranslation, Long> {

	@Autowired
	GenericDAO<AcTranslation, Long> acTranslationDAO;

	@Override
	protected GenericDAO<AcTranslation, Long> getDao() {
		return acTranslationDAO;
	}

	@Override
	protected Long getExistingTestKey() {
		return new Long(1);
	}

	@Override
	protected AcTranslation getTestObject() {
		AcTranslation newAcTranslation = new AcTranslation();
		newAcTranslation.setId(new Long(-1));
		newAcTranslation.setFromTime(2);
		newAcTranslation.setToTime(5000);

		AcTranslationSeries existingTranslationSeries = new AcTranslationSeries();
		existingTranslationSeries.setId(new Long(1));
		existingTranslationSeries.addTranslation(newAcTranslation);

		return newAcTranslation;
	}
}