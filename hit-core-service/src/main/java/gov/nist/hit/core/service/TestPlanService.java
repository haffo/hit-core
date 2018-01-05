package gov.nist.hit.core.service;

import java.util.List;
import java.util.Set;

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestingStage;

public interface TestPlanService {

  public TestPlan findOne(Long testPlanId);

  public List<TestArtifact> findAllTestPackages(TestingStage stage);

  public List<TestPlan> findShortAllByStage(TestingStage stage);

  public List<TestPlan> findShortAllByStageAndScope(TestingStage stage, TestScope scope);

  public List<TestPlan> findShortAllByStageAndAuthor(TestingStage stage, String authorUsername);

  public List<TestPlan> findShortAllByStageAndScopeAndAuthor(TestingStage stage, TestScope scope,
      String authorUsername);

  public Set<String> findAllCategoriesByStageAndScope(TestingStage stage, TestScope scope);

  public Set<String> findAllCategoriesByStageAndScopeAndUser(TestingStage stage, TestScope scope,
      String username);

  void delete(TestPlan testPlan);

  public TestPlan save(TestPlan testPlan);

  public List<TestPlan> findAllShortByStageAndUsernameAndScope(TestingStage stage,
      String authorUsername, TestScope scope);



}
