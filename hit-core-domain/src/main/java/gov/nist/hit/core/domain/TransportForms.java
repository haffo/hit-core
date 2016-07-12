package gov.nist.hit.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;


@Entity
public class TransportForms implements Serializable {

  private static final long serialVersionUID = 8805967508478985159L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  private String protocol;

  @NotNull
  private String domain;

  private String description;


  @NotNull
  @Column(columnDefinition = "LONGTEXT")
  private String taInitiatorForm;

  @NotNull
  @Column(columnDefinition = "LONGTEXT")
  private String sutInitiatorForm;


  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public String getTaInitiatorForm() {
    return taInitiatorForm;
  }

  public void setTaInitiatorForm(String taInitiatorForm) {
    this.taInitiatorForm = taInitiatorForm;
  }

  public String getSutInitiatorForm() {
    return sutInitiatorForm;
  }

  public void setSutInitiatorForm(String sutInitiatorForm) {
    this.sutInitiatorForm = sutInitiatorForm;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }



}
