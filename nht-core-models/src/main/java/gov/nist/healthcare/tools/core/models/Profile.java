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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Harold Affo (NIST)
 * 
 */

@Entity
public class Profile implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@JsonIgnore
	@OneToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(unique = true)
	protected TestCaseContext testContext;

	@JsonIgnore
	@OneToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(unique = true)
	protected TestStepContext testStepContext;

	public TestStepContext getTestStepContext() {
		return testStepContext;
	}

	public void setTestStepContext(TestStepContext testStepContext) {
		this.testStepContext = testStepContext;
	}

	@NotNull
	@Column(columnDefinition = "LONGTEXT")
	protected String xml;

	@NotNull
	@Column(columnDefinition = "LONGTEXT")
	protected String json;

	@Column(nullable = true)
	protected String name;

	@Column(nullable = true)
	protected String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Profile() {
		super();
	}

	public Profile(String name, String description, String xml) {
		super();
		this.name = name;
		this.description = description;
		this.xml = xml;
	}

	public Profile(String xml) {
		super();
		this.name = null;
		this.description = null;
		this.xml = xml;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public TestCaseContext getTestContext() {
		return testContext;
	}

	public void setTestContext(TestCaseContext testContext) {
		this.testContext = testContext;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

}
