package edu.banda.coel.server.dao.dyn;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.serverbase.persistence.GenericDAO;
import com.banda.math.domain.dynamics.MultiRunAnalysisSpec;
import com.banda.math.domain.dynamics.SingleRunAnalysisSpec;
import com.banda.math.domain.rand.UniformDistribution;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class MultiRunAnalysisSpecDaoTest extends CoelBasicDaoTest<MultiRunAnalysisSpec<?>, Long> {

	private static final Long EXISTING_MULTI_RUN_ANALYSIS_SPEC_ID = 1l;

	@Autowired
	GenericDAO<MultiRunAnalysisSpec<?>, Long> multiRunAnalysisSpecDAO;

	@Override
	public void setUpTestData() {
		// TODO: resolve the generic casting problem
		//		setUp(multiRunAnalysisSpecDAO, (Class<? extends MultiRunAnalysisSpec<?>>) MultiRunAnalysisSpec.class, EXISTING_MULTI_RUN_ANALYSIS_SPEC_ID);
	}

	@Override
	protected MultiRunAnalysisSpec getTestObject() {
		MultiRunAnalysisSpec testObject = super.getTestObject();

		SingleRunAnalysisSpec existingSingleRunAnalysisSpec = new SingleRunAnalysisSpec();
		existingSingleRunAnalysisSpec.setId(1l);
		testObject.setSingleRunSpec(existingSingleRunAnalysisSpec);

//		RandomDistribution<?> existingDistribution = new UniformDistribution();
//		existingDistribution.setId(1l);
//		testObject.setInitialStateDistribution(existingDistribution);

		UniformDistribution<Double> newDistribution = new UniformDistribution<Double>();
		newDistribution.setFrom(0d);
		newDistribution.setTo(0.8);
		testObject.setInitialStateDistribution(newDistribution);
		
		return testObject;
	}
}