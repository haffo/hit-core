package gov.nist.hit.core.api;

import gov.nist.hit.core.domain.MessageModel;
import gov.nist.hit.core.domain.MessageParserCommand;
import gov.nist.hit.core.domain.MessageValidationCommand;
import gov.nist.hit.core.domain.MessageValidationResult;
import gov.nist.hit.core.domain.TestContext;
import gov.nist.hit.core.service.MessageParser;
import gov.nist.hit.core.service.MessageValidator;
import gov.nist.hit.core.service.TestStepService;
import gov.nist.hit.core.service.TestStepValidationReportService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.ValidationReportConverter;
import gov.nist.hit.core.service.exception.MessageParserException;
import gov.nist.hit.core.service.exception.MessageValidationException;
import gov.nist.hit.core.service.exception.TestCaseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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


@Api(value = "TestContexts", tags = "Test contexts")
public abstract class TestContextController {

  Logger logger = LoggerFactory.getLogger(TestContextController.class);

  @Autowired
  private TestStepValidationReportService validationReportService;

  @Autowired
  private TestStepService testStepService;

  @Autowired
  private UserService userService;


  public abstract MessageValidator getMessageValidator();

  public abstract MessageParser getMessageParser();

  public abstract TestContext getTestContext(Long testContextId);

  public abstract ValidationReportConverter getValidatioReportConverter();


  @ApiOperation(value = "Get a test context by its id", nickname = "getTestContextById")
  @RequestMapping(value = "/{testContextId}", produces = "application/json",
      method = RequestMethod.GET)
  public TestContext getTestContextById(@ApiParam(value = "the id of the test context",
      required = true) @PathVariable final Long testContextId) {
    logger.info("Fetching testContext with id=" + testContextId);
    TestContext testContext = getTestContext(testContextId);
    if (testContext == null) {
      throw new TestCaseException("No test context available with id=" + testContextId);
    }
    return testContext;
  }

  @ApiOperation(value = "Parse a message in a test context",
      nickname = "parseMessageWithTestContext")
  @RequestMapping(value = "/{testContextId}/parseMessage", method = RequestMethod.POST,
      produces = "application/json")
  public MessageModel parseMessageWithTestContext(
      @ApiParam(value = "the id of the test context", required = true) @PathVariable final Long testContextId,
      @ApiParam(value = "the request to be parsed", required = true) @RequestBody final MessageParserCommand command)
      throws MessageParserException {
    logger.info("Parsing message");
    return getMessageParser().parse(getTestContext(testContextId), command);
  }


  @ApiOperation(value = "Validate a message in a test context",
      nickname = "validateMessageWithTestContex")
  @RequestMapping(value = "/{testContextId}/validateMessage", method = RequestMethod.POST,
      produces = "application/json")
  public MessageValidationResult validateMessageWithTestContex(
      @ApiParam(value = "the id of the test context", required = true) @PathVariable final Long testContextId,
      @ApiParam(value = "the request to be validated", required = true) @RequestBody final MessageValidationCommand command,
      HttpServletRequest request, HttpServletResponse response, HttpSession session)
      throws MessageValidationException {
    try {
      logger.info("Validating a message");
      MessageValidationResult result =
          getMessageValidator().validate(getTestContext(testContextId), command);
      result.setXml(getValidatioReportConverter().toXML(result.getJson()));
      return result;
    } catch (Exception e) {
      throw new MessageValidationException(e);
    }
  }

}
