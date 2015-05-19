package gov.nist.healthcare.tools.core.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TestContext implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7669461668488662066L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	protected Profile profile;

	protected ValueSetLibrary valueSetLibrary;

	protected ValidationContext validationContext;

	protected Message message;

	protected Constraints constraints;

	public TestContext() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public ValueSetLibrary getValueSetLibrary() {
		return valueSetLibrary;
	}

	public void setValueSetLibrary(ValueSetLibrary valueSetLibrary) {
		this.valueSetLibrary = valueSetLibrary;
	}

	public ValidationContext getValidationContext() {
		return validationContext;
	}

	public void setValidationContext(ValidationContext validationContext) {
		this.validationContext = validationContext;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Constraints getConstraints() {
		return constraints;
	}

	public void setConstraints(Constraints constraints) {
		this.constraints = constraints;
	}

}
