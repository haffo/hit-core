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

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.service.Streamer;
import gov.nist.hit.core.service.TestCaseService;
import gov.nist.hit.core.service.UserIdService;
import gov.nist.hit.core.service.exception.TestCaseException;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RequestMapping("/testcases")
@RestController
public class TestCaseController {

	Logger logger = LoggerFactory.getLogger(TestCaseController.class);

	@Autowired
	private TestCaseService testCaseService;

	@Autowired
	private UserIdService userIdService;

	@Autowired
	private Streamer streamer;

	private TestCase findTestCase(Long testCaseId) {
		TestCase testCase = testCaseService.findOne(testCaseId);
		if (testCase == null) {
			throw new TestCaseException(testCaseId);
		}
		return testCase;
	}

	/**
	 * find a test case by its id
	 * 
	 * @param testCaseId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/{testCaseId}", method = RequestMethod.GET, produces = "application/json")
	public TestCase testCase(HttpServletResponse response, @PathVariable final Long testCaseId) throws IOException {
		logger.info("Fetching testCase with id=" + testCaseId);
		TestCase testCase = findTestCase(testCaseId);
		return testCase;
	}

	/**
	 * 
	 * @param testCaseId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/{testCaseId}/details", method = RequestMethod.GET, produces = "application/json")
	public void details(HttpServletResponse response, @PathVariable("testCaseId") final Long testCaseId)
			throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		logger.info("Fetching testcase " + testCaseId + " artifacts ");
		TestCase testCase = findTestCase(testCaseId);
		result.put("testStory", testCase.getTestStory());
		result.put("jurorDocument", testCase.getJurorDocument());
		result.put("supplements", testCase.getSupplements());
		streamer.stream(response.getOutputStream(), result);
	}

	/**
	 * 
	 * @param testCaseId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/{testCaseId}/teststory", method = RequestMethod.GET)
	public void tcTestStory(HttpServletResponse response, @PathVariable("testCaseId") Long testCaseId)
			throws IOException {
		logger.info("Fetching teststory of testcase/teststep with id=" + testCaseId);
		TestArtifact artifact = testCaseService.testStory(testCaseId);
		streamer.stream(response.getOutputStream(), artifact);

	}

}
