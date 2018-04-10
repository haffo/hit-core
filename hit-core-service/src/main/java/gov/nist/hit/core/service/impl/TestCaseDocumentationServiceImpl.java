package gov.nist.hit.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nist.hit.core.domain.TestCaseDocumentation;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.repo.TestCaseDocumentationRepository;
import gov.nist.hit.core.service.TestCaseDocumentationService;

@Service
public class TestCaseDocumentationServiceImpl implements TestCaseDocumentationService {

  @Autowired
  private TestCaseDocumentationRepository testCaseDocumentationRepository;

  @Override
  public TestCaseDocumentation findOneByStageAndDomainAndScope(TestingStage stage, String domain,
      TestScope scope) {
    return testCaseDocumentationRepository.findOneByStageAndDomainAndScope(stage, domain, scope);
  }


  @Override
  public TestCaseDocumentation findOneByStageAndDomainAndAuthorAndScope(TestingStage stage,
      String domain, String username, TestScope scope) {
    return testCaseDocumentationRepository.findOneByStageAndDomainAndAuthorAndScope(stage, domain,
        username, scope);
  }



}
