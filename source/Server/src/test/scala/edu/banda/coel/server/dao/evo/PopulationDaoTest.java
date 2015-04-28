package edu.banda.coel.server.dao.evo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.banda.serverbase.persistence.GenericDAO;
import com.banda.math.domain.evo.EvoRun;
import com.banda.math.domain.evo.Population;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
@SuppressWarnings("rawtypes")
public class PopulationDaoTest extends CoelBasicDaoTest<Population, Long> {

	private static final Long EXISTING_POPULATION_ID = 1l;

	@Autowired
	@Qualifier(value="populationDAO")
	GenericDAO<Population, Long> populationDAO;

	@Override
	public void setUpTestData() {
		setUp(populationDAO, Population.class, EXISTING_POPULATION_ID);
	}

	@Override
	protected Population getTestObject() {
		Population population = super.getTestObject();

		EvoRun existingEvoRun = new EvoRun();
		existingEvoRun.setId(1l);
		existingEvoRun.addPopulation(population);

		return population;
	}
}