package gov.nist.hit.core.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class TestCaseValidationResult {
  
  private TestCase testCase; 
  
  private  List<TestStepValidationReport> testStepReports; 
  
  private Date date; 
  
  private Map<String, String> nav = new HashMap<String, String>(); 
  
  private TestResult result = null;
  
  private String comments;
  
  
  public TestCaseValidationResult(TestCase testCase, List<TestStepValidationReport> testStepReports) {
    super();
    this.testCase = testCase;
    this.testStepReports = testStepReports;
  } 
  
  public TestCaseValidationResult() {
    super();
  }
  

  public TestCase getTestCase() {
    return testCase;
  }

  public void setTestCase(TestCase testCase) {
    this.testCase = testCase;
  }

  

  public List<TestStepValidationReport> getTestStepReports() {
    return testStepReports;
  }

  public void setTestStepReports(List<TestStepValidationReport> testStepReports) {
    this.testStepReports = testStepReports;
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

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public TestResult getResult() {
    return result;
  }

  public void setResult(TestResult result) {
    this.result = result;
  }

  
  
  
  
  
}
