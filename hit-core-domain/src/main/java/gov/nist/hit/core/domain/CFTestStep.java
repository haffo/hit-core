package gov.nist.hit.core.domain;

import java.io.Serializable;

import javax.persistence.Entity;

import gov.nist.auth.hit.core.domain.TestingType;
import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(value = "CFTestStep", description = "Data Model representing context-free test case")
public class CFTestStep extends TestStep implements Serializable {

  private static final long serialVersionUID = 8805967508478985159L;

  public CFTestStep() {
    super();
    this.type = ObjectType.TestObject;
    this.testingType = TestingType.DATAINSTANCE;
    this.stage = TestingStage.CF;
    this.scope = TestScope.GLOBAL;
  }


  public CFTestStep(String name) {
    super(name);
  }

  // @LazyCollection(LazyCollectionOption.FALSE)
  // @OneToMany(cascade = {CascadeType.ALL})
  // @JoinTable(name = "cf_tc", joinColumns = {@JoinColumn(name = "parent_id")},
  // inverseJoinColumns = {@JoinColumn(name = "child_id")})
  // private List<CFTestStep> children = new ArrayList<CFTestStep>();
  //

  @Override
  public TestContext getTestContext() {
    return testContext;
  }

  @Override
  public void setTestContext(TestContext testContext) {
    this.testContext = testContext;
  }


  // public List<CFTestStep> getChildren() {
  // return children;
  // }
  //
  // public void setChildren(List<CFTestStep> children) {
  // this.children = children;
  // }
  //

  @Override
  public TestingType getTestingType() {
    return testingType;
  }

  @Override
  public void setTestingType(TestingType testingType) {
    this.testingType = testingType;
  }



}
