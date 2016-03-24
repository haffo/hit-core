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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Harold Affo (NIST)
 * 
 */

@Entity
@ApiModel(value="ConformanceProfile", description="Data Model representing the conformance profile")
public class ConformanceProfile implements Serializable {
 
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id; 
  
  @JsonIgnore
  @ManyToOne
  IntegrationProfile integrationProfile; 
  
  @NotNull
  @JsonIgnore
  @Column(nullable = false)
  protected String sourceId;

  @NotNull
  @JsonIgnore
  @Column(columnDefinition = "LONGTEXT")
  protected String json;

  
  @JsonIgnore
  @Column(columnDefinition = "LONGTEXT")
  protected String xml;
  
 
  public IntegrationProfile getIntegrationProfile() {
    return integrationProfile;
  }

  public void setIntegrationProfile(IntegrationProfile integrationProfile) {
    this.integrationProfile = integrationProfile;
  }

  public ConformanceProfile() {
    super();
  }  

  public ConformanceProfile(String json) {
    super();
     this.json = json;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  } 
  
    
  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }

  public String getSourceId() {
    return sourceId;
  }

  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

  public String getXml() {
    return xml;
  }

  public void setXml(String xml) {
    this.xml = xml;
  }
 
 
  
  
  
  
}
