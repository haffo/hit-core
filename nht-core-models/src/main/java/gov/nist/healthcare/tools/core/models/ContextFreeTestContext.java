package gov.nist.healthcare.tools.core.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ContextFreeTestContext extends TestCaseContext {

	protected static final long serialVersionUID = 1L;

	@OneToOne(mappedBy = "testContext", cascade = CascadeType.ALL)
	protected Profile profile;

	@JsonIgnore
	@OneToOne(mappedBy = "testContext", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	protected TableLibrary tableLibrary;

	@OneToMany(mappedBy = "testContext", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	protected Set<VocabularyCollection> vocabularyCollections = new HashSet<VocabularyCollection>();

	@JsonIgnore
	@OneToOne(mappedBy = "testContext", cascade = CascadeType.ALL)
	protected Constraints constraints;

	@OneToMany(mappedBy = "testContext", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	protected Set<Message> exampleMessages = new HashSet<Message>();

	public void addMessage(Message exampleMessage) {
		exampleMessages.add(exampleMessage);
		exampleMessage.setTestContext(this);
	}

	public Constraints getConstraints() {
		return constraints;
	}

	public TableLibrary getTableLibrary() {
		return tableLibrary;
	}

	public void setTableLibrary(TableLibrary tableLibrary) {
		this.tableLibrary = tableLibrary;
		this.tableLibrary.setTestContext(this);
	}

	public void setConstraints(Constraints constraints) {
		this.constraints = constraints;
		this.constraints.setTestContext(this);
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
