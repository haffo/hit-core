package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.account.Account;

import java.util.List;
import java.util.Map;


public interface AccountService {

  boolean exitBySutInitiatorPropertiesAndProtocol(Map<String, String> criteria, String protocol);

  void delete(Account user);

  void delete(Long id);

  Account save(Account user);

  Account findOne(Long id);

  List<Account> findAll();

}
