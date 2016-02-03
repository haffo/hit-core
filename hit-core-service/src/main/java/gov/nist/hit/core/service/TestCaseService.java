package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestCase;

public interface TestCaseService {

  public TestCase findOne(Long id);

  public TestArtifact testStory(Long id);



}
