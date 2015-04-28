package edu.banda.coel.server.dao.task;

import java.util.Collection;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.banda.core.domain.task.Task;
import com.banda.math.task.EvoRunTask;

import edu.banda.coel.server.dao.CoelBasicDaoTest;
import edu.banda.coel.server.dao.TaskDAO;

/**
 * @author Peter Banda
 * @since 2011
 */
public class TaskDaoTest extends CoelBasicDaoTest<Task, Long> {

	private static final Long EXISTING_TASK_ID = 1l;

	@Autowired
	TaskDAO taskDAO;

	@Override
	public void setUpTestData() {
		setUp(taskDAO, EvoRunTask.class, EXISTING_TASK_ID);
	}

	@Test
	public void testGetAllToRepeat() {
		Collection<Task> taskDefsToRepeat = taskDAO.getAllToRepeat();
		assertFalse(taskDefsToRepeat.isEmpty());
	}

	@Override
	protected Task getTestObject() {
		EvoRunTask newEvoRunTaskDef = (EvoRunTask) super.getTestObject();

		newEvoRunTaskDef.setEvoTaskId(1l);

		return newEvoRunTaskDef;
	}
}