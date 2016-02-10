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

import java.util.ArrayList;
import java.util.List;

public class TestCaseDocument {
  protected Long id;
  protected String type;
  protected String title;
  protected String mcPath; // message content 
  protected String tdsPath; // test data specification
  protected String tpPath; // test procedure
  protected String tpsPath; // test plan summary
  protected String jdPath; // juror document 
  protected String tsPath; // test story 
  protected String csPath; // constraint 
  protected String format;



  protected List<TestCaseDocument> children = new ArrayList<TestCaseDocument>();

  public TestCaseDocument() {
    super();
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getMcPath() {
    return mcPath;
  }

  public void setMcPath(String mcPath) {
    this.mcPath = mcPath;
  }

  public String getTdsPath() {
    return tdsPath;
  }

  public void setTdsPath(String tdsPath) {
    this.tdsPath = tdsPath;
  }

  public String getTsPath() {
    return tsPath;
  }

  public void setTsPath(String tsPath) {
    this.tsPath = tsPath;
  }

  public String getCsPath() {
    return csPath;
  }

  public void setCsPath(String csPath) {
    this.csPath = csPath;
  }

  public String getTpPath() {
    return tpPath;
  }

  public void setTpPath(String tpPath) {
    this.tpPath = tpPath;
  }

  public String getJdPath() {
    return jdPath;
  }

  public void setJdPath(String jdPath) {
    this.jdPath = jdPath;
  }

  public List<TestCaseDocument> getChildren() {
    return children;
  }

  public void setChildren(List<TestCaseDocument> children) {
    this.children = children;
  }
 
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public String getTpsPath() {
    return tpsPath;
  }

  public void setTpsPath(String tpsPath) {
    this.tpsPath = tpsPath;
  }

  
   

  

}
