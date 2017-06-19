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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.hit.core.domain.TestContext;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.repo.TestCaseRepository;
import gov.nist.hit.core.service.Streamer;
import gov.nist.hit.core.service.TestStepService;
import gov.nist.hit.core.service.exception.TestCaseException;
import io.swagger.annotations.ApiParam;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RequestMapping("/teststeps")
@RestController
public class TestStepController {

	Logger logger = LoggerFactory.getLogger(TestStepController.class);

	@Autowired
	protected TestCaseRepository testCaseRepository;

	@Autowired
	private TestStepService testStepService;

	@Autowired
	private Streamer streamer;

	/**
	 * find a test step by its id
	 * 
	 * @param teststepId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/{teststepId}", method = RequestMethod.GET, produces = "application/json")
	public TestStep testStep(HttpServletResponse response,
			@ApiParam(value = "the id of the test step", required = true) @PathVariable final Long teststepId)
			throws IOException {
		logger.info("Fetching test step with id=" + teststepId);
		TestStep testStep = findTestStep(teststepId);
		return testStep;
	}

	private TestStep findTestStep(Long teststepId) {
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
	 * @throws IOException
	 */
	@RequestMapping(value = "/{testStepId}/testcontext", method = RequestMethod.GET, produces = "application/json")
	public TestContext testContext(HttpServletResponse response,
			@ApiParam(value = "the id of the test step", required = true) @PathVariable final Long testStepId)
			throws IOException {
		logger.info("Fetching testContext from testStepId=" + testStepId);
		TestContext testContext = findTestStep(testStepId).getTestContext();
		if (testContext == null)
			throw new TestCaseException("No testcontext available for teststep id=" + testStepId);
		return testContext;
	}

	@RequestMapping(value = "/{testStepId}/details", method = RequestMethod.GET)
	public void details(HttpServletResponse response,
			@ApiParam(value = "the id of the test step", required = true) @PathVariable final Long testStepId)
			throws IOException {
		logger.info("Fetching artifacts of teststep with id=" + testStepId);
		TestStep testStep = findTestStep(testStepId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("jurorDocument", testStep.getJurorDocument());
		result.put("messageContent", testStep.getMessageContent());
		result.put("testDataSpecification", testStep.getTestDataSpecification());
		result.put("testStory", testStep.getTestStory());
		result.put("supplements", testStep.getSupplements());
		streamer.stream(response.getOutputStream(), result);
	}

	@RequestMapping(value = "/{testStepId}/jurordocument", method = RequestMethod.GET)
	public void tcJurordocument(HttpServletResponse response, @PathVariable final Long testStepId) throws IOException {
		logger.info("Fetching juror document of testcase/teststep with id=" + testStepId);
		streamer.stream(response.getOutputStream(), testStepService.jurorDocument(testStepId));
	}

	@RequestMapping(value = "/{testStepId}/messagecontent", method = RequestMethod.GET)
	public void messageContent(HttpServletResponse response, @PathVariable final Long testStepId) throws IOException {
		logger.info("Fetching messagecontent of testcase/teststep with id=" + testStepId);
		streamer.stream(response.getOutputStream(), testStepService.messageContent(testStepId));
	}

	@RequestMapping(value = "/{testStepId}/teststory", method = RequestMethod.GET)
	public void testStory(HttpServletResponse response, @PathVariable final Long testStepId) throws IOException {
		logger.info("Fetching teststory of testcase/teststep with id=" + testStepId);
		streamer.stream(response.getOutputStream(), testStepService.testStory(testStepId));
	}

	@RequestMapping(value = "/{testStepId}/tds", method = RequestMethod.GET)
	public void tcTestDataSpecification(HttpServletResponse response, @PathVariable final Long testStepId)
			throws IOException {
		logger.info("Fetching testDataSpecification of testcase/teststep with id=" + testStepId);
		streamer.stream(response.getOutputStream(), testStepService.testDataSpecification(testStepId));
	}

}
