package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.KeyValuePair;
import gov.nist.hit.core.domain.TestStepTestingType;
import gov.nist.hit.core.domain.TransportConfig;
import gov.nist.hit.core.repo.TransportConfigRepository;
import gov.nist.hit.core.service.TransportConfigService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransportConfigServiceImpl implements TransportConfigService {

  @Autowired
  protected TransportConfigRepository transportConfigRepository;

  @Override
  public TransportConfig create(String protocol) {
    TransportConfig config = new TransportConfig(protocol);
    return config;
  }


  @Override
  public TransportConfig findOneByCriteria(KeyValuePair criteria, TestStepTestingType type) {
    return transportConfigRepository.findOneByCriteria(criteria, type);
  }

  @Override
  public TransportConfig findOneByOneMultipleCriteria(List<KeyValuePair> criteria,
      TestStepTestingType type) {
    return transportConfigRepository.findOneByOneMultipleCriteria(criteria, type);
  }

  // /**
  // *
  // * @param pairs
  // * @return
  // */
  // @Override
  // public TransportConfig set(List<KeyValuePair> pairs, TestStepTestingType type) {
  // TransportConfig config = create();
  // if (pairs.isEmpty())
  // return config;
  // for (KeyValuePair pair : pairs) {
  // set(pair, type, config);
  // }
  // return config;
  // }

  /**
   * 
   * @param pair
   * @param config
   * @return
   */
  @Override
  public TransportConfig set(KeyValuePair pair, TestStepTestingType type, TransportConfig config) {
    config.getConfigInfo(type).put(pair.getKey(), pair.getValue());
    return config;
  }

  /**
   * 
   * @param pairs
   * @param config
   * @return
   */
  @Override
  public TransportConfig set(List<KeyValuePair> pairs, TestStepTestingType type,
      TransportConfig config) {
    if (pairs.isEmpty())
      return config;
    for (KeyValuePair pair : pairs) {
      set(pair, type, config);
    }
    return config;
  }

  @Override
  public TransportConfig save(TransportConfig config) {
    return transportConfigRepository.saveAndFlush(config);
  }


  @Override
  public TransportConfig findOne(Long id) {
    return transportConfigRepository.findOne(id);
  }


  @Override
  public TransportConfig findOneByUserAndProtocol(Long userId, String protocol) {
    return transportConfigRepository.findOneByUserAndProtocol(userId, protocol);
  }


  public TransportConfigRepository getTransportConfigRepository() {
    return transportConfigRepository;
  }

  public void setTransportConfigRepository(TransportConfigRepository transportConfigRepository) {
    this.transportConfigRepository = transportConfigRepository;
  }



}
