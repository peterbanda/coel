package edu.banda.coel.server.dao.ac;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.banda.chemistry.domain.AcCompartment;
import com.banda.chemistry.domain.ArtificialChemistry;
import com.banda.core.plotter.Plotter;
import com.banda.math.domain.dynamics.SingleRunAnalysisResultType;
import com.banda.math.domain.Stats;
import com.banda.math.domain.StatsType;
import com.banda.core.domain.um.User;
import com.banda.serverbase.persistence.GenericDAO;
import com.banda.math.business.dynamics.DynamicsAnalysisResultProcessor;
import com.banda.math.business.dynamics.JavaStatsPlotter;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class ArtificialChemistryDaoTest extends CoelBasicDaoTest<ArtificialChemistry, Long> {

	private static final Long EXISTING_AC_ID = 1017l;

	@Autowired
	GenericDAO<ArtificialChemistry, Long> artificialChemistryDAO;

	@Override
	public void setUpTestData() {
		setUp(artificialChemistryDAO, ArtificialChemistry.class, EXISTING_AC_ID);
	}

	@Override
	protected ArtificialChemistry getTestObject() {
		ArtificialChemistry testObject = super.getTestObject();

		User existingUser = new User();
		existingUser.setId(new Long(1));
		testObject.setCreatedBy(existingUser);

		AcCompartment existingAcMembraneStructure = new AcCompartment();
		existingAcMembraneStructure.setId(new Long(1));
		testObject.setSkinCompartment(existingAcMembraneStructure);

		return testObject;
	}

	@Test
	public void testLoadWithSelection() {
		ArtificialChemistry testObject = artificialChemistryDAO.get(EXISTING_AC_ID);
		
		Iterable<Stats> results = new DynamicsAnalysisResultProcessor(StatsType.Mean).
			calcStatsForTypeMulti(SingleRunAnalysisResultType.DerridaResults).apply(testObject.getMultiRunAnalysisResults());

		JavaStatsPlotter statsPlotter = new JavaStatsPlotter(Plotter.createDisplayInstance());
		statsPlotter.plotStats(results, "Test", "Perturbation Strength", "val", null);
	}
}