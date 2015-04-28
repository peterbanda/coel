package edu.banda.coel.server.dao.dyn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.banda.core.util.RandomUtil;
import com.banda.math.domain.Stats;
import com.banda.math.domain.StatsSequence;
import com.banda.math.domain.dynamics.SingleRunAnalysisResult;
import com.banda.math.domain.dynamics.SingleRunAnalysisResultType;

import edu.banda.coel.server.dao.CoelBasicDaoTest;
import edu.banda.coel.server.dao.SingleRunAnalysisResultDAO;

/**
 * @author Peter Banda
 * @since 2011
 */
public class SingleRunAnalysisResultDaoTest extends CoelBasicDaoTest<SingleRunAnalysisResult, Long> {

	private static final Long EXISTING_SINGLE_RUN_ANALYSIS_RESULT_ID = 1000l;

	@Autowired
	SingleRunAnalysisResultDAO singleRunAnalysisResultDAO;

	@Override
	public void setUpTestData() {
		setUp(singleRunAnalysisResultDAO, SingleRunAnalysisResult.class, EXISTING_SINGLE_RUN_ANALYSIS_RESULT_ID);
	}

	@Override
	protected SingleRunAnalysisResult getTestObject() {
		SingleRunAnalysisResult testObject = super.getTestObject();

		testObject.setInitialState(createRandomDoubles());
		testObject.setNeighborTimeCorrelations(createRandomDoubles());
		testObject.setMeanFixedPointsDetected(createRandomDoubles());
		testObject.setFinalLyapunovExponents(createRandomDoubles());

		testObject.setFinalFixedPointsDetected(createRandomBooleans());
		testObject.setUnboundValuesDetected(createRandomBooleans());
		
		testObject.setSpatialCorrelations(createRandomStatsSequence());
		testObject.setTimeCorrelations(createRandomStatsSequence());
		testObject.setSpatialStationaryPointsPerTime(createRandomStatsSequence());
		testObject.setTimeStationaryPointsPerTime(createRandomStatsSequence());
		testObject.setSpatialCumulativeDiffPerTime(createRandomStatsSequence());
		testObject.setTimeCumulativeDiffPerTime(createRandomStatsSequence());
		testObject.setSpatialNonlinearityErrors(createRandomStatsSequence());
		testObject.setTimeNonlinearityErrors(createRandomStatsSequence());
		testObject.setDerridaResults(createRandomStatsSequence());

		return testObject;
	}

	private StatsSequence createRandomStatsSequence() {
		StatsSequence statsSequence = new StatsSequence();
		statsSequence.setTimeCreated(new Date());
		for (int i = 0; i < 10; i++) {
			Stats stats = createRandomObject(Stats.class);
			stats.setId(null);
			statsSequence.addStats(stats);
		}
		return statsSequence;
	}

	private List<Double> createRandomDoubles() {
		List<Double> doubles = new ArrayList<Double>();
		for (int i = 0; i < 10; i++) {
			doubles.add(RandomUtil.nextDouble());
		}
		return doubles;
	}

	private List<Boolean> createRandomBooleans() {
		List<Boolean> Booleans = new ArrayList<Boolean>();
		for (int i = 0; i < 10; i++) {
			Booleans.add(RandomUtil.nextBoolean());
		}
		return Booleans;
	}

	@Test
	public void testGetListWithPropertyAndInitialState() {
		final Collection<SingleRunAnalysisResult> objects = singleRunAnalysisResultDAO.getListWithPropertyAndInitialState(
				SingleRunAnalysisResultType.DerridaResults, 1000l);

		for (SingleRunAnalysisResult object : objects) {
			checkPropertyOnlyInstance(object);
		}
	}

	private void checkPropertyOnlyInstance(SingleRunAnalysisResult object) {
		assertNotNull(object);
		assertNotNull(object.getDerridaResults());
		assertNotNull(object.getDerridaResults().getId());
	}
}