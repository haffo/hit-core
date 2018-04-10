package gov.nist.hit.core.service.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

  @Autowired
  @PersistenceContext(unitName = "base-tool")
  protected EntityManager entityManager;


  @Override
  public List<CFTestPlan> findShortAllByStageAndScopeAndDomain(TestingStage stage, TestScope scope,
      String domain) {
    // TODO Auto-generated method stub
    return testPlanRepository.findShortAllByStageAndScopeAndDomain(stage, scope, domain);
  }

  @Override
  public List<CFTestPlan> findShortAllByStageAndAuthorAndScopeAndDomain(TestingStage stage,
      String authorUsername, TestScope scope, String domain) {
    // TODO Auto-generated method stub
    return testPlanRepository.findShortAllByStageAndAuthorAndScopeAndDomain(stage, authorUsername,
        scope, domain);
  }

  @Override
  public CFTestPlan findOne(Long testPlanId) {
    // TODO Auto-generated method stub
    return testPlanRepository.findOne(testPlanId);
  }

  @Override
  public List<CFTestPlan> findAllByStageAndScopeAndDomain(TestingStage stage, TestScope scope,
      String domain) {
    // TODO Auto-generated method stub
    return testPlanRepository.findAllByStageAndScopeAndDomain(stage, scope, domain);
  }

  @Override
  public List<CFTestPlan> findAllByScopeAndDomain(TestScope scope, String domain) {
    // TODO Auto-generated method stub
    return testPlanRepository.findAllByScopeAndDomain(scope, domain);
  }


  @Override
  public List<CFTestPlan> findByIds(Set<Long> ids) {
    // TODO Auto-generated method stub
    return testPlanRepository.findByIds(ids);
  }



  @Override
  public List<CFTestPlan> findShortAllByScopeAndUsernameAndDomain(TestScope scope,
      String authorUsername, String domain) {
    // TODO Auto-generated method stub
    return testPlanRepository.findShortAllByScopeAndUsernameAndDomain(scope, authorUsername,
        domain);

  }

  @Override
  public List<CFTestPlan> findAllByScopeAndUsernameAndDomain(TestScope scope, String authorUsername,
      String domain) {
    return testPlanRepository.findAllByScopeAndUsernameAndDomain(scope, authorUsername, domain);
  }

  @Override
  public Set<String> findAllCategoriesByScopeAndDomain(TestScope scope, String domain) {
    // TODO Auto-generated method stub
    return testPlanRepository.findAllCategoriesByScopeAndDomain(scope, domain);
  }

  @Override
  public Set<String> findAllCategoriesByScopeAndUserAndDomain(TestScope scope, String username,
      String domain) {
    // TODO Auto-generated method stub
    return testPlanRepository.findAllCategoriesByScopeAndUserAndDomain(scope, username, domain);
  }

  @Override
  public CFTestPlan save(CFTestPlan testPlan) {
    return testPlanRepository.saveAndFlush(testPlan);
  }

  @Override
  public void updateCategory(Set<Long> ids, String cat) {
    String sql = "update CFTestPlan tp set tp.category = ? where tp.id IN ?";
    entityManager.createNativeQuery(sql, CFTestPlan.class).setParameter(1, cat).setParameter(2, ids)
        .executeUpdate();
  }

  @Override
  public List<CFTestPlan> findShortAllByScopeAndDomain(TestScope scope, String domain) {
    // TODO Auto-generated method stub
    return testPlanRepository.findShortAllByScopeAndDomain(scope, domain);
  }

  @Override
  public void delete(CFTestPlan testPlan) {
    testPlanRepository.delete(testPlan);

  }



}
