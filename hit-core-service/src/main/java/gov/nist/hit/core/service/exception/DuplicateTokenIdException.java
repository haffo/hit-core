package gov.nist.hit.core.service.exception;

public class DuplicateTokenIdException extends Exception {
	private static final long serialVersionUID = 1L;

	public DuplicateTokenIdException(String tokenId) {
		super("tokenId " + tokenId + "' already exist.");
	}

}
