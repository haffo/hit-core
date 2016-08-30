package gov.nist.hit.core.service;

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

	public void addOrReplaceCFTestCase() throws IOException;

	public void handleTS(Long testCaseId, TestStep ts) throws NotFoundException;

	public void handleTCg(Long testCaseGroup, TestCase tc)
			throws NotFoundException;

	public void handleTCG(Long testPlan, TestCaseGroup tcg)
			throws NotFoundException;

	public void handleTP(TestPlan tp);

	public List<TestStep> mergeTS(List<TestStep> newL, List<TestStep> oldL);

	public List<TestStep> createTS() throws IOException;

	public List<TestCase> createTC() throws IOException;

	public List<TestCaseGroup> createTCG() throws IOException;

	public List<TestPlan> createTP() throws IOException;

	public List<TestCase> mergeTC(List<TestCase> newL, List<TestCase> oldL);

	public List<TestCaseGroup> mergeTCG(List<TestCaseGroup> newL,
			List<TestCaseGroup> oldL);

}
