package gov.nist.hit.core.domain.log;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
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
 * Created by mcl1 on 10/21/16.
 *
 * The ValidationLogReport is a minimalist version of the EnhancedReport that contains only the
 * information we need to display the validation logs.
 *
 */
@Entity
public class ValidationLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;


  @NotNull
  @Column(nullable = false)
  private Long userId;

  @NotNull
  @Column(nullable = false)
  private Long testStepId;
  private String messageId;
  private String format;
  private String testingStage;
  private int errorCount = 0;
  private int warningCount = 0;

  @NotNull
  @Column(nullable = false)
  private Date date;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "ErrorCountInSegment",
      joinColumns = @JoinColumn(name = "validation_log_id"))
  @MapKeyColumn(name = "segment_name")
  @Column(name = "number_errors")
  private Map<String, Integer> errorCountInSegment = new HashMap<String, Integer>();

  private boolean validationResult = true;


  public ValidationLog() {

  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public int getErrorCount() {
    return errorCount;
  }

  public void setErrorCount(int errorCount) {
    this.errorCount = errorCount;
  }

  public int getWarningCount() {
    return warningCount;
  }

  public void setWarningCount(int warningCount) {
    this.warningCount = warningCount;
  }

  public String getDate(String format) {
    // Format the date as specified
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
    if (this.date == null) {
      this.date = new Date();
    }
    return simpleDateFormat.format(this.date);
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Map<String, Integer> getErrorCountInSegment() {
    return errorCountInSegment;
  }

  public void setErrorCountInSegment(Map<String, Integer> errorCountInSegment) {
    this.errorCountInSegment = errorCountInSegment;
  }

  public boolean isValidationResult() {
    return validationResult;
  }

  public void setValidationResult(boolean validationResult) {
    this.validationResult = validationResult;
  }

  public String getTestingStage() {
    return testingStage;
  }

  public void setTestingStage(String testingStage) {
    this.testingStage = testingStage;
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



}
