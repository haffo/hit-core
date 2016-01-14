package gov.nist.hit.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class MessageValidationResult implements Serializable {
  
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
  
  @Column(columnDefinition = "TEXT")
  private String json; 
  
  @Column(columnDefinition = "TEXT")
  private String html;
  
  
  public MessageValidationResult(String json, String html,TestStep testStep, User user) {
    super();
    this.json = json;
    this.html = html;
    this.testStep  =testStep;
    this.user = user;
  }

  public MessageValidationResult(String json, String html) {
    super();
    this.json = json;
    this.html = html;
  }
 
  public MessageValidationResult() {
     // TODO Auto-generated constructor stub
  }

  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }

  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
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
  
  
  
}
