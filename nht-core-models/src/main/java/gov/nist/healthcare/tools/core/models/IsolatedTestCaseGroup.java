package gov.nist.healthcare.tools.core.models;

import java.io.Serializable;
import java.util.ArrayList;
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
	private List<IsolatedTestCase> testCases = new ArrayList<IsolatedTestCase>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<IsolatedTestCase> getTestCases() {
		return testCases;
	}

	public void setTestCases(List<IsolatedTestCase> testCases) {
		this.testCases = testCases;
	}

	public void addTestCase(IsolatedTestCase testcase) {
		this.testCases.add(testcase);
	}

}
