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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @(#) UserTransaction.java
 */
@Entity
@ApiModel(value = "Transaction", description = "Data Model representing a transaction")
public class Transaction implements java.io.Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;

  @ApiModelProperty(required = true, value = "message sent by the sut (system under test)")
  @NotNull
  @Column(nullable = false, columnDefinition = "LONGTEXT")
  protected String incoming;

  @ApiModelProperty(required = true, value = "message sent to the sut (system under test)")
  @NotNull
  @Column(nullable = false, columnDefinition = "LONGTEXT")
  protected String outgoing;

  @ApiModelProperty(required = true, value = "id of the test step")
  protected Long testStepId;

  @ApiModelProperty(required = true, value = "user executing the transaction")
  @JsonIgnore
  protected Long userId;

  @ApiModelProperty(required = true, value = "list of properties of the transaction")
  @JsonIgnore
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "transaction_config", joinColumns = @JoinColumn(name = "transaction_id"))
  @MapKeyColumn(name = "property_key")
  @Column(name = "property_value")
  protected Map<String, String> properties = new HashMap<String, String>();

  @ApiModelProperty(required = false, value = "id of the response message id of the transaction")
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


  @Override
  public String toString() {
    return "Transaction [id=" + id + ", incoming=" + incoming + ", outgoing=" + outgoing
        + ", testStepId=" + testStepId + ", user=" + userId + ", properties=" + properties
        + ", responseMessageId=" + responseMessageId + "]";
  }



}
