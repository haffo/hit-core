package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.KeyValuePair;
import gov.nist.hit.core.domain.TestingType;
import gov.nist.hit.core.domain.TransportConfig;
import gov.nist.hit.core.repo.TransportConfigRepository;
import gov.nist.hit.core.service.TransportConfigService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
public class TransportConfigServiceImpl implements TransportConfigService {

  @Autowired
  protected TransportConfigRepository transportConfigRepository;



  @Autowired
  @PersistenceContext(unitName = "base-tool")   
  protected EntityManager entityManager;

  @Override
  public TransportConfig create(String protocol, String domain) {
    TransportConfig config = new TransportConfig(protocol, domain);
    return config;
  }


  /**
   * 
   * @param pair
   * @param config
   * @return
   */
  @Override
  public TransportConfig set(KeyValuePair pair, TestingType type, TransportConfig config) {
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
  public TransportConfig set(List<KeyValuePair> pairs, TestingType type, TransportConfig config) {
    if (pairs.isEmpty())
      return config;
    for (KeyValuePair pair : pairs) {
      set(pair, type, config);
    }
    return config;
  }

  @Override
  public TransportConfig save(TransportConfig config) {
    List<TransportConfig> configs = transportConfigRepository.findAllByUserAndProtocolAndDomain(config.getUserId(), config.getProtocol(), config.getDomain());
    List<TransportConfig> toBeDeleted = new ArrayList<>();
    if(config.getId()==null) {
      if (configs.size() > 0) {
        for (TransportConfig transportConfig : configs) {
          toBeDeleted.add(transportConfig);
        }
      }
    } else {
      if (configs.size() > 1) {
        for (TransportConfig transportConfig : configs) {
          if (transportConfig.getId() != config.getId()) {
            toBeDeleted.add(transportConfig);
          }
        }
      }
    }
    if(toBeDeleted.size()>0){
      delete(toBeDeleted);
    }
    return transportConfigRepository.saveAndFlush(config);
  }


  @Override
  public TransportConfig findOne(Long id) {
    return transportConfigRepository.findOne(id);
  }


  @Override
  public TransportConfig findOneByUserAndProtocolAndDomain(Long userId, String protocol,
      String domain) {
    List<TransportConfig> configs = transportConfigRepository.findAllByUserAndProtocolAndDomain(userId,protocol,domain);
    if(configs.size()>1){
      for(int i=1;i<configs.size();i++){
        transportConfigRepository.delete(configs.get(i));
        configs.remove(i);
      }
    }
    List<TransportConfig> configs2Test = transportConfigRepository.findAllByUserAndProtocolAndDomain(userId,protocol,domain);
    if(configs.size()==1) {
      return configs.get(0);
    }
    return null;
  }


  private String toInitiatorQuery(Map<String, String> criteria, TestingType type, String protocol) {
    String table =
        type == TestingType.SUT_INITIATOR ? "sut_initiator_config" : "ta_initiator_config";
    String sql = "SELECT * FROM transportconfig tr";
    ArrayList<String> conditions = new ArrayList<>();
    Iterator<Entry<String, String>> it = criteria.entrySet().iterator();
    int i = 1;
    while (it.hasNext()) {
      Map.Entry<String, String> pair = it.next();
      String key = pair.getKey();
      String value = pair.getValue();
      String alias = table + i;
      sql +=
          " LEFT OUTER JOIN " + table + " " + alias + " ON tr.id = " + alias
              + ".transport_config_id AND " + alias + ".property_key = '" + key + "' AND " + alias
              + ".property_value = '" + value + "'";
      conditions.add(alias + ".property_key is not null");
      i++;
    }
    if(conditions.size()>1) {
      sql += " WHERE ";
      for (int j = 0; j < conditions.size(); j++) {
        if (j > 0) {
          sql += " AND ";
        }
        sql += conditions.get(j);
      }
      sql += " AND tr.protocol = '" + protocol + "'";
    } else {
      sql += " WHERE tr.protocol = '" + protocol + "'";
    }
    return sql;
  }

  @Override
  public TransportConfig findOneByPropertiesAndProtocol(Map<String, String> criteria,
      TestingType type, String protocol) {
    String sql = toInitiatorQuery(criteria, type, protocol);
    Query q = entityManager.createNativeQuery(sql, TransportConfig.class);
    TransportConfig tr = getSingleResult(q);
    return tr;
  }


  @Override
  public List<TransportConfig> findAllByUser(@Param("userId") Long userId) {
    return transportConfigRepository.findAllByUser(userId);
  }


  private TransportConfig getSingleResult(Query query) {
    query.setMaxResults(1);
    List<TransportConfig> list = query.getResultList();
    if (list == null || list.isEmpty()) {
      return null;
    }

    return list.get(0);
  }


  public TransportConfigRepository getTransportConfigRepository() {
    return transportConfigRepository;
  }

  public void setTransportConfigRepository(TransportConfigRepository transportConfigRepository) {
    this.transportConfigRepository = transportConfigRepository;
  }


  @Override
  public void delete(List<TransportConfig> configs) {
    transportConfigRepository.delete(configs);
  }



}
