package gov.nist.hit.core.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@ApiModel(value="MessageValidationResult", description="Data Model representing the result of a message validation")
public class MessageValidationResult implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(required=true, value="json validation report")
  private String json;

  @ApiModelProperty(required=false, value="html validation report")
  private String html;

  @ApiModelProperty(required=false, value="xml validation report")
  private String xml;

  public MessageValidationResult(String json, String html) {
    super();
    this.json = json;
    this.html = html;
  }

  public MessageValidationResult() {
    // TODO Auto-generated constructor stub
  }

  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }

  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
  }

  public String getXml() {
    return xml;
  }

  public void setXml(String xml) {
    this.xml = xml;
  }

  

}
