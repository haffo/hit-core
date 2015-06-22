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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

// @Entity
// @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// @DiscriminatorColumn(name = "TESTING_TYPE")
// @Entity
public abstract class MessageData implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull
  @Column(nullable = false)
  protected String location;

  protected String displayName;

  @NotNull
  @Column(nullable = false)
  protected String dataType;

  @NotNull
  @Column(nullable = false)
  protected String category;

  @NotNull
  @Column(nullable = false)
  protected String data;

  @NotNull
  @Column(nullable = false)
  protected String valueSet;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  public MessageData(Long id, String location, String displayName, String dataType,
      String category, String data, String valueSet) {
    super();
    this.id = id;
    this.location = location;
    this.displayName = displayName;
    this.dataType = dataType;
    this.category = category;
    this.data = data;
    this.valueSet = valueSet;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public MessageData() {
    super();
  }

  public MessageData(String location, String displayName, String dataType, String category,
      String data, String valueSet) {
    super();
    this.location = location;
    this.displayName = displayName;
    this.dataType = dataType;
    this.category = category;
    this.data = data;
    this.valueSet = valueSet;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getValueSet() {
    return valueSet;
  }

  public void setValueSet(String valueSet) {
    this.valueSet = valueSet;
  }

}
