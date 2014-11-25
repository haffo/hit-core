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
public class CFTestContext extends TestContext {

	protected static final long serialVersionUID = 1L;

	@OneToOne(mappedBy = "testContext", cascade = CascadeType.ALL)
	protected Profile profile;

	@JsonIgnore
	@OneToMany(mappedBy = "testContext", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	protected List<TableLibrary> tableLibraries = new ArrayList<TableLibrary>();

	@JsonIgnore
	@OneToOne(mappedBy = "testContext", cascade = CascadeType.ALL)
	protected ValidationContext validationContext;

	@OneToMany(mappedBy = "testContext", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	protected Set<Message> exampleMessages = new HashSet<Message>();

	@OneToMany(mappedBy = "testContext", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	protected Set<VocabularyCollection> vocabularyCollections = new HashSet<VocabularyCollection>();

	public void addMessage(Message exampleMessage) {
		exampleMessages.add(exampleMessage);
		exampleMessage.setTestContext(this);
	}

	public List<TableLibrary> getTableLibraries() {
		return (List<TableLibrary>) Collections
				.unmodifiableCollection(tableLibraries);
	}

	public ValidationContext getValidationContext() {
		return validationContext;
	}

	public void addTableLibrary(TableLibrary tableLibrary) {
		tableLibraries.add(tableLibrary);
		tableLibrary.setTestContext(this);
	}

	public void setValidationContext(ValidationContext validationContext) {
		this.validationContext = validationContext;
		this.validationContext.setTestContext(this);
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
		this.profile.setTestContext(this);
	}

	public Set<Message> getExampleMessages() {
		return Collections.unmodifiableSet(exampleMessages);
	}

	public Profile getProfile() {
		return profile;
	}

	public Set<VocabularyCollection> getVocabularyCollections() {
		return Collections.unmodifiableSet(vocabularyCollections);
	}

	public void addVocabularyCollection(
			VocabularyCollection vocabularyCollection) {
		vocabularyCollections.add(vocabularyCollection);
		vocabularyCollection.setTestContext(this);
	}

}
