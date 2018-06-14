package gov.nist.hit.core.service;

import java.util.List;

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestingStage;

public interface TestStepService {

  public TestStep findOne(Long id);

  public TestStep findOneByTestContext(Long testContextId);

  public List<TestStep> findAllByStage(TestingStage stage);

  public TestArtifact jurorDocument(Long id);

  public TestArtifact testStory(Long id);

  public TestArtifact messageContent(Long id);

  public TestArtifact testDataSpecification(Long id);

  public void delete(TestStep testStep);

  void save(TestStep testStep);



}
