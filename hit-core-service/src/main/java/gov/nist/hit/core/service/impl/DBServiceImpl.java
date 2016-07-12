package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.service.DBService;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DBServiceImpl implements DBService {

  private final static Logger logger = Logger.getLogger(DBServiceImpl.class);

  @Autowired
  @PersistenceContext(unitName = "base-tool")
  protected EntityManager entityManager;

  @SuppressWarnings("unchecked")
  @Override
  @Transactional
  public void clearDB() {
    logger.info("clearing the db");
    String dropConstraintSql = "ALTER TABLE %s DROP FOREIGN KEY %s;";
    String deleteSqlTemplate = "DELETE FROM %s;";
    String tableByForeignKeySql =
        "SELECT ta.table_name, co.constraint_name FROM INFORMATION_SCHEMA.TABLES ta, INFORMATION_SCHEMA.CONSTRAINTS co where ta.table_name = co.table_name and ta.table_type='TABLE' and ta.table_schema='PUBLIC' and co.table_schema='PUBLIC' and co.constraint_schema = 'PUBLIC' and co.constraint_type = 'REFERENTIAL'";
    Query query = entityManager.createNativeQuery(tableByForeignKeySql);
    List<Object[]> tables = query.getResultList();
    String deleteTableSql = "";
    String dropFkSql = "";
    if (tables != null) {
      for (Object[] table : tables) {
        String tableName = (String) table[0];
        String fk = (String) table[1];
        dropFkSql += String.format(dropConstraintSql, tableName, fk);
        deleteTableSql += String.format(deleteSqlTemplate, tableName);
      }
    }
    Query q1 = entityManager.createNativeQuery(dropFkSql.concat(deleteTableSql));
    q1.executeUpdate();
  }

  public void deleteRecords(String sql) {
    logger.info("clearing the table");
    Query query = entityManager.createNativeQuery(sql);
    query.executeUpdate();
  }


}
