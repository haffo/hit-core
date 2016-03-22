package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.TransportMessage;
import gov.nist.hit.core.domain.UserConfig;

import java.util.List;
import java.util.Map;


public interface UserConfigService {

  Long findUserIdByProperties(Map<String, String> criteria);

  void delete(UserConfig config);

  void delete(Long id);

  UserConfig save(UserConfig config);

  void delete(List<UserConfig> confs);

  UserConfig findOne(Long userConfigId);

  UserConfig findOneByUserId(Long userId);

}
