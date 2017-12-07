package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.CFTestStep;

public interface CFTestStepService {

  public CFTestStep findOne(Long id);

  public void delete(Long id);

  public void delete(CFTestStep testStep);

  public void save(CFTestStep testStep);


}
