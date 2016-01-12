package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.Transaction;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public interface TransactionService extends Serializable {

  String getIncomingMessageByUserIdAndTestStepId(Long userId, Long testStepId);

  String getOutgoingMessageByUserIdAndTestStepId(Long userId, Long testStepId);

  Transaction findOneByUserAndTestStep(Long userId, Long testStepId);

  List<Transaction> findAllByUser(Long userId);

  Transaction findOneByTestStepIdAndProperties(Map<String, String> criteria, Long testStepId);

  Transaction findOneByProperties(Map<String, String> criteria);

  List<Transaction> findAllByProperties(Map<String, String> criteria);

  void delete(List<Transaction> transactions);

  void delete(Transaction transaction);

  void delete(Long id);

  Transaction save(Transaction transaction);

  List<Transaction> save(List<Transaction> transactions);

  Transaction findOne(Long id);


}
