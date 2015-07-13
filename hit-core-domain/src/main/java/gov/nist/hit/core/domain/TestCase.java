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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class TestCase extends AbstractTestCase implements Serializable {

  private static final long serialVersionUID = 8805967508478985159L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  
  public TestCase() {
    super();
    this.type = TestType.TestCase;
   }

  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinTable(name = "tc_ts", joinColumns = {@JoinColumn(name = "testcase_id")},
      inverseJoinColumns = {@JoinColumn(name = "teststep_id")})
  private Set<TestStep> testSteps = new HashSet<TestStep>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<TestStep> getTestSteps() {
    return testSteps;
  }

  public void setTestSteps(Set<TestStep> testSteps) {
    this.testSteps = testSteps;
  }

   
  public static long getSerialversionuid() {
    return serialVersionUID;
  }
 

  
}
