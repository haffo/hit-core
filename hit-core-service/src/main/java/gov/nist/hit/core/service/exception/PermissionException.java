package gov.nist.hit.core.service.exception;

public class PermissionException extends Exception {

	private static final long serialVersionUID = 4262180673193951190L;

	public PermissionException(String errorMessage) {
		super(errorMessage);
	}

	public PermissionException(Exception error) {
		super(error);
	}
	
}
