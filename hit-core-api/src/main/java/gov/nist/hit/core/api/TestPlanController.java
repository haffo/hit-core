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

import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.repo.TestPlanRepository;
import gov.nist.hit.core.service.Streamer;
import gov.nist.hit.core.service.exception.TestPlanException;
import io.swagger.annotations.ApiParam;

/**
 * @author Harold Affo (NIST)
 * 
 */

@RestController
@RequestMapping("/testplans")
public class TestPlanController {

	static final Logger logger = LoggerFactory.getLogger(TestPlanController.class);

	@Autowired
	protected TestPlanRepository testPlanRepository;

	@Autowired
	private Streamer streamer;

	@RequestMapping(value = "/{testPlanId}", method = RequestMethod.GET, produces = "application/json")
	public void testPlan(HttpServletResponse response,
			@ApiParam(value = "the id of the test plan", required = true) @PathVariable final Long testPlanId)
			throws IOException {
		logger.info("Fetching test plan with id=" + testPlanId);
		TestPlan testPlan = findTestPlan(testPlanId);
		streamer.stream(response.getOutputStream(), testPlan);
	}

	private TestPlan findTestPlan(Long testPlanId) {
		TestPlan testPlan = testPlanRepository.findOne(testPlanId);
		if (testPlan == null) {
			throw new TestPlanException(testPlanId);
		}
		return testPlan;
	}

	@RequestMapping(value = "/{testPlanId}/details", method = RequestMethod.GET, produces = "application/json")
	public void details(HttpServletResponse response,
			@ApiParam(value = "the id of the test plan", required = true) @PathVariable final Long testPlanId)
			throws IOException {
		logger.info("Fetching artifacts of testplan with id=" + testPlanId);
		TestPlan testPlan = findTestPlan(testPlanId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("testStory", testPlan.getTestStory());
		result.put("supplements", testPlan.getSupplements());
		streamer.stream(response.getOutputStream(), result);
	}

}
