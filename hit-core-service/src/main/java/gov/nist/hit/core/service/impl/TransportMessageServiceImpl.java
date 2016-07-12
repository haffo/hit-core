package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.TransportMessage;
import gov.nist.hit.core.repo.TransportMessageRepository;
import gov.nist.hit.core.service.TransportMessageService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransportMessageServiceImpl implements TransportMessageService {

  @Autowired
  protected TransportMessageRepository transportMessageRepository;

  @Autowired
  @PersistenceContext(unitName = "base-tool")
   protected EntityManager entityManager;


  @Override
  public TransportMessage findOneByProperties(Map<String, String> criteria) {
    String sql = findOneQuery(criteria);
    Query q = entityManager.createNativeQuery(sql, TransportMessage.class);
    TransportMessage tr = getTransportMessageResult(q);
    return tr;
  }

  private String findOneQuery(Map<String, String> criteria) {
    String sql = "SELECT * FROM TRANSPORTMESSAGE tr";
    ArrayList<String> conditions = new ArrayList<>();
    Iterator<Entry<String, String>> it = criteria.entrySet().iterator();
    int i = 1;
    while (it.hasNext()) {
      Map.Entry<String, String> pair = it.next();
      String key = pair.getKey();
      String value = pair.getValue();
      String alias = "transport_message_config" + i;
      sql +=
          " LEFT OUTER JOIN transport_message_config " + alias + " ON tr.id = " + alias
              + ".transport_message_id AND " + alias + ".property_key = '" + key + "' AND " + alias
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
    }
    return sql;
  }

  private TransportMessage getTransportMessageResult(Query query) {
    query.setMaxResults(1);
    List<TransportMessage> list = query.getResultList();
    if (list == null || list.isEmpty()) {
      return null;
    }

    return list.get(0);
  }



  private Long getMessageIdResult(Query query) {
    query.setMaxResults(1);
    List<Long> list = query.getResultList();
    if (list == null || list.isEmpty()) {
      return null;
    }

    return list.get(0);
  }



  @Override
  public void delete(TransportMessage transaction) {
    transportMessageRepository.delete(transaction);
  }

  @Override
  public TransportMessage save(TransportMessage transaction) {
    return transportMessageRepository.saveAndFlush(transaction);
  }


  @Override
  public void delete(Long id) {
    transportMessageRepository.delete(id);
  }

  @Override
  public TransportMessage findOne(Long id) {
    return transportMessageRepository.findOne(id);
  }

  @Override
  public void delete(List<TransportMessage> confs) {
    transportMessageRepository.delete(confs);
  }

  @Override
  public Long findMessageIdByProperties(Map<String, String> criteria) {
    TransportMessage tm = findOneByProperties(criteria);
    return tm != null ? tm.getMessageId() : null;
  }

  @Override
  public List<TransportMessage> findAllByProperties(Map<String, String> criteria) {
    String sql = findOneQuery(criteria);
    Query q = entityManager.createNativeQuery(sql, TransportMessage.class);
    return q.getResultList();
  }

}
