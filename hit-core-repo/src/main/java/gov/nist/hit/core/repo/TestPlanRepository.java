/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */

package gov.nist.hit.core.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestingStage;

public interface TestPlanRepository extends JpaRepository<TestPlan, Long> {

  @Transactional(value = "transactionManager")
  @Query("select tp from TestPlan tp where tp.stage= :stage and tp.domain = :domain")
  public List<TestPlan> findAllByStageAndDomain(@Param("stage") TestingStage stage,
      @Param("domain") String domain);

  @Transactional(value = "transactionManager")
  @Query("select tp from TestPlan tp where tp.stage=:stage and tp.scope=:scope and tp.domain=:domain")
  public List<TestPlan> findAllByStageAndScopeAndDomain(@Param("stage") TestingStage stage,
      @Param("scope") TestScope scope, @Param("domain") String domain);


  @Transactional(value = "transactionManager")
  @Query("select tp from TestPlan tp where tp.stage=:stage and tp.scope=:scope and tp.domain=:domain and tp.authorUsername = :authorUsername")
  public List<TestPlan> findAllByStageAndScopeAndDomainAndAuthor(@Param("stage") TestingStage stage,
      @Param("scope") TestScope scope, @Param("domain") String domain,
      @Param("authorUsername") String authorUsername);


  @Transactional(value = "transactionManager")
  @Query("select new gov.nist.hit.core.domain.TestPlan(id, name, description, position, transport, domain, persistentId) from TestPlan tp where tp.stage = ?1 and tp.domain=?2")
  public List<TestPlan> findShortAllByStageAndDomain(TestingStage stage, String domain);

  @Transactional(value = "transactionManager")
  @Query("select new gov.nist.hit.core.domain.TestPlan(id, name, description, position, transport, domain, persistentId) from TestPlan tp where tp.stage = ?1 and tp.scope = ?2 and tp.domain=?3")
  public List<TestPlan> findShortAllByStageAndScopeAndDomain(TestingStage stage, TestScope scope,
      String domain);

  @Transactional(value = "transactionManager")
  @Query("select new gov.nist.hit.core.domain.TestPlan(id, name, description, position, transport, domain, persistentId) from TestPlan tp where tp.stage = ?1 and tp.authorUsername = ?2 and tp.domain=?3")
  public List<TestPlan> findShortAllByStageAndAuthorAndDomain(TestingStage stage,
      String authorUsername, String domain);

  @Transactional(value = "transactionManager")
  @Query("select new gov.nist.hit.core.domain.TestPlan(id, name, description, position, transport, domain, persistentId) from TestPlan tp where tp.stage = ?1 and tp.authorUsername = ?2 and tp.scope = ?3 and tp.domain=?4")
  public List<TestPlan> findAllShortByStageAndUsernameAndScopeAndDomain(TestingStage stage,
      String authorUsername, TestScope scope, String domain);


  @Transactional(value = "transactionManager")
  @Query("select tp.testPackage from TestPlan tp where tp.stage = :stage and tp.domain = :domain")
  public List<TestArtifact> findAllTestPackagesByDomain(@Param("stage") TestingStage stage,
      @Param("domain") String domain);

  @Transactional(value = "transactionManager")
  @Query("select tp.testPlanSummary from TestPlan tp where tp.stage = :stage and tp.domain = :domain")
  public List<TestArtifact> findAllTestPlanSummary(@Param("stage") TestingStage stage,
      @Param("domain") String domain);

  @Query("select tp.testPlanSummary from TestPlan tp where tp.id = :id")
  public TestArtifact testPlanSummary(@Param("id") Long id);

  @Query("select tp.testPackage from TestPlan tp where tp.id = :id")
  public TestArtifact testPackage(@Param("id") Long id);

  @Query("select tp from TestPlan tp where tp.persistentId = :id")
  public TestPlan getByPersistentId(@Param("id") Long id);

  @Modifying
  @Transactional(value = "transactionManager")
  @Query("delete from TestPlan to where to.preloaded = true")
  public void deletePreloaded();

  @Modifying
  @Transactional(value = "transactionManager")
  @Query("delete from TestPlan to where to.preloaded = false")
  public void deleteNonPreloaded();


}
