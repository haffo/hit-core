package gov.nist.hit.core.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author haffo
 * 
 */
@MappedSuperclass
public abstract class AbstractTestCase implements Comparable<AbstractTestCase> {

  @ApiModelProperty(required = true, value = "name of the test case")
  @Column(columnDefinition = "TEXT")
  protected String name;

  @NotNull
  @Column(unique = true, nullable = false)
  protected Long persistentId;

  @JsonIgnore
  @Column(nullable = true)
  protected String authorUsername;


  @ApiModelProperty(required = false, value = "description of the test")
  @Column(columnDefinition = "TEXT")
  protected String description;

  @ApiModelProperty(required = true, value = "type of the test")
  @NotNull
  @Enumerated(EnumType.STRING)
  protected ObjectType type;

  @ApiModelProperty(required = false, value = "stage of the test")
  @Enumerated(EnumType.STRING)
  protected TestingStage stage;


  @ApiModelProperty(required = true, value = "position of the test")
  @Min(1)
  protected int position;

  @ApiModelProperty(required = true, value = "version of the test")
  protected Double version;

  @ApiModelProperty(required = true, value = "test story of the test case")
  @JsonIgnore

  @OneToOne(cascade = CascadeType.ALL, optional = true, orphanRemoval = true)
  protected TestArtifact testStory;


  @ApiModelProperty(required = true, value = "Supplement documents")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  protected Set<Document> supplements = new HashSet<Document>();

  @NotNull
  @Enumerated(EnumType.STRING)
  protected TestScope scope = TestScope.GLOBAL;

  protected boolean preloaded = true;

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

  public ObjectType getType() {
    return type;
  }

  public void setType(ObjectType type) {
    this.type = type;
  }


  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public TestingStage getStage() {
    return stage;
  }

  public void setStage(TestingStage stage) {
    this.stage = stage;
  }

  public Double getVersion() {
    return version;
  }

  public void setVersion(Double version) {
    this.version = version;
  }

  public TestArtifact getTestStory() {
    return testStory;
  }

  public void setTestStory(TestArtifact testStory) {
    this.testStory = testStory;
  }


  // public String getParentName() {
  // return parentName;
  // }
  //
  // public void setParentName(String parentName) {
  // this.parentName = parentName;
  // }
  //
  // public String getTestCaseName() {
  // return testCaseName;
  // }
  //
  // public void setTestCaseName(String testCaseName) {
  // this.testCaseName = testCaseName;
  // }
  //
  // public String getTestPlanName() {
  // return testPlanName;
  // }
  //
  // public void setTestPlanName(String testPlanName) {
  // this.testPlanName = testPlanName;
  // }
  //
  // public String getTestCaseGroupName() {
  // return testCaseGroupName;
  // }
  //
  // public void setTestCaseGroupName(String testCaseGroupName) {
  // this.testCaseGroupName = testCaseGroupName;
  // }
  //
  // public String getTestStepName() {
  // return testStepName;
  // }
  //
  // public void setTestStepName(String testStepName) {
  // this.testStepName = testStepName;
  // }

  @Override
  public int compareTo(AbstractTestCase o) {
    // TODO Auto-generated method stub
    return this.getPosition() - o.getPosition();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AbstractTestCase other = (AbstractTestCase) obj;
    if (!this.getPersistentId().equals(other.getPersistentId()))
      return false;
    return true;
  }

  public Long getPersistentId() {
    return persistentId;
  }

  public void setPersistentId(Long persistentId) {
    this.persistentId = persistentId;
  }

  public Set<Document> getSupplements() {
    return supplements;
  }

  public void setSupplements(Set<Document> supplements) {
    this.supplements = supplements;
  }

  public String getAuthorUsername() {
    return authorUsername;
  }

  public void setAuthorUsername(String authorUsername) {
    this.authorUsername = authorUsername;
  }

  public TestScope getScope() {
    return scope;
  }

  public void setScope(TestScope scope) {
    this.scope = scope;
  }

  public boolean isPreloaded() {
    return preloaded;
  }

  public void setPreloaded(boolean preloaded) {
    this.preloaded = preloaded;
  }



}
