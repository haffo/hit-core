package gov.nist.hit.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import gov.nist.hit.core.domain.Domain;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.repo.DomainRepository;
import gov.nist.hit.core.service.DomainService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.exception.DomainException;
import gov.nist.hit.core.service.exception.PermissionException;

@Service
public class DomainServiceImpl implements DomainService {

  @Autowired
  protected DomainRepository domainRepo;

  @Autowired
  private UserService userService;

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
        if (TestScope.GLOBAL.equals(dom.getScope())
            || authorUsername.equals(dom.getAuthorUsername()) || (dom.getParticipantEmails() != null
                && dom.getParticipantEmails().contains(participantEmail))) {
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

  @Override
  public void hasPermission(String domainKey, Authentication auth) throws Exception {
    String username = auth.getName();
    if (!"app".equalsIgnoreCase(domainKey)) {
      Domain domain = findOneByKey(domainKey);
      if (domain == null) {
        throw new DomainException("Unknown domain " + domainKey);
      }
      if (!domain.getAuthorUsername().equals(auth.getName()) && !userService.isAdmin(username)) {
        throw new PermissionException("You do not have the permission to perform this task");
      }
    } else {
      if (!userService.isAdmin(username)) {
        throw new PermissionException("You do not have the permission to perform this task");
      }
    }
  }


}
