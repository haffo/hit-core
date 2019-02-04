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

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import gov.nist.hit.core.service.exception.DomainException;
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
@Api(value = "Tool Scope Api", tags = "tool scope api")
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

	private void checkManagementSupport(Authentication auth) throws DomainException {
		if (!isDomainManagementSupported()) {
			try {
				if (!userService.hasGlobalAuthorities(auth.getName()) && !userService.isAdmin(auth.getName())
						&& !userService.isSupervisor(auth.getName()) && !userService.isDeployer(auth.getName())) {
					Account account = accountService.findByTheAccountsUsername(auth.getName());
					if (account != null) {
						String email = account.getEmail();
						if (!userService.isAdminByEmail(email)) {
							throw new DomainException("You do not have the permission to perform this operation");
						}
					} else {
						throw new DomainException("You do not have the permission to perform this operation");
					}
				}
			} catch (NoUserFoundException e) {
				throw new DomainException(e);
			}
		}
	}

	private boolean isDomainManagementSupported() throws DomainException {
		return appInfoService.get().isDomainManagementSupported();
	}

	@ApiOperation(value = "Find all tool scopes", nickname = "findDomains")
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public List<Domain> findDomains(HttpServletRequest request) throws DomainException {
		try {
			logger.info("Fetching all tool scopes ...");
			Long userId = SessionContext.getCurrentUserId(request.getSession(false));
			if (userId != null) {
				Account account = accountService.findOne(userId);
				if (account != null && !account.isGuestAccount()) {
					String email = account.getEmail();
					if (userService.isAdminByEmail(email) || userService.isAdmin(account.getUsername())) {
						return domainService.findShortAll();
					} else {
						return domainService.findShortAllWithGlobalOrAuthornameOrParticipantEmail(account.getUsername(),
								account.getEmail());
					}
				}
			}
			return domainService.findShortAllGlobalDomains();
		} catch (NoUserFoundException e) {
			throw new DomainException(e);
		}

	}

	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "Find the user scopes", nickname = "findDomainByUsername")
	@RequestMapping(method = RequestMethod.GET, value = "/findByUser", produces = "application/json")
	public List<Domain> findDomainByUsername(HttpServletRequest request, Authentication authentication)
			throws DomainException {
		logger.info("Fetching all tool scopes ...");
		return domainService.findShortAllByAuthorname(authentication.getName());
	}

	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "Find the user scopes", nickname = "findDomainByUserAndRole")
	@RequestMapping(method = RequestMethod.GET, value = "/findByUserAndRole", produces = "application/json")
	public List<Domain> findByUserAndRole(HttpServletRequest request, Authentication authentication)
			throws DomainException {
		try {
			logger.info("Fetching all tool scopes ...");
			Long userId = SessionContext.getCurrentUserId(request.getSession(false));
			if (userId != null) {
				Account account = accountService.findOne(userId);
				if (account != null && !account.isGuestAccount()) {
					String email = account.getEmail();
					if (userService.isAdminByEmail(email) || userService.isAdmin(account.getUsername())) {
						return domainService.findShortAll();
					} else {
						return domainService.findShortAllByAuthorname(authentication.getName());
					}
				}
			}

			return null;

		} catch (NoUserFoundException e) {
			throw new DomainException(e);
		}
	}

	private void hasScopeAccess(TestScope scope, Authentication auth) throws DomainException {
		try {
			if (scope.equals(TestScope.GLOBAL) && !userService.hasGlobalAuthorities(auth.getName())
					&& !userService.isAdmin(auth.getName()) && !userService.isSupervisor(auth.getName())
					&& !userService.isDeployer(auth.getName())) {
				Account account = accountService.findByTheAccountsUsername(auth.getName());
				if (account != null) {
					String email = account.getEmail();
					if (!userService.isAdminByEmail(email)) {
						throw new DomainException("You do not have the permission to perform this operation");
					}
				} else {
					throw new DomainException("You do not have the permission to perform this operation");
				}
			}
		} catch (NoUserFoundException e) {
			throw new DomainException(e);
		}
	}

	private void hasDomainAccess(Domain domain, Authentication auth) throws DomainException {
		try {
			if (!userService.hasGlobalAuthorities(auth.getName()) && !userService.isAdmin(auth.getName())
					&& !userService.isSupervisor(auth.getName()) && !userService.isDeployer(auth.getName())) {
				Account account = accountService.findByTheAccountsUsername(auth.getName());
				if (account != null) {
					String email = account.getEmail();
					if (!userService.isAdminByEmail(email)) {
						throw new DomainException("You do not have the permission to perform this operation");
					}
				} else {
					throw new DomainException("You do not have the permission to perform this operation");
				}
			}
		} catch (NoUserFoundException e) {
			throw new DomainException(e);
		}
	}

	@ApiOperation(value = "Find a tool scope by its key", nickname = "findDomainByKey")
	@RequestMapping(value = "/searchByKey", method = RequestMethod.GET, produces = "application/json")
	public Domain findDomainByKey(HttpServletRequest request, @RequestParam(name = "key", required = true) String key)
			throws DomainException {
		try {
			Domain domain = domainService.findOneByKey(key);
			if (domain == null) {
				String safeKey = Jsoup.clean(key, Whitelist.simpleText());
				throw new DomainException("Unknown tool scope with key=" + safeKey);
			}
			if (domain.getScope().equals(TestScope.USER)) {
				Long userId = SessionContext.getCurrentUserId(request.getSession(false));
				Account account = accountService.findOne(userId);
				if (account != null && !account.isGuestAccount()) {
					String email = account.getEmail();
					if (!domain.getAuthorUsername().equals(account.getUsername())
							&& !domain.getParticipantEmails().contains(account.getEmail())
							&& !userService.isAdminByEmail(email) && !userService.isAdmin(account.getUsername())) {
						throw new DomainException("You do not have the permission to access this tool scope");
					}
				} else {
					throw new DomainException("You do not have the permission to access this tool scope");
				}
			}
			return domain;

		} catch (NoUserFoundException e) {
			throw new DomainException(e);

		}
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/searchByScope", method = RequestMethod.GET, produces = "application/json")
	public List<Domain> findUserDomains(HttpServletRequest request,
			@RequestParam(name = "scope", required = true) TestScope scope, Authentication authentication)
			throws DomainException {
		checkManagementSupport(authentication);
		String username = authentication.getName();
		return domainService.findShortAllByScopeAndAuthorname(scope, username);
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public Domain findDomainById(HttpServletRequest request, @PathVariable("id") Long id, Authentication auth)
			throws DomainException {
		try {
			checkManagementSupport(auth);
			Domain domain = domainService.findOne(id);
			if (domain == null) {
				throw new DomainException("Unknown tool scope");
			}
			if (domain.getScope().equals(TestScope.USER)) {
				Long userId = SessionContext.getCurrentUserId(request.getSession(false));
				Account account = accountService.findOne(userId);
				if (account != null && !account.isGuestAccount()) {
					String email = account.getEmail();
					if (!domain.getAuthorUsername().equals(account.getUsername())
							&& !domain.getParticipantEmails().contains(account.getEmail())
							&& !userService.isAdminByEmail(email) && !userService.isAdmin(account.getUsername())) {
						throw new DomainException("You do not have the permission to access this tool scope");
					}
				} else {
					throw new DomainException("You do not have the permission to access this tool scope");
				}

			}
			return domain;
		} catch (NoUserFoundException e) {
			throw new DomainException(e);

		}
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = "application/json")
	public Domain saveDomain(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody Domain domain,
			Authentication authentication) throws DomainException {
		try {
			checkManagementSupport(authentication);
			domainService.hasPermission(domain.getDomain(), authentication);
			Domain result = domainService.findOne(id);
			if (result == null) {
				throw new DomainException("Unknown tool scope " + domain);
			}
			result.merge(domain);
			result.setPreloaded(false);
			hasScopeAccess(result.getScope(), authentication);
			domainService.save(result);
			return result;
		} catch (NoUserFoundException e) {
			throw new DomainException(e);
		} catch (Exception e) {
			throw new DomainException(e);
		}
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/{id}/canModify", method = RequestMethod.GET, produces = "application/json")
	public boolean canModify(HttpServletRequest request, @PathVariable("id") Long id, Authentication authentication)
			throws DomainException {
		try {
			Domain domain = findDomainById(request, id, authentication);
			Account account = accountService.findByTheAccountsUsername(authentication.getName());
			if (account != null && !account.isGuestAccount()) {
				String email = account.getEmail();
				if (!domain.getAuthorUsername().equals(account.getUsername())
						&& !domain.getParticipantEmails().contains(account.getEmail())
						&& !userService.isAdminByEmail(email) && !userService.isAdmin(account.getUsername())) {
					return false;
				}
			} else {
				return false;
			}
			return true;
		} catch (NoUserFoundException e) {
			throw new DomainException(e);
		} catch (Exception e) {
			throw new DomainException(e);
		}
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
	public Domain createDomain(HttpServletRequest request, @RequestBody Domain domain, Authentication authentication)
			throws DomainException {
		checkManagementSupport(authentication);

		String key = domain.getDomain();
		String name = domain.getName();
		TestScope scope = domain.getScope();

		if (org.springframework.util.StringUtils.isEmpty(key)) {
			throw new DomainException("Tool scope's key is missing");
		}
		if (org.springframework.util.StringUtils.isEmpty(name)) {
			throw new DomainException("Tool scope's name is missing");
		}

		if (org.springframework.util.StringUtils.isEmpty(scope)) {
			throw new DomainException("Tool scope's scope is missing");
		}

		Domain found = domainService.findOneByKey(key);
		if (found != null) {
			throw new DomainException("A Tool scope with key=" + key + " already exists");
		}

		Domain result = new Domain();
		result.setAuthorUsername(authentication.getName());
		result.setScope(scope);
		result.setDomain(key);
		result.setName(name);
		result.setHomeTitle(domain.getHomeTitle());
		result.setDisabled(false);
		hasScopeAccess(result.getScope(), authentication);
		domainService.save(result);
		return result;
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/new", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded; charset=UTF-8", produces = "application/json")
	public Domain createDomain(HttpServletRequest request, @RequestParam("key") String key,
			@RequestParam("name") String name, @RequestParam("homeTitle") String homeTitle,
			Authentication authentication) throws DomainException {
		Domain d = new Domain();
		d.setDomain(key);
		d.setName(name);
		d.setScope(TestScope.USER);
		d.setHomeTitle(homeTitle);
		d.setDisabled(false);
		return createDomain(request, d, authentication);
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST, produces = "application/json")
	public boolean deleteDomain(HttpServletRequest request, @PathVariable("id") Long id, Authentication authentication)
			throws DomainException {
		try {
			checkManagementSupport(authentication);
			Domain found = domainService.findOne(id);
			if (found == null) {
				throw new DomainException("Tool Scope with id=" + id + " not found");
			}
			domainService.canDelete(found.getDomain(), authentication);
			domainService.delete(found);
			return true;
		} catch (Exception e) {
			throw new DomainException(e);
		}
	}

	@PreAuthorize("hasRole('tester')")
	@PostMapping(value = "/save-publish", produces = "application/json")
	public Domain publishDomain(HttpServletRequest request, @RequestBody Domain domain, Authentication authentication)
			throws DomainException {
		return saveAndupdateScope(domain, TestScope.GLOBAL, authentication);
	}

	@PreAuthorize("hasRole('tester')")
	@PostMapping(value = "/save-unpublish", produces = "application/json")
	public Domain unpublishDomain(HttpServletRequest request, @RequestBody Domain domain, Authentication authentication)
			throws DomainException {
		return saveAndupdateScope(domain, TestScope.USER, authentication);
	}

	@PreAuthorize("hasRole('tester')")
	@PostMapping(value = "/{id}/publish", produces = "application/json")
	public Domain publishDomain(HttpServletRequest request, @PathVariable("id") Long id, Authentication authentication)
			throws DomainException {
		return updateScope(id, TestScope.GLOBAL, authentication);
	}

	@PreAuthorize("hasRole('tester')")
	@PostMapping(value = "/{id}/unpublish", produces = "application/json")
	public Domain unpublishDomain(HttpServletRequest request, @PathVariable("id") Long id,
			Authentication authentication) throws DomainException {
		return updateScope(id, TestScope.USER, authentication);
	}

	public Domain saveAndupdateScope(Domain domain, TestScope scope, Authentication authentication)
			throws DomainException {
		checkManagementSupport(authentication);
		try {
			if (domain.getId() == null) {
				throw new DomainException("Invalid Tool Scope");
			}
			Domain found = domainService.findOne(domain.getId());
			if (found == null) {
				throw new DomainException("Tool Scope with id=" + domain.getId() + " not found");
			}
			domainService.canPublish(found, authentication);
			found.merge(domain);
			found.setScope(scope);
			domainService.save(found);
			return found;
		} catch (Exception e) {
			throw new DomainException(e);
		}
	}

	public Domain updateScope(Long id, TestScope scope, Authentication authentication) throws DomainException {
		checkManagementSupport(authentication);
		try {
			if (id == null) {
				throw new DomainException("Invalid Tool Scope");
			}
			Domain found = domainService.findOne(id);
			if (found == null) {
				throw new DomainException("Tool Scope with id=" + id + " not found");
			}
			domainService.canPublish(found, authentication);
			found.setScope(scope);
			domainService.save(found);
			return found;
		} catch (Exception e) {
			throw new DomainException(e);
		}

	}

}
