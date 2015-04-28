package edu.banda.coel.server.dao.ac;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.chemistry.domain.AcInteraction;
import com.banda.chemistry.domain.AcSpecies;
import com.banda.chemistry.domain.AcSpeciesInteraction;
import com.banda.function.domain.AbstractFunction;
import com.banda.function.domain.Expression;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcSpeciesInteractionDaoTest extends CoelBasicDaoTest<AcSpeciesInteraction, Long> {

	private static final Long EXISTING_AC_SPECIES_INTERACTION_ID = 1l;

	@Autowired
	GenericDAO<AcSpeciesInteraction, Long> acSpeciesInteractionDAO;

	@Override
	public void setUpTestData() {
		setUp(acSpeciesInteractionDAO, AcSpeciesInteraction.class, EXISTING_AC_SPECIES_INTERACTION_ID);
	}

	@Override
	protected AcSpeciesInteraction getTestObject() {
		AcSpeciesInteraction newAcSpeciesInteraction = super.getTestObject();

		AcSpecies existingSpecies = new AcSpecies();
		existingSpecies.setId(new Long(1));
		newAcSpeciesInteraction.setSpecies(existingSpecies);

		AcInteraction existingAction = new AcInteraction();
		existingAction.setId(new Long(1));
		existingAction.addToSpeciesActions(newAcSpeciesInteraction);

		AbstractFunction<Double, Double> newFunction = Expression.Double("x1 + 2");
		newAcSpeciesInteraction.setSettingFunction(newFunction);

		return newAcSpeciesInteraction;
	}
}