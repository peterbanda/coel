package edu.banda.coel.server.dao.um;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.banda.core.domain.um.User;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class UserDaoTest extends CoelBasicDaoTest<User, Long> {

	private static final Long EXISTING_USER_ID = 1l;

	@Autowired
	GenericDAO<User, Long> userDAO;

	@Override
	public void setUpTestData() {
		setUp(userDAO, User.class, EXISTING_USER_ID);
	}
}