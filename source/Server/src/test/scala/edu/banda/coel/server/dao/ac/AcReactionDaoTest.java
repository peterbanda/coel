package edu.banda.coel.server.dao.ac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.banda.chemistry.domain.*;
import com.banda.function.domain.AbstractFunction;
import com.banda.function.domain.Expression;
import com.banda.serverbase.persistence.GenericDAO;
import com.banda.serverbase.test.AbstractDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcReactionDaoTest extends AbstractDaoTest<AcReaction, Long> {

	@Autowired
	@Qualifier(value="acReactionDAO")
	GenericDAO<AcReaction, Long> acReactionDAO;

	@Override
	protected GenericDAO<AcReaction, Long> getDao() {
		return acReactionDAO;
	}

	@Override
	protected Long getExistingTestKey() {
		return new Long(1);
	}

	@Override
	protected AcReaction getTestObject() {
		AcReaction newAcReaction = new AcReaction();
		newAcReaction.setLabel("JXxd");
		newAcReaction.setSortOrder(new Integer(1));
		newAcReaction.setPriority(1);

		AcSpecies existingSpecies1 = new AcSpecies();
		existingSpecies1.setId(new Long(1));

		AcSpecies existingSpecies2 = new AcSpecies();
		existingSpecies2.setId(new Long(1));

		AcSpeciesReactionAssociation reactant = new AcSpeciesReactionAssociation(existingSpecies1, new Double(2));
		newAcReaction.addSpeciesAssociation(reactant, AcSpeciesAssociationType.Reactant);

		AcSpeciesReactionAssociation product = new AcSpeciesReactionAssociation(existingSpecies2, new Double(1));
		newAcReaction.addSpeciesAssociation(product, AcSpeciesAssociationType.Product);

		AcReactionSet existingAcReactionSet = new AcReactionSet();
		existingAcReactionSet.setId(new Long(1));
		existingAcReactionSet.addReaction(newAcReaction);

		AbstractFunction<Double, Double> newFunction = Expression.Double("x1 + 2");
		newAcReaction.setForwardRateFunction(newFunction);

		return newAcReaction;
	}
}