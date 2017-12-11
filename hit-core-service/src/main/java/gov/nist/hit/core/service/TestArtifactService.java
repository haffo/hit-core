package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.TestArtifact;

public interface TestArtifactService {

  public TestArtifact findOne(Long id);

  public void delete(TestArtifact testArtifact);

  void save(TestArtifact testArtifact);


}
