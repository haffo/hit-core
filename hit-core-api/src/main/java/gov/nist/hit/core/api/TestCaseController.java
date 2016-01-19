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
import gov.nist.hit.core.domain.ValidationReport;
import gov.nist.hit.core.repo.TestContextRepository;
import gov.nist.hit.core.service.TestCaseService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.ValidationReportService;
import gov.nist.hit.core.service.exception.MessageValidationException;
import gov.nist.hit.core.service.exception.TestCaseException;
import gov.nist.hit.core.service.exception.ValidationReportException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
  private ValidationReportService validationReportService;

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
        validationReportService.findAllByTestCaseAndUser(testCaseId, userId);
    if (results != null && !results.isEmpty()) {
      validationReportService.delete(results);
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
    result.put("jurorDocument", testCase.getJurorDocument());
    result.put("messageContent", testCase.getMessageContent());
    result.put("testDataSpecification", testCase.getTestDataSpecification());
    result.put("testStory", testCase.getTestStory());
    result.put("testPackage", testCase.getTestPackage());
    return result;
  }

  /**
   * 
   * @param testCaseId
   * @return
   */
  @RequestMapping(value = "/{testCaseId}/jurordocument", method = RequestMethod.GET)
  public TestArtifact tcJurordocument(@PathVariable("testCaseId") final Long testCaseId) {
    logger.info("Fetching juror document of testcase with id=" + testCaseId);
    return testCaseService.jurorDocument(testCaseId);
  }

  /**
   * 
   * @param testCaseId
   * @return
   */
  @RequestMapping(value = "/{testCaseId}/messagecontent", method = RequestMethod.GET)
  public TestArtifact tcMessageContent(@PathVariable("testCaseId") final Long testCaseId) {
    logger.info("Fetching messagecontent of testcase with id=" + testCaseId);
    return testCaseService.messageContent(testCaseId);
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

  /**
   * 
   * @param testCaseId
   * @return
   */
  @RequestMapping(value = "/{testCaseId}/tds", method = RequestMethod.GET)
  public TestArtifact tcTestDataSpecification(@PathVariable("testCaseId") final Long testCaseId) {
    logger.info("Fetching testDataSpecification of testcase/teststep with id=" + testCaseId);
    return testCaseService.testDataSpecification(testCaseId);
  }

  /**
   * 
   * @param testCaseId
   * @return
   */
  @RequestMapping(value = "/{testCaseId}/testpackage", method = RequestMethod.GET)
  public TestArtifact tcTestPackage(@PathVariable("testCaseId") final Long testCaseId) {
    logger.info("Fetching testDataSpecification of testcase/teststep with id=" + testCaseId);
    return testCaseService.testPackage(testCaseId);
  }



}
