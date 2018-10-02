package gov.nist.hit.core.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.auth.hit.core.domain.Account;
import gov.nist.auth.hit.core.domain.ValidationLog;
import gov.nist.hit.core.domain.ResponseMessage;
import gov.nist.hit.core.domain.ResponseMessage.Type;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.TestStepService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.ValidationLogService;
import gov.nist.hit.core.service.exception.NoUserFoundException;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/logs/validation")
@RestController
public class ValidationLogController {

	static final Logger logger = LoggerFactory.getLogger(ValidationLogController.class);

	@Autowired
	private ValidationLogService validationLogService;

	@Autowired
	private UserService userService;

	@Autowired
	protected TestStepService testStepService;

	@Autowired
	private AccountService accountService;

	private void checkPermission(Authentication auth) throws Exception {
		String username = auth.getName();
		if (username == null)
			throw new NoUserFoundException("User could not be found");
		Account account = accountService.findByTheAccountsUsername(username);
		if (account == null) {
			throw new NoUserFoundException("You do not have the permission to perform this task");
		}
		if (!userService.hasGlobalAuthorities(username) && !userService.isAdminByEmail(account.getEmail())) {
			throw new NoUserFoundException("You do not have the permission to perform this task");
		}
	}

	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "get all logs", nickname = "getAll")
	@RequestMapping(value = "/{domain}", method = RequestMethod.GET, produces = "application/json")
	public List<ValidationLog> getAll(@PathVariable("domain") String domain, Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Fetching all validation logs...");
		checkPermission(authentication);
		return validationLogService.findAll(domain);
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/{domain}/count", method = RequestMethod.GET, produces = "application/json")
	public long countAll(@PathVariable("domain") String domain, Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Fetching all validation logs count...");
		checkPermission(authentication);
		return validationLogService.countAll(domain);
	}

	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "delete log", nickname = "delete log by id")
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST, produces = "application/json")
	public ResponseMessage deleteLog(Authentication authentication, HttpServletRequest request,
			@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
		logger.info("deleting validation log with id=" + id + "...");
		checkPermission(authentication);
		validationLogService.delete(id);
		return new ResponseMessage(Type.success, "Validation Log " + id + " deleted successfully", id + "", true);
	}

}
