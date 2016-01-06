package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.Transaction;
import gov.nist.hit.core.repo.TransactionRepository;
import gov.nist.hit.core.service.TransactionService;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

  @Autowired
  protected TransactionRepository transactionRepository;

  @Autowired
  protected EntityManager entityManager;


  @Override
  public String getIncomingMessageByUserIdAndTestStepId(Long userId, Long testStepId) {
    return transactionRepository.getIncomingMessageByUserIdAndTestStepId(userId, testStepId);
  }

  @Override
  public String getOutgoingMessageByUserIdAndTestStepId(Long userId, Long testStepId) {
    return transactionRepository.getOutgoingMessageByUserIdAndTestStepId(userId, testStepId);
  }

  @Override
  public Transaction findOneByUserAndTestStep(Long userId, @Param("testStepId") Long testStepId) {
    return transactionRepository.findOneByUserAndTestStep(userId, testStepId);
  }

  @Override
  public List<Transaction> findAllByUser(Long userId) {
    return transactionRepository.findAllByUser(userId);
  }

  @Override
  public Transaction findOneByTestStepIdAndProperties(Map<String, String> criteria, Long testStepId) {
    return null;
  }

  @Override
  public Transaction findOneByProperties(Map<String, String> criteria, Long testStepId) {
    return null;
  }


  @Override
  public Transaction findOneByProperties(Map<String, String> criteria) {
    String where = "";
    for (String key : criteria.keySet()) {
      where = where != null ? " and " : "" + where + " tr." + key + "=" + criteria.get(key);
    }

    String query = "select * from TRANSACTION tr, TRANSACTION_CONFIG tc where " + where;


    return null;
  }

  @Override
  public void delete(List<Transaction> transactions) {
    transactionRepository.delete(transactions);
  }

  @Override
  public void delete(Transaction transaction) {
    transactionRepository.delete(transaction);
  }

  @Override
  public Transaction save(Transaction transaction) {
    return transactionRepository.saveAndFlush(transaction);
  }

  @Override
  public List<Transaction> save(List<Transaction> transactions) {
    return transactionRepository.save(transactions);
  }


}
