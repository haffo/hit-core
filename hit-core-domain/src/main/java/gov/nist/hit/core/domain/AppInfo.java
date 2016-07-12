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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * @author Harold Affo (NIST)
 * 
 */
@Entity
public class AppInfo implements Serializable {

  private static final long serialVersionUID = 8805967508478985159L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String url;

  private String version;

  private String date;

  private String name;

  private String domain;

  private String header;

  private String adminEmail;

  private String homeTitle;

  @Column(columnDefinition = "TEXT")
  private String messageContentInfo;

  @Column(columnDefinition = "TEXT")
  private String homeContent;

  @Column(columnDefinition = "TEXT")
  private String profileInfo;

  @Column(columnDefinition = "TEXT")
  private String valueSetCopyright;


  @Column(columnDefinition = "TEXT")
  private String disclaimer;


  @Column(columnDefinition = "TEXT")
  private String confidentiality;


  @Column(columnDefinition = "TEXT")
  private String validationResultInfo;

  @Column(columnDefinition = "TEXT")
  private String acknowledgment;

  private String csrfToken;

  private String rsbVersion;

  private String apiDocsPath;

  private String mailFrom = "hit-testing@nist.gov";

  @Column(columnDefinition = "TEXT")
  private String registrationTitle;

  @Column(columnDefinition = "TEXT")
  private String registrationAgreement;

  @Column(columnDefinition = "TEXT")
  private String registrationSubmittedContent;

  @Column(columnDefinition = "TEXT")
  private String registrationSubmittedTitle;

  @Column(columnDefinition = "TEXT")
  private String registrationAcceptanceTitle;


  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public String getAdminEmail() {
    return adminEmail;
  }

  public void setAdminEmail(String adminEmail) {
    this.adminEmail = adminEmail;
  }


  public String getHomeContent() {
    return homeContent;
  }

  public void setHomeContent(String homeContent) {
    this.homeContent = homeContent;
  }

  public String getProfileInfo() {
    return profileInfo;
  }

  public void setProfileInfo(String profileInfo) {
    this.profileInfo = profileInfo;
  }


  public String getValueSetCopyright() {
    return valueSetCopyright;
  }

  public void setValueSetCopyright(String valueSetCopyright) {
    this.valueSetCopyright = valueSetCopyright;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }



  public String getDisclaimer() {
    return disclaimer;
  }

  public void setDisclaimer(String disclaimer) {
    this.disclaimer = disclaimer;
  }

  public String getConfidentiality() {
    return confidentiality;
  }

  public void setConfidentiality(String confidentiality) {
    this.confidentiality = confidentiality;
  }


  public String getValidationResultInfo() {
    return validationResultInfo;
  }

  public void setValidationResultInfo(String validationResultInfo) {
    this.validationResultInfo = validationResultInfo;
  }

  public String getAcknowledgment() {
    return acknowledgment;
  }

  public void setAcknowledgment(String acknowledgment) {
    this.acknowledgment = acknowledgment;
  }


  public String getHomeTitle() {
    return homeTitle;
  }

  public void setHomeTitle(String homeTitle) {
    this.homeTitle = homeTitle;
  }


  public String getCsrfToken() {
    return csrfToken;
  }

  public void setCsrfToken(String csrfToken) {
    this.csrfToken = csrfToken;
  }



  public String getMessageContentInfo() {
    return messageContentInfo;
  }

  public void setMessageContentInfo(String messageContentInfo) {
    this.messageContentInfo = messageContentInfo;
  }



  public String getRsbVersion() {
    return rsbVersion;
  }

  public void setRsbVersion(String rsbVersion) {
    this.rsbVersion = rsbVersion;
  }



  public String getApiDocsPath() {
    return apiDocsPath;
  }

  public void setApiDocsPath(String apiDocsPath) {
    this.apiDocsPath = apiDocsPath;
  }

  @Override
  public String toString() {
    return "AppInfo [id=" + id + ", url=" + url + ", version=" + version + ", date=" + date
        + ", name=" + name + ", domain=" + domain + ", header=" + header + ", adminEmail="
        + adminEmail + ", homeContent=" + homeContent + ", profileInfo=" + profileInfo + "]";
  }

  public String getMailFrom() {
    return mailFrom;
  }

  public void setMailFrom(String mailFrom) {
    this.mailFrom = mailFrom;
  }

  public String getRegistrationAgreement() {
    return registrationAgreement;
  }

  public void setRegistrationAgreement(String registrationAgreement) {
    this.registrationAgreement = registrationAgreement;
  }

  public String getRegistrationTitle() {
    return registrationTitle;
  }

  public void setRegistrationTitle(String registrationTitle) {
    this.registrationTitle = registrationTitle;
  }

  public String getRegistrationSubmittedContent() {
    return registrationSubmittedContent;
  }

  public void setRegistrationSubmittedContent(String registrationSubmittedContent) {
    this.registrationSubmittedContent = registrationSubmittedContent;
  }

  public String getRegistrationSubmittedTitle() {
    return registrationSubmittedTitle;
  }

  public void setRegistrationSubmittedTitle(String registrationSubmittedTitle) {
    this.registrationSubmittedTitle = registrationSubmittedTitle;
  }

  public String getRegistrationAcceptanceTitle() {
    return registrationAcceptanceTitle;
  }

  public void setRegistrationAcceptanceTitle(String registrationAcceptanceTitle) {
    this.registrationAcceptanceTitle = registrationAcceptanceTitle;
  }



}
