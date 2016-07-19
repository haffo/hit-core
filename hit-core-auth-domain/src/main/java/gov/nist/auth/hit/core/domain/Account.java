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
package gov.nist.auth.hit.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author fdevaulx
 * 
 */
@Entity
@JsonIgnoreProperties(value = "new", ignoreUnknown = true)
public class Account implements Serializable {

  private static final long serialVersionUID = 20130625L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;

  @Transient
  private String registrationPassword;

  @JsonIgnore
  private boolean entityDisabled = false;

  @JsonIgnore
  // TODO remove it and check it doesn't affect REST API security
  private boolean pending = false;

  @Length(max = 100)
  private String accountType;

  @Length(max = 100)
  @Column(unique = true)
  private String username;

  @Email
  @Length(max = 100)
  @Column(unique = true)
  private String email;

  @Length(max = 100)
  @Column(unique = true)
  private String fullName;


  @JsonIgnore
  private boolean guestAccount = true;


  // @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  // @JoinTable(name = "user_configs", joinColumns = {@JoinColumn(name = "user_id")},
  // inverseJoinColumns = {@JoinColumn(name = "confi_id")})
  //
  // @OneToMany(mappedBy = "user")
  // private Set<TransportConfig> configs = new HashSet<TransportConfig>();



  private Boolean signedConfidentialityAgreement = false;

  public Account() {
    this(null);
  }

  /**
   * 
   * @param id
   */
  public Account(Long id) {
    this.setId(id);
  }

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }



  /**
   * @return the entityDisabled
   */
  public boolean isEntityDisabled() {
    return entityDisabled;
  }

  /**
   * @param entityDisabled the entityDisabled to set
   */
  public void setEntityDisabled(boolean entityDisabled) {
    this.entityDisabled = entityDisabled;
  }

  /**
   * @return the registrationPassword
   */
  public String getPassword() {
    return registrationPassword;
  }

  /**
   * 
   * @param registrationPassword
   */
  public void setPassword(String registrationPassword) {
    this.registrationPassword = registrationPassword;
  }

  /**
   * @return the accountType
   */
  public String getAccountType() {
    return accountType;
  }

  /**
   * @param accountType the accountType to set
   */
  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }



  /**
   * @return the signedConfidentialityAgreement
   */
  public Boolean getSignedConfidentialityAgreement() {
    return signedConfidentialityAgreement;
  }

  /**
   * @param signedConfidentialityAgreement the signedConfidentialityAgreement to set
   */
  public void setSignedConfidentialityAgreement(Boolean signedConfidentialityAgreement) {
    this.signedConfidentialityAgreement = signedConfidentialityAgreement;
  }

  /**
   * @return the pending
   */
  public boolean isPending() {
    return pending;
  }

  /**
   * @param pending the pending to set
   */
  public void setPending(boolean pending) {
    this.pending = pending;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getRegistrationPassword() {
    return registrationPassword;
  }

  public void setRegistrationPassword(String registrationPassword) {
    this.registrationPassword = registrationPassword;
  }

  public boolean isGuestAccount() {
    return guestAccount;
  }

  public void setGuestAccount(boolean guestAccount) {
    this.guestAccount = guestAccount;
  }



}
