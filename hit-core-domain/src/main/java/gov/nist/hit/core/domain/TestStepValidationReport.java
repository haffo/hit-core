package gov.nist.hit.core.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class TestStepValidationReport implements Serializable {
  
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id; 
  
  @JsonIgnore
  @ManyToOne(optional=false)
  private TestStep testStep;
    
  @JsonIgnore
  @ManyToOne(optional=false)
  private User user; 
  
  @JsonIgnore
  @Column(columnDefinition = "TEXT")
  private String xml;   
 
  @Transient
  private String html;  
  
  
  @Enumerated(EnumType.STRING)
  private TestResult result = null;
  
  @Column(columnDefinition = "TEXT")
  private String comments;
  
  public TestStepValidationReport(String content,TestStep testStep, User user) {
    super();
    this.xml = content;
    this.testStep  =testStep;
    this.user = user;
  }

 
  public TestStepValidationReport() {
   }

   
  


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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
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

  
  
  
}
