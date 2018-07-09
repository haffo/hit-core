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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Message extends TestResource {


  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;

  @Column(nullable = true)
  protected String name;

  @Column(nullable = true)
  protected String description;

  @Column(columnDefinition = "LONGTEXT")
  protected String content;

  public Message() {}

  public Message(String name, String description, String content, String domain) {
    super();
    this.name = name;
    this.description = description;
    this.content = content;
    this.domain = domain;
  }

  public Message(String name, String content) {
    super();
    this.name = name;
    this.description = null;
    this.content = content;
    this.domain = null;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Name: ").append(getName()).append(", ");
    sb.append("Description: ").append(getDescription()).append(", ");
    sb.append("Content: ").append(getContent());
    return sb.toString();
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

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

}
