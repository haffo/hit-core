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

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.transaction.annotation.Transactional;
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

import gov.nist.auth.hit.core.domain.TestingType;
import gov.nist.auth.hit.core.repo.AccountRepository;
import gov.nist.hit.core.domain.AbstractTestCase;
import gov.nist.hit.core.domain.AppInfo;
import gov.nist.hit.core.domain.CFTestPlan;
import gov.nist.hit.core.domain.CFTestStep;
import gov.nist.hit.core.domain.CFTestStepGroup;
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
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestStepFieldPair;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.domain.TransportForms;
import gov.nist.hit.core.domain.VocabularyLibrary;
import gov.nist.hit.core.repo.AppInfoRepository;
import gov.nist.hit.core.repo.CFTestPlanRepository;
import gov.nist.hit.core.repo.CFTestStepRepository;
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
import gov.nist.hit.core.service.util.GCUtil;
import gov.nist.hit.core.service.util.ResourcebundleHelper;

@PropertySource(value = {"classpath:app-config.properties"})
@Transactional(value = "transactionManager")
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
  public static final String DEFAULT_CATEGORY = "Default";


  @Autowired
  protected IntegrationProfileRepository integrationProfileRepository;

  @Autowired
  protected MessageRepository messageRepository;

  @Autowired
  protected ConstraintsRepository constraintsRepository;

  @Autowired
  protected AppInfoRepository appInfoRepository;

  @Autowired
  protected DocumentRepository documentRepository;

  @Autowired
  protected TestPlanRepository testPlanRepository;

  @Autowired
  protected CFTestStepRepository cfTestStepRepository;

  @Autowired
  protected CFTestPlanRepository cfTestPlanRepository;


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

  private Map<Long, String> idLocationMap;

  @Value("${admin.emails}")
  private List<String> adminEmails;

  @Value("${cf.management.supported:false}")
  private boolean cfManagementSupported;

  @Value("${cb.management.supported:false}")
  private boolean cbManagementSupported;

  @Value("${authentication.required:false}")
  private boolean authenticationRequired;

  @Value("${employer.required:false}")
  private boolean employerRequired;

  @Value("${app.organization.logo:null}")
  private String appOrganizationLogo;

  @Value("${app.organization.name:'NIST'}")
  private String organizationName;

  @Value("${app.organization.link:null}")
  private String appOrganizationLink;

  @Value("${app.division.link:null}")
  private String appDivisionLink;

  @Value("${app.division.logo:null}")
  private String appDivisionLogo;

  @Value("${app.division.name:null}")
  private String appDivisionName;

  @Value("${app.name}")
  private String appName;

  @Value("${app.version}")
  private String appVersion;

  @Value("${app.domain}")
  private String appDomain;

  @Value("${app.home.title}")
  private String appHomeTitle;

  @Value("${app.home.content}")
  private String appHomeContent;

  @Value("${app.resourceBundleVersion:null}")
  private String appResourceBundleVersion;


  @Value("${app.igVersion:null}")
  private String appIgVersion;

  @Value("${app.description}")
  private String appDescription;


  @Value("${app.header}")
  private String appHeader;


  @Value("${app.contactEmail}")
  private String appContactEmail;


  @Value("${app.upload.contentTypes:'application/xml,text/xml,text/plain'}")
  private String appUploadContentTypes;

  @Value("${app.upload.maxSize:'100MB'}")
  private String appUploadMaxSize;

  @Value("${app.acknowledgment.content:null}")
  private String appAcknowledgment;

  @Value("${app.privacy.content:null}")
  private String appPrivacyContent;

  @Value("${app.privacy.link:null}")
  private String appPrivacyLink;

  @Value("${app.disclaimer.content:null}")
  private String appDisclaimerContent;

  @Value("${app.disclaimer.link:null}")
  private String appDisclaimerLink;

  @Value("${app.confidentiality.content:null}")
  private String appConfidentialityContent;

  @Value("${app.confidentiality.link:null}")
  private String appConfidentialityLink;


  @Value("${app.messageContentInfo.content:null}")
  private String appMessageContentInfoContent;

  @Value("${app.validationResultInfo.content:null}")
  private String appValidationResultInfoContent;

  @Value("${app.valueSetCopyRight.content:null}")
  private String appValueSetCopyRightContent;

  @Value("${app.profileInfo.content:null}")
  private String appProfileInfoContent;


  @Value("${app.registration.title}")
  private String appRegistrationTitle;

  @Value("${app.registration.agreement}")
  private String appRegistrationAgreement;


  @Value("${app.registration.submittedContent}")
  private String appRegistrationSubmittedContent;

  @Value("${app.registration.submittedTitle}")
  private String appRegistrationSubmittedTitle;

  @Value("${app.registration.acceptanceTitle}")
  private String appRegistrationAcceptanceTitle;

  @Value("${download.war.disabled:false}")
  private boolean appDownloadWarDisabled;



  public ResourcebundleLoader() {
    idLocationMap = new HashMap<>();
    obm = new com.fasterxml.jackson.databind.ObjectMapper();
    obm.setSerializationInclusion(Include.NON_NULL);
  }


  public boolean isNewResourcebundle() throws JsonProcessingException, IOException {
    String oldRsbVersion = appInfoService.getRsbVersion();
    if(oldRsbVersion == null){
      oldRsbVersion = getRsbleVersion();
    }
    return oldRsbVersion == null || appResourceBundleVersion == null
        || !appResourceBundleVersion.equals(oldRsbVersion);
  }

  public void clearDB() {
    appInfoRepository.deleteAll();
    validationResultRepository.deleteAll();
    testPlanRepository.deletePreloaded();
    cfTestPlanRepository.deletePreloaded();
    constraintsRepository.deletePreloaded();
    vocabularyLibraryRepository.deletePreloaded();
    integrationProfileRepository.deletePreloaded();
    testCaseDocumentationRepository.deleteAll();
    transportFormsRepository.deleteAll();
    documentRepository.deleteAll();
    transportMessageRepository.deleteAll();
    transactionRepository.deleteAll();
  }

  public void load(String directory)
      throws JsonProcessingException, IOException, ProfileParserException {
    if (appResourceBundleVersion == null) {
      appResourceBundleVersion = getRsbleVersion();
    }
    if (isNewResourcebundle()) {
      logger.info("clearing tables...");
      clearDB();
      this.idLocationMap = new HashMap<>();
      this.loadAppInfo();
      this.loadConstraints(directory);
      this.loadVocabularyLibraries(directory);
      this.loadIntegrationProfiles(directory);
      this.loadContextFreeTestCases(directory);
      this.loadContextBasedTestCases(directory);
      this.loadTestCasesDocumentation();
      this.loadUserDocs(directory);
      this.loadKownIssues(directory);
      this.loadReleaseNotes(directory);
      this.loadResourcesDocs(directory);
      this.loadToolDownloads(directory);
      this.loadTransport(directory);
      cachedRepository.getCachedProfiles().clear();
      cachedRepository.getCachedVocabLibraries().clear();
      cachedRepository.getCachedConstraints().clear();
      logger.info("resource bundle loaded successfully...");
    }
    this.loadDynamicValues();
    GCUtil.performGC();
  }

  public void load() throws JsonProcessingException, IOException, ProfileParserException {
    load("");
  }

  public abstract TestContext testContext(String location, JsonNode parentOb, TestingStage stage,
      String rootPath) throws IOException;

  public abstract TestCaseDocument generateTestCaseDocument(TestContext c) throws IOException;

  public abstract ProfileModel parseProfile(String integrationProfileXml,
      String conformanceProfileId, String constraintsXml, String additionalConstraintsXml)
      throws ProfileParserException, UnsupportedOperationException;

  public abstract VocabularyLibrary vocabLibrary(String content) throws JsonGenerationException,
      JsonMappingException, IOException, UnsupportedOperationException;

  public void loadAppInfo() throws JsonProcessingException, IOException {
    logger.info("loading app info...");
    AppInfo appInfo = new AppInfo();
    appInfoRepository.save(appInfo);
    logger.info("loading app info...DONE");
  }


  public void loadUserDocs(String rootPath) throws IOException {
    logger.info("loading user documents...");
    Resource resource = getResource(USERDOCS_PATTERN + USERDOCS_FILE_PATTERN, rootPath);
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
            document.setPosition(node.findValue("order") != null
                ? node.findValue("order").intValue() : userDocs.size() + 1);
            document.setTitle(
                node.findValue("title") != null ? node.findValue("title").textValue() : null);
            if (node.findValue("name") != null) {
              String path = node.findValue("name").textValue();
              if (path.endsWith("*")) {
                Resource rs = getLatestResource(
                    USERDOCS_PATTERN + node.findValue("name").textValue(), rootPath);
                path = rs.getFilename();
              }
              document.setName(path);
              document.setPath(USERDOCS_PATTERN + path);
            } else if (node.findValue("link") != null) {
              document.setPath(node.findValue("link").textValue());
            }
            document.setDate(
                node.findValue("date") != null ? node.findValue("date").textValue() : null);
            document.setType(DocumentType.USERDOC);
            document.setComments(
                node.findValue("comments") != null ? node.findValue("comments").textValue() : null);
            userDocs.add(document);
          }
          if (!userDocs.isEmpty()) {
            documentRepository.save(userDocs);
          }
        }
      }
    }
  }

  public void loadTransport(String rootPath) throws IOException {
    logger.info("loading protocols info...");
    JsonNode json = toJsonObj(TRANSPORT_PATTERN + TRANSPORT_CONF_PATTERN, rootPath);
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
            tForms.setDescription(node.findValue("description") != null
                ? node.findValue("description").textValue() : null);
            JsonNode forms = node.findValue("forms");
            if (forms.get("TA_INITIATOR") == null || forms.get("SUT_INITIATOR") == null) {
              throw new RuntimeException(
                  "Transport.json TA_INITIATOR or SUT_INITIATOR form is missing");
            }
            tForms.setTaInitiatorForm(FileUtil.getContent(
                getResource(TRANSPORT_PATTERN + forms.get("TA_INITIATOR").textValue(), rootPath)));
            tForms.setSutInitiatorForm(FileUtil.getContent(
                getResource(TRANSPORT_PATTERN + forms.get("SUT_INITIATOR").textValue(), rootPath)));
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

  private List<gov.nist.hit.core.domain.Document> getProfilesDocs(String rootPath)
      throws IOException {
    logger.info("loading integration profiles...");
    List<gov.nist.hit.core.domain.Document> resourceDocs =
        new ArrayList<gov.nist.hit.core.domain.Document>();
    JsonNode conf = toJsonObj(PROFILE_PATTERN + PROFILES_CONF_FILE_PATTERN, rootPath);
    Set<String> skipped = null;
    JsonNode ordersObj = null;
    if (conf != null) {
      skipped = skippedAsList(conf.findValue("skip"));
      ordersObj = conf.findValue("orders");

    }
    List<Resource> resources = getResources(PROFILE_PATTERN + "*.xml", rootPath);
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String fileName = resource.getFilename();
        if (skipped == null || !skipped.contains(fileName)) {
          gov.nist.hit.core.domain.Document document = new gov.nist.hit.core.domain.Document();
          document.setTitle(
              resource.getFilename().substring(0, resource.getFilename().indexOf(".xml")));
          document.setName(resource.getFilename());
          document.setPath(PROFILE_PATTERN + resource.getFilename());
          document.setType(DocumentType.PROFILE);
          document.setPosition(ordersObj != null && ordersObj.findValue(fileName) != null
              ? ordersObj.findValue(fileName).intValue() : 0);
          resourceDocs.add(document);
        }
      }
    }
    return resourceDocs;
  }

  private List<gov.nist.hit.core.domain.Document> getConstraintsDocs(String rootPath)
      throws IOException {
    logger.info("loading constraints...");
    List<gov.nist.hit.core.domain.Document> resourceDocs =
        new ArrayList<gov.nist.hit.core.domain.Document>();
    JsonNode conf = toJsonObj(CONSTRAINT_PATTERN + CONSTRAINTS_CONF_FILE_PATTERN, rootPath);
    Set<String> skipped = null;
    JsonNode ordersObj = null;
    if (conf != null) {
      skipped = skippedAsList(conf.findValue("skip"));
      ordersObj = conf.findValue("orders");
    }
    List<Resource> resources = getResources(CONSTRAINT_PATTERN + "*.xml", rootPath);
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String fileName = resource.getFilename();
        if (skipped == null || !skipped.contains(fileName)) {
          gov.nist.hit.core.domain.Document document = new gov.nist.hit.core.domain.Document();
          document.setTitle(
              resource.getFilename().substring(0, resource.getFilename().indexOf(".xml")));
          document.setName(resource.getFilename());
          document.setPath(CONSTRAINT_PATTERN + resource.getFilename());
          document.setType(DocumentType.CONSTRAINT);
          document.setPosition(ordersObj != null && ordersObj.findValue(fileName) != null
              ? ordersObj.findValue(fileName).intValue() : 0);
          resourceDocs.add(document);
        }
      }
    }
    return resourceDocs;
  }

  private List<gov.nist.hit.core.domain.Document> getValueSetsDocs(String rootPath)
      throws IOException {
    logger.info("loading constraints...");
    List<gov.nist.hit.core.domain.Document> resourceDocs =
        new ArrayList<gov.nist.hit.core.domain.Document>();
    JsonNode conf = toJsonObj(VALUESET_PATTERN + TABLES_CONF_FILE_PATTERN, rootPath);
    Set<String> skipped = null;
    JsonNode ordersObj = null;
    logger.info("loading value sets...");
    // value sets
    skipped = null;
    if (conf != null) {
      skipped = skippedAsList(conf.findValue("skip"));
      ordersObj = conf.findValue("orders");
    }
    List<Resource> resources = getResources(VALUESET_PATTERN + "*.xml", rootPath);
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String fileName = resource.getFilename();
        if (skipped == null || !skipped.contains(fileName)) {
          gov.nist.hit.core.domain.Document document = new gov.nist.hit.core.domain.Document();
          document.setTitle(
              resource.getFilename().substring(0, resource.getFilename().indexOf(".xml")));
          document.setName(resource.getFilename());
          document.setPath(VALUESET_PATTERN + resource.getFilename());
          document.setType(DocumentType.TABLE);
          document.setPosition(ordersObj != null && ordersObj.findValue(fileName) != null
              ? ordersObj.findValue(fileName).intValue() : 0);
          resourceDocs.add(document);
        }
      }
    }
    return resourceDocs;
  }

  public void loadResourcesDocs(String rootPath) throws IOException {
    logger.info("loading resource documents...");
    List<gov.nist.hit.core.domain.Document> resourceDocs =
        new ArrayList<gov.nist.hit.core.domain.Document>();
    resourceDocs.addAll(getProfilesDocs(rootPath));
    resourceDocs.addAll(getConstraintsDocs(rootPath));
    resourceDocs.addAll(getValueSetsDocs(rootPath));
    if (!resourceDocs.isEmpty()) {
      documentRepository.save(resourceDocs);
    }
  }

  public void loadKownIssues(String rootPath) throws IOException {
    logger.info("loading known issues...");
    Resource resource = getResource(KNOWNISSUE_PATTERN + KNOWNISSUE_FILE_PATTERN, rootPath);
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
            document.setVersion(
                node.findValue("version") != null ? node.findValue("version").textValue() : null);
            document.setTitle(
                node.findValue("title") != null ? node.findValue("title").textValue() : null);
            document.setName(
                node.findValue("name") != null ? node.findValue("name").textValue() : null);
            document.setPath(node.findValue("name") != null
                ? KNOWNISSUE_PATTERN + node.findValue("name").textValue() : null);
            document.setDate(
                node.findValue("date") != null ? node.findValue("date").textValue() : null);
            document.setType(DocumentType.KNOWNISSUE);
            document.setComments(
                node.findValue("comments") != null ? node.findValue("comments").textValue() : null);
            knownIssues.add(document);
          }
          if (!knownIssues.isEmpty()) {
            documentRepository.save(knownIssues);
          }
        }
      }
    }
  }

  public void loadReleaseNotes(String rootPath) throws IOException {
    logger.info("loading release notes...");
    Resource resource = getResource(RELEASENOTE_PATTERN + RELEASENOTE_FILE_PATTERN, rootPath);
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
            document.setVersion(
                node.findValue("version") != null ? node.findValue("version").textValue() : null);
            document.setTitle(
                node.findValue("title") != null ? node.findValue("title").textValue() : null);
            document.setName(
                node.findValue("name") != null ? node.findValue("name").textValue() : null);
            document.setPath(node.findValue("name") != null
                ? RELEASENOTE_PATTERN + node.findValue("name").textValue() : null);
            document.setDate(
                node.findValue("date") != null ? node.findValue("date").textValue() : null);
            document.setType(DocumentType.RELEASENOTE);
            document.setComments(
                node.findValue("comments") != null ? node.findValue("comments").textValue() : null);
            releaseNotes.add(document);
          }
          if (!releaseNotes.isEmpty()) {
            documentRepository.save(releaseNotes);
          }
        }
      }
    }
  }

  public void loadToolDownloads(String rootPath) throws IOException {
    if (!appInfoService.get().isDownloadWarDisabled()) {
      logger.info("loading tool downloads...");
      JsonNode confObj = toJsonObj(TOOL_DOWNLOADS_PATTERN + TOOL_DOWNLOADS_CONF_PATTERN, rootPath);
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
          installation
              .setPath(TOOL_DOWNLOADS_PATTERN + instructionObj.findValue("name").textValue());
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
                gov.nist.hit.core.domain.Document document =
                    new gov.nist.hit.core.domain.Document();
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
  }

  private Resource getLatestResource(String pathWithWilcard, String rootPath) throws IOException {
    List<Resource> resources = getResources(pathWithWilcard, rootPath);
    if (resources != null && !resources.isEmpty()) {
      Collections.sort(resources, new Comparator<Resource>() {
        @Override
        public int compare(Resource o1, Resource o2) {
          return o2.getFilename().compareTo(o1.getFilename());
        }
      });
      return resources.get(0);
    }
    throw new IllegalArgumentException(
        "Could not determine the most recent file matching " + pathWithWilcard);
  }

  public void loadConstraints(String rootPath) throws IOException {
    logger.info("loading constraints...");
    List<Resource> resources = getResources(domainPath(CONSTRAINT_PATTERN) + "*.xml", rootPath);
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String content = FileUtil.getContent(resource);
        Constraints constraints = constraint(content);
        cachedRepository.getCachedConstraints().put(constraints.getSourceId(), constraints);
        this.constraintsRepository.save(constraints);
      }
    }
  }

  private JsonNode toJsonObj(String path, String rootPath) throws IOException {
    Resource resource = getResource(path, rootPath);
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

  public void loadIntegrationProfiles(String rootPath) throws IOException {
    logger.info("loading integration profiles...");
    List<Resource> resources = getResources(domainPath(PROFILE_PATTERN) + "*.xml", rootPath);
    if (resources != null && !resources.isEmpty()) {

      for (Resource resource : resources) {
        IntegrationProfile integrationProfile = integrationProfile(FileUtil.getContent(resource));
        integrationProfileRepository.save(integrationProfile);
      }
    }
  }

  public void loadVocabularyLibraries(String rootPath) throws IOException {
    logger.info("loading value set libraries...");
    List<Resource> resources = getResources(domainPath(VALUESET_PATTERN) + "*.xml", rootPath);
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String content = FileUtil.getContent(resource);
        try {
          VocabularyLibrary vocabLibrary = vocabLibrary(content);
          this.vocabularyLibraryRepository.save(vocabLibrary);
          cachedRepository.getCachedVocabLibraries().put(vocabLibrary.getSourceId(), vocabLibrary);
        } catch (UnsupportedOperationException e) {
        }
      }
    }
  }

  protected Constraints additionalConstraints(String content) throws IOException {
    if (content == null) {
      return null;
    }
    return constraint(content);

  }

  public IntegrationProfile integrationProfile(String content) {
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

    // Message IDs
    List<String> ids = new ArrayList<String>();

    for (int j = 0; j < messages.getLength(); j++) {
      Element elmCode = (Element) messages.item(j);
      String id = elmCode.getAttribute("ID");
      ids.add(id);

      if (cachedRepository.getCachedProfiles().containsKey(id)) {
        throw new RuntimeException("Found duplicate conformance profile id " + id);
      }
      cachedRepository.getCachedProfiles().put(id, integrationProfile);
    }
    integrationProfile.setMessages(ids);
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

  public Constraints constraint(String content) {
    Document doc = this.stringToDom(content);
    Constraints constraints = new Constraints();
    constraints.setXml(content);
    Element constraintsElement = (Element) doc.getElementsByTagName("ConformanceContext").item(0);
    constraints.setSourceId(constraintsElement.getAttribute("UUID"));

    Element metaDataElement = (Element) constraintsElement.getElementsByTagName("MetaData").item(0);
    if (metaDataElement != null)
      constraints.setDescription(metaDataElement.getAttribute("Description"));
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
    try {
      ProfileModel profileModel = parseProfile(integrationProfileXml, conformanceProfileId,
          constraintsXml, additionalConstraintsXml);
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

  private void checkPersistentId(Long persistentId, String location) {
    if (null == persistentId) {
      logger.error("Null id at location: " + location);
    } else if (idLocationMap.containsKey(persistentId)
        && !idLocationMap.get(persistentId).equals(location)
        && location.toLowerCase().endsWith(".json")) {
      logger.error("Duplicate id (" + persistentId + ") at location: " + location
          + " - already exists at location: " + idLocationMap.get(persistentId));
    } else {
      idLocationMap.put(persistentId, location);
    }
  }

  public void loadContextBasedTestCases(String rootPath) throws IOException {
    List<Resource> resources =
        this.getDirectories(domainPath(CONTEXTBASED_PATTERN) + "*/", rootPath);
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        String location = fileName.substring(fileName.indexOf(domainPath(CONTEXTBASED_PATTERN)),
            fileName.length());
        TestPlan testPlan = testPlan(location, TestingStage.CB, rootPath);
        if (testPlan != null) {
          checkPersistentId(testPlan.getPersistentId(), location);
          testPlanRepository.save(testPlan);
        }
      }
    }
  }

  public void loadContextFreeTestCases(String rootPath) throws IOException, ProfileParserException {
    List<Resource> resources =
        this.getDirectories(domainPath(CONTEXTFREE_PATTERN) + "*/", rootPath);
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        String location = fileName.substring(fileName.indexOf(domainPath(CONTEXTFREE_PATTERN)),
            fileName.length());
        CFTestPlan testPlan = cfTestPlan(location, rootPath);
        if (testPlan != null) {
          checkPersistentId(testPlan.getPersistentId(), fileName);
          cfTestPlanRepository.save(testPlan);
        }
      }
    }
  }

  protected TestCase testCase(String location, TestingStage stage, boolean transportSupported,
      String rootPath) throws IOException {
    logger.info("Processing test case located at:" + location);
    TestCase tc = new TestCase();
    Resource res = this.getResource(location + "TestCase.json", rootPath);
    if (res == null)
      throw new IllegalArgumentException("No TestCase.json found at " + location);
    String descriptorContent = FileUtil.getContent(res);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testCaseObj = mapper.readTree(descriptorContent);
    tc.setName(testCaseObj.findValue("name").textValue());
    if (!testCaseObj.has("id")) {
      throw new IllegalArgumentException("Missing id for Test Case at " + location);
    }
    tc.setPreloaded(true);
    tc.setScope(TestScope.GLOBAL);
    tc.setPersistentId(Long.parseLong(testCaseObj.findValue("id").asText()));
    tc.setDescription(testCaseObj.findValue("description").textValue());
    tc.setVersion(!testCaseObj.has("version") ? 1.0
        : Double.parseDouble(testCaseObj.findValue("version").asText()));

    tc.setTestStory(testStory(location, rootPath));
    tc.setJurorDocument(jurorDocument(location, rootPath));
    if (testCaseObj.findValue("position") != null) {
      tc.setPosition(testCaseObj.findValue("position").intValue());
    }
    if (testCaseObj.has("supplements")) {
      tc.getSupplements().addAll((testDocuments(location, testCaseObj.findValue("supplements"))));
    }
    List<Resource> resources = this.getDirectories(location + "*", rootPath);
    for (Resource resource : resources) {
      String fileName = fileName(resource);
      String tcLocation = fileName.substring(fileName.indexOf(location), fileName.length());
      TestStep testStep = testStep(tcLocation, stage, transportSupported, rootPath);
      checkPersistentId(testStep.getPersistentId(), fileName);
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
        TestStepFieldPair target = new TestStepFieldPair(
            findTestStep(tc.getTestSteps(), parseTestStepPosition(targetPair.getKey())),
            targetPair.getValue().asText());
        Boolean optional = false;
        JsonNode optionalNode = node.findValue("optional");
        if (optionalNode != null && optionalNode.asBoolean()) {
          optional = true;
        }
        if (source != null && target != null) {
          DataMapping dataMapping = new DataMapping(source, target, tc, optional);
          logger.info("Saving data mapping : " + dataMapping.toString());
          dataMappings.add(dataMapping);
        }
      }
      tc.setStage(stage);
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

  private TestStep findTestStep(Set<TestStep> testStepMap, int position) {
    for (TestStep testStep : testStepMap) {
      if (testStep.getPosition() == position) {
        return testStep;
      }
    }
    return null;
  }

  protected TestStep testStep(String location, TestingStage stage, boolean transportSupported,
      String rootPath) throws IOException {
    logger.info("Processing test step at:" + location);
    TestStep testStep = new TestStep();
    Resource res = this.getResource(location + "TestStep.json", rootPath);
    if (res == null)
      throw new IllegalArgumentException("No TestStep.json found at " + location);
    String descriptorContent = FileUtil.getContent(res);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testStepObj = mapper.readTree(descriptorContent);
    testStep.setName(testStepObj.findValue("name").textValue());
    if (!testStepObj.has("id")) {
      throw new IllegalArgumentException("Missing id for Test Step at " + location);
    }
    testStep.setPreloaded(true);
    testStep.setScope(TestScope.GLOBAL);
    testStep.setPersistentId(Long.parseLong(testStepObj.findValue("id").asText()));
    testStep.setDescription(testStepObj.findValue("description").textValue());
    testStep.setVersion(!testStepObj.has("version") ? 1.0
        : Double.parseDouble(testStepObj.findValue("version").asText()));
    JsonNode ttypeObj = testStepObj.findValue("type");
    String tttypeValue = ttypeObj != null ? ttypeObj.textValue() : null;
    TestingType testingType = tttypeValue != null && !"".equals(tttypeValue)
        ? TestingType.valueOf(tttypeValue) : TestingType.DATAINSTANCE;
    testStep.setTestingType(testingType);
    if (transportSupported && (TestingType.SUT_INITIATOR.equals(testingType)
        || TestingType.TA_INITIATOR.equals(testingType))) {
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
      testStep.setTestContext(testContext(location, testStepObj, stage, rootPath));
    }

    if (testStepObj.has("supplements")) {
      testStep.getSupplements()
          .addAll((testDocuments(location, testStepObj.findValue("supplements"))));
    }
    testStep.setTestStory(testStory(location, rootPath));
    testStep.setJurorDocument(jurorDocument(location, rootPath));
    testStep.setMessageContent(messageContent(location, rootPath));
    testStep.setTestDataSpecification(testDataSpecification(location, rootPath));
    if (testStepObj.findValue("position") != null) {
      testStep.setPosition(testStepObj.findValue("position").intValue());
    }
    testStep.setStage(stage);
    return testStep;
  }

  private TestArtifact testStory(String location, String rootPath) throws IOException {
    return artifact(location, "TestStory", rootPath);
  }

  private TestArtifact jurorDocument(String location, String rootPath) throws IOException {
    return artifact(location, "JurorDocument", rootPath);
  }

  private TestArtifact artifact(String location, String type, String rootPath) throws IOException {
    TestArtifact doc = null;

    String path = location + type + ".html";
    Resource resource = this.getResource(path, rootPath);
    if (resource != null) {
      doc = doc == null ? new TestArtifact(type) : doc;
      doc.setHtml(FileUtil.getContent(resource));
    }

    path = location + type + ".pdf";
    resource = this.getResource(path, rootPath);
    if (resource != null) {
      doc = doc == null ? new TestArtifact(type) : doc;
      doc.setPdfPath(path);
    }

    if (type.equals("TestStory")) { // TODO: Temporary hack
      path = location + type + ".json";
      resource = this.getResource(path, rootPath);
      if (resource != null) {
        doc = doc == null ? new TestArtifact(type) : doc;
        doc.setJson(FileUtil.getContent(resource));
      }
    }

    return doc;
  }

  private TestArtifact messageContent(String location, String rootPath) throws IOException {
    return artifact(location, "MessageContent", rootPath);
  }

  private TestArtifact testDataSpecification(String location, String rootPath) throws IOException {
    return artifact(location, "TestDataSpecification", rootPath);
  }

  private TestArtifact testPackage(String location, String rootPath) throws IOException {
    return artifact(location, "TestPackage", rootPath);
  }

  private TestArtifact testPlanSummary(String location, String rootPath) throws IOException {
    // return artifact(location, "QuickTestCaseReferenceGuide");
    return artifact(location, "TestPlanSummary", rootPath);
  }

  protected TestCaseGroup testCaseGroup(String location, TestingStage stage,
      boolean transportEnabled, String rootPath) throws IOException {
    logger.info("Processing test case group at:" + location);
    Resource descriptorRsrce = this.getResource(location + "TestCaseGroup.json", rootPath);
    if (descriptorRsrce == null)
      throw new IllegalArgumentException("No TestCaseGroup.json found at " + location);
    String descriptorContent = FileUtil.getContent(descriptorRsrce);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testPlanObj = mapper.readTree(descriptorContent);
    if (testPlanObj.findValue("skip") == null || !testPlanObj.findValue("skip").booleanValue()) {
      TestCaseGroup tcg = new TestCaseGroup();
      tcg.setName(testPlanObj.findValue("name").textValue());

      if (!testPlanObj.has("id")) {
        throw new IllegalArgumentException("Missing id for Test Case Group at " + location);
      }
      tcg.setPreloaded(true);
      tcg.setScope(TestScope.GLOBAL);
      tcg.setPersistentId(Long.parseLong(testPlanObj.findValue("id").asText()));
      tcg.setDescription(testPlanObj.findValue("description").textValue());
      tcg.setVersion(!testPlanObj.has("version") ? 1.0
          : Double.parseDouble(testPlanObj.findValue("version").asText()));
      tcg.setTestStory(testStory(location, rootPath));
      if (testPlanObj.findValue("position") != null) {
        tcg.setPosition(testPlanObj.findValue("position").intValue());
      } else {
        tcg.setPosition(1);
      }
      if (testPlanObj.has("supplements")) {
        tcg.getSupplements().addAll(testDocuments(location, testPlanObj.findValue("supplements")));
      }
      List<Resource> resources = this.getDirectories(location + "*/", rootPath);
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        String tcLocation = fileName.substring(fileName.indexOf(location), fileName.length());
        Resource descriptorResource = getDescriptorResource(tcLocation, rootPath);
        if (descriptorResource != null) {
          String filename = descriptorResource.getFilename();
          if (filename.endsWith("TestCaseGroup.json")) {
            TestCaseGroup testCaseGroup =
                testCaseGroup(tcLocation, stage, transportEnabled, rootPath);
            checkPersistentId(testCaseGroup.getPersistentId(), location);
            tcg.getTestCaseGroups().add(testCaseGroup);
          } else if (filename.endsWith("TestCase.json")) {
            TestCase testCase = testCase(tcLocation, stage, transportEnabled, rootPath);
            checkPersistentId(testCase.getPersistentId(), location);
            tcg.getTestCases().add(testCase);
          }
        }
      }
      return tcg;
    }
    return null;
  }


  protected CFTestStepGroup cfTestStepGroup(String location, String rootPath) throws IOException {
    logger.info("Processing test case group at:" + location);
    Resource descriptorRsrce = this.getResource(location + "TestStepGroup.json", rootPath);
    if (descriptorRsrce == null)
      throw new IllegalArgumentException("No TestStepGroup.json found at " + location);
    String descriptorContent = FileUtil.getContent(descriptorRsrce);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testPlanObj = mapper.readTree(descriptorContent);
    if (testPlanObj.findValue("skip") == null || !testPlanObj.findValue("skip").booleanValue()) {
      CFTestStepGroup tcg = new CFTestStepGroup();
      tcg.setName(testPlanObj.findValue("name").textValue());

      if (!testPlanObj.has("id")) {
        throw new IllegalArgumentException("Missing id for Test Step Group at " + location);
      }
      tcg.setPreloaded(true);
      tcg.setScope(TestScope.GLOBAL);
      tcg.setPersistentId(Long.parseLong(testPlanObj.findValue("id").asText()));
      tcg.setDescription(testPlanObj.findValue("description").textValue());
      tcg.setVersion(!testPlanObj.has("version") ? 1.0
          : Double.parseDouble(testPlanObj.findValue("version").asText()));
      tcg.setTestStory(testStory(location, rootPath));
      if (testPlanObj.findValue("position") != null) {
        tcg.setPosition(testPlanObj.findValue("position").intValue());
      } else {
        tcg.setPosition(1);
      }
      if (testPlanObj.has("supplements")) {
        tcg.getSupplements().addAll(testDocuments(location, testPlanObj.findValue("supplements")));
      }
      List<Resource> resources = this.getDirectories(location + "*/", rootPath);
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        String tcLocation = fileName.substring(fileName.indexOf(location), fileName.length());
        Resource descriptorResource = getDescriptorResource(tcLocation, rootPath);
        if (descriptorResource != null) {
          String filename = descriptorResource.getFilename();
          if (filename.endsWith("TestStepGroup.json")) {
            CFTestStepGroup testStepGroup = cfTestStepGroup(tcLocation, rootPath);
            if (testStepGroup != null) {
              checkPersistentId(testStepGroup.getPersistentId(), filename);
              tcg.getTestStepGroups().add(testStepGroup);
            }
          } else if (filename.endsWith("TestObject.json")) {
            CFTestStep testStep = cfTestStep(tcLocation, rootPath);
            if (testStep != null) {
              checkPersistentId(testStep.getPersistentId(), filename);
              tcg.getTestSteps().add(testStep);
            }
          }
        }
      }
      return tcg;
    }
    return null;
  }


  protected TestPlan testPlan(String location, TestingStage stage, String rootPath)
      throws IOException {
    logger.info("Processing test plan  at:" + location);

    Resource res = this.getResource(location + "TestPlan.json", rootPath);
    if (res == null)
      throw new IllegalArgumentException("No TestPlan.json found at " + location);
    String descriptorContent = FileUtil.getContent(res);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testPlanObj = mapper.readTree(descriptorContent);
    if (testPlanObj.findValue("skip") == null || !testPlanObj.findValue("skip").booleanValue()) {
      TestPlan tp = new TestPlan();
      if (!testPlanObj.has("id")) {
        throw new IllegalArgumentException("Missing id for Test Plan at " + location);
      }
      tp.setPreloaded(true);
      tp.setScope(TestScope.GLOBAL);
      tp.setPersistentId(Long.parseLong(testPlanObj.findValue("id").asText()));
      tp.setName(testPlanObj.findValue("name").textValue());
      tp.setDescription(testPlanObj.findValue("description").textValue());
      tp.setCategory(testPlanObj.findValue("category") != null
          ? testPlanObj.findValue("category").textValue() : DEFAULT_CATEGORY);
      tp.setVersion(!testPlanObj.has("version") ? 1.0
          : Double.parseDouble(testPlanObj.findValue("version").asText()));
      tp.setTestStory(testStory(location, rootPath));
      tp.setStage(stage);

      if (testPlanObj.findValue("transport") != null
          && testPlanObj.findValue("transport").booleanValue()
          && (testPlanObj.findValue("domain") == null
              || testPlanObj.findValue("domain").textValue() == null
              || testPlanObj.findValue("domain").textValue() == "")) {
        throw new IllegalArgumentException(
            "Transport is supported for the following test plan but no domain is defined. Test Plan location="
                + location);
      }
      tp.setDomain(testPlanObj.findValue("domain") != null
          ? testPlanObj.findValue("domain").textValue() : null);
      tp.setTransport(testPlanObj.findValue("transport") != null
          ? testPlanObj.findValue("transport").booleanValue() : false);
      if (testPlanObj.findValue("position") != null) {
        tp.setPosition(testPlanObj.findValue("position").intValue());
      } else {
        tp.setPosition(1);
      }
      tp.setTestPackage(testPackage(location, rootPath));
      tp.setTestPlanSummary(testPlanSummary(location, rootPath));
      if (testPlanObj.has("supplements")) {
        tp.getSupplements().addAll((testDocuments(location, testPlanObj.findValue("supplements"))));
      }

      List<Resource> resources = this.getDirectories(location + "*/", rootPath);
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        String loca = fileName.substring(fileName.indexOf(location), fileName.length());
        Resource descriptorResource = getDescriptorResource(loca, rootPath);
        if (descriptorResource != null) {
          String filename = descriptorResource.getFilename();
          if (filename.endsWith("TestCaseGroup.json")) {
            TestCaseGroup testCaseGroup = testCaseGroup(loca, stage, tp.isTransport(), rootPath);
            checkPersistentId(testCaseGroup.getPersistentId(), fileName);
            tp.getTestCaseGroups().add(testCaseGroup);
          } else if (filename.endsWith("TestCase.json")) {
            TestCase testCase = testCase(loca, stage, tp.isTransport(), rootPath);
            checkPersistentId(testCase.getPersistentId(), fileName);
            tp.getTestCases().add(testCase);
          }
        }
      }
      return tp;
    }
    return null;
  }


  protected CFTestPlan cfTestPlan(String testPlanPath, String rootPath) throws IOException {
    logger.info("Processing test plan at:" + testPlanPath);
    Resource res = this.getResource(testPlanPath + "TestPlan.json", rootPath);
    if (res == null)
      throw new IllegalArgumentException("No TestPlan.json found at " + testPlanPath);
    String descriptorContent = FileUtil.getContent(res);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testPlanObj = mapper.readTree(descriptorContent);
    if (testPlanObj.findValue("skip") == null || !testPlanObj.findValue("skip").booleanValue()) {
      CFTestPlan testPlan = new CFTestPlan();
      testPlan.setName(testPlanObj.findValue("name").textValue());
      if (!testPlanObj.has("id")) {
        throw new IllegalArgumentException("Missing id for Test Object at " + testPlanPath);
      }
      testPlan.setPreloaded(true);
      testPlan.setScope(TestScope.GLOBAL);
      testPlan.setCategory(testPlanObj.findValue("category") != null
          ? testPlanObj.findValue("category").textValue() : DEFAULT_CATEGORY);
      testPlan.setPersistentId(Long.parseLong(testPlanObj.findValue("id").asText()));
      if (testPlanObj.findValue("position") != null) {
        testPlan.setPosition(testPlanObj.findValue("position").intValue());
      }
      testPlan.setPersistentId(Long.parseLong(testPlanObj.findValue("id").asText()));
      testPlan.setDescription(testPlanObj.findValue("description").textValue());
      testPlan.setVersion(!testPlanObj.has("version") ? 1.0
          : Double.parseDouble(testPlanObj.findValue("version").asText()));
      if (testPlanObj.has("supplements")) {
        testPlan.getSupplements()
            .addAll(testDocuments(testPlanPath, testPlanObj.findValue("supplements")));
      }


      List<Resource> resources = this.getDirectories(testPlanPath + "*/", rootPath);
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        String tcLocation = fileName.substring(fileName.indexOf(testPlanPath), fileName.length());
        Resource descriptorResource = getDescriptorResource(tcLocation, rootPath);
        if (descriptorResource != null) {
          String filename = descriptorResource.getFilename();
          if (filename.endsWith("TestStepGroup.json")) {
            CFTestStepGroup testStepeGroup = cfTestStepGroup(tcLocation, rootPath);
            if (testStepeGroup != null) {
              checkPersistentId(testStepeGroup.getPersistentId(), filename);
              testPlan.getTestStepGroups().add(testStepeGroup);
            }
          } else if (filename.endsWith("TestObject.json")) {
            CFTestStep testStep = cfTestStep(tcLocation, rootPath);
            if (testStep != null) {
              checkPersistentId(testStep.getPersistentId(), filename);
              testPlan.getTestSteps().add(testStep);
            }
          }
        }
      }
      return testPlan;
    }
    return null;
  }



  protected CFTestStep cfTestStep(String testObjectPath, String rootPath) throws IOException {
    logger.info("Processing test object at:" + testObjectPath);
    Resource res = this.getResource(testObjectPath + "TestObject.json", rootPath);
    if (res == null)
      throw new IllegalArgumentException("No TestObject.json found at " + testObjectPath);
    String descriptorContent = FileUtil.getContent(res);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testPlanObj = mapper.readTree(descriptorContent);
    if (testPlanObj.findValue("skip") == null || !testPlanObj.findValue("skip").booleanValue()) {
      CFTestStep testStep = new CFTestStep();
      testStep.setName(testPlanObj.findValue("name").textValue());
      if (!testPlanObj.has("id")) {
        throw new IllegalArgumentException("Missing id for Test Object at " + testObjectPath);
      }
      testStep.setPreloaded(true);
      testStep.setScope(TestScope.GLOBAL);
      testStep.setPersistentId(Long.parseLong(testPlanObj.findValue("id").asText()));
      if (testPlanObj.findValue("position") != null) {
        testStep.setPosition(testPlanObj.findValue("position").intValue());
      }
      testStep.setDescription(testPlanObj.findValue("description").textValue());
      testStep.setVersion(!testPlanObj.has("version") ? 1.0
          : Double.parseDouble(testPlanObj.findValue("version").asText()));
      testStep.setTestContext(testContext(testObjectPath, testPlanObj, TestingStage.CF, rootPath));
      if (testPlanObj.has("supplements")) {
        testStep.getSupplements()
            .addAll(testDocuments(testObjectPath, testPlanObj.findValue("supplements")));
      }
      return testStep;
    }
    return null;
  }

  public void loadTestCasesDocumentation() throws IOException {
    TestCaseDocumentation doc = generateCFTestPlanDocumentation("Context-free", TestingStage.CF,
        cfTestPlanRepository.findAllByStageAndScope(TestingStage.CF, TestScope.GLOBAL));
    if (doc != null) {
      doc.setJson(obm.writeValueAsString(doc));
      testCaseDocumentationRepository.save(doc);
    }
    doc = generateCBTestPlansDocumentation("Context-based", TestingStage.CB,
        testPlanRepository.findAllByStageAndScope(TestingStage.CB, TestScope.GLOBAL));
    if (doc != null) {
      doc.setJson(obm.writeValueAsString(doc));
      testCaseDocumentationRepository.save(doc);
    }
  }

  private TestCaseDocumentation generateCBTestPlansDocumentation(String title, TestingStage stage,
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

  private TestCaseDocumentation generateCFTestPlanDocumentation(String title, TestingStage stage,
      List<CFTestPlan> tos) throws IOException {
    if (tos != null && !tos.isEmpty()) {
      TestCaseDocumentation documentation = new TestCaseDocumentation();
      documentation.setTitle(title);
      documentation.setStage(stage);
      Collections.sort(tos);
      for (CFTestPlan to : tos) {
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
      List<TestCaseGroup> list = new ArrayList<TestCaseGroup>(tp.getTestCaseGroups());
      Collections.sort(list);
      for (TestCaseGroup tcg : list) {
        doc.getChildren().add(generateTestCaseDocument(tcg));
      }
    }
    if (tp.getTestCases() != null && !tp.getTestCases().isEmpty()) {
      List<TestCase> list = new ArrayList<TestCase>(tp.getTestCases());
      Collections.sort(list);
      for (TestCase tc : list) {
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
      List<TestCaseGroup> list = new ArrayList<TestCaseGroup>(tcg.getTestCaseGroups());
      Collections.sort(list);
      for (TestCaseGroup child : list) {
        doc.getChildren().add(generateTestCaseDocument(child));
      }
    }

    if (tcg.getTestCases() != null && !tcg.getTestCases().isEmpty()) {
      List<TestCase> list = new ArrayList<TestCase>(tcg.getTestCases());
      Collections.sort(list);
      for (TestCase tc : list) {
        doc.getChildren().add(generateTestCaseDocument(tc));
      }
    }

    return doc;
  }


  private gov.nist.hit.core.domain.TestCaseDocument generateTestCaseDocument(CFTestStepGroup tcg)
      throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = initTestCaseDocument(tcg);
    doc.setId(tcg.getId());
    if (tcg.getTestStepGroups() != null && !tcg.getTestStepGroups().isEmpty()) {
      List<CFTestStepGroup> list = new ArrayList<CFTestStepGroup>(tcg.getTestStepGroups());
      Collections.sort(list);
      for (CFTestStepGroup child : list) {
        doc.getChildren().add(generateTestCaseDocument(child));
      }
    }
    if (tcg.getTestSteps() != null && !tcg.getTestSteps().isEmpty()) {
      List<CFTestStep> list = new ArrayList<CFTestStep>(tcg.getTestSteps());
      Collections.sort(list);
      for (CFTestStep tc : list) {
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
      List<TestStep> list = new ArrayList<TestStep>(tc.getTestSteps());
      Collections.sort(list);
      for (TestStep ts : list) {
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

  private gov.nist.hit.core.domain.TestCaseDocument generateTestCaseDocument(CFTestStep to)
      throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = generateTestCaseDocument(to.getTestContext());
    doc = initTestCaseDocument(to, doc);
    doc.setId(to.getId());
    return doc;
  }

  private gov.nist.hit.core.domain.TestCaseDocument generateTestCaseDocument(CFTestPlan tp)
      throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = initTestCaseDocument(tp);
    doc.setId(tp.getId());
    if (tp.getTestStepGroups() != null && !tp.getTestStepGroups().isEmpty()) {
      List<CFTestStepGroup> list = new ArrayList<CFTestStepGroup>(tp.getTestStepGroups());
      Collections.sort(list);
      for (CFTestStepGroup tcg : list) {
        doc.getChildren().add(generateTestCaseDocument(tcg));
      }
    }
    if (tp.getTestSteps() != null && !tp.getTestSteps().isEmpty()) {
      List<CFTestStep> list = new ArrayList<CFTestStep>(tp.getTestSteps());
      Collections.sort(list);
      for (CFTestStep tc : list) {
        doc.getChildren().add(generateTestCaseDocument(tc));
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

  protected Resource getDescriptorResource(String location, String rootPath) throws IOException {
    Resource resource = this.getResource(location + "TestPlan.json", rootPath);
    resource =
        resource == null ? this.getResource(location + "TestCaseGroup.json", rootPath) : resource;
    resource =
        resource == null ? this.getResource(location + "TestStepGroup.json", rootPath) : resource;
    resource = resource == null ? this.getResource(location + "TestCase.json", rootPath) : resource;
    resource = resource == null ? this.getResource(location + "TestStep.json", rootPath) : resource;
    resource =
        resource == null ? this.getResource(location + "TestObject.json", rootPath) : resource;
    resource = resource == null ? null : resource;
    return resource;
  }

  protected String fileName(Resource resource) throws IOException {
    String location = resource.getURL().toString();
    return location.replaceAll("%20", " ");
  }


  private Set<gov.nist.hit.core.domain.Document> testDocuments(String testPath, JsonNode nodeObj) {
    Set<gov.nist.hit.core.domain.Document> documents =
        new HashSet<gov.nist.hit.core.domain.Document>();
    Iterator<JsonNode> it = nodeObj.elements();
    if (it != null) {
      while (it.hasNext()) {
        JsonNode node = it.next();
        gov.nist.hit.core.domain.Document document = new gov.nist.hit.core.domain.Document();
        if (node.findValue("title") == null
            || (node.findValue("link") == null && node.findValue("name") == null)
            || node.findValue("date") == null) {
          throw new IllegalArgumentException(
              "The 'documents' field is missing one of those: title, link, name, date");
        }
        document
            .setTitle(node.findValue("title") != null ? node.findValue("title").textValue() : null);
        document
            .setName(node.findValue("name") != null ? node.findValue("name").textValue() : null);
        if (node.findValue("link") == null) {
          document.setPath(node.findValue("name") != null
              ? testPath + node.findValue("name").textValue() : null);
        } else {
          document.setPath(node.findValue("link").textValue());
        }
        document
            .setDate(node.findValue("date") != null ? node.findValue("date").textValue() : null);
        document.setType(DocumentType.TESTDOCUMENT);
        document.setComments(
            node.findValue("comments") != null ? node.findValue("comments").textValue() : null);
        documents.add(document);
      }
    }
    return documents;
  }



  /**
   * @throws JsonProcessingException
   * @throws IOException Loads values dynamically at each app startup (not only at database
   *         creation)
   */
  public void loadDynamicValues() throws JsonProcessingException, IOException {
    logger.info("loading app info...");
    AppInfo appInfo = appInfoService.get();

    if (adminEmails == null) {
      throw new RuntimeException("Administrator emails address are missing");
    }
    appInfo.setAdminEmails(adminEmails);
    appInfo.setCbManagementSupported(cbManagementSupported);
    appInfo.setCfManagementSupported(cfManagementSupported);
    appInfo.setAuthenticationRequired(authenticationRequired);
    appInfo.setEmployerRequired(employerRequired);

    appInfo.setOrganization(organizationName);
    appInfo.setOrganizationName(organizationName);
    appInfo.setOrganizationLogo(appOrganizationLogo);
    appInfo.setOrganizationLink(appOrganizationLink);

    if(appResourceBundleVersion.equals("null")){
      appResourceBundleVersion = getRsbleVersion();
    }
    appInfo.setRsbVersion(appResourceBundleVersion);

    appInfo.setDomain(appDomain);
    appInfo.setHeader(appHeader);
    appInfo.setHomeTitle(appHomeTitle);
    appInfo.setHomeContent(appHomeContent); // compatibility
    appInfo.setName(appName);
    appInfo.setVersion(appVersion);
    appInfo.setDate(new Date().getTime() + "");
    appInfo.setContactEmail(appContactEmail);

    appInfo.setDisclaimer(appDisclaimerContent);
    appInfo.setDisclaimerLink(appDisclaimerLink);

    appInfo.setHomeContent(appHomeContent);

    appInfo.setMessageContentInfo(appMessageContentInfoContent);
    appInfo.setValidationResultInfo(appValidationResultInfoContent);
    appInfo.setAcknowledgment(appAcknowledgment);
    appInfo.setProfileInfo(appProfileInfoContent);
    appInfo.setValueSetCopyright(appValueSetCopyRightContent);
    appInfo.setConfidentiality(appConfidentialityContent);
    appInfo.setConfidentialityLink(appConfidentialityLink);
    appInfo.setPrivacy(appPrivacyContent);
    appInfo.setPrivacyLink(appPrivacyLink);
    appInfo.setRegistrationTitle(appRegistrationTitle);
    appInfo.setRegistrationAgreement(appRegistrationAgreement);
    appInfo.setRegistrationSubmittedContent(appRegistrationSubmittedContent);
    appInfo.setRegistrationSubmittedTitle(appRegistrationSubmittedTitle);
    appInfo.setRegistrationAcceptanceTitle(appRegistrationAcceptanceTitle);
    appInfo.setApiDocsPath("/apidocs/swagger-ui.html");

    appInfo.setDivisionLogo(appDivisionLogo);
    appInfo.setDivisionLink(appDivisionLink);
    appInfo.setDivisionName(appDivisionName);

    appInfo.setUploadContentTypes(appUploadContentTypes);
    appInfo.setUploadMaxSize(appUploadMaxSize);
    appInfo.setDownloadWarDisabled(appDownloadWarDisabled);


    appInfoRepository.save(appInfo);
    logger.info("loading app info...DONE");
  }


  public static String getRsbleVersion() throws JsonProcessingException, IOException {
    Resource resource =
        ResourcebundleHelper.getResource(ResourcebundleLoader.ABOUT_PATTERN + "MetaData.json");
    if (resource == null)
      throw new RuntimeException("No MetaData.json found in the resource bundle");
    ObjectMapper mapper = new ObjectMapper();
    JsonNode metaData = mapper.readTree(FileUtil.getContent(resource));
    if (metaData != null) {
      if (metaData.get("rsbVersion") == null || "".equals(metaData.get("rsbVersion").textValue()))
        throw new RuntimeException("rsbVersion not set or found in MetaData.json");
      return metaData.get("rsbVersion").textValue();
    }
    return null;
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

  public void setVocabularyLibraryRepository(
      VocabularyLibraryRepository vocabularyLibraryRepository) {
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

  public List<Resource> getDirectories(String pattern, String rootPath) throws IOException {
    if (rootPath.isEmpty())
      return ResourcebundleHelper.getDirectories(pattern);
    else
      return ResourcebundleHelper.getDirectoriesFile(rootPath + pattern);
  }

  public Resource getResource(String pattern, String rootPath) throws IOException {
    if (rootPath.isEmpty())
      return ResourcebundleHelper.getResource(pattern);
    else
      return ResourcebundleHelper.getResourceFile(rootPath + pattern);
  }

  public List<Resource> getResources(String pattern, String rootPath) throws IOException {
    if (rootPath.isEmpty())
      return ResourcebundleHelper.getResources(rootPath + pattern);
    else
      return ResourcebundleHelper.getResourcesFile(rootPath + pattern);
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

  public CFTestStepRepository getCfTestStepRepository() {
    return cfTestStepRepository;
  }

  public void setCfTestStepRepository(CFTestStepRepository cfTestStepRepository) {
    this.cfTestStepRepository = cfTestStepRepository;
  }

  public CFTestPlanRepository getCfTestPlanRepository() {
    return cfTestPlanRepository;
  }

  public void setCfTestPlanRepository(CFTestPlanRepository cfTestPlanRepository) {
    this.cfTestPlanRepository = cfTestPlanRepository;
  }

  public DataMappingRepository getDataMappingRepository() {
    return dataMappingRepository;
  }

  public void setDataMappingRepository(DataMappingRepository dataMappingRepository) {
    this.dataMappingRepository = dataMappingRepository;
  }

  public static String getRsbVersion() throws IOException {
    String rsbVersion = null;
    Resource resource = new ClassPathResource("/app-config.properties");
    Properties props = PropertiesLoaderUtils.loadProperties(resource);
    if(props != null) {
      rsbVersion = props.getProperty("app.resourceBundleVersion");
    }
    if(rsbVersion == null) {
      return getRsbleVersion();
    } else {
      return rsbVersion;
    }
  }



}
