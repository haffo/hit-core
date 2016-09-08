package gov.nist.hit.core.api;

import java.io.IOException;
import java.util.List;

import gov.nist.hit.core.domain.AddOrUpdateRequest;
import gov.nist.hit.core.domain.CFTestInstance;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.service.ResourceLoader;
import gov.nist.hit.core.service.exception.NotFoundException;
import gov.nist.hit.core.service.exception.ProfileParserException;
import gov.nist.hit.core.service.util.ResourcebundleHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/editResources")
@Api(value = "Add or Update", tags = "Add or Update resources")
public class ResourceController {

	@Autowired
	private ResourceLoader resourceLoader;
	

	@ModelAttribute("requestObj")
	public AddOrUpdateRequest initRequest(@RequestBody AddOrUpdateRequest req)
			throws Exception {
		String zipFolder = ResourcebundleHelper.getResourcesFromZip(req
				.getUrl());
		req.setZip(zipFolder);
		return req;
	}
	
// Context Based API Methods

	// Test Step
	
	@PreAuthorize("hasRole('tester')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@ApiOperation(value = "Adds Test Step or Update if already exists", nickname = "addOrUpdateTS")
	@RequestMapping(value = "/cb/addOrUpdate/testStep", method = RequestMethod.POST, produces = "application/json")
	public String addTestStep(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		List<TestStep> steps = resourceLoader.createTS();

		for(TestStep ts : steps){
			resourceLoader.handleTS(request.getId(), ts);
		}
		return "";
	}
	
	// Test Case
	
	@PreAuthorize("hasRole('tester')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@ApiOperation(value = "Updates an existing Test Case", nickname = "updateTC")
	@RequestMapping(value = "/cb/update/testCase", method = RequestMethod.POST, produces = "application/json")
	public String updateTestCase(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		List<TestCase> cases = resourceLoader.createTC();
		for(TestCase tc : cases){
			resourceLoader.updateTC(tc);
		}
		return "";
	}
	
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "Adds a Test Case to a Test Plan", nickname = "addTCtoTP")
	@RequestMapping(value = "/cb/add/testCase/to/testPlan", method = RequestMethod.POST, produces = "application/json")
	public String addTestCaseToPlan(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		List<TestCase> cases = resourceLoader.createTC();
		for(TestCase tc : cases){
			resourceLoader.addTC(request.getId(), tc, "plan");
		}
		return "";
	}
	
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "Adds a Test Case to a Test Case Group", nickname = "addTCtoTCG")
	@RequestMapping(value = "/cb/add/testCase/to/testCaseGroup", method = RequestMethod.POST, produces = "application/json")
	public String addTestCaseToGroup(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		List<TestCase> cases = resourceLoader.createTC();
		for(TestCase tc : cases){
			resourceLoader.addTC(request.getId(), tc, "group");
		}
		return "";
	}
	
	// Test Case Group
	
	@PreAuthorize("hasRole('tester')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@ApiOperation(value = "Updates an existing Test Case Group", nickname = "updateTCG")
	@RequestMapping(value = "/cb/update/testCaseGroup", method = RequestMethod.POST, produces = "application/json")
	public String updateTestGroup(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		List<TestCaseGroup> groups = resourceLoader.createTCG();
		for(TestCaseGroup tcg : groups){
			resourceLoader.updateTCG(tcg);
		}
		return "";
	}
	
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "Adds a Test Case Group to a Test Plan", nickname = "addTCGtoTP")
	@RequestMapping(value = "/cb/add/testCaseGroup/to/testPlan", method = RequestMethod.POST, produces = "application/json")
	public String addTestGroupToPlan(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		List<TestCaseGroup> groups = resourceLoader.createTCG();
		for(TestCaseGroup tcg : groups){
			resourceLoader.addTCG(request.getId(), tcg, "plan");
		}
		
		return "";
	}
	
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "Adds a Test Case Group to a Test Case Group", nickname = "addTCGtoTCG")
	@RequestMapping(value = "/cb/add/testCaseGroup/to/testCaseGroup", method = RequestMethod.POST, produces = "application/json")
	public String addTestGroupToGroup(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		List<TestCaseGroup> groups = resourceLoader.createTCG();
		for(TestCaseGroup tcg : groups){
			resourceLoader.addTCG(request.getId(), tcg, "group");
		}
		
		return "";
	}

	// Test Plan
	
	@PreAuthorize("hasRole('tester')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@ApiOperation(value = "Adds Test Plan or Update if already exists", nickname = "addOrUpdateTP")
	@RequestMapping(value = "/cb/addOrUpdate/testPlan", method = RequestMethod.POST, produces = "application/json")
	public String addOrUpdateTestPlan(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		List<TestPlan> plans = resourceLoader.createTP();
		for(TestPlan tp : plans){
			resourceLoader.handleTP(tp);
		}
		return "";
	}

// Context Free API Methods
	
	@PreAuthorize("hasRole('tester')")
	@CacheEvict(value = "HitCache", key = "'cf-testcases'", allEntries = true)
	@ApiOperation(value = "Adds Context-Free Test Case or Update if already exists", nickname = "cf_addOrUpdateTC")
	@RequestMapping(value = "/cf/addOrUpdate/testCase", method = RequestMethod.POST, produces = "application/json")
	public String addCFTestCase(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		List<CFTestInstance> cases = resourceLoader.createCFTC();
		for(CFTestInstance tc : cases){
			resourceLoader.handleCFTC(request.getId(), tc);
		}
		return "";
		
	}
	
// Global Resources API Methods
	
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "Adds Profile or Update if already exists", nickname = "addOrUpdateProfile")
	@RequestMapping(value = "/global/addOrUpdate/profile", method = RequestMethod.POST, produces = "application/json")
	public String addOrUpdateProfile(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		resourceLoader.addOrReplaceIntegrationProfile();
		return "";
	}
	
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "Adds Constraints or Update if already exists", nickname = "addOrUpdateConstraints")
	@RequestMapping(value = "/global/addOrUpdate/constraints", method = RequestMethod.POST, produces = "application/json")
	public String addOrUpdateConstraints(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		resourceLoader.addOrReplaceConstraints();
		return "";
	}
	
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "Adds ValueSet or Update if already exists", nickname = "addOrUpdateValueSet")
	@RequestMapping(value = "/addOrUpdate/valueSet", method = RequestMethod.POST, produces = "application/json")
	public String addOrUpdateValueSet(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		resourceLoader.addOrReplaceValueSet();
		return "";
	}


}
