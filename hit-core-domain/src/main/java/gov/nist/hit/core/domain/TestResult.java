package gov.nist.hit.core.domain;

public enum TestResult {

  PASSED ("Passed"),PASSED_NOTABLE_EXCEPTION("Passed - Notable Exception") ,FAILED("Failed"), FAILED_NOT_SUPPORTED("Failed - Not Supported"),INCOMPLETE("Incomplete"),INCONCLUSIVE("Inconclusive"), INPROGRESS("In Progress");
  
  private String title;
  
  TestResult(String title){
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
  
  
  
}
