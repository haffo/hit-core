package gov.nist.hit.core.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.*;

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

@ApiModel(value = "TestCase", description = "Data Model representing a test case")
@Entity
public class TestCase extends AbstractTestCase implements Serializable {

  private static final long serialVersionUID = 8805967508478985159L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  public TestCase() {
    super();
    this.type = ObjectType.TestCase;
  }

  @ApiModelProperty(required = true, value = "test steps of the test case")
  @OneToMany(mappedBy = "testCase", orphanRemoval = true, fetch = FetchType.EAGER,
      cascade = {CascadeType.ALL})
  private List<TestStep> testSteps = new ArrayList<TestStep>();

  @ApiModelProperty(required = false, value = "mapping of data of the test case")
  @OneToMany(mappedBy = "testCase", orphanRemoval = true, fetch = FetchType.EAGER,
      cascade = {CascadeType.ALL})
  protected Collection<DataMapping> dataMappings;

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



  public void addTestStep(TestStep testStep) {
    if (testStep.getTestCase() != null) {
      throw new RuntimeException("Test step belongs to a different test case");
    }
    getTestSteps().add(testStep);
    testStep.setTestCase(this);
  }

  public Collection<DataMapping> getDataMappings() {
    return dataMappings;
  }

  public void setDataMappings(Collection<DataMapping> dataMappings) {
    this.dataMappings = dataMappings;
  }



}
