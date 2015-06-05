package gov.nist.hit.core.service.exception;

public class UserTokenIdNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public UserTokenIdNotFoundException(String identifier) {
		super("FacilityId '" + identifier + "' not found.");
	}

}
