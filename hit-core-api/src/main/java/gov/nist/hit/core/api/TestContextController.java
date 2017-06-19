package gov.nist.hit.core.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gov.nist.auth.hit.core.domain.Account;
import gov.nist.hit.core.domain.MessageModel;
import gov.nist.hit.core.domain.MessageParserCommand;
import gov.nist.hit.core.domain.MessageValidationCommand;
import gov.nist.hit.core.domain.MessageValidationResult;
import gov.nist.hit.core.domain.TestContext;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.MessageParser;
import gov.nist.hit.core.service.MessageValidator;
import gov.nist.hit.core.service.Streamer;
import gov.nist.hit.core.service.TestStepService;
import gov.nist.hit.core.service.TestStepValidationReportService;
import gov.nist.hit.core.service.ValidationReportConverter;
import gov.nist.hit.core.service.exception.MessageParserException;
import gov.nist.hit.core.service.exception.MessageValidationException;
import gov.nist.hit.core.service.exception.TestCaseException;
import gov.nist.hit.core.service.exception.ValidationReportException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "Test Context", tags = "Test Context Controller")
public abstract class TestContextController {

	Logger logger = LoggerFactory.getLogger(TestContextController.class);

	@Autowired
	private TestStepValidationReportService validationReportService;

	@Autowired
	private TestStepService testStepService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private Streamer streamer;

	public abstract MessageValidator getMessageValidator();

	public abstract MessageParser getMessageParser();

	public abstract TestContext getTestContext(Long testContextId);

	public abstract ValidationReportConverter getValidatioReportConverter();

	@ApiOperation(value = "Get a test context by its id", nickname = "getTestContextById")
	@RequestMapping(value = "/{testContextId}", produces = "application/json", method = RequestMethod.GET)
	public void getTestContextById(HttpServletResponse response,
			@ApiParam(value = "the id of the test context", required = true) @PathVariable final Long testContextId) {
		try {
			logger.info("Fetching testContext with id=" + testContextId);
			TestContext testContext = getTestContext(testContextId);
			if (testContext == null) {
				throw new TestCaseException("No test context available with id=" + testContextId);
			}
			streamer.stream(response.getOutputStream(), testContext);
		} catch (IOException ex) {
			throw new TestCaseException("Failed to retrieve the test context");
		}
	}

	@ApiOperation(value = "Parse a message in a test context", nickname = "parseMessageWithTestContext")
	@RequestMapping(value = "/{testContextId}/parseMessage", method = RequestMethod.POST, produces = "application/json")
	public void parseMessage(
			@ApiParam(value = "the id of the test context", required = true) @PathVariable final Long testContextId,
			@ApiParam(value = "the request to be parsed", required = true) @RequestBody final MessageParserCommand command,
			HttpServletRequest request, HttpServletResponse response) throws MessageParserException {
		try {
			logger.info("Parsing message");
			MessageModel result = getMessageParser().parse(getTestContext(testContextId), command);
			streamer.stream(response.getOutputStream(), result);
		} catch (IOException ex) {
			throw new MessageParserException("Failed to parse the message");
		}
	}

	@ApiOperation(value = "Validate a message in a test context", nickname = "validateMessageWithTestContext")
	@RequestMapping(value = "/{testContextId}/validateMessage", method = RequestMethod.POST, produces = "application/json")
	public void validateMessage(
			@ApiParam(value = "the id of the test context", required = true) @PathVariable final Long testContextId,
			@ApiParam(value = "the request to be validated", required = true) @RequestBody final MessageValidationCommand command,
			HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws MessageValidationException {
		try {
			logger.info("Validating a message");
			TestContext testContext = getTestContext(testContextId);
			MessageValidationResult result = getMessageValidator().validate(testContext, command);
			TestStepValidationReport report = saveReport(testContextId, result,
					SessionContext.getCurrentUserId(request.getSession(false)));
			result.setHtml(null);
			result.setXml(null);
			result.setReportId(report.getId());
			streamer.stream(response.getOutputStream(), result);
		} catch (IOException ex) {
			throw new MessageValidationException("Failed to validate the message");
		} catch (Exception e) {
			throw new MessageValidationException(e);
		}
	}

	/**
	 * Save the report
	 * 
	 * @param testContextId:
	 *            id of the current context
	 * @param result:
	 *            validation result
	 * @param userId:
	 *            id of the user in session
	 * @return: the saved report
	 * @throws Exception
	 */
	private TestStepValidationReport saveReport(Long testContextId, MessageValidationResult result, Long userId)
			throws Exception {
		TestStep testStep = testStepService.findOneByTestContext(testContextId);
		Account account = null;
		if (userId == null || (account = accountService.findOne(userId)) == null) {
			logger.error("User not found");
			throw new ValidationReportException("Invalid user credentials");
		}
		TestStepValidationReport report = findUserTestStepValidationReportByTestStep(testStep.getId(), account.getId());
		if (report == null) {
			report = new TestStepValidationReport();
			report.setTestStep(testStep);
			report.setUserId(account.getId());
		}
		report.setXml((getValidatioReportConverter().toXML(result.getJson())));
		report.setJson(result.getJson());
		report.setHtml(null);
		return validationReportService.save(report);
	}

	private TestStepValidationReport findUserTestStepValidationReportByTestStep(Long testStepId, Long userId) {
		TestStepValidationReport report = null;
		List<TestStepValidationReport> reports = validationReportService.findAllByTestStepAndUser(testStepId, userId);
		if (reports != null && !reports.isEmpty()) {
			if (reports.size() == 1) {
				report = reports.get(0);
			} else {
				validationReportService.delete(reports);
			}
		}
		return report;
	}

}
