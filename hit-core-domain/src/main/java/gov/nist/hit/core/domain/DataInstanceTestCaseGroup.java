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
public class DataInstanceTestCaseGroup extends TestObject implements
		Serializable {

	private static final long serialVersionUID = 2555650104975908781L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public DataInstanceTestCaseGroup() {
		super();
		this.type = TestType.TestCaseGroup;
		category = TestCategory.DataInstance;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinTable(name = "ditcg_ditc", joinColumns = { @JoinColumn(name = "testcasegroup_id") }, inverseJoinColumns = { @JoinColumn(name = "testcase_id") })
	private Set<DataInstanceTestCase> testCases = new HashSet<DataInstanceTestCase>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<DataInstanceTestCase> getTestCases() {
		return testCases;
	}

	public void setTestCases(Set<DataInstanceTestCase> testCases) {
		this.testCases = testCases;
	}

	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = super.hashCode();
	// result = prime * result
	// + ((testCases == null) ? 0 : testCases.hashCode());
	// return result;
	// }
	//
	// @Override
	// public boolean equals(Object obj) {
	// if (this == obj)
	// return true;
	// if (!super.equals(obj))
	// return false;
	// if (getClass() != obj.getClass())
	// return false;
	// DataInstanceTestCaseGroup other = (DataInstanceTestCaseGroup) obj;
	// if (testCases == null) {
	// if (other.testCases != null)
	// return false;
	// } else if (!testCases.equals(other.testCases))
	// return false;
	// return true;
	// }

}
