package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.Stage;
import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestPlan;

import java.util.List;

public interface TestPlanService {

  public List<TestPlan> findAllByStage(Stage stage);

  public List<TestArtifact> findAllTestPackages(Stage stage);
}
