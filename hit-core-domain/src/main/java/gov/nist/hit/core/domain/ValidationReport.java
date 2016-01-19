package gov.nist.hit.core.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ValidationReport implements Serializable {
  
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
  
  
  public ValidationReport(String xml,TestStep testStep, User user) {
    super();
    this.xml = xml;
    this.testStep  =testStep;
    this.user = user;
  }

 
  public ValidationReport() {
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

//  public byte[] getPdf() {
//    return pdf;
//  }
//
//  public void setPdf(byte[] pdf) {
//    this.pdf = pdf;
//  }
//  
  
  
}
