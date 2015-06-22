package gov.nist.hit.core.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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

@Entity
public class TestPlan  implements Serializable {

  private static final long serialVersionUID = 8324105895492403037L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id; 
  
  protected String name;
  protected String description;
  @Enumerated(EnumType.STRING)
  protected TestType type;
  @Enumerated(EnumType.STRING)
  protected TestCategory category; 
  @Enumerated(EnumType.STRING)
  protected Stage stage;

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(unique = true)
  protected TestArtifact testProcedure; 

  public TestPlan() {
    this.type = TestType.TestPlan;
    category = TestCategory.DataInstance;
  }
 
 
  
  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @JoinTable(name = "tp_tc", joinColumns = {@JoinColumn(name = "testplan_id")},
      inverseJoinColumns = {@JoinColumn(name = "testcase_id")})
  private Set<TestCase> testCases = new HashSet<TestCase>();

  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinTable(name = "tp_tcg", joinColumns = {@JoinColumn(name = "testplan_id")},
      inverseJoinColumns = {@JoinColumn(name = "testcasegroup_id")})
  private Set<TestCaseGroup> testCaseGroups = new HashSet<TestCaseGroup>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void addTestCase(TestCase testCase) {
    this.testCases.add(testCase);
  }

  public Set<TestCase> getTestCases() {
    return testCases;
  }

  public void setTestCases(Set<TestCase> testCases) {
    this.testCases = testCases;
  }

  public Set<TestCaseGroup> getTestCaseGroups() {
    return testCaseGroups;
  }

  public void setTestCaseGroups(Set<TestCaseGroup> testCaseGroups) {
    this.testCaseGroups = testCaseGroups;
  }

  public TestArtifact getTestProcedure() {
    return testProcedure;
  }

  public void setTestProcedure(TestArtifact testProcedure) {
    this.testProcedure = testProcedure;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TestType getType() {
    return type;
  }

  public void setType(TestType type) {
    this.type = type;
  }

  public TestCategory getCategory() {
    return category;
  }

  public void setCategory(TestCategory category) {
    this.category = category;
  }

  public Stage getStage() {
    return stage;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  
  
  

}
