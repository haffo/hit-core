package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.Transaction;
import gov.nist.hit.core.repo.TransactionRepository;
import gov.nist.hit.core.service.TransactionService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
 public class TransactionServiceImpl implements TransactionService {

  private static final long serialVersionUID = 1L;
 
  @Autowired
  protected TransactionRepository transactionRepository;

  //
  @Autowired
  @PersistenceContext(unitName = "base-tool")  
  protected EntityManager entityManager;
  


  @Override
  public String getIncomingMessageByUserIdAndTestStepId(Long userId, Long testStepId) {
    return transactionRepository.getIncomingMessageByUserIdAndTestStepId(userId, testStepId);
  }

  @Override
  public String getOutgoingMessageByUserIdAndTestStepId(Long userId, Long testStepId) {
    return transactionRepository.getOutgoingMessageByUserIdAndTestStepId(userId, testStepId);
  }

  @Override
  public Transaction findOneByUserAndTestStep(Long userId, Long testStepId) {
    return transactionRepository.findOneByUserAndTestStep(userId, testStepId);
  }

  @Override
  public List<Transaction> findAllByUser(Long userId) {
    return transactionRepository.findAllByUser(userId);
  }

  @Override
  public Transaction findOneByTestStepIdAndProperties(Map<String, String> criteria, Long testStepId) {
    String sql = toQuery(criteria);
    sql = sql + " AND tr.testStepId = " + testStepId;
    Query q = entityManager.createNativeQuery(sql, Transaction.class);
    Transaction tr = getSingleResult(q);
    return tr;
  }

  @Override
  public Transaction findOneByProperties(Map<String, String> criteria) {
    String sql = toQuery(criteria);
    Query q = entityManager.createNativeQuery(sql, Transaction.class);
    Transaction tr = getSingleResult(q);
    return tr;
  }

  private String toQuery(Map<String, String> criteria) {
    String sql = "SELECT * FROM transaction tr";
    ArrayList<String> conditions = new ArrayList<>();
    Iterator<Entry<String, String>> it = criteria.entrySet().iterator();
    int i = 1;
    while (it.hasNext()) {
      Map.Entry<String, String> pair = it.next();
      String key = pair.getKey();
      String value = pair.getValue();
      String alias = "transaction_config" + i;
      sql +=
          " LEFT OUTER JOIN transaction_config " + alias + " ON tr.id = " + alias
              + ".transaction_id AND " + alias + ".property_key = '" + key + "' AND " + alias
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

  private Transaction getSingleResult(Query query) {
    query.setMaxResults(1);
    List<Transaction> list = query.getResultList();
    if (list == null || list.isEmpty()) {
      return null;
    }

    return list.get(0);
  }


  @Override
  public void delete(List<Transaction> transactions) {
    transactionRepository.delete(transactions);
  }

  @Override
  public void delete(Transaction transaction) {
    transactionRepository.delete(transaction);
  }

  @Override
  public Transaction save(Transaction transaction) {
    return transactionRepository.saveAndFlush(transaction);
  }

  @Override
  public List<Transaction> save(List<Transaction> transactions) {
    return transactionRepository.save(transactions);
  }

  @Override
  public void delete(Long id) {
    transactionRepository.delete(id);
  }

  @Override
  public Transaction findOne(Long id) {
    return transactionRepository.findOne(id);
  }

  @Override
  public List<Transaction> findAllByProperties(Map<String, String> criteria) {
    String sql = toQuery(criteria);
    Query q = entityManager.createNativeQuery(sql, Transaction.class);
    return q.getResultList();
  }


}
