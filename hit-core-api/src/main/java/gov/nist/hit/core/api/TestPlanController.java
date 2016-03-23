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

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.repo.TestPlanRepository;
import gov.nist.hit.core.service.exception.TestPlanException;
import io.swagger.annotations.Api;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harold Affo (NIST)
 * 
 */

@RestController
@RequestMapping("/testplans")
@Api(value = "TestPlans", position = 1, description = "TestPlans API")
public class TestPlanController {

  static final Logger logger = LoggerFactory.getLogger(TestPlanController.class);

  @Autowired
  protected TestPlanRepository testPlanRepository;

  @RequestMapping(value = "/{testPlanId}")
  public TestPlan testPlan(@PathVariable final Long testPlanId) {
    logger.info("Fetching test plan with id=" + testPlanId);
    TestPlan testPlan = testPlanRepository.findOne(testPlanId);
    if (testPlan == null) {
      throw new TestPlanException(testPlanId);
    }
    return testPlan;
  }

  @RequestMapping(value = "/{testPlanId}/details", method = RequestMethod.GET)
  public Map<String, TestArtifact> details(@PathVariable final Long testPlanId) {
    logger.info("Fetching artifacts of testplan with id=" + testPlanId);
    TestPlan testPlan = testPlan(testPlanId);
    Map<String, TestArtifact> result = new HashMap<String, TestArtifact>();
    result.put("testStory", testPlan.getTestStory());
    return result;
  }



}
