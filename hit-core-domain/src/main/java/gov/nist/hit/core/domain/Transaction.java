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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @(#) UserTransaction.java
 */
@Entity
public class Transaction implements java.io.Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;
 

  @NotNull
  @Column(nullable = false, columnDefinition = "LONGTEXT")
  protected String incoming;

  @NotNull
  @Column(nullable = false, columnDefinition = "LONGTEXT")
  protected String outgoing;
  
  @JsonIgnore
  @OneToOne(cascade = CascadeType.DETACH, optional = true, fetch = FetchType.LAZY)
  protected TestStep testStep;
  
  @JsonIgnore
  @OneToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
  protected User user; 
  
  @JsonIgnore
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name="TRANSACTION_CONFIG", joinColumns=@JoinColumn(name="TRANSACTION_ID"))
  @MapKeyColumn(name = "PROPERTY_KEY")
  @Column(name = "PROPERTY_VALUE")
  protected Map<String, String> properties = new HashMap<String, String>();
  
  @JsonIgnore
  @Column(nullable = true)
  protected Long responseMessageId;
  
 
  public Transaction() {
    super();
   }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Transaction(String incoming, String outgoing) {
    this.incoming = incoming;
    this.outgoing = outgoing;
  }

  public String getIncoming() {
    return incoming;
  }

  public void setIncoming(String incoming) {
    this.incoming = incoming;
  }

  public String getOutgoing() {
    return outgoing;
  }

  public void setOutgoing(String outgoing) {
    this.outgoing = outgoing;
  }


  public void close() {
    clear();
   }

  private void clear() {
    this.incoming = null;
    this.outgoing = null;
  }

  public void init() {
    clear();
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

 
  public Map<String, String> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, String> properties) {
    this.properties = properties;
  }

  public Long getResponseMessageId() {
    return responseMessageId;
  }

  public void setResponseMessageId(Long responseMessageId) {
    this.responseMessageId = responseMessageId;
  }

  public boolean matches(String key, String value) {
    if(properties != null){
      return key != null && properties.containsKey(key)
        && properties.get(key).equals(value);
    }
    return false;
  }
 
  public boolean matches(Map<String, String> criteria) {
     if (!criteria.isEmpty()) {
      for(String key: criteria.keySet()){
        if (!matches(key, criteria.get(key))) {
          return false;
        } 
      }
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return "Transaction [id=" + id + ", incoming=" + incoming
        + ", outgoing=" + outgoing + ", testStep=" + testStep + ", user=" + user + ", properties="
        + properties + ", responseMessageId=" + responseMessageId + "]";
  }

  
  
  

}
