package gov.nist.hit.core.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "MessageValidationResult",
    description = "Data Model representing the result of a message validation")
public class MessageValidationResult implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(required = false, value = "generated validation report id")
  private Long reportId;

  @ApiModelProperty(required = true, value = "json validation report")
  private String json;

  @JsonIgnore
  
  @ApiModelProperty(required = false, value = "html validation report")
  private String html;

  @JsonIgnore
  
  @ApiModelProperty(required = false, value = "xml validation report")
  private String xml;

  public MessageValidationResult(String json, String html) {
    super();
    this.json = json;
    this.html = html;
  }

  public MessageValidationResult(String json) {
    super();
    this.json = json;
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

  public Long getReportId() {
    return reportId;
  }

  public void setReportId(Long reportId) {
    this.reportId = reportId;
  }



}
