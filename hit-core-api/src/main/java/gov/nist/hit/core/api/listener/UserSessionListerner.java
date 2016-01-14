package gov.nist.hit.core.api.listener;

import java.util.List;

import gov.nist.hit.core.api.SessionContext;
import gov.nist.hit.core.api.TestArtifactController;
import gov.nist.hit.core.domain.Transaction;
import gov.nist.hit.core.domain.TransportConfig;
import gov.nist.hit.core.domain.User;
import gov.nist.hit.core.repo.TransportConfigRepository;
import gov.nist.hit.core.repo.UserRepository;
import gov.nist.hit.core.service.TransactionService;
import gov.nist.hit.core.service.TransportConfigService;
import gov.nist.hit.core.service.UserService;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@WebListener
public class UserSessionListerner implements HttpSessionListener {
  static final Logger logger = LoggerFactory.getLogger(UserSessionListerner.class);
 

  
  @Override
  public void sessionCreated(HttpSessionEvent sessionEvent) {
    logger.info("New Session Created");
    sessionEvent.getSession().setMaxInactiveInterval(3600);
    User user = new User();
    UserService userService = getUserService(sessionEvent);
    userService.save(user);
    SessionContext.setCurrentUserId(sessionEvent.getSession(),  user.getId());
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent sessionEvent) {
    try{
    Long userId = SessionContext.getCurrentUserId(sessionEvent.getSession());
    UserService userService = getUserService(sessionEvent);
    userService.delete(userId);
    }catch(RuntimeException e){
    }catch(Exception e){
    }
    logger.info("Session deleted"); 
  }
 
  private UserService getUserService(HttpSessionEvent sessionEvent){
    HttpSession session = sessionEvent.getSession();
    ApplicationContext ctx = 
          WebApplicationContextUtils.
                getWebApplicationContext(session.getServletContext());
    UserService userService = 
                (UserService) ctx.getBean("userService");
    return userService;
 }
  
 
 
}
