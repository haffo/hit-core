package gov.nist.hit.core.repo;


import gov.nist.hit.core.domain.KeyValuePair;
import gov.nist.hit.core.domain.Transaction;
import gov.nist.hit.core.domain.TransactionStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  @Query("select transaction.incoming from Transaction transaction where transaction.user.id = :userId and transaction.testStep.id = :testStepId")
  String getIncomingMessageByUserIdAndTestStepId(@Param("userId") Long userId,
      @Param("testStepId") Long testStepId);

  @Query("select transaction.outgoing from Transaction transaction where transaction.user.id = :userId and transaction.testStep.id = :testStepId")
  String getOutgoingMessageByUserIdAndTestStepId(@Param("userId") Long userId,
      @Param("testStepId") Long testStepId);

  @Query("select transaction from Transaction transaction where transaction.user.id = :userId and transaction.testStep.id = :testStepId")
  Transaction findOneByUserAndTestStep(@Param("userId") Long userId,
      @Param("testStepId") Long testStepId);

  @Query("select transaction from Transaction transaction where transaction.user.id = :userId")
  List<Transaction> findAllByUser(@Param("userId") Long userId);

  @Query("delete from Transaction transaction where transaction.user.id = :userId")
  void deleteAllByUser(@Param("userId") Long userId);

  @Query("select transaction from Transaction transaction where transaction.testStep.id = :testStepId and transaction.matches(:criteria) = true")
  Transaction findOneByTestStepIdAndCriteria(@Param("criteria") List<KeyValuePair> criteria,
      @Param("testStepId") Long testStepId);

  @Query("select transaction from Transaction transaction where transaction.testStep.id = :testStepId and transaction.matches(:criteria) = true")
  Transaction findOneByCriteria(@Param("criteria") List<KeyValuePair> criteria,
      @Param("testStepId") Long testStepId);

  @Query("select transaction.status from Transaction transaction where transaction.testStep.id = :testStepId and transaction.matches(:criteria) = true")
  TransactionStatus getStatusByCriteria(@Param("criteria") List<KeyValuePair> criteria,
      @Param("testStepId") Long testStepId);

  @Query("select transaction.status from Transaction transaction where transaction.matches(:criteria) = true")
  TransactionStatus getStatusByCriteria(@Param("criteria") List<KeyValuePair> criteria);


  @Query("select transaction from Transaction transaction where transaction.matches(:criteria) = true")
  Transaction findOneByCriteria(@Param("criteria") List<KeyValuePair> criteria);


}
