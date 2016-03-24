/*
 * Meaningful Use Core Artifact.java October 14, 2011
 * 
 * This code was produced by the National Institute of Standards and Technology (NIST). See the
 * 'nist.disclaimer' file given in the distribution for information on the use and redistribution of
 * this software.
 */
package gov.nist.hit.core.domain;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Harold Affo (NIST)
 */
@Entity
@ApiModel(value="ConfoDocumentrmanceProfile", description="Data Model representing a document")
public class Document implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  protected Long id;

  private static final long serialVersionUID = 1L;

  private String title;
  
  private String name;
  
  private String path; 
    
  private String version;

  private String comments;

  private String date;

  private int position;
  
  @JsonIgnore
  @Enumerated(EnumType.STRING)
  private DocumentType type;

  public Document() {
    super();
  }

//  public Document(DocumentType type, String title, String description, String path, String version, String format,
//      String date) {
//    this(type,title, description, path, version, format, date);
//   }

//  public Document(DocumentType type,String title, String description, String path, String version) {
//    this(type, title, description, path, version);
//    }

//  public Document(DocumentType type, String title, String description, String path, String version, String format,
//      String date) {
//    super();
//    this.title = title;
//    this.description = description;
//    this.path = path;
//    this.version = version;
//    this.format = format;
//    this.date = date;
//    this.type = type;
//  }
//
//  public Document(DocumentType type, String title, String description, String path, String version) {
//    super();
//    this.title = title;
//    this.description = description;
//    this.path = path;
//    this.version = version;
//  }
//
//  public Document(DocumentType type, String title, String description) {
//    this(type,title, description, null, null);
//  }
//
//  public Document(DocumentType type, String title, String description, String path) {
//    this(type,title, description, path, null);
//  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public DocumentType getType() {
    return type;
  }

  public void setType(DocumentType type) {
    this.type = type;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

 
}
