package edu.banda.coel.server.dao.um;

import org.springframework.beans.factory.annotation.Autowired;

import com.banda.core.domain.um.Role;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class RoleDaoTest extends CoelBasicDaoTest<Role, Long> {

	private static final Long EXISTING_ROLE_ID = 1l;

	@Autowired
	GenericDAO<Role, Long> roleDAO;

	@Override
	public void setUpTestData() {
		setUp(roleDAO, Role.class, EXISTING_ROLE_ID);
	}
}