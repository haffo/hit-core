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
public class Domain extends TestResource implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

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

  public Domain(String name, String domain) {
    this.name = name;
    this.domain = domain;
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
    return domain;
  }

  public void setValue(String domain) {
    this.domain = domain;
  }

  public Set<String> getOwnerEmails() {
    return ownerEmails;
  }

  public void setOwnerEmails(Set<String> ownerEmails) {
    this.ownerEmails = ownerEmails;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void merge(Domain source) {
    this.homeTitle = source.homeTitle;
    this.authorUsername = source.authorUsername;
    this.disabled = source.disabled;
    this.domain = source.domain;
    this.messageContentInfo = source.messageContentInfo;
    this.name = source.name;
    this.ownerEmails = source.ownerEmails;
    this.preloaded = source.preloaded;
    this.profileInfo = source.profileInfo;
    this.scope = source.scope;
    this.validationResultInfo = source.validationResultInfo;
    this.valueSetCopyright = source.valueSetCopyright;
  }



}
