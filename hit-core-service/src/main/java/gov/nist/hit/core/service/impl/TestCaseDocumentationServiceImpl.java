package gov.nist.hit.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

import gov.nist.hit.core.domain.AbstractTestCase;
import gov.nist.hit.core.domain.CFTestPlan;
import gov.nist.hit.core.domain.CFTestStep;
import gov.nist.hit.core.domain.CFTestStepGroup;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseDocument;
import gov.nist.hit.core.domain.TestCaseDocumentation;
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.repo.CFTestPlanRepository;
import gov.nist.hit.core.repo.TestPlanRepository;
import gov.nist.hit.core.service.ResourcebundleLoader;
import gov.nist.hit.core.service.TestCaseDocumentationService;
import gov.nist.hit.core.service.ZipGenerator;
import gov.nist.hit.core.service.util.DocumentationUtils;

@Service
public class TestCaseDocumentationServiceImpl implements TestCaseDocumentationService {


  @Autowired
  private ResourcebundleLoader resourcebundleLoader;

  @Autowired
  protected TestPlanRepository testPlanRepository;


  @Autowired
  protected CFTestPlanRepository cfTestPlanRepository;



  @Autowired
  private ZipGenerator zipGenerator;


  protected com.fasterxml.jackson.databind.ObjectMapper obm;

  public TestCaseDocumentationServiceImpl() {
    obm = new com.fasterxml.jackson.databind.ObjectMapper();
    obm.setSerializationInclusion(Include.NON_NULL);
  }



  /**
   * 
   * @param scope
   * @param preloaded
   * @param domain
   * @throws IOException
   */
  @Override
  public List<TestCaseDocumentation> generate(TestScope scope, String domain) throws IOException {
    List<TestCaseDocumentation> documents = new ArrayList<TestCaseDocumentation>();
    List<CFTestPlan> cfTestPlans =
        cfTestPlanRepository.findAllByStageAndScopeAndDomain(TestingStage.CF, scope, domain);
    documents.addAll(generateCfDocumentations(cfTestPlans));
    List<TestPlan> cbTestPlans =
        testPlanRepository.findAllByStageAndScopeAndDomain(TestingStage.CB, scope, domain);
    documents.addAll(generateCbDocumentations(cbTestPlans));
    return documents;
  }

  @Override
  public List<TestCaseDocumentation> generate(TestScope scope, String domain, String username)
      throws IOException {
    List<TestCaseDocumentation> documents = new ArrayList<TestCaseDocumentation>();
    List<CFTestPlan> cfTestPlans = cfTestPlanRepository
        .findAllByStageAndAuthorAndScopeAndDomain(TestingStage.CF, username, scope, domain);
    documents.addAll(generateCfDocumentations(cfTestPlans));
    List<TestPlan> cbTestPlans = testPlanRepository
        .findAllByStageAndScopeAndDomainAndAuthor(TestingStage.CB, scope, domain, username);
    documents.addAll(generateCbDocumentations(cbTestPlans));
    return documents;
  }


  public List<TestCaseDocumentation> generateCbDocumentations(List<TestPlan> testPlans)
      throws IOException {
    List<TestCaseDocumentation> documents = new ArrayList<TestCaseDocumentation>();
    TestCaseDocumentation doc = cb("Context-based", TestingStage.CB, testPlans);
    if (doc != null) {
      doc.setJson(obm.writeValueAsString(doc));
      documents.add(doc);
    }
    return documents;
  }

  public List<TestCaseDocumentation> generateCfDocumentations(List<CFTestPlan> testPlans)
      throws IOException {
    List<TestCaseDocumentation> documents = new ArrayList<TestCaseDocumentation>();
    TestCaseDocumentation doc = cf("Context-free", TestingStage.CF, testPlans);
    if (doc != null) {
      doc.setJson(obm.writeValueAsString(doc));
      documents.add(doc);
    }
    return documents;
  }



  private TestCaseDocumentation cb(String title, TestingStage stage, List<TestPlan> tps)
      throws IOException {
    if (tps != null && !tps.isEmpty()) {
      TestCaseDocumentation documentation = new TestCaseDocumentation();
      documentation.setTitle(title);
      documentation.setStage(stage);
      for (TestPlan testPlan : tps) {
        documentation.getChildren().add(generate(testPlan));
      }
      return documentation;
    }
    return null;
  }

  private TestCaseDocumentation cf(String title, TestingStage stage, List<CFTestPlan> tos)
      throws IOException {
    if (tos != null && !tos.isEmpty()) {
      TestCaseDocumentation documentation = new TestCaseDocumentation();
      documentation.setTitle(title);
      documentation.setStage(stage);
      Collections.sort(tos);
      for (CFTestPlan to : tos) {
        documentation.getChildren().add(generate(to));
      }
      return documentation;
    }
    return null;
  }

  private gov.nist.hit.core.domain.TestCaseDocument generate(TestPlan tp) throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = initTestCaseDocument(tp);
    doc.setId(tp.getId());
    if (tp.getTestCaseGroups() != null && !tp.getTestCaseGroups().isEmpty()) {
      List<TestCaseGroup> list = new ArrayList<TestCaseGroup>(tp.getTestCaseGroups());
      Collections.sort(list);
      for (TestCaseGroup tcg : list) {
        doc.getChildren().add(generate(tcg));
      }
    }
    if (tp.getTestCases() != null && !tp.getTestCases().isEmpty()) {
      List<TestCase> list = new ArrayList<TestCase>(tp.getTestCases());
      Collections.sort(list);
      for (TestCase tc : list) {
        doc.getChildren().add(generate(tc));
      }
    }
    return doc;
  }

  private gov.nist.hit.core.domain.TestCaseDocument generate(TestCaseGroup tcg) throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = initTestCaseDocument(tcg);
    doc.setId(tcg.getId());
    if (tcg.getTestCaseGroups() != null && !tcg.getTestCaseGroups().isEmpty()) {
      List<TestCaseGroup> list = new ArrayList<TestCaseGroup>(tcg.getTestCaseGroups());
      Collections.sort(list);
      for (TestCaseGroup child : list) {
        doc.getChildren().add(generate(child));
      }
    }

    if (tcg.getTestCases() != null && !tcg.getTestCases().isEmpty()) {
      List<TestCase> list = new ArrayList<TestCase>(tcg.getTestCases());
      Collections.sort(list);
      for (TestCase tc : list) {
        doc.getChildren().add(generate(tc));
      }
    }

    return doc;
  }


  private gov.nist.hit.core.domain.TestCaseDocument generate(CFTestStepGroup tcg)
      throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = initTestCaseDocument(tcg);
    doc.setId(tcg.getId());
    if (tcg.getTestStepGroups() != null && !tcg.getTestStepGroups().isEmpty()) {
      List<CFTestStepGroup> list = new ArrayList<CFTestStepGroup>(tcg.getTestStepGroups());
      Collections.sort(list);
      for (CFTestStepGroup child : list) {
        doc.getChildren().add(generate(child));
      }
    }
    if (tcg.getTestSteps() != null && !tcg.getTestSteps().isEmpty()) {
      List<CFTestStep> list = new ArrayList<CFTestStep>(tcg.getTestSteps());
      Collections.sort(list);
      for (CFTestStep tc : list) {
        doc.getChildren().add(generate(tc));
      }
    }
    return doc;
  }


  private gov.nist.hit.core.domain.TestCaseDocument generate(TestCase tc) throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = initTestCaseDocument(tc);
    doc.setId(tc.getId());
    if (tc.getTestSteps() != null && !tc.getTestSteps().isEmpty()) {
      List<TestStep> list = new ArrayList<TestStep>(tc.getTestSteps());
      Collections.sort(list);
      for (TestStep ts : list) {
        doc.getChildren().add(generate(ts));
      }
    }
    return doc;
  }

  private gov.nist.hit.core.domain.TestCaseDocument generate(TestStep ts) throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc =
        resourcebundleLoader.generateTestCaseDocument(ts.getTestContext());
    doc = initTestCaseDocument(ts, doc);
    if (ts.getTestContext() != null) {
      doc.setId(ts.getTestContext().getId());
    }
    return doc;
  }

  private gov.nist.hit.core.domain.TestCaseDocument generate(CFTestStep ts) throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc =
        resourcebundleLoader.generateTestCaseDocument(ts.getTestContext());
    doc = initTestCaseDocument(ts, doc);
    if (ts.getTestContext() != null) {
      doc.setId(ts.getTestContext().getId());
    }
    return doc;
  }

  private gov.nist.hit.core.domain.TestCaseDocument generate(CFTestPlan tp) throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = initTestCaseDocument(tp);
    doc.setId(tp.getId());

    if (tp.getTestStepGroups() != null && !tp.getTestStepGroups().isEmpty()) {
      List<CFTestStepGroup> list = new ArrayList<CFTestStepGroup>(tp.getTestStepGroups());
      Collections.sort(list);
      for (CFTestStepGroup tcg : list) {
        doc.getChildren().add(generate(tcg));
      }
    }
    if (tp.getTestSteps() != null && !tp.getTestSteps().isEmpty()) {
      List<CFTestStep> list = new ArrayList<CFTestStep>(tp.getTestSteps());
      Collections.sort(list);
      for (CFTestStep tc : list) {
        doc.getChildren().add(generate(tc));
      }
    }

    return doc;
  }



  private gov.nist.hit.core.domain.TestCaseDocument initTestCaseDocument(AbstractTestCase ts)
      throws IOException {
    return initTestCaseDocument(ts, new TestCaseDocument());
  }

  private gov.nist.hit.core.domain.TestCaseDocument initTestCaseDocument(AbstractTestCase ts,
      TestCaseDocument doc) throws IOException {
    doc.setTitle(ts.getName());
    doc.setType(ts.getType().toString());
    doc.setTsPath(ts.getTestStory() != null ? ts.getTestStory().getPdfPath() : null);
    if (ts instanceof TestPlan) {
      TestPlan tp = (TestPlan) ts;
      doc.setTpPath(tp.getTestPackage() != null ? tp.getTestPackage().getPdfPath() : null);
      doc.setTpsPath(tp.getTestPlanSummary() != null ? tp.getTestPlanSummary().getPdfPath() : null);
    } else if (ts instanceof TestStep) {
      TestStep tStep = (TestStep) ts;
      doc.setMcPath(
          tStep.getMessageContent() != null ? tStep.getMessageContent().getPdfPath() : null);
      doc.setTdsPath(tStep.getTestDataSpecification() != null
          ? tStep.getTestDataSpecification().getPdfPath() : null);
      doc.setJdPath(
          tStep.getJurorDocument() != null ? tStep.getJurorDocument().getPdfPath() : null);
    }
    return doc;
  }



  @Override
  public InputStream zipContextfreeMessages(TestScope scope, String domain) throws Exception {
    List<CFTestPlan> testPlans =
        cfTestPlanRepository.findAllByStageAndScopeAndDomain(TestingStage.CF, scope, domain);
    String name = "ContextFreeExampleMessages";
    Path path = Files.createTempDirectory(null);
    File rootFolder = path.toFile();
    if (!rootFolder.exists()) {
      rootFolder.mkdir();
    }
    String folderToZip = rootFolder.getAbsolutePath() + File.separator + "ToZip";
    for (CFTestPlan testPlan : testPlans) {
      DocumentationUtils.createMessageFile(testPlan, folderToZip);
    }
    String zipFilename = rootFolder + File.separator + name + ".zip";
    zipGenerator.zip(zipFilename, folderToZip);
    FileInputStream io = new FileInputStream(new File(zipFilename));
    return io;
  }

  @Override
  public InputStream zipContextbasedTestPackages(TestScope scope, String domain) throws Exception {
    String name = "ContextbasedTestPackages";
    List<TestPlan> testPlans =
        testPlanRepository.findAllByStageAndScopeAndDomain(TestingStage.CB, scope, domain);
    Path path = Files.createTempDirectory(null);
    File rootFolder = path.toFile();
    if (!rootFolder.exists()) {
      rootFolder.mkdir();
    }
    String folderToZip = rootFolder.getAbsolutePath() + File.separator + "ToZip";
    for (TestPlan testPlan : testPlans) {
      DocumentationUtils.createTestPackageFile(testPlan, folderToZip);
    }
    String zipFilename = rootFolder + File.separator + name + ".zip";
    zipGenerator.zip(zipFilename, folderToZip);
    FileInputStream io = new FileInputStream(new File(zipFilename));
    return io;
  }



  @Override
  public InputStream zipContextbasedMessages(TestScope scope, String domain) throws Exception {
    String name = "ContextbasedExampleMessages";
    List<TestPlan> testPlans =
        testPlanRepository.findAllByStageAndScopeAndDomain(TestingStage.CB, scope, domain);
    Path path = Files.createTempDirectory(null);
    File rootFolder = path.toFile();
    if (!rootFolder.exists()) {
      rootFolder.mkdir();
    }
    String folderToZip = rootFolder.getAbsolutePath() + File.separator + "ToZip";
    for (TestPlan testPlan : testPlans) {
      DocumentationUtils.createMessageFile(testPlan, folderToZip);
    }
    String zipFilename = rootFolder + File.separator + name + ".zip";
    zipGenerator.zip(zipFilename, folderToZip);
    FileInputStream io = new FileInputStream(new File(zipFilename));
    return io;
  }



}
