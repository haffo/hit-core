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

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestCase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {

  @Query("select tc.testStory from TestCase tc where tc.id = :id")
  public TestArtifact testStory(@Param("id") Long id);
  
  @Query("select tc from TestCase tc where tc.persistentId = :id")
  public TestCase getByPersistentId(@Param("id") Long id);

  @Modifying
  @Transactional(value = "transactionManager")
  @Query("delete from TestCase tc where tc.preloaded = true")
  public void deletePreloaded();
  
}
