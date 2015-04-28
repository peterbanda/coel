package edu.banda.coel.domain.service;

import java.util.List;

import com.banda.core.domain.um.Role;
import com.banda.core.domain.um.User;

/**
 * UserManagementService
 *
 * @author Peter Banda
 * @since 2012
 */
public interface UserManagementService {

    /**
     * Retrieves the user by the id. An exception is thrown if the required user is not found.
     *
     * @param userId The user's identifier
     * @return User
     */
    User getUser(Long userId);

    /**
     * Retrieves the user by the name and password. An exception is thrown if the required user is not found.
     *
     * @param userName The user's name
     * @param password The user's password
     * @return User
     */
    User getUserByPassword(String userName, String password);

    /**
     * Retrieves the user by the name. An exception is thrown if the required user is not found.
     *
     * @param userName The user's name
     * @return User
     */
    User getUserByUsername(String userName);

    /**
     * Retrieves all users.
     * @return Users
     */
    List<User> getAllUsers();

    /**
     * Saves the user.
     *
     * @param user
     *
     * @throws UserExistsException thrown if the user already exists
     * @return user The updated user
     */
    User saveUser(User user);
    //throws UserExistsException;

    /**
     * Removes the user with the given id.
     *
     * @param userId The user's id
     */
    void removeUser(Long userId);

	/**
     * Retrieves all roles.
     * @return All roles
     */
	List<Role> getAllRoles();

	/**
     * Finds the role by the name.
     * @param rolename
     * @return Role
     */
	Role getRole(String rolename);

	/**
     * Saves the role.
     *
     * @param role
     * @return The updated role
     */
	Role saveRole(Role role);

	/**
     * Removes the role with the given id.
     *
     * @param roleId The role's id
     */
	void removeRole(Long roleId);
}
