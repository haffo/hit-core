package gov.nist.hit.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.repo.TestStepRepository;
import gov.nist.hit.core.service.TestStepService;

@Service
public class TestStepServiceImpl implements TestStepService {

  @Autowired
  private TestStepRepository testStepRepository;

  @Override
  public TestArtifact jurorDocument(Long id) {
    return testStepRepository.jurorDocument(id);
  }

  @Override
  public TestArtifact testStory(Long id) {
    return testStepRepository.testStory(id);
  }

  @Override
  public TestArtifact messageContent(Long id) {
    return testStepRepository.messageContent(id);
  }

  @Override
  public TestArtifact testDataSpecification(Long id) {
    return testStepRepository.testDataSpecification(id);
  }

  @Override
  public TestStep findOne(Long id) {
    return testStepRepository.findOne(id);
  }

  @Override
  // @Cacheable(value = "HitCache", key = "#stage.name() + 'TestSteps'")
  public List<TestStep> findAllByStage(TestingStage stage) {
    return testStepRepository.findAllByStage(stage);
  }

  @Override
  public TestStep findOneByTestContext(Long testContextId) {
    return testStepRepository.findOneByTestContextId(testContextId);
  }

  @Override
  @Transactional(value = "transactionManager")
  public void delete(TestStep testStep) {
    testStepRepository.delete(testStep);
  }

  @Override
  @Transactional(value = "transactionManager")
  public void save(TestStep testStep) {
    testStepRepository.saveAndFlush(testStep);
  }


}
