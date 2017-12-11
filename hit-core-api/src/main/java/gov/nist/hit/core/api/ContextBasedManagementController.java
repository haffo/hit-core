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

import java.io.File;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.hit.core.domain.AbstractTestCase;
import gov.nist.hit.core.domain.ResourceType;
import gov.nist.hit.core.domain.ResourceUploadAction;
import gov.nist.hit.core.domain.ResourceUploadResult;
import gov.nist.hit.core.domain.ResourceUploadStatus;
import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.TestArtifactService;
import gov.nist.hit.core.service.TestCaseGroupService;
import gov.nist.hit.core.service.TestCaseService;
import gov.nist.hit.core.service.TestPlanService;
import gov.nist.hit.core.service.TestStepService;
import gov.nist.hit.core.service.UserIdService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.exception.NoUserFoundException;
import io.swagger.annotations.Api;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RequestMapping("/cb/management")
@RestController
@Api(value = "Context-based Testing", tags = "Context-free Testing", position = 1)
public class ContextBasedManagementController {

	static final Logger logger = LoggerFactory.getLogger(ContextBasedManagementController.class);

	public static final String CB_UPLOAD_DIR = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath() + "/cb";

	public static final String CB_RESOURCE_BUNDLE_DIR = new File(System.getProperty("RESOURCE_BUNDLE_DIR"))
			.getAbsolutePath() + "/cb";

	@Autowired
	private TestStepService testStepService;

	@Autowired
	private TestPlanService testPlanService;

	@Autowired
	private TestCaseService testCaseService;

	@Autowired
	private TestCaseGroupService testCaseGroupService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserIdService userIdService;

	@Value("${server.email}")
	private String SERVER_EMAIL;

	@Value("${mail.tool}")
	private String TOOL_NAME;

	@Autowired
	private TestArtifactService testArtifactService;

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/testCases/{testCaseId}/delete", method = RequestMethod.POST, produces = "application/json")
	public ResourceUploadStatus deleteTestCase(HttpServletRequest request, @PathVariable("testCaseId") Long testCaseId,
			Principal p) throws Exception {
		TestCase testCase = testCaseService.findOne(testCaseId);
		checkRight(testCaseId, testCase, p);
		testCaseService.delete(testCase);
		ResourceUploadStatus result = new ResourceUploadStatus();
		result.setType(ResourceType.TESTCASE);
		result.setAction(ResourceUploadAction.DELETE);
		result.setId(testCase.getId());
		result.setStatus(ResourceUploadResult.SUCCESS);
		return result;
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/testSteps/{testStepId}/delete", method = RequestMethod.POST, produces = "application/json")
	public ResourceUploadStatus deleteTestStep(HttpServletRequest request, @PathVariable("testStepId") Long testStepId,
			Principal p) throws Exception {
		TestStep testStep = testStepService.findOne(testStepId);
		checkRight(testStepId, testStep, p);
		testStepService.delete(testStep);
		ResourceUploadStatus result = new ResourceUploadStatus();
		result.setType(ResourceType.TESTSTEP);
		result.setAction(ResourceUploadAction.DELETE);
		result.setId(testStep.getId());
		result.setStatus(ResourceUploadResult.SUCCESS);
		return result;
	}

	@RequestMapping(value = "/testPlans/{testPlanId}/delete", method = RequestMethod.POST, produces = "application/json")
	public ResourceUploadStatus deleteTestPlan(HttpServletRequest request, @PathVariable("testPlanId") Long testPlanId,
			Principal p) throws Exception {
		TestPlan testPlan = testPlanService.findOne(testPlanId);
		checkRight(testPlanId, testPlan, p);
		testPlanService.delete(testPlan);
		ResourceUploadStatus result = new ResourceUploadStatus();
		result.setType(ResourceType.TESTSTEP);
		result.setAction(ResourceUploadAction.DELETE);
		result.setId(testPlan.getId());
		result.setStatus(ResourceUploadResult.SUCCESS);
		return result;
	}

	private void checkRight(Long id, AbstractTestCase testObject, Principal p) throws Exception {
		String username = userIdService.getCurrentUserName(p);
		if (username == null)
			throw new NoUserFoundException("User could not be found");
		if (testObject == null)
			throw new Exception("No " + testObject.getType() + " (" + id + ") found");
		TestScope scope = testObject.getScope();
		if (scope.equals(TestScope.GLOBAL) && !userService.hasGlobalAuthorities(username)) {
			throw new NoUserFoundException("You do not have the permission to perform this task");
		}

		if (!username.equals(testObject.getAuthorUsername())) {
			throw new NoUserFoundException("You do not have the permission to perform this task");
		}
	}

	@RequestMapping(value = "/testCaseGroups/{testCaseGroupId}/delete", method = RequestMethod.POST, produces = "application/json")
	public ResourceUploadStatus deleteTestCaseGroup(HttpServletRequest request,
			@PathVariable("testCaseGroupId") Long testCaseGroupId, Principal p) throws Exception {
		TestCaseGroup testCaseGroup = testCaseGroupService.findOne(testCaseGroupId);
		checkRight(testCaseGroupId, testCaseGroup, p);
		testCaseGroupService.delete(testCaseGroup);
		ResourceUploadStatus result = new ResourceUploadStatus();
		result.setType(ResourceType.TESTCASEGROUP);
		result.setAction(ResourceUploadAction.DELETE);
		result.setId(testCaseGroup.getId());
		result.setStatus(ResourceUploadResult.SUCCESS);
		return result;
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/artifacts/{artifactId}", method = RequestMethod.POST, produces = "application/json", consumes = {
			"application/x-www-form-urlencoded;" })
	public ResourceUploadStatus updateArtifact(HttpServletRequest request, @PathVariable("artifactId") Long artifactId,
			@RequestParam("content") String content, Principal p,
			@RequestParam(name = "token", required = false) String token) throws Exception {
		// String username = null;
		String username = userIdService.getCurrentUserName(p);
		if (username == null)
			throw new NoUserFoundException("User could not be found");
		TestArtifact testArtifact = testArtifactService.findOne(artifactId);
		if (testArtifact == null) {
			throw new NoUserFoundException("Artifact not found");
		}

		if (content != null) {
			testArtifact.setHtml(content);
		}

		testArtifactService.save(testArtifact);
		ResourceUploadStatus result = new ResourceUploadStatus();
		result.setType(ResourceType.TESTARTIFACT);
		result.setAction(ResourceUploadAction.UPDATE);
		result.setId(testArtifact.getId());
		result.setStatus(ResourceUploadResult.SUCCESS);
		return result;
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/artifacts/{artifactId}/delete", method = RequestMethod.POST, produces = "application/json")
	public ResourceUploadStatus deleteArtifact(HttpServletRequest request, @PathVariable("artifactId") Long artifactId,
			Principal p) throws Exception {
		// String username = null;
		String username = userIdService.getCurrentUserName(p);
		if (username == null)
			throw new NoUserFoundException("User could not be found");
		TestArtifact testArtifact = testArtifactService.findOne(artifactId);
		if (testArtifact == null) {
			throw new NoUserFoundException("Artifact not found");
		}
		testArtifactService.delete(testArtifact);
		ResourceUploadStatus result = new ResourceUploadStatus();
		result.setType(ResourceType.TESTARTIFACT);
		result.setAction(ResourceUploadAction.DELETE);
		result.setId(testArtifact.getId());
		result.setStatus(ResourceUploadResult.SUCCESS);
		return result;
	}

}
