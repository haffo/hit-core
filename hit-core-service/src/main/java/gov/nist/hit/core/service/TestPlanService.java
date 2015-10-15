package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestPlan;

import java.util.List;

public interface TestPlanService {

  public List<TestPlan> findAllByStage(TestingStage stage);

  public List<TestArtifact> findAllTestPackages(TestingStage stage);
}
