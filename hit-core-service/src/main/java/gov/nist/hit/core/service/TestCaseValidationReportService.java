package gov.nist.hit.core.service;

import java.io.InputStream;

import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseValidationResult;
import gov.nist.hit.core.service.exception.ValidationReportException;

public interface TestCaseValidationReportService {

  public String generateXml(TestCaseValidationResult result) throws ValidationReportException;

  public InputStream generatePdf(TestCase testCase, Long userId, String result, String comments,
      String testPlan, String testGroup) throws ValidationReportException;

  public String generateXml(TestCase testCase, Long userId, String result, String comments,
      String testPlan, String testGroup) throws ValidationReportException;

  public String generateHtml(TestCase testCase, Long userId, String result, String comments,
      String testPlan, String testGroup) throws ValidationReportException;

  public String generateHtml(String xml) throws ValidationReportException;

  public String generateHtml(TestCaseValidationResult result) throws ValidationReportException;

  public InputStream generatePdf(String xml) throws ValidationReportException;

  public InputStream generatePdf(TestCaseValidationResult result) throws ValidationReportException;

  public String generateXhtml(String xml) throws ValidationReportException;

  public String generateXhtml(TestCaseValidationResult result) throws ValidationReportException;

  void deleteByTestCaseAndUser(Long userId, Long testCaseId);

  String generateXml(TestCase testCase, Long userId, String res, String comments)
      throws ValidationReportException;

  String generateHtml(TestCase testCase, Long userId, String result, String comments)
      throws ValidationReportException;


}
