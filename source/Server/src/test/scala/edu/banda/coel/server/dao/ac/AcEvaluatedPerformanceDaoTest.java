package edu.banda.coel.server.dao.ac;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.chemistry.domain.AcCompartment;
import com.banda.chemistry.domain.AcInteractionSeries;
import com.banda.chemistry.domain.AcEvaluatedPerformance;
import com.banda.chemistry.domain.AcEvaluation;
import com.banda.chemistry.domain.AcSimulationConfig;
import com.banda.core.domain.um.User;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcEvaluatedPerformanceDaoTest extends CoelBasicDaoTest<AcEvaluatedPerformance, Long> {

	private static final Long EXISTING_AC_EVALUATED_PERFORMANCE_ID = -1l;

	@Autowired
	GenericDAO<AcEvaluatedPerformance, Long> acEvaluatedPerformanceDAO;

	@Override
	public void setUpTestData() {
		setUp(acEvaluatedPerformanceDAO, AcEvaluatedPerformance.class, EXISTING_AC_EVALUATED_PERFORMANCE_ID);
	}

	@Override
	protected AcEvaluatedPerformance getTestObject() {
		AcEvaluatedPerformance newAcPerformance = super.getTestObject();
		newAcPerformance.setAveragedCorrectRates(new Double[]{0.991, 0.992, 0.9904, 0.9993});

		User existingUser = new User();
		existingUser.setId(new Long(1));
		newAcPerformance.setCreatedBy(existingUser);

		AcInteractionSeries existingActionSeries = new AcInteractionSeries();
		existingActionSeries.setId(new Long(1));
		newAcPerformance.setActionSeries(existingActionSeries);

		AcCompartment existingCompartment = new AcCompartment();
		existingCompartment.setId(new Long(1));
		newAcPerformance.setCompartment(existingCompartment);

		AcSimulationConfig existingSimConfig = new AcSimulationConfig();
		existingSimConfig.setId(new Long(1));
		newAcPerformance.setSimulationConfig(existingSimConfig);

		AcEvaluation existingAcEvaluation = new AcEvaluation();
		existingAcEvaluation.setId(new Long(1));
		newAcPerformance.setEvaluation(existingAcEvaluation);

		return newAcPerformance;
	}
}