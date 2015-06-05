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
public class IsolatedTestCaseGroup extends TestObject implements Serializable {

	private static final long serialVersionUID = 2555650104975908781L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	public IsolatedTestCaseGroup() {
		super();
		category = TestCategory.Isolated;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinTable(name = "itcg_itc", joinColumns = { @JoinColumn(name = "testcasegroup_id") }, inverseJoinColumns = { @JoinColumn(name = "testcase_id") })
	private Set<IsolatedTestCase> testCases = new HashSet<IsolatedTestCase>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Set<IsolatedTestCase> getTestCases() {
		return testCases;
	}

	public void setTestCases(Set<IsolatedTestCase> testCases) {
		this.testCases = testCases;
	}

	public void addTestCase(IsolatedTestCase testcase) {
		this.testCases.add(testcase);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
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
		IsolatedTestCaseGroup other = (IsolatedTestCaseGroup) obj;
		if (testCases == null) {
			if (other.testCases != null)
				return false;
		} else if (!testCases.equals(other.testCases))
			return false;
		return true;
	}

}
