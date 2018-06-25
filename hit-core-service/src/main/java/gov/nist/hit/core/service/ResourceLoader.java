package gov.nist.hit.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import gov.nist.hit.core.domain.CFTestStep;
import gov.nist.hit.core.domain.CFTestStepGroup;
import gov.nist.hit.core.domain.ResourceType;
import gov.nist.hit.core.domain.ResourceUploadAction;
import gov.nist.hit.core.domain.ResourceUploadResult;
import gov.nist.hit.core.domain.ResourceUploadStatus;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.repo.TestCaseGroupRepository;
import gov.nist.hit.core.repo.TestCaseRepository;
import gov.nist.hit.core.repo.TestStepRepository;
import gov.nist.hit.core.service.util.ResourcebundleHelper;

public abstract class ResourceLoader extends ResourcebundleLoader {

  @Autowired
  private TestCaseRepository testCaseRepository;

  @Autowired
  private TestCaseGroupRepository testCaseGroupRepository;

  @Autowired
  private TestStepRepository testStepRepository;

  public List<Resource> getApiDirectories(String pattern, String rootPath) throws IOException {
    return ResourcebundleHelper.getDirectoriesFile(rootPath + pattern);
  }

  public Resource getApiResource(String pattern, String rootPath) throws IOException {
    return ResourcebundleHelper.getResourceFile(rootPath + pattern);
  }

  public List<Resource> getApiResources(String pattern, String rootPath) throws IOException {
    return ResourcebundleHelper.getResourcesFile(rootPath + pattern);
  }

  // ------ Context-Free test Case

  @org.springframework.transaction.annotation.Transactional("transactionManager")
  public ResourceUploadStatus handleCFTC(Long testStepGroupId, CFTestStep tc) {

    ResourceUploadStatus result = new ResourceUploadStatus();
    result.setType(ResourceType.TESTSTEP);
    result.setId(tc.getPersistentId());

    CFTestStep existing = this.cfTestStepRepository.getByPersistentId(tc.getPersistentId());

    if (existing != null) {
      if (existing.getTestCase().getDomain() != tc.getDomain()) {
        result.setAction(ResourceUploadAction.UPDATE);
        result.setStatus(ResourceUploadResult.FAILURE);
        result.setMessage("CFTestStepGroup(" + testStepGroupId + ") and CFTestStep("
            + tc.getPersistentId() + ") cannot belong to different domain");
        return result;
      }
      result.setAction(ResourceUploadAction.UPDATE);
      Long exId = existing.getId();
      tc.setId(exId);
      this.cfTestStepRepository.saveAndFlush(tc);
      result.setStatus(ResourceUploadResult.SUCCESS);
      return result;
    } else {
      result.setAction(ResourceUploadAction.ADD);
      if (testStepGroupId != null && testStepGroupId != -1) {
        CFTestStepGroup parent = this.cfTestSteGroupRepository.getByPersistentId(testStepGroupId);
        if (parent == null) {
          result.setStatus(ResourceUploadResult.FAILURE);
          result.setMessage("CFTestStepGroup(" + testStepGroupId + ") not found");
          return result;
        } else {
          if (tc.getDomain() != parent.getDomain()) {
            result.setAction(ResourceUploadAction.UPDATE);
            result.setStatus(ResourceUploadResult.FAILURE);
            result.setMessage("CFTestStepGroup(" + testStepGroupId + ") and CFTestStep("
                + tc.getPersistentId() + ") cannot belong to different domains");
            return result;
          }
          this.cfTestSteGroupRepository.saveAndFlush(parent);
          result.setStatus(ResourceUploadResult.SUCCESS);
          return result;
        }
      } else {
        // tc.setRoot(true);
        this.cfTestStepRepository.saveAndFlush(tc);
        result.setStatus(ResourceUploadResult.SUCCESS);
        return result;
      }
    }
  }

  // ------ Context-Based test Case
  @org.springframework.transaction.annotation.Transactional("transactionManager")
  public ResourceUploadStatus handleTS(Long testCaseId, TestStep ts) {

    ResourceUploadStatus result = new ResourceUploadStatus();
    result.setType(ResourceType.TESTSTEP);
    result.setId(ts.getPersistentId());

    TestStep existing = this.testStepRepository.getByPersistentId(ts.getPersistentId());

    if (existing != null) {
      if (existing.getTestCase().getDomain() != ts.getDomain()) {
        result.setAction(ResourceUploadAction.UPDATE);
        result.setStatus(ResourceUploadResult.FAILURE);
        result.setMessage("TestCase(" + testCaseId + ") and TestStep(" + ts.getPersistentId()
            + ") cannot belong to different domain");
        return result;
      }
      result.setAction(ResourceUploadAction.UPDATE);
      Long exId = existing.getId();
      ts.setId(exId);
      ts.setTestCase(existing.getTestCase());
      this.testStepRepository.saveAndFlush(ts);
      result.setStatus(ResourceUploadResult.SUCCESS);
      return result;
    } else {
      result.setAction(ResourceUploadAction.ADD);
      TestCase tc = this.testCaseRepository.getByPersistentId(testCaseId);
      if (tc == null) {
        result.setStatus(ResourceUploadResult.FAILURE);
        result.setMessage("TestCase(" + testCaseId + ") not found");
        return result;
      } else {
        if (tc.getDomain() != ts.getDomain()) {
          result.setAction(ResourceUploadAction.UPDATE);
          result.setStatus(ResourceUploadResult.FAILURE);
          result.setMessage("TestCase(" + testCaseId + ") and TestStep(" + ts.getPersistentId()
              + ") cannot belong to different domains");
          return result;
        }
        tc.addTestStep(ts);
        this.testCaseRepository.saveAndFlush(tc);
        result.setStatus(ResourceUploadResult.SUCCESS);
        return result;
      }
    }

  }

  @org.springframework.transaction.annotation.Transactional("transactionManager")
  public ResourceUploadStatus addTC(Long parentId, TestCase tc, String where) {

    ResourceUploadStatus result = new ResourceUploadStatus();
    result.setType(ResourceType.TESTCASE);
    result.setAction(ResourceUploadAction.ADD);
    result.setId(tc.getPersistentId());

    if (where.toLowerCase().equals("group")) {
      TestCaseGroup tcg = this.testCaseGroupRepository.getByPersistentId(parentId);
      if (tcg == null) {
        result.setStatus(ResourceUploadResult.FAILURE);
        result.setMessage("TestCaseGroup(" + parentId + ") not found");
        return result;
      } else {
        if (tcg.getDomain() != tc.getDomain()) {
          result.setAction(ResourceUploadAction.UPDATE);
          result.setStatus(ResourceUploadResult.FAILURE);
          result.setMessage("TestCaseGroup(" + parentId + ") and TestCase(" + tc.getPersistentId()
              + ") cannot belong to different domain");
          return result;
        }
        tcg.getTestCases().add(tc);
        this.testCaseGroupRepository.saveAndFlush(tcg);
        result.setStatus(ResourceUploadResult.SUCCESS);
        return result;
      }

    } else if (where.toLowerCase().equals("plan")) {
      TestPlan tp = this.testPlanRepository.getByPersistentId(parentId);
      if (tp == null) {
        result.setStatus(ResourceUploadResult.FAILURE);
        result.setMessage("TestPlan(" + parentId + ") not found");
        return result;
      } else {
        if (tp.getDomain() != tc.getDomain()) {
          result.setAction(ResourceUploadAction.UPDATE);
          result.setStatus(ResourceUploadResult.FAILURE);
          result.setMessage("TestPlan(" + parentId + ") and TestCase(" + tc.getPersistentId()
              + ") cannot belong to different domains");
          return result;
        }
        tp.getTestCases().add(tc);
        this.testPlanRepository.saveAndFlush(tp);
        result.setStatus(ResourceUploadResult.SUCCESS);
        return result;
      }
    } else {
      return null;
    }
  }

  @org.springframework.transaction.annotation.Transactional("transactionManager")
  public ResourceUploadStatus updateTC(TestCase tc) {
    ResourceUploadStatus result = new ResourceUploadStatus();
    result.setType(ResourceType.TESTCASE);
    result.setAction(ResourceUploadAction.UPDATE);
    result.setId(tc.getPersistentId());

    TestCase existing = this.testCaseRepository.getByPersistentId(tc.getPersistentId());

    if (existing != null) {
      if (tc.getDomain() != existing.getDomain()) {
        result.setAction(ResourceUploadAction.UPDATE);
        result.setStatus(ResourceUploadResult.FAILURE);
        result.setMessage("You cannot change the TestCase(" + tc.getPersistentId()
            + ") domain from " + existing.getDomain() + " to " + tc.getDomain());
        return result;
      }
      Long exId = existing.getId();
      Set<TestStep> merged = this.mergeTS(tc.getTestSteps(), existing.getTestSteps());
      tc.setId(exId);
      tc.setDataMappings(existing.getDataMappings());
      tc.setTestSteps(merged);
      this.testCaseRepository.saveAndFlush(tc);
      result.setStatus(ResourceUploadResult.SUCCESS);
      return result;
    } else {
      result.setStatus(ResourceUploadResult.FAILURE);
      result.setMessage("TestCase(" + tc.getPersistentId() + ") not found");
      return result;
    }

  }

  @org.springframework.transaction.annotation.Transactional("transactionManager")
  public ResourceUploadStatus addTCG(Long parentId, TestCaseGroup tcg, String where) {

    ResourceUploadStatus result = new ResourceUploadStatus();
    result.setType(ResourceType.TESTCASEGROUP);
    result.setAction(ResourceUploadAction.ADD);
    result.setId(tcg.getPersistentId());

    if (where.toLowerCase().equals("plan")) {
      TestPlan tp = this.testPlanRepository.getByPersistentId(parentId);
      if (tp == null) {
        result.setStatus(ResourceUploadResult.FAILURE);
        result.setMessage("TestPlan(" + parentId + ") not found");
        return result;
      } else {

        if (tcg.getDomain() != tp.getDomain()) {
          result.setAction(ResourceUploadAction.UPDATE);
          result.setStatus(ResourceUploadResult.FAILURE);
          result.setMessage("TestPlan(" + parentId + ") and TestCaseGroup(" + tcg.getPersistentId()
              + ") cannot belong to different domain");
          return result;
        }

        tp.getTestCaseGroups().add(tcg);
        this.testPlanRepository.saveAndFlush(tp);
        result.setStatus(ResourceUploadResult.SUCCESS);
        return result;
      }
    } else if (where.toLowerCase().equals("group")) {
      TestCaseGroup tcgg = this.testCaseGroupRepository.getByPersistentId(parentId);
      if (tcgg == null) {
        result.setStatus(ResourceUploadResult.FAILURE);
        result.setMessage("TestCaseGroup(" + parentId + ") not found");
        return result;
      } else {
        if (tcg.getDomain() != tcgg.getDomain()) {
          result.setAction(ResourceUploadAction.UPDATE);
          result.setStatus(ResourceUploadResult.FAILURE);
          result.setMessage("TestCaseGroup' Parent (" + parentId + ") and TestCaseGroup("
              + tcg.getPersistentId() + ") cannot belong to different domain");
          return result;
        }

        tcgg.getTestCaseGroups().add(tcg);
        this.testCaseGroupRepository.saveAndFlush(tcgg);
        result.setStatus(ResourceUploadResult.SUCCESS);
        return result;
      }

    } else {
      return null;
    }
  }

  @org.springframework.transaction.annotation.Transactional("transactionManager")
  public ResourceUploadStatus updateTCG(TestCaseGroup tcg) {

    ResourceUploadStatus result = new ResourceUploadStatus();
    result.setType(ResourceType.TESTCASEGROUP);
    result.setAction(ResourceUploadAction.UPDATE);
    result.setId(tcg.getPersistentId());
    TestCaseGroup existing = this.testCaseGroupRepository.getByPersistentId(tcg.getPersistentId());
    if (existing != null) {
      if (tcg.getDomain() != existing.getDomain()) {
        result.setAction(ResourceUploadAction.UPDATE);
        result.setStatus(ResourceUploadResult.FAILURE);
        result.setMessage("You cannot change a Test Case Group domain from " + existing.getDomain()
            + " to " + tcg.getDomain());
        return result;
      }
      Long exId = existing.getId();
      Set<TestCase> mergedTc = this.mergeTC(tcg.getTestCases(), existing.getTestCases());
      Set<TestCaseGroup> mergedTcg =
          this.mergeTCG(tcg.getTestCaseGroups(), existing.getTestCaseGroups());
      tcg.setId(exId);
      tcg.setTestCases(mergedTc);
      tcg.setTestCaseGroups(mergedTcg);
      this.testCaseGroupRepository.saveAndFlush(tcg);
      result.setStatus(ResourceUploadResult.SUCCESS);
      return result;
    } else {

      result.setStatus(ResourceUploadResult.FAILURE);
      result.setMessage("TestCaseGroup(" + tcg.getPersistentId() + ") not found");
      return result;

    }
  }

  @org.springframework.transaction.annotation.Transactional("transactionManager")
  public ResourceUploadStatus handleTP(TestPlan tp) {

    ResourceUploadStatus result = new ResourceUploadStatus();
    result.setType(ResourceType.TESTPLAN);
    result.setId(tp.getPersistentId());
    TestPlan existing = this.testPlanRepository.getByPersistentId(tp.getPersistentId());

    if (existing != null) {
      if (!existing.getDomain().equals(tp.getDomain())) {
        result.setAction(ResourceUploadAction.UPDATE);
        result.setStatus(ResourceUploadResult.FAILURE);
        result.setMessage("You cannot change TestPlan(" + tp.getPersistentId() + ") domain from "
            + existing.getDomain() + " to " + tp.getDomain());
        return result;
      }
      Long exId = existing.getId();
      Set<TestCase> mergedTc = this.mergeTC(tp.getTestCases(), existing.getTestCases());
      Set<TestCaseGroup> mergedTcg =
          this.mergeTCG(tp.getTestCaseGroups(), existing.getTestCaseGroups());
      tp.setId(exId);
      tp.setTestCases(mergedTc);
      tp.setTestCaseGroups(mergedTcg);
      result.setAction(ResourceUploadAction.UPDATE);
    } else {
      result.setAction(ResourceUploadAction.ADD);
    }

    this.testPlanRepository.saveAndFlush(tp);
    result.setStatus(ResourceUploadResult.SUCCESS);
    return result;
  }

  // ---- Helper Functions
  // Creation Methods

  public List<TestStep> createTS(String rootPath, String domain, TestScope scope,
      String authorUsername, boolean preloaded) throws Exception {

    List<TestStep> tmp = new ArrayList<TestStep>();
    List<Resource> resources = getApiDirectories("*", rootPath);
    for (Resource resource : resources) {
      String fileName = resource.getFilename();
      TestStep testStep = testStep(fileName + "/", TestingStage.CB, false, rootPath, domain, scope,
          authorUsername, preloaded);
      if (testStep != null) {
        tmp.add(testStep);
      }
    }
    return tmp;
  }

  public List<TestCase> createTC(String rootPath, String domain, TestScope scope,
      String authorUsername, boolean preloaded) throws Exception {
    List<TestCase> tmp = new ArrayList<TestCase>();
    List<Resource> resources = getApiDirectories("*", rootPath);
    for (Resource resource : resources) {
      String fileName = resource.getFilename();
      TestCase testCase = testCase(fileName + "/", TestingStage.CB, false, rootPath, domain, scope,
          authorUsername, preloaded);
      if (testCase != null) {
        tmp.add(testCase);
      }
    }
    return tmp;
  }

  public List<TestCaseGroup> createTCG(String rootPath, String domain, TestScope scope,
      String authorUsername, boolean preloaded) throws Exception {
    List<TestCaseGroup> tmp = new ArrayList<TestCaseGroup>();
    List<Resource> resources = getApiDirectories("*", rootPath);
    for (Resource resource : resources) {
      String fileName = resource.getFilename();
      TestCaseGroup testCaseGroup = testCaseGroup(fileName + "/", TestingStage.CB, false, rootPath,
          domain, scope, authorUsername, preloaded);
      if (testCaseGroup != null) {
        tmp.add(testCaseGroup);
      }
    }
    return tmp;
  }

  public List<TestPlan> createTP(String rootPath, String domain, TestScope scope,
      String authorUsername, boolean preloaded) throws Exception {
    List<TestPlan> tmp = new ArrayList<TestPlan>();
    List<Resource> resources;
    try {
      resources = getApiDirectories("*", rootPath);
      for (Resource resource : resources) {
        String fileName = resource.getFilename();
        TestPlan testPlan = testPlan(fileName + "/", TestingStage.CB, rootPath, domain, scope,
            authorUsername, preloaded);
        if (testPlan != null) {
          tmp.add(testPlan);
        }
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return tmp;
  }

  public List<CFTestStep> createCFTC(String rootPath, String domain, TestScope scope,
      String authorUsername, boolean preloaded) throws Exception {

    List<CFTestStep> tmp = new ArrayList<CFTestStep>();
    List<Resource> resources = getApiDirectories("*", rootPath);
    for (Resource resource : resources) {
      String fileName = resource.getFilename();
      CFTestStep testObject =
          cfTestStep(fileName + "/", rootPath, domain, scope, authorUsername, preloaded);
      if (testObject != null) {
        tmp.add(testObject);
      }
    }
    return tmp;
  }

  // Merge Methods

  public Set<TestStep> mergeTS(Set<TestStep> newL, Set<TestStep> oldL) {
    int index = -1;
    List<TestStep> tmp = new ArrayList<TestStep>();
    tmp.addAll(oldL);

    for (TestStep tcs : newL) {

      if ((index = tmp.indexOf(tcs)) != -1) {
        tcs.setId(tmp.get(index).getId());
        if (tmp.get(index).getTestContext() != null) {
          tcs.getTestContext().setId(tmp.get(index).getTestContext().getId());
        }
        tmp.set(index, tcs);
      } else {
        tmp.add(tcs);
      }

    }

    return new HashSet<TestStep>(tmp);
  }

  public Set<TestCase> mergeTC(Set<TestCase> newL, Set<TestCase> oldL) {
    int index = -1;
    List<TestCase> tmp = new ArrayList<TestCase>();
    tmp.addAll(oldL);

    for (TestCase tcs : newL) {

      if ((index = tmp.indexOf(tcs)) != -1) {
        Set<TestStep> newLs = mergeTS(tcs.getTestSteps(), tmp.get(index).getTestSteps());
        tcs.setTestSteps(newLs);
        TestCase existing = tmp.get(index);
        tcs.setDataMappings(existing.getDataMappings());
        tcs.setId(existing.getId());
        tmp.set(index, tcs);
      } else {
        tmp.add(tcs);
      }

    }
    return new HashSet<TestCase>(tmp);
  }

  public Set<TestCaseGroup> mergeTCG(Set<TestCaseGroup> newL, Set<TestCaseGroup> oldL) {
    int index = -1;
    List<TestCaseGroup> tmp = new ArrayList<TestCaseGroup>();
    tmp.addAll(oldL);

    for (TestCaseGroup tcs : newL) {

      if ((index = tmp.indexOf(tcs)) != -1) {
        Set<TestCase> newLs = mergeTC(tcs.getTestCases(), tmp.get(index).getTestCases());
        tcs.setTestCases(newLs);
        if (tcs.getTestCaseGroups() != null && tcs.getTestCaseGroups().size() > 0) {
          Set<TestCaseGroup> newLsg =
              mergeTCG(tcs.getTestCaseGroups(), tmp.get(index).getTestCaseGroups());
          tcs.setTestCaseGroups(newLsg);
        }
        tcs.setId(tmp.get(index).getId());
        tmp.set(index, tcs);
      } else {
        tmp.add(tcs);
      }

    }
    return new HashSet<TestCaseGroup>(tmp);
  }

  public List<CFTestStep> mergeCFTC(List<CFTestStep> newL, List<CFTestStep> oldL) {
    int index = -1;
    List<CFTestStep> tmp = new ArrayList<CFTestStep>();
    tmp.addAll(oldL);

    for (CFTestStep tcs : newL) {

      if ((index = tmp.indexOf(tcs)) != -1) {
        tcs.setId(tmp.get(index).getId());
        tmp.set(index, tcs);
      } else
        tmp.add(tcs);
    }
    return tmp;
  }

  // Delete
  @org.springframework.transaction.annotation.Transactional("transactionManager")
  public ResourceUploadStatus deleteTS(Long id) {
    ResourceUploadStatus result = new ResourceUploadStatus();
    result.setType(ResourceType.TESTSTEP);
    result.setAction(ResourceUploadAction.DELETE);
    result.setId(id);
    TestStep s = this.testStepRepository.getByPersistentId(id);
    if (s == null) {
      result.setStatus(ResourceUploadResult.FAILURE);
      result.setMessage("TestStep(" + id + ") not found");
    } else {
      this.testStepRepository.delete(s.getId());
      result.setStatus(ResourceUploadResult.SUCCESS);
    }
    return result;
  }

  @org.springframework.transaction.annotation.Transactional("transactionManager")
  public ResourceUploadStatus deleteTC(Long id) {
    ResourceUploadStatus result = new ResourceUploadStatus();
    result.setType(ResourceType.TESTCASE);
    result.setAction(ResourceUploadAction.DELETE);
    result.setId(id);
    TestCase s = this.testCaseRepository.getByPersistentId(id);
    if (s == null) {
      result.setStatus(ResourceUploadResult.FAILURE);
      result.setMessage("TestCase(" + id + ") not found");
    } else {
      this.testCaseRepository.delete(s.getId());
      result.setStatus(ResourceUploadResult.SUCCESS);
    }
    return result;
  }

  @org.springframework.transaction.annotation.Transactional("transactionManager")
  public ResourceUploadStatus deleteTCG(Long id) {
    ResourceUploadStatus result = new ResourceUploadStatus();
    result.setType(ResourceType.TESTCASEGROUP);
    result.setAction(ResourceUploadAction.DELETE);
    result.setId(id);
    TestCaseGroup s = this.testCaseGroupRepository.getByPersistentId(id);
    if (s == null) {
      result.setStatus(ResourceUploadResult.FAILURE);
      result.setMessage("TestCaseGroup(" + id + ") not found");
    } else {
      this.testCaseGroupRepository.delete(s.getId());
      result.setStatus(ResourceUploadResult.SUCCESS);
    }
    return result;
  }

  @org.springframework.transaction.annotation.Transactional("transactionManager")
  public ResourceUploadStatus deleteTP(Long id) {
    ResourceUploadStatus result = new ResourceUploadStatus();
    result.setType(ResourceType.TESTPLAN);
    result.setAction(ResourceUploadAction.DELETE);
    result.setId(id);
    TestPlan s = this.testPlanRepository.getByPersistentId(id);
    if (s == null) {
      result.setStatus(ResourceUploadResult.FAILURE);
      result.setMessage("TestPlan(" + id + ") not found");
    } else {
      this.testPlanRepository.delete(s.getId());
      result.setStatus(ResourceUploadResult.SUCCESS);
    }
    return result;
  }

  @org.springframework.transaction.annotation.Transactional("transactionManager")
  public ResourceUploadStatus deleteCFTC(Long id) {
    ResourceUploadStatus result = new ResourceUploadStatus();
    result.setType(ResourceType.CFTESTCASE);
    result.setAction(ResourceUploadAction.DELETE);
    result.setId(id);
    CFTestStep s = this.cfTestStepRepository.getByPersistentId(id);
    if (s == null) {
      result.setStatus(ResourceUploadResult.FAILURE);
      result.setMessage("CF TestCase(" + id + ") not found");
    } else {
      this.cfTestStepRepository.delete(s.getId());
      result.setStatus(ResourceUploadResult.SUCCESS);
    }
    return result;
  }

  @org.springframework.transaction.annotation.Transactional("transactionManager")
  public void flush() {
    this.testStepRepository.flush();
    this.testCaseRepository.flush();
    this.testCaseGroupRepository.flush();
    this.testPlanRepository.flush();
  }

  public abstract List<ResourceUploadStatus> addOrReplaceValueSet(String rootPath, String domain,
      TestScope scope, String username, boolean preloaded) throws IOException;

  public abstract List<ResourceUploadStatus> addOrReplaceConstraints(String rootPath, String domain,
      TestScope scope, String username, boolean preloaded) throws IOException;

  public abstract List<ResourceUploadStatus> addOrReplaceIntegrationProfile(String rootPath,
      String domain, TestScope scope, String username, boolean preloaded) throws IOException;

}
