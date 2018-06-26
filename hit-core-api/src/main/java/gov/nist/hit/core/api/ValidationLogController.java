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

import gov.nist.auth.hit.core.domain.ValidationLog;
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

	private void checkPermission(Authentication auth) throws Exception {
		String username = auth.getName();
		if (username == null)
			throw new NoUserFoundException("User could not be found");
		if (!userService.hasGlobalAuthorities(username)) {
			throw new NoUserFoundException("You do not have the permission to perform this task");
		}
	}

	@PreAuthorize("hasRole('admin')")
	@ApiOperation(value = "get all logs", nickname = "getAll")
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public List<ValidationLog> getAll(Authentication authentication, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Fetching all validation logs...");
		checkPermission(authentication);
		return validationLogService.findAll();
	}

	@PreAuthorize("hasRole('admin')")
	@RequestMapping(value = "/count", method = RequestMethod.GET, produces = "application/json")
	public long countAll(Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.info("Fetching all validation logs count...");
		checkPermission(authentication);
		return validationLogService.countAll();
	}

}
