package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.domain.TestingType;
import gov.nist.hit.core.domain.Transaction;
import gov.nist.hit.core.domain.TransportConfig;
import gov.nist.hit.core.domain.account.Account;
import gov.nist.hit.core.repo.AccountRepository;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.TestStepValidationReportService;
import gov.nist.hit.core.service.TransactionService;
import gov.nist.hit.core.service.TransportConfigService;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "accountService")
public class AccountServiceImpl implements AccountService {

  @Autowired
  protected AccountRepository accountRepository;

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
  public void delete(Account user) {
    accountRepository.delete(user);
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

      accountRepository.delete(id);
    }
  }

  @Override
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



}
