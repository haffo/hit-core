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

import gov.nist.hit.core.domain.MessageValidationResult;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.User;
import gov.nist.hit.core.repo.TestStepRepository;
import gov.nist.hit.core.service.TestStepService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.ValidationReportGenerator;
import gov.nist.hit.core.service.MessageValidationResultService;
import gov.nist.hit.core.service.exception.ValidationReportException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
public abstract class ValidationReportController {

  static final Logger logger = LoggerFactory.getLogger(ValidationReportController.class);


  public abstract ValidationReportGenerator getValidationReportGenerator();


  @Autowired
  private MessageValidationResultService validationResultService;

  @Autowired
  private TestStepService testStepService;

  @Autowired
  private UserService userService;

  @RequestMapping(value = "/generate", method = RequestMethod.POST,
      consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public Map<String, String> generate(@RequestParam("xmlReport") final String xmlReport) {
    logger.info("Generating html validation report");
    if (xmlReport == null) {
      throw new ValidationReportException("No xml report provided");
    }
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("htmlReport", createHtml(xmlReport));
    return map;
  }

  private String createHtml(String xmlReport) {
    String htmlReport = getValidationReportGenerator().toHTML(xmlReport);
    return htmlReport;
  }

  @RequestMapping(value = "/{resultId}/download", method = RequestMethod.POST,
      consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public void download(@RequestParam("format") String format,
      @PathVariable("resultId") Long resultId, HttpServletRequest request,
      HttpServletResponse response) {
    try {
      logger.info("Downloading validation report " + resultId + " in " + format);

      if(format == null)
        throw new ValidationReportException("No format specified");
      MessageValidationResult result = validationResultService.findOne(resultId);
      if (result == null)
        throw new ValidationReportException("No validation report found");
      TestStep testStep =  result.getTestStep();
      if(testStep == null)
           throw new ValidationReportException("No associated test step found");
      String title = testStep.getName();
      String ext = format.toLowerCase();
      String content = result.getJson();
      String xmlReport = getValidationReportGenerator().toXML(content);
      if (xmlReport == null) {
        throw new ValidationReportException("Cannot parse the report");
      }
      InputStream io = null;
      if ("HTML".equalsIgnoreCase(format)) {
        io = IOUtils.toInputStream(createHtml(xmlReport), "UTF-8");
        response.setContentType("text/html");
      } else if ("DOC".equalsIgnoreCase(format)) {
        io = IOUtils.toInputStream(createHtml(xmlReport), "UTF-8");
        response.setContentType("application/msword");
      } else if ("XML".equalsIgnoreCase(format)) {
        io = IOUtils.toInputStream(xmlReport, "UTF-8");
        response.setContentType("application/xml");
      } else if ("PDF".equalsIgnoreCase(format)) {
        io = getValidationReportGenerator().toPDF(xmlReport);
        response.setContentType("application/pdf");
      } else {
        throw new ValidationReportException("Unsupported report format " + format);
      }
      response.setHeader("Content-disposition", "attachment;filename=" + title
          + "-ValidationReport." + ext);
      FileCopyUtils.copy(io, response.getOutputStream());
    } catch (ValidationReportException | IOException e) {
      throw new ValidationReportException("Failed to download the report");
    } catch (Exception e) {
      throw new ValidationReportException("Failed to download the report");
    }
  }

}
