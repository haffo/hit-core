package gov.nist.hit.core.transport.exception;

public class TransportClientException extends Exception {
	private static final long serialVersionUID = 1L;

	public TransportClientException() {
		super();
	}

	public TransportClientException(String error) {
		super(error);
	}

	public TransportClientException(Exception error) {
		super(error);
	}

}
