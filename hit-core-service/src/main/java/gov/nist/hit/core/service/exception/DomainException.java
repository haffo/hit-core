package gov.nist.hit.core.service.exception;

public class DomainException extends Exception {

	private static final long serialVersionUID = 4262180673193951190L;

	public DomainException(String errorMessage) {
		super(errorMessage);
	}

	public DomainException(Exception error) {
		super(error);
	}
	
}
