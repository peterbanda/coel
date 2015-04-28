package edu.banda.coel.server.dao;

import java.io.Serializable;

import org.springframework.test.context.ContextConfiguration;

import com.banda.serverbase.test.BasicDaoTest;

/**
 * Abstract class testing the core DAO functionality in COEL project.
 * 
 * @param <T> Object handled by DAO
 * @param <PK> Primary key of object handled by DAO
 * @see com.banda.serverbase.persistence.GenericDAO
 * 
 * @author © Peter Banda
 * @since 2012
 */
@ContextConfiguration(locations = {
		"classpath:test-config.xml", "classpath:server.xml", "classpath:persistence.xml"})
public abstract class CoelBasicDaoTest<T, PK extends Serializable> extends BasicDaoTest<T, PK> {

}