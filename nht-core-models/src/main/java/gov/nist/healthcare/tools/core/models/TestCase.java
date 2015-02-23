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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @(#) TestPlan.java
 */
@Entity
public class TestCase implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	protected SutType sutType;

	@NotNull
	@Column(nullable = false)
	@JsonProperty("label")
	protected String name;

	protected String testType;

	protected String testPackagePath;

	transient protected TestClassType type = TestClassType.TestCase;

	protected int position;

	@Column(nullable = true)
	protected String parentName;

	@Column(columnDefinition = "TEXT")
	protected String instructionsText;

	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(length = 100000, nullable = true)
	protected byte[] instructionsImage;

	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(length = 100000, nullable = true)
	protected byte[] messageContentImage;

	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(length = 100000, nullable = true)
	protected byte[] testDataSpecificationImage;

	@Embedded
	protected TestStory testStory;

	@JsonIgnore
	@OneToOne(mappedBy = "testCase", cascade = CascadeType.PERSIST)
	protected TestCaseContext testContext;

	@JsonIgnoreProperties({ "id", "description", "testProcedurePath",
			"testCases", })
	@JsonIgnore
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	protected TestPlan testPlan;

	@JsonProperty("children")
	@OrderBy("name ASC")
	@OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	protected Set<TestStep> testSteps = new HashSet<TestStep>();

	public TestCase() {
		super();
		testStory = new TestStory();
	}

	public void setName(String name) {
		this.name = name;
	}

	public TestCase(String name) {
		setName(name);
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id: ").append(getId()).append(", ");
		sb.append("Name: ").append(getName()).append(", ");
		return sb.toString();
	}

	public TestStory getTestStory() {
		return testStory;
	}

	public void setTestStory(TestStory testStory) {
		this.testStory = testStory;
	}

	public SutType getSutType() {
		return sutType;
	}

	public void setSutType(SutType sutType) {
		this.sutType = sutType;
	}

	public TestCaseContext getTestContext() {
		return testContext;
	}

	public void setTestContext(TestCaseContext testContext) {
		this.testContext = testContext;
		this.testContext.setTestCase(this);
	}

	public TestPlan getTestPlan() {
		return testPlan;
	}

	public void setTestPlan(TestPlan testPlan) {
		this.testPlan = testPlan;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getTestPackagePath() {
		return testPackagePath;
	}

	public void setTestPackagePath(String testPackagePath) {
		this.testPackagePath = testPackagePath;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getInstructionsText() {
		return instructionsText;
	}

	public void setInstructionsText(String instructionsText) {
		this.instructionsText = instructionsText;
	}

	public byte[] getInstructionsImage() {
		return instructionsImage;
	}

	public void setInstructionsImage(byte[] instructionsImage) {
		this.instructionsImage = instructionsImage;
	}

	public byte[] getMessageContentImage() {
		return messageContentImage;
	}

	public void setMessageContentImage(byte[] messageContentImage) {
		this.messageContentImage = messageContentImage;
	}

	public byte[] getTestDataSpecificationImage() {
		return testDataSpecificationImage;
	}

	public void setTestDataSpecificationImage(byte[] testDataSpecificationImage) {
		this.testDataSpecificationImage = testDataSpecificationImage;
	}

	public Set<TestStep> getTestSteps() {
		return testSteps;
	}

	public void setTestSteps(Set<TestStep> testSteps) {
		this.testSteps = testSteps;
	}

	public void addTestStep(TestStep testStep) {
		testSteps.add(testStep);
		testStep.setTestCase(this);
		testStep.setParentName(this.name);
	}

	public TestClassType getType() {
		return type;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
