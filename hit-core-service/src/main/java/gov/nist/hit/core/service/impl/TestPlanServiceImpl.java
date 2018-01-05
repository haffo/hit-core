package gov.nist.hit.core.service.impl;


import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  @Transactional(value = "transactionManager")
  public List<TestPlan> findShortAllByStage(TestingStage stage) {
    return testPlanRepository.findShortAllByStage(stage);
  }

  @Override
  @Transactional(value = "transactionManager")
  public List<TestPlan> findShortAllByStageAndScope(TestingStage stage, TestScope scope) {
    return testPlanRepository.findShortAllByStageAndScope(stage, scope);
  }

  @Override
  @Transactional(value = "transactionManager")
  public List<TestPlan> findAllShortByStageAndUsernameAndScope(TestingStage stage,
      String authorUsername, TestScope scope) {
    return testPlanRepository.findAllShortByStageAndUsernameAndScope(stage, authorUsername, scope);
  }


  @Override
  @Transactional(value = "transactionManager")
  public List<TestArtifact> findAllTestPackages(TestingStage stage) {
    return testPlanRepository.findAllTestPackages(stage);
  }

  @Override
  public TestPlan findOne(Long testPlanId) {
    // TODO Auto-generated method stub
    return testPlanRepository.findOne(testPlanId);
  }

  @Override
  @Transactional(value = "transactionManager")
  public List<TestPlan> findShortAllByStageAndAuthor(TestingStage stage, String authorUsername) {
    return testPlanRepository.findShortAllByStageAndAuthor(stage, authorUsername);
  }

  @Override
  @Transactional(value = "transactionManager")
  public Set<String> findAllCategoriesByStageAndScope(TestingStage stage, TestScope scope) {
    return testPlanRepository.findAllCategoriesByStageAndScope(stage, scope);
  }

  @Override
  @Transactional(value = "transactionManager")
  public Set<String> findAllCategoriesByStageAndScopeAndUser(TestingStage stage, TestScope scope,
      String username) {
    return testPlanRepository.findAllCategoriesByStageAndScopeAndUser(stage, scope, username);
  }

  @Override
  public List<TestPlan> findShortAllByStageAndScopeAndAuthor(TestingStage stage, TestScope scope,
      String authorUsername) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  @Transactional(value = "transactionManager")
  public void delete(TestPlan testPlan) {
    testPlanRepository.delete(testPlan);
  }

  @Override
  public TestPlan save(TestPlan testPlan) {
    // TODO Auto-generated method stub
    return testPlanRepository.save(testPlan);
  }



}
