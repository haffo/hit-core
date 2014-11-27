package gov.nist.healthcare.tools.core.services.exception;

public class UserTokenIdNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public UserTokenIdNotFoundException(String identifier) {
		super("FacilityId '" + identifier + "' not found.");
	}

}
