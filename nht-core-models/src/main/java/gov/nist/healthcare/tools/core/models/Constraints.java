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
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author Harold Affo
 * 
 */
@Entity
public class Constraints {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@NotNull
	@Column(columnDefinition = "TEXT", nullable = false)
	protected String content;

	@JsonIgnore
	@OneToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	protected TestCaseContext testContext;

	@JsonIgnore
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	protected TestStepContext testStepContext;

	public TestStepContext getTestStepContext() {
		return testStepContext;
	}

	public void setTestStepContext(TestStepContext testStepContext) {
		this.testStepContext = testStepContext;
	}

	public Constraints() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public Constraints(String content) {
		super();
		this.content = content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public TestCaseContext getTestContext() {
		return testContext;
	}

	public void setTestContext(TestCaseContext testContext) {
		this.testContext = testContext;
	}

}
