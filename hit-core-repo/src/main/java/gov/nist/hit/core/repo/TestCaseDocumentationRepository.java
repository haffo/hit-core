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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gov.nist.hit.core.domain.TestCaseDocumentation;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestingStage;

public interface TestCaseDocumentationRepository
    extends JpaRepository<TestCaseDocumentation, Long> {

  @Query("select docu from TestCaseDocumentation docu where docu.stage = :stage and docu.domain = :domain and docu.scope = :scope")
  public TestCaseDocumentation findOneByStageAndDomainAndScope(@Param("stage") TestingStage stage,
      @Param("domain") String domain, @Param("scope") TestScope scope);

  @Query("select docu from TestCaseDocumentation docu where docu.stage = :stage and docu.domain = :domain and  docu.authorUsername = :authorUsername and docu.scope = :scope")
  public TestCaseDocumentation findOneByStageAndDomainAndAuthorAndScope(
      @Param("stage") TestingStage stage, @Param("domain") String domain,
      @Param("authorUsername") String authorUsername, @Param("scope") TestScope scope);


}
