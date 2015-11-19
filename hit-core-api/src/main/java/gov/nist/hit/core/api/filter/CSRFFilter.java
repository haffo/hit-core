/*
 * NIST Lab Result Interface (LRI) Constants.java 03/20/2013 This code was produced by the National
 * Institute of Standards and Technology (NIST). See the "nist.disclaimer" file given in the
 * distribution for information on the use and redistribution of this software.
 */
package gov.nist.hit.core.api.filter;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Harold Affo (NIST)
 * 
 */
//@WebFilter(urlPatterns = "*")
public class CSRFFilter implements Filter {

  /**
	 * 
	 */
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

    // TODO Auto-generated method stub
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    String method = req.getMethod();
    String path = req.getRequestURI().substring(req.getContextPath().length());
    if (method.equals("POST")) {
      String csrfToken = req.getHeader("csrfToken");
      if (csrfToken == null || req.getServletContext().getInitParameter("csrfToken") == null || !csrfToken.equals(req.getServletContext().getInitParameter("csrfToken"))) {
        res.sendError(401); // request not allowed
        return;
      }
    }
    chain.doFilter(request, response);
  }



  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.Filter#destroy()
   */
  @Override
  public void destroy() {
    // TODO Auto-generated method stub

  }

}
