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

package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.AbstractTestCase;
import gov.nist.hit.core.domain.ConnectionType;
import gov.nist.hit.core.domain.Constraints;
import gov.nist.hit.core.domain.IntegrationProfile;
import gov.nist.hit.core.domain.Message;
import gov.nist.hit.core.domain.ProfileModel;
import gov.nist.hit.core.domain.Stage;
import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseDocument;
import gov.nist.hit.core.domain.TestCaseDocumentation;
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.domain.TestCategory;
import gov.nist.hit.core.domain.TestContext;
import gov.nist.hit.core.domain.TestObject;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.VocabularyLibrary;
import gov.nist.hit.core.repo.ConstraintsRepository;
import gov.nist.hit.core.repo.IntegrationProfileRepository;
import gov.nist.hit.core.repo.MessageRepository;
import gov.nist.hit.core.repo.TestCaseDocumentationRepository;
import gov.nist.hit.core.repo.TestObjectRepository;
import gov.nist.hit.core.repo.TestPlanRepository;
import gov.nist.hit.core.repo.TestStepRepository;
import gov.nist.hit.core.repo.VocabularyLibraryRepository;
import gov.nist.hit.core.service.exception.ProfileParserException;
import gov.nist.hit.core.service.util.FileUtil;
import gov.nist.hit.core.service.util.ResourcebundleHelper;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

public abstract class ResourcebundleLoader {

  static final public Logger logger = LoggerFactory.getLogger(ResourcebundleLoader.class);
  final static public String PROFILE_PATTERN = "Global/Profiles/";
  final static public String VALUESET_PATTERN = "Global/Tables/";
  final static public String CONSTRAINT_PATTERN = "Global/Constraints/";
  final static public String CONSTRAINTS_FILE_PATTERN = "Constraints.xml";
  final static public String CONTEXTBASED_PATTERN = "Contextbased/";
  final static public String CONTEXTFREE_PATTERN = "Contextfree/";
  final static public String DOCUMENTATION_PATTERN = "Documentation/";
  final static public String TEST_PLAN_FILE_PATTERN = "TestPlan.json";
  final static public String TEST_CASE_FILE_PATTERN = "TestCase.json";
  final static public String TEST_CASEGROUP_FILE_PATTERN = "TestCaseGroup.json";
  final static public String TEST_STEP_FILE_PATTERN = "TestStep.json";
  final static public String TEST_OBJECT_FILE_PATTERN = "TestObject.json";
  final static public String TEST_PROCEDURE_FILE_PATTERN = "TestProcedure.json";
  final static public String TEST_PACKAGE_FILE_PATTERN = "TestPackage.json";
  final static public String TEST_STORY_FILE_PATTERN = "TestStory.json";
  final static public String MESSAGE_PATTERN = "Message.txt";
  final static public String ABOUT_PATTERN = "About/";
  final static String ISOLATED_PATTERN = "Isolated/";

  Map<String, IntegrationProfile> cachedProfiles = new HashMap<String, IntegrationProfile>();
  Map<String, VocabularyLibrary> cachedVocabLibraries = new HashMap<String, VocabularyLibrary>();
  Map<String, Constraints> cachedConstraints = new HashMap<String, Constraints>();

  @Autowired
  IntegrationProfileRepository integrationProfileRepository;

  @Autowired
  VocabularyLibraryRepository vocabularyLibraryRepository;

  @Autowired
  MessageRepository messageRepository;

  @Autowired
  ConstraintsRepository constraintsRepository;

  @Autowired
  ValueSetLibrarySerializer valueSetLibrarySerializer;


  @PostConstruct
  public void load() throws JsonProcessingException, IOException, ProfileParserException {
    logger.info("loading resource bundle...");
    this.loadConstraints();
    this.loadVocabularyLibraries();
    this.loadIntegrationProfiles();
    this.loadCfTestCases();
    this.loadCbTestCases();
    this.loadIsolatedTestCases();
    this.loadTestCasesDocumentation();
    cachedProfiles.clear();
    cachedVocabLibraries.clear();
    cachedConstraints.clear();
    logger.info("loading resource bundle...DONE");
  }


  protected void loadConstraints() throws IOException {
    logger.info("loading constraints...");
    List<Resource> resources = getResources(domainPath(CONSTRAINT_PATTERN) + "*");
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String content = FileUtil.getContent(resource);
        Constraints constraints = constraint(content);
        cachedConstraints.put(constraints.getSourceId(), constraints);
      }
    }
    logger.info("loading constraints...DONE");
  }

  protected void loadIntegrationProfiles() throws IOException {
    logger.info("loading integration profiles...");
    List<Resource> resources = getResources(domainPath(PROFILE_PATTERN) + "*");
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        IntegrationProfile integrationProfile = integrationProfile(FileUtil.getContent(resource));
        integrationProfileRepository.save(integrationProfile);
      }
    }
    logger.info("loading integration profiles...DONE");
  }

  protected void loadVocabularyLibraries() throws IOException {
    logger.info("loading value set libraries...");
    List<Resource> resources = getResources(domainPath(VALUESET_PATTERN) + "*");
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String content = FileUtil.getContent(resource);
        VocabularyLibrary vocabLibrary = vocabLibrary(content);
        cachedVocabLibraries.put(vocabLibrary.getSourceId(), vocabLibrary);
      }
    }
    logger.info("loading value set libraries...DONE");
  }

  protected VocabularyLibrary vocabLibrary(String content) throws JsonGenerationException,
      JsonMappingException, IOException {
    Document doc = this.stringToDom(content);
    VocabularyLibrary vocabLibrary = new VocabularyLibrary();
    Element valueSetLibraryeElement = (Element) doc.getElementsByTagName("ValueSetLibrary").item(0);
    vocabLibrary.setSourceId(valueSetLibraryeElement.getAttribute("ValueSetLibraryIdentifier"));
    vocabLibrary.setName(valueSetLibraryeElement.getAttribute("Name"));
    vocabLibrary.setDescription(valueSetLibraryeElement.getAttribute("Description"));
    vocabLibrary.setXml(content);
    vocabLibrary.setJson(obm.writeValueAsString(valueSetLibrarySerializer.toObject(content)));
    vocabularyLibraryRepository.save(vocabLibrary);
    return vocabLibrary;
  }


  protected Constraints additionalConstraints(String location) throws IOException {
    Resource resource = getResource(location);
    if (resource == null) {
      return null;
    }
    Constraints constraints = new Constraints();
    constraints.setXml(FileUtil.getContent(resource));
    return constraints;
  }

  protected IntegrationProfile integrationProfile(String content) {
    Document doc = this.stringToDom(content);
    IntegrationProfile integrationProfile = new IntegrationProfile();
    Element profileElement = (Element) doc.getElementsByTagName("ConformanceProfile").item(0);
    integrationProfile.setSourceId(profileElement.getAttribute("ID"));
    Element metaDataElement = (Element) profileElement.getElementsByTagName("MetaData").item(0);
    integrationProfile.setName(metaDataElement.getAttribute("Name"));
    integrationProfile.setXml(content);
    Element conformanceProfilElementRoot =
        (Element) profileElement.getElementsByTagName("Messages").item(0);
    NodeList messages = conformanceProfilElementRoot.getElementsByTagName("Message");
    for (int j = 0; j < messages.getLength(); j++) {
      Element elmCode = (Element) messages.item(j);
      String id = elmCode.getAttribute("ID");
      if (cachedProfiles.containsKey(id)) {
        throw new RuntimeException("Found duplicate conformance profile id " + id);
      }
      cachedProfiles.put(id, integrationProfile);
    }
    return integrationProfile;
  }

  protected Message message(String content) {
    if (content != null) {
      Message m = new Message();
      m.setContent(content);
      return m;
    }
    return null;
  }

  protected Constraints constraint(String content) {
    Document doc = this.stringToDom(content);
    Constraints constraints = new Constraints();
    Element constraintsElement = (Element) doc.getElementsByTagName("ConformanceContext").item(0);
    constraints.setSourceId(constraintsElement.getAttribute("UUID"));
    Element metaDataElement = (Element) constraintsElement.getElementsByTagName("MetaData").item(0);
    constraints.setDescription(metaDataElement.getAttribute("Description"));
    constraints.setXml(content);
    constraintsRepository.save(constraints);
    return constraints;
  }

  protected Document stringToDom(String xmlSource) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    factory.setIgnoringComments(false);
    factory.setIgnoringElementContentWhitespace(true);
    DocumentBuilder builder;
    try {
      builder = factory.newDocumentBuilder();
      return builder.parse(new InputSource(new StringReader(xmlSource)));
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }



  public String jsonConformanceProfile(String integrationProfileXml, String conformanceProfileId,
      String constraintsXml, String additionalConstraintsXml) throws ProfileParserException,
      JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
    ProfileModel profileModel =
        parseProfile(integrationProfileXml, conformanceProfileId, constraintsXml,
            additionalConstraintsXml);
    String json = obm.writeValueAsString(profileModel);
    return json;
  }


  protected IntegrationProfile getIntegrationProfile(String id) throws IOException {
    IntegrationProfile p = cachedProfiles.get(id);
    if (p == null) {
      throw new IllegalArgumentException(
          "Cannot find IntegrationProfile associated to ConformanceProfile with id = " + id);
    }

    return p;
  }

  protected Constraints getConstraints(String id) throws IOException {
    Constraints c = cachedConstraints.get(id);
    if (c == null) {
      throw new IllegalArgumentException("Constraints with id = " + id + " not found");
    }
    return c;
  }

  protected VocabularyLibrary getVocabularyLibrary(String id) throws IOException {
    VocabularyLibrary v = cachedVocabLibraries.get(id);
    if (v == null) {
      throw new IllegalArgumentException("VocabularyLibrary with id = " + id + " not found");
    }
    return v;
  }



  @Autowired
  protected TestPlanRepository testPlanRepository;

  @Autowired
  protected TestObjectRepository testObjectRepository;

  @Autowired
  protected TestStepRepository testStepRepository;

  @Autowired
  protected TestCaseDocumentationRepository testCaseDocumentationRepository;


  protected com.fasterxml.jackson.databind.ObjectMapper obm;

  public ResourcebundleLoader() {
    obm = new com.fasterxml.jackson.databind.ObjectMapper();
    obm.setSerializationInclusion(Include.NON_NULL);
  }

  /**
   * 
   */
  public abstract TestContext testContext(String location, JsonNode parentObj) throws IOException;

  /**
   * 
   */
  public abstract TestCaseDocument setTestContextDocument(TestContext c, TestCaseDocument doc)
      throws IOException;


  public abstract ProfileModel parseProfile(String integrationProfileXml,
      String conformanceProfileId, String constraintsXml, String additionalConstraintsXml)
      throws ProfileParserException;


  public String domainPath(String path) {
    return path;
  }


  protected void loadIsolatedTestCases() throws IOException {
    List<Resource> resources = getDirectories(domainPath(ISOLATED_PATTERN) + "*/");
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        String location =
            fileName.substring(fileName.indexOf(domainPath(ISOLATED_PATTERN)), fileName.length());
        TestPlan testPlan = testPlan(location, Stage.ISOLATED);
        testPlanRepository.save(testPlan);
      }
    }
  }

  protected void loadCbTestCases() throws IOException {
    List<Resource> resources = getDirectories(domainPath(CONTEXTBASED_PATTERN) + "*/");
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        String location =
            fileName.substring(fileName.indexOf(domainPath(CONTEXTBASED_PATTERN)),
                fileName.length());
        TestPlan testPlan = testPlan(location, Stage.CB);
        testPlanRepository.save(testPlan);
      }
    }
  }

  protected void loadCfTestCases() throws IOException, ProfileParserException {
    List<Resource> resources = getDirectories(domainPath(CONTEXTFREE_PATTERN) + "*/");
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        TestObject testObject =
            testObject(fileName.substring(fileName.indexOf(domainPath(CONTEXTFREE_PATTERN)),
                fileName.length()));
        testObject.setRoot(true);
        testObjectRepository.save(testObject);
      }
    }
  }



  public TestCase testCase(String location, Stage stage) throws IOException {
    logger.info("Processing test case at:" + location);
    TestCase tc = new TestCase();
    Resource res = ResourcebundleHelper.getResource(location + "TestCase.json");
    if (res == null)
      throw new IllegalArgumentException("No TestCase.json found at " + location);
    String descriptorContent = FileUtil.getContent(res);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testCaseObj = mapper.readTree(descriptorContent);
    tc.setName(testCaseObj.findValue("name").getTextValue());
    tc.setDescription(testCaseObj.findValue("description").getTextValue());
    tc.setTestStory(testStory(location));
    tc.setTestPackage(testPackage(location));
    tc.setJurorDocument(jurorDocument(location));
    tc.setMessageContent(messageContent(location));
    tc.setTestDataSpecification(testDataSpecification(location));
    if (testCaseObj.findValue("position") != null) {
      tc.setPosition(testCaseObj.findValue("position").getIntValue());
    }
    if (testCaseObj.findValue("type") != null) {
      tc.setCategory(TestCategory.valueOf(testCaseObj.findValue("type").getTextValue()));
    }
    List<Resource> resources = getDirectories(location + "*");
    for (Resource resource : resources) {
      String fileName = fileName(resource);
      String tcLocation = fileName.substring(fileName.indexOf(location), fileName.length());
      TestStep testStep = testStep(tcLocation, stage);
      testStep.setParentName(tc.getName());
      tc.getTestSteps().add(testStep);
    }
    return tc;
  }

  public TestStep testStep(String location, Stage stage) throws IOException {
    logger.info("Processing test step at:" + location);
    TestStep testStep = new TestStep();
    Resource res = ResourcebundleHelper.getResource(location + "TestStep.json");
    if (res == null)
      throw new IllegalArgumentException("No TestStep.json found at " + location);
    String descriptorContent = FileUtil.getContent(res);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testStepObj = mapper.readTree(descriptorContent);
    testStep.setName(testStepObj.findValue("name").getTextValue());
    testStep.setDescription(testStepObj.findValue("description").getTextValue());
    ConnectionType connType = null;
    if (testStepObj.findValue("type") != null) {
      String type = testStepObj.findValue("type").getTextValue();
      connType = type != null ? ConnectionType.valueOf(type) : null;
      testStep.setConnectionType(connType);
    }
    if (connType == null
        || (!connType.equals(ConnectionType.SUT_MANUAL) && !connType
            .equals(ConnectionType.TA_MANUAL))) {
      testStep.setTestContext(testContext(location, testStepObj));
    }
    testStep.setTestStory(testStory(location));
    testStep.setTestPackage(testPackage(location));
    testStep.setJurorDocument(jurorDocument(location));
    testStep.setMessageContent(messageContent(location));
    testStep.setTestDataSpecification(testDataSpecification(location));
    if (testStepObj.findValue("position") != null) {
      testStep.setPosition(testStepObj.findValue("position").getIntValue());
    }
    return testStep;
  }


  public TestArtifact testStory(String location) throws IOException {
    return artifact(location, "TestStory");
  }

  public TestArtifact testProcedure(String location) throws IOException {
    return artifact(location, "TestProcedure");
  }

  public TestArtifact jurorDocument(String location) throws IOException {
    return artifact(location, "JurorDocument");
  }

  public TestArtifact artifact(String location, String type) throws IOException {
    TestArtifact doc = null;
    String path = location + type + ".pdf";
    Resource resource = ResourcebundleHelper.getResource(path);
    if (resource != null) {
      doc = new TestArtifact(type);
      doc.setPdfPath(path);
    }
    path = location + type + ".html";
    resource = ResourcebundleHelper.getResource(path);
    if (resource != null) {
      doc = doc == null ? new TestArtifact(type) : doc;
      doc.setHtml(FileUtil.getContent(resource));
    }

    if (type.equals("TestStory")) { // TODO: Temporary hack
      path = location + type + ".json";
      resource = ResourcebundleHelper.getResource(path);
      if (resource != null) {
        doc = doc == null ? new TestArtifact(type) : doc;
        doc.setJson(FileUtil.getContent(resource));
      }
    }

    return doc;
  }


  public TestArtifact messageContent(String location) throws IOException {
    return artifact(location, "MessageContent");
  }

  public TestArtifact testDataSpecification(String location) throws IOException {
    return artifact(location, "TestDataSpecification");
  }



  public TestArtifact testPackage(String location) throws IOException {
    return artifact(location, "TestPackage");
  }


  protected TestCaseGroup testCaseGroup(String location, Stage stage) throws IOException {
    logger.info("Processing test case group at:" + location);
    TestCaseGroup tcg = new TestCaseGroup();
    Resource descriptorRsrce = ResourcebundleHelper.getResource(location + "TestCaseGroup.json");
    if (descriptorRsrce == null)
      throw new IllegalArgumentException("No TestCaseGroup.json found at " + location);
    String descriptorContent = FileUtil.getContent(descriptorRsrce);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testPlanObj = mapper.readTree(descriptorContent);
    tcg.setName(testPlanObj.findValue("name").getTextValue());
    tcg.setDescription(testPlanObj.findValue("description").getTextValue());
    if (testPlanObj.findValue("position") != null) {
      tcg.setPosition(testPlanObj.findValue("position").getIntValue());
    }
    List<Resource> resources = getDirectories(location + "*/");
    for (Resource resource : resources) {
      String fileName = fileName(resource);
      String tcLocation = fileName.substring(fileName.indexOf(location), fileName.length());
      Resource descriptorResource = getDescriptorResource(tcLocation);
      String filename = descriptorResource.getFilename();
      if (filename.endsWith("TestCaseGroup.json")) {
        TestCaseGroup testCaseGroup = testCaseGroup(tcLocation, stage);
        tcg.getTestCaseGroups().add(testCaseGroup);
      } else if (filename.endsWith("TestCase.json")) {
        TestCase testCase = testCase(tcLocation, stage);
        testCase.setParentName(tcg.getName());
        tcg.getTestCases().add(testCase);
      }
    }
    return tcg;
  }


  protected TestPlan testPlan(String testPlanPath, Stage stage) throws IOException {
    logger.info("Processing test plan  at:" + testPlanPath);
    TestPlan tp = new TestPlan();
    Resource res = ResourcebundleHelper.getResource(testPlanPath + "TestPlan.json");
    if (res == null)
      throw new IllegalArgumentException("No TestPlan.json found at " + testPlanPath);
    String descriptorContent = FileUtil.getContent(res);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testPlanObj = mapper.readTree(descriptorContent);
    tp.setName(testPlanObj.findValue("name").getTextValue());
    tp.setDescription(testPlanObj.findValue("description").getTextValue());
    tp.setStage(stage);
    if (testPlanObj.findValue("position") != null) {
      tp.setPosition(testPlanObj.findValue("position").getIntValue());
    }
    tp.setTestProcedure(testProcedure(testPlanPath));
    tp.setTestPackage(testPackage(testPlanPath));
    List<Resource> resources = getDirectories(testPlanPath + "*/");
    for (Resource resource : resources) {
      String fileName = fileName(resource);
      String location = fileName.substring(fileName.indexOf(testPlanPath), fileName.length());
      Resource descriptorResource = getDescriptorResource(location);
      String filename = descriptorResource.getFilename();
      if (filename.endsWith("TestCaseGroup.json")) {
        TestCaseGroup testCaseGroup = testCaseGroup(location, stage);
        tp.getTestCaseGroups().add(testCaseGroup);
      } else if (filename.endsWith("TestCase.json")) {
        TestCase testCase = testCase(location, stage);
        testCase.setParentName(tp.getName());
        tp.getTestCases().add(testCase);
      }
    }
    return tp;
  }

  protected TestObject testObject(String testObjectPath) throws IOException {
    logger.info("Processing test object at:" + testObjectPath);
    TestObject parent = new TestObject();
    Resource res = ResourcebundleHelper.getResource(testObjectPath + "TestObject.json");
    if (res == null)
      throw new IllegalArgumentException("No TestObject.json found at " + testObjectPath);
    String descriptorContent = FileUtil.getContent(res);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testPlanObj = mapper.readTree(descriptorContent);
    parent.setName(testPlanObj.findValue("name").getTextValue());
    if (testPlanObj.findValue("position") != null) {
      parent.setPosition(testPlanObj.findValue("position").getIntValue());
    }
    parent.setDescription(testPlanObj.findValue("description").getTextValue());
    JsonNode messageId = testPlanObj.findValue("messageId");
    JsonNode constraintId = testPlanObj.findValue("constraintId");
    JsonNode valueSetLibraryId = testPlanObj.findValue("valueSetLibraryId");
    if (messageId != null && constraintId != null && valueSetLibraryId != null) {
      parent.setTestContext(testContext(testObjectPath, testPlanObj));
    }
    List<Resource> resources = getDirectories(testObjectPath + "*/");
    for (Resource resource : resources) {
      String fileName = fileName(resource);
      String location = fileName.substring(fileName.indexOf(testObjectPath), fileName.length());
      TestObject testObject = testObject(location);
      parent.getChildren().add(testObject);
    }

    return parent;
  }

  protected void loadTestCasesDocumentation() throws IOException {
    TestCaseDocumentation doc =
        generateTestObjectDocumentation("Context-free", Stage.CF,
            testObjectRepository.findAllAsRoot());
    if (doc != null) {
      doc.setJson(obm.writeValueAsString(doc));
      testCaseDocumentationRepository.save(doc);
    }

    doc =
        generateTestCaseDocumentation("Context-based", Stage.CB,
            testPlanRepository.findAllByStage(Stage.CB));
    if (doc != null) {
      doc.setJson(obm.writeValueAsString(doc));
      testCaseDocumentationRepository.save(doc);
    }

    doc =
        generateTestCaseDocumentation("Isolated", Stage.ISOLATED,
            testPlanRepository.findAllByStage(Stage.ISOLATED));
    if (doc != null) {
      doc.setJson(obm.writeValueAsString(doc));
      testCaseDocumentationRepository.save(doc);
    }
  }

  protected TestCaseDocumentation generateTestCaseDocumentation(String title, Stage stage,
      List<TestPlan> tps) throws IOException {
    if (tps != null && !tps.isEmpty()) {
      TestCaseDocumentation documentation = new TestCaseDocumentation();
      documentation.setTitle(title);
      documentation.setStage(stage);
      for (TestPlan testPlan : tps) {
        documentation.getChildren().add(generateTestCaseDocument(testPlan));
      }
      return documentation;
    }
    return null;
  }

  protected TestCaseDocumentation generateTestObjectDocumentation(String title, Stage stage,
      List<TestObject> tos) throws IOException {
    if (tos != null && !tos.isEmpty()) {
      TestCaseDocumentation documentation = new TestCaseDocumentation();
      documentation.setTitle(title);
      documentation.setStage(stage);
      for (TestObject to : tos) {
        documentation.getChildren().add(generateTestCaseDocument(to));
      }
      return documentation;
    }
    return null;
  }



  protected gov.nist.hit.core.domain.TestCaseDocument generateTestCaseDocument(TestPlan tp)
      throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = generateAbsTestCaseDocument(tp);
    doc.setId(tp.getId());
    if (tp.getTestCaseGroups() != null && !tp.getTestCaseGroups().isEmpty()) {
      for (TestCaseGroup tcg : tp.getTestCaseGroups()) {
        doc.getChildren().add(generateTestCaseDocument(tcg));
      }
    }
    if (tp.getTestCases() != null && !tp.getTestCases().isEmpty()) {
      for (TestCase tc : tp.getTestCases()) {
        doc.getChildren().add(generateTestCaseDocument(tc));
      }
    }
    return doc;
  }

  protected gov.nist.hit.core.domain.TestCaseDocument generateTestCaseDocument(TestCaseGroup tcg)
      throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = generateAbsTestCaseDocument(tcg);
    doc.setId(tcg.getId());
    if (tcg.getTestCaseGroups() != null && !tcg.getTestCaseGroups().isEmpty()) {
      for (TestCaseGroup child : tcg.getTestCaseGroups()) {
        doc.getChildren().add(generateTestCaseDocument(child));
      }
    }

    if (tcg.getTestCases() != null && !tcg.getTestCases().isEmpty()) {
      for (TestCase tc : tcg.getTestCases()) {
        doc.getChildren().add(generateTestCaseDocument(tc));
      }
    }

    return doc;
  }


  protected gov.nist.hit.core.domain.TestCaseDocument generateTestCaseDocument(TestCase tc)
      throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = generateAbsTestCaseDocument(tc);
    doc.setId(tc.getId());
    if (tc.getTestSteps() != null && !tc.getTestSteps().isEmpty()) {
      for (TestStep ts : tc.getTestSteps()) {
        doc.getChildren().add(generateTestCaseDocument(ts));
      }
    }
    return doc;
  }

  protected gov.nist.hit.core.domain.TestCaseDocument generateTestCaseDocument(TestStep ts)
      throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = generateAbsTestCaseDocument(ts);
    doc.setId(ts.getId());
    setTestContextDocument(ts.getTestContext(), doc);
    return doc;
  }

  protected gov.nist.hit.core.domain.TestCaseDocument generateTestCaseDocument(TestObject to)
      throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = generateAbsTestCaseDocument(to);
    doc.setId(to.getId());
    setTestContextDocument(to.getTestContext(), doc);
    if (to.getChildren() != null && !to.getChildren().isEmpty()) {
      for (TestObject child : to.getChildren()) {
        doc.getChildren().add(generateTestCaseDocument(child));
      }
    }
    return doc;
  }


  protected gov.nist.hit.core.domain.TestCaseDocument generateAbsTestCaseDocument(
      AbstractTestCase ts) throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = new gov.nist.hit.core.domain.TestCaseDocument();
    doc.setTitle(ts.getName());
    doc.setType(ts.getType().toString());
    doc.setMcPath(ts.getMessageContent() != null ? ts.getMessageContent().getPdfPath() : null);
    doc.setTdsPath(ts.getTestDataSpecification() != null ? ts.getTestDataSpecification()
        .getPdfPath() : null);
    doc.setMcPath(ts.getTestStory() != null ? ts.getTestStory().getPdfPath() : null);
    doc.setTpPath(ts.getTestPackage() != null ? ts.getTestPackage().getPdfPath() : null);
    doc.setJdPath(ts.getJurorDocument() != null ? ts.getJurorDocument().getPdfPath() : null);
    return doc;
  }



  protected Resource getDescriptorResource(String location) throws IOException {
    Resource resource = ResourcebundleHelper.getResource(location + "TestPlan.json");
    resource =
        resource == null ? ResourcebundleHelper.getResource(location + "TestCaseGroup.json")
            : resource;
    resource =
        resource == null ? ResourcebundleHelper.getResource(location + "TestCase.json") : resource;
    resource =
        resource == null ? ResourcebundleHelper.getResource(location + "TestStep.json") : resource;
    resource = resource == null ? null : resource;
    return resource;
  }


  protected String fileName(Resource resource) throws IOException {
    String location = resource.getURL().toString();
    return location.replaceAll("%20", " ");
  }



  public TestPlanRepository getTestPlanRepository() {
    return testPlanRepository;
  }


  public void setTestPlanRepository(TestPlanRepository testPlanRepository) {
    this.testPlanRepository = testPlanRepository;
  }


  public TestObjectRepository getTestObjectRepository() {
    return testObjectRepository;
  }


  public void setTestObjectRepository(TestObjectRepository testObjectRepository) {
    this.testObjectRepository = testObjectRepository;
  }


  public TestStepRepository getTestStepRepository() {
    return testStepRepository;
  }


  public void setTestStepRepository(TestStepRepository testStepRepository) {
    this.testStepRepository = testStepRepository;
  }


  public TestCaseDocumentationRepository getTestCaseDocumentationRepository() {
    return testCaseDocumentationRepository;
  }


  public void setTestCaseDocumentationRepository(
      TestCaseDocumentationRepository testCaseDocumentationRepository) {
    this.testCaseDocumentationRepository = testCaseDocumentationRepository;
  }


  public com.fasterxml.jackson.databind.ObjectMapper getObm() {
    return obm;
  }

  public void setObm(com.fasterxml.jackson.databind.ObjectMapper obm) {
    this.obm = obm;
  }

  public List<Resource> getDirectories(String pattern) throws IOException {
    return ResourcebundleHelper.getDirectories(pattern);
  }

  public Resource getResource(String pattern) throws IOException {
    return ResourcebundleHelper.getResource(pattern);
  }

  public List<Resource> getResources(String pattern) throws IOException {
    return ResourcebundleHelper.getResources(pattern);
  }



  public IntegrationProfileRepository getIntegrationProfileRepository() {
    return integrationProfileRepository;
  }

  public void setIntegrationProfileRepository(
      IntegrationProfileRepository integrationProfileRepository) {
    this.integrationProfileRepository = integrationProfileRepository;
  }

  public VocabularyLibraryRepository getVocabularyLibraryRepository() {
    return vocabularyLibraryRepository;
  }

  public void setVocabularyLibraryRepository(VocabularyLibraryRepository vocabularyLibraryRepository) {
    this.vocabularyLibraryRepository = vocabularyLibraryRepository;
  }

  public MessageRepository getMessageRepository() {
    return messageRepository;
  }

  public void setMessageRepository(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  public ConstraintsRepository getConstraintsRepository() {
    return constraintsRepository;
  }

  public void setConstraintsRepository(ConstraintsRepository constraintsRepository) {
    this.constraintsRepository = constraintsRepository;
  }

  public ValueSetLibrarySerializer getValueSetLibrarySerializer() {
    return valueSetLibrarySerializer;
  }

  public void setValueSetLibrarySerializer(ValueSetLibrarySerializer valueSetLibrarySerializer) {
    this.valueSetLibrarySerializer = valueSetLibrarySerializer;
  }



  public Map<String, IntegrationProfile> getCachedProfiles() {
    return cachedProfiles;
  }


  public void setCachedProfiles(Map<String, IntegrationProfile> cachedProfiles) {
    this.cachedProfiles = cachedProfiles;
  }


  public Map<String, VocabularyLibrary> getCachedVocabLibraries() {
    return cachedVocabLibraries;
  }


  public void setCachedVocabLibraries(Map<String, VocabularyLibrary> cachedVocabLibraries) {
    this.cachedVocabLibraries = cachedVocabLibraries;
  }


  public Map<String, Constraints> getCachedConstraints() {
    return cachedConstraints;
  }


  public void setCachedConstraints(Map<String, Constraints> cachedConstraints) {
    this.cachedConstraints = cachedConstraints;
  }



}
