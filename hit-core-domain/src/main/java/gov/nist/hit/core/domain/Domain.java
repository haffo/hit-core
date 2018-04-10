package gov.nist.hit.core.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Domain implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @Column(unique = true, nullable = false)
  private String value;

  private String name;
  private String homeTitle;
  private boolean disabled = false;

  @Column(columnDefinition = "TEXT")
  private String messageContentInfo;

  @Column(columnDefinition = "TEXT")
  private String homeContent;

  @Column(columnDefinition = "TEXT")
  private String profileInfo;

  @Column(columnDefinition = "TEXT")
  private String valueSetCopyright;

  @Column(columnDefinition = "TEXT")
  private String validationResultInfo;

  @ElementCollection(fetch = FetchType.EAGER)
  @Column(name = "OWNER_EMAILS", nullable = false)
  @NotNull
  private Set<String> ownerEmails = new HashSet<String>();


  public Domain() {}

  public Domain(String name, String value) {
    this.name = name;
    this.value = value;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getHomeContent() {
    return homeContent;
  }

  public void setHomeContent(String homeContent) {
    this.homeContent = homeContent;
  }

  public String getHomeTitle() {
    return homeTitle;
  }

  public void setHomeTitle(String homeTitle) {
    this.homeTitle = homeTitle;
  }

  public String getMessageContentInfo() {
    return messageContentInfo;
  }

  public void setMessageContentInfo(String messageContentInfo) {
    this.messageContentInfo = messageContentInfo;
  }

  public String getProfileInfo() {
    return profileInfo;
  }

  public void setProfileInfo(String profileInfo) {
    this.profileInfo = profileInfo;
  }

  public boolean isDisabled() {
    return disabled;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  public String getValueSetCopyright() {
    return valueSetCopyright;
  }

  public void setValueSetCopyright(String valueSetCopyright) {
    this.valueSetCopyright = valueSetCopyright;
  }

  public String getValidationResultInfo() {
    return validationResultInfo;
  }

  public void setValidationResultInfo(String validationResultInfo) {
    this.validationResultInfo = validationResultInfo;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Set<String> getOwnerEmails() {
    return ownerEmails;
  }

  public void setOwnerEmails(Set<String> ownerEmails) {
    this.ownerEmails = ownerEmails;
  }



}
