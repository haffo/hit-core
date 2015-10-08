package gov.nist.hit.core.domain;

public class MessageValidationResult {
 
  
  public MessageValidationResult(String json, String html) {
    super();
    this.json = json;
    this.html = html;
  }

  private String json; 
  
  private String html;

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
  
  
  
  
  
}
