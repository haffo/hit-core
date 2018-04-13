package gov.nist.hit.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nist.hit.core.domain.Domain;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.repo.DomainRepository;
import gov.nist.hit.core.service.DomainService;

@Service
public class DomainServiceImpl implements DomainService {

  @Autowired
  protected DomainRepository domainEntryRepo;

  @Override
  public Domain findOneByKey(String key) {
    // TODO Auto-generated method stub
    return domainEntryRepo.findOneByKey(key);
  }

  @Override
  public void save(Domain entry) {
    domainEntryRepo.saveAndFlush(entry);
  }


  @Override
  public List<Domain> findShortAll(boolean disabled) {
    // TODO Auto-generated method stub
    return domainEntryRepo.findShortAll(disabled);
  }

  @Override
  public List<Domain> findAllByScopeAndAuthorname(TestScope scope, String authorUsername) {
    // TODO Auto-generated method stub
    return domainEntryRepo.findShortAllByScopeAndAuthorname(scope, authorUsername);
  }


  @Override
  public Domain findOne(Long id) {
    // TODO Auto-generated method stub
    return domainEntryRepo.findOne(id);
  }

  @Override
  public void delete(Domain domain) {
    // TODO Auto-generated method stub
    domainEntryRepo.delete(domain);
  }



}
