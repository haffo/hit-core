package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.TestCaseDocumentation;
import gov.nist.hit.core.domain.TestingStage;

public interface TestCaseDocumentationService {

  public TestCaseDocumentation findOneByStageAndDomain(TestingStage stage, String domain);


}
