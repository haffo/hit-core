package gov.nist.hit.core.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@ApiModel(value="TestArtifact", description="Data Model representing a test artifact")
public class TestArtifact implements Serializable {
 
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  
  @ApiModelProperty(required=true, value="name of the artifact")
  private String name;
  
  @ApiModelProperty(required=false, value="html representation of the artifact")
  @Column(columnDefinition = "TEXT")
  private String html;
  
  @ApiModelProperty(required=false, value="pdf path of the artifact")
  @Column(columnDefinition = "TEXT")
  private String pdfPath;
  
  @ApiModelProperty(required=false, value="json representation of the artifact")
   @Column(columnDefinition = "TEXT")
  private String json;
 
  
  public TestArtifact() {
    super();
    this.name = null;
  }

  public TestArtifact(String name) {
    super();
    this.name = name;
  }

  public String getPdfPath() {
    return pdfPath;
  }

  public void setPdfPath(String pdfPath) {
    this.pdfPath = pdfPath;
  }

  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
  }

  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

 
}
