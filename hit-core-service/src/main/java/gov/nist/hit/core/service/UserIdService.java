package gov.nist.hit.core.service;

import java.security.Principal;

public interface UserIdService {
	public Long getCurrentUserId(Principal p);
	public String getCurrentUserName(Principal p);
}
