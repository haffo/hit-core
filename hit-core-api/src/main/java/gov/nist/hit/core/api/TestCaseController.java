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
import gov.nist.hit.core.service.TestCaseService;
import gov.nist.hit.core.service.TestCaseValidationReportService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.exception.TestCaseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.Map;

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
@Api(value = "testcases", tags = "Test Cases")
public class TestCaseController {

  Logger logger = LoggerFactory.getLogger(TestCaseController.class);

  @Autowired
  private TestCaseService testCaseService;

  @Autowired
  private UserService userService;

  @Autowired
  private TestCaseValidationReportService testCaseValidationReportService;


  /**
   * find a test case by its id
   * 
   * @param testCaseId
   * @return
   */
  @ApiOperation(value = "Get a test case (context-free or context-based) by its id",
      nickname = "getTestCaseById")
  @RequestMapping(value = "/{testCaseId}", method = RequestMethod.GET,
      produces = "application/json")
  public TestCase testCase(@PathVariable final Long testCaseId) {
    logger.info("Fetching testCase with id=" + testCaseId);
    TestCase testCase = testCaseService.findOne(testCaseId);
    if (testCase == null) {
      throw new TestCaseException(testCaseId);
    }
    return testCase;
  }


  /**
   * 
   * @param testCaseId
   * @return
   */
  @ApiOperation(value = "Get a test case (context-free or context-based) details by its id",
      nickname = "getTestCaseDetailsById")
  @RequestMapping(value = "/{testCaseId}/details", method = RequestMethod.GET,
      produces = "application/json")
  public Map<String, TestArtifact> getTestCaseDetailsById(
      @PathVariable("testCaseId") final Long testCaseId) {
    Map<String, TestArtifact> result = new HashMap<String, TestArtifact>();
    logger.info("Fetching testcase " + testCaseId + " artifacts ");
    TestCase testCase = testCase(testCaseId);
    result.put("testStory", testCase.getTestStory());
    result.put("jurorDocument", testCase.getJurorDocument());
    return result;
  }


  /**
   * 
   * @param testCaseId
   * @return
   */
  @ApiOperation(value = "", hidden = true)
  @RequestMapping(value = "/{testCaseId}/teststory", method = RequestMethod.GET)
  public TestArtifact tcTestStory(@PathVariable("testCaseId") Long testCaseId) {
    logger.info("Fetching teststory of testcase/teststep with id=" + testCaseId);
    return testCaseService.testStory(testCaseId);
  }



}
