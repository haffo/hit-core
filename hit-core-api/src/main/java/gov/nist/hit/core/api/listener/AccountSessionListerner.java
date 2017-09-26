package gov.nist.hit.core.api.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.context.support.WebApplicationContextUtils;

import gov.nist.auth.hit.core.domain.Account;
import gov.nist.hit.core.api.SessionContext;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.UserService;

@WebListener
public class AccountSessionListerner implements HttpSessionListener {
	static final Logger logger = LoggerFactory.getLogger(AccountSessionListerner.class);

	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		AccountService accountService = getAccountService(sessionEvent);
		UserService userService = getUserService(sessionEvent);
		User u = userService.getCurrentUser();
		Account account = null;
		if (u != null) {
			account = accountService.findByTheAccountsUsername(u.getUsername());
		} else {
			account = new Account();
			accountService.save(account);
		}
		SessionContext.setCurrentUserId(sessionEvent.getSession(), account.getId());
		logger.info("Session started successfully  for userId=" + account.getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		try {
			Long accountId = SessionContext.getCurrentUserId(sessionEvent.getSession());
			AccountService accountService = getAccountService(sessionEvent);
			Account account = accountService.findOne(accountId);
			if (account.isGuestAccount()) {
				accountService.delete(accountId);
			}
			logger.info("Session ended successfully  for userId=" + accountId);
		} catch (RuntimeException e) {
			logger.info("Deleting session failed: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Deleting session failed: " + e.getMessage());
		}
	}

	private AccountService getAccountService(HttpSessionEvent sessionEvent) {
		HttpSession session = sessionEvent.getSession();
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
		AccountService userService = (AccountService) ctx.getBean("accountService");
		return userService;
	}

	private UserService getUserService(HttpSessionEvent sessionEvent) {
		HttpSession session = sessionEvent.getSession();
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
		UserService userService = (UserService) ctx.getBean("userService");
		return userService;
	}

}
