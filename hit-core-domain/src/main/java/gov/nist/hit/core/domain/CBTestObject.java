package gov.nist.hit.core.domain;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

//@Entity
//@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)

@MappedSuperclass
public abstract class CBTestObject {

  protected String name;
  protected String description;
  @Enumerated(EnumType.STRING)
  protected TestType type;
  @Enumerated(EnumType.STRING)
  protected TestCategory category;
  @Enumerated(EnumType.STRING)
  protected Stage stage;
  protected int position;
   

  @OneToOne(cascade = CascadeType.PERSIST)
  protected TestArtifact testPackage; 
  

  @OneToOne(cascade = CascadeType.PERSIST)
  protected TestStory TestStory; 
  
 
  @OneToOne(cascade = CascadeType.PERSIST)
  protected TestArtifact jurorDocument;
  
  
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

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public Stage getStage() {
    return stage;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public TestStory getTestStory() {
    return TestStory;
  }

  public void setTestStory(TestStory testStory) {
    TestStory = testStory;
  }

  public TestArtifact getTestPackage() {
    return testPackage;
  }

  public void setTestPackage(TestArtifact testPackage) {
    this.testPackage = testPackage;
  }

  public TestArtifact getJurorDocument() {
    return jurorDocument;
  }

  public void setJurorDocument(TestArtifact jurorDocument) {
    this.jurorDocument = jurorDocument;
  }

  
  

}
