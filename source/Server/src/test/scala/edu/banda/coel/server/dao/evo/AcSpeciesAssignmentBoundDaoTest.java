package edu.banda.coel.server.dao.evo;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.chemistry.domain.AcSpeciesInteraction;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.domain.evo.AcSpeciesAssignmentBound;
import edu.banda.coel.domain.evo.EvoAcInteractionSeriesTask;
import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcSpeciesAssignmentBoundDaoTest extends CoelBasicDaoTest<AcSpeciesAssignmentBound, Long> {

	private static final Long EXISTING_ID = 1l;

	@Autowired
	GenericDAO<AcSpeciesAssignmentBound, Long> acSpeciesAssignmentBoundDAO;

	@Override
	public void setUpTestData() {
		setUp(acSpeciesAssignmentBoundDAO, AcSpeciesAssignmentBound.class, EXISTING_ID);
	}
	
	@Override
	protected GenericDAO<AcSpeciesAssignmentBound, Long> getDao() {
		return acSpeciesAssignmentBoundDAO;
	}

	@Override
	protected AcSpeciesAssignmentBound getTestObject() {
		AcSpeciesAssignmentBound newAcSpeciesAssignmentBound = new AcSpeciesAssignmentBound();
		newAcSpeciesAssignmentBound.setId(new Long(1));
		AcSpeciesInteraction speciesInteraction = new AcSpeciesInteraction();
		speciesInteraction.setId(1l);

		newAcSpeciesAssignmentBound.addAssignment(speciesInteraction);
		newAcSpeciesAssignmentBound.setFrom(0.1d);
		newAcSpeciesAssignmentBound.setTo(0.8d);

		EvoAcInteractionSeriesTask existingEvoAcTask = new EvoAcInteractionSeriesTask();
		existingEvoAcTask.setId(new Long(3));
		existingEvoAcTask.addSpeciesAssignmentBound(newAcSpeciesAssignmentBound);

		return newAcSpeciesAssignmentBound;
	}
}