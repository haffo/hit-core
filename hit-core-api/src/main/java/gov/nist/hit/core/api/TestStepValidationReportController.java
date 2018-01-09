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

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.auth.hit.core.domain.Account;
import gov.nist.auth.hit.core.domain.UserTestStepReport;
import gov.nist.auth.hit.core.service.UserTestStepReportService;
import gov.nist.hit.core.domain.TestResult;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.domain.TestStepValidationReportRequest;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.domain.UserTestStepReportRequest;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.Streamer;
import gov.nist.hit.core.service.TestStepService;
import gov.nist.hit.core.service.TestStepValidationReportService;
import gov.nist.hit.core.service.exception.MessageValidationException;
import gov.nist.hit.core.service.exception.TestStepException;
import gov.nist.hit.core.service.exception.UserNotFoundException;
import gov.nist.hit.core.service.exception.ValidationReportException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RestController
@RequestMapping("/tsReport")
@Api(value = "Test Step validation report api", tags = "Test Step Validation Report", position = 5)
public class TestStepValidationReportController {

	static final Logger logger = LoggerFactory.getLogger(TestStepValidationReportController.class);

	@Autowired
	private TestStepValidationReportService validationReportService;

	@Autowired
	private TestStepService testStepService;

	@Autowired
	private AccountService userService;

	@Autowired
	private UserTestStepReportService userTestStepReportService;

	@Autowired
	private Streamer streamer;

	@ApiOperation(value = "", hidden = true)
	@RequestMapping(value = "/downloadPersistentUserTestStepReport", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded; charset=UTF-8")
	public void downloadPersistentUserTestStepReport(
			@ApiParam(value = "the targeted format (html,pdf etc...)", required = true) @RequestParam("format") String format,
			@ApiParam(value = "the account id of the user", required = true) @RequestParam("accountId") final Long accountId,
			@ApiParam(value = "the id of the test step", required = true) @PathVariable("testStepId") Long testStepId,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			logger.info("Downloading validation report  in " + format);
			Long userId = SessionContext.getCurrentUserId(request.getSession(false));
			if (userId == null || (userService.findOne(userId) == null))
				throw new MessageValidationException("Invalid user credentials");
			if (format == null)
				throw new ValidationReportException("No format specified");
			TestStep testStep = testStepService.findOne(testStepId);
			if (testStep == null) {
				throw new TestStepException(testStepId);
			}
			UserTestStepReport userTestStepReport = userTestStepReportService.findOneByAccountAndTestStepId(accountId,
					testStep.getPersistentId());
			if (userTestStepReport == null) {
				logger.error("No testStep Report for account " + accountId + " and testStep " + testStepId);
				throw new ValidationReportException(
						"No testStepReport for account " + accountId + " and testStep " + testStepId);
			}
			if (userTestStepReport.getXml() == null) {
				throw new ValidationReportException("No validation report available for this test step");
			}
			String title = testStep.getName();
			String ext = format.toLowerCase();
			InputStream io = null;
			if ("HTML".equalsIgnoreCase(format)) {
				io = IOUtils.toInputStream(validationReportService.generateHtml(userTestStepReport.getXml()), "UTF-8");
				response.setContentType("text/html");
			} else if ("XML".equalsIgnoreCase(format)) {
				io = IOUtils.toInputStream(userTestStepReport.getXml(), "UTF-8");
				response.setContentType("application/xml");
			} else if ("PDF".equalsIgnoreCase(format)) {
				io = validationReportService.generatePdf(userTestStepReport.getXml());
				response.setContentType("application/pdf");
			} else {
				throw new ValidationReportException("Unsupported report format " + format);
			}
			title = title.replaceAll(" ", "-");
			response.setHeader("Content-disposition", "attachment;filename=" + title + "-ValidationReport." + ext);
			streamer.stream(response.getOutputStream(), io);
		} catch (ValidationReportException | IOException e) {
			throw new ValidationReportException("Failed to generate the report");
		} catch (Exception e) {
			throw new ValidationReportException("Failed to generate the report");
		}
	}

	@ApiOperation(value = "Download the message validation report of a test step by its id", nickname = "download", produces = "text/html,application/msword,application/xml,application/pdf", hidden = true)
	@RequestMapping(value = "/{testStepValidationReportId}/download", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded; charset=UTF-8")
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

			if (report == null) {
				throw new ValidationReportException("No validation report found for this test step");
			}

			if (report.getUserId() == null || !userId.equals(report.getUserId())) {
				throw new MessageValidationException("Forbidden access");
			}

			Long testStepId = report.getTestStepId();
			TestStep testStep = null;

			if (testStepId != null)
				testStep = testStepService.findOne(testStepId);

			if (testStep == null)
				throw new ValidationReportException("No associated test step found");

			String xmlReport = generateXml(report, testStep.getStage(), testStep);
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
			response.setHeader("Content-disposition", "attachment;filename=" + title + "-ValidationReport." + ext);
			streamer.stream(response.getOutputStream(), io);
		} catch (ValidationReportException | IOException e) {
			throw new ValidationReportException("Failed to generate the report");
		} catch (Exception e) {
			throw new ValidationReportException("Failed to generate the report");
		}
	}

	@ApiOperation(value = "", hidden = true)
	@RequestMapping(value = "/savePersistentUserTestStepReport", method = RequestMethod.POST, produces = "application/json")
	public UserTestStepReport savePersistentUserTestStepReport(@RequestBody UserTestStepReportRequest command,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("Saving persistent test step report");
		try {
			Long userId = SessionContext.getCurrentUserId(request.getSession(false));
			Account user = userService.findOne(userId);
			if (user == null) {
				logger.error("Account " + userId + " not found");
				throw new UserNotFoundException("Account " + userId + " not found");
			}
			Long testStepId = command.getTestStepId();
			TestStep testStep = testStepService.findOne(testStepId);
			if (testStep == null) {
				throw new TestStepException(testStepId);
			}
			TestStepValidationReport report = validationReportService
					.findOneByTestStepAndUser(testStep.getPersistentId(), userId);
			UserTestStepReport userTestStepReport = new UserTestStepReport(report.getXml(), report.getHtml(),
					testStep.getVersion(), user.getId(), testStep.getPersistentId(), report.getComments());
			userTestStepReport = userTestStepReportService.save(userTestStepReport);
			return userTestStepReport;
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@ApiOperation(value = "", hidden = true)
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public void saveReport(@RequestBody TestStepValidationReportRequest command, HttpServletRequest request,
			HttpServletResponse response) {
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
			if (testStepId == null || ((testStep = testStepService.findOne(testStepId)) == null))
				throw new ValidationReportException("No test step or unknown test step specified");
			Long testReportId = command.getReportId();
			TestStepValidationReport report = findReport(testReportId, testStepId, userId);
			if (report == null) {
				report = new TestStepValidationReport();
				report.setTestStepId(testStepId);
				report.setUserId(userId);
			}

			if (!userId.equals(report.getUserId()))
				throw new MessageValidationException("Invalid user credentials");
			report.setComments(comments);
			report.setTestStepId(testStepId);
			report.setResult(StringUtils.isNotEmpty(result) && result != null ? TestResult.valueOf(result) : null);
			validationReportService.save(report);
			String tmpXml = generateXml(report, testStep.getStage(), testStep);
			report.setHtml(validationReportService.generateHtml(tmpXml));
			report.setXml(null);
			report.setJson(null);
			streamer.stream(response.getOutputStream(), report);
		} catch (Exception e) {
			throw new ValidationReportException("Failed to generate the report");
		}
	}

	@ApiOperation(value = "Return the json format of the report by its id")
	@RequestMapping(value = "/json", method = RequestMethod.GET, produces = "application/json")
	public void getReportJson(@RequestParam Long testStepId, @RequestParam(required = false) Long testReportId,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			logger.info("Getting validation report json");
			TestStepValidationReport report = getValidationReport(testStepId, testReportId, request);
			streamer.stream(response.getOutputStream(), report != null ? report.getJson() : null);
		} catch (Exception e) {
			throw new ValidationReportException("Failed to generate the report");
		}
	}

	@ApiOperation(value = "Return the html format of the report by its id")
	@RequestMapping(value = "/html", method = RequestMethod.GET, produces = "application/json")
	public void getReportHtml(@RequestParam Long testStepId, @RequestParam(required = false) Long testReportId,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			logger.info("Getting validation report html");
			TestStepValidationReport report = getValidationReport(testStepId, testReportId, request);
			String html = report != null ? validationReportService.generateHtml(report.getXml()) : null;
			streamer.stream(response.getOutputStream(), html);
		} catch (Exception e) {
			throw new ValidationReportException("Failed to generate the report");
		}
	}

	private TestStepValidationReport getValidationReport(Long testStepId, Long testReportId, HttpServletRequest request)
			throws MessageValidationException {
		Long userId = SessionContext.getCurrentUserId(request.getSession(false));
		if (userId == null || ((userService.findOne(userId)) == null))
			throw new MessageValidationException("Invalid user credentials");
		if (testStepId == null || ((testStepService.findOne(testStepId)) == null))
			throw new ValidationReportException("No test step or unknown test step specified");
		TestStepValidationReport report = findReport(testReportId, testStepId, userId);
		if (userId == null || !userId.equals(report.getUserId()))
			throw new MessageValidationException("Invalid user credentials");
		return report;
	}

	private TestStepValidationReport findReport(Long testReportId, Long testStepId, Long userId) {
		if (testReportId != null) {
			return validationReportService.findOne(testReportId);
		} else {
			List<TestStepValidationReport> reports = validationReportService.findAllByTestStepAndUser(testStepId,
					userId);
			if (reports != null && reports.size() > 1) {
				Collections.sort(reports, new Comparator<TestStepValidationReport>() {
					@Override
					public int compare(TestStepValidationReport o1, TestStepValidationReport o2) {
						return o2.getDateUpdated().compareTo(o1.getDateUpdated());
					}
				});
				for (int i = 1; i < reports.size(); i++) {
					validationReportService.delete(reports.get(i).getId());
				}
			}
			if (reports.size() == 1)
				return reports.get(0);
		}

		return null;
	}

	private String generateXml(TestStepValidationReport report, TestingStage stage, TestStep testStep) {
		if (TestingStage.CF.equals(stage)) {
			return validationReportService.updateXmlTestValidationReportElement(report);
		} else {
			return validationReportService.generateXmlTestStepValidationReport(report.getXml(), report, testStep);
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
	@RequestMapping(value = "/{testStepId}/delete", method = RequestMethod.POST)
	public boolean clearRecords(
			@ApiParam(value = "the id of the test step", required = true) @PathVariable("testStepId") final Long testStepId,
			HttpServletRequest request) throws MessageValidationException {
		logger.info("Generating html validation report");
		Long userId = SessionContext.getCurrentUserId(request.getSession(false));
		if (userId == null || userService.findOne(userId) == null)
			throw new ValidationReportException("Invalid user credentials");
		List<TestStepValidationReport> results = validationReportService.findAllByTestStepAndUser(testStepId, userId);
		if (results != null) {
			for (TestStepValidationReport report : results) {
				validationReportService.delete(report.getId());
			}
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
