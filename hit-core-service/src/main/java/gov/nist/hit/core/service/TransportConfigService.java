package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.KeyValuePair;
import gov.nist.hit.core.domain.TestStepTestingType;
import gov.nist.hit.core.domain.TransportConfig;

import java.util.List;

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
  public TransportConfig findOneByUserAndProtocolAndDomain(Long userId, String protocol,
      String domain);


  /**
   * 
   * @return
   */
  public TransportConfig save(TransportConfig config);


  /**
   * 
   * @param criteria
   * @param type
   * @return
   */
  public TransportConfig findOneByCriteria(KeyValuePair criteria, TestStepTestingType type);

  /**
   * 
   * @param criteria
   * @param type
   * @return
   */
  public TransportConfig findOneByOneMultipleCriteria(List<KeyValuePair> criteria,
      TestStepTestingType type);

  // /**
  // *
  // * @param pairs
  // * @return
  // */
  // public TransportConfig set(List<KeyValuePair> pairs, TestStepTestingType type);

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



}