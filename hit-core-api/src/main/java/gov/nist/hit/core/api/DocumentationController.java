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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.auth.hit.core.domain.Account;
import gov.nist.hit.core.domain.Document;
import gov.nist.hit.core.domain.DocumentType;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.repo.DocumentRepository;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.Streamer;
import gov.nist.hit.core.service.TestCaseDocumentationService;
import gov.nist.hit.core.service.ZipGenerator;
import gov.nist.hit.core.service.exception.DownloadDocumentException;
import io.swagger.annotations.ApiParam;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RequestMapping("/documentation")
@RestController
public class DocumentationController {

	static final Logger logger = LoggerFactory.getLogger(DocumentationController.class);

	@Autowired
	private TestCaseDocumentationService testCaseDocumentationService;

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private ZipGenerator zipGenerator;

	@Autowired
	private Streamer streamer;

	@Autowired
	private AccountService userService;

	// @Cacheable(value = "HitCache", key = "'testcases-documentation'")
	@RequestMapping(value = "/testcases", method = RequestMethod.GET, produces = "application/json")
	public void testCases(HttpServletResponse response, @RequestParam(required = true) String domain,
			@RequestParam(required = false) TestScope scope, HttpServletRequest request) throws IOException {
		logger.info("Fetching test case documentation");
		scope = scope == null ? TestScope.GLOBAL : scope;
		if (TestScope.USER.equals(scope)) {
			Long userId = SessionContext.getCurrentUserId(request.getSession(false));
			if (userId != null) {
				Account account = userService.findOne(userId);
				if (account != null) {
					streamer.stream(response.getOutputStream(),
							testCaseDocumentationService.findOneByStageAndDomainAndAuthorAndScope(TestingStage.CB,
									domain, account.getUsername(), scope));
				}
			}
		} else {
			streamer.stream(response.getOutputStream(),
					testCaseDocumentationService.findOneByStageAndDomainAndScope(TestingStage.CB, domain, scope));
		}
	}

	// @Cacheable(value = "HitCache", key = "'releasenotes'")
	@RequestMapping(value = "/releasenotes", method = RequestMethod.GET, produces = "application/json")
	public void releaseNotes(HttpServletResponse response) throws IOException {
		logger.info("Fetching  all release notes");
		streamer.streamDocs(response.getOutputStream(), documentRepository.findAllReleaseNotes());
	}

	// @Cacheable(value = "HitCache", key = "'userdocs'")
	@RequestMapping(value = "/userdocs", method = RequestMethod.GET, produces = "application/json")
	public void userDocs(HttpServletResponse response, @RequestParam(required = true) String domain,
			@RequestParam(required = false) TestScope scope, HttpServletRequest request) throws IOException {
		logger.info("Fetching  all release notes");
		scope = scope == null ? TestScope.GLOBAL : scope;
		if (TestScope.USER.equals(scope)) {
			Long userId = SessionContext.getCurrentUserId(request.getSession(false));
			if (userId != null) {
				Account account = userService.findOne(userId);
				if (account != null) {
					streamer.streamDocs(response.getOutputStream(), documentRepository
							.findAllUserDocsByDomainAndAuthorAndScope(domain, account.getUsername(), scope));
				}
			}
		} else {
			streamer.streamDocs(response.getOutputStream(),
					documentRepository.findAllUserDocsByDomainAndScope(domain, TestScope.GLOBAL));
		}
	}

	// @Cacheable(value = "HitCache", key = "'knownissues'")
	@RequestMapping(value = "/knownissues", method = RequestMethod.GET, produces = "application/json")
	public void knownIssues(HttpServletResponse response) throws IOException {
		logger.info("Fetching  all known issues");
		streamer.streamDocs(response.getOutputStream(), documentRepository.findAllKnownIssues());
	}

	// @Cacheable(value = "HitCache", key = "#type.name() +
	// 'resource-documentation'")
	@RequestMapping(value = "/resourcedocs", method = RequestMethod.GET, produces = "application/json")
	public void resourcedocs(@RequestParam("type") DocumentType type, HttpServletResponse response,
			@RequestParam(required = true) String domain, @RequestParam(required = false) TestScope scope,
			HttpServletRequest request) throws IOException {
		logger.info("Fetching all resources docs of type=" + type);
		scope = scope == null ? TestScope.GLOBAL : scope;
		if (TestScope.USER.equals(scope)) {
			Long userId = SessionContext.getCurrentUserId(request.getSession(false));
			if (userId != null) {
				Account account = userService.findOne(userId);
				if (account != null) {
					streamer.streamDocs(response.getOutputStream(),
							documentRepository.findAllResourceDocsByTypeAndDomainAndAuthorAndScope(type, domain,
									account.getUsername(), scope));
				}
			}
		} else {
			streamer.streamDocs(response.getOutputStream(),
					documentRepository.findAllResourceDocsByTypeAndDomainAndScope(type, domain, TestScope.GLOBAL));
		}

	}

	// @Cacheable(value = "HitCache", key = "'deliverables-documentation'")
	@RequestMapping(value = "/deliverables", method = RequestMethod.GET, produces = "application/json")
	public void toolDownloads(HttpServletResponse response) throws IOException {
		logger.info("Fetching all tooldownloads");
		streamer.streamDocs(response.getOutputStream(), documentRepository.findAllDeliverableDocs());
	}

	// @Cacheable(value = "HitCache", key = "'installationguide-documentation'")
	@RequestMapping(value = "/installationguide", method = RequestMethod.GET, produces = "application/json")
	public void installationGuide(HttpServletResponse response) throws IOException {
		logger.info("Fetching installation guide");
		streamer.stream(response.getOutputStream(), documentRepository.findInstallationDoc());
	}

	@RequestMapping(value = "/downloadDocument", method = RequestMethod.POST)
	public void downloadDocumentByPath(
			@ApiParam(value = "the path of the document", required = true) @RequestParam("path") String path,
			HttpServletRequest request, HttpServletResponse response) throws DownloadDocumentException {
		try {
			if (path != null) {
				String fileName = null;
				path = !path.startsWith("/") ? "/" + path : path;
				InputStream content = DocumentationController.class.getResourceAsStream(path);
				if (content != null) {
					fileName = path.substring(path.lastIndexOf("/") + 1);
					response.setContentType(getContentType(path));
					fileName = fileName.replaceAll(" ", "-");
					response.setHeader("Content-disposition", "attachment;filename=" + fileName);
					streamer.stream(response.getOutputStream(), content);
				}
			}
		} catch (IOException e) {
			logger.debug("Failed to download the test packages ");
			throw new DownloadDocumentException("Cannot download the document");
		}
	}

	@RequestMapping(value = "/downloadResourceDocs", method = RequestMethod.POST, produces = "application/zip")
	public void downloadResourceDocs(
			@ApiParam(value = "the type of the document, example messages, test packages etc...", required = true) @RequestParam("type") DocumentType type,
			HttpServletRequest request, HttpServletResponse response) throws DownloadDocumentException {
		try {
			InputStream stream = zipResourceDocs(type);
			response.setContentType("application/zip");
			String name = type.name().toLowerCase();
			name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase() + "s";
			response.setHeader("Content-disposition", "attachment;filename=" + name + ".zip");
			streamer.stream(response.getOutputStream(), stream);
		} catch (IOException e) {
			logger.debug("Failed to download resource documentation of  " + type);
			throw new DownloadDocumentException("Failed to download resource documentation of  " + type);
		} catch (Exception e) {
			logger.debug("Failed to download the test packages ");
			throw new DownloadDocumentException("Failed to download resource documentation of  " + type);
		}
	}

	@RequestMapping(value = "/doc", method = RequestMethod.GET)
	public void downloadDocByName(
			@ApiParam(value = "the name of the document", required = true) @RequestParam("name") String name,
			HttpServletRequest request, HttpServletResponse response) throws DownloadDocumentException {
		if (name != null) {
			Document d = documentRepository.findOneByName(name);
			if (d != null) {
				downloadDocumentByPath(d.getPath(), request, response);
			} else {
				throw new DownloadDocumentException("Unknown document");
			}
		}
	}

	@RequestMapping(value = "/testPackages", method = RequestMethod.POST, produces = "application/zip")
	public void downloadTestPackages(HttpServletRequest request, HttpServletResponse response)
			throws DownloadDocumentException {
		try {
			InputStream stream = zipTestPackages(TestingStage.CB);
			response.setContentType("application/zip");
			response.setHeader("Content-disposition", "attachment;filename=ContextBasedTestPackages.zip");
			streamer.stream(response.getOutputStream(), stream);
		} catch (IOException e) {
			logger.debug("Failed to download the test packages ");
			throw new DownloadDocumentException("Cannot download the test packages");
		} catch (Exception e) {
			logger.debug("Failed to download the test packages ");
			throw new DownloadDocumentException("Cannot download the test packages");
		}
	}

	@RequestMapping(value = "/exampleMessages", method = RequestMethod.POST, produces = "application/zip")
	public void downloadAllExampleMessages(HttpServletRequest request, HttpServletResponse response)
			throws DownloadDocumentException {
		try {
			InputStream stream = zipExampleMessages(TestingStage.CB);
			response.setContentType("application/zip");
			response.setHeader("Content-disposition", "attachment;filename=" + "ContextBasedExampleMessages.zip");
			streamer.stream(response.getOutputStream(), stream);
		} catch (IOException e) {
			logger.debug("Failed to download the example messages ");
			throw new DownloadDocumentException("Cannot download the example messages");
		} catch (Exception e) {
			logger.debug("Failed to download the example messages ");
			throw new DownloadDocumentException("Cannot download the example messages");
		}
	}

	@RequestMapping(value = "/artifact", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded; charset=UTF-8")
	public void downloadArtifactByPath(
			@ApiParam(value = "the title of the document", required = false) @RequestParam("title") String title,
			@ApiParam(value = "the path of the document", required = true) @RequestParam("path") String path,
			HttpServletRequest request, HttpServletResponse response) throws DownloadDocumentException {
		try {
			InputStream content = getContent(path);
			String fileName = path.substring(path.lastIndexOf("/") + 1);
			if (content != null && fileName != null) {
				response.setContentType(getContentType(path));
				fileName = title + "-" + fileName;
				fileName = fileName.replaceAll(" ", "-");
				response.setHeader("Content-disposition", "attachment;filename=" + fileName);
				streamer.stream(response.getOutputStream(), content);
			} else {
				throw new DownloadDocumentException("Invalid Path Provided");
			}
		} catch (IOException e) {
			logger.debug("Failed to download the test package ");
			throw new DownloadDocumentException("Cannot download the artifact " + e.getMessage());
		}
	}

	public InputStream zipTestPackages(TestingStage stage) throws Exception {
		String pattern = null;
		String name = null;
		if (stage == TestingStage.CB) {
			pattern = "/*Contextbased/**/TestPackage.pdf";
			name = "ContextbasedTestPackages";
		} else if (stage == TestingStage.CF) {
			pattern = "/*Contextfree/**/TestPackage.pdf";
			name = "ContextfreeTestPackages";
		} else if (stage == TestingStage.ISOLATED) {
			pattern = "/*Isolated/**/TestPackage.pdf";
			name = "IsolatedTestPackages";
		}
		return generateZip(pattern, name);
	}

	public InputStream zipResourceDocs(DocumentType type) throws Exception {
		String pattern = null;
		String name = null;
		if (type == DocumentType.PROFILE) {
			pattern = "/*Global/Profiles/**/*.xml";
			name = "Profiles";
		} else if (type == DocumentType.TABLE) {
			pattern = "/*Global/Tables/**/*.xml";
			name = "Tables";
		} else if (type == DocumentType.CONSTRAINT) {
			pattern = "/*Global/Constraints/**/*.xml";
			name = "Constraints";
		}
		return generateZip(pattern, name);
	}

	public InputStream zipExampleMessages(TestingStage stage) throws Exception {
		String pattern = null;
		String name = null;
		if (stage == TestingStage.CB) {
			pattern = "/*Contextbased/**/Message.*";
			name = "ContextbasedExampleMessages";
		} else if (stage == TestingStage.CF) {
			pattern = "/*Contextfree/**/Message.*";
			name = "ContextfreeExampleMessages";
		} else if (stage == TestingStage.ISOLATED) {
			pattern = "/*Isolated/**/Message.*";
			name = "IsolatedExampleMessages";
		}
		return generateZip(pattern, name);
	}

	private InputStream generateZip(String pattern, String name) throws Exception {
		if (pattern != null && name != null) {
			String filename = zipGenerator.generate(pattern, name);
			FileInputStream io = new FileInputStream(new File(filename));
			return io;
		}
		return null;
	}

	private InputStream getContent(String path) {
		InputStream content = null;
		if (!path.startsWith("/")) {
			content = DocumentationController.class.getResourceAsStream("/" + path);
		} else {
			content = DocumentationController.class.getResourceAsStream(path);
		}
		return content;
	}

	private String getContentType(String fileName) {
		String contentType = "application/octet-stream";
		String fileExtension = getExtension(fileName);
		if (fileExtension != null) {
			fileExtension = fileExtension.toLowerCase();
		}
		if (fileExtension.equals("pdf")) {
			contentType = "application/pdf";
		} else if (fileExtension.equals("doc")) {
			contentType = "application/msword";
		} else if (fileExtension.equals("docx")) {
			contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		} else if (fileExtension.equals("xls")) {
			contentType = "application/vnd.ms-excel";
		} else if (fileExtension.equals("xlsx")) {
			contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		} else if (fileExtension.equals("jpeg")) {
			contentType = "image/jpeg";
		} else if (fileExtension.equals("xml")) {
			contentType = "text/xml";
		} else if (fileExtension.equals("war") || fileExtension.equals("zip")) {
			contentType = "application/x-zip";
		} else if (fileExtension.equals("pptx")) {
			contentType = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
		} else if (fileExtension.equals("ppt")) {
			contentType = "application/vnd.ms-powerpoint";
		}
		return contentType;
	}

	private String getExtension(String fileName) {
		String ext = "";
		int i = fileName.lastIndexOf('.');
		if (i != -1) {
			ext = fileName.substring(i + 1);
		}
		return ext;
	}

}
