package gov.nist.hit.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.repo.TestPlanRepository;
import gov.nist.hit.core.service.TestPlanService;

@Service
public class TestPlanServiceImpl implements TestPlanService {

  @Autowired
  private TestPlanRepository testPlanRepository;

  @Override
  public List<TestPlan> findAllByStage(TestingStage stage) {
    return testPlanRepository.findAllByStage(stage);
  }

  @Override
  public List<TestPlan> findShortAllByStage(TestingStage stage) {
    return testPlanRepository.findShortAllByStage(stage);
  }

  @Override
  public List<TestPlan> findShortAllByStageAndScope(TestingStage stage, TestScope scope) {
    return testPlanRepository.findShortAllByStageAndScope(stage, scope);
  }


  @Override
  public List<TestArtifact> findAllTestPackages(TestingStage stage) {
    return testPlanRepository.findAllTestPackages(stage);
  }

  @Override
  public TestPlan findOne(Long testPlanId) {
    // TODO Auto-generated method stub
    return testPlanRepository.findOne(testPlanId);
  }

  @Override
  public List<TestPlan> findShortAllByStageAndAuthor(TestingStage stage, String authorUsername) {
    return testPlanRepository.findShortAllByStageAndAuthor(stage, authorUsername);
  }


}
