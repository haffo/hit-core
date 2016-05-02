package gov.nist.hit.core.service;

import gov.nist.auth.hit.core.domain.Account;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface AccountService extends JpaSpecificationExecutor<Account> {

  boolean exitBySutInitiatorPropertiesAndProtocol(Map<String, String> criteria, String protocol);

  void delete(Account user);

  void delete(Long id);

  Account save(Account user);

  Account findOne(Long id);

  List<Account> findAll();

  void reconcileAccounts(Long oldAccountId, Long newAccountId);

  Account findByTheAccountsUsername(String username);

  Account findByTheAccountsEmail(String email);

  List<Account> findByTheAccountsAccountType(String accountType);


}
