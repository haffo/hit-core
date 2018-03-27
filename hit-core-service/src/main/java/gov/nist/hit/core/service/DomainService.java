package gov.nist.hit.core.service;

import java.util.List;

import gov.nist.hit.core.domain.Domain;

public interface DomainService {

  public Domain findOneByKey(String key);

  public void save(Domain domain);

  List<Domain> findShortAll();

}
