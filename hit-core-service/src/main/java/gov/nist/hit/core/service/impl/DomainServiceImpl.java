package gov.nist.hit.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nist.hit.core.domain.Domain;
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
  public List<Domain> findShortAll() {
    // TODO Auto-generated method stub
    return domainEntryRepo.findShortAll();
  }



}
