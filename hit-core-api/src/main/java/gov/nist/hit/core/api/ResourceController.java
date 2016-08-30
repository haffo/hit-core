package gov.nist.hit.core.api;

import java.io.IOException;
import java.util.List;

import gov.nist.hit.core.domain.AddOrUpdateRequest;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.service.ResourceLoader;
import gov.nist.hit.core.service.exception.NotFoundException;
import gov.nist.hit.core.service.exception.ProfileParserException;
import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
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
//		String zipFolder = ResourcebundleHelper.getResourcesFromZip(req
//				.getUrl());
//		req.setZip(zipFolder);
		return req;
	}

	@RequestMapping(value = "/reInit", method = RequestMethod.POST, produces = "application/json")
	public void reInitBundle(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		resourceLoader.loadAppInfo();
		resourceLoader.loadConstraints();
		resourceLoader.loadVocabularyLibraries();
		resourceLoader.loadIntegrationProfiles();
		resourceLoader.loadContextFreeTestCases();
		resourceLoader.loadContextBasedTestCases();
		resourceLoader.loadTestCasesDocumentation();
		resourceLoader.loadUserDocs();
		resourceLoader.loadKownIssues();
		resourceLoader.loadReleaseNotes();
		resourceLoader.loadResourcesDocs();
		resourceLoader.loadToolDownloads();
		resourceLoader.loadTransport();
		
	}

	@RequestMapping(value = "/cb/testGroup", method = RequestMethod.POST, produces = "application/json")
	public String addTestGroup(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		List<TestCaseGroup> groups = resourceLoader.createTCG();
		for(TestCaseGroup tcg : groups){
			resourceLoader.handleTCG(request.getId(), tcg);
		}
		
		return "";
		
	}
	
	@RequestMapping(value = "/cb/testCase", method = RequestMethod.POST, produces = "application/json")
	public String addTestCase(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		List<TestCase> cases = resourceLoader.createTC();
		for(TestCase tc : cases){
			resourceLoader.handleTCg(request.getId(), tc);
		}
		return "";
	}
	
	@RequestMapping(value = "/cb/testStep", method = RequestMethod.POST, produces = "application/json")
	public String addTestStep(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		List<TestStep> steps = resourceLoader.createTS();
		System.out.println("Number of created : "+steps.size());
		for(TestStep ts : steps){
			System.out.println("Handling : "+ts.getId());
			resourceLoader.handleTS(request.getId(), ts);
		}
		return "";
	}
	
	@RequestMapping(value = "/cb/testPlan", method = RequestMethod.POST, produces = "application/json")
	public String addTestPlan(@ModelAttribute("requestObj") AddOrUpdateRequest request) throws ProfileParserException, IOException, NotFoundException {
		
		System.out.println(request.getZip()+"/");
		resourceLoader.setDirectory(request.getZip() + "/");

		List<TestPlan> plans = resourceLoader.createTP();
		for(TestPlan tp : plans){
			resourceLoader.handleTP(tp);
		}
		return "";
	}
	

}
