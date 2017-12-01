package gov.nist.hit.core.service.impl;

import java.security.Principal;

import gov.nist.auth.hit.core.domain.Account;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.UserIdService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserIdServiceImpl implements UserIdService{

	@Autowired
	private AccountService accountService;
	
	@Override
	public Long getCurrentUserId(Principal p) {
		Account a = accountService.findByTheAccountsUsername(p.getName());
		if(a != null && !a.isPending() && !a.isGuestAccount()){
			return a.getId();
		}
		return null;
	}
	
	@Override
	public String getCurrentUserName(Principal p) {
		Account a = accountService.findByTheAccountsUsername(p.getName());
		if(a != null && !a.isPending() && !a.isGuestAccount()){
			return a.getUsername();
		}
		return null;
	}
	
}
