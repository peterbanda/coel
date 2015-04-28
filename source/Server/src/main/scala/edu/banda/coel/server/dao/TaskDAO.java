package edu.banda.coel.server.dao;

import java.util.Collection;

import com.banda.core.domain.task.Task;
import com.banda.serverbase.persistence.GenericDAO;

/**
 * @author © Peter Banda
 * @since 2013
 */
public interface TaskDAO extends GenericDAO<Task, Long>{

	Collection<Task> getAllToRepeat();
}