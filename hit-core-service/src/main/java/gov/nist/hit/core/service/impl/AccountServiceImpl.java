package gov.nist.hit.core.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import gov.nist.auth.hit.core.domain.Account;
import gov.nist.auth.hit.core.repo.AccountRepository;
import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.domain.TestingType;
import gov.nist.hit.core.domain.Transaction;
import gov.nist.hit.core.domain.TransportConfig;
import gov.nist.hit.core.repo.TestCaseValidationReportRepository;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.TestStepValidationReportService;
import gov.nist.hit.core.service.TransactionService;
import gov.nist.hit.core.service.TransportConfigService;

@Service(value = "accountService")
@org.springframework.transaction.annotation.Transactional("authTransactionManager")
public class AccountServiceImpl implements AccountService {
  @Autowired
  protected AccountRepository accountRepository;

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private TestCaseValidationReportRepository testCaseValidationReportRepository;


  @Autowired
  protected TransportConfigService transportConfigService;

  @Autowired
  protected TestStepValidationReportService validationReportService;

  @Override
  public boolean exitBySutInitiatorPropertiesAndProtocol(Map<String, String> criteria,
      String protocol) {
    TransportConfig config = transportConfigService.findOneByPropertiesAndProtocol(criteria,
        TestingType.SUT_INITIATOR, protocol);
    return config != null && config.getUserId() != null;
  }

  @Override
  @org.springframework.transaction.annotation.Transactional("authTransactionManager")
  public void delete(Account user) {
    accountRepository.delete(user);
  }

  @Override
  @org.springframework.transaction.annotation.Transactional("authTransactionManager")
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

      accountRepository.delete(id);
    }
  }

  @Override
  @org.springframework.transaction.annotation.Transactional("authTransactionManager")
  // @Transactional(propagation = Propagation.REQUIRED)
  public Account save(Account user) {
    return accountRepository.saveAndFlush(user);
  }

  @Override
  public Account findOne(Long id) {
    return accountRepository.findOne(id);
  }

  @Override
  public List<Account> findAll() {
    // TODO Auto-generated method stub
    return accountRepository.findAll();
  }

  @Override
  @org.springframework.transaction.annotation.Transactional("transactionManager")
  public void reconcileAccounts(Long oldAccountId, Long newAccountId) {

    List<TransportConfig> configs = transportConfigService.findAllByUser(oldAccountId);
    if (configs != null) {
      transportConfigService.delete(configs);
    }
    List<Transaction> transactions = transactionService.findAllByUser(oldAccountId);
    if (transactions != null) {
      transactionService.delete(transactions);
    }
    List<TestStepValidationReport> reports =
        testCaseValidationReportRepository.findAllByUser(oldAccountId);
    if (reports != null && !reports.isEmpty()) {
      Account newAccount = this.findOne(newAccountId);
      for (TestStepValidationReport report : reports) {
        report.setUserId(newAccount.getId());
      }
      testCaseValidationReportRepository.save(reports);
    }
  }

  @Override
  public Account findByTheAccountsUsername(String username) {
    // TODO Auto-generated method stub
    return accountRepository.findByTheAccountsUsername(username);
  }

  @Override
  public Account findOne(Specification<Account> spec) {
    // TODO Auto-generated method stub
    return accountRepository.findOne(spec);
  }

  @Override
  public List<Account> findAll(Specification<Account> spec) {
    // TODO Auto-generated method stub
    return accountRepository.findAll(spec);
  }

  @Override
  public Page<Account> findAll(Specification<Account> spec, Pageable pageable) {
    // TODO Auto-generated method stub
    return accountRepository.findAll(spec, pageable);
  }

  @Override
  public List<Account> findAll(Specification<Account> spec, Sort sort) {
    // TODO Auto-generated method stub
    return accountRepository.findAll(spec, sort);
  }

  @Override
  public long count(Specification<Account> spec) {
    // TODO Auto-generated method stub
    return accountRepository.count(spec);
  }

  @Override
  public Account findByTheAccountsEmail(String email) {
    // TODO Auto-generated method stub
    return accountRepository.findByTheAccountsEmail(email);
  }

  @Override
  public List<Account> findByTheAccountsAccountType(String accountType) {
    // TODO Auto-generated method stub
    return accountRepository.findByTheAccountsAccountType(accountType);
  }



}
