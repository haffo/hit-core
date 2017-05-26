package gov.nist.hit.core.domain;


import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class UserTestCaseGroup implements Serializable {

  private static final long serialVersionUID = 880596750847898513L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;



  public UserTestCaseGroup() {
    super();
  }

  @JsonIgnore
  @NotNull
  private Long userId;

  @NotNull
  private String name;
  private String description;

  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinTable(name = "gvt_tcg_tc",
      joinColumns = {@JoinColumn(name = "gvt_tcg_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "gvt_tc_id", referencedColumnName = "id")})

  @JsonProperty("children")
  private List<UserCFTestInstance> testCases;

  @JsonIgnore
  @NotNull
  private boolean preloaded;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

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

  public List<UserCFTestInstance> getTestCases() {
    return testCases;
  }

  public void setTestCases(List<UserCFTestInstance> testCases) {
    this.testCases = testCases;
  }

  public boolean isPreloaded() {
    return preloaded;
  }

  public void setPreloaded(boolean preloaded) {
    this.preloaded = preloaded;
  }

}
