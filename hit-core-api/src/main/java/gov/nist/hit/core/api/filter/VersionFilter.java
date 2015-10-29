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
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@WebFilter(urlPatterns = "/api/*")
public class VersionFilter implements Filter {

  /*
   * flaw: Browser Mime Sniffing - fix: X-Content-Type-Options flaw: Cached SSL Content - fix:
   * Cache-Control flaw: Cross-Frame Scripting - fix: X-Frame-Options flaw: Cross-Site Scripting -
   * fix: X-XSS-Protection flaw: Force SSL - fix: Strict-Transport-Security
   * 
   * assure no-cache for login page to prevent IE from caching
   */

  protected final Log logger = LogFactory.getLog(getClass());

  private FilterConfig filterConfig;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    this.filterConfig = filterConfig;
  }

  @Override
  public void destroy() {
    this.filterConfig = null;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
      if (response instanceof HttpServletResponse) {
      HttpServletResponse httpResponse = (HttpServletResponse) response;
      httpResponse.setHeader("Access-Control-Expose-Headers", "dTime");
      httpResponse.addHeader("dTime", request.getServletContext().getInitParameter("dTime"));
      }
    chain.doFilter(request, response);
  }

}
