package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.CFTestInstance;

import java.util.List;

public interface TestObjectService {

  public CFTestInstance findOne(Long id);

  public List<CFTestInstance> findAllAsRoot();

}
