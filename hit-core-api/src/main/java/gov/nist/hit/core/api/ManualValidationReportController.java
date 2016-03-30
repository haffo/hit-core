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

import gov.nist.hit.core.service.TestStepService;
import gov.nist.hit.core.service.TestStepValidationReportService;
import gov.nist.hit.core.service.UserService;
import io.swagger.annotations.Api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RestController
@RequestMapping("/manual/report")
@Api(value = "Manual validation report api", tags = "Manual Validation Report")
public class ManualValidationReportController {

  static final Logger logger = LoggerFactory.getLogger(ManualValidationReportController.class);

  @Autowired
  private TestStepValidationReportService validationReportService;

  @Autowired
  private TestStepService testStepService;

  @Autowired
  private UserService userService;

  //
  // @ApiOperation(value = "Download a manual test step validation report by id",
  // nickname = "downloadManualTestStepValidationReportById")
  // @RequestMapping(value = "/teststep/{testStepId}/download", method = RequestMethod.POST,
  // consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  // public void downloadManualTestStepValidationReportById(
  // @ApiParam(value = "the targeted format", required = true) @RequestParam("format") String
  // format,
  // @ApiParam(value = "the id of the test step", required = true) @PathVariable("testStepId") Long
  // testStepId,
  // HttpServletRequest request, HttpServletResponse response) {
  // try {
  // logger.info("Downloading validation report  in " + format);
  // Long userId = SessionContext.getCurrentUserId(request.getSession(false));
  // User user = null;
  // if (userId == null || ((user = userService.findOne(userId)) == null))
  // throw new MessageValidationException("Invalid user credentials");
  // if (format == null)
  // throw new ValidationReportException("No format specified");
  // TestStepValidationReport report =
  // validationReportService.findOneByTestStepAndUser(testStepId, userId);
  // String xmlReport = null;
  // if (report == null || ((xmlReport = report.getXml()) == null)) {
  // throw new ValidationReportException("No validation report available for this test step");
  // }
  // TestStep testStep = report.getTestStep();
  // if (testStep == null)
  // throw new ValidationReportException("No associated test step found");
  // String title = testStep.getName().replaceAll(" ", "-");
  // String ext = format.toLowerCase();
  // InputStream io = null;
  // if ("HTML".equalsIgnoreCase(format)) {
  // io = IOUtils.toInputStream(manualHtml(xmlReport), "UTF-8");
  // response.setContentType("text/html");
  // } else if ("XML".equalsIgnoreCase(format)) {
  // io = IOUtils.toInputStream(xmlReport, "UTF-8");
  // response.setContentType("application/xml");
  // } else if ("PDF".equalsIgnoreCase(format)) {
  // io = validationReportService.generatePdf(xmlReport);
  // response.setContentType("application/pdf");
  // } else {
  // throw new ValidationReportException("Unsupported report format " + format);
  // }
  // title = title.replaceAll(" ", "-");
  // response.setHeader("Content-disposition", "attachment;filename=" + title
  // + "-ValidationReport." + ext);
  // FileCopyUtils.copy(io, response.getOutputStream());
  // } catch (ValidationReportException | IOException e) {
  // throw new ValidationReportException("Failed to download the report");
  // } catch (Exception e) {
  // throw new ValidationReportException("Failed to download the report");
  // }
  // }
  //
  // public InputStream generateManualPdf(TestStepValidationReport report) {
  // try {
  // String xmlReport = report.getXml();
  // InputStream io = validationReportService.generateManualPdf(xmlReport);
  // return io;
  // } catch (ValidationReportException e) {
  // throw new ValidationReportException("Failed to download the report");
  // } catch (Exception e) {
  // throw new ValidationReportException("Failed to download the report");
  // }
  // }
  //
  // @ApiOperation(hidden = true, value = "")
  // @RequestMapping(value = "/save", method = RequestMethod.POST)
  // public TestStepValidationReport saveManualReport(
  // @RequestBody ManualValidationResult validationResult, HttpServletRequest request,
  // HttpServletResponse response) {
  // try {
  // logger.info("Saving validation report");
  // Long userId = SessionContext.getCurrentUserId(request.getSession(false));
  // User user = null;
  // if (userId == null || ((user = userService.findOne(userId)) == null))
  // throw new MessageValidationException("Invalid user credentials");
  //
  // Long testStepId = validationResult.getTestStepId();
  // TestStep testStep = testStepService.findOne(testStepId);
  // if (testStepId == null || ((testStep = testStepService.findOne(testStepId)) == null))
  // throw new ValidationReportException("No test step or unknown test step specified");
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
  // report.setUser(user);
  // report.setComments(validationResult.getComments());
  // report.setResult(validationResult.getValue() != null ? TestResult.valueOf(validationResult
  // .getValue()) : null);
  // String xml = validationReportService.generateXmlTestStepValidationReport(null, report);
  // report.setXml(xml);
  // validationReportService.save(report);
  // return report;
  // } catch (ValidationReportException e) {
  // throw new ValidationReportException("Failed to download the report");
  // } catch (Exception e) {
  // throw new ValidationReportException("Failed to download the report");
  // }
  // }
  //
  // @ApiOperation(value = "Get the html format of an xml validation report", nickname = "toHtml")
  // @RequestMapping(value = "/html", method = RequestMethod.POST,
  // consumes = "application/x-www-form-urlencoded; charset=UTF-8", produces = "application/json")
  // public Map<String, String> toManualHTML(
  // @ApiParam(value = "the xml report", required = true) @RequestParam("xmlReport") String content,
  // HttpServletRequest request, HttpServletResponse response) {
  // try {
  // String html = validationReportService.generateManualHtml(content);
  // Map<String, String> res = new HashMap<String, String>(1);
  // res.put("html", html);
  // return res;
  // } catch (ValidationReportException e) {
  // throw new ValidationReportException("Failed to download the report");
  // } catch (Exception e) {
  // throw new ValidationReportException("Failed to download the report");
  // }
  // }

  // private String manualHtml(String xml) {
  // return validationReportService.generateManualHtml(xml);
  // }

}
