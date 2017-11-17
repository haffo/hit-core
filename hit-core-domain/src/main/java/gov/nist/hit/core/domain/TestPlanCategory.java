package gov.nist.hit.core.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "TestPlanCategory",
    description = "Data Model representing a category of test plan")
@Entity
public class TestPlanCategory extends AbstractTestCase implements Serializable {

  private static final long serialVersionUID = 2555650104975908781L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  public TestPlanCategory() {
    super();
    this.type = ObjectType.TestPlanCategory;
  }

  @ApiModelProperty(required = false, value = "children test plan  of the test plan category")
  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinTable(name = "tpc_tp", joinColumns = {@JoinColumn(name = "parent_id")},
      inverseJoinColumns = {@JoinColumn(name = "child_id")})
  private Set<TestPlan> testPlans = new HashSet<TestPlan>();


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<TestPlan> getTestPlans() {
    return testPlans;
  }

  public void setTestPlans(Set<TestPlan> testPlans) {
    this.testPlans = testPlans;
  }

  public void addTestStep(TestPlan testPlan) {
    if (testPlan.getTestPlanCategory() != null) {
      throw new RuntimeException("Test plan belongs to a different test plan category");
    }
    getTestPlans().add(testPlan);
    testPlan.setTestPlanCategory(this);
  }



}
