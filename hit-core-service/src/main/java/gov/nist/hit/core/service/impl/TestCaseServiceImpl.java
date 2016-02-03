package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.repo.TestCaseRepository;
import gov.nist.hit.core.service.TestCaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
