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

package gov.nist.hit.core.api;

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestStepTestingType;
import gov.nist.hit.core.domain.ValidationReport;
import gov.nist.hit.core.repo.TestContextRepository;
import gov.nist.hit.core.service.ManualValidationReportService;
import gov.nist.hit.core.service.TestCaseService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.ValidationReportService;
import gov.nist.hit.core.service.exception.MessageValidationException;
import gov.nist.hit.core.service.exception.TestCaseException;
import gov.nist.hit.core.service.exception.ValidationReportException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RequestMapping("/testcases")
@RestController
public class TestCaseController {

  Logger logger = LoggerFactory.getLogger(TestCaseController.class);

  @Autowired
  private TestContextRepository testContextRepository;



  @Autowired
  private TestCaseService testCaseService;

  @Autowired
  private UserService userService;

  @Autowired
  private ValidationReportService messageValidationReportService;


  @Autowired
  private ManualValidationReportService manualValidationReportService;



  /**
   * find a test case by its id
   * 
   * @param testCaseId
   * @return
   */
  @RequestMapping(value = "/{testCaseId}")
  public TestCase testCase(@PathVariable final Long testCaseId) {
    logger.info("Fetching testCase with id=" + testCaseId);
    TestCase testCase = testCaseService.findOne(testCaseId);
    if (testCase == null) {
      throw new TestCaseException(testCaseId);
    }
    return testCase;
  }

  /**
   * Clear a user records related to a test case
   * 
   * @param testCaseId
   * @param request
   * @return
   * @throws MessageValidationException
   */
  @RequestMapping(value = "/{testCaseId}/clearRecords", method = RequestMethod.POST)
  public boolean clearRecords(@PathVariable("testCaseId") final Long testCaseId,
      HttpServletRequest request) throws MessageValidationException {
    logger.info("Clearing user records for testcase " + testCaseId);
    Long userId = SessionContext.getCurrentUserId(request.getSession(false));
    if (userId == null || userService.findOne(userId) == null)
      throw new ValidationReportException("Invalid user credentials");
    List<ValidationReport> results =
        messageValidationReportService.findAllByTestCaseAndUser(testCaseId, userId);
    if (results != null && !results.isEmpty()) {
      messageValidationReportService.delete(results);
    }
    return true;
  }

  /**
   * 
   * @param testCaseId
   * @return
   */
  @RequestMapping(value = "/{testCaseId}/details", method = RequestMethod.GET)
  public Map<String, TestArtifact> details(@PathVariable("testCaseId") final Long testCaseId) {
    Map<String, TestArtifact> result = new HashMap<String, TestArtifact>();
    logger.info("Fetching testcase " + testCaseId + " artifacts ");
    TestCase testCase = testCase(testCaseId);
    result.put("testStory", testCase.getTestStory());
    return result;
  }


  /**
   * 
   * @param testCaseId
   * @return
   */
  @RequestMapping(value = "/{testCaseId}/teststory", method = RequestMethod.GET)
  public TestArtifact tcTestStory(@PathVariable("testCaseId") Long testCaseId) {
    logger.info("Fetching teststory of testcase/teststep with id=" + testCaseId);
    return testCaseService.testStory(testCaseId);
  }

  @RequestMapping(value = "/{testCaseId}/reports/download", method = RequestMethod.POST,
      consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public boolean zipReports(@PathVariable("testCaseId") final Long testCaseId,
      HttpServletRequest request, HttpServletResponse response) throws ValidationReportException {
    try {
      logger.info("Clearing user records for testcase " + testCaseId);
      Long userId = SessionContext.getCurrentUserId(request.getSession(false));
      if (userId == null || userService.findOne(userId) == null)
        throw new ValidationReportException("Invalid user credentials");
      TestCase testCase = testCaseService.findOne(testCaseId);
      if (testCase == null)
        throw new TestCaseException(testCaseId);
      List<ValidationReport> results =
          messageValidationReportService.findAllByTestCaseAndUser(testCaseId, userId);
      if (results != null && !results.isEmpty()) {
        HashMap<String, InputStream> resultStreams =
            new HashMap<String, InputStream>(results.size());
        for (ValidationReport result : results) {
          TestStep tesStep = result.getTestStep();
          InputStream pdf = pdf(result);
          resultStreams.put(
              tesStep.getPosition() + "." + tesStep.getName().concat("-ValidationReport.pdf"), pdf);
        }
        InputStream io =
            messageValidationReportService.zipReports(testCase.getName(), resultStreams);
        response.setContentType("application/zip");
        response.setHeader("Content-disposition", "attachment;filename=" + testCase.getName()
            + "-ValidationReports.zip");
        FileCopyUtils.copy(io, response.getOutputStream());
      }
    } catch (Exception e) {
      throw new ValidationReportException("Failed to download the reports");
    }
    return true;
  }

  public InputStream pdf(ValidationReport report) {
    try {
      InputStream io = null;
      String xmlReport = report.getXml();
      TestStep testStep = report.getTestStep();
      if (testStep.getTestingType().equals(TestStepTestingType.SUT_MANUAL)
          || testStep.getTestingType().equals(TestStepTestingType.SUT_MANUAL)) {
        io = manualValidationReportService.toPDF(xmlReport);
      } else {
        io = messageValidationReportService.toPDF(xmlReport);
      }
      return io;
    } catch (ValidationReportException e) {
      throw new ValidationReportException("Failed to generate the report pdf");
    } catch (Exception e) {
      throw new ValidationReportException("Failed to generate the report pdf");
    }
  }

}
