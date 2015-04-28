package edu.banda.coel.server.dao.ac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.banda.chemistry.domain.AcRateConstantTypeBound;
import com.banda.chemistry.domain.AcRateConstantType;
import com.banda.serverbase.persistence.GenericDAO;
import com.banda.serverbase.test.AbstractDaoTest;

import edu.banda.coel.domain.evo.EvoAcRateConstantTask;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcRateConstantTypeBoundDaoTest extends AbstractDaoTest<AcRateConstantTypeBound, Long> {

	@Autowired
	@Qualifier(value="acRateConstantTypeBoundDAO")
	GenericDAO<AcRateConstantTypeBound, Long> acRateConstantTypeBoundDAO;

	@Override
	protected GenericDAO<AcRateConstantTypeBound, Long> getDao() {
		return acRateConstantTypeBoundDAO;
	}

	@Override
	protected Long getExistingTestKey() {
		return new Long(1);
	}

	@Override
	protected AcRateConstantTypeBound getTestObject() {
		AcRateConstantTypeBound newAcRateConstantTypeBound = new AcRateConstantTypeBound();
		newAcRateConstantTypeBound.setId(new Long(-1));
		newAcRateConstantTypeBound.setRateConstantType(AcRateConstantType.NoncooperativeInhibitition);
		newAcRateConstantTypeBound.setFrom(0.1d);
		newAcRateConstantTypeBound.setTo(0.8d);

		EvoAcRateConstantTask existingEvoAcTask = new EvoAcRateConstantTask();
		existingEvoAcTask.setId(new Long(1));
		existingEvoAcTask.addRateConstantTypeBound(newAcRateConstantTypeBound);

		return newAcRateConstantTypeBound;
	}
}