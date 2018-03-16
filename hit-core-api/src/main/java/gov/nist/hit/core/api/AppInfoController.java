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

package gov.nist.hit.core.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.hit.core.domain.AppInfo;
import gov.nist.hit.core.service.AppInfoService;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RestController
@RequestMapping("/appInfo")
@PropertySource(value = { "classpath:app-config.properties" })
public class AppInfoController {

	@Value("${app.baseUrl:@null}")
	private String baseUrl;

	@Autowired
	private AppInfoService appInfoService;

	// @Autowired
	// private ApplicationContext applicationContext;

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public AppInfo info(HttpServletRequest request) {
		AppInfo appInfo = appInfoService.get();
		if (appInfo == null) {
			appInfo = new AppInfo();
		}
		String url = baseUrl != null ? baseUrl : getUrl(request);
		appInfo.setUrl(url);
		return appInfo;
	}

	private String getUrl(HttpServletRequest request) {
		String scheme = request.getScheme();
		String host = request.getHeader("Host");
		// return scheme + "://" + host + "/hit-tool";
		String url = scheme + "://" + host + request.getContextPath();
		return url;
	}

	// @RequestMapping(value = "/beans", method = RequestMethod.GET)
	// public String[] beans() {
	// String[] beanNames = applicationContext.getBeanDefinitionNames();
	// return beanNames;
	// }

}
