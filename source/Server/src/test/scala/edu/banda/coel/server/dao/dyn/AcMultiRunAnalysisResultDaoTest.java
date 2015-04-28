package edu.banda.coel.server.dao.dyn;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.banda.chemistry.domain.AcMultiRunAnalysisResult;
import com.banda.chemistry.domain.ArtificialChemistry;
import com.banda.math.domain.Stats;
import com.banda.math.domain.dynamics.MultiRunAnalysisSpec;
import com.banda.math.domain.dynamics.SingleRunAnalysisResultType;

import edu.banda.coel.server.dao.AcMultiRunAnalysisResultDAO;
import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcMultiRunAnalysisResultDaoTest extends CoelBasicDaoTest<AcMultiRunAnalysisResult, Long> {

	private static final Long EXISTING_AC_MULTI_RUN_ANALYSIS_RESULT_ID = 1000l;

	@Autowired
	AcMultiRunAnalysisResultDAO acMultiRunAnalysisResultDAO;

	@Override
	public void setUpTestData() {
		setUp(acMultiRunAnalysisResultDAO, AcMultiRunAnalysisResult.class, EXISTING_AC_MULTI_RUN_ANALYSIS_RESULT_ID);
	}

	@Override
	protected AcMultiRunAnalysisResult getTestObject() {
		AcMultiRunAnalysisResult testObject = super.getTestObject();

		MultiRunAnalysisSpec existingSpec = new MultiRunAnalysisSpec();
		existingSpec.setId(1l);
		testObject.setSpec(existingSpec);

		ArtificialChemistry existingAc = new ArtificialChemistry();
		existingAc.setId(1l);
		testObject.setAc(existingAc);

		return testObject;
	}

	@Test
	public void testGetStatsForPropertyOnly() {
		Collection<Stats> results = acMultiRunAnalysisResultDAO.getStatsForPropertyOnly(
				SingleRunAnalysisResultType.DerridaResults, Arrays.asList(new Long[] {1000l, 1001l}));
		assertNotNull(results);
	}

	@Test
	public void testGetForPropertyOnly() {
		Collection<List<Double>> results = (Collection<List<Double>>) acMultiRunAnalysisResultDAO.getForPropertyOnly(
				SingleRunAnalysisResultType.FinalLyapunovExponents, Arrays.asList(new Long[] {1000l, 1001l}));
		assertNotNull(results);
	}
}