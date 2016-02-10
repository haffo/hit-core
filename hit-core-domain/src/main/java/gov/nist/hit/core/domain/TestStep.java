package gov.nist.hit.core.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class TestStep extends AbstractTestCase implements Serializable {

  private static final long serialVersionUID = 8805967508478985159L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
 
  @NotNull
  @Enumerated(EnumType.STRING)
  protected TestStepTestingType testingType;  
  
  
  @JsonIgnore
  @OneToOne(cascade = CascadeType.ALL, optional = true, orphanRemoval=true)
  protected TestArtifact jurorDocument;

  @JsonIgnore
  @OneToOne(cascade = CascadeType.ALL, optional = true, orphanRemoval=true)
  protected TestArtifact messageContent;

  @JsonIgnore
  @OneToOne(cascade = CascadeType.ALL, optional = true, orphanRemoval=true)
  protected TestArtifact testDataSpecification;
  
  protected String protocol;
  
  public TestStep() {
    super();
    this.type = ObjectType.TestStep;
  }

  @OneToOne(cascade = CascadeType.ALL, optional = true, fetch = FetchType.EAGER,
      orphanRemoval = true)
  protected TestContext testContext;

  @JsonIgnore
  @ManyToOne(optional=true)
  protected TestCase testCase;
  
 
  public TestStep(String name) {
    super();
    this.name = name;
  }

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

  public TestStepTestingType getTestingType() {
    return testingType;
  }

  public void setTestingType(TestStepTestingType testingType) {
    this.testingType = testingType;
  }

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public TestCase getTestCase() {
    return testCase;
  }

  public void setTestCase(TestCase testCase) {
    this.testCase = testCase;
  }

  public TestArtifact getJurorDocument() {
    return jurorDocument;
  }

  public void setJurorDocument(TestArtifact jurorDocument) {
    this.jurorDocument = jurorDocument;
  }

  public TestArtifact getMessageContent() {
    return messageContent;
  }

  public void setMessageContent(TestArtifact messageContent) {
    this.messageContent = messageContent;
  }

  public TestArtifact getTestDataSpecification() {
    return testDataSpecification;
  }

  public void setTestDataSpecification(TestArtifact testDataSpecification) {
    this.testDataSpecification = testDataSpecification;
  }

  
  
 
  
 


}
