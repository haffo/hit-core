package gov.nist.hit.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class VocabularyLibrary implements Serializable {
 
 
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;
  
  @JsonIgnore
  @NotNull
  @Column(nullable = false)
  protected String sourceId;
  
  @JsonIgnore
  protected String name; 
  
  @JsonIgnore
  protected String description;
  
  @JsonIgnore
  protected String key;
  
  @JsonIgnore
  @Column(columnDefinition = "LONGTEXT")
  protected String xml;

  @JsonIgnore
  @Column(columnDefinition = "LONGTEXT")
  protected String json; 
  
  public String getXml() {
    return xml;
  }

  public void setXml(String xml) {
    this.xml = xml;
  }

  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }





  public VocabularyLibrary(String valueSetXml) {
    super();
    this.xml = valueSetXml;
  }
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSourceId() {
    return sourceId;
  }

  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
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
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }
  
  
  
  

  public VocabularyLibrary() {
    super();
    // TODO Auto-generated constructor stub
  }

}
