/**
 * This software was developed at the National Institute of Standards and Technology by employees
 * of the Federal Government in the course of their official duties. Pursuant to title 17 Section 105 of the
 * United States Code this software is not subject to copyright protection and is in the public domain.
 * This is an experimental system. NIST assumes no responsibility whatsoever for its use by other parties,
 * and makes no guarantees, expressed or implied, about its quality, reliability, or any other characteristic.
 * We would appreciate acknowledgement if the software is used. This software can be redistributed and/or
 * modified freely provided that any derivative works bear some notice that they are derived from it, and any
 * modified versions bear some notice that they have been modified.
 */
package gov.nist.healthcare.tools.core.models.testcase;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @(#) TestPlan.java
 */
public class TestPlan {

	private String name;

	private TestStory testStory;

	@JsonManagedReference
	@JsonProperty("children")
	protected Set<TestCase> testCases;

	private String rootCaseName;

	public TestPlan() {
		super();
	}

	public TestPlan(TestPlan testPlan) {
		setName(testPlan.getName());
		setRootCaseName(testPlan.getRootCaseName());
		setTestStory(testPlan.getTestStory());
		setTestCases(testPlan.getTestCases());
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<TestCase> getTestCases() {
		return Collections.unmodifiableSet(this.testCases);
	}

	public String getRootCaseName() {
		return this.rootCaseName;
	}

	public void setRootCaseName(String rootCaseName) {
		this.rootCaseName = rootCaseName;
	}

	public TestStory getTestStory() {
		return testStory;
	}

	public void setTestStory(TestStory testStory) {
		this.testStory = testStory;
	}

	public void setTestCases(Set<TestCase> testCases) {
		this.testCases = testCases;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: ").append(getName()).append(", ");
		// sb.append("Description: ").append(getDescription()).append(", ");
		sb.append("TestStory: ").append(getTestStory()).append(", ");
		sb.append("TestCases: ")
				.append(getTestCases() == null ? "null" : getTestCases().size())
				.append(", ");
		sb.append("RootCaseName: ").append(getRootCaseName());
		return sb.toString();
	}

	public void addTestCase(TestCase testCase) {
		if (testCase == null)
			throw new NullPointerException("Can't add null Test Case");
		if (testCases == null)
			testCases = new HashSet<TestCase>();
		testCases.add(testCase);
	}

}
