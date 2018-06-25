/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */
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
import javax.persistence.OneToMany;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "CFTestStepGroup", description = "Data Model representing a group of test cases")
@Entity
public class CFTestStepGroup extends AbstractTestCase implements Serializable {

  private static final long serialVersionUID = 2555650104975908781L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;



  public CFTestStepGroup() {
    super();
    this.type = ObjectType.TestStepGroup;
    this.stage = TestingStage.CF;
    this.preloaded = false;
  }

  @ApiModelProperty(required = false, value = "test steps groups of the test step group")
  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true)
  @JoinTable(name = "teststepgroup_teststepgroupchildren",
      joinColumns = {@JoinColumn(name = "parent_teststepgroup_id")},
      inverseJoinColumns = {@JoinColumn(name = "teststepgroup_id")})
  private Set<CFTestStepGroup> testStepGroups = new HashSet<CFTestStepGroup>();

  @ApiModelProperty(required = false, value = "test steps of the test step group")
  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true)
  @JoinTable(name = "teststepgroup_teststeps",
      joinColumns = {@JoinColumn(name = "teststepgroup_id")},
      inverseJoinColumns = {@JoinColumn(name = "teststep_id")})
  private Set<CFTestStep> testSteps = new HashSet<CFTestStep>();


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
