package gov.nist.auth.hit.core.domain;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

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
 *
 * Created by Harold Affo
 *
 *
 */
@MappedSuperclass
public class LogEntry {

  @NotNull
  @Column(nullable = false)
  protected Long userId;

  @NotNull
  @Column(nullable = false)
  protected Long testStepId;

  @NotNull
  @Column(nullable = false)
  protected Date date;

  protected String testingType;


  // autofill fields
  protected String userFullname;
  protected String testStepName;
  protected String companyName;



  public LogEntry() {

  }



  public Long getTestStepId() {
    return testStepId;
  }

  public void setTestStepId(Long testStepId) {
    this.testStepId = testStepId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Date getDate() {
    return date;
  }



  public String getUserFullname() {
    return userFullname;
  }

  public void setUserFullname(String userFullname) {
    this.userFullname = userFullname;
  }

  public String getTestStepName() {
    return testStepName;
  }

  public void setTestStepName(String testStepName) {
    this.testStepName = testStepName;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }


  public void setDate(Date date) {
    this.date = date;
  }

  public String getTestingType() {
    return testingType;
  }



  public void setTestingType(String testingType) {
    this.testingType = testingType;
  }



}
