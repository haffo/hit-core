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
public class DataInstanceTestPlan extends TestObject implements Serializable {

	private static final long serialVersionUID = 8324105895492403037L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public DataInstanceTestPlan() {
		super();
		this.type = TestType.TestPlan;
		category = TestCategory.DataInstance;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST,
			CascadeType.REMOVE })
	@JoinTable(name = "ditp_ditc", joinColumns = { @JoinColumn(name = "testplan_id") }, inverseJoinColumns = { @JoinColumn(name = "testcase_id") })
	private Set<DataInstanceTestCase> testCases = new HashSet<DataInstanceTestCase>();

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinTable(name = "ditp_ditcg", joinColumns = { @JoinColumn(name = "testplan_id") }, inverseJoinColumns = { @JoinColumn(name = "testcasegroup_id") })
	private Set<DataInstanceTestCaseGroup> testCaseGroups = new HashSet<DataInstanceTestCaseGroup>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void addTestCase(DataInstanceTestCase testCase) {
		this.testCases.add(testCase);
	}

	public Set<DataInstanceTestCase> getTestCases() {
		return testCases;
	}

	public void setTestCases(Set<DataInstanceTestCase> testCases) {
		this.testCases = testCases;
	}

	public Set<DataInstanceTestCaseGroup> getTestCaseGroups() {
		return testCaseGroups;
	}

	public void setTestCaseGroups(Set<DataInstanceTestCaseGroup> testCaseGroups) {
		this.testCaseGroups = testCaseGroups;
	}

	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = super.hashCode();
	// result = prime * result
	// + ((testCaseGroups == null) ? 0 : testCaseGroups.hashCode());
	// // result = prime * result
	// // + ((testCases == null) ? 0 : testCases.hashCode());
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
	// DataInstanceTestPlan other = (DataInstanceTestPlan) obj;
	// if (testCaseGroups == null) {
	// if (other.testCaseGroups != null)
	// return false;
	// } else if (!testCaseGroups.equals(other.testCaseGroups))
	// return false;
	// // if (testCases == null) {
	// // if (other.testCases != null)
	// // return false;
	// // } else if (!testCases.equals(other.testCases))
	// // return false;
	// return true;
	// }

}
