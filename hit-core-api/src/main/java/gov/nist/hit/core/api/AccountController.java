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
package gov.nist.hit.core.api;

import static org.springframework.data.jpa.domain.Specifications.where;
import gov.nist.auth.hit.core.domain.Account;
import gov.nist.auth.hit.core.domain.AccountChangeCredentials;
import gov.nist.auth.hit.core.domain.AccountPasswordReset;
import gov.nist.auth.hit.core.domain.CurrentUser;
import gov.nist.auth.hit.core.domain.ShortAccount;
import gov.nist.auth.hit.core.domain.util.UserUtil;
import gov.nist.auth.hit.core.repo.util.AccountSpecsHelper;
import gov.nist.hit.core.domain.ResponseMessage;
import gov.nist.hit.core.service.AccountPasswordResetService;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.CustomSortHandler;
import gov.nist.hit.core.service.RandomPasswordGenerator;
import gov.nist.hit.core.service.TestCaseValidationReportService;
import gov.nist.hit.core.service.UserService;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

/**
 * @author fdevaulx
 * @author Harold Affo
 * 
 */
@RestController
@PropertySource(value = {"classpath:app-auth-config.properties"})
public class AccountController {

  static final Logger logger = LoggerFactory.getLogger(AccountController.class);

  public final String DEFAULT_PAGE_SIZE = "0";


  public AccountController() {}


  @Autowired
  AccountService accountService;

  @Autowired
  UserService userService;

  @Autowired
  private MailSender mailSender;

  @Autowired
  private SimpleMailMessage templateMessage;

  private final String AUTHORIZED_ACCOUNT_TYPE_UNAUTH_REG = "tester";

  @Value("${server.email}")
  private String SERVER_EMAIL;

  @Value("${admin.email}")
  private String ADMIN_EMAIL;


  @Value("${mail.tool}")
  private String TOOL_NAME;


  @Autowired
  TestCaseValidationReportService testCaseValidationService;


  @Autowired
  AccountPasswordResetService accountPasswordResetService;


  @PreAuthorize("hasRole('supervisor') or hasRole('admin')")
  @RequestMapping(value = "/accounts", method = RequestMethod.GET)
  public List<Account> getAccounts() {

    List<Account> accs = new LinkedList<Account>();

    for (Account acc : accountService.findAll()) {
      if (!acc.isEntityDisabled()) {
        accs.add(acc);
      }
    }
    return accs;
  }

  @PreAuthorize("hasRole('supervisor') or hasRole('admin')")
  @RequestMapping(value = "/authors/page", method = RequestMethod.GET)
  public Page<ShortAccount> getProvidersPage(
      @RequestParam(required = false, defaultValue = "0") int value, @RequestParam(
          required = false, defaultValue = DEFAULT_PAGE_SIZE) int size, @RequestParam(
          required = false) List<String> sort, @RequestParam(required = false) List<String> filter) {

    List<ShortAccount> saccs = new LinkedList<ShortAccount>();

    AccountSpecsHelper accH = new AccountSpecsHelper();

    Page<Account> pa =
        accountService.findAll((where(accH.getSpecificationFromString("accountType", "tester"))
            .and(accH.getSpecification(filter))), new PageRequest(value, size,
            (new CustomSortHandler(sort)).getSort()));

    if (pa.getContent() != null && !pa.getContent().isEmpty()) {
      for (Account acc : pa.getContent()) {
        if (!acc.isEntityDisabled()) {

          ShortAccount sacc = new ShortAccount();
          sacc.setId(acc.getId());
          sacc.setEmail(acc.getEmail());
          sacc.setFullName(acc.getFullName());
          sacc.setUsername(acc.getUsername());
          saccs.add(sacc);
        }
      }
    }

    Pageable p = new PageRequest(pa.getNumber(), pa.getSize(), pa.getSort());
    Page<ShortAccount> sap = new PageImpl<ShortAccount>(saccs, p, pa.getTotalElements());

    return sap;
  }

  // @PreAuthorize("hasRole('supervisor') or hasRole('admin')")
  // @RequestMapping(value = "/authorizedVendors/page", method =
  // RequestMethod.GET)
  // public Page<ShortAccount> getAuthorizedVendorsPage(
  // @RequestParam(required = false, defaultValue = "0") int value,
  // @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) int
  // size,
  // @RequestParam(required = false) List<String> sort,
  // @RequestParam(required = false) List<String> filter) {
  //
  // List<ShortAccount> saccs = new LinkedList<ShortAccount>();
  //
  // AccountSpecsHelper accH = new AccountSpecsHelper();
  //
  // Page<Account> pa = accountService.findAll((where(accH
  // .getSpecificationFromString("accountType", "authorizedVendor"))
  // .and(accH.getSpecification(filter))), new PageRequest(value,
  // size, (new CustomSortHandler(sort)).getSort()));
  //
  // if (pa.getContent() != null && !pa.getContent().isEmpty()) {
  // for (Account acc : pa.getContent()) {
  // if (!acc.isEntityDisabled()) {
  //
  // ShortAccount sacc = new ShortAccount();
  // sacc.setId(acc.getId());
  // sacc.setEmail(acc.getEmail());
  // sacc.setFirstname(acc.getFirstname());
  // sacc.setLastname(acc.getLastname());
  // sacc.setCompany(acc.getCompany());
  //
  // saccs.add(sacc);
  // }
  // }
  // }
  //
  // Pageable p = new PageRequest(pa.getNumber(), pa.getSize(), pa.getSort());
  // Page<ShortAccount> sap = new PageImpl<ShortAccount>(saccs, p,
  // pa.getTotalElements());
  //
  // return sap;
  // }

  @PreAuthorize("hasRole('supervisor') or hasRole('admin')")
  @RequestMapping(value = "/shortaccounts/page", method = RequestMethod.GET)
  public Page<ShortAccount> getShortAccountsPage(
      @RequestParam(required = false, defaultValue = "0") int value, @RequestParam(
          required = false, defaultValue = DEFAULT_PAGE_SIZE) int size, @RequestParam(
          required = false) List<String> sort, @RequestParam(required = false) List<String> filter) {

    List<ShortAccount> saccs = new LinkedList<ShortAccount>();

    // adding default sort if necessary
    sort = sort != null ? sort : new LinkedList<String>();
    if (sort.isEmpty()) {
      sort.add("accountType::ASC");
    }

    Page<Account> pa =
        accountService.findAll((new AccountSpecsHelper()).getSpecification(filter),
            new PageRequest(value, size, (new CustomSortHandler(sort)).getSort()));

    if (pa.getContent() != null && !pa.getContent().isEmpty()) {
      for (Account acc : pa.getContent()) {
        if (!acc.isEntityDisabled()) {

          ShortAccount sacc = new ShortAccount();
          sacc.setId(acc.getId());
          sacc.setEmail(acc.getEmail());
          sacc.setFullName(acc.getFullName());
          sacc.setAccountType(acc.getAccountType());
          sacc.setUsername(acc.getUsername());

          saccs.add(sacc);
        }
      }
    }

    Pageable p = new PageRequest(pa.getNumber(), pa.getSize(), pa.getSort());
    Page<ShortAccount> sap = new PageImpl<ShortAccount>(saccs, p, pa.getTotalElements());

    return sap;
  }

  @RequestMapping(value = "/shortaccounts", method = RequestMethod.GET)
  public List<ShortAccount> getShortAccounts(@RequestParam(required = false) List<String> filter) {

    List<ShortAccount> saccs = new LinkedList<ShortAccount>();
    filter = filter != null ? filter : new LinkedList<String>();

    User authU = userService.getCurrentUser();
    if (authU != null && authU.isEnabled()) {
      if (authU.getAuthorities().contains(new SimpleGrantedAuthority("tester"))) {
        filter.clear();
      } else if (authU.getAuthorities().contains(new SimpleGrantedAuthority("supervisor"))
          || authU.getAuthorities().contains(new SimpleGrantedAuthority("admin"))) {
        // Do nothing
      } else {
        return saccs;
      }
    } else {
      return saccs;
    }

    List<Account> accs =
        accountService.findAll((new AccountSpecsHelper()).getSpecification(filter));
    if (accs != null && !accs.isEmpty()) {
      for (Account acc : accs) {
        if (!acc.isEntityDisabled()) {
          ShortAccount sacc = new ShortAccount();
          sacc.setId(acc.getId());
          sacc.setEmail(acc.getEmail());
          sacc.setFullName(acc.getFullName());
          sacc.setPending(acc.isPending());
          sacc.setEntityDisabled(acc.isEntityDisabled());
          sacc.setUsername(acc.getUsername());
          saccs.add(sacc);
        }
      }
    }

    return saccs;

  }

  @RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET)
  public Account getAccountById(@PathVariable Long id) {

    Account acc = accountService.findOne(id);

    if (acc == null || acc.isEntityDisabled()) {
      return null;
    } else {
      return acc;
    }
  }

  @PreAuthorize("hasPermission(#id, 'accessAccountBasedResource')")
  @RequestMapping(value = "/accounts/{id}", method = RequestMethod.POST)
  public ResponseMessage updateAccountById(@PathVariable Long id,
      @Valid @RequestBody Account account) {

    Account acc = accountService.findOne(id);
    if (acc == null || acc.isEntityDisabled()) {
      return new ResponseMessage(ResponseMessage.Type.danger, "badAccount", id.toString());
    } else {
      // Validation
      if (account.getEmail() == null || account.getEmail().isEmpty()) {
        return new ResponseMessage(ResponseMessage.Type.danger, "emptyEmail", account.getEmail());
      }
      if (!acc.getEmail().equalsIgnoreCase(account.getEmail())
          && accountService.findByTheAccountsEmail(account.getEmail()) != null) {
        return new ResponseMessage(ResponseMessage.Type.danger, "duplicateEmail",
            account.getEmail());
      }

      acc.setFullName(account.getFullName());
      acc.setEmail(account.getEmail());

      accountService.save(acc);

      return new ResponseMessage(ResponseMessage.Type.success, "accountUpdated", acc.getId()
          .toString());
    }
  }

  /* Other */

  @RequestMapping(value = "/sooa/emails/{email:.*}", method = RequestMethod.GET)
  public ResponseMessage accountEmailExist(@PathVariable String email, @RequestParam(
      required = false) String email1) {

    if (accountService.findByTheAccountsEmail(email) != null) {
      return new ResponseMessage(ResponseMessage.Type.success, "emailFound", email);
    } else {
      return new ResponseMessage(ResponseMessage.Type.success, "emailNotFound", email);
    }
  }

  @RequestMapping(value = "/sooa/usernames/{username}", method = RequestMethod.GET)
  public ResponseMessage accountUsernameExist(@PathVariable String username) {

    if (accountService.findByTheAccountsUsername(username) != null) {
      return new ResponseMessage(ResponseMessage.Type.success, "usernameFound", username);
    } else {
      return new ResponseMessage(ResponseMessage.Type.success, "usernameNotFound", username);
    }
  }



  /**
   * Authenticated registering agent registers a new user account accountType -> (authorizedVendor,
   * supervisor, provider, admin)
   * 
   * { \"accountType\":\"\" , \"email\":\"\" }
   * */
  @PreAuthorize("hasRole('supervisor') or hasRole('admin')")
  @RequestMapping(value = "/accounts/register", method = RequestMethod.POST)
  public ResponseMessage registerUserWhenAuthenticated(@RequestBody Account account,
      HttpServletRequest request) throws Exception {

    // validate entry
    boolean validEntry = true;
    // validEntry = userService.userExists(account.getUsername()) == true ?
    // validEntry = false : validEntry;
    validEntry =
        accountService.findByTheAccountsEmail(account.getEmail()) != null ? validEntry = false
            : validEntry;
    // validEntry =
    // accountService.findByTheAccountsUsername(account.getUsername()) !=
    // null ? validEntry = false : validEntry;

    if (!validEntry) {
      return new ResponseMessage(ResponseMessage.Type.danger, "duplicateInformation", null);
    }

    // verify account type
    if (account.getAccountType() == null || account.getAccountType().isEmpty()) {
      return new ResponseMessage(ResponseMessage.Type.danger, "accountTypeMissing", null);
    }
    boolean validAccountType = false;
    for (String acct : UserUtil.ACCOUNT_TYPE_LIST) {
      if (acct.equals(account.getAccountType())) {
        validAccountType = true;
      }
    }
    if (!validAccountType) {
      return new ResponseMessage(ResponseMessage.Type.danger, "accountTypeNotValid", null);
    }

    // generate username
    String generatedUsername = this.generateNextUsername(account.getAccountType());
    account.setUsername(generatedUsername);

    // generate password
    String generatedPassword = RandomPasswordGenerator.generatePassword(10, 13, 4, 4, 2).toString();

    // create new user
    try {
      userService.createUserWithAuthorities(generatedUsername, generatedPassword,
          "user," + account.getAccountType());
      User user = userService.retrieveUserByUsername(generatedUsername);
    } catch (Exception e) {
      return new ResponseMessage(ResponseMessage.Type.danger, "errorWithUser", null);
    }

    // create account
    try {
      // Make sure only desired data gets persisted
      Account registeredAccount = new Account();
      registeredAccount.setUsername(generatedUsername);
      registeredAccount.setAccountType(account.getAccountType());
      registeredAccount.setEmail(account.getEmail());
      registeredAccount.setPending(false);
      registeredAccount.setGuestAccount(false);
      accountService.save(registeredAccount);
    } catch (Exception e) {
      return new ResponseMessage(ResponseMessage.Type.danger, "errorWithAccount", null);
    }

    // start password reset process (for registration)
    // Create reset token. First get accountPasswordReset element from the
    // repository. If null create it.
    AccountPasswordReset arp =
        accountPasswordResetService.findByTheAccountsUsername(account.getUsername());
    if (arp == null) {
      arp = new AccountPasswordReset();
      arp.setUsername(account.getUsername());
    }

    arp.setCurrentToken(arp.getNewToken());
    arp.setTimestamp(new Date());
    arp.setNumberOfReset(arp.getNumberOfReset() + 1);

    accountPasswordResetService.save(arp);

    // String port = "";
    // if (SERVER_PORT != null && !SERVER_PORT.isEmpty()) {
    // port = ":" + SERVER_PORT;
    // }

    // Generate url and email

    String url =
        getUrl(request) + "/#/registerResetPassword?userId=" + account.getUsername() + "&username="
            + account.getUsername() + "&token="
            + UriUtils.encodeQueryParam(arp.getCurrentToken(), "UTF-8");

    // generate and send email
    this.sendAccountRegistrationPasswordResetNotification(account, url);

    return new ResponseMessage(ResponseMessage.Type.success, "userAdded", account.getUsername());

  }

  @PreAuthorize("hasRole('supervisor') or hasRole('admin')")
  @RequestMapping(value = "/accounts/{accountId}/resendregistrationinvite",
      method = RequestMethod.POST)
  public ResponseMessage resendRegistrationWhenAuthenticated(@PathVariable Long accountId,
      HttpServletRequest request) throws Exception {

    // get account
    Account acc = accountService.findOne(accountId);

    if (acc == null || acc.isEntityDisabled()) {
      return new ResponseMessage(ResponseMessage.Type.danger, "badAccount", accountId.toString());
    }

    // verify account is pending
    if (!acc.isPending()) {
      return new ResponseMessage(ResponseMessage.Type.danger, "accountIsNotPending",
          accountId.toString());
    }

    // generate a new token
    AccountPasswordReset arp =
        accountPasswordResetService.findByTheAccountsUsername(acc.getUsername());
    if (arp == null) {
      arp = new AccountPasswordReset();
      arp.setUsername(acc.getUsername());
    }

    arp.setCurrentToken(arp.getNewToken());
    arp.setTimestamp(new Date());
    arp.setNumberOfReset(arp.getNumberOfReset() + 1);

    accountPasswordResetService.save(arp);

    // // generate url
    // String port = "";
    // if (SERVER_PORT != null && !SERVER_PORT.isEmpty()) {
    // port = ":" + SERVER_PORT;
    // }

    String url =
        getUrl(request) + "/#/registerResetPassword?userId=" + acc.getUsername() + "&"
            + "username=" + acc.getUsername() + "&" + "token="
            + UriUtils.encodeQueryParam(arp.getCurrentToken(), "UTF-8");

    // generate and send email
    this.sendAccountRegistrationPasswordResetNotification(acc, url);

    return new ResponseMessage(ResponseMessage.Type.success, "resentRegistrationInvite",
        acc.getUsername());
  }

  @PreAuthorize("hasRole('admin')")
  @RequestMapping(value = "/accounts/{accountId}/approveaccount", method = RequestMethod.POST)
  public ResponseMessage approveAccount(@PathVariable Long accountId, HttpServletRequest request)
      throws Exception {

    // get account
    Account acc = accountService.findOne(accountId);

    if (acc == null || acc.isEntityDisabled()) {
      return new ResponseMessage(ResponseMessage.Type.danger, "badAccount", accountId.toString(),
          true);
    }

    acc.setPending(false);
    accountService.save(acc);

    // generate and send email
    this.sendAccountApproveNotification(acc);

    return new ResponseMessage(ResponseMessage.Type.success, "accountApproved", acc.getUsername(),
        true);
  }

  @PreAuthorize("hasRole('admin')")
  @RequestMapping(value = "/accounts/{accountId}/suspendaccount", method = RequestMethod.POST)
  public ResponseMessage suspendAccount(@PathVariable Long accountId, HttpServletRequest request)
      throws Exception {
    // get account
    Account acc = accountService.findOne(accountId);

    if (acc == null || acc.isEntityDisabled()) {
      return new ResponseMessage(ResponseMessage.Type.danger, "badAccount", accountId.toString(),
          true);
    }

    acc.setPending(true);
    accountService.save(acc);
    // generate and send email
    return new ResponseMessage(ResponseMessage.Type.success, "accountSuspended", acc.getUsername(),
        true);
  }

  /**
   * Unauthenticated user registers himself accountType -> (provider)
   * 
   * {\"username\":\"\" , \"password\":\"\" , \"firstname\":\"\" , \"lastname\":\"\" ,
   * \"email\":\"\" , \"company\":\"\" , \"signedConfidentialAgreement\":\"\" }
   * */
  @RequestMapping(value = "/sooa/accounts/register", method = RequestMethod.POST)
  public ResponseMessage registerUserWhenNotAuthenticated(@RequestBody Account account) {

    // validate entry
    boolean validEntry = true;
    validEntry =
        userService.userExists(account.getUsername()) == true ? validEntry = false : validEntry;
    validEntry =
        accountService.findByTheAccountsEmail(account.getEmail()) != null ? validEntry = false
            : validEntry;
    validEntry =
        accountService.findByTheAccountsUsername(account.getUsername()) != null ? validEntry =
            false : validEntry;

    if (!validEntry) {
      return new ResponseMessage(ResponseMessage.Type.danger, "duplicateInformation", null);
    }

    Set<String> authAccT = StringUtils.commaDelimitedListToSet(AUTHORIZED_ACCOUNT_TYPE_UNAUTH_REG);

    if (account.getAccountType() == null || !authAccT.contains(account.getAccountType())) {
      return new ResponseMessage(ResponseMessage.Type.danger, "accountTypeNotValid", null);
    }

    // create new user with provider role
    try {
      userService.createUserWithAuthorities(account.getUsername(), account.getPassword(), "user,"
          + account.getAccountType());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseMessage(ResponseMessage.Type.danger, "errorWithUser", null);
    }

    // create account
    Account registeredAccount = new Account();
    try {
      // Make sure only desired data gets persisted
      registeredAccount.setPending(false);
      registeredAccount.setUsername(account.getUsername());
      registeredAccount.setAccountType(account.getAccountType());
      registeredAccount.setFullName(account.getFullName());
      registeredAccount.setEmail(account.getEmail());
      registeredAccount.setSignedConfidentialityAgreement(account
          .getSignedConfidentialityAgreement());
      registeredAccount.setGuestAccount(false);

      accountService.save(registeredAccount);
    } catch (Exception e) {
      userService.deleteUser(account.getUsername());
      logger.error(e.getMessage(), e);
      return new ResponseMessage(ResponseMessage.Type.danger, "errorWithAccount", null);
    }

    // generate and send email
    this.sendRegistrationNotificationToAdmin(account);
    this.sendApplicationConfirmationNotification(account);

    return new ResponseMessage(ResponseMessage.Type.success, "userAdded", registeredAccount.getId()
        .toString(), "true");
  }

  /**
   * User forgot his password and requests a password reset
   * */
  @RequestMapping(value = "/sooa/accounts/passwordreset", method = RequestMethod.POST)
  public ResponseMessage requestAccountPasswordReset(
      @RequestParam(required = false) String username, HttpServletRequest request) throws Exception {

    Account acc = null;

    if (username != null) {
      acc = accountService.findByTheAccountsUsername(username);
      if (acc == null) {
        acc = accountService.findByTheAccountsEmail(username);
      }
    } else {
      return new ResponseMessage(ResponseMessage.Type.danger, "noUsernameOrEmail", null);
    }

    if (acc == null) {
      return new ResponseMessage(ResponseMessage.Type.danger, "wrongUsernameOrEmail", null);
    }

    User user = userService.retrieveUserByUsername(acc.getUsername());

    // start password reset process (for reset)
    // Create reset token. First get accountPasswordReset element from the
    // repository. If null create it.
    AccountPasswordReset arp =
        accountPasswordResetService.findByTheAccountsUsername(acc.getUsername());
    if (arp == null) {
      arp = new AccountPasswordReset();
      arp.setUsername(acc.getUsername());
    }

    arp.setCurrentToken(arp.getNewToken());
    arp.setTimestamp(new Date());
    arp.setNumberOfReset(arp.getNumberOfReset() + 1);

    accountPasswordResetService.save(arp);
    //
    // String port = "";
    // if (SERVER_PORT != null && !SERVER_PORT.isEmpty()) {
    // port = ":" + SERVER_PORT;
    // }

    // Generate url and email
    String url =
        getUrl(request) + "/#/resetPassword?userId=" + user.getUsername() + "&username="
            + acc.getUsername() + "&token="
            + UriUtils.encodeQueryParam(arp.getCurrentToken(), "UTF-8");

    // System.out.println("****************** "+url+" *******************");

    // generate and send email
    this.sendAccountPasswordResetRequestNotification(acc, url);

    return new ResponseMessage(ResponseMessage.Type.success, "resetRequestProcessed", acc.getId()
        .toString(), true);
  }

  /**
   * User wants to change his password when already logged in
   * */
  @PreAuthorize("hasPermission(#accountId, 'accessAccountBasedResource')")
  @RequestMapping(value = "/accounts/{accountId}/passwordchange", method = RequestMethod.POST)
  public ResponseMessage changeAccountPassword(@RequestBody AccountChangeCredentials acc,
      @PathVariable Long accountId) {

    // check there is a username in the request
    if (acc.getUsername() == null || acc.getUsername().isEmpty()) {
      return new ResponseMessage(ResponseMessage.Type.danger, "usernameMissing", null);
    }

    if (acc.getNewPassword() == null || acc.getNewPassword().length() < 4) {
      return new ResponseMessage(ResponseMessage.Type.danger, "invalidPassword", null);
    }

    Account onRecordAccount = accountService.findOne(accountId);
    if (!onRecordAccount.getUsername().equals(acc.getUsername())) {
      return new ResponseMessage(ResponseMessage.Type.danger, "invalidUsername", null);
    }

    userService.changePasswordForPrincipal(acc.getPassword(), acc.getNewPassword());

    // send email notification
    this.sendChangeAccountPasswordNotification(onRecordAccount);

    return new ResponseMessage(ResponseMessage.Type.success, "accountPasswordReset",
        onRecordAccount.getId().toString(), true);
  }

  /**
   * Admin wants to change user password
   * */
  @PreAuthorize("hasRole('admin')")
  @RequestMapping(value = "/accounts/{accountId}/userpasswordchange", method = RequestMethod.POST)
  public ResponseMessage adminChangeAccountPassword(@RequestBody AccountChangeCredentials acc,
      @PathVariable Long accountId) {
    String newPassword = acc.getNewPassword();
    // check there is a username in the request
    if (acc.getUsername() == null || acc.getUsername().isEmpty()) {
      return new ResponseMessage(ResponseMessage.Type.danger, "usernameMissing", null);
    }

    if (acc.getNewPassword() == null || acc.getNewPassword().length() < 4) {
      return new ResponseMessage(ResponseMessage.Type.danger, "invalidPassword", null);
    }

    Account onRecordAccount = accountService.findOne(accountId);
    if (!onRecordAccount.getUsername().equals(acc.getUsername())) {
      return new ResponseMessage(ResponseMessage.Type.danger, "invalidUsername", null);
    }

    userService.changePasswordForUser(acc.getUsername(), acc.getNewPassword());

    // send email notification
    this.sendChangeAccountPasswordNotification(onRecordAccount, newPassword);

    return new ResponseMessage(ResponseMessage.Type.success, "accountPasswordReset",
        onRecordAccount.getId().toString(), true);
  }

  /**
   * User has to change his password and accept the agreement to complete the registration process
   * */
  @RequestMapping(value = "/sooa/accounts/{userId}/passwordreset", method = RequestMethod.POST,
      params = "token")
  public ResponseMessage resetAccountPassword(@RequestBody Account acc,
      @PathVariable String userId, @RequestParam(required = true) String token) {

    // logger.debug("^^^^^^^^^^^^^^^^^^^^^ -5 ^^^^^^^^^^^^^^^^^^");

    // check there is a username in the request
    if (acc.getUsername() == null || acc.getUsername().isEmpty()) {
      return new ResponseMessage(ResponseMessage.Type.danger, "usernameMissing", null);
    }

    // logger.debug("^^^^^^^^^^^^^^^^^^^^^ -4 ^^^^^^^^^^^^^^^^^^");

    AccountPasswordReset apr =
        accountPasswordResetService.findByTheAccountsUsername(acc.getUsername());

    // logger.debug("^^^^^^^^^^^^^^^^^^^^^ -3 ^^^^^^^^^^^^^^^^^^");

    // check there is a reset request on record
    if (apr == null) {
      return new ResponseMessage(ResponseMessage.Type.danger, "noResetRequestFound", null);
    }

    // logger.debug("^^^^^^^^^^^^^^^^^^^^^ -2 ^^^^^^^^^^^^^^^^^^");

    // check that for username, the token in record is the token passed in
    // request
    if (!apr.getCurrentToken().equals(token)) {
      return new ResponseMessage(ResponseMessage.Type.danger, "incorrectToken", null);
    }

    // logger.debug("^^^^^^^^^^^^^^^^^^^^^ -1 ^^^^^^^^^^^^^^^^^^");

    // check token is not expired
    if (apr.isTokenExpired()) {
      return new ResponseMessage(ResponseMessage.Type.danger, "expiredToken", null);
    }

    // logger.debug("^^^^^^^^^^^^^^^^^^^^^ 0 ^^^^^^^^^^^^^^^^^^");

    User onRecordUser = userService.retrieveUserByUsername(userId);

    // logger.debug("^^^^^^^^^^^^^^^^^^^^^ "+onRecordUser.getPassword()+" ^^^^^^^^^^^^^^^^^^");

    // logger.debug("^^^^^^^^^^^^^^^^^^^^^ 1 ^^^^^^^^^^^^^^^^^^");

    Account onRecordAccount = accountService.findByTheAccountsUsername(acc.getUsername());

    // logger.debug("^^^^^^^^^^^^^^^^^^^^^ 2 ^^^^^^^^^^^^^^^^^^");

    userService.changePasswordForUser(onRecordUser.getPassword(), acc.getPassword(), userId);
    if (!onRecordUser.isCredentialsNonExpired()) {
      userService.enableUserCredentials(userId);
    }
    // logger.debug("^^^^^^^^^^^^^^^^^^^^^ 3 ^^^^^^^^^^^^^^^^^^");

    // send email notification
    this.sendResetAccountPasswordNotification(onRecordAccount);

    return new ResponseMessage(ResponseMessage.Type.success, "accountPasswordReset",
        onRecordAccount.getId().toString());
  }

  /**
   * 
   * */
  @RequestMapping(value = "/sooa/accounts/register/{userId}/passwordreset",
      method = RequestMethod.POST, params = "token")
  public ResponseMessage resetRegisteredAccountPassword(@RequestBody AccountChangeCredentials racc,
      @PathVariable String userId, @RequestParam(required = true) String token) {

    // check there is a username in the request
    if (racc.getUsername() == null || racc.getUsername().isEmpty()) {
      return new ResponseMessage(ResponseMessage.Type.danger, "usernameMissing", null);
    }

    // logger.debug("^^^^^^^^^^^^^^^^^^^^^ 0 ^^^^^^^^^^^^^^^^^^");

    AccountPasswordReset apr =
        accountPasswordResetService.findByTheAccountsUsername(racc.getUsername());

    // logger.debug("^^^^^^^^^^^^^^^^^^^^^ 1 ^^^^^^^^^^^^^^^^^^");

    // check there is a reset request on record
    if (apr == null) {
      return new ResponseMessage(ResponseMessage.Type.danger, "noResetRequestFound", null);
    }

    // logger.debug("^^^^^^^^^^^^^^^^^^^^^ 2 ^^^^^^^^^^^^^^^^^^");

    // check that for username, the token in record is the token passed in
    // request
    if (!apr.getCurrentToken().equals(token)) {
      return new ResponseMessage(ResponseMessage.Type.danger, "incorrectToken", null);
    }

    // logger.debug("^^^^^^^^^^^^^^^^^^^^^ 3 ^^^^^^^^^^^^^^^^^^");

    // check token is not expired
    if (apr.isTokenExpired()) {
      return new ResponseMessage(ResponseMessage.Type.danger, "expiredToken", null);
    }

    // logger.debug("^^^^^^^^^^^^^^^^^^^^^ 4 "+userId+" ^^^^^^^^^^^^^^^^^^");

    User onRecordUser = userService.retrieveUserByUsername(userId);
    // logger.debug("^^^^^^^^^^^^^^^^^^^^^ "+onRecordUser.getPassword()+" ^^^^^^^^^^^^^^^^^^");

    // logger.debug("^^^^^^^^^^^^^^^^^^^^^ 5 ^^^^^^^^^^^^^^^^^^");

    Account onRecordAccount = accountService.findByTheAccountsUsername(racc.getUsername());

    // logger.debug("^^^^^^^^^^^^^^^^^^^^^ 6 ^^^^^^^^^^^^^^^^^^");

    // change the password
    userService.changePasswordForUser(onRecordUser.getPassword(), racc.getPassword(), userId);
    if (!onRecordUser.isCredentialsNonExpired()) {
      userService.enableUserCredentials(userId);
    }
    // logger.debug("^^^^^^^^^^^^^^^^^^^^^ 7 ^^^^^^^^^^^^^^^^^^");

    // update the agreement
    onRecordAccount.setSignedConfidentialityAgreement(racc.getSignedConfidentialityAgreement());
    onRecordAccount.setPending(false);
    onRecordAccount.setGuestAccount(false);

    accountService.save(onRecordAccount);

    // logger.debug("^^^^^^^^^^^^^^^^^^^^^ 8 ^^^^^^^^^^^^^^^^^^");
    Long expireTokenTime = (new Date()).getTime() - AccountPasswordReset.tokenValidityTimeInMilis;
    Date expireTokenDate = new Date();
    expireTokenDate.setTime(expireTokenTime);
    apr.setTimestamp(expireTokenDate);
    accountPasswordResetService.save(apr);

    // send email notification
    this.sendResetRegistrationAccountPasswordNotification(onRecordAccount);

    return new ResponseMessage(ResponseMessage.Type.success, "registeredAccountPasswordReset",
        onRecordAccount.getId().toString());
  }

  /**
   * 
   * */
  @RequestMapping(value = "/sooa/accounts/forgottenusername", method = RequestMethod.GET)
  public ResponseMessage retrieveForgottenUsername(@RequestParam String email) {

    if (email == null || email.isEmpty()) {
      return new ResponseMessage(ResponseMessage.Type.danger, "badEmail", email);
    }

    Account acc = accountService.findByTheAccountsEmail(email);
    if (acc == null) {
      return new ResponseMessage(ResponseMessage.Type.danger, "noEmailRecords", email);
    }

    // send email with username
    this.sendRetrieveForgottenUsernameNotification(acc);

    return new ResponseMessage(ResponseMessage.Type.success, "usernameFound", email);

  }

  /**
   * 
   * */
  @PreAuthorize("hasPermission(#id, 'accessAccountBasedResource')")
  @RequestMapping(value = "/accounts/{id}", method = RequestMethod.DELETE)
  public ResponseMessage deleteAccountById(@PathVariable Long id) {

    Account acc = accountService.findOne(id);

    if (acc == null || acc.isEntityDisabled()) {
      return new ResponseMessage(ResponseMessage.Type.danger, "badAccount", id.toString());
    } else {
      User u = userService.retrieveUserByUsername(acc.getUsername());
      if (u == null || !u.isEnabled()) {
        return new ResponseMessage(ResponseMessage.Type.danger, "badAccount", id.toString());
      } else {
        logger.debug("^^^^^^^^^^^^^^^^ about to disable user " + acc.getUsername()
            + " ^^^^^^^^^^^^^^^^^");
        userService.disableUser(acc.getUsername());
        acc.setEntityDisabled(true);
        logger.debug("^^^^^^^^^^^^^^^^ about to save ^^^^^^^^^^^^^^^^^");
        accountService.save(acc);
        logger.debug("^^^^^^^^^^^^^^^^ saved ^^^^^^^^^^^^^^^^^");
        return new ResponseMessage(ResponseMessage.Type.success, "deletedAccount", id.toString(),
            true);
      }
    }
  }

  /**
   * User wants to log in
   * */
  // @PreAuthorize("hasRole('tester') or hasRole('admin')")
  @RequestMapping(value = "/accounts/login", method = RequestMethod.GET)
  public ResponseMessage doNothing(HttpSession session) {
    User u = userService.getCurrentUser();
    Account a = accountService.findByTheAccountsUsername(u.getUsername());
    Long guestId = SessionContext.getCurrentUserId(session);
    if (guestId != null) {
      accountService.reconcileAccounts(guestId, a.getId());
    }
    SessionContext.setCurrentUserId(session, a.getId());
    return new ResponseMessage(ResponseMessage.Type.success, "loginSuccess", "succes");
  }

  /**
   * 
   * */
  @RequestMapping(value = "/accounts/cuser", method = RequestMethod.GET)
  public CurrentUser getCUser() {
    User u = userService.getCurrentUser();
    CurrentUser cu = null;
    if (u != null && u.isEnabled()) {
      Account a = accountService.findByTheAccountsUsername(u.getUsername());
      if (!a.isPending()) {
        cu = new CurrentUser();
        cu.setUsername(u.getUsername());
        cu.setAccountId(a.getId());
        cu.setAuthenticated(true);
        cu.setAuthorities(u.getAuthorities());
        cu.setPending(a.isPending());
        cu.setFullName(a.getFullName());
        cu.setGuestAccount(false);
      }
    }
    return cu;
  }

  private void sendApplicationConfirmationNotification(Account acc) {
    SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
    msg.setSubject("NIST  Application Received");
    msg.setTo(acc.getEmail());
    msg.setText("Dear " + acc.getUsername() + " \n\n"
        + "Thank you for submitting an application for use of the " + TOOL_NAME + "\n\n"
        + "Please refer to the user guide for the detailed steps. " + "\n\n" + "Sincerely, "
        + "\n\n" + "The " + TOOL_NAME + " Team" + "\n\n" + "P.S: If you need help, contact us at '"
        + ADMIN_EMAIL + "'");
    try {
      this.mailSender.send(msg);
    } catch (MailException ex) {
      logger.error(ex.getMessage(), ex);
    }
  }

  private void sendAccountRegistrationNotification(Account acc) {
    SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);

    msg.setSubject("Welcome! You are successfully registered on " + TOOL_NAME + "");
    msg.setTo(acc.getEmail());
    msg.setText("Dear " + acc.getUsername() + " \n\n" + "You've successfully registered on the "
        + TOOL_NAME + " Site." + " \n" + "Your username is: " + acc.getUsername() + " \n\n"
        + "Please refer to the user guide for the detailed steps. " + "\n\n" + "Sincerely, "
        + "\n\n" + "The " + TOOL_NAME + " Team" + "\n\n" + "P.S: If you need help, contact us at '"
        + ADMIN_EMAIL + "'");

    try {
      this.mailSender.send(msg);
    } catch (MailException ex) {
      logger.error(ex.getMessage(), ex);
    }
  }

  private void sendRegistrationNotificationToAdmin(Account acc) {
    SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
    msg.setSubject("New Registration Application on IGAMT");
    msg.setTo(ADMIN_EMAIL);
    msg.setText("Hello Admin,  \n A new application has been submitted and is waiting for approval. The user information are as follow: \n\n"
        + "Name: "
        + acc.getFullName()
        + "\n"
        + "Email: "
        + acc.getEmail()
        + "\n"
        + "Username: "
        + acc.getUsername()
        + "\n"
        + " \n\n"
        + "Sincerely, "
        + "\n\n"
        + "The "
        + TOOL_NAME
        + " Team" + "\n\n");
    try {
      this.mailSender.send(msg);
    } catch (MailException ex) {
      logger.error(ex.getMessage(), ex);
    }
  }

  private void sendAccountApproveNotification(Account acc) {
    SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);

    msg.setTo(acc.getEmail());
    msg.setSubject("" + TOOL_NAME + " Account Approval Notification ");
    msg.setText("Dear " + acc.getUsername() + " \n\n"
        + "**** If you have not requested a new account, please disregard this email **** \n\n\n"
        + "Your account has been approved and you can proceed " + "to login .\n" + "\n\n"
        + "Sincerely, " + "\n\n" + "The " + TOOL_NAME + " Team" + "\n\n"
        + "P.S: If you need help, contact us at '" + ADMIN_EMAIL + "'");
    try {
      this.mailSender.send(msg);
    } catch (MailException ex) {
      logger.error(ex.getMessage(), ex);
    }
  }

  private void sendAccountRegistrationPasswordResetNotification(Account acc, String url) {
    SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);

    msg.setTo(acc.getEmail());
    msg.setSubject("" + TOOL_NAME + " Registration Notification ");
    msg.setText("Dear " + acc.getUsername() + " \n\n"
        + "**** If you have not requested a new account, please disregard this email **** \n\n\n"
        + "Your account request has been processed and you can proceed " + "to login .\n"
        + "You need to change your password in order to login.\n"
        + "Copy and paste the following url to your browser to initiate the password change:\n"
        + url + " \n\n" + "Please refer to the user guide for the detailed steps. " + "\n\n"
        + "Sincerely, " + "\n\n" + "The " + TOOL_NAME + " Team" + "\n\n"
        + "P.S: If you need help, contact us at '" + ADMIN_EMAIL + "'");

    try {
      this.mailSender.send(msg);
    } catch (MailException ex) {
      logger.error(ex.getMessage(), ex);
    }
  }

  private void sendAccountPasswordResetRequestNotification(Account acc, String url) {
    SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);

    msg.setTo(acc.getEmail());
    msg.setSubject("" + TOOL_NAME + " Password Reset Request Notification");
    msg.setText("Dear "
        + acc.getUsername()
        + " \n\n"
        + "**** If you have not requested a password reset, please disregard this email **** \n\n\n"
        + "You password reset request has been processed.\n"
        + "Copy and paste the following url to your browser to initiate the password change:\n"
        + url + " \n\n" + "Sincerely, " + "\n\n" + "The IGAMT Team" + "\n\n"
        + "P.S: If you need help, contact us at '" + ADMIN_EMAIL + "'");

    try {
      this.mailSender.send(msg);
    } catch (MailException ex) {
      logger.error(ex.getMessage(), ex);
    }
  }

  private void sendChangeAccountPasswordNotification(Account acc) {
    SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);

    msg.setTo(acc.getEmail());
    msg.setSubject("" + TOOL_NAME + " Password Change Notification");
    msg.setText("Dear " + acc.getUsername() + " \n\n"
        + "Your password has been successfully changed." + " \n\n" + "Sincerely,\n\n" + "The "
        + TOOL_NAME + " Team");

    try {
      this.mailSender.send(msg);
    } catch (MailException ex) {
      logger.error(ex.getMessage(), ex);
    }
  }

  private void sendChangeAccountPasswordNotification(Account acc, String newPassword) {
    SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
    msg.setTo(acc.getEmail());
    msg.setSubject("" + TOOL_NAME + " Password Change Notification");
    msg.setText("Dear " + acc.getUsername() + " \n\n"
        + "Your password has been successfully changed." + " \n\n"
        + "Your new temporary password is ." + newPassword + " \n\n"
        + "Please update your password once logged in. \n\n" + "Sincerely,\n\n" + "The "
        + TOOL_NAME + " Team");

    try {
      this.mailSender.send(msg);
    } catch (MailException ex) {
      logger.error(ex.getMessage(), ex);
    }
  }

  private void sendResetAccountPasswordNotification(Account acc) {
    SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);

    msg.setTo(acc.getEmail());
    msg.setSubject("" + TOOL_NAME + " Password Rest Notification");
    msg.setText("Dear " + acc.getUsername() + " \n\n"
        + "Your password has been successfully reset." + " \n" + "Your username is: "
        + acc.getUsername() + " \n\n" + "Sincerely,\n\n" + "The " + TOOL_NAME + " Team");

    try {
      this.mailSender.send(msg);
    } catch (MailException ex) {
      logger.error(ex.getMessage(), ex);
    }
  }

  private void sendResetRegistrationAccountPasswordNotification(Account acc) {
    SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);

    msg.setTo(acc.getEmail());
    msg.setSubject("" + TOOL_NAME + " Registration and Password Notification");
    msg.setText("Dear " + acc.getUsername() + " \n\n" + "Your password has been successfully set."
        + " \n" + "Your username is: " + acc.getUsername() + " \n" + "Your registration with the "
        + TOOL_NAME + " is complete." + " \n\n" + "Sincerely,\n\n" + "The " + TOOL_NAME + " Team");

    try {
      this.mailSender.send(msg);
    } catch (MailException ex) {
      logger.error(ex.getMessage(), ex);
    }
  }

  private void sendRetrieveForgottenUsernameNotification(Account acc) {
    SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);

    msg.setTo(acc.getEmail());
    msg.setSubject("" + TOOL_NAME + " Username Notification");
    msg.setText("Dear " + acc.getUsername() + " \n\n" + "Your username is: " + acc.getUsername()
        + " \n\n" + "Sincerely,\n\n" + "The " + TOOL_NAME + " Team");

    try {
      this.mailSender.send(msg);
    } catch (MailException ex) {
      logger.error(ex.getMessage(), ex);
    }
  }

  private String generateNextUsername(String accountType) throws Exception {

    StringBuffer result = new StringBuffer();

    if (accountType.equals("provider")) {
      result.append("P-");
    } else if (accountType.equals("authorizedVendor")) {
      result.append("AV-");
    } else if (accountType.equals("supervisor")) {
      result.append("S-");
    } else if (accountType.equals("admin")) {
      result.append("A-");
    } else {
      result.append("U-");
    }

    int MAX_RETRY = 10;
    int retry = 0;
    while (userService.userExists(result.append(UserUtil.generateRandom()).toString())) {
      if (retry == MAX_RETRY) {
        throw new Exception("Can't generate username");
      }
      result.append(UserUtil.generateRandom()).toString();
      retry++;
    }

    return result.toString();
  }

  private String getUrl(HttpServletRequest request) {
    String scheme = request.getScheme();
    String host = request.getHeader("Host");
    return scheme + "://" + host + "/" + request.getContextPath();
  }


  @RequestMapping(value = "/accounts/guest", method = RequestMethod.POST)
  public CurrentUser currentGuest(HttpSession session) {
    CurrentUser cu = null;
    Long id = SessionContext.getCurrentUserId(session);
    if (id != null) {
      Account account = accountService.findOne(id);
      if (account != null) {
        cu = new CurrentUser();
        cu.setAccountId(account.getId());
        cu.setAuthenticated(false);
        cu.setGuestAccount(true);
      }
    }
    return cu;
  }

  @RequestMapping(value = "/accounts/guest/createIfNotExist", method = RequestMethod.POST)
  public CurrentUser createGuest(HttpSession session) {
    CurrentUser cu = null;
    Account account = null;
    Long id = SessionContext.getCurrentUserId(session);
    if (id != null) {
      account = accountService.findOne(id);
    } else {
      account = new Account();
      account.setGuestAccount(true);
      accountService.save(account);
    }
    SessionContext.setCurrentUserId(session, account.getId());
    if (account != null) {
      cu = new CurrentUser();
      cu.setAccountId(account.getId());
      cu.setAuthenticated(false);
      cu.setGuestAccount(true);
    }
    return cu;
  }


  // @RequestMapping(value = "/accounts/guest/delete", method = RequestMethod.POST)
  // public boolean delete(HttpSession session) {
  // Long id = SessionContext.getCurrentUserId(session);
  // if (id != null) {
  // Account user = accountService.findOne(id);
  // if (user.isGuestAccount()) {
  // accountService.delete(user);
  // }
  // }
  // SessionContext.setCurrentUserId(session, null);
  // return true;
  // }

}
