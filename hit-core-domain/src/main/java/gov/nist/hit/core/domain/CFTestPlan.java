package gov.nist.hit.core.domain;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

@Entity
public class CFTestPlan extends AbstractTestCase implements Serializable {

  private static final long serialVersionUID = 880596750847898513L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ApiModelProperty(required = true, value = "category of the test plan",
      example = "CDC, AIRA etc...")
  @Column(nullable = false)
  private String category;

  public CFTestPlan() {
    super();
    this.type = ObjectType.TestPlan;
    this.stage = TestingStage.CF;
  }

  public CFTestPlan(Long id, String name, String description, int position, Long persistentId) {
    super();
    this.id = id;
    this.name = name;
    this.description = description;
    this.position = position;
    this.persistentId = persistentId;
  }

  public CFTestPlan(Long id, String name, String description, int position, Long persistentId,
      String category) {
    super();
    this.id = id;
    this.name = name;
    this.description = description;
    this.position = position;
    this.persistentId = persistentId;
    this.category = category;
  }

  public CFTestPlan(String category) {
    super();
    this.id = null;
    this.name = null;
    this.description = null;
    this.position = 0;
    this.persistentId = null;
    this.category = category;
  }


  @ApiModelProperty(required = false, value = "list of test steps of the test plan")
  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true)
  @JoinTable(name = "cf_tcg_tc",
      joinColumns = {@JoinColumn(name = "cf_tcg_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "cf_tc_id", referencedColumnName = "id")})
  @JsonProperty("children")
  private Set<CFTestStep> testSteps = new HashSet<CFTestStep>();


  @ApiModelProperty(required = false, value = "list of test steps groups of the test plan")
  @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinTable(name = "tp_tcg",
      joinColumns = {@JoinColumn(name = "testplan_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "teststepgroup_id", referencedColumnName = "id")})
  private Set<CFTestStepGroup> testStepGroups = new HashSet<CFTestStepGroup>();



  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }



  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }



  public Set<CFTestStep> getTestSteps() {
    return this.testSteps;
  }


  public Set<CFTestStepGroup> getTestStepGroups() {
    return this.testStepGroups;
  }


  public void setTestSteps(Set<CFTestStep> testSteps) {
    this.testSteps = testSteps;
  }


  public void setTestStepGroups(Set<CFTestStepGroup> testStepGroups) {
    this.testStepGroups = testStepGroups;
  }



}
