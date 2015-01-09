package gov.nist.healthcare.tools.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ContextBasedTestStepContext extends TestStepContext {

	protected static final long serialVersionUID = 1L;

	@OneToOne(optional = true, mappedBy = "testStepContext", cascade = CascadeType.ALL)
	protected Profile profile;

	@JsonIgnore
	@OneToMany(mappedBy = "testStepContext", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	protected List<TableLibrary> tableLibraries = new ArrayList<TableLibrary>();

	@OneToMany(mappedBy = "testStepContext", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	protected Set<VocabularyCollection> vocabularyCollections = new HashSet<VocabularyCollection>();

	@JsonIgnore
	@OneToOne(optional = true, mappedBy = "testStepContext", cascade = CascadeType.ALL)
	protected ValidationContext validationContext;

	@OneToOne(optional = true, mappedBy = "testStepContext", cascade = CascadeType.ALL)
	protected Message message;

	public ValidationContext getValidationContext() {
		return validationContext;
	}

	public void setValidationContext(ValidationContext validationContext) {
		this.validationContext = validationContext;
		this.validationContext.setTestStepContext(this);
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
		this.profile.setTestStepContext(this);
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
		this.message.setTestStepContext(this);
	}

	public Profile getProfile() {
		return profile;
	}

	public List<TableLibrary> getTableLibraries() {
		return (List<TableLibrary>) Collections
				.unmodifiableCollection(tableLibraries);
	}

	public void addTableLibrary(TableLibrary tableLibrary) {
		tableLibraries.add(tableLibrary);
		tableLibrary.setTestStepContext(this);
	}

	public Set<VocabularyCollection> getVocabularyCollections() {
		return Collections.unmodifiableSet(vocabularyCollections);
	}

	public void addVocabularyCollection(
			VocabularyCollection vocabularyCollection) {
		vocabularyCollections.add(vocabularyCollection);
		vocabularyCollection.setTestStepContext(this);
	}

}
