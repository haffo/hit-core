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
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.repo.TestCaseGroupRepository;
import gov.nist.hit.core.service.exception.TestCaseGroupException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
@RequestMapping("/testcasegroups")
@Api(value = "TestCaseGroups", tags = "Test cases groups")
public class TestCaseGroupController {

  static final Logger logger = LoggerFactory.getLogger(TestCaseGroupController.class);

  @Autowired
  protected TestCaseGroupRepository testCaseGroupRepository;

  @ApiOperation(value = "Get a test case group by its id", nickname = "getTestCaseGroupById")
  @RequestMapping(value = "/{testCaseGroupId}")
  public TestCaseGroup getTestCaseGroupById(@ApiParam(value = "the id of the test case group",
      required = true) @PathVariable final Long testCaseGroupId) {
    logger.info("Fetching test case group with id=" + testCaseGroupId);
    TestCaseGroup testCaseGroup = testCaseGroupRepository.findOne(testCaseGroupId);
    if (testCaseGroup == null) {
      throw new TestCaseGroupException(testCaseGroupId);
    }
    return testCaseGroup;
  }

  @ApiOperation(value = "Get a test case group details by its id",
      nickname = "getTestCaseGroupDetailsById")
  @RequestMapping(value = "/{testCaseGroupId}/details", method = RequestMethod.GET,
      produces = "application/json")
  public Map<String, TestArtifact> details(@ApiParam(value = "the id of the test case group",
      required = true) @PathVariable final Long testCaseGroupId) {
    logger.info("Fetching artifacts of test case group with id=" + testCaseGroupId);
    TestCaseGroup testCaseGroup = getTestCaseGroupById(testCaseGroupId);
    Map<String, TestArtifact> result = new HashMap<String, TestArtifact>();
    result.put("testStory", testCaseGroup.getTestStory());
    return result;
  }

}
