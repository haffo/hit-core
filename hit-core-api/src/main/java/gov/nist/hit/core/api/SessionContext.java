package gov.nist.hit.core.api;
 

import java.io.Serializable;
 
import javax.servlet.http.HttpSession;
 
 
public class SessionContext implements Serializable {
 
  private static final long serialVersionUID = 1L;
  
  private final static String CURRENT_USER_ID = "CURRENT_USER_ID";

  private final static String  VALIDATION_REPORT = "VALIDATION_REPORT_";
  
  
  public static Long getCurrentUserId(HttpSession session){
    return (Long) session.getAttribute(CURRENT_USER_ID);
  }
  
  public static void setCurrentUserId(HttpSession session, Long id){
    session.setAttribute(CURRENT_USER_ID, id);
  }  
  
  public static void setValidationReport(HttpSession session, Long testStepId, String report){
    session.setAttribute(VALIDATION_REPORT + "" + testStepId, report);
  } 
   
  public static String getValidationReport(HttpSession session, Long testStepId){
    return (String) session.getAttribute(VALIDATION_REPORT + "" + testStepId);
  } 
  
  public void clearSession(HttpSession session){
    session.setAttribute(CURRENT_USER_ID, null);
    
  }
  
}
