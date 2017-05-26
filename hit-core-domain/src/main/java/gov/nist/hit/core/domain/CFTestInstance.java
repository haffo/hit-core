package gov.nist.hit.core.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gov.nist.auth.hit.core.domain.TestingType;
import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(value = "CFTestInstance", description = "Data Model representing context-free test case")
public class CFTestInstance extends TestStep implements Serializable {

  private static final long serialVersionUID = 8805967508478985159L;

  @JsonIgnore
  private boolean root;

  public CFTestInstance() {
    super();
    this.type = ObjectType.TestObject;
    this.testingType = TestingType.DATAINSTANCE;
    this.stage = TestingStage.CF;
  }


  public CFTestInstance(String name) {
    super(name);
  }

  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinTable(name = "cf_tc", joinColumns = {@JoinColumn(name = "parent_id")},
      inverseJoinColumns = {@JoinColumn(name = "child_id")})
  private List<CFTestInstance> children = new ArrayList<CFTestInstance>();


  @Override
  public TestContext getTestContext() {
    return testContext;
  }

  @Override
  public void setTestContext(TestContext testContext) {
    this.testContext = testContext;
  }


  public List<CFTestInstance> getChildren() {
    return children;
  }

  public void setChildren(List<CFTestInstance> children) {
    this.children = children;
  }


  public boolean isRoot() {
    return root;
  }

  public void setRoot(boolean root) {
    this.root = root;
  }

  @Override
  public TestingType getTestingType() {
    return testingType;
  }

  @Override
  public void setTestingType(TestingType testingType) {
    this.testingType = testingType;
  }



}
