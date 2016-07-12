package gov.nist.hit.core.repo;


import gov.nist.hit.core.domain.Transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long>,
    JpaSpecificationExecutor<Transaction> {

  @Query("select transaction.incoming from Transaction transaction where transaction.userId = :userId and transaction.testStepId = :testStepId")
  String getIncomingMessageByUserIdAndTestStepId(@Param("userId") Long userId,
      @Param("testStepId") Long testStepId);

  @Query("select transaction.outgoing from Transaction transaction where transaction.userId = :userId and transaction.testStepId = :testStepId")
  String getOutgoingMessageByUserIdAndTestStepId(@Param("userId") Long userId,
      @Param("testStepId") Long testStepId);

  @Query("select transaction from Transaction transaction where transaction.userId = :userId and transaction.testStepId = :testStepId")
  Transaction findOneByUserAndTestStep(@Param("userId") Long userId,
      @Param("testStepId") Long testStepId);

  @Query("select transaction from Transaction transaction where transaction.userId = :userId")
  List<Transaction> findAllByUser(@Param("userId") Long userId);

}
