package gov.nist.hit.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.repo.TestArtifactRepository;
import gov.nist.hit.core.service.TestArtifactService;

@Service
public class TestArtifactServiceImpl implements TestArtifactService {

  @Autowired
  private TestArtifactRepository testArtifactRepository;



  @Override
  public TestArtifact findOne(Long id) {
    return testArtifactRepository.findOne(id);
  }


  @Override
  @Transactional(value = "transactionManager")
  public void delete(TestArtifact testArtifact) {
    testArtifactRepository.delete(testArtifact);
  }

  @Override
  @Transactional(value = "transactionManager")
  public void save(TestArtifact testArtifact) {
    testArtifactRepository.saveAndFlush(testArtifact);
  }


}
