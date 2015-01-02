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
package gov.nist.healthcare.tools.core.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class TestStep implements java.io.Serializable {

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

	@Column
	protected String dataSheetHtmlPath;

	@Column
	protected String dataSheetPdfPath;

	@Embedded
	protected TestStory testStory;

	@NotNull
	@Column(nullable = false)
	protected String parentName;

	@JsonIgnore
	@OneToOne(mappedBy = "testStep", cascade = CascadeType.PERSIST)
	protected TestStepContext testContext;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	protected TestCase testCase;

	public TestStep() {
		super();
		testStory = new TestStory();
	}

	public void setName(String name) {
		this.name = name;
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

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TestStepContext getTestContext() {
		return testContext;
	}

	public void setTestContext(TestStepContext testContext) {
		this.testContext = testContext;
	}

	public TestCase getTestCase() {
		return testCase;
	}

	public void setTestCase(TestCase testCase) {
		this.testCase = testCase;
	}

	public String getDataSheetHtmlPath() {
		return dataSheetHtmlPath;
	}

	public void setDataSheetHtmlPath(String dataSheetHtmlPath) {
		this.dataSheetHtmlPath = dataSheetHtmlPath;
	}

	public String getDataSheetPdfPath() {
		return dataSheetPdfPath;
	}

	public void setDataSheetPdfPath(String dataSheetPdfPath) {
		this.dataSheetPdfPath = dataSheetPdfPath;
	}

}
