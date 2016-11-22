package gov.nist.hit.core.api.filter;

import gov.nist.hit.core.domain.ResourceUploadLock;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.support.WebApplicationContextUtils;

@WebFilter(urlPatterns = "/api/editResources/*")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ResourceUploadFilter implements Filter {

	private ResourceUploadLock filter;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filter = WebApplicationContextUtils.
				  getRequiredWebApplicationContext(filterConfig.getServletContext()).
				  getBean(ResourceUploadLock.class);
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
	    HttpServletResponse res = (HttpServletResponse) response;
	    if(filter.isEnabled()){
	    	res.sendError(423, "The resource you are trying to access is locked");
	    }
	    else {
	    	chain.doFilter(request, response);
	    }
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
