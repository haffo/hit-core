package gov.nist.healthcare.tools.core.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class EnvelopeTestContext extends TestContext {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "testContext", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	protected Set<Message> exampleMessages = new HashSet<Message>();

	public Set<Message> getExampleMessages() {
		return Collections.unmodifiableSet(exampleMessages);
	}

	public void addMessage(Message exampleMessage) {
		exampleMessages.add(exampleMessage);
		exampleMessage.setTestContext(this);
	}
}
