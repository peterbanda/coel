package edu.banda.coel.server.dao.evo;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.chemistry.domain.AcInteractionVariableAssignment;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.domain.evo.AcInteractionVariableAssignmentBound;
import edu.banda.coel.domain.evo.EvoAcInteractionSeriesTask;
import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcInteractionVariableAssignmentBoundDaoTest extends CoelBasicDaoTest<AcInteractionVariableAssignmentBound, Long> {

	private static final Long EXISTING_ID = 1l;

	@Autowired
	GenericDAO<AcInteractionVariableAssignmentBound, Long> acInteractionVariableAssignmentBoundDAO;

	@Override
	public void setUpTestData() {
		setUp(acInteractionVariableAssignmentBoundDAO, AcInteractionVariableAssignmentBound.class, EXISTING_ID);
	}

	@Override
	protected GenericDAO<AcInteractionVariableAssignmentBound, Long> getDao() {
		return acInteractionVariableAssignmentBoundDAO;
	}

	@Override
	protected AcInteractionVariableAssignmentBound getTestObject() {
		AcInteractionVariableAssignmentBound newAcInteractionVariableAssignmentBound = new AcInteractionVariableAssignmentBound();
//		newAcInteractionVariableAssignmentBound.setId(new Long(-1));
		AcInteractionVariableAssignment variableAssignment = new AcInteractionVariableAssignment();
		variableAssignment.setId(1l);

		newAcInteractionVariableAssignmentBound.addAssignment(variableAssignment);
		newAcInteractionVariableAssignmentBound.setFrom(0.1d);
		newAcInteractionVariableAssignmentBound.setTo(0.8d);

		EvoAcInteractionSeriesTask existingEvoAcTask = new EvoAcInteractionSeriesTask();
		existingEvoAcTask.setId(new Long(1));
		existingEvoAcTask.addVariableAssignmentBound(newAcInteractionVariableAssignmentBound);

		return newAcInteractionVariableAssignmentBound;
	}
}