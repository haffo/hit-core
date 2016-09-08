package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.CFTestInstance;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.service.exception.NotFoundException;
import gov.nist.hit.core.service.exception.ProfileParserException;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ResourceLoader {

    public void loadAppInfo() throws JsonProcessingException, IOException;
    public void loadConstraints() throws IOException;
    public void loadVocabularyLibraries() throws IOException;
    public void loadIntegrationProfiles() throws IOException;
    public void loadContextFreeTestCases() throws IOException, ProfileParserException;
    public void loadContextBasedTestCases() throws IOException;
    public void loadTestCasesDocumentation() throws IOException;
    public void loadUserDocs() throws IOException;
    public void loadKownIssues() throws IOException;
    public void loadReleaseNotes() throws IOException;
    public void loadResourcesDocs() throws IOException;
    public void loadToolDownloads() throws IOException;
    public void loadTransport() throws IOException;

	public void setDirectory(String dir);

	public String getDirectory();

	public List<Resource> getDirectories(String pattern) throws IOException;

	public Resource getResource(String pattern) throws IOException;

	public List<Resource> getResources(String pattern) throws IOException;

	public void addOrReplaceValueSet() throws IOException;

	public void addOrReplaceConstraints() throws IOException;

	public void addOrReplaceIntegrationProfile() throws IOException;

	public void handleCFTC(Long testCaseId, CFTestInstance tc) throws NotFoundException;

	public void handleTP(TestPlan tp);
	
	public void addTCG(Long parentId, TestCaseGroup tcg, String where) throws NotFoundException;
	
	public void updateTCG(TestCaseGroup tcg) throws NotFoundException;
	
	public void addTC(Long parentId, TestCase tc, String where) throws NotFoundException;
	
	public void updateTC(TestCase tc) throws NotFoundException;
	
	public void handleTS(Long testCaseId, TestStep ts) throws NotFoundException;
	
	public List<TestPlan> createTP() throws IOException;
	
	public List<TestCaseGroup> createTCG() throws IOException;
	
	public List<TestCase> createTC() throws IOException;

	public List<TestStep> createTS() throws IOException;
	
	public List<CFTestInstance> createCFTC() throws IOException;


}
