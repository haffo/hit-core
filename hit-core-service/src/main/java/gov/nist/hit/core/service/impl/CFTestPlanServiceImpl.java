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
  public List<CFTestPlan> findShortAllByStageAndScope(TestingStage stage, TestScope scope) {
    // TODO Auto-generated method stub
    return testPlanRepository.findShortAllByStageAndScope(stage, scope);
  }

  @Override
  public List<CFTestPlan> findShortAllByStageAndAuthor(TestingStage stage, String authorUsername,
      TestScope scope) {
    // TODO Auto-generated method stub
    return testPlanRepository.findShortAllByStageAndAuthor(stage, authorUsername, scope);
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

  @Override
  public List<CFTestPlan> findAllByScope(TestScope scope) {
    // TODO Auto-generated method stub
    return testPlanRepository.findAllByScope(scope);
  }


  @Override
  public List<CFTestPlan> findByIds(Set<Long> ids) {
    // TODO Auto-generated method stub
    return testPlanRepository.findByIds(ids);
  }



  @Override
  public List<CFTestPlan> findShortAllByScopeAndUsername(TestScope scope, String authorUsername) {
    // TODO Auto-generated method stub
    return testPlanRepository.findShortAllByScopeAndUsername(scope, authorUsername);

  }

  @Override
  public List<CFTestPlan> findAllByScopeAndUsername(TestScope scope, String authorUsername) {
    return testPlanRepository.findAllByScopeAndUsername(scope, authorUsername);
  }

  @Override
  public Set<String> findAllCategoriesByScope(TestScope scope) {
    // TODO Auto-generated method stub
    return testPlanRepository.findAllCategoriesByScope(scope);
  }

  @Override
  public Set<String> findAllCategoriesByScopeAndUser(TestScope scope, String username) {
    // TODO Auto-generated method stub
    return testPlanRepository.findAllCategoriesByScopeAndUser(scope, username);
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
  public List<CFTestPlan> findShortAllByScope(TestScope scope) {
    // TODO Auto-generated method stub
    return testPlanRepository.findShortAllByScope(scope);
  }

  @Override
  public void delete(CFTestPlan testPlan) {
    testPlanRepository.delete(testPlan);

  }



}
