package gov.nist.auth.hit.core.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gov.nist.auth.hit.core.domain.Account;


/**
 * Repository interface for {@link Account} instances. Provides basic CRUD operations due to the
 * extension of {@link JpaRepository}.
 * 
 * @author fdevaulx
 */
public interface AccountRepository
    extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

  /**
   * Find an account by the username of the account. Username is unique.
   */
  @Query("select a from Account a where a.username = ?1")
  public Account findByTheAccountsUsername(String username);

  /**
   * Find an account by the email address of the account. Email address is unique.
   */
  @Query("select a from Account a where a.email = ?1")
  public Account findByTheAccountsEmail(String email);

  /**
     * 
     * */
  @Query("select a from Account a where a.accountType = ?1")
  public List<Account> findByTheAccountsAccountType(String accountType);

  @Modifying
  @Query("update Account a set a.lastTestPlanPersistenceId = :testPlanPersistenceId where a.id = :accountId and a.guestAccount = false")
  public void recordLastTestPlan(@Param("accountId") Long accountId,
      @Param("testPlanPersistenceId") Long testPlanPersistenceId);

  @Modifying
  @Query("update Account a set a.lastLoggedInDate = :lastLoggedInDate where a.id = :accountId")
  public void recordLastLoggedInDate(@Param("accountId") Long accountId,
      @Param("lastLoggedInDate") Date lastLoggedInDate);

  @Modifying
  @Query("update Account a set a.lastCFTestPlanPersistenceId = :testPlanPersistenceId where a.id = :accountId and a.guestAccount = false")
  public void recordLastCFTestPlan(@Param("accountId") Long accountId,
      @Param("testPlanPersistenceId") Long testPlanPersistenceId);



}
