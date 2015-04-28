package edu.banda.coel.server.dao.dyn;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.serverbase.persistence.GenericDAO;
import com.banda.math.domain.dynamics.SingleRunAnalysisSpec;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class SingleRunAnalysisSpecDaoTest extends CoelBasicDaoTest<SingleRunAnalysisSpec, Long> {

	private static final Long EXISTING_SINGLE_RUN_ANALYSIS_SPEC_ID = 1l;

	@Autowired
	GenericDAO<SingleRunAnalysisSpec, Long> singleRunAnalysisSpecDAO;

	@Override
	public void setUpTestData() {
		setUp(singleRunAnalysisSpecDAO, SingleRunAnalysisSpec.class, EXISTING_SINGLE_RUN_ANALYSIS_SPEC_ID);
	}
}