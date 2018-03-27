package gov.nist.hit.core.domain;

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
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ApiModel(value = "TestContext", description = "Data Model representing the context of a test")
public class TestContext extends DomainBased {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  protected Long id;

  @ApiModelProperty(required = true, value = "format of the test context",
      example = "hl7v2,edi,etc...")
  protected String format;

  @ApiModelProperty(required = false, value = "example message of the test context")
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(unique = true, nullable = true, insertable = true, updatable = true)
  protected Message message;

  @ApiModelProperty(required = false, value = "stage of the test context",
      example = "CB,CF, etc...")
  @JsonIgnore

  @Enumerated(EnumType.STRING)
  protected TestingStage stage;

  @ApiModelProperty(required = false, value = "message ID of the test context",
      example = "PSDI_08,NEWRX,etc...")
  protected String type;

  @JsonIgnore
  @OneToOne(fetch = FetchType.EAGER, optional = false, mappedBy = "testContext")
  protected TestStep testStep;

  public TestContext() {}

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public Message getMessage() {
    return message;
  }

  public void setMessage(Message message) {
    this.message = message;
  }

  public TestingStage getStage() {
    return stage;
  }

  public void setStage(TestingStage stage) {
    this.stage = stage;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public TestStep getTestStep() {
    return testStep;
  }

  public void setTestStep(TestStep testStep) {
    this.testStep = testStep;
  }
}
