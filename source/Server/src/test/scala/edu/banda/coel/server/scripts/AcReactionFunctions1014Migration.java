package edu.banda.coel.server.scripts;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.banda.chemistry.business.AcReplicator;
import com.banda.chemistry.domain.AcReaction;
import com.banda.chemistry.domain.AcReactionSet;
import com.banda.core.util.ObjectUtil;
import com.banda.function.domain.AbstractFunction;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.CoelTest;


@Transactional
@Ignore
public class AcReactionFunctions1014Migration extends CoelTest {

	private static int AC_REACTION_SET_ID = 1014;

	@Autowired
	@Qualifier(value="acReactionSetDAO")
	GenericDAO<AcReactionSet, Long> acReactionSetDAO;

	AcReplicator replicator = AcReplicator.getInstance();

	@Test
	@Rollback(false)
	public void migrateAllTranslatedRuns() {
		AcReactionSet reactionSet = acReactionSetDAO.get(new Long(AC_REACTION_SET_ID));
		for (AcReaction reaction : reactionSet.getReactions()) {
			reaction.setForwardRateFunction(replicator.cloneFunction(reaction.getForwardRateFunction()));
			ObjectUtil.nullIdAndVersion((AbstractFunction<Double, Double>) reaction.getForwardRateFunction());
		}
		assertTrue(true);
	}
}