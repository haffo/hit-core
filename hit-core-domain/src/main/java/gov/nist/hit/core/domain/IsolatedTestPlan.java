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

@Entity
public class IsolatedTestPlan extends TestObject implements Serializable {

	private static final long serialVersionUID = 8324105895492403037L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinTable(name = "itp_itc", joinColumns = { @JoinColumn(name = "testplan_id") }, inverseJoinColumns = { @JoinColumn(name = "testcase_id") })
	private Set<IsolatedTestCase> testCases = new HashSet<IsolatedTestCase>();

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinTable(name = "itp_itcg", joinColumns = { @JoinColumn(name = "testplan_id") }, inverseJoinColumns = { @JoinColumn(name = "testcasegroup_id") })
	private Set<IsolatedTestCaseGroup> testCaseGroups = new HashSet<IsolatedTestCaseGroup>();

	public IsolatedTestPlan() {
		super();
		category = TestCategory.Isolated;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void addTestCase(IsolatedTestCase testcase) {
		this.testCases.add(testcase);
	}

	public Set<IsolatedTestCase> getTestCases() {
		return testCases;
	}

	public void setTestCases(Set<IsolatedTestCase> testCases) {
		this.testCases = testCases;
	}

	public Set<IsolatedTestCaseGroup> getTestCaseGroups() {
		return testCaseGroups;
	}

	public void setTestCaseGroups(Set<IsolatedTestCaseGroup> testCaseGroups) {
		this.testCaseGroups = testCaseGroups;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((testCaseGroups == null) ? 0 : testCaseGroups.hashCode());
		result = prime * result
				+ ((testCases == null) ? 0 : testCases.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		IsolatedTestPlan other = (IsolatedTestPlan) obj;
		if (testCaseGroups == null) {
			if (other.testCaseGroups != null)
				return false;
		} else if (!testCaseGroups.equals(other.testCaseGroups))
			return false;
		if (testCases == null) {
			if (other.testCases != null)
				return false;
		} else if (!testCases.equals(other.testCases))
			return false;
		return true;
	}

}
