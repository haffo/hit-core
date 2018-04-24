package gov.nist.hit.core.service.impl;

import java.util.ArrayList;
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
  protected DomainRepository domainRepo;

  @Override
  public Domain findOneByKey(String key) {
    // TODO Auto-generated method stub
    return domainRepo.findOneByKey(key);
  }

  @Override
  public void save(Domain entry) {
    domainRepo.saveAndFlush(entry);
  }


  // @Override
  // public List<Domain> findShortAll(boolean disabled) {
  // // TODO Auto-generated method stub
  // return domainRepo.findShortAll(disabled);
  // }

  @Override
  public List<Domain> findShortAllByScopeAndAuthorname(TestScope scope, String authorUsername) {
    // TODO Auto-generated method stub
    return domainRepo.findShortAllByScopeAndAuthorname(scope, authorUsername);
  }


  @Override
  public Domain findOne(Long id) {
    // TODO Auto-generated method stub
    return domainRepo.findOne(id);
  }

  @Override
  public void delete(Domain domain) {
    // TODO Auto-generated method stub
    domainRepo.delete(domain);
  }

  @Override
  public List<Domain> findShortAllWithGlobalOrAuthornameOrParticipantEmail(String authorUsername,
      String participantEmail) {
    // TODO Auto-generated method stub
    List<Domain> all = domainRepo.findAll();
    List<Domain> results = null;
    if (all != null && !all.isEmpty()) {
      results = new ArrayList<Domain>();
      for (Domain dom : all) {
        if (dom.getScope().equals(TestScope.GLOBAL)
            || dom.getAuthorUsername().equals(authorUsername)
            || dom.getParticipantEmails().contains(participantEmail)) {
          results.add(dom);
        }
      }
    }
    return results;
  }

  @Override
  public List<Domain> findShortAllGlobalDomains() {
    return domainRepo.findAllShortWithGlobal(false);
  }

  @Override
  public void deletePreloaded() {
    domainRepo.deletePreloaded();

  }


}
