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
import java.io.StringWriter;
import java.io.Writer;
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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
import org.w3c.dom.Node;
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
import gov.nist.hit.core.domain.AppInfo;
import gov.nist.hit.core.domain.CFTestPlan;
import gov.nist.hit.core.domain.CFTestStep;
import gov.nist.hit.core.domain.CFTestStepGroup;
import gov.nist.hit.core.domain.Constraints;
import gov.nist.hit.core.domain.DataMapping;
import gov.nist.hit.core.domain.DocumentType;
import gov.nist.hit.core.domain.Domain;
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
import gov.nist.hit.core.repo.CFTestStepGroupRepository;
import gov.nist.hit.core.repo.CFTestStepRepository;
import gov.nist.hit.core.repo.ConformanceProfileRepository;
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
  final static public String VALUESETS_FILE_PATTERN = "ValueSets.xml";
  final static public String PROFILE_FILE_PATTERN = "Profile.xml";
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
  public static final String TOOL_DOCUMENT_DOMAIN = "app";

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
  protected CFTestStepGroupRepository cfTestSteGroupRepository;

  @Autowired
  protected CFTestPlanRepository cfTestPlanRepository;

  @Autowired
  protected TestStepRepository testStepRepository;

  @Autowired
  protected TestCaseDocumentationRepository testCaseDocumentationRepository;

  // @Autowired
  // protected CachedRepository cachedRepository;

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
  protected DomainService domainService;

  ConformanceProfileRepository conformanceProfileRepository;

  @Autowired
  protected VocabularyLibraryRepository vocabularyLibraryRepository;

  private Map<Long, String> idLocationMap;

  @Value("${app.ownerUsername}")
  private String ownerUsername;

  @Value("${admin.emails}")
  private List<String> adminEmails;

  @Value("${cf.management.supported:#{false}}")
  private boolean cfManagementSupported;

  @Value("${cb.management.supported:#{false}}")
  private boolean cbManagementSupported;

  @Value("${doc.management.supported:#{false}}")
  private boolean docManagementSupported;

  @Value("${authentication.required:#{false}}")
  private boolean authenticationRequired;

  @Value("${employer.required:#{false}}")
  private boolean employerRequired;

  @Value("${app.organization.logo:#{null}}")
  private String appOrganizationLogo;

  @Value("${app.organization.name:'NIST'}")
  private String organizationName;

  @Value("${app.organization.link:#{null}}")
  private String appOrganizationLink;

  @Value("${app.division.link:#{null}}")
  private String appDivisionLink;

  @Value("${app.division.logo:#{null}}")
  private String appDivisionLogo;

  @Value("${app.division.name:#{null}}")
  private String appDivisionName;

  @Value("${app.name}")
  private String appName;

  @Value("${app.version}")
  private String appVersion;

  @Value("${app.domain}")
  private String appSubTitle;

  @Value("${app.header}")
  private String appHeader;

  @Value("${app.contactEmail}")
  private String appContactEmail;

  @Value("${app.upload.contentTypes:'application/xml,text/xml,text/plain'}")
  private String appUploadContentTypes;

  @Value("${app.upload.maxSize:'100MB'}")
  private String appUploadMaxSize;

  @Value("${app.acknowledgment.content:#{null}}")
  private String appAcknowledgment;

  @Value("${app.privacy.content:#{null}}")
  private String appPrivacyContent;

  @Value("${app.privacy.link:#{null}}")
  private String appPrivacyLink;

  @Value("${app.disclaimer.content:#{null}}")
  private String appDisclaimerContent;

  @Value("${app.disclaimer.link:#{null}}")
  private String appDisclaimerLink;

  @Value("${app.confidentiality.content:#{null}}")
  private String appConfidentialityContent;

  @Value("${app.confidentiality.link:#{null}}")
  private String appConfidentialityLink;

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

  @Value("${download.war.disabled:#{false}}")
  private boolean appDownloadWarDisabled;

  @Value("${domain.management.supported:#{true}}")
  private boolean domainManagementSupported;

  // conformance profile source Id - integration profile id
  protected HashMap<String, String> profilesMap = new HashMap<String, String>();

  public ResourcebundleLoader() {
    idLocationMap = new HashMap<>();
    obm = new com.fasterxml.jackson.databind.ObjectMapper();
    obm.setSerializationInclusion(Include.NON_NULL);
  }

  public boolean reloadDb() throws IOException {
    AppInfo appInfo = appInfoService.get();
    String create = System.getProperty("RELOAD_DB");
    create = create == null ? System.getenv("RELOAD_DB") : create;
    return (create != null && (Boolean.valueOf(create) == true)) || appInfo == null;
  }

  public void clearDB() {
    appInfoRepository.deleteAll();
    domainService.deletePreloaded();
    validationResultRepository.deleteAll();
    testPlanRepository.deletePreloaded();
    cfTestPlanRepository.deletePreloaded();
    vocabularyLibraryRepository.deletePreloaded();
    constraintsRepository.deletePreloaded();
    integrationProfileRepository.deletePreloaded();
    testCaseDocumentationRepository.deleteAll();
    transportFormsRepository.deleteAll();
    documentRepository.deleteAll();
    transportMessageRepository.deleteAll();
    transactionRepository.deleteAll();
  }

  public void init() throws Exception {
    load("");
  }

  public void load(String directory) throws Exception {
    if (reloadDb()) {
      logger.info("clearing tables...");
      clearDB();
      this.idLocationMap = new HashMap<>();
      this.loadAppInfo();
      this.loadDomains(directory);
      logger.info("resource bundle loaded successfully...");
    }
    GCUtil.performGC();
  }

  public void load() throws Exception {
    load("");
  }

  public abstract TestContext testContext(String location, JsonNode parentOb, TestingStage stage,
      String rootPath, String domain, TestScope scope, String authorUsername, boolean preloaded)
      throws Exception;

  public abstract TestCaseDocument generateTestCaseDocument(TestContext c) throws IOException;

  public abstract ProfileModel parseProfile(String integrationProfileXml,
      String conformanceProfileId, String constraintsXml, String additionalConstraintsXml)
      throws ProfileParserException, UnsupportedOperationException;

  public abstract VocabularyLibrary vocabLibrary(String content, String domain, TestScope scope,
      String authorUsername, boolean preloaded) throws JsonGenerationException,
      JsonMappingException, IOException, UnsupportedOperationException;

  public void loadDomains(String rootPath) throws Exception {
    logger.info("loading domains...");
    Resource metadataResource =
        ResourcebundleHelper.getResource(ResourcebundleLoader.ABOUT_PATTERN + "MetaData.json");
    if (metadataResource == null)
      throw new RuntimeException("No MetaData.json found in the resource bundle");
    ObjectMapper mapper = new ObjectMapper();
    JsonNode metaData = mapper.readTree(FileUtil.getContent(metadataResource));
    if (metaData.get("domains") == null) {
      throw new RuntimeException("domains not found in MetaData.json");
    }
    Iterator<JsonNode> it = metaData.get("domains").iterator();
    while (it.hasNext()) {
      JsonNode node = it.next();
      Domain domain = getDomain(node, rootPath);
      domainService.save(domain);
      loadDomainsArtifacts(rootPath, domain.getScope(), domain.isPreloaded(), domain.getDomain());
    }
    loadDomainsArtifacts(rootPath, TestScope.GLOBAL, true, TOOL_DOCUMENT_DOMAIN);

    logger.info("loading app info...DONE");
  }

  private void loadDomainsArtifacts(String directory, TestScope scope, boolean preloaded,
      String domain) throws Exception {
    this.loadConstraints(directory, scope, preloaded, domain);
    this.loadVocabularyLibraries(directory, scope, preloaded, domain);
    this.loadIntegrationProfiles(directory, scope, preloaded, domain);
    this.loadContextFreeTestCases(directory, scope, preloaded, domain);
    this.loadContextBasedTestCases(directory, scope, preloaded, domain);
    this.loadUserDocs(directory, scope, preloaded, domain);
    this.loadKownIssues(directory, scope, preloaded, domain);
    this.loadReleaseNotes(directory, scope, preloaded, domain);
    this.loadProfilesDocs(directory, scope, preloaded, domain);
    this.loadConstraintsDocs(directory, scope, preloaded, domain);
    this.loadValuesetsDocs(directory, scope, preloaded, domain);
    this.loadToolDownloads(directory, scope, preloaded, domain);
    this.loadTransports(directory, scope, preloaded, domain);
  }

  public Domain getDomain(JsonNode node, String rootPath) throws IOException {
    boolean disabled = node.get("disabled") != null && (node.get("disabled").asBoolean() == true);
    Domain entry = new Domain();
    String key = node.get("key").textValue();
    entry.setDomain(key);
    entry.setScope(node.get("scope") != null ? TestScope.valueOf(node.get("scope").textValue())
        : TestScope.GLOBAL);
    entry.setName(node.get("name").textValue());
    entry.setHomeTitle(node.get("homeTitle").textValue());
    entry.setAuthorUsername(node.get("authorUsername").textValue());
    entry.setDisabled(disabled);
    if (node.get("rsbVersion") != null) {
      entry.setRsbVersion(node.get("rsbVersion").asText());
    }
    if (node.get("igVersion") != null) {
      entry.setRsbVersion(node.get("igVersion").asText());
    }
    if (node.get("participantEmails") != null && node.get("participantEmails").isArray()) {
      Iterator<JsonNode> ownerEmailNodes = node.get("participantEmails").iterator();
      while (ownerEmailNodes.hasNext()) {
        JsonNode ownerNode = ownerEmailNodes.next();
        entry.getParticipantEmails().add(ownerNode.textValue());
      }
    }

    String domainPath = getDomainBasedPath(ResourcebundleLoader.ABOUT_PATTERN, key);
    Resource resource = this.getResource(domainPath + PROFILE_INFO_PATTERN, rootPath);
    if (resource != null) {
      entry.setProfileInfo(FileUtil.getContent(resource));
    }
    resource =
        this.getResource(domainPath + ResourcebundleLoader.VALUE_SET_COPYRIGHT_PATTERN, rootPath);
    if (resource != null) {
      entry.setValueSetCopyright(FileUtil.getContent(resource));
    }
    resource =
        this.getResource(domainPath + ResourcebundleLoader.VALIDATIONRESULT_INFO_PATTERN, rootPath);
    if (resource != null) {
      entry.setValidationResultInfo(FileUtil.getContent(resource));
    }
    resource = this.getResource(domainPath + ResourcebundleLoader.HOME_PATTERN, rootPath);
    if (resource == null) {
      throw new IllegalArgumentException(
          "No " + ResourcebundleLoader.HOME_PATTERN + " found at " + domainPath);
    }

    entry.setHomeContent(FileUtil.getContent(resource));
    resource =
        this.getResource(domainPath + ResourcebundleLoader.MESSAGECONTENT_INFO_PATTERN, rootPath);
    if (resource != null) {
      entry.setMessageContentInfo(FileUtil.getContent(resource));
    }

    return entry;

  }

  public void loadUserDocs(String rootPath, TestScope scope, boolean preloaded, String domain)
      throws IOException {
    logger.info("loading user documents of domain=" + domain);
    Resource resource =
        getResource(getDomainBasedPath(USERDOCS_PATTERN, domain) + USERDOCS_FILE_PATTERN, rootPath);
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
            gov.nist.hit.core.domain.Document document =
                new gov.nist.hit.core.domain.Document(domain);
            document.setPosition(node.findValue("order") != null
                ? node.findValue("order").intValue() : userDocs.size() + 1);
            document.setTitle(
                node.findValue("title") != null ? node.findValue("title").textValue() : null);
            if (node.findValue("name") != null) {
              String path = node.findValue("name").textValue();
              if (path.endsWith("*")) {
                Resource rs = getLatestResource(getDomainBasedPath(USERDOCS_PATTERN, domain)
                    + node.findValue("name").textValue(), rootPath);
                path = rs.getFilename();
              }
              document.setName(path);
              document.setPath(getDomainBasedPath(USERDOCS_PATTERN, domain) + path);
            } else if (node.findValue("link") != null) {
              document.setPath(node.findValue("link").textValue());
            }
            document.setDate(
                node.findValue("date") != null ? node.findValue("date").textValue() : null);
            document.setType(DocumentType.USERDOC);
            document.setComments(
                node.findValue("comments") != null ? node.findValue("comments").textValue() : null);
            document.setScope(scope);
            document.setAuthorUsername(getDomainAuthorname(domain));
            document.setPreloaded(preloaded);

            userDocs.add(document);
          }
          if (!userDocs.isEmpty()) {
            documentRepository.save(userDocs);
          }
        }
      }
    }
  }

  public void loadTransports(String rootPath, TestScope scope, boolean preloaded, String domain)
      throws IOException {
    logger.info("loading transport for domain=" + domain);
    Resource resource = getResource(
        getDomainBasedPath(TRANSPORT_PATTERN, domain) + TRANSPORT_CONF_PATTERN, rootPath);
    if (resource != null) {
      logger.info("loading protocols info...");
      String descriptorContent = FileUtil.getContent(resource);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode json = mapper.readTree(descriptorContent);
      if (json != null) {
        List<TransportForms> transportForms = new ArrayList<TransportForms>();
        if (json.isArray()) {
          Iterator<JsonNode> it = json.elements();
          while (it.hasNext()) {
            JsonNode node = it.next();
            if (node.findValue("protocol") != null && node.findValue("forms") != null) {
              TransportForms tForms = new TransportForms();
              tForms.setProtocol(node.findValue("protocol").textValue());
              tForms.setDescription(node.findValue("description") != null
                  ? node.findValue("description").textValue() : null);
              JsonNode forms = node.findValue("forms");
              if (forms.get("TA_INITIATOR") == null || forms.get("SUT_INITIATOR") == null) {
                throw new RuntimeException(
                    "Transport.json TA_INITIATOR or SUT_INITIATOR form is missing");
              }
              tForms.setTaInitiatorForm(
                  FileUtil.getContent(getResource(getDomainBasedPath(TRANSPORT_PATTERN, domain)
                      + forms.get("TA_INITIATOR").textValue(), rootPath)));
              tForms.setSutInitiatorForm(
                  FileUtil.getContent(getResource(getDomainBasedPath(TRANSPORT_PATTERN, domain)
                      + forms.get("SUT_INITIATOR").textValue(), rootPath)));
              tForms.setScope(scope);
              tForms.setAuthorUsername(getDomainAuthorname(domain));
              tForms.setDomain(domain);
              transportForms.add(tForms);
            } else {
              throw new RuntimeException(
                  "Properties protocol or forms not found in Transport.json");
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
  }

  private List<gov.nist.hit.core.domain.Document> getProfilesDocs(String rootPath, TestScope scope,
      boolean preloaded, String domain) throws IOException {
    List<gov.nist.hit.core.domain.Document> resourceDocs =
        new ArrayList<gov.nist.hit.core.domain.Document>();

    logger.info("loading integration profiles documents for domain=" + domain);

    JsonNode conf = toJsonObj(
        getDomainBasedPath(PROFILE_PATTERN, domain) + PROFILES_CONF_FILE_PATTERN, rootPath);
    Set<String> skipped = null;
    JsonNode ordersObj = null;
    if (conf != null) {
      skipped = skippedAsList(conf.findValue("skip"));
      ordersObj = conf.findValue("orders");

    }
    List<Resource> resources =
        getResources(getDomainBasedPath(PROFILE_PATTERN, domain) + "*.xml", rootPath);
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String fileName = resource.getFilename();
        if (skipped == null || !skipped.contains(fileName)) {
          gov.nist.hit.core.domain.Document document =
              new gov.nist.hit.core.domain.Document(domain);
          document.setTitle(
              resource.getFilename().substring(0, resource.getFilename().indexOf(".xml")));
          document.setName(resource.getFilename());
          document.setPath(getDomainBasedPath(PROFILE_PATTERN, domain) + resource.getFilename());
          document.setType(DocumentType.PROFILE);
          document.setPosition(ordersObj != null && ordersObj.findValue(fileName) != null
              ? ordersObj.findValue(fileName).intValue() : 0);
          document.setScope(scope);
          document.setAuthorUsername(getDomainAuthorname(domain));
          document.setPreloaded(preloaded);
          resourceDocs.add(document);
        }
      }
    }
    return resourceDocs;
  }

  private List<gov.nist.hit.core.domain.Document> getConstraintsDocs(String rootPath,
      TestScope scope, boolean preloaded, String domain) throws IOException {
    List<gov.nist.hit.core.domain.Document> resourceDocs =
        new ArrayList<gov.nist.hit.core.domain.Document>();
    logger.info("loading constraints documents for domain=" + domain);
    JsonNode conf = toJsonObj(
        getDomainBasedPath(CONSTRAINT_PATTERN, domain) + CONSTRAINTS_CONF_FILE_PATTERN, rootPath);
    Set<String> skipped = null;
    JsonNode ordersObj = null;
    if (conf != null) {
      skipped = skippedAsList(conf.findValue("skip"));
      ordersObj = conf.findValue("orders");
    }
    List<Resource> resources =
        getResources(getDomainBasedPath(CONSTRAINT_PATTERN, domain) + "*.xml", rootPath);
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String fileName = resource.getFilename();
        if (skipped == null || !skipped.contains(fileName)) {
          gov.nist.hit.core.domain.Document document =
              new gov.nist.hit.core.domain.Document(domain);
          document.setTitle(
              resource.getFilename().substring(0, resource.getFilename().indexOf(".xml")));
          document.setName(resource.getFilename());
          document.setPath(getDomainBasedPath(CONSTRAINT_PATTERN, domain) + resource.getFilename());
          document.setType(DocumentType.CONSTRAINT);
          document.setPosition(ordersObj != null && ordersObj.findValue(fileName) != null
              ? ordersObj.findValue(fileName).intValue() : 0);
          document.setScope(scope);
          document.setAuthorUsername(getDomainAuthorname(domain));
          document.setPreloaded(preloaded);
          document.setDomain(domain);
          resourceDocs.add(document);
        }
      }
    }
    return resourceDocs;
  }

  private List<gov.nist.hit.core.domain.Document> getValueSetsDocs(String rootPath, TestScope scope,
      boolean preloaded, String domain) throws IOException {
    List<gov.nist.hit.core.domain.Document> resourceDocs =
        new ArrayList<gov.nist.hit.core.domain.Document>();
    logger.info("loading constraints documents for domain=" + domain);
    JsonNode conf = toJsonObj(
        getDomainBasedPath(VALUESET_PATTERN, domain) + TABLES_CONF_FILE_PATTERN, rootPath);
    Set<String> skipped = null;
    JsonNode ordersObj = null;
    logger.info("loading value sets...");
    // value sets
    skipped = null;
    if (conf != null) {
      skipped = skippedAsList(conf.findValue("skip"));
      ordersObj = conf.findValue("orders");
    }
    List<Resource> resources =
        getResources(getDomainBasedPath(VALUESET_PATTERN, domain) + "*.xml", rootPath);
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String fileName = resource.getFilename();
        if (skipped == null || !skipped.contains(fileName)) {
          gov.nist.hit.core.domain.Document document =
              new gov.nist.hit.core.domain.Document(domain);
          document.setTitle(
              resource.getFilename().substring(0, resource.getFilename().indexOf(".xml")));
          document.setName(resource.getFilename());
          document.setPath(getDomainBasedPath(VALUESET_PATTERN, domain) + resource.getFilename());
          document.setType(DocumentType.TABLE);
          document.setPosition(ordersObj != null && ordersObj.findValue(fileName) != null
              ? ordersObj.findValue(fileName).intValue() : 0);
          document.setAuthorUsername(getDomainAuthorname(domain));
          document.setScope(scope);
          document.setPreloaded(preloaded);
          resourceDocs.add(document);
        }
      }
    }
    return resourceDocs;
  }

  public void loadProfilesDocs(String rootPath, TestScope scope, boolean preloaded, String domain)
      throws IOException {
    logger.info("loading profiles documents...");
    List<gov.nist.hit.core.domain.Document> resourceDocs =
        getProfilesDocs(rootPath, scope, preloaded, domain);
    if (!resourceDocs.isEmpty()) {
      documentRepository.save(resourceDocs);
    }
  }

  public void loadConstraintsDocs(String rootPath, TestScope scope, boolean preloaded,
      String domain) throws IOException {
    logger.info("loading constraints documents...");
    List<gov.nist.hit.core.domain.Document> resourceDocs =
        getConstraintsDocs(rootPath, scope, preloaded, domain);
    if (!resourceDocs.isEmpty()) {
      documentRepository.save(resourceDocs);
    }
  }

  public void loadValuesetsDocs(String rootPath, TestScope scope, boolean preloaded, String domain)
      throws IOException {
    logger.info("loading valuesets documents...");
    List<gov.nist.hit.core.domain.Document> resourceDocs =
        getValueSetsDocs(rootPath, scope, preloaded, domain);
    if (!resourceDocs.isEmpty()) {
      documentRepository.save(resourceDocs);
    }
  }

  public void loadKownIssues(String rootPath, TestScope scope, boolean preloaded, String domain)
      throws IOException {
    logger.info("loading known issues...");
    List<gov.nist.hit.core.domain.Document> knownIssues =
        new ArrayList<gov.nist.hit.core.domain.Document>();
    logger.info("loading known issues documents for domain=" + domain);
    JsonNode knownIssueObj = toJsonObj(
        getDomainBasedPath(KNOWNISSUE_PATTERN, domain) + KNOWNISSUE_FILE_PATTERN, rootPath);
    if (knownIssueObj != null && knownIssueObj.isArray()) {
      Iterator<JsonNode> it = knownIssueObj.elements();
      if (it != null) {
        while (it.hasNext()) {
          JsonNode node = it.next();
          gov.nist.hit.core.domain.Document document = getDocument(node, scope, domain, preloaded,
              KNOWNISSUE_PATTERN, DocumentType.KNOWNISSUE);
          knownIssues.add(document);
        }
      }
    }
    if (!knownIssues.isEmpty()) {
      documentRepository.save(knownIssues);
    }
  }

  private gov.nist.hit.core.domain.Document getDocument(JsonNode node, TestScope scope,
      String domain, boolean preloaded, String pattern, DocumentType type) {
    gov.nist.hit.core.domain.Document document = new gov.nist.hit.core.domain.Document(domain);
    document.setVersion(
        node.findValue("version") != null ? node.findValue("version").textValue() : null);
    document.setTitle(node.findValue("title") != null ? node.findValue("title").textValue() : null);
    document.setName(node.findValue("name") != null ? node.findValue("name").textValue() : null);
    document.setPath(node.findValue("name") != null
        ? getDomainBasedPath(pattern, domain) + node.findValue("name").textValue() : null);
    document.setDate(node.findValue("date") != null ? node.findValue("date").textValue() : null);
    document.setType(type);
    document.setComments(
        node.findValue("comments") != null ? node.findValue("comments").textValue() : null);
    document.setScope(scope);
    document.setAuthorUsername(getDomainAuthorname(domain));
    document.setDomain(domain);
    document.setPreloaded(preloaded);
    return document;
  }

  public void loadReleaseNotes(String rootPath, TestScope scope, boolean preloaded, String domain)
      throws IOException {
    List<gov.nist.hit.core.domain.Document> releaseNotes =
        new ArrayList<gov.nist.hit.core.domain.Document>();
    logger.info("loading release notes documents for domain=" + domain);
    JsonNode releaseNoteObj = toJsonObj(
        getDomainBasedPath(RELEASENOTE_PATTERN, domain) + RELEASENOTE_FILE_PATTERN, rootPath);
    if (releaseNoteObj != null && releaseNoteObj.isArray()) {
      Iterator<JsonNode> it = releaseNoteObj.elements();
      if (it != null) {
        while (it.hasNext()) {
          JsonNode node = it.next();
          gov.nist.hit.core.domain.Document document = getDocument(node, scope, domain, preloaded,
              RELEASENOTE_PATTERN, DocumentType.RELEASENOTE);
          releaseNotes.add(document);
        }
      }
    }
    if (!releaseNotes.isEmpty()) {
      documentRepository.save(releaseNotes);
    }
  }

  public void loadInstallationGuides(String rootPath, TestScope scope, boolean preloaded,
      String domain) throws IOException {
    if (!appInfoService.get().isDownloadWarDisabled()) {
      logger.info("loading tool downloads...");
      JsonNode confObj = toJsonObj(
          getDomainBasedPath(TOOL_DOWNLOADS_PATTERN, domain) + TOOL_DOWNLOADS_CONF_PATTERN,
          rootPath);
      if (confObj != null && confObj.isArray()) {
        List<gov.nist.hit.core.domain.Document> docs =
            new ArrayList<gov.nist.hit.core.domain.Document>();
        Iterator<JsonNode> it = confObj.elements();
        while (it.hasNext()) {
          JsonNode instructionObj = it.next();
          gov.nist.hit.core.domain.Document document =
              new gov.nist.hit.core.domain.Document(domain);
          if (instructionObj.findValue("title") == null || instructionObj.findValue("name") == null
              || instructionObj.findValue("date") == null) {
            throw new IllegalArgumentException(
                "The installation guide is missing one of those: title, link, date");
          }

          document.setTitle(instructionObj.findValue("title").textValue());
          document.setName(instructionObj.findValue("name").textValue());
          document.setPath(getDomainBasedPath(TOOL_DOWNLOADS_PATTERN, TOOL_DOCUMENT_DOMAIN)
              + instructionObj.findValue("name").textValue());
          document.setDate(instructionObj.findValue("date").textValue());
          document.setType(DocumentType.INSTALLATION);
          document.setScope(scope);
          document.setPreloaded(preloaded);
          document.setAuthorUsername(getDomainAuthorname(domain));
          docs.add(document);
        }
        if (!docs.isEmpty()) {
          documentRepository.save(docs);
        }
      }
    }
  }

  public void loadToolDownloads(String rootPath, TestScope scope, boolean preloaded, String domain)
      throws IOException {
    if (!appInfoService.get().isDownloadWarDisabled()) {
      logger.info("loading tool downloads...");
      JsonNode confObj = toJsonObj(
          getDomainBasedPath(TOOL_DOWNLOADS_PATTERN, domain) + TOOL_DOWNLOADS_CONF_PATTERN,
          rootPath);
      if (confObj != null && confObj.isArray()) {
        List<gov.nist.hit.core.domain.Document> docs =
            new ArrayList<gov.nist.hit.core.domain.Document>();
        Iterator<JsonNode> it = confObj.elements();
        if (it != null) {
          while (it.hasNext()) {
            JsonNode node = it.next();
            gov.nist.hit.core.domain.Document document =
                new gov.nist.hit.core.domain.Document(domain);
            if (node.findValue("title") == null || node.findValue("link") == null
                || node.findValue("date") == null) {
              throw new IllegalArgumentException(
                  "Download is missing one of those: title, link, date");
            }
            document.setTitle(node.findValue("title").textValue());
            document.setPath(node.findValue("link").textValue());
            document.setDate(node.findValue("date").textValue());
            document.setType(DocumentType.DELIVERABLE);
            document.setScope(scope);
            document.setPreloaded(preloaded);
            document.setDomain(domain);
            document.setAuthorUsername(getDomainAuthorname(domain));
            docs.add(document);
          }
          if (!docs.isEmpty()) {
            documentRepository.save(docs);
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

  public void loadConstraints(String rootPath, TestScope scope, boolean preloaded, String domain)
      throws IOException {
    logger.info("loading constraints of domain=" + domain);
    List<Resource> resources =
        getResources(getDomainBasedPath(CONSTRAINT_PATTERN, domain) + "*.xml", rootPath);
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String username = getDomainAuthorname(domain);
        String content = FileUtil.getContent(resource);
        Constraints constraints = constraint(content, domain, scope, username, preloaded);
        constraints.setDomain(domain);
        constraints.setScope(scope);
        constraints.setAuthorUsername(username);
        constraints.setPreloaded(preloaded);
        // cachedRepository.getCachedConstraints().put(constraints.getSourceId(),
        // constraints);
        this.constraintsRepository.save(constraints);
      }
    }
  }

  public String getDomainAuthorname(String domain) {
    if (!TOOL_DOCUMENT_DOMAIN.equals(domain)) {
      Domain dom = domainService.findOneByKey(domain);
      if (dom == null) {
        throw new IllegalArgumentException("Domain with key=" + domain + " not found");
      }
      return dom.getAuthorUsername();
    } else {
      return ownerUsername;
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

  private String getDomainBasedPath(String path, String domain) {
    if (domain.equals("")) {
      return path;
    } else {
      return path + domain + "/";
    }
  }

  public void loadIntegrationProfiles(String rootPath, TestScope scope, boolean preloaded,
      String domain) throws IOException {
    logger.info("loading integration profiles... of domain=" + domain);
    List<Resource> resources =
        getResources(getDomainBasedPath(PROFILE_PATTERN, domain) + "*.xml", rootPath);
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        IntegrationProfile integrationProfile = integrationProfile(FileUtil.getContent(resource),
            domain, scope, getDomainAuthorname(domain), preloaded);
        integrationProfileRepository.save(integrationProfile);
      }
    }
  }

  public void loadVocabularyLibraries(String rootPath, TestScope scope, boolean preloaded,
      String domain) throws IOException {
    logger.info("loading value set libraries of domain=" + domain);
    List<Resource> resources =
        getResources(getDomainBasedPath(VALUESET_PATTERN, domain) + "*.xml", rootPath);
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String content = FileUtil.getContent(resource);
        try {
          VocabularyLibrary vocabLibrary =
              vocabLibrary(content, domain, scope, getDomainAuthorname(domain), preloaded);
          this.vocabularyLibraryRepository.save(vocabLibrary);
          // cachedRepository.getCachedVocabLibraries().put(vocabLibrary.getSourceId(),
          // vocabLibrary);
        } catch (UnsupportedOperationException e) {
        }
      }
    }
  }

  protected Constraints additionalConstraints(String content, String domain, TestScope scope,
      String username, boolean preloaded) throws IOException {
    if (content == null) {
      return null;
    }
    return constraint(content, domain, scope, username, preloaded);
  }

  public IntegrationProfile integrationProfile(String content, String domain, TestScope scope,
      String authorUsername, boolean preloaded) {
    Document doc = this.stringToDom(content);
    IntegrationProfile integrationProfile = new IntegrationProfile();
    Element profileElement = (Element) doc.getElementsByTagName("ConformanceProfile").item(0);
    integrationProfile.setSourceId(profileElement.getAttribute("ID"));
    Element metaDataElement = (Element) profileElement.getElementsByTagName("MetaData").item(0);
    integrationProfile.setName(metaDataElement.getAttribute("Name"));
    integrationProfile.setXml(content);
    integrationProfile.setDomain(domain);
    integrationProfile.setScope(scope);
    integrationProfile.setAuthorUsername(authorUsername);
    integrationProfile.setPreloaded(preloaded);

    Element conformanceProfilElementRoot =
        (Element) profileElement.getElementsByTagName("Messages").item(0);
    NodeList messages = conformanceProfilElementRoot.getElementsByTagName("Message");

    // Message IDs
    // List<String> ids = new ArrayList<String>();

    for (int j = 0; j < messages.getLength(); j++) {
      Element elmCode = (Element) messages.item(j);
      String id = elmCode.getAttribute("ID");
      // ids.add(id);
      profilesMap.put(id, integrationProfile.getSourceId());
    }
    // integrationProfile.setMessages(ids);

    // for (String messageId : ids) {
    // ConformanceProfile conformanceProfile =
    // conformanceProfileRepository.findOneBySourceId(messageId);
    // if (conformanceProfile == null) {
    // conformanceProfile = new ConformanceProfile();
    // }
    // conformanceProfile.setXml(getConformanceProfileContent(content,
    // messageId));
    // conformanceProfile.setIntegrationProfile(integrationProfile);
    // conformanceProfile.setSourceId(messageId);
    // conformanceProfile.setDomain(domain);
    // conformanceProfile.setScope(scope);
    // conformanceProfileRepository.save(conformanceProfile);
    //
    // }

    return integrationProfile;
  }

  protected Message message(String content, String domain, TestScope scope, String authorUsername,
      boolean preloaded) {
    if (content != null) {
      Message m = new Message();
      m.setContent(content);
      m.setDomain(domain);
      m.setScope(scope);
      m.setPreloaded(preloaded);
      m.setAuthorUsername(authorUsername);
      return m;
    }
    return null;
  }

  public Constraints constraint(String content, String domain, TestScope scope, String username,
      boolean preloaded) {
    Document doc = this.stringToDom(content);
    Constraints constraints = new Constraints();
    constraints.setXml(content);
    Element constraintsElement = (Element) doc.getElementsByTagName("ConformanceContext").item(0);
    constraints.setSourceId(constraintsElement.getAttribute("UUID"));
    constraints.setDomain(domain);
    constraints.setScope(scope);
    constraints.setAuthorUsername(username);
    constraints.setPreloaded(preloaded);
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

  protected String prettyPrint(Document xml) {
    try {
      Transformer tf = TransformerFactory.newInstance().newTransformer();
      tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      tf.setOutputProperty(OutputKeys.INDENT, "yes");
      Writer out = new StringWriter();
      tf.transform(new DOMSource(xml), new StreamResult(out));
      return out.toString();
    } catch (Exception e) {

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

  public String getConformanceProfileContent(String integrationProfileXml, String messageId) {
    Document doc = stringToDom(integrationProfileXml);
    IntegrationProfile integrationProfile = new IntegrationProfile();
    Element profileElement = (Element) doc.getElementsByTagName("ConformanceProfile").item(0);
    integrationProfile.setSourceId(profileElement.getAttribute("ID"));
    Element conformanceProfilElementRoot =
        (Element) profileElement.getElementsByTagName("Messages").item(0);
    NodeList messages = conformanceProfilElementRoot.getElementsByTagName("Message");
    List<Node> toRemove = new ArrayList<Node>();
    for (int index = 0; index < messages.getLength(); index++) {
      Node node = messages.item(index);
      Element messagesElm = (Element) node;
      String id = messagesElm.getAttribute("ID");
      if (!id.equals(messageId)) {
        toRemove.add(node);
      }
    }

    if (!toRemove.isEmpty()) {
      for (int index = 0; index < toRemove.size(); index++) {
        conformanceProfilElementRoot.removeChild(toRemove.get(index));
      }
    }
    doc.normalize();
    return prettyPrint(doc);
  }

  protected IntegrationProfile getIntegrationProfile(String sourceId) throws IOException {
    IntegrationProfile p = integrationProfileRepository.findBySourceId(sourceId);
    if (p == null) {
      throw new IllegalArgumentException(
          "Cannot find integration profile with sourceId = " + sourceId);
    }

    return p;
  }

  protected Constraints getConstraints(String sourceId) throws IOException {
    Constraints c = constraintsRepository.findOneBySourceId(sourceId);
    if (c == null) {
      throw new IllegalArgumentException("Constraints with sourceId = " + sourceId + " not found");
    }
    return c;
  }

  protected VocabularyLibrary getVocabularyLibrary(String sourceId) throws IOException {
    VocabularyLibrary v = vocabularyLibraryRepository.findOneBySourceId(sourceId);
    if (v == null) {
      throw new IllegalArgumentException("VocabularyLibrary with id = " + sourceId + " not found");
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

  public void loadContextBasedTestCases(String rootPath, TestScope scope, boolean preloaded,
      String domain) throws Exception {
    List<Resource> resources =
        this.getDirectories(getDomainBasedPath(CONTEXTBASED_PATTERN, domain) + "*/", rootPath);
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        String location = fileName.substring(
            fileName.indexOf(getDomainBasedPath(CONTEXTBASED_PATTERN, domain)), fileName.length());
        TestPlan testPlan = testPlan(location, TestingStage.CB, rootPath, domain, scope,
            getDomainAuthorname(domain), preloaded);
        if (testPlan != null) {
          checkPersistentId(testPlan.getPersistentId(), location);
          testPlanRepository.save(testPlan);
        }
      }
    }
  }

  public void loadContextFreeTestCases(String rootPath, TestScope scope, boolean preloaded,
      String domain) throws Exception {
    List<Resource> resources =
        this.getDirectories(getDomainBasedPath(CONTEXTFREE_PATTERN, domain) + "*/", rootPath);
    if (resources != null && !resources.isEmpty()) {
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        String location = fileName.substring(
            fileName.indexOf(getDomainBasedPath(CONTEXTFREE_PATTERN, domain)), fileName.length());
        CFTestPlan testPlan =
            cfTestPlan(location, rootPath, domain, scope, getDomainAuthorname(domain), preloaded);
        if (testPlan != null) {
          checkPersistentId(testPlan.getPersistentId(), fileName);
          cfTestPlanRepository.save(testPlan);
        }
      }
    }
  }

  protected TestCase testCase(String location, TestingStage stage, boolean transportSupported,
      String rootPath, String domain, TestScope scope, String authorUsername, boolean preloaded)
      throws Exception {
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
    tc.setPreloaded(preloaded);
    tc.setScope(scope);
    tc.setDomain(domain);
    tc.setPersistentId(Long.parseLong(testCaseObj.findValue("id").asText()));
    tc.setDescription(testCaseObj.findValue("description").textValue());
    tc.setVersion(!testCaseObj.has("version") ? 1.0
        : Double.parseDouble(testCaseObj.findValue("version").asText()));

    tc.setTestStory(testStory(location, rootPath, domain));
    tc.setJurorDocument(jurorDocument(location, rootPath, domain));
    if (testCaseObj.findValue("position") != null) {
      tc.setPosition(testCaseObj.findValue("position").intValue());
    }
    if (testCaseObj.has("supplements")) {
      tc.getSupplements()
          .addAll((testDocuments(location, testCaseObj.findValue("supplements"), domain)));
    }
    List<Resource> resources = this.getDirectories(location + "*", rootPath);
    for (Resource resource : resources) {
      String fileName = fileName(resource);
      String tcLocation = fileName.substring(fileName.indexOf(location), fileName.length());
      TestStep testStep = testStep(tcLocation, stage, transportSupported, rootPath, domain, scope,
          authorUsername, preloaded);
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
      String rootPath, String domain, TestScope scope, String authorUsername, boolean preloaded)
      throws Exception {
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
    testStep.setPreloaded(preloaded);
    testStep.setDomain(domain);
    testStep.setScope(scope);
    testStep.setAuthorUsername(authorUsername);
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
      testStep.setTestContext(testContext(location, testStepObj, stage, rootPath, domain, scope,
          authorUsername, preloaded));
    }

    if (testStepObj.has("supplements")) {
      testStep.getSupplements()
          .addAll((testDocuments(location, testStepObj.findValue("supplements"), domain)));
    }
    testStep.setTestStory(testStory(location, rootPath, domain));
    testStep.setJurorDocument(jurorDocument(location, rootPath, domain));
    testStep.setMessageContent(
        messageContent(location, rootPath, domain, scope, authorUsername, preloaded));
    testStep.setTestDataSpecification(
        testDataSpecification(location, rootPath, domain, scope, authorUsername, preloaded));
    if (testStepObj.findValue("position") != null) {
      testStep.setPosition(testStepObj.findValue("position").intValue());
    }
    testStep.setStage(stage);
    return testStep;
  }

  private TestArtifact testStory(String location, String rootPath, String domain)
      throws IOException {
    return artifact(location, "TestStory", rootPath, domain);
  }

  private TestArtifact jurorDocument(String location, String rootPath, String domain)
      throws IOException {
    return artifact(location, "JurorDocument", rootPath, domain);
  }

  private TestArtifact artifact(String location, String type, String rootPath, String domain)
      throws IOException {
    TestArtifact doc = null;

    String path = location + type + ".html";
    Resource resource = this.getResource(path, rootPath);
    if (resource != null) {
      doc = doc == null ? new TestArtifact(type) : doc;
      doc.setHtml(FileUtil.getContent(resource));
      doc.setDomain(domain);
      doc.setScope(TestScope.GLOBAL);
    }

    path = location + type + ".pdf";
    resource = this.getResource(path, rootPath);
    if (resource != null) {
      doc = doc == null ? new TestArtifact(type) : doc;
      doc.setPdfPath(path);
      doc.setDomain(domain);
      doc.setScope(TestScope.GLOBAL);

    }

    if (type.equals("TestStory")) { // TODO: Temporary hack
      path = location + type + ".json";
      resource = this.getResource(path, rootPath);
      if (resource != null) {
        doc = doc == null ? new TestArtifact(type) : doc;
        doc.setJson(FileUtil.getContent(resource));
        doc.setDomain(domain);
        doc.setScope(TestScope.GLOBAL);

      }
    }

    return doc;
  }

  private TestArtifact messageContent(String location, String rootPath, String domain,
      TestScope scope, String authorUsername, boolean preloaded) throws IOException {
    return artifact(location, "MessageContent", rootPath, domain);
  }

  private TestArtifact testDataSpecification(String location, String rootPath, String domain,
      TestScope scope, String authorUsername, boolean preloaded) throws IOException {
    return artifact(location, "TestDataSpecification", rootPath, domain);
  }

  private TestArtifact testPackage(String location, String rootPath, String domain, TestScope scope,
      String authorUsername, boolean preloaded) throws IOException {
    return artifact(location, "TestPackage", rootPath, domain);
  }

  private TestArtifact testPlanSummary(String location, String rootPath, String domain,
      TestScope scope, String authorUsername, boolean preloaded) throws IOException {
    // return artifact(location, "QuickTestCaseReferenceGuide");
    return artifact(location, "TestPlanSummary", rootPath, domain);
  }

  protected TestCaseGroup testCaseGroup(String location, TestingStage stage,
      boolean transportEnabled, String rootPath, String domain, TestScope scope,
      String authorUsername, boolean preloaded) throws Exception {
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
      tcg.setPreloaded(preloaded);
      tcg.setScope(scope);
      tcg.setAuthorUsername(authorUsername);
      tcg.setDomain(domain);
      tcg.setPersistentId(Long.parseLong(testPlanObj.findValue("id").asText()));
      tcg.setDescription(testPlanObj.findValue("description").textValue());
      tcg.setVersion(!testPlanObj.has("version") ? 1.0
          : Double.parseDouble(testPlanObj.findValue("version").asText()));
      tcg.setTestStory(testStory(location, rootPath, domain));
      if (testPlanObj.findValue("position") != null) {
        tcg.setPosition(testPlanObj.findValue("position").intValue());
      } else {
        tcg.setPosition(1);
      }
      if (testPlanObj.has("supplements")) {
        tcg.getSupplements()
            .addAll(testDocuments(location, testPlanObj.findValue("supplements"), domain));
      }
      List<Resource> resources = this.getDirectories(location + "*/", rootPath);
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        String tcLocation = fileName.substring(fileName.indexOf(location), fileName.length());
        Resource descriptorResource = getDescriptorResource(tcLocation, rootPath);
        if (descriptorResource != null) {
          String filename = descriptorResource.getFilename();
          if (filename.endsWith("TestCaseGroup.json")) {
            TestCaseGroup testCaseGroup = testCaseGroup(tcLocation, stage, transportEnabled,
                rootPath, domain, scope, authorUsername, preloaded);
            checkPersistentId(testCaseGroup.getPersistentId(), location);
            tcg.getTestCaseGroups().add(testCaseGroup);
          } else if (filename.endsWith("TestCase.json")) {
            TestCase testCase = testCase(tcLocation, stage, transportEnabled, rootPath, domain,
                scope, authorUsername, preloaded);
            checkPersistentId(testCase.getPersistentId(), location);
            tcg.getTestCases().add(testCase);
          }
        }
      }
      return tcg;
    }
    return null;
  }

  protected CFTestStepGroup cfTestStepGroup(String location, String rootPath, String domain,
      TestScope scope, String authorUsername, boolean preloaded) throws Exception {
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
      tcg.setPreloaded(preloaded);
      tcg.setAuthorUsername(authorUsername);
      tcg.setScope(scope);
      tcg.setPersistentId(Long.parseLong(testPlanObj.findValue("id").asText()));
      tcg.setDescription(testPlanObj.findValue("description").textValue());
      tcg.setVersion(!testPlanObj.has("version") ? 1.0
          : Double.parseDouble(testPlanObj.findValue("version").asText()));
      tcg.setTestStory(testStory(location, rootPath, domain));
      if (testPlanObj.findValue("position") != null) {
        tcg.setPosition(testPlanObj.findValue("position").intValue());
      } else {
        tcg.setPosition(1);
      }
      if (testPlanObj.has("supplements")) {
        tcg.getSupplements()
            .addAll(testDocuments(location, testPlanObj.findValue("supplements"), domain));
      }
      tcg.setDomain(domain);
      List<Resource> resources = this.getDirectories(location + "*/", rootPath);
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        String tcLocation = fileName.substring(fileName.indexOf(location), fileName.length());
        Resource descriptorResource = getDescriptorResource(tcLocation, rootPath);
        if (descriptorResource != null) {
          String filename = descriptorResource.getFilename();
          if (filename.endsWith("TestStepGroup.json")) {
            CFTestStepGroup testStepGroup =
                cfTestStepGroup(tcLocation, rootPath, domain, scope, authorUsername, preloaded);
            if (testStepGroup != null) {
              checkPersistentId(testStepGroup.getPersistentId(), filename);
              tcg.getTestStepGroups().add(testStepGroup);
            }
          } else if (filename.endsWith("TestObject.json")) {
            CFTestStep testStep =
                cfTestStep(tcLocation, rootPath, domain, scope, authorUsername, preloaded);
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

  protected TestPlan testPlan(String location, TestingStage stage, String rootPath, String domain,
      TestScope scope, String authorUsername, boolean preloaded) throws Exception {
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

      Long persistenceId = Long.parseLong(testPlanObj.findValue("id").asText());
      TestPlan oldTestPlan = testPlanRepository.getByPersistentId(persistenceId);

      if (oldTestPlan != null) {
        if (oldTestPlan != null && !oldTestPlan.getAuthorUsername().equals(authorUsername)) {
          throw new IllegalArgumentException("You do not have sufficient right to add test plan "
              + testPlanObj.findValue("name").textValue()
              + ". A different test plan with the same identifier already exists and belongs to a different user");
        }
        testPlanRepository.delete(oldTestPlan);
      }

      tp.setPreloaded(preloaded);
      tp.setScope(scope);
      tp.setAuthorUsername(authorUsername);
      tp.setDomain(domain);
      tp.setPersistentId(Long.parseLong(testPlanObj.findValue("id").asText()));
      tp.setName(testPlanObj.findValue("name").textValue());
      tp.setDescription(testPlanObj.findValue("description").textValue());
      // tp.setCategory(testPlanObj.findValue("category") != null
      // ? testPlanObj.findValue("category").textValue() :
      // DEFAULT_CATEGORY);
      tp.setVersion(!testPlanObj.has("version") ? 1.0
          : Double.parseDouble(testPlanObj.findValue("version").asText()));
      tp.setTestStory(testStory(location, rootPath, domain));
      tp.setStage(stage);

      tp.setTransport(testPlanObj.findValue("transport") != null
          ? testPlanObj.findValue("transport").booleanValue() : false);
      if (testPlanObj.findValue("position") != null) {
        tp.setPosition(testPlanObj.findValue("position").intValue());
      } else {
        tp.setPosition(1);
      }
      tp.setTestPackage(testPackage(location, rootPath, domain, scope, authorUsername, preloaded));
      tp.setTestPlanSummary(
          testPlanSummary(location, rootPath, domain, scope, authorUsername, preloaded));
      if (testPlanObj.has("supplements")) {
        tp.getSupplements()
            .addAll((testDocuments(location, testPlanObj.findValue("supplements"), domain)));
      }

      List<Resource> resources = this.getDirectories(location + "*/", rootPath);
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        String loca = fileName.substring(fileName.indexOf(location), fileName.length());
        Resource descriptorResource = getDescriptorResource(loca, rootPath);
        if (descriptorResource != null) {
          String filename = descriptorResource.getFilename();
          if (filename.endsWith("TestCaseGroup.json")) {
            TestCaseGroup testCaseGroup = testCaseGroup(loca, stage, tp.isTransport(), rootPath,
                domain, scope, authorUsername, preloaded);
            checkPersistentId(testCaseGroup.getPersistentId(), fileName);
            tp.getTestCaseGroups().add(testCaseGroup);
          } else if (filename.endsWith("TestCase.json")) {
            TestCase testCase = testCase(loca, stage, tp.isTransport(), rootPath, domain, scope,
                authorUsername, preloaded);
            checkPersistentId(testCase.getPersistentId(), fileName);
            tp.getTestCases().add(testCase);
          }
        }
      }
      return tp;
    }
    return null;
  }

  protected CFTestPlan cfTestPlan(String testPlanPath, String rootPath, String domain,
      TestScope scope, String authorUsername, boolean preloaded) throws Exception {
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
      testPlan.setPreloaded(preloaded);
      testPlan.setDomain(domain);
      testPlan.setScope(scope);
      testPlan.setAuthorUsername(authorUsername);
      // testPlan.setCategory(testPlanObj.findValue("category") != null
      // ? testPlanObj.findValue("category").textValue() :
      // DEFAULT_CATEGORY);
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
            .addAll(testDocuments(testPlanPath, testPlanObj.findValue("supplements"), domain));
      }

      List<Resource> resources = this.getDirectories(testPlanPath + "*/", rootPath);
      for (Resource resource : resources) {
        String fileName = fileName(resource);
        String tcLocation = fileName.substring(fileName.indexOf(testPlanPath), fileName.length());
        Resource descriptorResource = getDescriptorResource(tcLocation, rootPath);
        if (descriptorResource != null) {
          String filename = descriptorResource.getFilename();
          if (filename.endsWith("TestStepGroup.json")) {
            CFTestStepGroup testStepeGroup =
                cfTestStepGroup(tcLocation, rootPath, domain, scope, authorUsername, preloaded);
            if (testStepeGroup != null) {
              checkPersistentId(testStepeGroup.getPersistentId(), filename);
              testPlan.getTestStepGroups().add(testStepeGroup);
            }
          } else if (filename.endsWith("TestObject.json")) {
            CFTestStep testStep =
                cfTestStep(tcLocation, rootPath, domain, scope, authorUsername, preloaded);
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

  protected CFTestStep cfTestStep(String testObjectPath, String rootPath, String domain,
      TestScope scope, String authorUsername, boolean preloaded) throws Exception {
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
      testStep.setPreloaded(preloaded);
      testStep.setDomain(domain);
      testStep.setScope(scope);
      testStep.setAuthorUsername(authorUsername);
      testStep.setPersistentId(Long.parseLong(testPlanObj.findValue("id").asText()));
      if (testPlanObj.findValue("position") != null) {
        testStep.setPosition(testPlanObj.findValue("position").intValue());
      }
      testStep.setDescription(testPlanObj.findValue("description").textValue());
      testStep.setVersion(!testPlanObj.has("version") ? 1.0
          : Double.parseDouble(testPlanObj.findValue("version").asText()));
      testStep.setTestContext(testContext(testObjectPath, testPlanObj, TestingStage.CF, rootPath,
          domain, scope, authorUsername, preloaded));
      if (testPlanObj.has("supplements")) {
        testStep.getSupplements()
            .addAll(testDocuments(testObjectPath, testPlanObj.findValue("supplements"), domain));
      }
      return testStep;
    }
    return null;
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

  private Set<gov.nist.hit.core.domain.Document> testDocuments(String testPath, JsonNode nodeObj,
      String domain) {
    Set<gov.nist.hit.core.domain.Document> documents =
        new HashSet<gov.nist.hit.core.domain.Document>();
    Iterator<JsonNode> it = nodeObj.elements();
    if (it != null) {
      while (it.hasNext()) {
        JsonNode node = it.next();
        gov.nist.hit.core.domain.Document document = new gov.nist.hit.core.domain.Document(domain);
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
  public void loadAppInfo() throws JsonProcessingException, IOException {
    logger.info("loading app info...");
    AppInfo appInfo = new AppInfo();
    if (adminEmails == null) {
      throw new RuntimeException("Administrator emails address are missing");
    }
    appInfo.setAdminEmails(adminEmails);
    appInfo.setCbManagementSupported(cbManagementSupported);
    appInfo.setCfManagementSupported(cfManagementSupported);
    appInfo.setAuthenticationRequired(authenticationRequired);
    appInfo.setEmployerRequired(employerRequired);
    appInfo.setSubTitle(appSubTitle);
    appInfo.setOrganization(organizationName);
    appInfo.setOrganizationName(organizationName);
    appInfo.setOrganizationLogo(appOrganizationLogo);
    appInfo.setOrganizationLink(appOrganizationLink);
    appInfo.setDocManagementSupported(docManagementSupported);

    appInfo.setHeader(appHeader);
    appInfo.setName(appName);
    appInfo.setVersion(appVersion);
    appInfo.setDate(new Date().getTime() + "");
    appInfo.setContactEmail(appContactEmail);

    appInfo.setDisclaimer(appDisclaimerContent);
    appInfo.setDisclaimerLink(appDisclaimerLink);
    appInfo.setOwnerUsername(ownerUsername);

    appInfo.setAcknowledgment(appAcknowledgment);
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
    appInfo.setDomainManagementSupported(domainManagementSupported);
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
    if (props != null) {
      rsbVersion = props.getProperty("app.resourceBundleVersion");
    }
    if (rsbVersion == null) {
      return getRsbleVersion();
    } else {
      return rsbVersion;
    }
  }

  public HashMap<String, String> getProfilesMap() {
    return profilesMap;
  }
}
