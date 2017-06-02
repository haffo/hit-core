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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.service.TestCaseService;
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

	/**
	 * find a test case by its id
	 * 
	 * @param testCaseId
	 * @return
	 */
	@RequestMapping(value = "/{testCaseId}", method = RequestMethod.GET, produces = "application/json")
	public TestCase testCase(@PathVariable final Long testCaseId) {
		logger.info("Fetching testCase with id=" + testCaseId);
		TestCase testCase = testCaseService.findOne(testCaseId);
		if (testCase == null) {
			throw new TestCaseException(testCaseId);
		}
		return testCase;
	}

	/**
	 * 
	 * @param testCaseId
	 * @return
	 */
	@RequestMapping(value = "/{testCaseId}/details", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Object> getTestCaseDetailsById(@PathVariable("testCaseId") final Long testCaseId) {
		Map<String, Object> result = new HashMap<String, Object>();
		logger.info("Fetching testcase " + testCaseId + " artifacts ");
		TestCase testCase = testCase(testCaseId);
		result.put("testStory", testCase.getTestStory());
		result.put("jurorDocument", testCase.getJurorDocument());
		result.put("supplements", testCase.getSupplements());

		return result;
	}

	/**
	 * 
	 * @param testCaseId
	 * @return
	 */
	@RequestMapping(value = "/{testCaseId}/teststory", method = RequestMethod.GET)
	public TestArtifact tcTestStory(@PathVariable("testCaseId") Long testCaseId) {
		logger.info("Fetching teststory of testcase/teststep with id=" + testCaseId);
		return testCaseService.testStory(testCaseId);
	}

}
