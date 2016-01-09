package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.User;

import java.util.Map;


public interface UserService {

  boolean exitBySutInitiatorPropertiesAndProtocol(Map<String, String> criteria, String protocol);

  User findOneBySutInitiatorConfigAndProtocol(Map<String, String> criteria, String protocol);

  void delete(User user);

  void delete(Long id);

  User save(User user);

  User findOne(Long id);


}
