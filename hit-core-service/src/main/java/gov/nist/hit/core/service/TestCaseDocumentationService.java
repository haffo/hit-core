package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.Stage;
import gov.nist.hit.core.domain.TestCaseDocumentation;

public interface TestCaseDocumentationService {

  public TestCaseDocumentation findOneByStage(Stage stage);


}
