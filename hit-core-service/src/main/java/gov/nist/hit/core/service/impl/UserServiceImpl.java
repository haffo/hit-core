package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.TestingType;
import gov.nist.hit.core.domain.Transaction;
import gov.nist.hit.core.domain.TransportConfig;
import gov.nist.hit.core.domain.User;
import gov.nist.hit.core.domain.ValidationReport;
import gov.nist.hit.core.repo.UserRepository;
import gov.nist.hit.core.service.TransactionService;
import gov.nist.hit.core.service.TransportConfigService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.ValidationReportService;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
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
  protected ValidationReportService validationReportService;

  @Override
  public boolean exitBySutInitiatorPropertiesAndProtocol(Map<String, String> criteria,
      String protocol) {
    TransportConfig config =
        transportConfigService.findOneByPropertiesAndProtocol(criteria,
            TestingType.SUT_INITIATOR, protocol);
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

      List<ValidationReport> reports = validationReportService.findAllByUser(id);
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



}
