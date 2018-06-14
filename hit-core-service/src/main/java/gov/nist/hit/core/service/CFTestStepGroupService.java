package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.CFTestStepGroup;

public interface CFTestStepGroupService {

  public CFTestStepGroup findOne(Long id);

  public void delete(Long id);

  public void delete(CFTestStepGroup testStep);

  public void save(CFTestStepGroup testStep);


}
