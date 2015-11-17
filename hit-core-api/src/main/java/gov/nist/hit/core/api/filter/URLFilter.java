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
@WebFilter(urlPatterns = "*")
public class URLFilter implements Filter {

  private static final String BOWER = "/bower_components";
  private static final String FONTS = "/fonts";
  private static final String IMAGES = "/images";
  private static final String LIB = "/lib";
  private static final String RESOURCES = "/resources";
  private static final String SCRIPTS = "/scripts";
  private static final String STYLES = "/styles";
  private static final String VIEWS = "/views";
  private static final String WEB_INF = "/WEB-INF";
  private static final String API = "/api";


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
    if (isExcluded(req)) {
      res.sendError(404);
      return;
    } else {
      chain.doFilter(request, response);
    }
  }

  /**
   * 
   * @param req
   * @return
   */
  private boolean isExcluded(HttpServletRequest req) {
    String path = req.getRequestURI().substring(req.getContextPath().length());
//    return path.startsWith(BOWER) || path.startsWith(FONTS) || path.startsWith(IMAGES)
//        || path.startsWith(LIB) || path.startsWith(RESOURCES) || path.startsWith(SCRIPTS)
//        || path.startsWith(STYLES) || path.startsWith(VIEWS) || path.startsWith(WEB_INF)
//        || (path.endsWith(".html") && !path.equals("/index.html"));
    
    return !path.startsWith(API) || (path.endsWith(".html") && !path.equals("/index.html"));
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
