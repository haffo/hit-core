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
  @Column(name = "PARTICIPANT_EMAILS", nullable = true)
  private Set<String> participantEmails = new HashSet<String>();


  private String rsbVersion;

  private String igVersion;

  private String owner;



  public Domain() {
    this.owner = this.authorUsername;
  }

  public Domain(String name, String domain) {
    this.name = name;
    this.domain = domain;
    this.owner = this.authorUsername;
  }


  public Domain(String name, String domain, TestScope scope, String authorUsername,
      Set<String> participantEmails) {
    this(name, domain);
    this.scope = scope;
    this.authorUsername = authorUsername;
    this.participantEmails = participantEmails;
    this.owner = authorUsername;
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

  public Set<String> getParticipantEmails() {
    return participantEmails;
  }

  public void setParticipantEmails(Set<String> participantEmails) {
    this.participantEmails = participantEmails;
  }

  public Long getId() {
    return id;
  }



  public String getRsbVersion() {
    return rsbVersion;
  }

  public void setRsbVersion(String rsbVersion) {
    this.rsbVersion = rsbVersion;
  }



  public String getIgVersion() {
    return igVersion;
  }

  public void setIgVersion(String igVersion) {
    this.igVersion = igVersion;
  }



  public String getOwner() {
    owner = authorUsername;
    return owner;
  }


  public void merge(Domain source) {
    this.homeTitle = source.homeTitle;
    this.homeContent = source.homeContent;
    this.disabled = source.disabled;
    this.domain = source.domain;
    this.messageContentInfo = source.messageContentInfo;
    this.name = source.name;
    this.participantEmails = source.participantEmails;
    this.profileInfo = source.profileInfo;
    this.validationResultInfo = source.validationResultInfo;
    this.valueSetCopyright = source.valueSetCopyright;
    this.rsbVersion = source.rsbVersion;
    this.igVersion = source.igVersion;
    this.owner = this.authorUsername;
  }



}
