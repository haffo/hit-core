package gov.nist.hit.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User implements java.io.Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;
 
  @Column(nullable = true)
  protected String accountInfo; 
  
  @Column(nullable = true)
  protected Long responseMessageId;  

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getResponseMessageId() {
    return responseMessageId;
  }

  public void setResponseMessageId(Long responseMessageId) {
    this.responseMessageId = responseMessageId;
  }

  public String getAccountInfo() {
    return accountInfo;
  }

  public void setAccountInfo(String accountInfo) {
    this.accountInfo = accountInfo;
  }

  
}
