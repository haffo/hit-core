package gov.nist.hit.core.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.auth.hit.core.domain.Account;
import gov.nist.auth.hit.core.domain.ValidationLog;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.TestStepService;
import gov.nist.hit.core.service.UserIdService;
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
	private AccountService accountService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserIdService userIdService;

	@Autowired
	protected TestStepService testStepService;

	private void checkPermission(Authentication auth) throws Exception {
		String username = auth.getName();
		if (username == null)
			throw new NoUserFoundException("User could not be found");
		if (!userService.hasGlobalAuthorities(username)) {
			throw new NoUserFoundException("You do not have the permission to perform this task");
		}
	}

	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "get all logs", nickname = "getAll")
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public List<ValidationLog> getAll(Authentication authentication, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Fetching all validation logs...");
		checkPermission(authentication);
		return process(validationLogService.findAll());
	}

	public List<ValidationLog> process(List<ValidationLog> logs) throws Exception {
		if (logs != null && !logs.isEmpty()) {
			for (ValidationLog log : logs) {
				TestStep step = testStepService.findOneByPersistenceId(log.getTestStepId());
				if (step != null) {
					String userfullName = null;
					boolean updated = false;
					if (log.getUserId() != null) {
						Account account = accountService.findOne(log.getUserId());
						userfullName = account != null ? account.getFullName() : "Guest-" + log.getUserId();
						if (log.getUserFullname() == null) {
							log.setUserFullname(userfullName);
							updated = true;
						}
						if (log.getCompanyName() == null) {
							updated = true;
							log.setCompanyName(account != null ? account.getEmployer() : "NA");
						}
					}
					if (log.getTestStepName() == null) {
						log.setTestStepName(step.getName());
						updated = true;
					}
					if (updated) {
						validationLogService.save(log);
					}
				}
			}
		}
		return logs;
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/count", method = RequestMethod.GET, produces = "application/json")
	public long countAll(Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.info("Fetching all validation logs count...");
		checkPermission(authentication);
		return validationLogService.countAll();
	}

}
