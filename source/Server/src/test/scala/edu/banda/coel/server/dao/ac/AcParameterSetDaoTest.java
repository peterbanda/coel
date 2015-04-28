package edu.banda.coel.server.dao.ac;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.banda.chemistry.domain.AcParameterSet;
import com.banda.chemistry.domain.AcSpeciesSet;
import com.banda.core.domain.um.User;
import com.banda.serverbase.persistence.GenericDAO;
import com.banda.serverbase.test.AbstractDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcParameterSetDaoTest extends AbstractDaoTest<AcParameterSet, Long> {

	@Autowired
	@Qualifier(value="acParameterSetDAO")
	GenericDAO<AcParameterSet, Long> acParameterSetDAO;

	@Autowired
	@Qualifier(value="acSpeciesSetDAO")
	GenericDAO<AcSpeciesSet, Long> acSpeciesSetDAO;

	@Override
	protected GenericDAO<AcParameterSet, Long> getDao() {
		return acParameterSetDAO;
	}

	@Override
	protected Long getExistingTestKey() {
		return new Long(1);
	}

	@Override
	protected AcParameterSet getTestObject() {
		AcSpeciesSet newAcSpeciesSet = new AcSpeciesSet();
		newAcSpeciesSet.setId(new Long(-1));
		newAcSpeciesSet.setName("LALA");
		newAcSpeciesSet.setCreateTime(new Date());
		newAcSpeciesSet.setVarSequenceNum(4);

		AcParameterSet newAcParameterSet = new AcParameterSet();
		newAcParameterSet.setId(new Long(-1));
		newAcParameterSet.setName("LALA");
		newAcParameterSet.setCreateTime(new Date());
		newAcParameterSet.setSpeciesSet(newAcSpeciesSet);
		newAcSpeciesSet.setParameterSet(newAcParameterSet);

		User existingUser = new User();
		existingUser.setId(new Long(1));
		newAcParameterSet.setCreatedBy(existingUser);
		newAcSpeciesSet.setCreatedBy(existingUser);

		newAcSpeciesSet = acSpeciesSetDAO.save(newAcSpeciesSet);
		newAcParameterSet = newAcSpeciesSet.getParameterSet();

		return newAcParameterSet;
	}

	@Test
	@Ignore
	public void testRemove() {
		super.testRemove();
	}
}