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
public class IsolatedTestCase extends NewTestCase implements Serializable {

	private static final long serialVersionUID = 8805967508478985159L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	public IsolatedTestCase() {
		type = TestType.IsolatedTestCase;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinTable(name = "itc_its", joinColumns = { @JoinColumn(name = "testcase_id") }, inverseJoinColumns = { @JoinColumn(name = "teststep_id") })
	private Set<IsolatedTestStep> testSteps = new HashSet<IsolatedTestStep>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Set<IsolatedTestStep> getTestSteps() {
		return testSteps;
	}

	public void setTestSteps(Set<IsolatedTestStep> testSteps) {
		this.testSteps = testSteps;
	}

}
