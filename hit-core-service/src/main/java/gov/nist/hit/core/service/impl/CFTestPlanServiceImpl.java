package gov.nist.hit.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nist.hit.core.domain.CFTestPlan;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.repo.CFTestPlanRepository;
import gov.nist.hit.core.service.CFTestPlanService;

@Service
public class CFTestPlanServiceImpl implements CFTestPlanService {

  @Autowired
  private CFTestPlanRepository testPlanRepository;


  @Override
  public List<CFTestPlan> findShortAllByStageAndScope(TestingStage stage, TestScope scope) {
    // TODO Auto-generated method stub
    return testPlanRepository.findShortAllByStageAndScope(stage, scope);
  }

  @Override
  public List<CFTestPlan> findShortAllByStageAndAuthor(TestingStage stage, String authorUsername) {
    // TODO Auto-generated method stub
    return testPlanRepository.findShortAllByStageAndAuthor(stage, authorUsername);
  }

  @Override
  public CFTestPlan findOne(Long testPlanId) {
    // TODO Auto-generated method stub
    return testPlanRepository.findOne(testPlanId);
  }

  @Override
  public List<CFTestPlan> findAllByStageAndScope(TestingStage stage, TestScope scope) {
    // TODO Auto-generated method stub
    return testPlanRepository.findAllByStageAndScope(stage, scope);
  }



}
