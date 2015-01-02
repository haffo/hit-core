package gov.nist.healthcare.tools.core.models;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;

@Entity
public class ConnectivityTestContext extends TestCaseContext {

	private static final long serialVersionUID = 1L;

	@Column(columnDefinition = "LONGTEXT")
	protected String message;

	@Column(columnDefinition = "LONGTEXT")
	protected String exampleMessage;

	@Column(nullable = true)
	private String requestValidationPhase;

	@Column(nullable = true)
	private String responseValidationPhase;

	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(length = 100000)
	protected byte[] requestContentImage;

	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(length = 100000)
	protected byte[] responseContentImage;

	public byte[] getRequestContentImage() {
		return requestContentImage;
	}

	public void setRequestContentImage(byte[] requestContentImage) {
		this.requestContentImage = requestContentImage;
	}

	public byte[] getResponseContentImage() {
		return responseContentImage;
	}

	public void setResponseContentImage(byte[] responseContentImage) {
		this.responseContentImage = responseContentImage;
	}

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
