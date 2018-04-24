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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.auth.hit.core.domain.Account;
import gov.nist.hit.core.domain.Domain;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.AppInfoService;
import gov.nist.hit.core.service.DomainService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.exception.NoUserFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RestController
@RequestMapping("/domains")
@PropertySource(value = { "classpath:app-config.properties" })
@Api(value = "Domain Api", tags = "domain api")
public class DomainController {

	static final Logger logger = LoggerFactory.getLogger(DomainController.class);

	@Autowired
	private DomainService domainService;

	@Autowired
	private AppInfoService appInfoService;

	@Autowired
	private UserService userService;

	@Autowired
	private AccountService accountService;

	private void checkManagementSupport() throws Exception {
		if (!appInfoService.get().isDomainManagementSupported()) {
			throw new Exception("This operation is not supported by this tool");
		}
	}

	@ApiOperation(value = "Find all domains", nickname = "findDomainByScope")
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public List<Domain> findDomainse(HttpServletRequest request) {
		logger.info("Fetching all domains ...");
		Long userId = SessionContext.getCurrentUserId(request.getSession(false));
		if (userId != null) {
			Account account = accountService.findOne(userId);
			if (account != null && !account.isGuestAccount()) {
				return domainService.findShortAllWithGlobalOrAuthornameOrParticipantEmail(account.getUsername(),
						account.getEmail());
			}
		}
		return domainService.findShortAllGlobalDomains();
	}

	private void checkPermission(Domain domain, Authentication auth) throws Exception {
		String username = auth.getName();
		if (username == null)
			throw new NoUserFoundException("User could not be found");
		TestScope scope = domain.getScope();
		if (scope.equals(TestScope.GLOBAL) && !userService.hasGlobalAuthorities(username)) {
			throw new NoUserFoundException("You do not have the permission to perform this task");
		}
		if (!username.equals(domain.getAuthorUsername()) && !userService.isAdmin(username)) {
			throw new NoUserFoundException("You do not have the permission to perform this task");
		}
	}

	@ApiOperation(value = "Find a domain by its key", nickname = "findDomainByKey")
	@RequestMapping(value = "/searchByKey", method = RequestMethod.GET, produces = "application/json")
	public Domain findDomainByKey(HttpServletRequest request, @RequestParam(name = "key", required = true) String key)
			throws NoUserFoundException {
		Domain domain = domainService.findOneByKey(key);
		if (domain == null) {
			throw new NoUserFoundException("Unknown domain with key=" + key);
		}
		if (domain.getScope().equals(TestScope.USER)) {
			Long userId = SessionContext.getCurrentUserId(request.getSession(false));
			Account account = accountService.findOne(userId);
			if (!domain.getAuthorUsername().equals(account.getUsername())
					|| domain.getParticipantEmails().contains(account.getEmail())) {
				throw new NoUserFoundException("You do not have the permission to access this domain");
			}
		}
		return domain;
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/searchByScope", method = RequestMethod.GET, produces = "application/json")
	public List<Domain> findUserDomains(HttpServletRequest request,
			@RequestParam(name = "scope", required = true) TestScope scope, Authentication authentication)
			throws Exception {
		checkManagementSupport();
		String username = authentication.getName();
		return domainService.findShortAllByScopeAndAuthorname(scope, username);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public Domain findDomainById(HttpServletRequest request, @PathVariable("id") Long id) throws Exception {
		checkManagementSupport();
		Domain domain = domainService.findOne(id);
		if (domain == null) {
			throw new NoUserFoundException("Unknown domain");
		}
		if (domain.getScope().equals(TestScope.USER)) {
			Long userId = SessionContext.getCurrentUserId(request.getSession(false));
			Account account = accountService.findOne(userId);
			if (!domain.getAuthorUsername().equals(account.getUsername())
					|| domain.getParticipantEmails().contains(account.getEmail())) {
				throw new NoUserFoundException("You do not have the permission to access this domain");
			}
		}
		return domain;
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = "application/json")
	public Domain saveDomain(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody Domain domain,
			Authentication authentication) throws Exception {
		checkManagementSupport();
		Domain result = null;
		result = domainService.findOne(id);
		if (result == null) {
			throw new IllegalArgumentException("Unknown domain " + domain);
		}
		if (result.getAuthorUsername() != authentication.getName()) {
			throw new IllegalArgumentException("You do not have the privilege to perform this action");
		}

		checkPermission(result, authentication);
		result.merge(domain);
		domainService.save(result);
		return result;
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/{id}/canModify", method = RequestMethod.GET, produces = "application/json")
	public boolean canModify(HttpServletRequest request, @PathVariable("id") Long id, Authentication authentication)
			throws Exception {
		Domain domain = findDomainById(request, id);
		return domain.getAuthorUsername().equals(authentication.getName());
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
	public Domain createDomain(HttpServletRequest request, @RequestBody Domain domain, Authentication authentication)
			throws Exception {
		checkManagementSupport();

		String key = domain.getDomain();
		String name = domain.getName();
		TestScope scope = domain.getScope();

		if (org.springframework.util.StringUtils.isEmpty(key)) {
			throw new IllegalArgumentException("domain's key is missing");
		}
		if (org.springframework.util.StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("domain's name is missing");
		}

		if (org.springframework.util.StringUtils.isEmpty(scope)) {
			throw new IllegalArgumentException("Domain's scope is missing");
		}

		Domain found = domainService.findOneByKey(key);
		if (found != null) {
			throw new IllegalArgumentException("A domain with key=" + key + " already exist");
		}

		Domain result = new Domain();
		result.setAuthorUsername(authentication.getName());
		result.setScope(scope);
		result.setDomain(key);
		result.setName(name);
		result.setDisabled(true);
		checkPermission(result, authentication);
		domainService.save(result);
		return result;
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST, produces = "application/json")
	public boolean deleteDomain(HttpServletRequest request, @PathVariable("id") Long id, Authentication authentication)
			throws Exception {
		checkManagementSupport();
		Domain found = domainService.findOne(id);
		if (found == null) {
			throw new IllegalArgumentException("Domain with id=" + id + " not found");
		}

		if (found.getAuthorUsername() != authentication.getName()) {
			throw new IllegalArgumentException("You do not have the privilege to perform this action");
		}
		checkPermission(found, authentication);
		domainService.delete(found);
		return true;
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/{id}/publish", method = RequestMethod.POST, produces = "application/json")
	public boolean deleteDomain(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody Domain domain,
			Authentication authentication) throws Exception {
		checkManagementSupport();
		Domain found = domainService.findOne(id);
		if (found == null) {
			throw new IllegalArgumentException("Domain with id=" + id + " not found");
		}

		if (found.getAuthorUsername() != authentication.getName()) {
			throw new IllegalArgumentException("You do not have the privilege to perform this action");
		}

		checkPermission(found, authentication);
		found.merge(domain);
		// TODO : publish artifacts
		return true;
	}

}
