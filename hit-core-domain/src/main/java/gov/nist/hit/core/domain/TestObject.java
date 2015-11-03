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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class TestObject extends AbstractTestCase implements Serializable{

  private static final long serialVersionUID = 8805967508478985159L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id; 
   
  @JsonIgnore
  private boolean root;
  
  public TestObject() {
    super();
    this.type = ObjectType.TestObject;
    this.testingType = TestStepTestingType.DATAINSTANCE;
  } 
  
  @NotNull
  @Enumerated(EnumType.STRING)
  protected TestStepTestingType testingType;

  @OneToOne(cascade = CascadeType.ALL, optional = true, fetch = FetchType.EAGER,
      orphanRemoval = true)
  private TestContext testContext;

  public TestObject(String name) {
    super();
    this.name = name;
  } 
  
  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinTable(name = "cf_tc", joinColumns = {@JoinColumn(name = "parent_id")},
      inverseJoinColumns = {@JoinColumn(name = "child_id")})
   private List<TestObject> children = new ArrayList<TestObject>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public TestContext getTestContext() {
    return testContext;
  }

  public void setTestContext(TestContext testContext) {
    this.testContext = testContext;
  }

   
  public List<TestObject> getChildren() {
     return children;
  }

  public void setChildren(List<TestObject> children) {
    this.children = children;
  }

  
  public boolean isRoot() {
    return root;
  }

  public void setRoot(boolean root) {
    this.root = root;
  }

  public TestStepTestingType getTestingType() {
    return testingType;
  }

  public void setTestingType(TestStepTestingType testingType) {
    this.testingType = testingType;
  }

  
  
 
  
}
