package gov.nist.hit.core.api.listener;

import java.util.List;

import gov.nist.hit.core.api.SessionContext;
import gov.nist.hit.core.api.TestArtifactController;
import gov.nist.hit.core.domain.Transaction;
import gov.nist.hit.core.domain.TransportConfig;
import gov.nist.hit.core.domain.User;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
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
public class SessionCreationRequestListerner implements ServletRequestListener  {
  static final Logger logger = LoggerFactory.getLogger(SessionCreationRequestListerner.class);
 
  @Override
  public void requestDestroyed(ServletRequestEvent sre) {
 
  }

  @Override
  public void requestInitialized(ServletRequestEvent sre) {
    HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
    String path = request.getRequestURI().substring(request.getContextPath().length());
//    if (path.startsWith("/api/user/invalid") && !path.equals("/api/appInfo")) {
//      
//    }
//    HttpSession session = request.getSession();
//    if(session == null){
//    request.getSession(true);
//    }
  }
  
}
