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
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class TestCaseDocumentation implements Serializable {


  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;

  private String title; 
  


  @Enumerated(EnumType.STRING)
  private TestingStage stage;

  @Transient
  private List<TestCaseDocument> children = new ArrayList<TestCaseDocument>();

  @NotNull
  @Column(columnDefinition = "LONGTEXT")
  private String json;

  public List<TestCaseDocument> getChildren() {
    return children;
  }

  public void setChildren(List<TestCaseDocument> children) {
    this.children = children;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public TestingStage getStage() {
    return stage;
  }

  public void setStage(TestingStage stage) {
    this.stage = stage;
  }

 
  

}
