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

import gov.nist.auth.hit.core.repo.AccountRepository;
import gov.nist.hit.core.domain.AbstractTestCase;
import gov.nist.hit.core.domain.AppInfo;
import gov.nist.hit.core.domain.CFTestInstance;
import gov.nist.hit.core.domain.Constraints;
import gov.nist.hit.core.domain.DataMapping;
import gov.nist.hit.core.domain.DocumentType;
import gov.nist.hit.core.domain.IntegrationProfile;
import gov.nist.hit.core.domain.MappingSource;
import gov.nist.hit.core.domain.MappingSourceConstant;
import gov.nist.hit.core.domain.MappingSourceCurrentDate;
import gov.nist.hit.core.domain.MappingSourceRandom;
import gov.nist.hit.core.domain.Message;
import gov.nist.hit.core.domain.ProfileModel;
import gov.nist.hit.core.domain.Protocol;
import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseDocument;
import gov.nist.hit.core.domain.TestCaseDocumentation;
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.domain.TestContext;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestStepFieldPair;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.domain.TestingType;
import gov.nist.hit.core.domain.TransportForms;
import gov.nist.hit.core.domain.VocabularyLibrary;
import gov.nist.hit.core.repo.AppInfoRepository;
import gov.nist.hit.core.repo.CFTestInstanceRepository;
import gov.nist.hit.core.repo.ConstraintsRepository;
import gov.nist.hit.core.repo.DataMappingRepository;
import gov.nist.hit.core.repo.DocumentRepository;
import gov.nist.hit.core.repo.IntegrationProfileRepository;
import gov.nist.hit.core.repo.MessageRepository;
import gov.nist.hit.core.repo.TestCaseDocumentationRepository;
import gov.nist.hit.core.repo.TestPlanRepository;
import gov.nist.hit.core.repo.TestStepRepository;
import gov.nist.hit.core.repo.TestStepValidationReportRepository;
import gov.nist.hit.core.repo.TransactionRepository;
import gov.nist.hit.core.repo.TransportFormsRepository;
import gov.nist.hit.core.repo.TransportMessageRepository;
import gov.nist.hit.core.repo.VocabularyLibraryRepository;
import gov.nist.hit.core.service.exception.ProfileParserException;
import gov.nist.hit.core.service.util.FileUtil;
import gov.nist.hit.core.service.util.ResourcebundleHelper;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
  final static public String SCHEMA_PATTERN = "Global/Schema/";
  public static final String PROFILES_CONF_FILE_PATTERN = "Profiles.json";
  public static final String TABLES_CONF_FILE_PATTERN = "Tables.json";
  public static final String CONSTRAINTS_CONF_FILE_PATTERN = "Constraints.json";
  public static final String MESSAGECONTENT_INFO_PATTERN = "MessageContentInfo.html";
  public static final String TOOL_DOWNLOADS_PATTERN = "Documentation/Downloads/";
  public static final String TOOL_DOWNLOADS_CONF_PATTERN = "Downloads.json";
  public static final String TRANSPORT_PATTERN = "Global/Transport/";
  public static final String TRANSPORT_CONF_PATTERN = "Transport.json";
  public static final String REGISTRATION = "Registration.json";

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
  protected CFTestInstanceRepository testInstanceRepository;

  @Autowired
  protected TestStepRepository testStepRepository;

  @Autowired
  protected TestCaseDocumentationRepository testCaseDocumentationRepository;

  @Autowired
  protected CachedRepository cachedRepository;

  @Autowired
  protected TransportFormsRepository transportFormsRepository;

  @Autowired
  protected DBService dbService;

  @Autowired
  protected AccountRepository userRepository;

  @Autowired
  protected TransportMessageRepository transportMessageRepository;

  @Autowired
  protected TransactionRepository transactionRepository;

  @Autowired
  protected TestStepValidationReportRepository validationResultRepository;

  @Autowired
  protected DataMappingRepository dataMappingRepository;

  protected com.fasterxml.jackson.databind.ObjectMapper obm;

  @Autowired
  protected AppInfoService appInfoService;

  @Autowired
  protected VocabularyLibraryRepository vocabularyLibraryRepository;

  public ResourcebundleLoader() {
    obm = new com.fasterxml.jackson.databind.ObjectMapper();
    obm.setSerializationInclusion(Include.NON_NULL);
  }

  public boolean isNewResourcebundle() throws JsonProcessingException, IOException {
    String rsbVersion = getRsbleVersion();
    String oldRsbVersion = appInfoService.getRsbVersion();
    return oldRsbVersion == null || !rsbVersion.equals(oldRsbVersion);
  }

  public void clearDB() {
    appInfoRepository.deleteAll();
    validationResultRepository.deleteAll();
    testPlanRepository.deleteAll();
    testInstanceRepository.deleteAll();
    constraintsRepository.deleteAll();
    vocabularyLibraryRepository.deleteAll();
    integrationProfileRepository.deleteAll();
    testCaseDocumentationRepository.deleteAll();
    transportFormsRepository.deleteAll();
    documentRepository.deleteAll();
    transportMessageRepository.deleteAll();
    transactionRepository.deleteAll();
  }

  public void load() throws JsonProcessingException, IOException, ProfileParserException {
    if (isNewResourcebundle()) {
      logger.info("clearing tables...");
      clearDB();
      this.loadAppInfo();
      this.loadConstraints();
      this.loadVocabularyLibraries();
      this.loadIntegrationProfiles();
      this.loadContextFreeTestCases();
      this.loadContextBasedTestCases();
      this.loadTestCasesDocumentation();
      this.loadUserDocs();
      this.loadKownIssues();
      this.loadReleaseNotes();
      this.loadResourcesDocs();
      this.loadToolDownloads();
      this.loadTransport();
      cachedRepository.getCachedProfiles().clear();
      cachedRepository.getCachedVocabLibraries().clear();
      cachedRepository.getCachedConstraints().clear();
      logger.info("resource bundle loaded successfully...");
    }
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
    AppInfo appInfo = new AppInfo();
    JsonNode metaData = getMetaData();
    String rsbVersion = getRsbleVersion();
    appInfo.setRsbVersion(rsbVersion);
    appInfo.setAdminEmail(metaData.get("adminEmail").textValue());
    appInfo.setDomain(metaData.get("domain").textValue());
    appInfo.setHeader(metaData.get("header").textValue());
    appInfo.setHomeTitle(metaData.get("homeTitle") != null ? metaData.get("homeTitle").textValue()
        : null);
    appInfo.setHomeContent(metaData.get("homeContent") != null ? metaData.get("homeContent")
        .textValue() : null); // backward compatibility
    appInfo.setName(metaData.get("name").textValue());
    appInfo.setVersion(metaData.get("version").textValue());
    appInfo.setDate(new Date().getTime() + "");
    Resource resource =
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

    resource =
        ResourcebundleHelper.getResource(ResourcebundleLoader.ABOUT_PATTERN
            + ResourcebundleLoader.MESSAGECONTENT_INFO_PATTERN);
    if (resource != null) {
      appInfo.setMessageContentInfo(FileUtil.getContent(resource));
    }

    loadRegistration(appInfo);

    appInfo.setApiDocsPath("/apidocs");
    appInfoRepository.save(appInfo);
    logger.info("loading app info...DONE");
  }

  private void loadRegistration(AppInfo appInfo) throws JsonProcessingException, IOException {
    Resource resource =
        ResourcebundleHelper.getResource(ResourcebundleLoader.ABOUT_PATTERN
            + ResourcebundleLoader.REGISTRATION);
    if (resource != null) {
      String content = FileUtil.getContent(resource);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode node = mapper.readTree(content);
      appInfo.setRegistrationTitle(node.findValue("title") != null ? node.findValue("title")
          .textValue() : null);
      appInfo.setRegistrationAgreement(node.findValue("agreement") != null ? node.findValue(
          "agreement").textValue() : null);
      appInfo.setRegistrationSubmittedContent(node.findValue("submittedContent") != null ? node
          .findValue("submittedContent").textValue() : null);
      appInfo.setRegistrationSubmittedTitle(node.findValue("submittedTitle") != null ? node
          .findValue("submittedTitle").textValue() : null);
      appInfo.setRegistrationAcceptanceTitle(node.findValue("acceptanceTitle") != null ? node
          .findValue("acceptanceTitle").textValue() : null);
    }
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
        Iterator<JsonNode> it = userDocsObj.elements();
        if (it != null) {
          while (it.hasNext()) {
            JsonNode node = it.next();
            gov.nist.hit.core.domain.Document document = new gov.nist.hit.core.domain.Document();
            document.setPosition(node.findValue("order") != null ? node.findValue("order")
                .intValue() : userDocs.size() + 1);
            document.setTitle(node.findValue("title") != null ? node.findValue("title").textValue()
                : null);
            if (node.findValue("name") != null) {
              String path = node.findValue("name").textValue();
              if (path.endsWith("*")) {
                Resource rs =
                    getLatestResource(USERDOCS_PATTERN + node.findValue("name").textValue());
                path = rs.getFilename();
              }
              document.setName(path);
              document.setPath(USERDOCS_PATTERN + path);
            } else if (node.findValue("link") != null) {
              document.setPath(node.findValue("link").textValue());
            }
            document.setDate(node.findValue("date") != null ? node.findValue("date").textValue()
                : null);
            document.setType(DocumentType.USERDOC);
            document.setComments(node.findValue("comments") != null ? node.findValue("comments")
                .textValue() : null);
            userDocs.add(document);
          }
          if (!userDocs.isEmpty()) {
            documentRepository.save(userDocs);
          }
        }
      }
    }
  }


  private void loadTransport() throws IOException {
    logger.info("loading protocols info...");
    JsonNode json = toJsonObj(TRANSPORT_PATTERN + TRANSPORT_CONF_PATTERN);
    if (json != null) {
      List<TransportForms> transportForms = new ArrayList<TransportForms>();
      if (json.isArray()) {
        Iterator<JsonNode> it = json.elements();
        while (it.hasNext()) {
          JsonNode node = it.next();
          if (node.findValue("protocol") != null && node.findValue("forms") != null
              && node.findValue("domain") != null) {
            TransportForms tForms = new TransportForms();
            tForms.setProtocol(node.findValue("protocol").textValue());
            tForms.setDomain(node.findValue("domain").textValue());
            tForms.setDescription(node.findValue("description") != null ? node.findValue(
                "description").textValue() : null);
            JsonNode forms = node.findValue("forms");
            if (forms.get("TA_INITIATOR") == null || forms.get("SUT_INITIATOR") == null) {
              throw new RuntimeException(
                  "Transport.json TA_INITIATOR or SUT_INITIATOR form is missing");
            }
            tForms.setTaInitiatorForm(FileUtil.getContent(getResource(TRANSPORT_PATTERN
                + forms.get("TA_INITIATOR").textValue())));
            tForms.setSutInitiatorForm(FileUtil.getContent(getResource(TRANSPORT_PATTERN
                + forms.get("SUT_INITIATOR").textValue())));
            transportForms.add(tForms);
          } else {
            throw new RuntimeException(
                "Properties protocol, domain or forms not found in Transport.json");
          }
        }
        if (!transportForms.isEmpty()) {
          transportFormsRepository.save(transportForms);
        }
      } else {
        throw new RuntimeException("Transport.json file content must be an array");
      }
    }
  }

  private List<gov.nist.hit.core.domain.Document> getProfilesDocs() throws IOException {
    logger.info("loading integration profiles...");
    List<gov.nist.hit.core.domain.Document> resourceDocs =
        new ArrayList<gov.nist.hit.core.domain.Document>();
    JsonNode conf = toJsonObj(PROFILE_PATTERN + PROFILES_CONF_FILE_PATTERN);
    Set<String> skipped = null;
    JsonNode ordersObj = null;
    if (conf != null) {
      skipped = skippedAsList(conf.findValue("skip"));
      ordersObj = conf.findValue("orders");

    }
    List<Resource> resources = getResources(PROFILE_PATTERN + "*.xml");
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String fileName = resource.getFilename();
        if (skipped == null || !skipped.contains(fileName)) {
          gov.nist.hit.core.domain.Document document = new gov.nist.hit.core.domain.Document();
          document.setTitle(resource.getFilename().substring(0,
              resource.getFilename().indexOf(".xml")));
          document.setName(resource.getFilename());
          document.setPath(PROFILE_PATTERN + resource.getFilename());
          document.setType(DocumentType.PROFILE);
          document
              .setPosition(ordersObj != null && ordersObj.findValue(fileName) != null ? ordersObj
                  .findValue(fileName).intValue() : 0);
          resourceDocs.add(document);
        }
      }
    }
    return resourceDocs;
  }

  private List<gov.nist.hit.core.domain.Document> getConstraintsDocs() throws IOException {
    logger.info("loading constraints...");
    List<gov.nist.hit.core.domain.Document> resourceDocs =
        new ArrayList<gov.nist.hit.core.domain.Document>();
    JsonNode conf = toJsonObj(CONSTRAINT_PATTERN + CONSTRAINTS_CONF_FILE_PATTERN);
    Set<String> skipped = null;
    JsonNode ordersObj = null;
    if (conf != null) {
      skipped = skippedAsList(conf.findValue("skip"));
      ordersObj = conf.findValue("orders");
    }
    List<Resource> resources = getResources(CONSTRAINT_PATTERN + "*.xml");
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String fileName = resource.getFilename();
        if (skipped == null || !skipped.contains(fileName)) {
          gov.nist.hit.core.domain.Document document = new gov.nist.hit.core.domain.Document();
          document.setTitle(resource.getFilename().substring(0,
              resource.getFilename().indexOf(".xml")));
          document.setName(resource.getFilename());
          document.setPath(CONSTRAINT_PATTERN + resource.getFilename());
          document.setType(DocumentType.CONSTRAINT);
          document
              .setPosition(ordersObj != null && ordersObj.findValue(fileName) != null ? ordersObj
                  .findValue(fileName).intValue() : 0);
          resourceDocs.add(document);
        }
      }
    }
    return resourceDocs;
  }


  private List<gov.nist.hit.core.domain.Document> getValueSetsDocs() throws IOException {
    logger.info("loading constraints...");
    List<gov.nist.hit.core.domain.Document> resourceDocs =
        new ArrayList<gov.nist.hit.core.domain.Document>();
    JsonNode conf = toJsonObj(VALUESET_PATTERN + TABLES_CONF_FILE_PATTERN);
    Set<String> skipped = null;
    JsonNode ordersObj = null;
    logger.info("loading value sets...");
    // value sets
    skipped = null;
    if (conf != null) {
      skipped = skippedAsList(conf.findValue("skip"));
      ordersObj = conf.findValue("orders");
    }
    List<Resource> resources = getResources(VALUESET_PATTERN + "*.xml");
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String fileName = resource.getFilename();
        if (skipped == null || !skipped.contains(fileName)) {
          gov.nist.hit.core.domain.Document document = new gov.nist.hit.core.domain.Document();
          document.setTitle(resource.getFilename().substring(0,
              resource.getFilename().indexOf(".xml")));
          document.setName(resource.getFilename());
          document.setPath(VALUESET_PATTERN + resource.getFilename());
          document.setType(DocumentType.TABLE);
          document
              .setPosition(ordersObj != null && ordersObj.findValue(fileName) != null ? ordersObj
                  .findValue(fileName).intValue() : 0);
          resourceDocs.add(document);
        }
      }
    }
    return resourceDocs;
  }



  private void loadResourcesDocs() throws IOException {
    logger.info("loading resource documents...");
    List<gov.nist.hit.core.domain.Document> resourceDocs =
        new ArrayList<gov.nist.hit.core.domain.Document>();
    resourceDocs.addAll(getProfilesDocs());
    resourceDocs.addAll(getConstraintsDocs());
    resourceDocs.addAll(getValueSetsDocs());
    if (!resourceDocs.isEmpty()) {
      documentRepository.save(resourceDocs);
    }
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
        Iterator<JsonNode> it = knownIssueObj.elements();
        if (it != null) {
          while (it.hasNext()) {
            JsonNode node = it.next();
            gov.nist.hit.core.domain.Document document = new gov.nist.hit.core.domain.Document();
            document.setVersion(node.findValue("version") != null ? node.findValue("version")
                .textValue() : null);
            document.setTitle(node.findValue("title") != null ? node.findValue("title").textValue()
                : null);
            document.setName(node.findValue("name") != null ? node.findValue("name").textValue()
                : null);
            document.setPath(node.findValue("name") != null ? KNOWNISSUE_PATTERN
                + node.findValue("name").textValue() : null);
            document.setDate(node.findValue("date") != null ? node.findValue("date").textValue()
                : null);
            document.setType(DocumentType.KNOWNISSUE);
            document.setComments(node.findValue("comments") != null ? node.findValue("comments")
                .textValue() : null);
            knownIssues.add(document);
          }
          if (!knownIssues.isEmpty()) {
            documentRepository.save(knownIssues);
          }
        }
      }
    }
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
        Iterator<JsonNode> it = releaseNoteObj.elements();
        if (it != null) {
          while (it.hasNext()) {
            JsonNode node = it.next();
            gov.nist.hit.core.domain.Document document = new gov.nist.hit.core.domain.Document();
            document.setVersion(node.findValue("version") != null ? node.findValue("version")
                .textValue() : null);
            document.setTitle(node.findValue("title") != null ? node.findValue("title").textValue()
                : null);
            document.setName(node.findValue("name") != null ? node.findValue("name").textValue()
                : null);
            document.setPath(node.findValue("name") != null ? RELEASENOTE_PATTERN
                + node.findValue("name").textValue() : null);
            document.setDate(node.findValue("date") != null ? node.findValue("date").textValue()
                : null);
            document.setType(DocumentType.RELEASENOTE);
            document.setComments(node.findValue("comments") != null ? node.findValue("comments")
                .textValue() : null);
            releaseNotes.add(document);
          }
          if (!releaseNotes.isEmpty()) {
            documentRepository.save(releaseNotes);
          }
        }
      }
    }
  }


  private void loadToolDownloads() throws IOException {
    logger.info("loading tool downloads...");
    JsonNode confObj = toJsonObj(TOOL_DOWNLOADS_PATTERN + TOOL_DOWNLOADS_CONF_PATTERN);
    if (confObj != null) {
      if (confObj.findValue("installationGuide") != null) {
        gov.nist.hit.core.domain.Document installation = new gov.nist.hit.core.domain.Document();
        JsonNode instructionObj = confObj.findValue("installationGuide");
        if (instructionObj.findValue("title") == null || instructionObj.findValue("name") == null
            || instructionObj.findValue("date") == null) {
          throw new IllegalArgumentException(
              "The Download installation field is missing one of those: title, link, date");
        }

        installation.setTitle(instructionObj.findValue("title").textValue());
        installation.setName(instructionObj.findValue("name").textValue());
        installation.setPath(TOOL_DOWNLOADS_PATTERN + instructionObj.findValue("name").textValue());
        installation.setDate(instructionObj.findValue("date").textValue());
        installation.setType(DocumentType.INSTALLATION);
        documentRepository.save(installation);
      }

      if (confObj.findValue("downloads") != null) {
        JsonNode warFilesObj = confObj.findValue("downloads");
        if (warFilesObj.isArray()) {
          List<gov.nist.hit.core.domain.Document> docs =
              new ArrayList<gov.nist.hit.core.domain.Document>();
          Iterator<JsonNode> it = warFilesObj.elements();
          if (it != null) {
            while (it.hasNext()) {
              JsonNode node = it.next();
              gov.nist.hit.core.domain.Document document = new gov.nist.hit.core.domain.Document();
              if (node.findValue("title") == null || node.findValue("link") == null
                  || node.findValue("date") == null) {
                throw new IllegalArgumentException(
                    "The Download 'downloads' field is missing one of those: title, link, date");
              }
              document.setTitle(node.findValue("title").textValue());
              document.setPath(node.findValue("link").textValue());
              document.setDate(node.findValue("date").textValue());
              document.setType(DocumentType.DELIVERABLE);
              docs.add(document);
            }
            if (!docs.isEmpty()) {
              documentRepository.save(docs);
            }
          }
        }
      }
    }
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
    List<Resource> resources = getResources(domainPath(CONSTRAINT_PATTERN) + "*.xml");
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String content = FileUtil.getContent(resource);
        Constraints constraints = constraint(content);
        cachedRepository.getCachedConstraints().put(constraints.getSourceId(), constraints);
      }
    }
  }

  private JsonNode toJsonObj(String path) throws IOException {
    Resource resource = getResource(path);
    String content = FileUtil.getContent(resource);
    return content != null ? new ObjectMapper().readTree(content) : null;
  }


  private Set<String> skippedAsList(JsonNode arrNode) {
    Set<String> skipped = null;
    if (arrNode != null) {
      if (arrNode.isArray()) {
        skipped = new HashSet<String>();
        for (final JsonNode objNode : arrNode) {
          skipped.add(objNode.textValue());
        }
      }
    }
    return skipped;
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
          "Cannot find integration profile of conformance profile with id = " + id);
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

  private void loadContextBasedTestCases() throws IOException {
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

  private void loadContextFreeTestCases() throws IOException, ProfileParserException {
    List<Resource> resources = getDirectories(domainPath(CONTEXTFREE_PATTERN) + "*/");
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        CFTestInstance testObject =
            testObject(fileName.substring(fileName.indexOf(domainPath(CONTEXTFREE_PATTERN)),
                fileName.length()));
        if (testObject != null) {
          testObject.setRoot(true);
          testInstanceRepository.save(testObject);
        }
      }
    }
  }

  private TestCase testCase(String location, TestingStage stage, boolean transportSupported)
      throws IOException {
    logger.info("Processing test case located at:" + location);
    TestCase tc = new TestCase();
    Resource res = ResourcebundleHelper.getResource(location + "TestCase.json");
    if (res == null)
      throw new IllegalArgumentException("No TestCase.json found at " + location);
    String descriptorContent = FileUtil.getContent(res);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testCaseObj = mapper.readTree(descriptorContent);
    tc.setName(testCaseObj.findValue("name").textValue());
    tc.setDescription(testCaseObj.findValue("description").textValue());
    tc.setTestStory(testStory(location));
    tc.setJurorDocument(jurorDocument(location));
    if (testCaseObj.findValue("position") != null) {
      tc.setPosition(testCaseObj.findValue("position").intValue());
    }
    List<Resource> resources = getDirectories(location + "*");
    for (Resource resource : resources) {
      String fileName = fileName(resource);
      String tcLocation = fileName.substring(fileName.indexOf(location), fileName.length());
      TestStep testStep = testStep(tcLocation, stage, transportSupported);
      tc.addTestStep(testStep);
    }

    Collection<DataMapping> dataMappings = new HashSet<>();
    if (testCaseObj.findValue("mapping") != null) {
      Iterator<JsonNode> it = testCaseObj.findValue("mapping").elements();
      while (it.hasNext()) {
        JsonNode node = it.next();
        Map.Entry<String, JsonNode> sourcePair = node.findValue("source").fields().next();
        MappingSource source = null;
        if (sourcePair.getKey().toLowerCase().startsWith("teststep")) {
          TestStep testStep =
              findTestStep(tc.getTestSteps(), parseTestStepPosition(sourcePair.getKey()));
          String field = sourcePair.getValue().asText();
          if (testStep != null) {
            DataMapping dm =
                dataMappingRepository.getDataMappingByTestStepIdAndField(testStep.getId(), field);
            if (dm != null) {
              source = dm.getSource();
            } else {
              source = new TestStepFieldPair(testStep, field);
            }
          } else {
            logger.error("Unable to find testStep " + parseTestStepPosition(sourcePair.getKey())
                + " in test case " + tc.getId() + "");
          }
        } else {
          switch (sourcePair.getKey()) {
            case "random":
              source = new MappingSourceRandom(sourcePair.getValue().asText());
              break;
            case "current-date":
              source = new MappingSourceCurrentDate(sourcePair.getValue().asText());
              break;
            case "constant":
              source = new MappingSourceConstant(sourcePair.getValue().asText());
              break;
          }
        }
        Map.Entry<String, JsonNode> targetPair = node.findValue("target").fields().next();
        TestStepFieldPair target =
            new TestStepFieldPair(findTestStep(tc.getTestSteps(),
                parseTestStepPosition(targetPair.getKey())), targetPair.getValue().asText());
        if (source != null && target != null) {
          DataMapping dataMapping = new DataMapping(source, target, tc);
          logger.info("Saving data mapping : " + dataMapping.toString());
          dataMappings.add(dataMapping);
        }
      }
      tc.setDataMappings(dataMappings);
    }

    return tc;
  }

  private int parseTestStepPosition(String testCaseId) {
    if (testCaseId.toLowerCase().startsWith("teststep")) {
      return Integer.parseInt(testCaseId.toLowerCase().substring("teststep".length()));
    }
    return 0;
  }

  private TestStep findTestStep(List<TestStep> testStepMap, int position) {
    for (TestStep testStep : testStepMap) {
      if (testStep.getPosition() == position) {
        return testStep;
      }
    }
    return null;
  }

  private TestStep testStep(String location, TestingStage stage, boolean transportSupported)
      throws IOException {
    logger.info("Processing test step at:" + location);
    TestStep testStep = new TestStep();
    Resource res = ResourcebundleHelper.getResource(location + "TestStep.json");
    if (res == null)
      throw new IllegalArgumentException("No TestStep.json found at " + location);
    String descriptorContent = FileUtil.getContent(res);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testStepObj = mapper.readTree(descriptorContent);
    testStep.setName(testStepObj.findValue("name").textValue());
    testStep.setDescription(testStepObj.findValue("description").textValue());
    JsonNode ttypeObj = testStepObj.findValue("type");
    String tttypeValue = ttypeObj != null ? ttypeObj.textValue() : null;
    TestingType testingType =
        tttypeValue != null && !"".equals(tttypeValue) ? TestingType.valueOf(tttypeValue)
            : TestingType.DATAINSTANCE;
    testStep.setTestingType(testingType);
    if (transportSupported
        && (TestingType.SUT_INITIATOR.equals(testingType) || TestingType.TA_INITIATOR
            .equals(testingType))) {
      JsonNode protocolsNode = testStepObj.findValue("protocols");
      if (protocolsNode == null || !protocolsNode.isArray()) {
        throw new IllegalArgumentException(
            "Transport is supported but no protocol defined. Test Step location=" + location);
      }
      for (int i = 0; i < protocolsNode.size(); i++) {
        String protocol = protocolsNode.get(i).textValue();
        testStep.getProtocols().add(new Protocol(protocol, i + 1, i == 0));
      }
    }

    if (!testingType.equals(TestingType.SUT_MANUAL) && !testingType.equals(TestingType.TA_MANUAL)) {
      testStep.setTestContext(testContext(location, testStepObj, stage));
    }
    testStep.setTestStory(testStory(location));
    testStep.setJurorDocument(jurorDocument(location));
    testStep.setMessageContent(messageContent(location));
    testStep.setTestDataSpecification(testDataSpecification(location));
    if (testStepObj.findValue("position") != null) {
      testStep.setPosition(testStepObj.findValue("position").intValue());
    }
    return testStep;
  }

  private TestArtifact testStory(String location) throws IOException {
    return artifact(location, "TestStory");
  }

  private TestArtifact jurorDocument(String location) throws IOException {
    return artifact(location, "JurorDocument");
  }

  private TestArtifact artifact(String location, String type) throws IOException {
    TestArtifact doc = null;

    String path = location + type + ".html";
    Resource resource = ResourcebundleHelper.getResource(path);
    if (resource != null) {
      doc = doc == null ? new TestArtifact(type) : doc;
      doc.setHtml(FileUtil.getContent(resource));
    }

    path = location + type + ".pdf";
    resource = ResourcebundleHelper.getResource(path);
    if (resource != null) {
      doc = doc == null ? new TestArtifact(type) : doc;
      doc.setPdfPath(path);
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


  private TestArtifact testPlanSummary(String location) throws IOException {
    // return artifact(location, "QuickTestCaseReferenceGuide");
    return artifact(location, "TestPlanSummary");
  }



  private TestCaseGroup testCaseGroup(String location, TestingStage stage, boolean transportEnabled)
      throws IOException {
    logger.info("Processing test case group at:" + location);
    Resource descriptorRsrce = ResourcebundleHelper.getResource(location + "TestCaseGroup.json");
    if (descriptorRsrce == null)
      throw new IllegalArgumentException("No TestCaseGroup.json found at " + location);
    String descriptorContent = FileUtil.getContent(descriptorRsrce);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testPlanObj = mapper.readTree(descriptorContent);
    if (testPlanObj.findValue("skip") == null || !testPlanObj.findValue("skip").booleanValue()) {
      TestCaseGroup tcg = new TestCaseGroup();
      tcg.setName(testPlanObj.findValue("name").textValue());
      tcg.setDescription(testPlanObj.findValue("description").textValue());
      tcg.setTestStory(testStory(location));
      if (testPlanObj.findValue("position") != null) {
        tcg.setPosition(testPlanObj.findValue("position").intValue());
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
          TestCaseGroup testCaseGroup = testCaseGroup(tcLocation, stage, transportEnabled);
          tcg.getTestCaseGroups().add(testCaseGroup);
        } else if (filename.endsWith("TestCase.json")) {
          TestCase testCase = testCase(tcLocation, stage, transportEnabled);
          tcg.getTestCases().add(testCase);
        }
      }
      return tcg;
    }
    return null;
  }


  private TestPlan testPlan(String location, TestingStage stage) throws IOException {
    logger.info("Processing test plan  at:" + location);
    Resource res = ResourcebundleHelper.getResource(location + "TestPlan.json");
    if (res == null)
      throw new IllegalArgumentException("No TestPlan.json found at " + location);
    String descriptorContent = FileUtil.getContent(res);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testPlanObj = mapper.readTree(descriptorContent);
    if (testPlanObj.findValue("skip") == null || !testPlanObj.findValue("skip").booleanValue()) {
      TestPlan tp = new TestPlan();
      tp.setName(testPlanObj.findValue("name").textValue());
      tp.setDescription(testPlanObj.findValue("description").textValue());
      tp.setTestStory(testStory(location));
      tp.setStage(stage);

      if (testPlanObj.findValue("transport") != null
          && testPlanObj.findValue("transport").booleanValue()
          && (testPlanObj.findValue("domain") == null
              || testPlanObj.findValue("domain").textValue() == null || testPlanObj.findValue(
              "domain").textValue() == "")) {
        throw new IllegalArgumentException(
            "Transport is supported for the following test plan but no domain is defined. Test Plan location="
                + location);
      }
      tp.setDomain(testPlanObj.findValue("domain") != null ? testPlanObj.findValue("domain")
          .textValue() : null);
      tp.setTransport(testPlanObj.findValue("transport") != null ? testPlanObj.findValue(
          "transport").booleanValue() : false);
      if (testPlanObj.findValue("position") != null) {
        tp.setPosition(testPlanObj.findValue("position").intValue());
      } else {
        tp.setPosition(1);
      }
      tp.setTestPackage(testPackage(location));
      tp.setTestPlanSummary(testPlanSummary(location));
      List<Resource> resources = getDirectories(location + "*/");
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        String loca = fileName.substring(fileName.indexOf(location), fileName.length());
        Resource descriptorResource = getDescriptorResource(loca);
        String filename = descriptorResource.getFilename();
        if (filename.endsWith("TestCaseGroup.json")) {
          TestCaseGroup testCaseGroup = testCaseGroup(loca, stage, tp.isTransport());
          tp.getTestCaseGroups().add(testCaseGroup);
        } else if (filename.endsWith("TestCase.json")) {
          TestCase testCase = testCase(loca, stage, tp.isTransport());
          tp.getTestCases().add(testCase);
        }
      }
      return tp;
    }
    return null;
  }

  private CFTestInstance testObject(String testObjectPath) throws IOException {
    logger.info("Processing test object at:" + testObjectPath);
    Resource res = ResourcebundleHelper.getResource(testObjectPath + "TestObject.json");
    if (res == null)
      throw new IllegalArgumentException("No TestObject.json found at " + testObjectPath);
    String descriptorContent = FileUtil.getContent(res);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testPlanObj = mapper.readTree(descriptorContent);
    if (testPlanObj.findValue("skip") == null || !testPlanObj.findValue("skip").booleanValue()) {
      CFTestInstance parent = new CFTestInstance();
      parent.setName(testPlanObj.findValue("name").textValue());
      if (testPlanObj.findValue("position") != null) {
        parent.setPosition(testPlanObj.findValue("position").intValue());
      }
      parent.setDescription(testPlanObj.findValue("description").textValue());
      parent.setTestContext(testContext(testObjectPath, testPlanObj, TestingStage.CF));
      List<Resource> resources = getDirectories(testObjectPath + "*/");
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        String location = fileName.substring(fileName.indexOf(testObjectPath), fileName.length());
        CFTestInstance testObject = testObject(location);
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
            testInstanceRepository.findAllAsRoot());
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
      List<CFTestInstance> tos) throws IOException {
    if (tos != null && !tos.isEmpty()) {
      TestCaseDocumentation documentation = new TestCaseDocumentation();
      documentation.setTitle(title);
      documentation.setStage(stage);
      Collections.sort(tos);
      for (CFTestInstance to : tos) {
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
      Collections.sort(tp.getTestCaseGroups());
      for (TestCaseGroup tcg : tp.getTestCaseGroups()) {
        doc.getChildren().add(generateTestCaseDocument(tcg));
      }
    }
    if (tp.getTestCases() != null && !tp.getTestCases().isEmpty()) {
      Collections.sort(tp.getTestCases());
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
      Collections.sort(tcg.getTestCaseGroups());
      for (TestCaseGroup child : tcg.getTestCaseGroups()) {
        doc.getChildren().add(generateTestCaseDocument(child));
      }
    }

    if (tcg.getTestCases() != null && !tcg.getTestCases().isEmpty()) {
      Collections.sort(tcg.getTestCases());
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
      Collections.sort(tc.getTestSteps());
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
    if (ts.getTestContext() != null) {
      doc.setId(ts.getTestContext().getId());
    }
    return doc;
  }

  private gov.nist.hit.core.domain.TestCaseDocument generateTestCaseDocument(CFTestInstance to)
      throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = generateTestCaseDocument(to.getTestContext());
    doc = initTestCaseDocument(to, doc);
    doc.setId(to.getId());
    if (to.getChildren() != null && !to.getChildren().isEmpty()) {
      Collections.sort(to.getChildren());
      for (CFTestInstance child : to.getChildren()) {
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
    doc.setTsPath(ts.getTestStory() != null ? ts.getTestStory().getPdfPath() : null);
    if (ts instanceof TestPlan) {
      TestPlan tp = (TestPlan) ts;
      doc.setTpPath(tp.getTestPackage() != null ? tp.getTestPackage().getPdfPath() : null);
      doc.setTpsPath(tp.getTestPlanSummary() != null ? tp.getTestPlanSummary().getPdfPath() : null);
    } else if (ts instanceof TestStep) {
      TestStep tStep = (TestStep) ts;
      doc.setMcPath(tStep.getMessageContent() != null ? tStep.getMessageContent().getPdfPath()
          : null);
      doc.setTdsPath(tStep.getTestDataSpecification() != null ? tStep.getTestDataSpecification()
          .getPdfPath() : null);
      doc.setJdPath(tStep.getJurorDocument() != null ? tStep.getJurorDocument().getPdfPath() : null);
    }
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


  public static String getRsbleVersion() throws JsonProcessingException, IOException {
    JsonNode metaData = getMetaData();
    if (metaData.get("rsbVersion") == null || "".equals(metaData.get("rsbVersion").textValue()))
      throw new RuntimeException("rsbVersion not set or found in MetaData.json");
    return metaData.get("rsbVersion").textValue();
  }

  public static JsonNode getMetaData() throws JsonProcessingException, IOException {
    Resource resource =
        ResourcebundleHelper.getResource(ResourcebundleLoader.ABOUT_PATTERN + "MetaData.json");
    if (resource == null)
      throw new RuntimeException("No MetaData.json found in the resource bundle");
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readTree(FileUtil.getContent(resource));
  }


  public TestPlanRepository getTestPlanRepository() {
    return testPlanRepository;
  }


  public void setTestPlanRepository(TestPlanRepository testPlanRepository) {
    this.testPlanRepository = testPlanRepository;
  }



  public AppInfoRepository getAppInfoRepository() {
    return appInfoRepository;
  }

  public void setAppInfoRepository(AppInfoRepository appInfoRepository) {
    this.appInfoRepository = appInfoRepository;
  }

  public DocumentRepository getDocumentRepository() {
    return documentRepository;
  }

  public void setDocumentRepository(DocumentRepository documentRepository) {
    this.documentRepository = documentRepository;
  }

  public CFTestInstanceRepository getTestInstanceRepository() {
    return testInstanceRepository;
  }

  public void setTestInstanceRepository(CFTestInstanceRepository testInstanceRepository) {
    this.testInstanceRepository = testInstanceRepository;
  }

  public CachedRepository getCachedRepository() {
    return cachedRepository;
  }

  public void setCachedRepository(CachedRepository cachedRepository) {
    this.cachedRepository = cachedRepository;
  }

  public TransportFormsRepository getTransportFormsRepository() {
    return transportFormsRepository;
  }

  public void setTransportFormsRepository(TransportFormsRepository transportFormsRepository) {
    this.transportFormsRepository = transportFormsRepository;
  }

  public DBService getDbService() {
    return dbService;
  }

  public void setDbService(DBService dbService) {
    this.dbService = dbService;
  }

  public AccountRepository getUserRepository() {
    return userRepository;
  }

  public void setUserRepository(AccountRepository userRepository) {
    this.userRepository = userRepository;
  }

  public TransportMessageRepository getTransportMessageRepository() {
    return transportMessageRepository;
  }

  public void setTransportMessageRepository(TransportMessageRepository transportMessageRepository) {
    this.transportMessageRepository = transportMessageRepository;
  }

  public TransactionRepository getTransactionRepository() {
    return transactionRepository;
  }

  public void setTransactionRepository(TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  public TestStepValidationReportRepository getValidationResultRepository() {
    return validationResultRepository;
  }

  public void setValidationResultRepository(
      TestStepValidationReportRepository validationResultRepository) {
    this.validationResultRepository = validationResultRepository;
  }

  public AppInfoService getAppInfoService() {
    return appInfoService;
  }

  public void setAppInfoService(AppInfoService appInfoService) {
    this.appInfoService = appInfoService;
  }

  public VocabularyLibraryRepository getVocabularyLibraryRepository() {
    return vocabularyLibraryRepository;
  }

  public void setVocabularyLibraryRepository(VocabularyLibraryRepository vocabularyLibraryRepository) {
    this.vocabularyLibraryRepository = vocabularyLibraryRepository;
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
