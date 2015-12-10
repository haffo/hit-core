package gov.nist.hit.core.transport.exception;

public class TransportServerException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TransportServerException() {
		super();
	}

	public TransportServerException(String error) {
		super(error);
	}

	public TransportServerException(String s, Throwable ex) {
		super(s, ex);
	}

	public TransportServerException(String s, Throwable ex, Object msg) {
		super(s, ex);
	}
}
