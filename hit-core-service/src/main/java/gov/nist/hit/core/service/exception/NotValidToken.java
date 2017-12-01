package gov.nist.hit.core.service.exception;

public class NotValidToken extends Exception {

	private static final long serialVersionUID = -4114788511915766249L;

	public NotValidToken(String errorMessage) {
		super(errorMessage);
	}

	public NotValidToken(Exception error) {
		super(error);
	}
	
}
