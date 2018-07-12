package gov.nist.hit.core.api;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.auth.hit.core.domain.Account;
import gov.nist.auth.hit.core.domain.TransportLog;
import gov.nist.hit.core.domain.ResponseMessage;
import gov.nist.hit.core.domain.ResponseMessage.Type;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.TestStepService;
import gov.nist.hit.core.service.TransportLogService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.exception.NoUserFoundException;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/logs/transport")
@RestController
public class TransportLogController {

	static final Logger logger = LoggerFactory.getLogger(TransportLogController.class);

	@Autowired
	private TransportLogService transportLogService;

	@Autowired
	protected TestStepService testStepService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private UserService userService;

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

	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public boolean saveLog(HttpServletRequest request, HttpServletResponse response, @RequestBody TransportLog log)
			throws Exception {
		logger.info("saving transport log ...");
		Long userId = SessionContext.getCurrentUserId(request.getSession(false));
		log.setUserId(userId);
		process(log);
		return true;
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public List<TransportLog> getAll(Authentication authentication, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Fetching all transport logs...");
		checkPermission(authentication);
		List<TransportLog> logs = transportLogService.findAll();
		return logs;
	}

	public void process(TransportLog log) throws Exception {
		TestStep step = testStepService.findOne(log.getTestStepId());
		if (step != null) {
			String userfullName = null;
			Account account = null;
			if (log.getUserId() != null) {
				account = accountService.findOne(log.getUserId());
			}
			userfullName = account != null && account.getFullName() != null ? account.getFullName()
					: "Guest-" + log.getUserId();
			log.setUserFullname(userfullName);
			log.setCompanyName(account != null ? account.getEmployer() : "NA");
			log.setTestStepName(step.getName());
			log.setTestStepId(step.getPersistentId());
			log.setTestingType(step.getTestingType() != null ? step.getTestingType().toString() : null);
		}
		log.setDate(new Date());
		transportLogService.save(log);
	}

	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/count", method = RequestMethod.GET, produces = "application/json")
	public long countAll(Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.info("Fetching all transport logs count...");
		checkPermission(authentication);
		return transportLogService.countAll();
	}

	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "delete log", nickname = "getAll")
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST, produces = "application/json")
	public ResponseMessage deleteLog(Authentication authentication, HttpServletRequest request,
			@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
		logger.info("deleting transport log with id=" + id + "...");
		checkPermission(authentication);
		transportLogService.delete(id);
		return new ResponseMessage(Type.success, "transport Log " + id + " deleted successfully", id + "", true);
	}

}
