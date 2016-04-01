package gov.nist.hit.core.api;

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


import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.domain.User;
import gov.nist.hit.core.service.MessageValidationReportService;
import gov.nist.hit.core.service.TestCaseService;
import gov.nist.hit.core.service.TestStepService;
import gov.nist.hit.core.service.UserService;
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
@RequestMapping("/messageValidationReport")
@Api(value = "Message validation report api", tags = "Message Validation Report")
public class MessageValidationReportController {

  static final Logger logger = LoggerFactory.getLogger(MessageValidationReportController.class);

  @Autowired
  private MessageValidationReportService validationReportService;

  @Autowired
  private TestStepService testStepService;

  @Autowired
  private TestCaseService testCaseService;

  @Autowired
  private UserService userService;

  @ApiOperation(value = "", hidden = true)
  @RequestMapping(value = "/create", method = RequestMethod.POST,
      consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public TestStepValidationReport save(
      @ApiParam(value = "the xml validation report", required = true) @RequestParam("xmlMessageValidationReport") String xmlMessageValidationReport,
      @ApiParam(value = "the id of the test step", required = true) @RequestParam("testStepId") Long testStepId,
      HttpServletRequest request, HttpServletResponse response) {
    try {
      logger.info("Saving validation report");
      Long userId = SessionContext.getCurrentUserId(request.getSession(false));
      User user = null;
      if (userId == null || ((user = userService.findOne(userId)) == null))
        throw new MessageValidationException("Invalid user credentials");
      TestStep testStep = null;
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
      report.setUser(user);
      report.setXml(xmlMessageValidationReport);
      validationReportService.save(report);
      return report;
    } catch (ValidationReportException e) {
      throw new ValidationReportException("Failed to download the report");
    } catch (Exception e) {
      throw new ValidationReportException("Failed to download the report");
    }
  }

  @ApiOperation(value = "Download the message validation report of a test step by its id",
      nickname = "download",
      produces = "text/html,application/msword,application/xml,application/pdf")
  @RequestMapping(value = "/{messageValidationReportId}/download", method = RequestMethod.POST,
      consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public void download(
      @ApiParam(value = "the targeted format (html,pdf etc...)", required = true) @RequestParam("format") String format,
      @ApiParam(value = "the id of the validation report", required = true) @PathVariable("messageValidationReportId") Long testStepValidationReportId,
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
      if (report.getUser() == null || !userId.equals(report.getUser().getId())) {
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
      throw new ValidationReportException("Failed to download the report");
    } catch (Exception e) {
      throw new ValidationReportException("Failed to download the report");
    }
  }


  private String generateHtml(String xmlReport) {
    return validationReportService.generateHtml(xmlReport);
  }


}
