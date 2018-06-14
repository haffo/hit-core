package gov.nist.hit.core.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gov.nist.hit.core.domain.AddOrUpdateRequest;
import gov.nist.hit.core.domain.CFTestStep;
import gov.nist.hit.core.domain.ResourceUploadStatus;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.service.ResourceLoader;
import gov.nist.hit.core.service.exception.NotFoundException;
import gov.nist.hit.core.service.exception.ProfileParserException;
import gov.nist.hit.core.service.util.ResourcebundleHelper;

@RestController
@RequestMapping("/editResources")
// @Api(value = "Add or Update", tags = "Add or Update resources", position =
// 10)
public class ResourceController {

	@Autowired
	@Qualifier("resourceLoader")
	private ResourceLoader resourceLoader;

	@ModelAttribute("requestObj")
	public AddOrUpdateRequest initRequest(Authentication u, @RequestPart("file") MultipartFile file,
			@RequestPart("id") Long id, @RequestPart("scope") String scope, @RequestPart("domain") String domain,
			HttpServletResponse resp) throws Exception {
		String zipFolder = "";
		if (!file.isEmpty()) {
			zipFolder = ResourcebundleHelper.getResourcesFromZip(file.getInputStream());
		}

		if (scope != null && scope.length() > 0) {
			try {
				TestScope.valueOf(scope);
			} catch (Exception e) {
				resp.sendError(500, "Invalid Scope");
			}
		}
		return new AddOrUpdateRequest(id, zipFolder, scope, domain);
	}

	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------- Context
	// Based API Methods
	// ----------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------

	// ***** TestStep *****

	// @ApiOperation(value = "Adds Test Step or Update if already exists",
	// nickname = "addOrUpdateTS")
	@PreAuthorize("hasRole('deployer') or hasRole('supervisor')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@RequestMapping(value = "/cb/addOrUpdate/testStep", method = RequestMethod.POST, produces = "application/json")
	public List<ResourceUploadStatus> addTestStep(@ModelAttribute("requestObj") AddOrUpdateRequest request,
			Authentication u) throws Exception {
		validateRequest(request);

		List<ResourceUploadStatus> results = new ArrayList<>();

		List<TestStep> steps = resourceLoader.createTS(request.getZip() + "/", request.getDomain(),
				TestScope.valueOf(request.getScope()), u.getName(), false);

		for (TestStep ts : steps) {
			ResourceUploadStatus tmp = resourceLoader.handleTS(request.getId(), ts);
			results.add(tmp);
		}

		FileUtils.deleteDirectory(new File(request.getZip()));
		return results;
	}

	// ***** TestCase *****

	// @ApiOperation(value = "Updates an existing Test Case", nickname
	// ="updateTC")
	@PreAuthorize("hasRole('deployer') or hasRole('supervisor')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@RequestMapping(value = "/cb/update/testCase", method = RequestMethod.POST, produces = "application/json")
	public List<ResourceUploadStatus> updateTestCase(@ModelAttribute("requestObj") AddOrUpdateRequest request,
			Authentication u) throws Exception {
		validateRequest(request);

		List<ResourceUploadStatus> results = new ArrayList<>();

		List<TestCase> cases = resourceLoader.createTC(request.getZip() + "/", request.getDomain(),
				TestScope.valueOf(request.getScope()), u.getName(), false);
		for (TestCase tc : cases) {
			ResourceUploadStatus tmp = resourceLoader.updateTC(tc);
			results.add(tmp);
		}

		FileUtils.deleteDirectory(new File(request.getZip()));
		return results;
	}

	// @ApiOperation(value = "Adds a Test Case to a Test Plan", nickname =
	// "addTCtoTP")
	@PreAuthorize("hasRole('deployer') or hasRole('supervisor')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@RequestMapping(value = "/cb/add/testCase/to/testPlan", method = RequestMethod.POST, produces = "application/json")
	public List<ResourceUploadStatus> addTestCaseToPlan(@ModelAttribute("requestObj") AddOrUpdateRequest request,
			Authentication u) throws Exception {
		validateRequest(request);

		List<ResourceUploadStatus> results = new ArrayList<>();

		List<TestCase> cases = resourceLoader.createTC(request.getZip() + "/", request.getDomain(),
				TestScope.valueOf(request.getScope()), u.getName(), false);
		for (TestCase tc : cases) {
			ResourceUploadStatus tmp = resourceLoader.addTC(request.getId(), tc, "plan");
			results.add(tmp);
		}

		FileUtils.deleteDirectory(new File(request.getZip()));
		return results;
	}

	// @ApiOperation(value = "Adds a Test Case to a Test Case Group", nickname =
	// "addTCtoTCG")
	@PreAuthorize("hasRole('deployer') or hasRole('supervisor')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@RequestMapping(value = "/cb/add/testCase/to/testCaseGroup", method = RequestMethod.POST, produces = "application/json")
	public List<ResourceUploadStatus> addTestCaseToGroup(@ModelAttribute("requestObj") AddOrUpdateRequest request,
			Authentication u) throws Exception {
		validateRequest(request);

		List<ResourceUploadStatus> results = new ArrayList<>();

		List<TestCase> cases = resourceLoader.createTC(request.getZip() + "/", request.getDomain(),
				TestScope.valueOf(request.getScope()), u.getName(), false);
		for (TestCase tc : cases) {
			ResourceUploadStatus tmp = resourceLoader.addTC(request.getId(), tc, "group");
			results.add(tmp);
		}

		FileUtils.deleteDirectory(new File(request.getZip()));
		return results;
	}

	// ***** TestCaseGroup *****

	// @ApiOperation(value = "Updates an existing Test Case Group", nickname
	// ="updateTCG")
	@PreAuthorize("hasRole('deployer') or hasRole('supervisor')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@RequestMapping(value = "/cb/update/testCaseGroup", method = RequestMethod.POST, produces = "application/json")
	public List<ResourceUploadStatus> updateTestGroup(@ModelAttribute("requestObj") AddOrUpdateRequest request,
			Authentication u) throws Exception {
		validateRequest(request);

		List<ResourceUploadStatus> results = new ArrayList<>();

		List<TestCaseGroup> groups = resourceLoader.createTCG(request.getZip() + "/", request.getDomain(),
				TestScope.valueOf(request.getScope()), u.getName(), false);
		for (TestCaseGroup tcg : groups) {
			ResourceUploadStatus tmp = resourceLoader.updateTCG(tcg);
			results.add(tmp);
		}

		FileUtils.deleteDirectory(new File(request.getZip()));
		return results;
	}

	// @ApiOperation(value = "Adds a Test Case Group to a Test Plan", nickname =
	// "addTCGtoTP")
	@PreAuthorize("hasRole('deployer') or hasRole('supervisor')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@RequestMapping(value = "/cb/add/testCaseGroup/to/testPlan", method = RequestMethod.POST, produces = "application/json")
	public List<ResourceUploadStatus> addTestGroupToPlan(@ModelAttribute("requestObj") AddOrUpdateRequest request,
			Authentication u) throws Exception {
		validateRequest(request);

		List<ResourceUploadStatus> results = new ArrayList<>();

		List<TestCaseGroup> groups = resourceLoader.createTCG(request.getZip() + "/", request.getDomain(),
				TestScope.valueOf(request.getScope()), u.getName(), false);
		for (TestCaseGroup tcg : groups) {
			ResourceUploadStatus tmp = resourceLoader.addTCG(request.getId(), tcg, "plan");
			results.add(tmp);
		}

		FileUtils.deleteDirectory(new File(request.getZip()));
		return results;
	}

	// @ApiOperation(value = "Adds a Test Case Group to a Test Case Group",
	// nickname = "addTCGtoTCG")
	@PreAuthorize("hasRole('deployer') or hasRole('supervisor')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@RequestMapping(value = "/cb/add/testCaseGroup/to/testCaseGroup", method = RequestMethod.POST, produces = "application/json")
	public List<ResourceUploadStatus> addTestGroupToGroup(@ModelAttribute("requestObj") AddOrUpdateRequest request,
			Authentication u) throws Exception {
		validateRequest(request);

		List<ResourceUploadStatus> results = new ArrayList<>();

		List<TestCaseGroup> groups = resourceLoader.createTCG(request.getZip() + "/", request.getDomain(),
				TestScope.valueOf(request.getScope()), u.getName(), false);
		for (TestCaseGroup tcg : groups) {
			ResourceUploadStatus tmp = resourceLoader.addTCG(request.getId(), tcg, "group");
			results.add(tmp);
		}

		FileUtils.deleteDirectory(new File(request.getZip()));
		return results;
	}

	// ***** TestPlan *****

	// @ApiOperation(value = "Adds Test Plan or Update if already exists",
	// nickname = "addOrUpdateTP")
	@PreAuthorize("hasRole('deployer') or hasRole('supervisor')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@RequestMapping(value = "/cb/addOrUpdate/testPlan", method = RequestMethod.POST, produces = "application/json")
	public List<ResourceUploadStatus> addOrUpdateTestPlan(Authentication u,
			@ModelAttribute("requestObj") AddOrUpdateRequest request, HttpServletResponse resp) throws Exception {
		validateRequest(request);

		String scope = request.getScope().replaceAll("\"", "");

		List<ResourceUploadStatus> results = new ArrayList<>();
		List<TestPlan> plans = resourceLoader.createTP(request.getZip() + "/", request.getDomain(),
				TestScope.valueOf(request.getScope()), u.getName(), false);

		if (scope == null) {
			resp.sendError(500, "Scope Argument is required");
		}

		for (TestPlan tp : plans) {
			ResourceUploadStatus tmp = resourceLoader.handleTP(tp);
			results.add(tmp);
		}

		FileUtils.deleteDirectory(new File(request.getZip()));
		return results;
	}

	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------- Context
	// Free API Methods
	// ----------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------

	// @ApiOperation(value = "Adds Context-Free Test Case or Update if already
	// exists", nickname = "cf_addOrUpdateTC")
	@PreAuthorize("hasRole('deployer') or hasRole('supervisor')")
	@CacheEvict(value = "HitCache", key = "'cf-testcases'", allEntries = true)
	@RequestMapping(value = "/cf/addOrUpdate/testCase", method = RequestMethod.POST, produces = "application/json")
	public List<ResourceUploadStatus> addCFTestCase(Authentication u,
			@ModelAttribute("requestObj") AddOrUpdateRequest request, HttpServletResponse resp) throws Exception {
		validateRequest(request);

		List<ResourceUploadStatus> results = new ArrayList<>();
		List<CFTestStep> cases = resourceLoader.createCFTC(request.getZip() + "/", request.getDomain(),
				TestScope.valueOf(request.getScope()), u.getName(), false);

		String scope = request.getScope().replaceAll("\"", "");
		if (scope == null) {
			resp.sendError(500, "Scope Argument is required");
		}

		for (CFTestStep tc : cases) {
			ResourceUploadStatus tmp = resourceLoader.handleCFTC(request.getId(), tc);
			results.add(tmp);
		}

		FileUtils.deleteDirectory(new File(request.getZip()));
		return results;

	}

	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------- Global
	// Resources API Methods
	// ----------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------

	// @ApiOperation(value = "Adds Profile or Update if already exists",
	// nickname = "addOrUpdateProfile")
	@PreAuthorize("hasRole('deployer') or hasRole('supervisor')")
	@RequestMapping(value = "/global/addOrUpdate/profile", method = RequestMethod.POST, produces = "application/json")
	public List<ResourceUploadStatus> addOrUpdateProfile(@ModelAttribute("requestObj") AddOrUpdateRequest request,
			Authentication u) throws IOException {
		validateRequest(request);

		List<ResourceUploadStatus> res = resourceLoader.addOrReplaceIntegrationProfile(request.getZip() + "/",
				request.getDomain(), TestScope.valueOf(request.getScope()), u.getName(), false);

		FileUtils.deleteDirectory(new File(request.getZip()));
		return res;
	}

	// @ApiOperation(value = "Adds Constraints or Update if already exists",
	// nickname = "addOrUpdateConstraints")
	@PreAuthorize("hasRole('deployer') or hasRole('supervisor')")
	@RequestMapping(value = "/global/addOrUpdate/constraints", method = RequestMethod.POST, produces = "application/json")
	public List<ResourceUploadStatus> addOrUpdateConstraints(@ModelAttribute("requestObj") AddOrUpdateRequest request,
			Authentication u) throws IOException {
		validateRequest(request);

		List<ResourceUploadStatus> res = resourceLoader.addOrReplaceConstraints(request.getZip() + "/",
				request.getDomain(), TestScope.valueOf(request.getScope()), u.getName(), false);

		FileUtils.deleteDirectory(new File(request.getZip()));
		return res;
	}

	// @ApiOperation(value = "Adds ValueSet or Update if already
	// exists",nickname = "addOrUpdateValueSet")
	@PreAuthorize("hasRole('deployer') or hasRole('supervisor')")
	@RequestMapping(value = "/global/addOrUpdate/valueSet", method = RequestMethod.POST, produces = "application/json")
	public List<ResourceUploadStatus> addOrUpdateValueSet(@ModelAttribute("requestObj") AddOrUpdateRequest request,
			Authentication u) throws IOException {
		validateRequest(request);
		List<ResourceUploadStatus> res = resourceLoader.addOrReplaceValueSet(request.getZip() + "/",
				request.getDomain(), TestScope.valueOf(request.getScope()), u.getName(), false);

		FileUtils.deleteDirectory(new File(request.getZip()));
		return res;
	}

	private void validateRequest(AddOrUpdateRequest request) {
		if (request.getDomain() == null || "".equals(request.getDomain())) {
			throw new IllegalArgumentException("Domain cannot be empty");
		}
		if (request.getScope() == null || "".equals(request.getScope())) {
			throw new IllegalArgumentException("Scope cannot be empty");
		}

		try {
			TestScope.valueOf(request.getScope());
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid Scope");
		}

	}

}
