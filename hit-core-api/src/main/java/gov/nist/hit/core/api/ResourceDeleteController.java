package gov.nist.hit.core.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.hit.core.domain.ResourceUploadStatus;
import gov.nist.hit.core.service.ResourceLoader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/deleteResources")
@Api(value = "Delete Resources", tags = "Delete Resources")
public class ResourceDeleteController {

	@Autowired
	private ResourceLoader resourceLoader;
	
	@PreAuthorize("hasRole('deployer')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@ApiOperation(value = "delete TestStep by id", nickname = "deleteTestStep")
	@RequestMapping(value = "/cb/delete/testStep/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResourceUploadStatus deleteTestStep(@PathVariable("id") Long id) {
		return resourceLoader.deleteTS(id);
	}
	
	@PreAuthorize("hasRole('deployer')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@ApiOperation(value = "delete TestCase by id", nickname = "deleteTestCase")
	@RequestMapping(value = "/cb/delete/testCase/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResourceUploadStatus deleteTestCase(@PathVariable("id") Long id) {
		return resourceLoader.deleteTC(id);
	}
	
	@PreAuthorize("hasRole('deployer')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@ApiOperation(value = "delete TestCaseGroup by id", nickname = "deleteTestCaseGroup")
	@RequestMapping(value = "/cb/delete/testCaseGroup/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResourceUploadStatus deleteTestCaseGroup(@PathVariable("id") Long id) {
		return resourceLoader.deleteTCG(id);
	}
	
	@PreAuthorize("hasRole('deployer')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@ApiOperation(value = "delete TestPlan by id", nickname = "deleteTestPlan")
	@RequestMapping(value = "/cb/delete/testPlan/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResourceUploadStatus deleteTestPlan(@PathVariable("id") Long id) {
		return resourceLoader.deleteTP(id);
	}
	
	@PreAuthorize("hasRole('deployer')")
	@CacheEvict(value = "HitCache", key = "'cb-testcases'", allEntries = true)
	@ApiOperation(value = "delete CF TestCase by id", nickname = "deleteCFTestCase")
	@RequestMapping(value = "/cf/delete/testCase/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResourceUploadStatus deleteCFTestCase(@PathVariable("id") Long id) {
		return resourceLoader.deleteCFTC(id);
	}
}
