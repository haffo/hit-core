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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CFTestObject  implements Serializable {

  private static final long serialVersionUID = 8805967508478985159L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id; 
  
  private String name; 
  
  private String description; 
  
  @JsonIgnore
  private boolean root;
  
  public CFTestObject() {
    super();
  }

  @OneToOne(cascade = CascadeType.ALL, optional = true, fetch = FetchType.EAGER,
      orphanRemoval = true)
  private TestContext testContext;

  public CFTestObject(String name) {
    super();
    this.name = name;
  } 
  
  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinTable(name = "cf_tc", joinColumns = {@JoinColumn(name = "parent_id")},
      inverseJoinColumns = {@JoinColumn(name = "child_id")})
  private Set<CFTestObject> children = new HashSet<CFTestObject>();

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<CFTestObject> getChildren() {
    return children;
  }

  public void setChildren(Set<CFTestObject> children) {
    this.children = children;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isRoot() {
    return root;
  }

  public void setRoot(boolean root) {
    this.root = root;
  } 
  
  
  

  
}
