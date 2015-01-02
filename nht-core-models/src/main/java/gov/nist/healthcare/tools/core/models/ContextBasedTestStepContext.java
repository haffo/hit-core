package gov.nist.healthcare.tools.core.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ContextBasedTestStepContext extends TestStepContext {

	protected static final long serialVersionUID = 1L;

	@OneToOne(mappedBy = "testStepContext", cascade = CascadeType.ALL)
	protected Profile profile;

	@JsonIgnore
	@OneToOne(mappedBy = "testStepContext", cascade = CascadeType.ALL)
	protected TableLibrary tableLibrary;

	@JsonIgnore
	@OneToOne(mappedBy = "testStepContext", cascade = CascadeType.ALL)
	protected ValidationContext validationContext;

	@OneToOne(mappedBy = "testContext", cascade = CascadeType.ALL)
	protected Message message;

	@OneToOne(mappedBy = "testContext", cascade = CascadeType.ALL)
	protected VocabularyCollection vocabularyCollection;

	public ValidationContext getValidationContext() {
		return validationContext;
	}

	public void setValidationContext(ValidationContext validationContext) {
		this.validationContext = validationContext;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public TableLibrary getTableLibrary() {
		return tableLibrary;
	}

	public void setTableLibrary(TableLibrary tableLibrary) {
		this.tableLibrary = tableLibrary;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public VocabularyCollection getVocabularyCollection() {
		return vocabularyCollection;
	}

	public void setVocabularyCollection(
			VocabularyCollection vocabularyCollection) {
		this.vocabularyCollection = vocabularyCollection;
	}

	public Profile getProfile() {
		return profile;
	}

}
