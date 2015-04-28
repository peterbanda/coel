package edu.banda.coel.server.service.impl;

import com.banda.core.grid.GridMetrics;
import com.banda.serverbase.grid.ComputationalGrid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.banda.core.domain.task.Task;
import com.banda.math.task.EvoRunTask;

import edu.banda.coel.domain.service.EvolutionService;
import edu.banda.coel.domain.service.TaskManager;
import edu.banda.coel.server.dao.TaskDAO;

/**
 * Title: ArtificialChemistryServiceImpl
 *
 * @see edu.banda.coel.domain.service.ArtificialChemistryService
 * @author Peter Banda
 * @since 2013
 */
class TaskManagerImpl implements TaskManager {

	@Autowired
	private TaskDAO taskDAO;
//	@Autowired
//	private ArtificialChemistryService artificialChemistryService;
	@Autowired
	private EvolutionService evolutionService;

	@Autowired
	private ComputationalGrid grid;

	private int maxGridActiveJobsToRunTasks = 5;

    protected final Log log = LogFactory.getLog(getClass());

	//////////////
	// SERVICES //
	//////////////

    @Transactional
	@Override
	public void runRepetitiveTasks() {
    	if (grid.getTotalActiveJobs() <= maxGridActiveJobsToRunTasks) {
    		log.info("Computational grid sits idle (jobs: " + grid.getTotalActiveJobs() + ").. Submitting repetitive tasks.");
    		for (Task task : taskDAO.getAllToRepeat()) {
    			if (task instanceof EvoRunTask) {
    				evolutionService.evolve((EvoRunTask) task);
    			}
    		}
    	}
	}

    @Transactional
	@Override
    public GridMetrics getGridMetrics() {
    	return grid.getMetrics();
    }

    @Transactional
	@Override
    public void cancelTask(String taskId) {
    	grid.cancelTask(taskId);
    }
}