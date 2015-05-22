package gov.nist.healthcare.tools.core.models;

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

@Entity
public class DataInstanceTestCase extends TestObject implements Serializable {

	private static final long serialVersionUID = 8805967508478985159L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public DataInstanceTestCase() {
		super();
		this.type = TestType.TestCase;
		category = TestCategory.DataInstance;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinTable(name = "dtc_dts", joinColumns = { @JoinColumn(name = "testcase_id") }, inverseJoinColumns = { @JoinColumn(name = "teststep_id") })
	private Set<DataInstanceTestStep> testSteps = new HashSet<DataInstanceTestStep>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<DataInstanceTestStep> getTestSteps() {
		return testSteps;
	}

	public void setTestSteps(Set<DataInstanceTestStep> testSteps) {
		this.testSteps = testSteps;
	}

}
