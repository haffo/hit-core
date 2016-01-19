package gov.nist.hit.core.api;


import java.io.Serializable;

import javax.servlet.http.HttpSession;


public class SessionContext implements Serializable {

  private static final long serialVersionUID = 1L;

  private final static String CURRENT_USER_ID = "CURRENT_USER_ID";


  public static Long getCurrentUserId(HttpSession session) {
    return session != null && session.getAttribute(CURRENT_USER_ID) != null ? (Long) session
        .getAttribute(CURRENT_USER_ID) : null;
  }

  public static void setCurrentUserId(HttpSession session, Long id) {
    if (session != null)
      session.setAttribute(CURRENT_USER_ID, id);
  }

  public void clearSession(HttpSession session) {
    if (session != null)
      session.setAttribute(CURRENT_USER_ID, null);

  }

}
