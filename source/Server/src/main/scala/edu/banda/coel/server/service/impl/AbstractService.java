package edu.banda.coel.server.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.banda.serverbase.grid.ComputationalGrid;
import net.sf.beanlib.hibernate.HibernateBeanReplicator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.banda.core.domain.KeyHolder;
import com.banda.core.reflection.GenericReflectionProvider;
import com.banda.core.util.ObjectUtil;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.core.task.SimpleMessageEvent;

/**
 * Title: AbstractService
 *
 * @author Peter Banda
 * @since 2013
 */
public abstract class AbstractService {

	@Autowired
	protected ComputationalGrid computationalGrid;

	@Autowired
	protected HibernateBeanReplicator hibernateReplicator;

	@Autowired
	protected ApplicationContext applicationContext;

	@Autowired
	protected GenericReflectionProvider genericReflectionProvider;

    protected final Log log = LogFactory.getLog(getClass());

    protected <T> T copyWithoutHibernate(T object) {
		return hibernateReplicator.copy(object);
	}

    protected <T> Collection<T> copyWithoutHibernate(Collection<T> objects) {
		Collection<T> newCollection = new ArrayList<T>();
		for (T object : objects) {
			newCollection.add(copyWithoutHibernate(object));
		}
		return newCollection;
	}

    protected <T> T saveObjectAndPostMessage(GenericDAO<T, ?> dao, T object, String name) {
		T savedObject = dao.save(object);
		postAndLogSaveMessageForKeys(Collections.singleton(dao.getKey(savedObject)), name);
		return savedObject;
	}

    protected <T, PK extends Serializable> Collection<T> saveObjectsAndPostMessage(GenericDAO<T, PK> dao, Collection<T> objects, String name) {
		Collection<T> savedObjects = dao.save(objects);
		Collection<PK> keys = new ArrayList<PK>();
		for (T object : objects) {
			keys.add(dao.getKey(object));
		}
		postAndLogSaveMessageForKeys(keys, name);
		return savedObjects;
	}

    protected void postAndLogSaveMessage(KeyHolder<?> savedObject, String name) {
		postAndLogSaveMessage(Collections.singleton(savedObject), name);
	}

    protected void postAndLogSaveMessageForKey(Object key, String name) {
    	postAndLogSaveMessageForKeys(Collections.singleton(key), name);
	}

    protected void postAndLogSaveMessage(Collection<? extends KeyHolder<?>> savedObjects, String name) {
		String message;
		if (savedObjects.size() == 1) {
			message = name + " " + ObjectUtil.getFirst(savedObjects).getKey() + " created";
		} else {
			message = savedObjects.size() + " " + name + "s created: " + ObjectUtil.getFirst(savedObjects).getKey() + ", " + ObjectUtil.getSecond(savedObjects).getKey() + ", ...";
		}
		log.info(message);
		postMessage(message);
	}

    protected void postAndLogSaveMessageForKeys(Collection<?> keys, String name) {
		String message;
		if (keys.size() == 1) {
			message = name + " " + ObjectUtil.getFirst(keys) + " created";
		} else {
			message = keys.size() + " " + name + "s created: " + ObjectUtil.getFirst(keys) + ", " + ObjectUtil.getSecond(keys) + ", ...";
		}
		log.info(message);
		postMessage(message);
	}

    protected void postMessage(String message) {
		applicationContext.publishEvent(new SimpleMessageEvent(message));
	}
}