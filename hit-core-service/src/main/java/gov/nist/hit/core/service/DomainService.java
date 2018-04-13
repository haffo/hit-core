package gov.nist.hit.core.service;

import java.util.List;

import gov.nist.hit.core.domain.Domain;
import gov.nist.hit.core.domain.TestScope;

public interface DomainService {

  public Domain findOneByKey(String key);

  public void save(Domain domain);

  List<Domain> findShortAll(boolean disabled);

  List<Domain> findAllByScopeAndAuthorname(TestScope scope, String authorname);

  Domain findOne(Long id);

  void delete(Domain domain);


}
