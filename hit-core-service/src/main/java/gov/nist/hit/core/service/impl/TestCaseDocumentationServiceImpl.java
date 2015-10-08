package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.Stage;
import gov.nist.hit.core.domain.TestCaseDocumentation;
import gov.nist.hit.core.repo.TestCaseDocumentationRepository;
import gov.nist.hit.core.service.TestCaseDocumentationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TestCaseDocumentationServiceImpl implements TestCaseDocumentationService {


  @Autowired
  private TestCaseDocumentationRepository testCaseDocumentationRepository;

  @Override
  @Cacheable(value = "documentationCache", key = "#stage.name() + 'TestCaseDocumentation'")
  public TestCaseDocumentation findOneByStage(Stage stage) {
    return testCaseDocumentationRepository.findOneByStage(stage);
  }


}
