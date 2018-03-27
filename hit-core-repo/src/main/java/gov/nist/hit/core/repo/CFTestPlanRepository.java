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
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import gov.nist.hit.core.domain.CFTestPlan;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestingStage;

public interface CFTestPlanRepository extends JpaRepository<CFTestPlan, Long> {

  @Query("select tp from CFTestPlan tp where tp.scope = :scope and tp.domain = :domain")
  public List<CFTestPlan> findAllByScopeAndDomain(@Param("scope") TestScope scope,
      @Param("domain") String domain);


  @Query("select new gov.nist.hit.core.domain.CFTestPlan(id, name, description, position,persistentId,category,domain) from CFTestPlan tp where tp.scope = :scope and tp.authorUsername = :authorUsername and tp.domain =:domain")
  public List<CFTestPlan> findShortAllByScopeAndUsernameAndDomain(@Param("scope") TestScope scope,
      @Param("authorUsername") String authorUsername, @Param("domain") String domain);

  @Query("select tp from CFTestPlan tp where tp.scope = :scope and tp.authorUsername = :authorUsername and tp.domain=:domain")
  public List<CFTestPlan> findAllByScopeAndUsernameAndDomain(@Param("scope") TestScope scope,
      @Param("authorUsername") String authorUsername, @Param("domain") String domain);

  @Query("select new gov.nist.hit.core.domain.CFTestPlan(id, name, description, position,persistentId,category,domain) from CFTestPlan tp where tp.scope = :scope and tp.domain = :domain")
  public List<CFTestPlan> findShortAllByScopeAndDomain(@Param("scope") TestScope scope,
      @Param("domain") String domain);


  @Query("select tp from CFTestPlan tp where tp.stage = :stage and tp.scope = :scope and tp.domain = :domain")
  public List<CFTestPlan> findAllByStageAndScopeAndDomain(@Param("stage") TestingStage stage,
      @Param("scope") TestScope scope, @Param("domain") String domain);

  @Query("select new gov.nist.hit.core.domain.CFTestPlan(id, name, description, position,persistentId,category,domain) from CFTestPlan tp where tp.stage = ?1 and tp.scope = ?2 and tp.domain = ?3")
  public List<CFTestPlan> findShortAllByStageAndScopeAndDomain(TestingStage stage, TestScope scope,
      String domain);

  @Query("select new gov.nist.hit.core.domain.CFTestPlan(id, name, description, position, persistentId,category,domain) from CFTestPlan tp where tp.stage = ?1 and tp.authorUsername = ?2 and tp.scope = ?3 and tp.domain = ?4")
  public List<CFTestPlan> findShortAllByStageAndAuthorAndDomain(TestingStage stage,
      String authorUsername, TestScope scope, String domain);

  @Modifying
  @Transactional(value = "transactionManager")
  @Query("delete from CFTestPlan to where to.preloaded = true")
  public void deletePreloaded();

  @Modifying
  @Transactional(value = "transactionManager")
  @Query("delete from CFTestPlan to where to.preloaded = false")
  public void deleteNonPreloaded();

  @Transactional(value = "transactionManager")
  @Query("select tp.category from CFTestPlan tp where tp.scope=:scope and tp.domain=:domain")
  public Set<String> findAllCategoriesByScopeAndDomain(@Param("scope") TestScope scope,
      @Param("domain") String domain);


  @Transactional(value = "transactionManager")
  @Query("select tp.category from CFTestPlan tp where tp.scope=:scope and tp.authorUsername = :username and tp.domain = :domain")
  public Set<String> findAllCategoriesByScopeAndUserAndDomain(@Param("scope") TestScope scope,
      @Param("username") String username, @Param("domain") String domain);


  @Query("select tp from CFTestPlan tp where tp.id IN (:ids)")
  public List<CFTestPlan> findByIds(@Param("ids") Set<Long> ids);



}
