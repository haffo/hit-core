package gov.nist.hit.core.domain;

import java.util.Date;
import java.util.Map;

public class ManualValidationResult {
  
  private Long testStepId;
  
  private String value;
  
  private String comments;
  
  private Date date;
  
  private Map<String, String> nav;;

  public ManualValidationResult() {
    super();
    // TODO Auto-generated constructor stub
  }

  public Long getTestStepId() {
    return testStepId;
  }

  public void setTestStepId(Long testStepId) {
    this.testStepId = testStepId;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Map<String, String> getNav() {
    return nav;
  }

  public void setNav(Map<String, String> nav) {
    this.nav = nav;
  }
  
  
  
}
