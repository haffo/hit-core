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
public class DataInstanceTestCaseGroup extends TestCaseGroup implements
		Serializable {

	private static final long serialVersionUID = 2555650104975908781L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public DataInstanceTestCaseGroup() {
		super();
		type = TestType.DataInstanceTestCaseGroup;
	}

	public TestType getType() {
		return type;
	}

	public void setType(TestType type) {
		this.type = type;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinTable(name = "ditcg_ditc", joinColumns = { @JoinColumn(name = "testcasegroup_id") }, inverseJoinColumns = { @JoinColumn(name = "testcase_id") })
	private final List<DataInstanceTestCase> testCases = new ArrayList<DataInstanceTestCase>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<DataInstanceTestCase> getTestCases() {
		return testCases;
	}

}
