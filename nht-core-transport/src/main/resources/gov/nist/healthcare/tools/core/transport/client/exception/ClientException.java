package gov.nist.healthcare.tools.core.transport.client.exception;

public class ClientException extends Exception {
	private static final long serialVersionUID = 1L;

	public ClientException() {
		super();
	}

	public ClientException(String error) {
		super(error);
	}

}
