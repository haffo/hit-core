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
public class DataInstanceTestPlan extends NewTestPlan implements Serializable {

	private static final long serialVersionUID = 8324105895492403037L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public DataInstanceTestPlan() {
		super();
		type = TestType.DataInstanceTestPlan;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
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

}
