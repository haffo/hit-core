package gov.nist.healthcare.tools.core.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

	@JsonIgnore
	protected ValidationContext validationContext;

	protected Message message;

	@JsonIgnore
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((constraints == null) ? 0 : constraints.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((profile == null) ? 0 : profile.hashCode());
		result = prime
				* result
				+ ((validationContext == null) ? 0 : validationContext
						.hashCode());
		result = prime * result
				+ ((valueSetLibrary == null) ? 0 : valueSetLibrary.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestContext other = (TestContext) obj;
		if (constraints == null) {
			if (other.constraints != null)
				return false;
		} else if (!constraints.equals(other.constraints))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (profile == null) {
			if (other.profile != null)
				return false;
		} else if (!profile.equals(other.profile))
			return false;
		if (validationContext == null) {
			if (other.validationContext != null)
				return false;
		} else if (!validationContext.equals(other.validationContext))
			return false;
		if (valueSetLibrary == null) {
			if (other.valueSetLibrary != null)
				return false;
		} else if (!valueSetLibrary.equals(other.valueSetLibrary))
			return false;
		return true;
	}

}
