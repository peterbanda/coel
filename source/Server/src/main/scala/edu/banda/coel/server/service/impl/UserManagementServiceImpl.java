package edu.banda.coel.server.service.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.banda.core.domain.um.Role;
import com.banda.core.domain.um.User;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.domain.service.UserManagementService;
import edu.banda.coel.server.common.SecurityUserDO;

/**
 * <p> Title: UserManagementServiceImpl </p>
 * 
 * @see edu.banda.coel.domain.service.UserManagementService
 * @author Peter Banda
 * @since 2009
 */
class UserManagementServiceImpl implements UserManagementService, UserDetailsService {

	/**
	 * Data Access Objects used by the services in this class to access
	 * persistent layer.
	 */
	private GenericDAO<User, Long> userDAO;
	private GenericDAO<Role, Long> roleDAO;

	//////////////
	// SERVICES //
	//////////////

	/**
	 * {@inheritDoc}
	 *
	 * @see edu.banda.coel.domain.service.UserManagementService#getAllRoles()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Role> getAllRoles() {
		return roleDAO.getAll();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see edu.banda.coel.domain.service.UserManagementService#getAllUsers()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<User> getAllUsers() {
		return userDAO.getAll();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see edu.banda.coel.domain.service.UserManagementService#getRole(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public Role getRole(String aRolename) {
		Role theFilterRole = new Role();
		theFilterRole.setName(aRolename);
		return roleDAO.getByUniqueExample(theFilterRole);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see edu.banda.coel.domain.service.UserManagementService#getUser(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public User getUser(Long userId) {
		return userDAO.get(userId);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see edu.banda.coel.domain.service.UserManagementService#getUserByPassword(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public User getUserByPassword(String userName, String password) {
		User theFilterUser = new User();
		theFilterUser.setUsername(userName);
		theFilterUser.setPassword(password);
		return userDAO.getByUniqueExample(theFilterUser);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see edu.banda.coel.domain.service.UserManagementService#getUserByUsername(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public User getUserByUsername(String userName) {
		User theFilterUser = new User();
		theFilterUser.setUsername(userName);
		return userDAO.getByUniqueExample(theFilterUser);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see edu.banda.coel.domain.service.UserManagementService#removeRole(java.lang.Long)
	 */
	@Override
	@Transactional
	public void removeRole(Long roleId) {
		roleDAO.remove(roleId);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see edu.banda.coel.domain.service.UserManagementService#removeUser(java.lang.Long)
	 */
	@Override
	@Transactional
	public void removeUser(Long userId) {
		userDAO.remove(userId);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see edu.banda.coel.domain.service.UserManagementService#saveRole(com.banda.core.domain.um.Role)
	 */
	@Override
	@Transactional
	public Role saveRole(Role role) {
		return roleDAO.save(role);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see edu.banda.coel.domain.service.UserManagementService#saveUser(com.banda.core.domain.um.User)
	 */
	@Override
	@Transactional
	public User saveUser(User user) {// throws UserExistsException {
		return userDAO.save(user);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
		User userDO = getUserByUsername(userName);
		if (userDO != null) {
			SecurityUserDO securityUser = new SecurityUserDO();
			securityUser.copyFrom(userDO);
			return securityUser;
		}
		return null;
	}

	////////////////
	// INJECTIONS //
	////////////////


	/**
	 * Gets the value of the attribute userDAO.
	 * 
	 * @return The value of the attribute userDAO
	 */
	public GenericDAO<User, Long> getUserDAO() {
		return userDAO;
	}

	/**
	 * Sets the value of the attribute userDAO.
	 *
	 * @param userDAO The value to set.
	 */
	public void setUserDAO(GenericDAO<User, Long> userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * Gets the value of the attribute roleDAO.
	 * 
	 * @return The value of the attribute roleDAO
	 */
	public GenericDAO<Role, Long> getRoleDAO() {
		return roleDAO;
	}

	/**
	 * Sets the value of the attribute roleDAO.
	 *
	 * @param roleDAO The value to set.
	 */
	public void setRoleDAO(GenericDAO<Role, Long> roleDAO) {
		this.roleDAO = roleDAO;
	}
}