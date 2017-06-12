package gov.nist.hit.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class TestStepValidationReport implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @JsonIgnore

  @ManyToOne(optional = false)
  private TestStep testStep;

  @JsonIgnore
  private Long userId;

  @JsonIgnore

  @Column(columnDefinition = "LONGTEXT")
  private String xml;

  @Column(columnDefinition = "LONGTEXT")
  private String html;

  @Column(columnDefinition = "LONGTEXT")
  private String json;

  @Enumerated(EnumType.STRING)
  private TestResult result = null;

  @Column(columnDefinition = "LONGTEXT")
  private String comments;

  @Temporal(TemporalType.TIMESTAMP)
  private Date dateUpdated;

  public TestStepValidationReport(String content, TestStep testStep, Long accountId) {
    super();
    this.xml = content;
    this.testStep = testStep;
    this.userId = accountId;
  }

  @PrePersist
  protected void onCreate() {
    dateUpdated = new Date();
  }

  @PreUpdate
  protected void onUpdate() {
    dateUpdated = new Date();
  }


  public TestStepValidationReport() {}



  public String getXml() {
    return xml;
  }


  public void setXml(String xml) {
    this.xml = xml;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public TestStep getTestStep() {
    return testStep;
  }

  public void setTestStep(TestStep testStep) {
    this.testStep = testStep;
  }



  public Long getUserId() {
    return userId;
  }


  public void setUserId(Long userId) {
    this.userId = userId;
  }


  public TestResult getResult() {
    return result;
  }


  public void setResult(TestResult result) {
    this.result = result;
  }


  public String getComments() {
    return comments;
  }


  public void setComments(String comments) {
    this.comments = comments;
  }


  public String getHtml() {
    return html;
  }


  public void setHtml(String html) {
    this.html = html;
  }


  public Date getDateUpdated() {
    return dateUpdated;
  }


  public void setDateUpdated(Date dateUpdated) {
    this.dateUpdated = dateUpdated;
  }

  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }



}
