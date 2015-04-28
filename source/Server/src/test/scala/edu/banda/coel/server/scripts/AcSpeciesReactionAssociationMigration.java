package edu.banda.coel.server.scripts;

import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.banda.chemistry.business.ArtificialChemistryUtil;
import com.banda.chemistry.domain.AcReaction;
import com.banda.chemistry.domain.AcSpeciesSet;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.CoelTest;


@Transactional
@Ignore
public class AcSpeciesReactionAssociationMigration extends CoelTest {

	@Autowired
	@Qualifier(value="acReactionDAO")
	GenericDAO<AcReaction, Long> acReactionDAO;

	ArtificialChemistryUtil acUtil = ArtificialChemistryUtil.getInstance();
	
	@Test
	@Rollback(false)
	public void migrateAllReactions() {
		Collection<AcReaction> reactions = acReactionDAO.getAll();

		for (AcReaction reaction : reactions) {
			AcSpeciesSet speciesSet = reaction.getSpeciesSet();
//			reaction.addReactants(acUtil.getSpeciesAssociations(speciesSet, reaction.getReactantsVector()));
//			reaction.addProducts(acUtil.getSpeciesAssociations(speciesSet, reaction.getProductsVector()));
		}

		assertTrue(true);
	}
}