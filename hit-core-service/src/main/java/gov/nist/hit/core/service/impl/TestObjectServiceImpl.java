package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.CFTestInstance;
import gov.nist.hit.core.repo.CFTestInstanceRepository;
import gov.nist.hit.core.service.TestObjectService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestObjectServiceImpl implements TestObjectService {

  @Autowired
  private CFTestInstanceRepository testObjectRepository;

  @Override
  public CFTestInstance findOne(Long id) {
    return testObjectRepository.findOne(id);
  }

  @Override
  // @Cacheable(value = "HitCache", key = "'TestObjects'")
  public List<CFTestInstance> findAllAsRoot() {
    return testObjectRepository.findAllAsRoot();
  }

}
