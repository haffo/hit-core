package gov.nist.hit.core.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
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
import javax.persistence.OrderBy;

@Entity
public class TestPlan extends AbstractTestCase  implements Serializable {

  private static final long serialVersionUID = 8324105895492403037L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id; 
  
  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(unique = true)
  protected TestArtifact testProcedure; 

  public TestPlan() {
    this.type = ObjectType.TestPlan;
  }
  
  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @JoinTable(name = "tp_tc", joinColumns = {@JoinColumn(name = "testplan_id")},
      inverseJoinColumns = {@JoinColumn(name = "testcase_id")})
  private List<TestCase> testCases = new ArrayList<TestCase>();

  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinTable(name = "tp_tcg", joinColumns = {@JoinColumn(name = "testplan_id")},
      inverseJoinColumns = {@JoinColumn(name = "testcasegroup_id")})
    private List<TestCaseGroup> testCaseGroups = new ArrayList<TestCaseGroup>();

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

  public TestArtifact getTestProcedure() {
    return testProcedure;
  }

  public void setTestProcedure(TestArtifact testProcedure) {
    this.testProcedure = testProcedure;
  }

  
 

}
