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
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


//@WebFilter(urlPatterns = "*")
public class XSSFilter implements Filter{

	protected final Log logger = LogFactory.getLog(getClass());

	private FilterConfig filterConfig;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	public void destroy() {
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		chain.doFilter(new XSSRequestWrapper((HttpServletRequest) request), response);

	}
	
	
	
	public class XSSRequestWrapper extends HttpServletRequestWrapper {
	    
	    protected final Log logger = LogFactory.getLog(getClass());
	    
	    public XSSRequestWrapper(HttpServletRequest servletRequest) {
	        super(servletRequest);
	    }

	    public String[] getParameterValues(String parameter) {
//	      logger.info("In getParameterValues .. parameter: "+parameter);
	        String[] values = super.getParameterValues(parameter);
	        if (values == null) {
	            return null;
	        }
	        if ("password".equals(parameter) ||
	                "pass".equals(parameter) ||
	                "pwd".equals(parameter)){
	            return values;
	        }
	        int count = values.length;
	        String[] encodedValues = new String[count];
	        for (int i = 0; i < count; i++) {
	            encodedValues[i] = cleanXSS(values[i]);
	        }
	        return encodedValues;
	    }

	    public String getParameter(String parameter) {
//	      logger.info("In getParameter .. parameter: "+parameter);
	        String value = super.getParameter(parameter);
	        if (value == null) {
	            return null;
	        }
	        if ("password".equals(parameter) ||
	                "pass".equals(parameter) ||
	                "pwd".equals(parameter)){
	            return value;
	        }
//	      logger.info("In getParameter RequestWrapper ........ value .......");
	        return cleanXSS(value);
	    }

	    public String getHeader(String name) {
//	      logger.info("In getHeader .. parameter: "+name);
	        String value = super.getHeader(name);
	        if (value == null)
	            return null;
//	      logger.info("In getHeader RequestWrapper ........... value ....");
	        return cleanXSS(value);
	    }

	    private String cleanXSS(String value) {
	        String origValue = value;
	        /* HTML special character */
	        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
	        
	        /* SQL special character */
	        value = value.replaceAll("'", "&#39;");
	        
	        /* Javascript */
	        value = value.replaceAll("eval\\((.*)\\)", "");
	        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");

	        value = value.replaceAll("(?i)<script.*?>.*?<script.*?>", "");
	        value = value.replaceAll("(?i)<script.*?>.*?</script.*?>", "");
	        value = value.replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "");
	        value = value.replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "");
	        //value = value.replaceAll("<script>", "");
	        //value = value.replaceAll("</script>", "");
	        
	        if (!origValue.equals(value)){
	            logger.info("XSS detection - original value: " + origValue+" - transformed value: "+value);
	        }
	        
	        return value;
	    }

	}

}
