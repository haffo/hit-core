package gov.nist.hit.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.repo.TestCaseGroupRepository;
import gov.nist.hit.core.service.TestCaseGroupService;

@Service
public class TestCaseGroupServiceImpl implements TestCaseGroupService {

  @Autowired
  private TestCaseGroupRepository testCaseGroupRepository;


  @Override
  public TestArtifact testStory(Long id) {
    return testCaseGroupRepository.testStory(id);
  }


  @Override
  public TestCaseGroup findOne(Long id) {
    return testCaseGroupRepository.findOne(id);
  }


  @Override
  @Transactional(value = "transactionManager")
  public void delete(TestCaseGroup testCaseGroup) {
    testCaseGroupRepository.delete(testCaseGroup);
  }

  @Override
  @Transactional(value = "transactionManager")
  public void save(TestCaseGroup testCaseGroup) {
    testCaseGroupRepository.saveAndFlush(testCaseGroup);
  }


}
