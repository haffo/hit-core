package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.KeyValuePair;
import gov.nist.hit.core.domain.TestStepTestingType;
import gov.nist.hit.core.domain.TransportConfig;

import java.util.List;
import java.util.Map;

public interface TransportConfigService {
  /**
   * 
   * @return
   */
  public TransportConfig create(String protocole);


  /**
   * 
   * @return
   */
  public TransportConfig findOne(Long id);

  /**
   * 
   * @return
   */
  public TransportConfig findOneByUserAndProtocol(Long userId, String protocol);


  /**
   * 
   * @return
   */
  public TransportConfig save(TransportConfig config);


  /**
   * 
   * @param pair
   * @param config
   * @return
   */
  public TransportConfig set(KeyValuePair pair, TestStepTestingType type, TransportConfig config);

  /**
   * 
   * @param pairs
   * @param config
   * @return
   */
  public TransportConfig set(List<KeyValuePair> pairs, TestStepTestingType type,
      TransportConfig config);

  public TransportConfig findOneByPropertiesAndProtocol(Map<String, String> criteria,
      TestStepTestingType type, String protocol);


}
