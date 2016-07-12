/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */
package gov.nist.hit.core.api.config;

import gov.nist.hit.core.service.ResourcebundleLoader;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebAppInitializer implements WebApplicationInitializer

{

  @Override
  public void onStartup(final ServletContext servletContext) throws ServletException {

    final AnnotationConfigWebApplicationContext context1 =
        new AnnotationConfigWebApplicationContext();
    context1.setServletContext(servletContext);
    context1.scan("gov.nist.hit.core", "gov.nist.auth.hit.core");
    // web app servlet
    servletContext.addListener(new ContextLoaderListener(context1));
    Dynamic apiServlet = servletContext.addServlet("hit-api", new DispatcherServlet(context1));
    apiServlet.setLoadOnStartup(1);
    apiServlet.addMapping("/api/*");
    apiServlet.setAsyncSupported(true);

    //
    // FilterRegistration.Dynamic securityFilter =
    // servletContext.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class);
    // securityFilter.addMappingForUrlPatterns(null, false, "/*");
    //
    // servletContext.addListener(new HttpSessionEventPublisher());
    //


    Dynamic apiDocsServlet =
        servletContext.addServlet("hit-api-docs", new DispatcherServlet(context1));
    apiDocsServlet.setLoadOnStartup(1);
    apiDocsServlet.addMapping("/apidocs/*");


    // final AnnotationConfigWebApplicationContext context2 =
    // new AnnotationConfigWebApplicationContext();
    // context2.setServletContext(servletContext);
    // Dynamic apiDocsServlet =
    // servletContext.addServlet("hit-api-docs", new DispatcherServlet(context2));
    // apiDocsServlet.setLoadOnStartup(1);
    // apiDocsServlet.addMapping("/docs/*");

    // Dynamic apiUiServlet = servletContext.addServlet("swagger-api-ui", new
    // DispatcherServlet(root));
    // apiUiServlet.setLoadOnStartup(3);
    // apiUiServlet.addMapping("/docs/*");


    try {
      servletContext.setInitParameter("rsbVersion", ResourcebundleLoader.getRsbleVersion());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
