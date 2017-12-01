package gov.nist.hit.core.domain;

import java.util.Set;

public class TestCaseWrapper {

  private String testcasename;
  private String testcasedescription;
  private String token;
  private Long groupId;
  private String scope;
  private String category;
  private int position = -1;
  private Set<UploadedProfileModel> added;
  private Set<UploadedProfileModel> removed;
  private Set<UploadedProfileModel> updated;


  public TestCaseWrapper() {
    super();
  }


  public TestCaseWrapper(Set<UploadedProfileModel> added, String testcasename,
      String testcasedescription) {
    super();
    this.added = added;
    this.testcasename = testcasename;
    this.testcasedescription = testcasedescription;
  }



  public TestCaseWrapper(Set<UploadedProfileModel> added, String testcasename,
      String testcasedescription, String token, String scope) {
    super();
    this.added = added;
    this.testcasename = testcasename;
    this.testcasedescription = testcasedescription;
    this.token = token;
    this.scope = scope;
  }



  public TestCaseWrapper(Set<UploadedProfileModel> added, String testcasename,
      String testcasedescription, String token, Long groupId, String scope) {
    super();
    this.added = added;
    this.testcasename = testcasename;
    this.testcasedescription = testcasedescription;
    this.token = token;
    this.groupId = groupId;
    this.scope = scope;
  }


  public String getTestcasename() {
    return testcasename;
  }

  public void setTestcasename(String testcasename) {
    this.testcasename = testcasename;
  }

  public String getTestcasedescription() {
    return testcasedescription;
  }

  public void setTestcasedescription(String testcasedescription) {
    this.testcasedescription = testcasedescription;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }


  public String getScope() {
    return scope;
  }


  public void setScope(String scope) {
    this.scope = scope;
  }


  public String getCategory() {
    return category;
  }


  public void setCategory(String category) {
    this.category = category;
  }


  public int getPosition() {
    return position;
  }


  public void setPosition(int position) {
    this.position = position;
  }


  public Set<UploadedProfileModel> getRemoved() {
    return removed;
  }


  public void setRemoved(Set<UploadedProfileModel> removed) {
    this.removed = removed;
  }


  public Set<UploadedProfileModel> getUpdated() {
    return updated;
  }


  public void setUpdated(Set<UploadedProfileModel> updated) {
    this.updated = updated;
  }


  public Set<UploadedProfileModel> getAdded() {
    return added;
  }


  public void setAdded(Set<UploadedProfileModel> added) {
    this.added = added;
  }



}
