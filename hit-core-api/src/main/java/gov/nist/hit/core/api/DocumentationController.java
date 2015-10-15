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

import gov.nist.hit.core.domain.Document;
import gov.nist.hit.core.domain.Message;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseDocumentation;
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.domain.TestObject;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.repo.DocumentRepository;
import gov.nist.hit.core.repo.TestCaseDocumentationRepository;
import gov.nist.hit.core.repo.TestContextRepository;
import gov.nist.hit.core.repo.TestObjectRepository;
import gov.nist.hit.core.repo.TestPlanRepository;
import gov.nist.hit.core.repo.TestStepRepository;
import gov.nist.hit.core.service.TestCaseDocumentationService;
import gov.nist.hit.core.service.TestPlanService;
import gov.nist.hit.core.service.ZipGenerator;
import gov.nist.hit.core.service.exception.DownloadDocumentException;
import gov.nist.hit.core.service.exception.MessageException;
import gov.nist.hit.core.service.exception.TestCaseException;
import gov.nist.hit.core.service.exception.ValidationReportException;
import gov.nist.hit.core.service.util.ResourcebundleHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


  @Cacheable(value = "documentationCache", key = "#stage.name() + 'testcases-documentation'")
  @RequestMapping(value = "/testcases", method = RequestMethod.GET)
  public TestCaseDocumentation testCases(@RequestParam("stage") TestingStage stage) {
    logger.info("Fetching " + stage + " test case documentation");
    TestCaseDocumentation doc = testCaseDocumentationService.findOneByStage(stage);
    return doc;
  }

  @Cacheable(value = "documentationCache", key = "'releasenotes'")
  @RequestMapping(value = "/releasenotes", method = RequestMethod.GET)
  public List<Document> releaseNotes() {
    logger.info("Fetching  all release notes");
    return documentRepository.findAllReleaseNotes();
  }

  @Cacheable(value = "documentationCache", key = "'userdocs'")
  @RequestMapping(value = "/userdocs", method = RequestMethod.GET)
  public List<Document> userDocs() {
    logger.info("Fetching  all release notes");
    return documentRepository.findAllUserDocs();
  }

  @Cacheable(value = "documentationCache", key = "'knownissues'")
  @RequestMapping(value = "/knownissues", method = RequestMethod.GET)
  public List<Document> knownIssues() {
    logger.info("Fetching  all known issues");
    return documentRepository.findAllKnownIssues();
  }



  @RequestMapping(value = "/downloadDocument", method = RequestMethod.POST)
  public void downloadDocument(@RequestParam("path") String path, HttpServletRequest request,
      HttpServletResponse response) throws DownloadDocumentException {
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
          FileCopyUtils.copy(content, response.getOutputStream());
        }
      }
    } catch (IOException e) {
      logger.debug("Failed to download the test packages ");
      throw new DownloadDocumentException("Cannot download the test packages");
    }
  }



  @RequestMapping(value = "/testPackages", method = RequestMethod.POST)
  public void testPackages(@RequestParam("stage") TestingStage stage, HttpServletRequest request,
      HttpServletResponse response) throws DownloadDocumentException {
    try {
      InputStream stream = zipTestPackages(stage);
      response.setContentType("application/zip");
      response
          .setHeader("Content-disposition", "attachment;filename=" + stage + "TestPackages.zip");
      FileCopyUtils.copy(stream, response.getOutputStream());

    } catch (IOException e) {
      logger.debug("Failed to download the test packages ");
      throw new DownloadDocumentException("Cannot download the test packages");
    } catch (Exception e) {
      logger.debug("Failed to download the test packages ");
      throw new DownloadDocumentException("Cannot download the test packages");
    }
  }

  @RequestMapping(value = "/exampleMessages", method = RequestMethod.POST)
  public void exampleMessages(@RequestParam("stage") TestingStage stage, HttpServletRequest request,
      HttpServletResponse response) throws DownloadDocumentException {
    try {
      InputStream stream = zipExampleMessages(stage);
      response.setContentType("application/zip");
      response.setHeader("Content-disposition", "attachment;filename=" + stage
          + "ExampleMessages.zip");
      FileCopyUtils.copy(stream, response.getOutputStream());
    } catch (IOException e) {
      logger.debug("Failed to download the example messages ");
      throw new DownloadDocumentException("Cannot download the example messages");
    } catch (Exception e) {
      logger.debug("Failed to download the example messages ");
      throw new DownloadDocumentException("Cannot download the example messages");
    }
  }

  @RequestMapping(value = "/artifact", method = RequestMethod.POST,
      consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public void download(@RequestParam("title") String title, @RequestParam("path") String path,
      HttpServletRequest request, HttpServletResponse response) throws DownloadDocumentException {
    try {
      InputStream content = getContent(path);
      String fileName = path.substring(path.lastIndexOf("/") + 1);
      if (content != null && fileName != null) {
        response.setContentType(getContentType(path));
        fileName = title + "-" + fileName;
        fileName = fileName.replaceAll(" ", "-");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        FileCopyUtils.copy(content, response.getOutputStream());
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


  public InputStream zipExampleMessages(TestingStage stage) throws Exception {
    String pattern = null;
    String name = null;
    if (stage == TestingStage.CB) {
      pattern = "/*Contextbased/**/Message.t*";
      name = "ContextbasedExampleMessages";
    } else if (stage == TestingStage.CF) {
      pattern = "/*Contextfree/**/Message.t*";
      name = "ContextfreeExampleMessages";
    } else if (stage == TestingStage.ISOLATED) {
      pattern = "/*Isolated/**/Message.t*";
      name = "IsolatedExampleMessages";
    }
    return generateZip(pattern, name);
  } 
  
  
  private InputStream generateZip(String pattern, String name) throws Exception{
    if(pattern != null && name != null){ 
      String filename =  zipGenerator.generate(pattern, name);
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
