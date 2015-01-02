/**
 * This software was developed at the National Institute of Simport java.util.HashSet;
import java.util.List;
import java.util.Set;
e course of their official duties. Pursuant to title 17 Section 105 of the
 * United States Code this software is not subject to copyright protection and is in the public domain.
 * This is an experimental system. NIST assumes no responsibility whatsoever for its use by other parties,
 * and makes no guarantees, expressed or implied, about its quality, reliability, or any other characteristic.
 * We would appreciate acknowledgement if the software is used. This software can be redistributed and/or
 * modified freely provided that any derivative works bear some notice that they are derived from it, and any
 * modified versions bear some notice that they have been modified.
 */
package gov.nist.healthcare.tools.core.models;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author Harold Affo
 * 
 */

@Entity
public class VocabularyCollection {
	@NotNull
	@Column(nullable = false)
	protected String name;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@JsonIgnore
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
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

	@OneToMany(mappedBy = "vocabularyCollection", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	protected Set<VocabularyLibrary> vocabularyLibraries = new HashSet<VocabularyLibrary>();

	public String getName() {
		return name;
	}

	public VocabularyCollection() {
		super();
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public VocabularyCollection(String name) {
		super();
		this.name = name;
	}

	public VocabularyCollection(String name, Set<VocabularyLibrary> libraries) {
		super();
		this.name = name;
		if (libraries != null && !libraries.isEmpty()) {
			Iterator<VocabularyLibrary> it = libraries.iterator();
			while (it.hasNext()) {
				addVocabularyLibrary(it.next());
			}
		}
	}

	public void addVocabularyLibrary(VocabularyLibrary library) {
		vocabularyLibraries.add(library);
		library.setVocabularyCollection(this);
	}

	public Set<VocabularyLibrary> getVocabularyLibraries() {
		return vocabularyLibraries;
	}

	public void setVocabularyLibraries(
			Set<VocabularyLibrary> vocabularyLibraries) {
		this.vocabularyLibraries = vocabularyLibraries;
	}

	public TestCaseContext getTestContext() {
		return testContext;
	}

	public void setTestContext(TestCaseContext testContext) {
		this.testContext = testContext;
	}

}