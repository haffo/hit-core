package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.TestCaseDocumentation;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestingStage;

public interface TestCaseDocumentationService {


  public TestCaseDocumentation findOneByStageAndDomainAndScope(TestingStage stage, String domain,
      TestScope scope);

  public TestCaseDocumentation findOneByStageAndDomainAndAuthorAndScope(TestingStage stage,
      String domain, String authorname, TestScope scope);



}
