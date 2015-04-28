package edu.banda.coel.domain.service;

import com.banda.core.grid.GridMetrics;

/**
 * TaskManager
 *
 * @author Peter Banda
 * @since 2013
 */
public interface TaskManager {

    /**
     * Runs tasks marked as 'repeat' if grid is idle.
     */
    void runRepetitiveTasks();

    /**
     * Gets the current grid metrics such as total active jobs, total cpus, average idle time, etc.
     *
     * @return
     */
    GridMetrics getGridMetrics();

    void cancelTask(String taskId);
}