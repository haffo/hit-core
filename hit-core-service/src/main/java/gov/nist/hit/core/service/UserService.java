package gov.nist.hit.core.service;

import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;

import gov.nist.hit.core.service.exception.NoUserFoundException;

public interface UserService {

	/**
	 * Returns whether a user exists
	 * 
	 * @param username
	 *            the name of a user
	 * @return whether the user exists
	 */
	public abstract Boolean userExists(String username);

	/**
	 * Returns whether a user exists
	 * 
	 * @param <T>
	 * 
	 * @param username
	 *            the name of a user
	 * @return whether the user exists
	 */
	public abstract <T> T retrieveUserByUsername(String username);

	/**
	 * Creates a user
	 * 
	 * @param username
	 *            the name of the user
	 * @param password
	 *            the password
	 */
	public abstract void createUserWithDefaultAuthority(String username, String password);

	/**
	 * Creates a user with given authorities
	 * 
	 * @param <T>
	 * 
	 * @param username
	 *            the name of the user
	 * @param password
	 *            the password
	 * @param authorities
	 *            the list of the authorities
	 */
	public abstract <T> void createUserWithAuthorities(String username, String password, T authorities)
			throws Exception;

	/**
	 * Changes the password for this principal
	 * 
	 * @param oldPassword
	 *            the current password for this principal
	 * @param newPassword
	 *            the new password for this principal
	 * @throws BadCredentialsException
	 *             the oldPassword is incorrect
	 */
	public abstract void changePasswordForPrincipal(String oldPassword, String newPassword)
			throws BadCredentialsException;

	/**
	 * Changes the password for this username
	 * 
	 * @param oldPassword
	 *            the current password for this principal
	 * @param newPassword
	 *            the new password for this principal
	 * @throws BadCredentialsException
	 *             the oldPassword is incorrect
	 */
	public abstract void changePasswordForUser(String oldPassword, String newPassword, String username)
			throws BadCredentialsException;

	/**
	 * Changes the password for this username
	 * 
	 * @param newPassword
	 *            the new password for this principal
	 * @throws BadCredentialsException
	 *             the oldPassword is incorrect
	 */
	public void changePasswordForUser(String newPassword, String username) throws BadCredentialsException;

	/**
	 * Deletes a user
	 * 
	 * @param username
	 *            the name of the user to delete
	 */
	public abstract void deleteUser(String username);

	/**
	 * Disables a user
	 * 
	 * @param username
	 *            the name of the user to disable
	 */
	public abstract void disableUser(String username);

	/**
	 * Returns the list of enabled users
	 * 
	 * @return the names of the users
	 */
	public abstract List<String> findAllEnabledUsers();

	/**
	 * Returns the list of disabled users
	 * 
	 * @return the names of the users
	 */
	public abstract List<String> findAllDisabledUsers();

	/**
	 * 
	 * */
	public abstract org.springframework.security.core.userdetails.User getCurrentUser();

	/**
	     * 
	     */
	public void enableUserCredentials(String username);

	/**
	 * 
	 * @param newAccountType
	 * @param username
	 * @throws BadCredentialsException
	 */
	public void changeAccountTypeForUser(String newAccountType, String username) throws BadCredentialsException;

	public boolean hasGlobalAuthorities(String username) throws NoUserFoundException;

	public boolean isAdmin(String username) throws NoUserFoundException;

	boolean isSupervisor(String username) throws NoUserFoundException;

	public boolean isAdminByEmail(String email) throws NoUserFoundException;

	boolean isDeployer(String username) throws NoUserFoundException;

}
