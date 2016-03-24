package gov.nist.hit.core.domain;

import io.swagger.annotations.ApiModel;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@ApiModel(value="CFTestInstance", description="Data Model representing context-free test case")
public class CFTestInstance extends TestStep implements Serializable {

  private static final long serialVersionUID = 8805967508478985159L;

  @JsonIgnore
  private boolean root;

  public CFTestInstance() {
    super();
    this.type = ObjectType.TestObject;
    this.testingType = TestingType.DATAINSTANCE;
  }


  public CFTestInstance(String name) {
    super(name);
  }

  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinTable(name = "cf_tc", joinColumns = {@JoinColumn(name = "parent_id")},
      inverseJoinColumns = {@JoinColumn(name = "child_id")})
  private List<CFTestInstance> children = new ArrayList<CFTestInstance>();


  public TestContext getTestContext() {
    return testContext;
  }

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

  public TestingType getTestingType() {
    return testingType;
  }

  public void setTestingType(TestingType testingType) {
    this.testingType = testingType;
  }



}
