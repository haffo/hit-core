/**
 * This software was developed at the National Institute of Standards and
 * Technology by employees of the Federal Government in the course of their
 * official duties. Pursuant to title 17 Section 105 of the United States Code
 * this software is not subject to copyright protection and is in the public
 * domain. This is an experimental system. NIST assumes no responsibility
 * whatsoever for its use by other parties, and makes no guarantees, expressed
 * or implied, about its quality, reliability, or any other characteristic. We
 * would appreciate acknowledgement if the software is used. This software can
 * be redistributed and/or modified freely provided that any derivative works
 * bear some notice that they are derived from it, and any modified versions
 * bear some notice that they have been modified.
 */

package gov.nist.healthcare.tools.core.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @(#) TestPlan.java
 */
@Entity
public class TestPlan implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@NotNull
	@Column(nullable = false)
	@JsonProperty("label")
	protected String name;

	@Column(nullable = true)
	protected String description;

	protected String testProcedurePath;

	@JsonProperty("children")
	@OrderBy("name ASC")
	@OneToMany(mappedBy = "testPlan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	protected Set<TestCase> testCases = new HashSet<TestCase>();

	transient protected final TestClassType type = TestClassType.TestPlan;

	public TestPlan() {
		super();
	}

	public TestPlan(String name) {
		setName(name);
	}

	public TestPlan(TestPlan testPlan) {
		setName(testPlan.getName());
		setDescription(testPlan.getDescription());
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id: ").append(getId()).append(", ");
		sb.append("Name: ").append(getName()).append(", ");
		// sb.append("Description: ").append(getDescription()).append(", ");
		sb.append("Description: ").append(getDescription()).append(", ");
		// sb.append("TestCases: ")
		// .append(getTestCases() == null ? "null" : getTestCases().size())
		// .append(", ");
		return sb.toString();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void addTestCase(TestCase testCase) {
		testCases.add(testCase);
		testCase.setTestPlan(this);
		testCase.setParentName(this.name);
	}

	public Set<TestCase> getTestCases() {
		return Collections.unmodifiableSet(testCases);
	}

	public String getTestProcedurePath() {
		return testProcedurePath;
	}

	public void setTestProcedurePath(String testProcedurePath) {
		this.testProcedurePath = testProcedurePath;
	}

	public TestClassType getType() {
		return type;
	}

}
