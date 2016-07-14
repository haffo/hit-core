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

import gov.nist.auth.hit.core.domain.Account;
import gov.nist.hit.core.domain.TestResult;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.domain.TestStepValidationReportRequest;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.TestCaseService;
import gov.nist.hit.core.service.TestStepService;
import gov.nist.hit.core.service.TestStepValidationReportService;
import gov.nist.hit.core.service.exception.MessageValidationException;
import gov.nist.hit.core.service.exception.ValidationReportException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RestController
@RequestMapping("/testStepValidationReport")
@Api(value = "Test Step validation report api", tags = "Test Step Validation Report")
public class TestStepValidationReportController {

  static final Logger logger = LoggerFactory.getLogger(TestStepValidationReportController.class);

  @Autowired
  private TestStepValidationReportService validationReportService;

  @Autowired
  private TestStepService testStepService;

  @Autowired
  private TestCaseService testCaseService;

  @Autowired
  private AccountService userService;


  @ApiOperation(value = "", hidden = true)
  @RequestMapping(value = "/init", method = RequestMethod.POST,
      consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public TestStepValidationReport createNew(@ApiParam(value = "the id of the test step",
      required = true) @RequestParam("testStepId") Long testStepId, HttpServletRequest request,
      HttpServletResponse response) {
    TestStepValidationReport report = null;
    try {
      logger.info("Create a new Test Step validation report");
      Long userId = SessionContext.getCurrentUserId(request.getSession(false));
      Account user = null;
      if (userId == null || ((user = userService.findOne(userId)) == null))
        throw new MessageValidationException("Invalid user credentials");
      TestStep testStep = null;
      if (testStepId == null || ((testStep = testStepService.findOne(testStepId)) == null))
        throw new ValidationReportException("No test step or unknown test step specified");
      report = findUserTestStepValidationReportByTestStep(testStepId, userId);
      if (report == null) {
        report = new TestStepValidationReport();
        report.setTestStep(testStep);
        report.setUserId(user.getId());
        validationReportService.save(report);
      }
    } catch (ValidationReportException e) {
      logger.info("Failed to initialize a Test Step validation report");
    } catch (Exception e) {
      logger.info("Failed to initialize a Test Step validation report");
    }
    return report;
  }


  private TestStepValidationReport findUserTestStepValidationReportByTestStep(Long testStepId,
      Long userId) {
    TestStepValidationReport report = null;
    List<TestStepValidationReport> reports =
        validationReportService.findAllByTestStepAndUser(testStepId, userId);
    if (reports != null && !reports.isEmpty()) {
      if (reports.size() == 1) {
        report = reports.get(0);
      } else {
        validationReportService.delete(reports);
      }
    }
    return report;
  }

  private void deleteByTestStepId(Long testStepId, Long userId) {
    try {
      List<TestStepValidationReport> reports =
          validationReportService.findAllByTestStepAndUser(testStepId, userId);
      validationReportService.delete(reports);
    } catch (RuntimeException e) {
    } catch (Exception e) {
    }
  }


  @ApiOperation(value = "", hidden = true)
  @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
  public TestStepValidationReport create(@RequestBody TestStepValidationReportRequest command,
      HttpServletRequest request, HttpServletResponse response) {
    try {
      logger.info("Saving validation report");
      Long userId = SessionContext.getCurrentUserId(request.getSession(false));
      Account user = null;
      if (userId == null || ((user = userService.findOne(userId)) == null))
        throw new MessageValidationException("Invalid user credentials");
      TestStep testStep = null;
      Long testStepId = command.getTestStepId();
      String xmlMessageValidationReport = command.getXmlMessageValidationReport();
      if (testStepId == null || ((testStep = testStepService.findOne(testStepId)) == null))
        throw new ValidationReportException("No test step or unknown test step specified");
      TestStepValidationReport report = null;
      List<TestStepValidationReport> reports =
          validationReportService.findAllByTestStepAndUser(testStepId, userId);
      if (reports != null && !reports.isEmpty()) {
        if (reports.size() == 1) {
          report = reports.get(0);
        } else {
          validationReportService.delete(reports);
          report = new TestStepValidationReport();
        }
      } else {
        report = new TestStepValidationReport();
      }
      report.setTestStep(testStep);
      report.setUserId(user.getId());
      report.setXml(xmlMessageValidationReport);
      validationReportService.save(report);
      return report;
    } catch (ValidationReportException e) {
      throw new ValidationReportException("Failed to download the report");
    } catch (Exception e) {
      throw new ValidationReportException("Failed to download the report");
    }
  }



  // @ApiOperation(value = "", hidden = true)
  // @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
  // public TestStepValidationReport create(@RequestBody TestStepValidationReportRequest command,
  // HttpServletRequest request, HttpServletResponse response) {
  // try {
  // logger.info("Saving validation report");
  // Long userId = SessionContext.getCurrentUserId(request.getSession(false));
  // Account user = null;
  // if (userId == null || ((user = userService.findOne(userId)) == null))
  // throw new MessageValidationException("Invalid user credentials");
  // TestStep testStep = null;
  // Long testStepId = command.getTestStepId();
  // String comments = command.getComments();
  // String result = command.getResult();
  // String xmlMessageValidationReport = command.getXmlMessageValidationReport();
  // if (testStepId == null || ((testStep = testStepService.findOne(testStepId)) == null))
  // throw new ValidationReportException("No test step or unknown test step specified");
  //
  // TestStepValidationReport report = null;
  // List<TestStepValidationReport> reports =
  // validationReportService.findAllByTestStepAndUser(testStepId, userId);
  // if (reports != null && !reports.isEmpty()) {
  // if (reports.size() == 1) {
  // report = reports.get(0);
  // } else {
  // validationReportService.delete(reports);
  // report = new TestStepValidationReport();
  // }
  // } else {
  // report = new TestStepValidationReport();
  // }
  // report.setTestStep(testStep);
  // report.setUserId(user.getId());
  // report.setComments(comments);
  // report.setResult(StringUtils.isNotEmpty(result) ? TestResult.valueOf(result) : null);
  // String xml = report.getXml();
  // if (StringUtils.isNotEmpty(xmlMessageValidationReport)) {
  // xml =
  // validationReportService.generateXmlTestStepValidationReport(xmlMessageValidationReport,
  // report);
  // report.setXml(xml);
  // }
  // if (StringUtils.isNotEmpty(xml)) {
  // report.setHtml(validationReportService.generateHtml(xml));
  // }
  // validationReportService.save(report);
  // return report;
  // } catch (ValidationReportException e) {
  // throw new ValidationReportException("Failed to generate the report");
  // } catch (Exception e) {
  // throw new ValidationReportException("Failed to generate the report");
  // }
  // }

  @ApiOperation(value = "Download the message validation report of a test step by its id",
      nickname = "download",
      produces = "text/html,application/msword,application/xml,application/pdf")
  @RequestMapping(value = "/{testStepValidationReportId}/download", method = RequestMethod.POST,
      consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public void download(
      @ApiParam(value = "the targeted format (html,pdf etc...)", required = true) @RequestParam("format") String format,
      @ApiParam(value = "the id of the test step", required = true) @PathVariable("testStepValidationReportId") Long testStepValidationReportId,
      HttpServletRequest request, HttpServletResponse response) {
    try {
      logger.info("Downloading validation report  in " + format);
      Long userId = SessionContext.getCurrentUserId(request.getSession(false));
      if (userId == null || (userService.findOne(userId) == null))
        throw new MessageValidationException("Invalid user credentials");
      if (format == null)
        throw new ValidationReportException("No format specified");
      TestStepValidationReport report = validationReportService.findOne(testStepValidationReportId);
      String xmlReport = null;
      if (report == null || ((xmlReport = report.getXml()) == null)) {
        throw new ValidationReportException("No validation report available for this test step");
      }
      if (report.getUserId() == null || !userId.equals(report.getUserId())) {
        throw new MessageValidationException("Forbidden access");
      }

      TestStep testStep = report.getTestStep();
      if (testStep == null)
        throw new ValidationReportException("No associated test step found");
      String title = testStep.getName();
      String ext = format.toLowerCase();
      InputStream io = null;
      if ("HTML".equalsIgnoreCase(format)) {
        io = IOUtils.toInputStream(generateHtml(xmlReport), "UTF-8");
        response.setContentType("text/html");
      } else if ("XML".equalsIgnoreCase(format)) {
        io = IOUtils.toInputStream(xmlReport, "UTF-8");
        response.setContentType("application/xml");
      } else if ("PDF".equalsIgnoreCase(format)) {
        io = validationReportService.generatePdf(xmlReport);
        response.setContentType("application/pdf");
      } else {
        throw new ValidationReportException("Unsupported report format " + format);
      }
      title = title.replaceAll(" ", "-");
      response.setHeader("Content-disposition", "attachment;filename=" + title
          + "-ValidationReport." + ext);
      FileCopyUtils.copy(io, response.getOutputStream());
    } catch (ValidationReportException | IOException e) {
      throw new ValidationReportException("Failed to generate the report");
    } catch (Exception e) {
      throw new ValidationReportException("Failed to generate the report");
    }
  }

  // @ApiOperation(value = "Get the html format of an test step validation report",
  // nickname = "generateHtml")
  // @RequestMapping(value = "/{testStepValidationReportId}/generateHtml",
  // method = RequestMethod.POST, produces = "application/json")
  // public Map<String, String> generateHtml(
  // @ApiParam(value = "the id of the test step validation report", required = true)
  // @PathVariable("testStepValidationReportId") Long testStepValidationReportId,
  // HttpServletRequest request, HttpServletResponse response) {
  // try {
  // logger.info("Generating html test step validation report");
  // Long userId = SessionContext.getCurrentUserId(request.getSession(false));
  // if (userId == null || (userService.findOne(userId) == null))
  // throw new MessageValidationException("Invalid user credentials");
  // TestStepValidationReport report = validationReportService.findOne(testStepValidationReportId);
  // String xmlReport = null;
  // if (report == null || ((xmlReport = report.getXml()) == null)) {
  // throw new ValidationReportException("No validation report found for this test step");
  // }
  // if (report.getUser() == null || report.getUser().getId() != userId) {
  // throw new MessageValidationException("Forbidden access");
  // }
  // TestStep testStep = report.getTestStep();
  // if (testStep == null)
  // throw new ValidationReportException("No associated test step found");
  // String html = validationReportService.generateHtml(xmlReport);
  // Map<String, String> res = new HashMap<String, String>(1);
  // res.put("html", html);
  // return res;
  // } catch (ValidationReportException e) {
  // throw new ValidationReportException("Failed to download the report");
  // } catch (Exception e) {
  // throw new ValidationReportException("Failed to download the report");
  // }
  // }


  // @ApiOperation(value = "Update the result of a test step by the test step's id",
  // nickname = "updateResult")
  // @RequestMapping(value = "/updateResult", method = RequestMethod.POST)
  // public boolean updateResult(
  // @ApiParam(value = "the id of the test step", required = true) @RequestParam("testStepId") final
  // Long testStepId,
  // @ApiParam(value = "the result", required = true) @RequestParam("result") final String result,
  // HttpServletRequest request) throws MessageValidationException {
  // logger.info("Setting a test step result");
  // Long userId = SessionContext.getCurrentUserId(request.getSession(false));
  // if (userId == null || userService.findOne(userId) == null)
  // throw new ValidationReportException("Invalid user credentials");
  // TestStepValidationReport report =
  // validationReportService.findOneByTestStepAndUser(testStepId, userId);
  // if (report != null) {
  // report.setResult(TestResult.valueOf(result));
  // }
  // return true;
  // }
  //
  // @ApiOperation(value = "Save the comments of a test step by the test step's id",
  // nickname = "updateComments")
  // @RequestMapping(value = "/updateComments", method = RequestMethod.POST)
  // public boolean updateComments(
  // @ApiParam(value = "the id of the test step", required = true) @RequestParam("testStepId") final
  // Long testStepId,
  // @ApiParam(value = "the comments", required = true) @RequestParam("comments") final String
  // comments,
  // HttpServletRequest request) throws MessageValidationException {
  // logger.info("Setting a test step comments");
  // Long userId = SessionContext.getCurrentUserId(request.getSession(false));
  // if (userId == null || userService.findOne(userId) == null)
  // throw new ValidationReportException("Invalid user credentials");
  // TestStepValidationReport report =
  // validationReportService.findOneByTestStepAndUser(testStepId, userId);
  // if (report != null) {
  // report.setComments(comments);
  // }
  // return true;
  // }


  @ApiOperation(value = "", hidden = true)
  @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json")
  public TestStepValidationReport update(@RequestBody TestStepValidationReportRequest command,
      HttpServletRequest request, HttpServletResponse response) {
    try {
      logger.info("Saving validation report");
      Long userId = SessionContext.getCurrentUserId(request.getSession(false));
      Account user = null;
      if (userId == null || ((user = userService.findOne(userId)) == null))
        throw new MessageValidationException("Invalid user credentials");
      TestStep testStep = null;
      Long testStepId = command.getTestStepId();
      String comments = command.getComments();
      String result = command.getResult();
      String xmlMessageValidationReport = command.getXmlMessageValidationReport();
      if (testStepId == null || ((testStep = testStepService.findOne(testStepId)) == null))
        throw new ValidationReportException("No test step or unknown test step specified");
      TestStepValidationReport report =
          validationReportService.findOneByTestStepAndUser(testStepId, userId);
      if (report != null) {
        report.setTestStep(testStep);
        report.setUserId(user.getId());
        report.setComments(comments);
        report.setResult(StringUtils.isNotEmpty(result) && result != null ? TestResult
            .valueOf(result) : null);
        String finalXmlReport = null;
        if (StringUtils.isNotEmpty(xmlMessageValidationReport)
            && xmlMessageValidationReport != null) {
          finalXmlReport =
              validationReportService.generateXmlTestStepValidationReport(
                  xmlMessageValidationReport, report);
        } else {
          if (StringUtils.isNotEmpty(report.getXml()) && report.getXml() != null) {
            finalXmlReport = validationReportService.updateXmlTestValidationReportElement(report);
          } else if (StringUtils.isNotEmpty(comments) || report.getResult() != null) {
            finalXmlReport =
                validationReportService.generateXmlTestStepValidationReport(null, report);
          }
        }
        if (finalXmlReport != null) {
          report.setXml(finalXmlReport);
          report.setHtml(validationReportService.generateHtml(finalXmlReport));
          validationReportService.save(report);
        }
      }

      return report;
    } catch (Exception e) {
      throw new ValidationReportException("Failed to generate the report");
    }
  }

  /**
   * Clear the user records related to a test step
   * 
   * @param testStepId
   * @param request
   * @return
   * @throws MessageValidationException
   */
  @ApiOperation(value = "", hidden = true)
  @RequestMapping(value = "/{testStepId}/clearRecords", method = RequestMethod.POST)
  public boolean clearRecords(
      @ApiParam(value = "the id of the test step", required = true) @PathVariable("testStepId") final Long testStepId,
      HttpServletRequest request) throws MessageValidationException {
    logger.info("Generating html validation report");
    Long userId = SessionContext.getCurrentUserId(request.getSession(false));
    if (userId == null || userService.findOne(userId) == null)
      throw new ValidationReportException("Invalid user credentials");
    TestStepValidationReport result =
        validationReportService.findOneByTestStepAndUser(testStepId, userId);
    if (result != null) {
      validationReportService.delete(result.getId());
    }
    return true;
  }



  public InputStream generatePdf(TestStepValidationReport report) {
    try {
      InputStream io = null;
      String xmlReport = report.getXml();
      io = validationReportService.generatePdf(xmlReport);
      return io;
    } catch (ValidationReportException e) {
      throw new ValidationReportException("Failed to generate the report pdf");
    } catch (Exception e) {
      throw new ValidationReportException("Failed to generate the report pdf");
    }
  }

  private String generateHtml(String xmlReport) {
    return validationReportService.generateHtml(xmlReport);
  }



}
