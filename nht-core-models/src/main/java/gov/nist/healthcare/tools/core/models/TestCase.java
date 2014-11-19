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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	protected String name;

	@Column(nullable = true)
	protected String label;

	@Embedded
	protected TestStory testStory;

	@OneToOne(mappedBy = "testCase", cascade = CascadeType.PERSIST)
	protected TestContext testContext;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	protected TestPlan testPlan;

	public TestCase() {
		super();
		testStory = new TestStory();
	}

	public void setName(String name) {
		this.name = name;
		this.label = name;
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public TestContext getTestContext() {
		return testContext;
	}

	public void setTestContext(TestContext testContext) {
		this.testContext = testContext;
		this.testContext.setTestCase(this);
	}

	public TestPlan getTestPlan() {
		return testPlan;
	}

	public void setTestPlan(TestPlan testPlan) {
		this.testPlan = testPlan;
	}

}
