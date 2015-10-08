package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.TestObject;

import java.util.List;

public interface TestObjectService {

  public TestObject findOne(Long id);

  public List<TestObject> findAllAsRoot();

}
