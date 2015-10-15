package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestStep;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TestStepService {

  public TestStep findOne(Long id);

  @Query("select ts from TestStep ts where ts.stage = :stage")
  public List<TestStep> findAllByStage(@Param("stage") TestingStage stage);

  public TestArtifact jurorDocument(Long id);

  public TestArtifact testPackage(Long id);

  public TestArtifact testStory(Long id);

  public TestArtifact messageContent(Long id);

  public TestArtifact testDataSpecification(Long id);


}
