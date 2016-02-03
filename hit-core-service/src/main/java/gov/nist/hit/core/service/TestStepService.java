package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestingStage;

import java.util.List;

import org.springframework.data.repository.query.Param;

public interface TestStepService {

  public TestStep findOne(Long id);

  public TestStep findOneByTestContext(Long testContextId);

  public List<TestStep> findAllByStage(@Param("stage") TestingStage stage);

  public TestArtifact jurorDocument(Long id);

  public TestArtifact testStory(Long id);

  public TestArtifact messageContent(Long id);

  public TestArtifact testDataSpecification(Long id);


}
