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
import gov.nist.hit.core.domain.AppInfo;
import gov.nist.hit.core.domain.Constraints;
import gov.nist.hit.core.domain.DocumentType;
import gov.nist.hit.core.domain.IntegrationProfile;
import gov.nist.hit.core.domain.Message;
import gov.nist.hit.core.domain.ProfileModel;
import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseDocument;
import gov.nist.hit.core.domain.TestCaseDocumentation;
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.domain.TestCaseTestingType;
import gov.nist.hit.core.domain.TestContext;
import gov.nist.hit.core.domain.TestObject;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestStepTestingType;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.domain.VocabularyLibrary;
import gov.nist.hit.core.repo.AppInfoRepository;
import gov.nist.hit.core.repo.ConstraintsRepository;
import gov.nist.hit.core.repo.DocumentRepository;
import gov.nist.hit.core.repo.IntegrationProfileRepository;
import gov.nist.hit.core.repo.MessageRepository;
import gov.nist.hit.core.repo.TestCaseDocumentationRepository;
import gov.nist.hit.core.repo.TestObjectRepository;
import gov.nist.hit.core.repo.TestPlanRepository;
import gov.nist.hit.core.repo.TestStepRepository;
import gov.nist.hit.core.service.exception.ProfileParserException;
import gov.nist.hit.core.service.util.FileUtil;
import gov.nist.hit.core.service.util.ResourcebundleHelper;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

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
  final static public String ISOLATED_PATTERN = "Isolated/";
  public static final String RELEASENOTE_PATTERN = "Documentation/ReleaseNotes/";
  public static final String RELEASENOTE_FILE_PATTERN = "ReleaseNotes.json";
  public static final String KNOWNISSUE_PATTERN = "Documentation/KnownIssues/";
  public static final String KNOWNISSUE_FILE_PATTERN = "KnownIssues.json";
  public static final String USERDOCS_PATTERN = "Documentation/UserDocs/";
  public static final String USERDOCS_FILE_PATTERN = "UserDocs.json";
  public static final String PROFILE_INFO_PATTERN = "ProfileInfo.html";
  public static final String VALUE_SET_COPYRIGHT_PATTERN = "Copyright.html";
  public static final String CONFIDENTIALITY_PATTERN = "Confidentiality.html";
  public static final String DISCLAIMER_PATTERN = "Disclaimer.html";
  public static final String VALIDATIONRESULT_INFO_PATTERN = "ValidationResultInfo.html";
  public static final String ACKNOWLEDGMENT_PATTERN = "Acknowledgment.html";
  public static final String HOME_PATTERN = "Home.html";

  @Autowired
  IntegrationProfileRepository integrationProfileRepository;

  @Autowired
  MessageRepository messageRepository;

  @Autowired
  ConstraintsRepository constraintsRepository;

  @Autowired
  AppInfoRepository appInfoRepository;

  @Autowired
  DocumentRepository documentRepository;

  @Autowired
  protected TestPlanRepository testPlanRepository;

  @Autowired
  protected TestObjectRepository testObjectRepository;

  @Autowired
  protected TestStepRepository testStepRepository;

  @Autowired
  protected TestCaseDocumentationRepository testCaseDocumentationRepository;

  @Autowired
  protected CachedRepository cachedRepository;

  protected com.fasterxml.jackson.databind.ObjectMapper obm;

  public ResourcebundleLoader() {
    obm = new com.fasterxml.jackson.databind.ObjectMapper();
    obm.setSerializationInclusion(Include.NON_NULL);
  }



  public void load() throws JsonProcessingException, IOException, ProfileParserException {
    logger.info("loading resource bundle...");
    this.loadAppInfo();
    this.loadConstraints();
    this.loadVocabularyLibraries();
    this.loadIntegrationProfiles();
    this.loadCfTestCases();
    this.loadCbTestCases();
    this.loadIsolatedTestCases();
    this.loadTestCasesDocumentation();
    this.loadUserDocs();
    this.loadKownIssues();
    this.loadReleaseNotes();
    cachedRepository.getCachedProfiles().clear();
    cachedRepository.getCachedVocabLibraries().clear();
    cachedRepository.getCachedConstraints().clear();
    logger.info("loading resource bundle...DONE");
  }


  public abstract TestContext testContext(String location, JsonNode parentOb, TestingStage stage)
      throws IOException;

  public abstract TestCaseDocument generateTestCaseDocument(TestContext c) throws IOException;

  public abstract ProfileModel parseProfile(String integrationProfileXml,
      String conformanceProfileId, String constraintsXml, String additionalConstraintsXml)
      throws ProfileParserException, UnsupportedOperationException;

  public abstract VocabularyLibrary vocabLibrary(String content) throws JsonGenerationException,
      JsonMappingException, IOException, UnsupportedOperationException;



  private void loadAppInfo() throws JsonProcessingException, IOException {
    logger.info("loading app info...");
    Resource resource =
        ResourcebundleHelper.getResource(ResourcebundleLoader.ABOUT_PATTERN + "MetaData.json");
    if (resource == null)
      throw new RuntimeException("No MetaData.json found in the resource bundle");
    AppInfo appInfo = new AppInfo();
    ObjectMapper mapper = new ObjectMapper();
    JsonNode appInfoJson = mapper.readTree(FileUtil.getContent(resource));
    appInfo.setAdminEmail(appInfoJson.get("adminEmail").getTextValue());
    appInfo.setDomain(appInfoJson.get("domain").getTextValue());
    appInfo.setHeader(appInfoJson.get("header").getTextValue());
    appInfo.setHomeTitle(appInfoJson.get("homeTitle") != null ? appInfoJson.get("homeTitle")
        .getTextValue() : null);
    appInfo.setHomeContent(appInfoJson.get("homeContent") != null ? appInfoJson.get("homeContent")
        .getTextValue() : null); // backward compatibility
    appInfo.setName(appInfoJson.get("name").getTextValue());
    appInfo.setVersion(appInfoJson.get("version").getTextValue());
    resource =
        ResourcebundleHelper.getResource(ResourcebundleLoader.PROFILE_PATTERN
            + PROFILE_INFO_PATTERN);
    if (resource != null) {
      appInfo.setProfileInfo(FileUtil.getContent(resource));
    }

    resource =
        ResourcebundleHelper.getResource(ResourcebundleLoader.VALUESET_PATTERN
            + ResourcebundleLoader.VALUE_SET_COPYRIGHT_PATTERN);
    if (resource != null) {
      appInfo.setValueSetCopyright(FileUtil.getContent(resource));
    }


    resource =
        ResourcebundleHelper.getResource(ResourcebundleLoader.ABOUT_PATTERN
            + ResourcebundleLoader.CONFIDENTIALITY_PATTERN);
    if (resource != null) {
      appInfo.setConfidentiality(FileUtil.getContent(resource));
    }

    resource =
        ResourcebundleHelper.getResource(ResourcebundleLoader.ABOUT_PATTERN
            + ResourcebundleLoader.DISCLAIMER_PATTERN);
    if (resource != null) {
      appInfo.setDisclaimer(FileUtil.getContent(resource));
    }

    resource =
        ResourcebundleHelper.getResource(ResourcebundleLoader.ABOUT_PATTERN
            + ResourcebundleLoader.VALIDATIONRESULT_INFO_PATTERN);
    if (resource != null) {
      appInfo.setValidationResultInfo(FileUtil.getContent(resource));
    }

    resource =
        ResourcebundleHelper.getResource(ResourcebundleLoader.ABOUT_PATTERN
            + ResourcebundleLoader.ACKNOWLEDGMENT_PATTERN);
    if (resource != null) {
      appInfo.setAcknowledgment(FileUtil.getContent(resource));
    }

    resource =
        ResourcebundleHelper.getResource(ResourcebundleLoader.ABOUT_PATTERN
            + ResourcebundleLoader.HOME_PATTERN);
    if (resource != null) {
      appInfo.setHomeContent(FileUtil.getContent(resource));
    }



    appInfoRepository.save(appInfo);

    logger.info("loading app info...DONE");
  }

  private void loadUserDocs() throws IOException {
    logger.info("loading user documents...");
    Resource resource = getResource(USERDOCS_PATTERN + USERDOCS_FILE_PATTERN);
    if (resource != null) {
      String descriptorContent = FileUtil.getContent(resource);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode userDocsObj = mapper.readTree(descriptorContent);
      List<gov.nist.hit.core.domain.Document> userDocs =
          new ArrayList<gov.nist.hit.core.domain.Document>();
      if (userDocsObj.isArray()) {
        Iterator<JsonNode> it = userDocsObj.getElements();
        if (it != null) {
          while (it.hasNext()) {
            JsonNode node = it.next();
            gov.nist.hit.core.domain.Document document = new gov.nist.hit.core.domain.Document();
            document.setPosition(node.findValue("order") != null ? node.findValue("order")
                .getIntValue() : userDocs.size() + 1);
            document.setTitle(node.findValue("title") != null ? node.findValue("title")
                .getTextValue() : null);
            if (node.findValue("name") != null) {
              String path = node.findValue("name").getTextValue();
              if (path.endsWith("*")) {
                Resource rs =
                    getLatestResource(USERDOCS_PATTERN + node.findValue("name").getTextValue());
                path = rs.getFilename();
              }
              document.setName(path);
              document.setPath(USERDOCS_PATTERN + path);
            } else if (node.findValue("link") != null) {
              document.setPath(node.findValue("link").getTextValue());
            }
            document.setDate(node.findValue("date") != null ? node.findValue("date").getTextValue()
                : null);
            document.setType(DocumentType.USERDOC);
            document.setComments(node.findValue("comments") != null ? node.findValue("comments")
                .getTextValue() : null);
            userDocs.add(document);
          }
          if (!userDocs.isEmpty()) {
            documentRepository.save(userDocs);
          }
        }
      }
    }

    logger.info("loading user documents...DONE");
  }

  private void loadKownIssues() throws IOException {
    logger.info("loading known issues...");
    Resource resource = getResource(KNOWNISSUE_PATTERN + KNOWNISSUE_FILE_PATTERN);
    if (resource != null) {
      String descriptorContent = FileUtil.getContent(resource);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode knownIssueObj = mapper.readTree(descriptorContent);
      List<gov.nist.hit.core.domain.Document> knownIssues =
          new ArrayList<gov.nist.hit.core.domain.Document>();
      if (knownIssueObj.isArray()) {
        Iterator<JsonNode> it = knownIssueObj.getElements();
        if (it != null) {
          while (it.hasNext()) {
            JsonNode node = it.next();
            gov.nist.hit.core.domain.Document document = new gov.nist.hit.core.domain.Document();
            document.setVersion(node.findValue("version") != null ? node.findValue("version")
                .getTextValue() : null);
            document.setTitle(node.findValue("title") != null ? node.findValue("title")
                .getTextValue() : null);
            document.setName(node.findValue("name") != null ? node.findValue("name").getTextValue()
                : null);
            document.setPath(node.findValue("name") != null ? KNOWNISSUE_PATTERN
                + node.findValue("name").getTextValue() : null);
            document.setDate(node.findValue("date") != null ? node.findValue("date").getTextValue()
                : null);
            document.setType(DocumentType.KNOWNISSUE);
            document.setComments(node.findValue("comments") != null ? node.findValue("comments")
                .getTextValue() : null);
            knownIssues.add(document);
          }
          if (!knownIssues.isEmpty()) {
            documentRepository.save(knownIssues);
          }
        }
      }
    }
    logger.info("loading known issues...DONE");
  }

  private void loadReleaseNotes() throws IOException {
    logger.info("loading release notes...");
    Resource resource = getResource(RELEASENOTE_PATTERN + RELEASENOTE_FILE_PATTERN);
    if (resource != null) {
      String descriptorContent = FileUtil.getContent(resource);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode releaseNoteObj = mapper.readTree(descriptorContent);
      List<gov.nist.hit.core.domain.Document> releaseNotes =
          new ArrayList<gov.nist.hit.core.domain.Document>();
      if (releaseNoteObj.isArray()) {
        Iterator<JsonNode> it = releaseNoteObj.getElements();
        if (it != null) {
          while (it.hasNext()) {
            JsonNode node = it.next();
            gov.nist.hit.core.domain.Document document = new gov.nist.hit.core.domain.Document();
            document.setVersion(node.findValue("version") != null ? node.findValue("version")
                .getTextValue() : null);
            document.setTitle(node.findValue("title") != null ? node.findValue("title")
                .getTextValue() : null);
            document.setName(node.findValue("name") != null ? node.findValue("name").getTextValue()
                : null);
            document.setPath(node.findValue("name") != null ? RELEASENOTE_PATTERN
                + node.findValue("name").getTextValue() : null);
            document.setDate(node.findValue("date") != null ? node.findValue("date").getTextValue()
                : null);
            document.setType(DocumentType.RELEASENOTE);
            document.setComments(node.findValue("comments") != null ? node.findValue("comments")
                .getTextValue() : null);
            releaseNotes.add(document);
          }
          if (!releaseNotes.isEmpty()) {
            documentRepository.save(releaseNotes);
          }
        }
      }
    }
    logger.info("loading release notes...DONE");
  }


  private Resource getLatestResource(String pathWithWilcard) throws IOException {
    List<Resource> resources = getResources(pathWithWilcard);
    if (resources != null && !resources.isEmpty()) {
      Collections.sort(resources, new Comparator<Resource>() {
        @Override
        public int compare(Resource o1, Resource o2) {
          return o2.getFilename().compareTo(o1.getFilename());
        }
      });
      return resources.get(0);
    }
    throw new IllegalArgumentException("Could not determine the most recent file matching "
        + pathWithWilcard);
  }

  private void loadConstraints() throws IOException {
    logger.info("loading constraints...");
    List<Resource> resources = getResources(domainPath(CONSTRAINT_PATTERN) + "*");
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String content = FileUtil.getContent(resource);
        Constraints constraints = constraint(content);
        cachedRepository.getCachedConstraints().put(constraints.getSourceId(), constraints);
      }
    }
    logger.info("loading constraints...DONE");
  }

  private void loadIntegrationProfiles() throws IOException {
    logger.info("loading integration profiles...");
    List<Resource> resources = getResources(domainPath(PROFILE_PATTERN) + "*.xml");
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        IntegrationProfile integrationProfile = integrationProfile(FileUtil.getContent(resource));
        integrationProfileRepository.save(integrationProfile);
      }
    }
    logger.info("loading integration profiles...DONE");
  }

  private void loadVocabularyLibraries() throws IOException {
    logger.info("loading value set libraries...");
    List<Resource> resources = getResources(domainPath(VALUESET_PATTERN) + "*.xml");
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String content = FileUtil.getContent(resource);
        try {
          VocabularyLibrary vocabLibrary = vocabLibrary(content);
          cachedRepository.getCachedVocabLibraries().put(vocabLibrary.getSourceId(), vocabLibrary);
        } catch (UnsupportedOperationException e) {
        }
      }
    }
    logger.info("loading value set libraries...DONE");
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

  private IntegrationProfile integrationProfile(String content) {
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
      if (cachedRepository.getCachedProfiles().containsKey(id)) {
        throw new RuntimeException("Found duplicate conformance profile id " + id);
      }
      cachedRepository.getCachedProfiles().put(id, integrationProfile);
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

  private Constraints constraint(String content) {
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

  protected String jsonConformanceProfile(String integrationProfileXml,
      String conformanceProfileId, String constraintsXml, String additionalConstraintsXml)
      throws ProfileParserException, JsonProcessingException,
      com.fasterxml.jackson.core.JsonProcessingException {
    try {
      ProfileModel profileModel =
          parseProfile(integrationProfileXml, conformanceProfileId, constraintsXml,
              additionalConstraintsXml);
      String json = obm.writeValueAsString(profileModel);
      return json;
    } catch (UnsupportedOperationException e) {

    }
    return null;
  }


  protected IntegrationProfile getIntegrationProfile(String id) throws IOException {
    IntegrationProfile p = cachedRepository.getCachedProfiles().get(id);
    if (p == null) {
      throw new IllegalArgumentException(
          "Cannot find IntegrationProfile associated to ConformanceProfile with id = " + id);
    }

    return p;
  }

  protected Constraints getConstraints(String id) throws IOException {
    Constraints c = cachedRepository.getCachedConstraints().get(id);
    if (c == null) {
      throw new IllegalArgumentException("Constraints with id = " + id + " not found");
    }
    return c;
  }

  protected VocabularyLibrary getVocabularyLibrary(String id) throws IOException {
    VocabularyLibrary v = cachedRepository.getCachedVocabLibraries().get(id);
    if (v == null) {
      throw new IllegalArgumentException("VocabularyLibrary with id = " + id + " not found");
    }
    return v;
  }



  public String domainPath(String path) {
    return path;
  }


  private void loadIsolatedTestCases() throws IOException {
    List<Resource> resources = getDirectories(domainPath(ISOLATED_PATTERN) + "*/");
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        String location =
            fileName.substring(fileName.indexOf(domainPath(ISOLATED_PATTERN)), fileName.length());
        TestPlan testPlan = testPlan(location, TestingStage.ISOLATED);
        if (testPlan != null)
          testPlanRepository.save(testPlan);
      }
    }
  }

  private void loadCbTestCases() throws IOException {
    List<Resource> resources = getDirectories(domainPath(CONTEXTBASED_PATTERN) + "*/");
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        String location =
            fileName.substring(fileName.indexOf(domainPath(CONTEXTBASED_PATTERN)),
                fileName.length());
        TestPlan testPlan = testPlan(location, TestingStage.CB);
        if (testPlan != null)
          testPlanRepository.save(testPlan);
      }
    }
  }

  private void loadCfTestCases() throws IOException, ProfileParserException {
    List<Resource> resources = getDirectories(domainPath(CONTEXTFREE_PATTERN) + "*/");
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        TestObject testObject =
            testObject(fileName.substring(fileName.indexOf(domainPath(CONTEXTFREE_PATTERN)),
                fileName.length()));
        if (testObject != null) {
          testObject.setRoot(true);
          testObjectRepository.save(testObject);
        }
      }
    }
  }

  private TestCase testCase(String location, TestingStage stage) throws IOException {
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
      tc.setTestingType(TestCaseTestingType.valueOf(testCaseObj.findValue("type").getTextValue()
          .toUpperCase()));
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

  private TestStep testStep(String location, TestingStage stage) throws IOException {
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
    JsonNode ttypeObj = testStepObj.findValue("type");
    String tttypeValue = ttypeObj != null ? ttypeObj.getTextValue() : null;
    TestStepTestingType testingType =
        tttypeValue != null ? TestStepTestingType.valueOf(tttypeValue)
            : TestStepTestingType.DATAINSTANCE;
    testStep.setTestingType(testingType);
    if (!testingType.equals(TestStepTestingType.SUT_MANUAL)
        && !testingType.equals(TestStepTestingType.TA_MANUAL)) {
      testStep.setTestContext(testContext(location, testStepObj, stage));
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

  private TestArtifact testStory(String location) throws IOException {
    return artifact(location, "TestStory");
  }

  private TestArtifact testProcedure(String location) throws IOException {
    return artifact(location, "TestProcedure");
  }

  private TestArtifact jurorDocument(String location) throws IOException {
    return artifact(location, "JurorDocument");
  }

  private TestArtifact artifact(String location, String type) throws IOException {
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


  private TestArtifact messageContent(String location) throws IOException {
    return artifact(location, "MessageContent");
  }

  private TestArtifact testDataSpecification(String location) throws IOException {
    return artifact(location, "TestDataSpecification");
  }

  private TestArtifact testPackage(String location) throws IOException {
    return artifact(location, "TestPackage");
  }


  private TestCaseGroup testCaseGroup(String location, TestingStage stage) throws IOException {
    logger.info("Processing test case group at:" + location);
    Resource descriptorRsrce = ResourcebundleHelper.getResource(location + "TestCaseGroup.json");
    if (descriptorRsrce == null)
      throw new IllegalArgumentException("No TestCaseGroup.json found at " + location);
    String descriptorContent = FileUtil.getContent(descriptorRsrce);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testPlanObj = mapper.readTree(descriptorContent);
    if (testPlanObj.findValue("skip") == null || !testPlanObj.findValue("skip").getBooleanValue()) {
      TestCaseGroup tcg = new TestCaseGroup();
      tcg.setName(testPlanObj.findValue("name").getTextValue());
      tcg.setDescription(testPlanObj.findValue("description").getTextValue());
      if (testPlanObj.findValue("position") != null) {
        tcg.setPosition(testPlanObj.findValue("position").getIntValue());
      } else {
        tcg.setPosition(1);
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
    return null;
  }


  private TestPlan testPlan(String testPlanPath, TestingStage stage) throws IOException {
    logger.info("Processing test plan  at:" + testPlanPath);
    Resource res = ResourcebundleHelper.getResource(testPlanPath + "TestPlan.json");
    if (res == null)
      throw new IllegalArgumentException("No TestPlan.json found at " + testPlanPath);
    String descriptorContent = FileUtil.getContent(res);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testPlanObj = mapper.readTree(descriptorContent);
    if (testPlanObj.findValue("skip") == null || !testPlanObj.findValue("skip").getBooleanValue()) {
      TestPlan tp = new TestPlan();
      tp.setName(testPlanObj.findValue("name").getTextValue());
      tp.setDescription(testPlanObj.findValue("description").getTextValue());
      tp.setStage(stage);
      if (testPlanObj.findValue("position") != null) {
        tp.setPosition(testPlanObj.findValue("position").getIntValue());
      } else {
        tp.setPosition(1);
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
    return null;
  }

  private TestObject testObject(String testObjectPath) throws IOException {
    logger.info("Processing test object at:" + testObjectPath);
    Resource res = ResourcebundleHelper.getResource(testObjectPath + "TestObject.json");
    if (res == null)
      throw new IllegalArgumentException("No TestObject.json found at " + testObjectPath);
    String descriptorContent = FileUtil.getContent(res);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testPlanObj = mapper.readTree(descriptorContent);
    if (testPlanObj.findValue("skip") == null || !testPlanObj.findValue("skip").getBooleanValue()) {
      TestObject parent = new TestObject();
      parent.setName(testPlanObj.findValue("name").getTextValue());
      if (testPlanObj.findValue("position") != null) {
        parent.setPosition(testPlanObj.findValue("position").getIntValue());
      }
      parent.setDescription(testPlanObj.findValue("description").getTextValue());
      parent.setTestContext(testContext(testObjectPath, testPlanObj, TestingStage.CF));
      List<Resource> resources = getDirectories(testObjectPath + "*/");
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        String location = fileName.substring(fileName.indexOf(testObjectPath), fileName.length());
        TestObject testObject = testObject(location);
        if (testObject != null) {
          parent.getChildren().add(testObject);
        }
      }

      return parent;
    }
    return null;
  }

  private void loadTestCasesDocumentation() throws IOException {
    TestCaseDocumentation doc =
        generateTestObjectDocumentation("Context-free", TestingStage.CF,
            testObjectRepository.findAllAsRoot());
    if (doc != null) {
      doc.setJson(obm.writeValueAsString(doc));
      testCaseDocumentationRepository.save(doc);
    }

    doc =
        generateTestCaseDocumentation("Context-based", TestingStage.CB,
            testPlanRepository.findAllByStage(TestingStage.CB));
    if (doc != null) {
      doc.setJson(obm.writeValueAsString(doc));
      testCaseDocumentationRepository.save(doc);
    }

    doc =
        generateTestCaseDocumentation("Isolated", TestingStage.ISOLATED,
            testPlanRepository.findAllByStage(TestingStage.ISOLATED));
    if (doc != null) {
      doc.setJson(obm.writeValueAsString(doc));
      testCaseDocumentationRepository.save(doc);
    }
  }

  private TestCaseDocumentation generateTestCaseDocumentation(String title, TestingStage stage,
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

  private TestCaseDocumentation generateTestObjectDocumentation(String title, TestingStage stage,
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



  private gov.nist.hit.core.domain.TestCaseDocument generateTestCaseDocument(TestPlan tp)
      throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = initTestCaseDocument(tp);
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

  private gov.nist.hit.core.domain.TestCaseDocument generateTestCaseDocument(TestCaseGroup tcg)
      throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = initTestCaseDocument(tcg);
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


  private gov.nist.hit.core.domain.TestCaseDocument generateTestCaseDocument(TestCase tc)
      throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = initTestCaseDocument(tc);
    doc.setId(tc.getId());
    if (tc.getTestSteps() != null && !tc.getTestSteps().isEmpty()) {
      for (TestStep ts : tc.getTestSteps()) {
        doc.getChildren().add(generateTestCaseDocument(ts));
      }
    }
    return doc;
  }

  private gov.nist.hit.core.domain.TestCaseDocument generateTestCaseDocument(TestStep ts)
      throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = generateTestCaseDocument(ts.getTestContext());
    doc = initTestCaseDocument(ts, doc);
    doc.setId(ts.getId());
    return doc;
  }

  private gov.nist.hit.core.domain.TestCaseDocument generateTestCaseDocument(TestObject to)
      throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = generateTestCaseDocument(to.getTestContext());
    doc = initTestCaseDocument(to, doc);
    doc.setId(to.getId());
    if (to.getChildren() != null && !to.getChildren().isEmpty()) {
      for (TestObject child : to.getChildren()) {
        doc.getChildren().add(generateTestCaseDocument(child));
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

  // public Map<String, IntegrationProfile> getCachedProfiles() {
  // return cachedProfiles;
  // }
  //
  //
  // public void setCachedProfiles(Map<String, IntegrationProfile> cachedProfiles) {
  // this.cachedProfiles = cachedProfiles;
  // }
  //
  //
  // public Map<String, VocabularyLibrary> getCachedVocabLibraries() {
  // return cachedVocabLibraries;
  // }
  //
  //
  // public void setCachedVocabLibraries(Map<String, VocabularyLibrary> cachedVocabLibraries) {
  // this.cachedVocabLibraries = cachedVocabLibraries;
  // }
  //
  //
  // public Map<String, Constraints> getCachedConstraints() {
  // return cachedConstraints;
  // }
  //
  //
  // public void setCachedConstraints(Map<String, Constraints> cachedConstraints) {
  // this.cachedConstraints = cachedConstraints;
  // }



}
