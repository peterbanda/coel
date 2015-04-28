package edu.banda.coel.server.dao;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.math.domain.rand.DiscreteDistribution;
import com.banda.math.domain.rand.RandomDistribution;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2012
 */
public class RandomDistributionDaoTest extends CoelBasicDaoTest<RandomDistribution, Long> {

	private static final Long EXISTING_RANDOM_DISTRIBUTION_ID = 1051l;

	@Autowired
	GenericDAO<RandomDistribution, Long> randomDistributionDAO;

	@Override
	public void setUpTestData() {
		setUp(randomDistributionDAO, DiscreteDistribution.class, EXISTING_RANDOM_DISTRIBUTION_ID);
	}

	@Override
	protected RandomDistribution getTestObject() {
		DiscreteDistribution newRandomDistribution = (DiscreteDistribution) super.getTestObject();
		newRandomDistribution.setVersion(1l);

		newRandomDistribution.setProbabilities(new double[] {0.2, 0.289, 0.123});
		newRandomDistribution.setValues(new Integer[] {12, 98, 33});

		return newRandomDistribution;
	}
}