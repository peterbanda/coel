package edu.banda.coel.server.dao.ac;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.chemistry.domain.AcReactionSpeciesForbiddenRedundancy;
import com.banda.chemistry.domain.AcSymmetricSpec;
import com.banda.chemistry.domain.ArtificialChemistrySpec;
import com.banda.serverbase.persistence.GenericDAO;
import com.banda.math.domain.rand.UniformDistribution;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class ArtificialChemistrySpecDaoTest extends CoelBasicDaoTest<ArtificialChemistrySpec, Long> {

	private static final Long EXISTING_AC_SPEC_ID = 1000l;

	@Autowired
	GenericDAO<ArtificialChemistrySpec, Long> artificialChemistrySpecDAO;

	@Override
	public void setUpTestData() {
		setUp(artificialChemistrySpecDAO, AcSymmetricSpec.class, EXISTING_AC_SPEC_ID);
	}

	@Override
	protected ArtificialChemistrySpec getTestObject() {
		ArtificialChemistrySpec testObject = super.getTestObject();

		testObject.setSpeciesForbiddenRedundancy(AcReactionSpeciesForbiddenRedundancy.None);
		
		UniformDistribution<Double> rateConstantDistribution = new UniformDistribution<Double>();
		rateConstantDistribution.setFrom(0d);
		rateConstantDistribution.setTo(0.8);
		testObject.setRateConstantDistribution(rateConstantDistribution);

		UniformDistribution<Double> outfluxNonReactiveRateConstantDistribution = new UniformDistribution<Double>();
		outfluxNonReactiveRateConstantDistribution.setFrom(0d);
		outfluxNonReactiveRateConstantDistribution.setTo(0.8);
		testObject.setOutfluxNonReactiveRateConstantDistribution(outfluxNonReactiveRateConstantDistribution);

		return testObject;
	}
}