package edu.banda.coel.server.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.banda.coel.domain.service.TaskManager;
import edu.banda.coel.server.CoelTest;

/**
 * @author Peter Banda
 * @since 2012
 */
@Transactional
public class TaskManagerTest extends CoelTest {

	@Autowired
	TaskManager taskManager;
	
	@Test
	public void testRunRepetitiveTasks() {
		taskManager.runRepetitiveTasks();
	}
}