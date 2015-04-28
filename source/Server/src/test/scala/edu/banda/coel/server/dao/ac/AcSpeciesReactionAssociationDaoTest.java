package edu.banda.coel.server.dao.ac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.banda.chemistry.domain.AcReaction;
import com.banda.chemistry.domain.AcSpecies;
import com.banda.chemistry.domain.AcSpeciesAssociationType;
import com.banda.chemistry.domain.AcSpeciesReactionAssociation;
import com.banda.serverbase.persistence.GenericDAO;
import com.banda.serverbase.test.AbstractDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcSpeciesReactionAssociationDaoTest extends AbstractDaoTest<AcSpeciesReactionAssociation, Long> {

	@Autowired
	@Qualifier(value="acSpeciesReactionAssociationDAO")
	GenericDAO<AcSpeciesReactionAssociation, Long> acSpeciesReactionAssociationDAO;

	@Override
	protected GenericDAO<AcSpeciesReactionAssociation, Long> getDao() {
		return acSpeciesReactionAssociationDAO;
	}

	@Override
	protected Long getExistingTestKey() {
		return new Long(1);
	}

	@Override
	protected AcSpeciesReactionAssociation getTestObject() {
		AcSpeciesReactionAssociation newAcSpeciesReactionAssociation = new AcSpeciesReactionAssociation();
		newAcSpeciesReactionAssociation.setId(new Long(-1));
		newAcSpeciesReactionAssociation.setStoichiometricFactor(new Double(2.2));
		newAcSpeciesReactionAssociation.setType(AcSpeciesAssociationType.Reactant);

		AcSpecies existingSpecies = new AcSpecies();
		existingSpecies.setId(new Long(101));
		newAcSpeciesReactionAssociation.setSpecies(existingSpecies);

		AcReaction existingReaction = new AcReaction();
		existingReaction.setId(new Long(1));
		existingReaction.addSpeciesAssociation(newAcSpeciesReactionAssociation);

		return newAcSpeciesReactionAssociation;
	}
}