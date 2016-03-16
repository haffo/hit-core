package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.KeyValuePair;
import gov.nist.hit.core.domain.TestingType;
import gov.nist.hit.core.domain.TransportConfig;

import java.util.List;
import java.util.Map;

public interface TransportConfigService {

  TransportConfig create(String protocole, String domain);

  TransportConfig findOne(Long id);

  TransportConfig findOneByUserAndProtocolAndDomain(Long userId, String protocol, String domain);

  TransportConfig save(TransportConfig config);

  TransportConfig set(KeyValuePair pair, TestingType type, TransportConfig config);

  TransportConfig set(List<KeyValuePair> pairs, TestingType type, TransportConfig config);

  TransportConfig findOneByPropertiesAndProtocol(Map<String, String> criteria,
      TestingType type, String protocol);

  List<TransportConfig> findAllByUser(Long userId);

  void delete(List<TransportConfig> configs);

}
