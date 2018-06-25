/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgment if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */
package gov.nist.hit.core.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import gov.nist.auth.hit.core.domain.util.UserUtil;
import gov.nist.hit.core.service.CustomJdbcUserDetailsManager;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.exception.NoUserFoundException;

/**
 * @author fdevaulx
 * 
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

  static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  private final String DEFAULT_AUTHORITY = "user";
  private final String TESTER_AUTHORITY = "tester";
  private final String ADMIN_AUTHORITY = "admin";
  private final String DEPLOYER_AUTHORITY = "deployer";
  private final String SUPERVISOR_AUTHORITY = "supervisor";

  Set<String> PUBLIC_AUTHORITIES =
      new HashSet<>(Arrays.asList(SUPERVISOR_AUTHORITY, ADMIN_AUTHORITY));


  Set<String> ADMIN_AUTHORITIES = new HashSet<>(Arrays.asList(ADMIN_AUTHORITY));



  @Autowired
  private CustomJdbcUserDetailsManager jdbcUserDetailsManager;

  @Autowired
  @Qualifier(value = "shaPasswordEncoder")
  private ShaPasswordEncoder passwordEncoder;

  // @Autowired
  // private ReflectionSaltSource saltSource;

  /*
   * (non-Javadoc)
   * 
   * @see gov.nist.itl.healthcare.ehrrandomizer.service.impl.UserService#userExists
   * (java.lang.String)
   */
  @Override
  public Boolean userExists(String username) {
    return jdbcUserDetailsManager.userExists(username);
  }

  /*
   * (non-Javadoc)
   * 
   * @see gov.nist.itl.healthcare.ehrrandomizer.service.impl.UserService#createUser
   * (java.lang.String, java.lang.String)
   */
  @Override
  public void createUserWithDefaultAuthority(String username, String password) {
    List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
    roles.add(new SimpleGrantedAuthority(DEFAULT_AUTHORITY));

    UserDetails userDetails = new User(username, passwordEncoder.encodePassword(password, username),
        true, true, true, true, roles);

    jdbcUserDetailsManager.createUser(userDetails);
  }

  /*
   * (non-Javadoc)
   * 
   * @see gov.nist.itl.healthcare.ehrrandomizer.service.impl.UserService#changePassword
   * (java.lang.String, java.lang.String)
   */
  @Override
  public void changePasswordForPrincipal(String oldPassword, String newPassword)
      throws BadCredentialsException {

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    User onRecordUser = this.retrieveUserByUsername(username);

    String oldEncodedPassword = onRecordUser.getPassword();
    String newEncodedPassword = passwordEncoder.encodePassword(newPassword, username);
    // logger.debug("[PASS] - old: " + oldEncodedPassword + " - new: " +
    // newEncodedPassword);
    if (oldEncodedPassword.equals(newEncodedPassword)) {
      throw new BadCredentialsException("New password must be different from previous password");
    }
    jdbcUserDetailsManager.changePassword(oldPassword, newEncodedPassword);
  }

  /*
   * (non-Javadoc)
   * 
   * @see gov.nist.itl.healthcare.ehrrandomizer.service.UserService#changePassword
   * (java.lang.String, java.lang.String, java.lang.String)
   */
  @Override
  public void changePasswordForUser(String oldPassword, String newPassword, String username)
      throws BadCredentialsException {
    String newEncodedPassword = passwordEncoder.encodePassword(newPassword, username);
    // logger.debug("[PASS] - old: " + oldPassword + " - new: " +
    // newEncodedPassword);
    if (oldPassword.equals(newEncodedPassword)) {
      throw new BadCredentialsException("New password must be different from previous password");
    }
    jdbcUserDetailsManager.getJdbcTemplate().update(jdbcUserDetailsManager.DEF_CHANGE_PASSWORD_SQL,
        newEncodedPassword, username);
  }


  @Override
  public void changePasswordForUser(String newPassword, String username)
      throws BadCredentialsException {
    String newEncodedPassword = passwordEncoder.encodePassword(newPassword, username);
    // logger.debug("[PASS] - old: " + oldPassword + " - new: " +
    // newEncodedPassword);
    jdbcUserDetailsManager.getJdbcTemplate().update(jdbcUserDetailsManager.DEF_CHANGE_PASSWORD_SQL,
        newEncodedPassword, username);
  }

  @Override
  public void changeAccountTypeForUser(String newAccountType, String username)
      throws BadCredentialsException {
    jdbcUserDetailsManager.getJdbcTemplate()
        .update(jdbcUserDetailsManager.DEF_DELETE_USER_AUTHORITIES_SQL, username);
    jdbcUserDetailsManager.getJdbcTemplate().update(jdbcUserDetailsManager.DEF_INSERT_AUTHORITY_SQL,
        username, TESTER_AUTHORITY);
    jdbcUserDetailsManager.getJdbcTemplate().update(jdbcUserDetailsManager.DEF_INSERT_AUTHORITY_SQL,
        username, DEFAULT_AUTHORITY);
    if (!TESTER_AUTHORITY.equals(newAccountType)) {
      jdbcUserDetailsManager.getJdbcTemplate()
          .update(jdbcUserDetailsManager.DEF_INSERT_AUTHORITY_SQL, username, newAccountType);
      if (ADMIN_AUTHORITY.equals(newAccountType)) {
        jdbcUserDetailsManager.getJdbcTemplate()
            .update(jdbcUserDetailsManager.DEF_INSERT_AUTHORITY_SQL, username, DEPLOYER_AUTHORITY);
      }
    }
  }


  /*
   * (non-Javadoc)
   * 
   * @see gov.nist.itl.healthcare.ehrrandomizer.service.impl.UserService#deleteUser
   * (java.lang.String)
   */
  @Override
  public void deleteUser(String username) {
    jdbcUserDetailsManager.deleteUser(username);
  }

  /*
   * (non-Javadoc)
   * 
   * @see gov.nist.itl.healthcare.ehrrandomizer.service.UserService#disableUser (java.lang.String)
   */
  @Override
  public void disableUser(String username) {
    User u = (User) jdbcUserDetailsManager.loadUserByUsername(username);
    // logger.debug("^^^^^^^^^^^^^^^^^^ u: "+u+" ^^^^^^^^^^^^^^^^");
    if (u != null) {
      // logger.debug("^^^^^^^^^^^^^^^^^^ u.isEnabled? "+u.isEnabled()+" ^^^^^^^^^^^^^^^^");
      if (u.isEnabled()) {
        // logger.debug("^^^^^^^^^^^^^^^^^^ 0 ^^^^^^^^^^^^^^^^");
        jdbcUserDetailsManager.getJdbcTemplate()
            .update("update users set enabled = 0 where username = ?", u.getUsername());
      }
    }
  }

  @Override
  public void createUserWithAuthorities(String username, String password, Object authorities)
      throws Exception {
    String authoritiesString = (String) authorities;

    List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
    roles.addAll(AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesString));

    List<String> authList = new ArrayList<String>(Arrays.asList(UserUtil.AUTHORITY_LIST));
    for (GrantedAuthority ga : roles) {
      if (!authList.contains(ga.getAuthority())) {
        throw new Exception("Invalid authorization setting used");
      }
    }

    UserDetails userDetails = new User(username, passwordEncoder.encodePassword(password, username),
        true, true, true, true, roles);

    jdbcUserDetailsManager.createUser(userDetails);
  }

  @Override
  public List<String> findAllEnabledUsers() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<String> findAllDisabledUsers() {
    // TODO Auto-generated method stub
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public User retrieveUserByUsername(String username) {
    return (User) jdbcUserDetailsManager.loadUserByUsername(username);
  }

  /*
   * (non-Javadoc)
   * 
   * @see gov.nist.itl.healthcare.ehrrandomizer.service.UserService#getCurrentUser ()
   */
  @Override
  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
      return null;
    }

    return (User) authentication.getPrincipal();
  }

  @Override
  public void enableUserCredentials(String username) {
    User u = (User) jdbcUserDetailsManager.loadUserByUsername(username);
    // logger.debug("^^^^^^^^^^^^^^^^^^ u: "+u+" ^^^^^^^^^^^^^^^^");
    if (u != null) {
      // logger.debug("^^^^^^^^^^^^^^^^^^ u.isEnabled? "+u.isEnabled()+" ^^^^^^^^^^^^^^^^");
      if (u.isEnabled()) {
        // logger.debug("^^^^^^^^^^^^^^^^^^ 0 ^^^^^^^^^^^^^^^^");
        jdbcUserDetailsManager.getJdbcTemplate().update(
            "update users set credentialsNonExpired = 1 where username = ?", u.getUsername());
      }
    }
  }

  @Override
  public boolean hasGlobalAuthorities(String username) throws NoUserFoundException {
    User user = this.retrieveUserByUsername(username);
    if (user == null) {
      throw new NoUserFoundException("User could not be found");
    }
    Collection<GrantedAuthority> authorit = user.getAuthorities();
    for (GrantedAuthority auth : authorit) {
      if (PUBLIC_AUTHORITIES.contains(auth.getAuthority())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isAdmin(String username) throws NoUserFoundException {
    User user = this.retrieveUserByUsername(username);
    if (user == null) {
      throw new NoUserFoundException("User could not be found");
    }
    Collection<GrantedAuthority> authorit = user.getAuthorities();
    for (GrantedAuthority auth : authorit) {
      if (ADMIN_AUTHORITIES.contains(auth.getAuthority())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isSupervisor(String username) throws NoUserFoundException {
    User user = this.retrieveUserByUsername(username);
    if (user == null) {
      throw new NoUserFoundException("User could not be found");
    }
    Collection<GrantedAuthority> authorit = user.getAuthorities();
    for (GrantedAuthority auth : authorit) {
      if (SUPERVISOR_AUTHORITY.equals(auth.getAuthority())) {
        return true;
      }
    }
    return false;
  }



}
