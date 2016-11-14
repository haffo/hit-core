package gov.nist.hit.core.api;

import java.io.File;
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

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
		
		FileUtils.deleteDirectory(new File(request.getZip()));
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
		
		FileUtils.deleteDirectory(new File(request.getZip()));
		return "";
	}
	
	@PreAuthorize("hasRole('tester')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@ApiOperation(value = "Adds a Test Case to a Test Plan", nickname = "addTCtoTP")
	@RequestMapping(value = "/cb/add/testCase/to/testPlan", method = RequestMethod.POST, produces = "application/json")
	public String addTestCaseToPlan(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		List<TestCase> cases = resourceLoader.createTC();
		for(TestCase tc : cases){
			resourceLoader.addTC(request.getId(), tc, "plan");
		}
		
		FileUtils.deleteDirectory(new File(request.getZip()));
		return "";
	}
	
	@PreAuthorize("hasRole('tester')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@ApiOperation(value = "Adds a Test Case to a Test Case Group", nickname = "addTCtoTCG")
	@RequestMapping(value = "/cb/add/testCase/to/testCaseGroup", method = RequestMethod.POST, produces = "application/json")
	public String addTestCaseToGroup(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		List<TestCase> cases = resourceLoader.createTC();
		for(TestCase tc : cases){
			resourceLoader.addTC(request.getId(), tc, "group");
		}
		
		FileUtils.deleteDirectory(new File(request.getZip()));
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
		
		FileUtils.deleteDirectory(new File(request.getZip()));
		return "";
	}
	
	@PreAuthorize("hasRole('tester')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@ApiOperation(value = "Adds a Test Case Group to a Test Plan", nickname = "addTCGtoTP")
	@RequestMapping(value = "/cb/add/testCaseGroup/to/testPlan", method = RequestMethod.POST, produces = "application/json")
	public String addTestGroupToPlan(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		List<TestCaseGroup> groups = resourceLoader.createTCG();
		for(TestCaseGroup tcg : groups){
			resourceLoader.addTCG(request.getId(), tcg, "plan");
		}
		
		FileUtils.deleteDirectory(new File(request.getZip()));
		return "";
	}
	
	@PreAuthorize("hasRole('tester')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@ApiOperation(value = "Adds a Test Case Group to a Test Case Group", nickname = "addTCGtoTCG")
	@RequestMapping(value = "/cb/add/testCaseGroup/to/testCaseGroup", method = RequestMethod.POST, produces = "application/json")
	public String addTestGroupToGroup(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		List<TestCaseGroup> groups = resourceLoader.createTCG();
		for(TestCaseGroup tcg : groups){
			resourceLoader.addTCG(request.getId(), tcg, "group");
		}
		
		FileUtils.deleteDirectory(new File(request.getZip()));
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
		
		FileUtils.deleteDirectory(new File(request.getZip()));
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
		
		FileUtils.deleteDirectory(new File(request.getZip()));
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
		
		FileUtils.deleteDirectory(new File(request.getZip()));
		return "";
	}
	
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "Adds Constraints or Update if already exists", nickname = "addOrUpdateConstraints")
	@RequestMapping(value = "/global/addOrUpdate/constraints", method = RequestMethod.POST, produces = "application/json")
	public String addOrUpdateConstraints(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		resourceLoader.addOrReplaceConstraints();
		
		FileUtils.deleteDirectory(new File(request.getZip()));
		return "";
	}
	
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "Adds ValueSet or Update if already exists", nickname = "addOrUpdateValueSet")
	@RequestMapping(value = "/global/addOrUpdate/valueSet", method = RequestMethod.POST, produces = "application/json")
	public String addOrUpdateValueSet(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		resourceLoader.addOrReplaceValueSet();
		
		FileUtils.deleteDirectory(new File(request.getZip()));
		return "";
	}
	
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "delete TestStep by id", nickname = "deleteTestStep")
	@RequestMapping(value = "/cb/delete/testStep/{id}", method = RequestMethod.POST, produces = "application/json")
	public String deleteTestStep(@PathVariable("id") Long id) throws ProfileParserException, IOException, NotFoundException {
		resourceLoader.deleteTS(id);
		return "";
	}
	
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "delete TestCase by id", nickname = "deleteTestCase")
	@RequestMapping(value = "/cb/delete/testCase/{id}", method = RequestMethod.POST, produces = "application/json")
	public String deleteTestCase(@PathVariable("id") Long id) throws ProfileParserException, IOException, NotFoundException {
		resourceLoader.deleteTC(id);
		return "";
	}
	
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "delete TestCaseGroup by id", nickname = "deleteTestCaseGroup")
	@RequestMapping(value = "/cb/delete/testCaseGroup/{id}", method = RequestMethod.POST, produces = "application/json")
	public String deleteTestCaseGroup(@PathVariable("id") Long id) throws ProfileParserException, IOException, NotFoundException {
		resourceLoader.deleteTCG(id);
		return "";
	}
	
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "delete TestPlan by id", nickname = "deleteTestPlan")
	@RequestMapping(value = "/cb/delete/testPlan/{id}", method = RequestMethod.POST, produces = "application/json")
	public String deleteTestPlan(@PathVariable("id") Long id) throws ProfileParserException, IOException, NotFoundException {
		resourceLoader.deleteTP(id);
		return "";
	}
	
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "delete CF TestCase by id", nickname = "deleteCFTestCase")
	@RequestMapping(value = "/cf/delete/testCase/{id}", method = RequestMethod.POST, produces = "application/json")
	public String deleteCFTestCase(@PathVariable("id") Long id) throws ProfileParserException, IOException, NotFoundException {
		resourceLoader.deleteCFTC(id);
		return "";
	}


}
