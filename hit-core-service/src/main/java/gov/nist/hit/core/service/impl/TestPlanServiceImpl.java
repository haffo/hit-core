package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.repo.TestPlanRepository;
import gov.nist.hit.core.service.TestPlanService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestPlanServiceImpl implements TestPlanService {

  @Autowired
  private TestPlanRepository testPlanRepository;

  @Override
  public List<TestPlan> findAllByStage(TestingStage stage) {
    return testPlanRepository.findAllByStage(stage);
  }

  @Override
  public List<TestArtifact> findAllTestPackages(TestingStage stage) {
    return testPlanRepository.findAllTestPackages(stage);
  }


}
