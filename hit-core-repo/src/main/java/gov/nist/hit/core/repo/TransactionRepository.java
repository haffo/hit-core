package gov.nist.hit.core.repo;


import gov.nist.hit.core.domain.Transaction;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long>,
    JpaSpecificationExecutor<Transaction> {

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

  // @Query("select transaction from Transaction transaction where transaction.testStep.id = :testStepId and transaction.matches(:criteria) = true")
  // Transaction findOneByTestStepIdAndProperties(@Param("criteria") Map<String, String> criteria,
  // @Param("testStepId") Long testStepId);
  //
  // @Query("select transaction from Transaction transaction where transaction.testStep.id = :testStepId and transaction.matches(:criteria) = true")
  // Transaction findOneByProperties(@Param("criteria") Map<String, String> criteria,
  // @Param("testStepId") Long testStepId);

  // @Query("select transaction.status from Transaction transaction where transaction.testStep.id = :testStepId and transaction.matches(:criteria) = true")
  // TransactionStatus getStatusByProperties(@Param("criteria") Map<String, String> criteria,
  // @Param("testStepId") Long testStepId);
  //
  // @Query("select transaction.status from Transaction transaction where transaction.matches(:criteria) = true")
  // TransactionStatus getStatusByProperties(@Param("criteria") Map<String, String> criteria);

  @Query("select tr from Transaction tr where tr.matches(:criteria) = true")
  Transaction findOneByProperties(@Param("criteria") Map<String, String> criteria);


}
