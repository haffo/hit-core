package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.TestObject;
import gov.nist.hit.core.repo.TestObjectRepository;
import gov.nist.hit.core.service.TestObjectService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TestObjectServiceImpl implements TestObjectService {

  @Autowired
  private TestObjectRepository testObjectRepository;

  @Override
  public TestObject findOne(Long id) {
    return testObjectRepository.findOne(id);
  }

  @Override
  @Cacheable(value = "testCaseCache", key = "'TestObjects'")
  public List<TestObject> findAllAsRoot() {
    return testObjectRepository.findAllAsRoot();
  }

}
