package edu.banda.coel.server.dao;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.banda.core.domain.task.Task;
import com.banda.serverbase.persistence.GenericHibernate3DAO;

/**
 * @author © Peter Banda
 * @since 2013
 */
public class TaskHibernateDAO extends GenericHibernate3DAO<Task, Long> implements TaskDAO {

	protected TaskHibernateDAO() {
		super(Task.class);
	}

	@Override
	public Collection<Task> getAllToRepeat() {
		Criteria c = createCriteria();
		c.addOrder(Order.asc(getIdPropertyName()));
		c.add(Restrictions.eq("repeat", Boolean.TRUE));
		return (Collection<Task>) c.list();
	}
}