package gov.nist.hit.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nist.hit.core.domain.CFTestStep;
import gov.nist.hit.core.repo.CFTestStepRepository;
import gov.nist.hit.core.service.CFTestStepService;

@Service
public class CFTestStepServiceImpl implements CFTestStepService {

  @Autowired
  private CFTestStepRepository testObjectRepository;

  @Override
  public CFTestStep findOne(Long id) {
    return testObjectRepository.findOne(id);
  }


}
