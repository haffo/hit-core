/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */
package gov.nist.hit.core.domain;

public class TableModel {

  private String tableNumber;
  private String tableName;
  private int tableId;

  public TableModel() {}

  public TableModel(int tblId, String tblNum, String tblname) {
    this.tableId = tblId;
    this.tableNumber = tblNum;
    this.tableName = tblname;
  }

  public void setTableId(int tblId) {
    this.tableId = tblId;
  }

  public void setTableName(String tblname) {
    this.tableName = tblname;
  }

  public void setTableNumber(String tblNum) {
    this.tableNumber = tblNum;
  }

  public int getTableId() {
    return tableId;
  }

  public String getTableNumber() {
    return tableNumber;
  }

  public String getTableName() {
    return tableName;
  }
}
