package gov.nist.hit.core.domain;


public class TestStepValidationReportRequest {

  private Long testStepId;

  private Long userId;

  private String xmlMessageValidationReport;

  private final String result = null;

  private String comments;



  public TestStepValidationReportRequest() {
    super();
    // TODO Auto-generated constructor stub
  }



  public TestStepValidationReportRequest(Long testStepId, Long userId,
      String xmlMessageValidationReport, String comments) {
    super();
    this.testStepId = testStepId;
    this.userId = userId;
    this.xmlMessageValidationReport = xmlMessageValidationReport;
    this.comments = comments;
  }



  public Long getTestStepId() {
    return testStepId;
  }

  public void setTestStepId(Long testStepId) {
    this.testStepId = testStepId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }


  public String getXmlMessageValidationReport() {
    return xmlMessageValidationReport;
  }



  public void setXmlMessageValidationReport(String xmlMessageValidationReport) {
    this.xmlMessageValidationReport = xmlMessageValidationReport;
  }



  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public String getResult() {
    return result;
  }



}
