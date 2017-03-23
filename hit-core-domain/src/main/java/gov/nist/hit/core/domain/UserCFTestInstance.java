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
@ApiModel(value="UserCFTestInstance", description="Data Model representing  user context-free test case")
public class UserCFTestInstance extends TestStep implements Serializable {

  private static final long serialVersionUID = 880596750847898512L;

  @JsonIgnore
  private boolean root;
  
  public UserCFTestInstance() {
	    super();
	    this.type = ObjectType.TestObject;
	    this.testingType = TestingType.DATAINSTANCE;
	  }

  public UserCFTestInstance(String name) {
    super(name);
  }



  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinTable(name = "cf_tc", joinColumns = {@JoinColumn(name = "parent_id")},
      inverseJoinColumns = {@JoinColumn(name = "child_id")})
  private List<UserCFTestInstance> children = new ArrayList<UserCFTestInstance>();


  public TestContext getTestContext() {
    return testContext;
  }

  public void setTestContext(TestContext testContext) {
    this.testContext = testContext;
  }


  public List<UserCFTestInstance> getChildren() {
    return children;
  }

  public void setChildren(List<UserCFTestInstance> children) {
    this.children = children;
  }


  public boolean isRoot() {
    return root;
  }

  public void setRoot(boolean root) {
    this.root = root;
  }

  public TestingType getTestingType() {
    return testingType;
  }

  public void setTestingType(TestingType testingType) {
    this.testingType = testingType;
  }



}
