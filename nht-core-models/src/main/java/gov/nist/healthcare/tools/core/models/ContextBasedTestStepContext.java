package gov.nist.healthcare.tools.core.models;

import javax.persistence.Entity;

@Entity
public class ContextBasedTestStepContext extends TestStepContext {

	protected static final long serialVersionUID = 1L;

	protected Profile profile;

	protected ValueSetLibrary valueSetLibrary;

	protected ValidationContext validationContext;

	protected Message message;

	public ValidationContext getValidationContext() {
		return validationContext;
	}

	public void setValidationContext(ValidationContext validationContext) {
		this.validationContext = validationContext;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
		// this.profile.setTestStepContext(this);
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
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

}
