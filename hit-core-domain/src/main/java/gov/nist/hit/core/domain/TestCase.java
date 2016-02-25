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
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class TestCase extends AbstractTestCase implements Serializable {

  private static final long serialVersionUID = 8805967508478985159L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id; 
  
  @NotNull
  @Enumerated(EnumType.STRING)
  private TestCaseTestingType testingType;
  
  public TestCase() {
    super();
    this.type = ObjectType.TestCase;
    this.testingType = TestCaseTestingType.DATAINSTANCE;
   }

  @OneToMany(mappedBy = "testCase", orphanRemoval=true,fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  private List<TestStep> testSteps = new ArrayList<TestStep>();
  
  private String protocol;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<TestStep> getTestSteps() {
      return testSteps;
  }

  public void setTestSteps(List<TestStep> testSteps) {
    this.testSteps = testSteps;
  }

  public TestCaseTestingType getTestingType() {
    return testingType;
  }

  public void setTestingType(TestCaseTestingType testingType) {
    this.testingType = testingType;
  }

  
  
  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public void addTestStep(TestStep testStep){
    if(testStep.getTestCase() != null){
      throw new RuntimeException("Test step belongs to a different test case");
    } 
    getTestSteps().add(testStep);
    testStep.setTestCase(this);
  }
  

 
 

  
}
