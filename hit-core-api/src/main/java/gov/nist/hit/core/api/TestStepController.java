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
import gov.nist.hit.core.domain.TestContext;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.repo.TestCaseRepository;
import gov.nist.hit.core.repo.TestContextRepository;
import gov.nist.hit.core.repo.TestStepRepository;
import gov.nist.hit.core.service.TestStepService;
import gov.nist.hit.core.service.TestStepValidationReportService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.exception.TestCaseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
@RequestMapping("/teststeps")
@RestController
@Api(value = "TestSteps", tags = "Test Steps")
public class TestStepController {

  Logger logger = LoggerFactory.getLogger(TestStepController.class);

  @Autowired
  private TestContextRepository testContextRepository;

  @Autowired
  private TestStepRepository testStepRepository;

  @Autowired
  protected TestCaseRepository testCaseRepository;

  @Autowired
  private TestStepService testStepService;

  @Autowired
  private UserService userService;

  @Autowired
  private TestStepValidationReportService validationReportService;

  /**
   * find a test step by its id
   * 
   * @param teststepId
   * @return
   */
  @ApiOperation(value = "Get a test step by its id", nickname = "getTestStepById")
  @RequestMapping(value = "/{teststepId}", method = RequestMethod.GET,
      produces = "application/json")
  public TestStep testStep(
      @ApiParam(value = "the id of the test step", required = true) @PathVariable final Long teststepId) {
    logger.info("Fetching test step with id=" + teststepId);
    TestStep testStep = testStepService.findOne(teststepId);
    if (testStep == null) {
      throw new TestCaseException(teststepId);
    }
    return testStep;
  }

  /**
   * find the test context of a test step
   * 
   * @param testStepId
   * @return
   */
  @ApiOperation(value = "Get a test ste's test context by the test step's id",
      nickname = "getTestStepTestContextByTestStepId")
  @RequestMapping(value = "/{testStepId}/testcontext", method = RequestMethod.GET,
      produces = "application/json")
  public TestContext getTestStepTestContextByTestStepId(@ApiParam(
      value = "the id of the test step", required = true) @PathVariable final Long testStepId) {
    logger.info("Fetching testContext from testStepId=" + testStepId);
    TestContext testContext = testStep(testStepId).getTestContext();
    if (testContext == null)
      throw new TestCaseException("No testcontext available for teststep id=" + testStepId);
    return testContext;
  }



  @ApiOperation(
      value = "Get a test step's details (juror document, test story etc...) by the test step's id",
      nickname = "getTestStepDetailByTestStepId")
  @RequestMapping(value = "/{testStepId}/details", method = RequestMethod.GET)
  public Map<String, TestArtifact> getTestStepDetailByTestStepId(@ApiParam(
      value = "the id of the test step", required = true) @PathVariable final Long testStepId) {
    logger.info("Fetching artifacts of teststep with id=" + testStepId);
    TestStep testStep = testStep(testStepId);
    Map<String, TestArtifact> result = new HashMap<String, TestArtifact>();
    result.put("jurorDocument", testStep.getJurorDocument());
    result.put("messageContent", testStep.getMessageContent());
    result.put("testDataSpecification", testStep.getTestDataSpecification());
    result.put("testStory", testStep.getTestStory());
    return result;
  }

  @ApiOperation(value = "", hidden = true)
  @RequestMapping(value = "/{testStepId}/jurordocument", method = RequestMethod.GET)
  public TestArtifact tcJurordocument(@PathVariable final Long testStepId) {
    logger.info("Fetching juror document of testcase/teststep with id=" + testStepId);

    return testStepService.jurorDocument(testStepId);

  }

  @ApiOperation(value = "", hidden = true)
  @RequestMapping(value = "/{testStepId}/messagecontent", method = RequestMethod.GET)
  public TestArtifact tcMessageContent(@PathVariable final Long testStepId) {
    logger.info("Fetching messagecontent of testcase/teststep with id=" + testStepId);

    return testStepService.messageContent(testStepId);

  }

  @ApiOperation(value = "", hidden = true)
  @RequestMapping(value = "/{testStepId}/teststory", method = RequestMethod.GET)
  public TestArtifact tcTestStory(@PathVariable final Long testStepId) {
    logger.info("Fetching teststory of testcase/teststep with id=" + testStepId);

    return testStepService.testStory(testStepId);

  }

  @ApiOperation(value = "", hidden = true)
  @RequestMapping(value = "/{testStepId}/tds", method = RequestMethod.GET)
  public TestArtifact tcTestDataSpecification(@PathVariable final Long testStepId) {
    logger.info("Fetching testDataSpecification of testcase/teststep with id=" + testStepId);
    return testStepService.testDataSpecification(testStepId);
  }



}
