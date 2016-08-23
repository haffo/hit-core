package gov.nist.hit.core.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ApiModel(value="TestContext", description="Data Model representing the context of a test")
public class TestContext implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  protected Long id;

  @ApiModelProperty(required = true, value = "format of the test context", example="hl7v2,edi,etc...")
  protected String format;

  @ApiModelProperty(required = false, value = "example message of the test context")
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(unique = true, nullable = true, insertable = true, updatable = true)
  protected Message message;

  @ApiModelProperty(required = false, value = "stage of the test context", example="CB,CF, etc...")
  @JsonIgnore
  @Enumerated(EnumType.STRING)
  protected TestingStage stage;


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

}
