package edu.banda.coel.server.dao.evo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.banda.serverbase.persistence.GenericDAO;
import com.banda.math.domain.evo.ArrayChromosome;
import com.banda.math.domain.evo.Chromosome;
import com.banda.math.domain.evo.Population;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
@SuppressWarnings("rawtypes")
public class ChromosomeDaoTest extends CoelBasicDaoTest<Chromosome, Long> {

	private static final Long EXISTING_CHROMOSOME_ID = 1000l;

	@Autowired
	GenericDAO<Chromosome, Long> chromosomeDAO;

	@Override
	public void setUpTestData() {
		setUp(chromosomeDAO, ArrayChromosome.class, EXISTING_CHROMOSOME_ID);
	}

	@Override
	protected Chromosome getTestObject() {
		ArrayChromosome chromosome = (ArrayChromosome) super.getTestObject();

		chromosome.setCode(new Boolean[] {true, false});

		Population existingPopulation = new Population();
		existingPopulation.setId(1000l);
		existingPopulation.addChromosome(chromosome);

		return chromosome;
	}

	@Test
	@Rollback(false)
	public void testSave() {
		super.testSave();
	}
}