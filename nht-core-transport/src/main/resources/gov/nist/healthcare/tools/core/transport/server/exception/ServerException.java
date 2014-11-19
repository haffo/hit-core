package gov.nist.healthcare.tools.core.transport.server.exception;

public class ServerException extends Exception {
	private static final long serialVersionUID = 1L;

	public ServerException() {
		super();
	}

	public ServerException(String error) {
		super(error);
	}

	public ServerException(String s, Throwable ex) {
		super(s, ex);
	}

	public ServerException(String s, Throwable ex, Object msg) {
		super(s, ex);
	}
}
