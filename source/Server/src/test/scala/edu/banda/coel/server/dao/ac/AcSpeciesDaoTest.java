package edu.banda.coel.server.dao.ac;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.chemistry.domain.AcSpecies;
import com.banda.chemistry.domain.AcSpeciesSet;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcSpeciesDaoTest extends CoelBasicDaoTest<AcSpecies, Long> {

	private static final Long EXISTING_SPECIES_ID = 1l;

	@Autowired
	GenericDAO<AcSpecies, Long> acSpeciesDAO;

	@Override
	public void setUpTestData() {
		setUp(acSpeciesDAO, AcSpecies.class, EXISTING_SPECIES_ID);
	}

	@Override
	protected AcSpecies getTestObject() {
		AcSpecies newAcSpecies = super.getTestObject();

		AcSpeciesSet existingAcSpeciesSet = new AcSpeciesSet();
		existingAcSpeciesSet.setId(new Long(1));
		existingAcSpeciesSet.addVariable(newAcSpecies);

		return newAcSpecies;
	}
}