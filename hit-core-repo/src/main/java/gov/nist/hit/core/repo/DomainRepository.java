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

import gov.nist.hit.core.domain.Domain;
import gov.nist.hit.core.domain.TestScope;

public interface DomainRepository extends JpaRepository<Domain, Long> {

  @Query("select dom from Domain dom  where dom.domain = :key")
  public Domain findOneByKey(@Param("key") String key);

  // @Query("select new gov.nist.hit.core.domain.Domain(name, domain) from Domain d where d.disabled
  // = :disabled")
  // public List<Domain> findShortAll(@Param("disabled") boolean disabled);

  @Query("select new gov.nist.hit.core.domain.Domain(name, domain) from Domain d where d.scope=:scope and d.authorUsername=:authorUsername")
  public List<Domain> findShortAllByScopeAndAuthorname(@Param("scope") TestScope scope,
      @Param("authorUsername") String authorUsername);

  @Query("select new gov.nist.hit.core.domain.Domain(name, domain) from Domain d where d.disabled = false and (d.scope='GLOBAL' or d.authorUsername=:authorUsername)")
  public List<Domain> findShortAllWithGlobalOrAuthorname(
      @Param("authorUsername") String authorUsername);


  @Query("select new gov.nist.hit.core.domain.Domain(name, domain) from Domain d where d.scope= 'GLOBAL' and d.disabled =:disabled")
  public List<Domain> findAllShortWithGlobal(@Param("disabled") boolean disabled);

  @Modifying
  @Transactional(value = "transactionManager")
  @Query("delete from Domain dom where dom.preloaded = true")
  public void deletePreloaded();


}
