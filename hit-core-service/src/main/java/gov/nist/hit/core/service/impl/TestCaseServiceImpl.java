package gov.nist.hit.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.repo.TestCaseRepository;
import gov.nist.hit.core.service.TestCaseService;

@Service
public class TestCaseServiceImpl implements TestCaseService {

  @Autowired
  private TestCaseRepository testCaseRepository;


  @Override
  public TestArtifact testStory(Long id) {
    return testCaseRepository.testStory(id);
  }


  @Override
  public TestCase findOne(Long id) {
    return testCaseRepository.findOne(id);
  }


  @Override
  @Transactional(value = "transactionManager")
  public void delete(TestCase testCase) {
    testCaseRepository.delete(testCase);
  }

  @Override
  @Transactional(value = "transactionManager")
  public void save(TestCase testCase) {
    testCaseRepository.saveAndFlush(testCase);
  }


}
