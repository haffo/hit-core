package gov.nist.hit.core.api.filter;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// @WebFilter(urlPatterns = "/api/*")
public class SessionTmeoutFilter implements Filter {

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
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    String path = req.getRequestURI().substring(req.getContextPath().length());
    HttpSession session = req.getSession(false);
    if (session == null
        && ((Pattern.compile("\\/api\\/(\\w+)\\/clearRecords").matcher(path).find()) || ((!Pattern
            .compile("\\/api\\/appInfo").matcher(path).find()
            // && !Pattern.compile("\\/api\\/accounts\\/login").matcher(path).find()
            && !Pattern.compile("\\/api\\/session\\/keepAlive").matcher(path).find()
            && !Pattern.compile("\\/api\\/session\\/create").matcher(path).find()
            && !Pattern.compile("\\/api\\/ws\\/").matcher(path).find()
            && !Pattern.compile("\\/api\\/transport\\/config\\/forms").matcher(path).find()
            && !Pattern.compile("\\/api\\/(\\w+)\\/testcases").matcher(path).find()
            && !Pattern.compile("\\/api\\/testcases").matcher(path).find()
            && !Pattern.compile("\\/api\\/teststeps").matcher(path).find()
            && !Pattern.compile("\\/api\\/testplans").matcher(path).find()
            && !Pattern.compile("\\/api\\/testcasegroups").matcher(path).find() && !Pattern
            .compile("\\/api\\/documentation\\/doc").matcher(path).find())))) {
      res.sendError(440); // session timeout
      return;
    }
    chain.doFilter(request, response);
  }
}
