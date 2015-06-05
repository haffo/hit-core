package gov.nist.hit.core.service.exception;

public class TableLibraryException extends Exception {
	private static final long serialVersionUID = 1L;

	public TableLibraryException(String message) {
		super(message);
	}

	public TableLibraryException(Exception exception) {
		super(exception);
	}
}