package gov.nist.hit.core.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@ApiModel(value = "TestPlan", description = "Data Model representing a test plan")
@Entity
public class TestPlan extends AbstractTestCase  implements Serializable {

  private static final long serialVersionUID = 8324105895492403037L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id; 
  
  @ApiModelProperty(required = false, value = "summary of the test plan")
  @JsonIgnoreProperties(value = {"html","json"})
  @OneToOne(cascade = CascadeType.ALL, optional = true, orphanRemoval=true)
  protected TestArtifact testPlanSummary; 

  @ApiModelProperty(required = false, value = "test package of the test plan")
  @JsonIgnoreProperties(value = {"html","json"})
  @OneToOne(cascade = CascadeType.ALL, optional = true, orphanRemoval=true)
  protected TestArtifact testPackage;
  

  public TestPlan() {
    this.type = ObjectType.TestPlan;
  }
  
  @ApiModelProperty(required = false, value = "list of test cases of the test plan")
  @OneToMany(orphanRemoval=true,fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @JoinTable(name = "tp_tc", joinColumns = {@JoinColumn(name = "testplan_id")},
      inverseJoinColumns = {@JoinColumn(name = "testcase_id")})
  private List<TestCase> testCases = new ArrayList<TestCase>();

  @ApiModelProperty(required = false, value = "list of test case groups of the test plan")
  @OneToMany(orphanRemoval=true,fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinTable(name = "tp_tcg", joinColumns = {@JoinColumn(name = "testplan_id")},
      inverseJoinColumns = {@JoinColumn(name = "testcasegroup_id")})
    private List<TestCaseGroup> testCaseGroups = new ArrayList<TestCaseGroup>();

  @ApiModelProperty(required = true, value = "transport support of the test plan")
  private boolean transport;
  
  @ApiModelProperty(required = true, value = "domain of the test plan", example="iz, erx, etc...")
  @Column(nullable = true)
  private String domain;
   
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void addTestCase(TestCase testCase) {
    this.testCases.add(testCase);
  }

  public List<TestCase> getTestCases() {
    return testCases;
  }

  public void setTestCases(List<TestCase> testCases) {
    this.testCases = testCases;
  }

  public List<TestCaseGroup> getTestCaseGroups() {
     return testCaseGroups;
  }

  public void setTestCaseGroups(List<TestCaseGroup> testCaseGroups) {
    this.testCaseGroups = testCaseGroups;
  }
  
  public boolean isTransport() {
    return transport;
  }

  public void setTransport(boolean transport) {
    this.transport = transport;
  }

  public TestArtifact getTestPlanSummary() {
    return testPlanSummary;
  }

  public void setTestPlanSummary(TestArtifact testPlanSummary) {
    this.testPlanSummary = testPlanSummary;
  }

  public TestArtifact getTestPackage() {
    return testPackage;
  }

  public void setTestPackage(TestArtifact testPackage) {
    this.testPackage = testPackage;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  
 

}
