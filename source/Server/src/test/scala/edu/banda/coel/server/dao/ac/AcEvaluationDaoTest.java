package edu.banda.coel.server.dao.ac;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.chemistry.domain.AcEvaluation;
import com.banda.chemistry.domain.AcTranslationSeries;
import com.banda.core.domain.um.User;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcEvaluationDaoTest extends CoelBasicDaoTest<AcEvaluation, Long> {

	private static final Long EXISTING_ID = 1000l;

	@Autowired
	GenericDAO<AcEvaluation, Long> acEvaluationDAO;

	@Override
	public void setUpTestData() {
		setUp(acEvaluationDAO, AcEvaluation.class, EXISTING_ID);
	}

	@Override
	protected AcEvaluation getTestObject() {
		AcEvaluation newAcEvaluation = super.getTestObject();

		User existingUser = new User();
		existingUser.setId(new Long(1));
		newAcEvaluation.setCreatedBy(existingUser);

		AcTranslationSeries existingTranslationSeries = new AcTranslationSeries();
		existingTranslationSeries.setId(new Long(1));
		newAcEvaluation.setTranslationSeries(existingTranslationSeries);

		return newAcEvaluation;
	}
}