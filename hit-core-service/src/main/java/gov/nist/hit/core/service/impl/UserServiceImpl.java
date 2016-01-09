package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.TestStepTestingType;
import gov.nist.hit.core.domain.TransportConfig;
import gov.nist.hit.core.domain.User;
import gov.nist.hit.core.repo.UserRepository;
import gov.nist.hit.core.service.TransportConfigService;
import gov.nist.hit.core.service.UserService;

import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  protected UserRepository userRepository;

  //
  @Autowired
  protected EntityManager entityManager;

  @Autowired
  protected TransportConfigService transportConfigService;

  @Override
  public boolean exitBySutInitiatorPropertiesAndProtocol(Map<String, String> criteria,
      String protocol) {
    TransportConfig config =
        transportConfigService.findOneByPropertiesAndProtocol(criteria,
            TestStepTestingType.SUT_INITIATOR, protocol);
    return config != null && config.getUser() != null;
  }

  @Override
  public User findOneBySutInitiatorConfigAndProtocol(Map<String, String> criteria, String protocol) {
    TransportConfig config =
        transportConfigService.findOneByPropertiesAndProtocol(criteria,
            TestStepTestingType.SUT_INITIATOR, protocol);
    return config != null ? config.getUser() : null;
  }


  @Override
  public void delete(User user) {
    userRepository.delete(user);
  }

  @Override
  public void delete(Long id) {
    userRepository.delete(id);
  }

  @Override
  public User save(User user) {
    return userRepository.saveAndFlush(user);
  }

  @Override
  public User findOne(Long id) {
    return userRepository.findOne(id);
  }



}
