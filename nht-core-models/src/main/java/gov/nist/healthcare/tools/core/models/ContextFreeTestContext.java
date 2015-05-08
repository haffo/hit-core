package gov.nist.healthcare.tools.core.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ContextFreeTestContext extends TestCaseContext {

	protected static final long serialVersionUID = 1L;

	protected Profile profile;

	protected ValueSetLibrary valueSetLibrary;

	@JsonIgnore
	protected Constraints constraints;

	@ElementCollection(fetch = FetchType.EAGER)
	protected Set<Message> exampleMessages = new HashSet<Message>();

	public void addMessage(Message exampleMessage) {
		exampleMessages.add(exampleMessage);
	}

	public Constraints getConstraints() {
		return constraints;
	}

	public void setConstraints(Constraints constraints) {
		this.constraints = constraints;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Set<Message> getExampleMessages() {
		return Collections.unmodifiableSet(exampleMessages);
	}

	public Profile getProfile() {
		return profile;
	}

	public ValueSetLibrary getValueSetLibrary() {
		return valueSetLibrary;
	}

	public void setValueSetLibrary(ValueSetLibrary valueSetLibrary) {
		this.valueSetLibrary = valueSetLibrary;
	}

	public void setExampleMessages(Set<Message> exampleMessages) {
		this.exampleMessages = exampleMessages;
	}

}
