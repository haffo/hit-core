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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import gov.nist.hit.core.domain.ResourceUploadLock;
import gov.nist.hit.core.service.CachedRepository;
import gov.nist.hit.core.service.ZipGenerator;
import gov.nist.hit.core.service.impl.ZipGeneratorImpl;

/**
 * @author Harold Affo (NIST)
 * 
 */

@Configuration
public class WebAppBeanConfig {

	@Bean
	public ZipGenerator zipGenerator() {
		return new ZipGeneratorImpl();
	}

	@Bean
	public CachedRepository cachedRepository() {
		return new CachedRepository();
	}

	@Bean
	@Profile("development")
	public ResourceUploadLock resourceFilterAllow() {
		return new ResourceUploadLock(false);
	}

	@Bean
	@Profile("production")
	public ResourceUploadLock resourceFilterBlock() {
		return new ResourceUploadLock(true);
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

}
