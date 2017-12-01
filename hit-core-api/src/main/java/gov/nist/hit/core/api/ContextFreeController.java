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
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.auth.hit.core.domain.Account;
import gov.nist.hit.core.domain.CFTestPlan;
import gov.nist.hit.core.domain.CFTestStep;
import gov.nist.hit.core.domain.ResourceUploadResult;
import gov.nist.hit.core.domain.ResourceUploadStatus;
import gov.nist.hit.core.domain.TestCaseWrapper;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.domain.UploadStatus;
import gov.nist.hit.core.domain.UploadedProfileModel;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.CFTestPlanService;
import gov.nist.hit.core.service.CFTestStepService;
import gov.nist.hit.core.service.ResourceLoader;
import gov.nist.hit.core.service.Streamer;
import gov.nist.hit.core.service.UserIdService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.exception.NoUserFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RequestMapping("/cf")
@RestController
@Api(value = "Context-free Testing", tags = "Context-free Testing", position = 1)
public class ContextFreeController {

	static final Logger logger = LoggerFactory.getLogger(ContextFreeController.class);

	public static final String CF_UPLOAD_DIR = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath() + "/cf";

	@Autowired
	private CFTestStepService testStepService;

	@Autowired
	private CFTestPlanService testPlanService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserIdService userIdService;

	@Autowired
	private ResourceLoader resouceLoader;

	@Autowired
	private Streamer streamer;

	@ApiOperation(value = "Find all context-free test plans by scope", nickname = "getTestPlansByScope")
	// @Cacheable(value = "HitCache", key = "'cb-testplans'")
	@RequestMapping(value = "/testplans", method = RequestMethod.GET, produces = "application/json")
	public void getTestPlansByScope(
			@ApiParam(value = "the scope of the test plans", required = false) @RequestParam(required = false) TestScope scope,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("Fetching context-free testplans of scope=" + scope + "...");
		List<CFTestPlan> results = null;
		scope = scope == null ? TestScope.GLOBAL : scope;
		if (TestScope.USER.equals(scope)) {
			Long userId = SessionContext.getCurrentUserId(request.getSession(false));
			if (userId != null) {
				Account account = accountService.findOne(userId);
				if (account != null) {
					results = testPlanService.findShortAllByStageAndAuthor(TestingStage.CF, account.getUsername(),
							scope);
				}
			}
		} else {
			results = testPlanService.findShortAllByStageAndScope(TestingStage.CF, scope);
		}
		streamer.stream2(response.getOutputStream(), results);
	}

	@ApiOperation(value = "Find a context-free test plan by its id", nickname = "getOneTestPlanById")
	@RequestMapping(value = "/testplans/{testPlanId}", method = RequestMethod.GET, produces = "application/json")
	public CFTestPlan testPlan(
			@ApiParam(value = "the id of the test plan", required = true) @PathVariable final Long testPlanId,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("Fetching  test case...");
		CFTestPlan testPlan = testPlanService.findOne(testPlanId);
		Long userId = SessionContext.getCurrentUserId(request.getSession(false));
		recordTestPlan(testPlan, userId);
		return testPlan;
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/groups", method = RequestMethod.GET, produces = "application/json")
	public List<CFTestPlan> getGroupsByScope(
			@ApiParam(value = "the scope of the test plans", required = false) @RequestParam(required = true) TestScope scope,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		scope = scope == null ? TestScope.GLOBAL : scope;
		String username = null;
		Long userId = SessionContext.getCurrentUserId(request.getSession(false));
		if (userId != null) {
			Account account = accountService.findOne(userId);
			if (account != null) {
				username = account.getUsername();
			}
		}
		return testPlanService.findShortAllByScopeAndUsername(scope, username);
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/groups/create", method = RequestMethod.POST, produces = "application/json")
	public CFTestPlan createGroup(HttpServletRequest request, @RequestParam("category") String category,
			@RequestParam("scope") TestScope scope, Principal p, @RequestParam("position") Integer position)
			throws NoUserFoundException {
		// String username = null;
		String username = userIdService.getCurrentUserName(p);
		if (username == null)
			throw new NoUserFoundException("User could not be found");

		if (scope == null)
			throw new NoUserFoundException("No scope provided");

		if (category == null)
			throw new NoUserFoundException("No category provided");

		if (scope.equals(TestScope.GLOBAL) && !userService.hasGlobalAuthorities(username)) {
			throw new NoUserFoundException("You do not have the permission to perform this task");
		}

		CFTestPlan testPlan = new CFTestPlan();
		testPlan.setAuthorUsername(username);
		testPlan.setCategory(category);
		testPlan.setDescription("Desc");
		testPlan.setName("Group" + new Date().getTime());
		testPlan.setPersistentId(new Date().getTime());
		testPlan.setPosition(position);
		testPlanService.save(testPlan);
		return testPlan;
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/groups/{groupId}/delete", method = RequestMethod.POST, produces = "application/json")
	public ResourceUploadStatus deleteGroup(HttpServletRequest request, @PathVariable("groupId") Long groupId,
			Principal p) throws Exception {
		// String username = null;
		String username = userIdService.getCurrentUserName(p);
		if (username == null)
			throw new NoUserFoundException("User could not be found");
		CFTestPlan testPlan = testPlanService.findOne(groupId);
		if (testPlan == null)
			throw new Exception("No Profile Group(" + groupId + ") found");
		TestScope scope = testPlan.getScope();
		if (scope.equals(TestScope.GLOBAL) && !userService.hasGlobalAuthorities(username)) {
			throw new NoUserFoundException("You do not have the permission to perform this task");
		}
		ResourceUploadStatus status = resouceLoader.deleteTP(testPlan.getPersistentId());
		return status;
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/groups/delete", method = RequestMethod.POST, produces = "application/json")
	public List<ResourceUploadStatus> createGroup(HttpServletRequest request,
			@RequestParam("groupIds") Set<Long> groupIds, Principal p) throws Exception {
		List<ResourceUploadStatus> status = new ArrayList<ResourceUploadStatus>();
		// String username = null;
		String username = userIdService.getCurrentUserName(p);
		if (username == null)
			throw new NoUserFoundException("User could not be found");

		for (Long groupId : groupIds) {
			status.add(deleteGroup(request, groupId, p));
		}
		return status;
	}

	@RequestMapping(value = "/categories", method = RequestMethod.GET, produces = "application/json")
	public Set<String> getTestPlanCategories(
			@ApiParam(value = "the scope of the test plans", required = false) @RequestParam(required = true) TestScope scope,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		Set<String> results = null;
		scope = scope == null ? TestScope.GLOBAL : scope;
		String username = null;
		Long userId = SessionContext.getCurrentUserId(request.getSession(false));
		if (userId != null) {
			Account account = accountService.findOne(userId);
			if (account != null) {
				username = account.getUsername();
			}
		}
		if (scope.equals(TestScope.GLOBAL)) {
			results = testPlanService.findAllCategoriesByScope(scope);
		} else {
			results = testPlanService.findAllCategoriesByScopeAndUser(scope, username);
		}
		return results;
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/groups/info", method = RequestMethod.POST)
	@ResponseBody
	public UploadStatus saveInfo(HttpServletRequest request, @RequestBody TestCaseWrapper wrapper, Principal p) {
		try {
			String username = userIdService.getCurrentUserName(p);
			if (username == null)
				throw new NoUserFoundException("User could not be found");
			if (wrapper.getScope() == null)
				throw new NoUserFoundException("Scope not be found");
			TestScope scope = TestScope.valueOf(wrapper.getScope().toUpperCase());
			if (scope.equals(TestScope.GLOBAL) && !userService.hasGlobalAuthorities(username)) {
				throw new NoUserFoundException("You do not have the permission to perform this task");
			}
			CFTestPlan testPlan = testPlanService.findOne(wrapper.getGroupId());
			if (testPlan == null)
				throw new Exception("Test Plan not found");

			if (wrapper.getCategory() != null)
				testPlan.setCategory(wrapper.getCategory());

			if (wrapper.getPosition() != -1) {
				testPlan.setPosition(wrapper.getPosition());
			}
			if (wrapper.getTestcasename() != null) {
				testPlan.setName(wrapper.getTestcasename());
			}

			if (wrapper.getTestcasedescription() != null) {
				testPlan.setName(wrapper.getTestcasedescription());
			}
		} catch (IOException e) {
			return new UploadStatus(ResourceUploadResult.FAILURE, "IO Error could not read files",
					ExceptionUtils.getStackTrace(e));
		} catch (NoUserFoundException e) {
			return new UploadStatus(ResourceUploadResult.FAILURE, "User could not be found",
					ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			return new UploadStatus(ResourceUploadResult.FAILURE, "An error occured while adding profiles",
					ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/groups/{id}/profiles", method = RequestMethod.GET, produces = "application/json")
	public List<UploadedProfileModel> getGroupProfiles(@PathVariable("groupId") Long groupId,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		CFTestPlan testPlan = testPlanService.findOne(groupId);
		if (testPlan != null) {
			Set<CFTestStep> steps = testPlan.getTestCases();
			List<UploadedProfileModel> models = new ArrayList<UploadedProfileModel>();
			for (CFTestStep step : steps) {
				UploadedProfileModel model = new UploadedProfileModel();
				model.setDescription(step.getDescription());
				model.setName(step.getName());
				model.setId(step.getId() + "");
				models.add(model);
			}
			return models;
		}
		return null;
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/categories", method = RequestMethod.POST, produces = "application/json")
	public boolean updateCategory(@RequestParam("category") String category, @RequestParam("groups") Set<Long> groups,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		testPlanService.updateCategory(groups, category);
		return true;
	}

	/**
	 * Clear files in tmp directory
	 * 
	 * @param request
	 *            Client request
	 * @param token
	 *            files' token
	 * @param p
	 *            Principal
	 * @return True/False as success indicator
	 * @throws NoUserFoundException
	 * @throws IOException
	 */
	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/tokens/{token}/delete", method = RequestMethod.POST)
	@ResponseBody
	public boolean clearFiles(ServletRequest request, @PathVariable("token") String token, Principal p)
			throws NoUserFoundException, IOException {
		Long userId = userIdService.getCurrentUserId(p);
		if (userId == null)
			throw new NoUserFoundException("User could not be found");
		FileUtils.deleteDirectory(new File(CF_UPLOAD_DIR + "/" + userId + "/" + token));
		return true;
	}

	private void recordTestPlan(CFTestPlan testPlan, Long userId) {
		if (testPlan != null && userId != null) {
			accountService.recordLastCFTestPlan(userId, testPlan.getPersistentId());
		}
	}

	public CFTestStepService getTestStepService() {
		return testStepService;
	}

	public void setTestStepService(CFTestStepService testStepService) {
		this.testStepService = testStepService;
	}

	public CFTestPlanService getTestPlanService() {
		return testPlanService;
	}

	public void setTestPlanService(CFTestPlanService testPlanService) {
		this.testPlanService = testPlanService;
	}

	public AccountService getUserService() {
		return accountService;
	}

	public void setUserService(AccountService accountService) {
		this.accountService = accountService;
	}

}
