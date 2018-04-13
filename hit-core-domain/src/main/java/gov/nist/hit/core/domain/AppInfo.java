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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import gov.nist.hit.core.Constant;
import io.swagger.annotations.ApiModelProperty;


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

  private String organization;

  private String header;

  private String contactEmail;


  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> adminEmails = new ArrayList<String>();


  @Column(columnDefinition = "TEXT")
  private String disclaimer;

  @Column
  private String disclaimerLink;

  @Column(columnDefinition = "TEXT")
  private String confidentiality;

  @Column
  private String confidentialityLink;

  @Column(columnDefinition = "TEXT")
  private String acknowledgment;


  private String subTitle;


  @Column(columnDefinition = "TEXT")
  private String privacy;

  @Column
  private String privacyLink;

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

  private String uploadMaxSize;
  private String uploadContentTypes; // comma separated supported mime-types and extensions

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "APP_OPTIONS")
  @MapKeyColumn(name = "OPTION_TYPE")
  @Column(name = "OPTION_VALUE")
  private Map<String, String> options = new HashMap<String, String>();


  @JsonIgnoreProperties(value = {"messageContentInfo", "homeContent", "profileInfo",
      "valueSetCopyright", "validationResultInfo", "adminEmails"})
  @ApiModelProperty(required = false, value = "test steps of the test step group")
  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true)
  @JoinTable(name = "AppInfo_domains", joinColumns = {@JoinColumn(name = "appInfo_id")},
      inverseJoinColumns = {@JoinColumn(name = "domain_id")})
  private Set<Domain> domains = new HashSet<Domain>();


  public AppInfo() {
    uploadMaxSize = "10MB";
    uploadContentTypes = "text/plain,text/xml,application/xml";

  }



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


  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public List<String> getAdminEmails() {
    return adminEmails;
  }

  public void setAdminEmails(List<String> adminEmails) {
    this.adminEmails = adminEmails;
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


  public String getAcknowledgment() {
    return acknowledgment;
  }

  public void setAcknowledgment(String acknowledgment) {
    this.acknowledgment = acknowledgment;
  }


  public String getCsrfToken() {
    return csrfToken;
  }

  public void setCsrfToken(String csrfToken) {
    this.csrfToken = csrfToken;
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
        + ", name=" + name + ", domains=" + domains + ", header=" + header + ", adminEmails="
        + adminEmails;
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



  public String getUploadMaxSize() {
    return uploadMaxSize;
  }



  public void setUploadMaxSize(String uploadMaxSize) {
    this.uploadMaxSize = uploadMaxSize;
  }



  public String getUploadContentTypes() {
    return uploadContentTypes;
  }



  public void setUploadContentTypes(String uploadContentTypes) {
    this.uploadContentTypes = uploadContentTypes;
  }



  public String getOrganization() {
    return organization;
  }


  public void setOrganization(String organization) {
    this.organization = organization;
  }


  public Map<String, String> getOptions() {
    if (this.options == null) {
      this.options = new HashMap<String, String>();
    }
    return options;
  }

  @Transient
  public void setEmployerRequired(boolean required) {
    this.getOptions().put(Constant.EMPLOYER_REQUIRED, Boolean.toString(required));
  }

  @Transient
  public Boolean isEmployerRequired() {
    return this.getOptions().get(Constant.EMPLOYER_REQUIRED) != null
        && Boolean.valueOf(this.getOptions().get(Constant.EMPLOYER_REQUIRED));
  }

  @Transient
  public Boolean isAuthenticationRequired() {
    return this.getOptions().get(Constant.AUTHENTICATION_REQUIRED) != null
        && Boolean.valueOf(this.getOptions().get(Constant.AUTHENTICATION_REQUIRED));
  }

  @Transient
  public Boolean isCfManagementSupported() {
    return this.getOptions().get(Constant.CF_MANAGEMENT_SUPPORTED) != null
        && Boolean.valueOf(this.getOptions().get(Constant.CF_MANAGEMENT_SUPPORTED));
  }

  @Transient
  public Boolean isCbManagementSupported() {
    return this.getOptions().get(Constant.CB_MANAGEMENT_SUPPORTED) != null
        && Boolean.valueOf(this.getOptions().get(Constant.CB_MANAGEMENT_SUPPORTED));
  }


  @Transient
  public Boolean isDocumentManagementSupported() {
    return this.getOptions().get(Constant.DOC_MANAGEMENT_SUPPORTED) != null
        && Boolean.valueOf(this.getOptions().get(Constant.DOC_MANAGEMENT_SUPPORTED));
  }


  @Transient
  public void setAuthenticationRequired(boolean required) {
    this.getOptions().put(Constant.AUTHENTICATION_REQUIRED, Boolean.toString(required));
  }

  @Transient
  public void setCbManagementSupported(boolean supported) {
    this.getOptions().put(Constant.CB_MANAGEMENT_SUPPORTED, Boolean.toString(supported));
  }

  @Transient
  public void setCfManagementSupported(boolean supported) {
    this.getOptions().put(Constant.CF_MANAGEMENT_SUPPORTED, Boolean.toString(supported));
  }

  @Transient
  public void setDisclaimerLink(String disclaimerLink) {
    this.getOptions().put(Constant.DISCLAIMER_LINK, disclaimerLink);
  }

  @Transient
  public void setPrivacyLink(String privacyLink) {
    this.getOptions().put(Constant.PRIVACY_LINK, privacyLink);
  }

  @Transient
  public void setOrganizationLink(String website) {
    this.getOptions().put(Constant.ORGANIZATION_LINK, website);
  }

  @Transient
  public void setOrganizationLogo(String logo) {
    this.getOptions().put(Constant.ORGANIZATION_LOGO, logo);
  }

  @Transient
  public void setDivisionLogo(String logo) {
    this.getOptions().put(Constant.DIVISION_LOGO, logo);
  }

  @Transient
  public void setDivisionLink(String site) {
    this.getOptions().put(Constant.DIVISION_LINK, site);
  }

  @Transient
  public void setDivisionName(String name) {
    this.getOptions().put(Constant.DIVISION_NAME, name);
  }

  @Transient
  public void setOrganizationName(String name) {
    this.getOptions().put(Constant.ORGANIZATION_NAME, name);
  }

  @Transient
  public void setDownloadWarDisabled(boolean disabled) {
    this.getOptions().put(Constant.DOWNLOAD_WAR_DISABLED, Boolean.toString(disabled));
  }

  @Transient
  public Boolean isDownloadWarDisabled() {
    return this.getOptions().get(Constant.DOWNLOAD_WAR_DISABLED) != null
        && Boolean.valueOf(this.getOptions().get(Constant.DOWNLOAD_WAR_DISABLED));
  }


  @Transient
  public void setDocManagementSupported(boolean supported) {
    this.getOptions().put(Constant.DOC_MANAGEMENT_SUPPORTED, Boolean.toString(supported));
  }

  @Transient
  public void setDomainManagementSupported(boolean supported) {
    this.getOptions().put(Constant.DOMAIN_MANAGEMENT_SUPPORTED, Boolean.toString(supported));
  }


  @Transient
  public Boolean isDomainManagementSupported() {
    return this.getOptions().get(Constant.DOMAIN_MANAGEMENT_SUPPORTED) != null
        && Boolean.valueOf(this.getOptions().get(Constant.DOMAIN_MANAGEMENT_SUPPORTED));
  }



  public String getPrivacy() {
    return privacy;
  }



  public void setPrivacy(String privacy) {
    this.privacy = privacy;
  }



  public String getConfidentialityLink() {
    return confidentialityLink;
  }



  public void setConfidentialityLink(String confidentialityLink) {
    this.confidentialityLink = confidentialityLink;
  }



  public String getDisclaimerLink() {
    return disclaimerLink;
  }



  public String getPrivacyLink() {
    return privacyLink;
  }



  public String getContactEmail() {
    return contactEmail;
  }



  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }



  public Set<Domain> getDomains() {
    return domains;
  }



  public void setDomains(Set<Domain> domains) {
    this.domains = domains;
  }



  public String getSubTitle() {
    return subTitle;
  }



  public void setSubTitle(String subTitle) {
    this.subTitle = subTitle;
  }



}
