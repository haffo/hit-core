package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.domain.TestingType;
import gov.nist.hit.core.domain.Transaction;
import gov.nist.hit.core.domain.TransportConfig;
import gov.nist.hit.core.domain.User;
import gov.nist.hit.core.repo.UserRepository;
import gov.nist.hit.core.service.CustomJdbcUserDetailsManager;
import gov.nist.hit.core.service.TestStepValidationReportService;
import gov.nist.hit.core.service.TransactionService;
import gov.nist.hit.core.service.TransportConfigService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.util.UserUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  private TransactionService transactionService;

  //
  @Autowired
  protected EntityManager entityManager;

  @Autowired
  protected TransportConfigService transportConfigService;

  @Autowired
  protected TestStepValidationReportService validationReportService;

  @Override
  public boolean exitBySutInitiatorPropertiesAndProtocol(Map<String, String> criteria,
      String protocol) {
    TransportConfig config =
        transportConfigService.findOneByPropertiesAndProtocol(criteria, TestingType.SUT_INITIATOR,
            protocol);
    return config != null && config.getUserId() != null;
  }

  @Override
  public void delete(User user) {
    userRepository.delete(user);
  }

  @Override
  public void delete(Long id) {
    if (id != null) {
      List<TransportConfig> configs = transportConfigService.findAllByUser(id);
      if (configs != null) {
        transportConfigService.delete(configs);
      }
      List<Transaction> transactions = transactionService.findAllByUser(id);
      if (transactions != null) {
        transactionService.delete(transactions);
      }

      List<TestStepValidationReport> reports = validationReportService.findAllByUser(id);
      if (reports != null)
        validationReportService.delete(reports);

      userRepository.delete(id);
    }
  }

  @Override
  public User save(User user) {
    return userRepository.saveAndFlush(user);
  }

  @Override
  public User findOne(Long id) {
    return userRepository.findOne(id);
  }


  private final String DEFAULT_AUTHORITY = "user";

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

    UserDetails userDetails =
        new org.springframework.security.core.userdetails.User(username,
            passwordEncoder.encodePassword(password, username), true, true, true, true, roles);

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
    org.springframework.security.core.userdetails.User onRecordUser =
        this.retrieveUserByUsername(username);

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
    org.springframework.security.core.userdetails.User u =
        (org.springframework.security.core.userdetails.User) jdbcUserDetailsManager
            .loadUserByUsername(username);
    // logger.debug("^^^^^^^^^^^^^^^^^^ u: "+u+" ^^^^^^^^^^^^^^^^");
    if (u != null) {
      // logger.debug("^^^^^^^^^^^^^^^^^^ u.isEnabled? "+u.isEnabled()+" ^^^^^^^^^^^^^^^^");
      if (u.isEnabled()) {
        // logger.debug("^^^^^^^^^^^^^^^^^^ 0 ^^^^^^^^^^^^^^^^");
        jdbcUserDetailsManager.getJdbcTemplate().update(
            "update users set enabled = 0 where username = ?", u.getUsername());
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

    UserDetails userDetails =
        new org.springframework.security.core.userdetails.User(username,
            passwordEncoder.encodePassword(password, username), true, true, true, true, roles);

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
  public org.springframework.security.core.userdetails.User retrieveUserByUsername(String username) {
    return (org.springframework.security.core.userdetails.User) jdbcUserDetailsManager
        .loadUserByUsername(username);
  }

  /*
   * (non-Javadoc)
   * 
   * @see gov.nist.itl.healthcare.ehrrandomizer.service.UserService#getCurrentUser ()
   */
  @Override
  public org.springframework.security.core.userdetails.User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
      return null;
    }

    return (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
  }

  @Override
  public void enableUserCredentials(String username) {
    org.springframework.security.core.userdetails.User u =
        (org.springframework.security.core.userdetails.User) jdbcUserDetailsManager
            .loadUserByUsername(username);
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


}
