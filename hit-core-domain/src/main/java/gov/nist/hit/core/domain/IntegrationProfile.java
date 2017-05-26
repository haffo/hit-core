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
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Harold Affo (NIST)
 * 
 */

@Entity
public class IntegrationProfile implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;

  @NotNull
  @JsonIgnore
  @Column(columnDefinition = "LONGTEXT")
  protected String xml;

  @JsonIgnore
  @NotNull
  @Column(unique = true)
  protected String sourceId;

  @Column(nullable = true)
  protected String name;

  @Column(nullable = true)
  protected String description;

  @Column(nullable = true)
  protected String key_;

  @Column(nullable = true)
  protected String type_;

  @Column(nullable = true)
  protected String schemaVersion;

  @Column(nullable = true)
  protected String hl7Version;

  @Column(nullable = true)
  @ElementCollection
  @JsonIgnore
  protected List<String> messages;

  @NotNull
  @Enumerated(EnumType.STRING)
  protected TestScope scope = TestScope.GLOBAL;

  public IntegrationProfile() {
    super();
    scope = TestScope.GLOBAL;
  }

  public IntegrationProfile(String name, String description, String xml) {
    super();
    this.name = name;
    this.description = description;
    this.xml = xml;
  }

  public IntegrationProfile(String xml) {
    super();
    this.name = null;
    this.description = null;
    this.xml = xml;
  }

  public List<String> getMessages() {
    return messages;
  }

  public void setMessages(List<String> messages) {
    this.messages = messages;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getKey() {
    return key_;
  }

  public void setKey(String key) {
    this.key_ = key;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getType() {
    return type_;
  }

  public void setType(String type) {
    this.type_ = type;
  }

  public String getSchemaVersion() {
    return schemaVersion;
  }

  public void setSchemaVersion(String schemaVersion) {
    this.schemaVersion = schemaVersion;
  }

  public String getHl7Version() {
    return hl7Version;
  }

  public void setHl7Version(String hl7Version) {
    this.hl7Version = hl7Version;
  }

  public String getXml() {
    return xml;
  }

  public void setXml(String xml) {
    this.xml = xml;
  }

  public String getSourceId() {
    return sourceId;
  }

  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

}
