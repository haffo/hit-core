package gov.nist.hit.core.api.listener;

import gov.nist.hit.core.api.SessionContext;
import gov.nist.hit.core.domain.account.Account;
import gov.nist.hit.core.service.AccountService;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@WebListener
public class UserSessionListerner implements HttpSessionListener {
  static final Logger logger = LoggerFactory.getLogger(UserSessionListerner.class);



  @Override
  public void sessionCreated(HttpSessionEvent sessionEvent) {
    logger.info("New Session Created");
    Account user = new Account();
    AccountService userService = getAccountService(sessionEvent);
    userService.save(user);
    SessionContext.setCurrentUserId(sessionEvent.getSession(), user.getId());
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent sessionEvent) {
    try {
      Long userId = SessionContext.getCurrentUserId(sessionEvent.getSession());
      AccountService userService = getAccountService(sessionEvent);
      userService.delete(userId);
    } catch (RuntimeException e) {
    } catch (Exception e) {
    }
    logger.info("Session deleted");
  }

  private AccountService getAccountService(HttpSessionEvent sessionEvent) {
    HttpSession session = sessionEvent.getSession();
    ApplicationContext ctx =
        WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
    AccountService userService = (AccountService) ctx.getBean("accountService");
    return userService;
  }



}
