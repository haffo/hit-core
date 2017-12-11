package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestCaseGroup;

public interface TestCaseGroupService {

  public TestCaseGroup findOne(Long id);

  public TestArtifact testStory(Long id);

  public void delete(TestCaseGroup testCase);

  void save(TestCaseGroup testCaseGroup);


}
