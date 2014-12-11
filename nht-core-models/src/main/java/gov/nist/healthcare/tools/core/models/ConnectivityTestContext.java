package gov.nist.healthcare.tools.core.models;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ConnectivityTestContext extends TestContext {

	private static final long serialVersionUID = 1L;

	@Column(columnDefinition = "LONGTEXT")
	protected String message;

	@Column(columnDefinition = "LONGTEXT")
	protected String exampleMessage;

	@Column(nullable = true)
	private String requestValidationPhase;

	@Column(nullable = true)
	private String responseValidationPhase;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getExampleMessage() {
		return exampleMessage;
	}

	public void setExampleMessage(String exampleMessage) {
		this.exampleMessage = exampleMessage;
	}

	public String getRequestValidationPhase() {
		return requestValidationPhase;
	}

	public void setRequestValidationPhase(String requestValidationPhase) {
		this.requestValidationPhase = requestValidationPhase;
	}

	public String getResponseValidationPhase() {
		return responseValidationPhase;
	}

	public void setResponseValidationPhase(String responseValidationPhase) {
		this.responseValidationPhase = responseValidationPhase;
	}

}
