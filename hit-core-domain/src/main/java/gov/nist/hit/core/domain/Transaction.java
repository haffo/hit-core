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

  @Column(nullable = true)
  @Enumerated(EnumType.STRING)
  protected TransactionStatus status;

  @Column(nullable = true, columnDefinition = "LONGTEXT")
  protected String incoming;

  @Column(nullable = true, columnDefinition = "LONGTEXT")
  protected String outgoing;
  
  @JsonIgnore
  @OneToOne(cascade = CascadeType.DETACH, optional = false, fetch = FetchType.LAZY)
  protected TestStep testStep;
  
  @JsonIgnore
  @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
  protected User user; 
  
  @ElementCollection
  @CollectionTable(name = "TRANSACTION_CONFIG")
  @MapKeyColumn(name = "PROPERTY_KEY")
  @Column(name = "PROPERTY_VALUE")
  protected Map<String, String> config;
  
  @Column(nullable = true)
  protected Long responseMessageId;
  
 
  public Transaction() {
    super();
    status = TransactionStatus.CLOSE;
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

  public TransactionStatus getStatus() {
    return status;
  }

  public void setStatus(TransactionStatus status) {
    this.status = status;
  }

  public void close() {
    clear();
    this.status = TransactionStatus.CLOSE;
  }

  private void clear() {
    this.incoming = null;
    this.outgoing = null;
  }

  public void init() {
    clear();
    this.status = TransactionStatus.OPEN;
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

  public Map<String, String> getConfig() {
    return config;
  }

  public void setConfig(Map<String, String> config) {
    this.config = config;
  }
  
  public Long getResponseMessageId() {
    return responseMessageId;
  }

  public void setResponseMessageId(Long responseMessageId) {
    this.responseMessageId = responseMessageId;
  }

  public boolean matches(KeyValuePair pair, TestStepTestingType type) {
    if(config != null){return pair.getKey() != null && config.containsKey(pair.getKey())
        && config.get(pair.getKey()).equals(pair.getValue());
    }
    return false;
  }
 
  
  
  

}
