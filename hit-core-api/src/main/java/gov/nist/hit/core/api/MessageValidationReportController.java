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

import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestingType;
import gov.nist.hit.core.domain.User;
import gov.nist.hit.core.domain.ValidationReport;
import gov.nist.hit.core.service.TestCaseService;
import gov.nist.hit.core.service.TestStepService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.ValidationReportService;
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
@RequestMapping("/report")
@Api(value = "MessageValidationReport")
public class MessageValidationReportController {

  static final Logger logger = LoggerFactory.getLogger(MessageValidationReportController.class);

  @Autowired
  private ValidationReportService validationReportService;

  @Autowired
  private TestStepService testStepService;

  @Autowired
  private TestCaseService testCaseService;

  @Autowired
  private UserService userService;

  @ApiOperation(value = "", hidden = true)
  @RequestMapping(value = "/save", method = RequestMethod.POST,
      consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public ValidationReport save(
      @ApiParam(value = "the xml validation report", required = true) @RequestParam("xmlReport") String content,
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

      ValidationReport report = null;
      List<ValidationReport> reports =
          validationReportService.findAllByTestStepAndUser(testStepId, userId);
      if (reports != null && !reports.isEmpty()) {
        if (reports.size() == 1) {
          report = reports.get(0);
        } else {
          validationReportService.delete(reports);
          report = new ValidationReport();
        }
      } else {
        report = new ValidationReport();
      }
      report.setTestStep(testStep);
      report.setUser(user);
      report.setXml(content);
      validationReportService.save(report);
      return report;
    } catch (ValidationReportException e) {
      throw new ValidationReportException("Failed to download the report");
    } catch (Exception e) {
      throw new ValidationReportException("Failed to download the report");
    }
  }

  @ApiOperation(value = "Download the message validation report of a test step by its id",
      produces = "text/html,application/msword,application/xml,application/pdf")
  @RequestMapping(value = "/teststep/{testStepId}/download", method = RequestMethod.POST,
      consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public void download(
      @ApiParam(value = "the targeted format (html,pdf etc...)", required = true) @RequestParam("format") String format,
      @ApiParam(value = "the id of the test step", required = true) @PathVariable("testStepId") Long testStepId,
      HttpServletRequest request, HttpServletResponse response) {
    try {
      logger.info("Downloading validation report  in " + format);
      Long userId = SessionContext.getCurrentUserId(request.getSession(false));
      User user = null;
      if (userId == null || ((user = userService.findOne(userId)) == null))
        throw new MessageValidationException("Invalid user credentials");
      if (format == null)
        throw new ValidationReportException("No format specified");
      ValidationReport report =
          validationReportService.findOneByTestStepAndUser(testStepId, userId);
      String xmlReport = null;
      if (report == null || ((xmlReport = report.getXml()) == null)) {
        throw new ValidationReportException("No validation report available for this test step");
      }
      TestStep testStep = report.getTestStep();
      if (testStep == null)
        throw new ValidationReportException("No associated test step found");
      String title = testStep.getName();
      String ext = format.toLowerCase();
      InputStream io = null;
      if ("HTML".equalsIgnoreCase(format)) {
        io = IOUtils.toInputStream(autoHtml(xmlReport), "UTF-8");
        response.setContentType("text/html");
      } else if ("DOC".equalsIgnoreCase(format)) {
        io = IOUtils.toInputStream(autoHtml(xmlReport), "UTF-8");
        response.setContentType("application/msword");
      } else if ("XML".equalsIgnoreCase(format)) {
        io = IOUtils.toInputStream(xmlReport, "UTF-8");
        response.setContentType("application/xml");
      } else if ("PDF".equalsIgnoreCase(format)) {
        io = validationReportService.toAutoPDF(xmlReport);
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

  public InputStream pdf(ValidationReport report) {
    try {
      InputStream io = null;
      String xmlReport = report.getXml();
      TestStep testStep = report.getTestStep();
      if (testStep.getTestingType().equals(TestingType.SUT_MANUAL)
          || testStep.getTestingType().equals(TestingType.SUT_MANUAL)) {
        io = validationReportService.toManualPDF(xmlReport);
      } else {
        io = validationReportService.toAutoPDF(xmlReport);
      }
      return io;
    } catch (ValidationReportException e) {
      throw new ValidationReportException("Failed to generate the report pdf");
    } catch (Exception e) {
      throw new ValidationReportException("Failed to generate the report pdf");
    }
  }



  private String autoHtml(String xmlReport) {
    return validationReportService.toAutoHTML(xmlReport);
  }


}
