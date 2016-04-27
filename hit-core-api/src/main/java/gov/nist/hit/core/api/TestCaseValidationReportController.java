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

import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.TestCaseService;
import gov.nist.hit.core.service.TestCaseValidationReportService;
import gov.nist.hit.core.service.exception.MessageValidationException;
import gov.nist.hit.core.service.exception.TestCaseException;
import gov.nist.hit.core.service.exception.ValidationReportException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RestController
@RequestMapping("/testCaseValidationReport")
@Api(value = "Test Step validation report api", tags = "Test Step Validation Report")
public class TestCaseValidationReportController {

  static final Logger logger = LoggerFactory.getLogger(TestCaseValidationReportController.class);

  @Autowired
  private TestCaseValidationReportService testCaseValidationReportService;


  @Autowired
  private TestCaseService testCaseService;

  @Autowired
  private AccountService userService;

  @ApiOperation(value = "Download a test case validation report by the test case's id",
      nickname = "downloadTestCaseValidationReport",
      produces = "text/html,application/xml,application/pdf")
  @RequestMapping(value = "/download", method = RequestMethod.POST,
      consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public boolean downloadTestCaseValidationReport(
      @ApiParam(value = "the id of the test case", required = true) @RequestParam("testCaseId") final Long testCaseId,
      @ApiParam(value = "the format of the report", required = true) @RequestParam("format") final String format,
      @ApiParam(value = "the result of the test case", required = true) @RequestParam(
          value = "result", required = true) String result, @ApiParam(
          value = "the comments of the test case", required = false) @RequestParam(
          value = "comments", required = false) String comments, HttpServletRequest request,
      HttpServletResponse response) throws ValidationReportException {
    try {
      logger.info("Clearing user records for testcase " + testCaseId);
      Long userId = SessionContext.getCurrentUserId(request.getSession(false));
      if (userId == null || userService.findOne(userId) == null)
        throw new ValidationReportException("Invalid user credentials");
      TestCase testCase = testCaseService.findOne(testCaseId);
      if (testCase == null)
        throw new TestCaseException(testCaseId);
      String title = testCase.getName().replaceAll(" ", "-");
      InputStream io = null;
      if ("HTML".equalsIgnoreCase(format)) {
        io =
            IOUtils.toInputStream(
                testCaseValidationReportService.generateHtml(testCase, userId, result, comments),
                "UTF-8");
        response.setContentType("text/html");
      } else if ("XML".equalsIgnoreCase(format)) {
        io =
            IOUtils.toInputStream(
                testCaseValidationReportService.generateXml(testCase, userId, result, comments),
                "UTF-8");
        response.setContentType("application/xml");
      } else if ("PDF".equalsIgnoreCase(format)) {
        io = testCaseValidationReportService.generatePdf(testCase, userId, result, comments);
        response.setContentType("application/pdf");
      } else {
        throw new ValidationReportException("Unsupported report format " + format);
      }
      response.setHeader("Content-disposition", "attachment;filename=" + title
          + "-ValidationReport." + format.toLowerCase());
      FileCopyUtils.copy(io, response.getOutputStream());
    } catch (Exception e) {
      throw new ValidationReportException("Failed to download the reports");
    }
    return true;
  }


  /**
   * Clear a user records related to a test case
   * 
   * @param testCaseId
   * @param request
   * @return
   * @throws MessageValidationException
   */
  @ApiOperation(value = "", hidden = true)
  @RequestMapping(value = "/{testCaseId}/clearRecords", method = RequestMethod.POST)
  public boolean clearRecords(@PathVariable("testCaseId") final Long testCaseId,
      HttpServletRequest request) throws MessageValidationException {
    logger.info("Clearing user records for testcase " + testCaseId);
    Long userId = SessionContext.getCurrentUserId(request.getSession(false));
    if (userId == null || userService.findOne(userId) == null)
      throw new ValidationReportException("Invalid user credentials");
    testCaseValidationReportService.deleteByTestCaseAndUser(userId, testCaseId);
    return true;
  }

}
