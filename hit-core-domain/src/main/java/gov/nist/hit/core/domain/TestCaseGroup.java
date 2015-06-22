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

@Entity
public class TestCaseGroup implements Serializable {

  private static final long serialVersionUID = 2555650104975908781L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  
  protected String name;
  protected String description;
  @Enumerated(EnumType.STRING)
  protected TestType type;
  @Enumerated(EnumType.STRING)
  protected TestCategory category;
 

  public TestCaseGroup() {
    super();
    this.type = TestType.TestCaseGroup;
    category = TestCategory.DataInstance;
  }

  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinTable(name = "tcg_tc", joinColumns = {@JoinColumn(name = "testcasegroup_id")},
      inverseJoinColumns = {@JoinColumn(name = "testcase_id")})
  private Set<TestCase> testCases = new HashSet<TestCase>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<TestCase> getTestCases() {
    return testCases;
  }

  public void setTestCases(Set<TestCase> testCases) {
    this.testCases = testCases;
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

 

}
