package gov.nist.hit.core.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class TestStep extends CBTestObject implements Serializable {

  private static final long serialVersionUID = 8805967508478985159L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "s_actor_id")
  private Actor sActor;

  @ManyToOne
  @JoinColumn(name = "r_actor_id")
  private Actor rActor; 
  
  
  @OneToOne(cascade = CascadeType.PERSIST)
  protected TestArtifact messageContent;
 
  @OneToOne(cascade = CascadeType.PERSIST)
  protected TestArtifact testDataSpecification;
  
  @Enumerated(EnumType.STRING)
  private ConnectionType connectionType; 
  
  public TestStep() {
    super();
    this.type = TestType.TestStep;
    this.testContext = new TestContext();
    category = TestCategory.DataInstance;
  }

  @OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER,
      orphanRemoval = true)
  private TestContext testContext;

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

  public Actor getsActor() {
    return sActor;
  }

  public void setsActor(Actor sActor) {
    this.sActor = sActor;
  }

  public Actor getrActor() {
    return rActor;
  }

  public void setrActor(Actor rActor) {
    this.rActor = rActor;
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

  public ConnectionType getConnectionType() {
    return connectionType;
  }

  public void setConnectionType(ConnectionType connectionType) {
    this.connectionType = connectionType;
  }

 


}
