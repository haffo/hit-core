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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.auth.hit.core.domain.TestingType;
import gov.nist.auth.hit.core.domain.TransportConfig;
import gov.nist.hit.core.api.exception.TransportConfigException;
import gov.nist.hit.core.domain.SaveConfigRequest;
import gov.nist.hit.core.domain.Transaction;
import gov.nist.hit.core.domain.TransportFormContent;
import gov.nist.hit.core.domain.TransportForms;
import gov.nist.hit.core.domain.TransportMessage;
import gov.nist.hit.core.repo.TransportFormsRepository;
import gov.nist.hit.core.service.Streamer;
import gov.nist.hit.core.service.TransactionService;
import gov.nist.hit.core.service.TransportConfigService;
import gov.nist.hit.core.service.TransportMessageService;
import io.swagger.annotations.ApiOperation;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RequestMapping("/transport")
@RestController
public class TransportController {

	static final Logger logger = LoggerFactory.getLogger(DocumentationController.class);

	@Autowired
	private TransportFormsRepository transportFormsRepository;

	@Autowired
	private TransportConfigService transportConfigService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private TransportMessageService transportMessageService;

	@Autowired
	private Streamer streamer;

	@RequestMapping(value = "/config/form", method = RequestMethod.GET)
	public TransportFormContent getConfigurationForm(@RequestParam("type") TestingType type,
			@RequestParam("protocol") String protocol, @RequestParam("domain") String domain) {
		String content = null;
		if (TestingType.SUT_INITIATOR.equals(type)) {
			content = transportFormsRepository.getSutInitiatorFormByProtocolAndDomain(protocol, domain);
		} else if (TestingType.TA_INITIATOR.equals(type)) {
			content = transportFormsRepository.getTaInitiatorFormByProtocolAndDomain(protocol, domain);
		}
		logger.info("Fetching  form of type=" + type + " and protocol=" + protocol + " and domain=" + domain);
		return new TransportFormContent(content);
	}

	@RequestMapping(value = "/config/save", method = RequestMethod.POST)
	public boolean saveConfig(@RequestBody SaveConfigRequest request) throws TransportConfigException {
		TransportConfig config = transportConfigService.findOneByUserAndProtocolAndDomain(request.getUserId(),
				request.getProtocol(), request.getDomain());
		if (config != null) {
			if (TestingType.SUT_INITIATOR.equals(request.getType())) {
				Map<String, String> newValues = request.getConfig();
				Map<String, String> sutConfig = config.getSutInitiator();
				for (String key : newValues.keySet()) {
					sutConfig.put(key, newValues.get(key));
				}
			} else if (TestingType.TA_INITIATOR.equals(request.getType())) {
				config.setTaInitiator(request.getConfig());
			}
			transportConfigService.save(config);
			return true;
		}
		throw new TransportConfigException("No transport's configuration found for domain=" + request.getDomain()
				+ ", protocol=" + request.getProtocol());
	}

	@RequestMapping(value = "/transaction/{transactionId}/delete", method = RequestMethod.POST)
	public boolean deleteTransaction(@PathVariable Long transactionId) {
		Transaction transaction = transactionService.findOne(transactionId);
		if (transaction != null) {
			Map<String, String> criteria = transaction.getProperties();
			List<TransportMessage> transportMessages = transportMessageService.findAllByProperties(criteria);
			if (transportMessages != null) {
				transportMessageService.delete(transportMessages);
			}
			transactionService.delete(transaction);
		}
		return true;
	}

	@ApiOperation(value = "", nickname = "", hidden = true)
	@RequestMapping(value = "/transaction/{transactionId}", method = RequestMethod.GET)
	public void transaction(HttpServletResponse response, @PathVariable Long transactionId) throws IOException {
		streamer.stream(response.getOutputStream(), transactionService.findOne(transactionId));
	}

	// @Cacheable(value = "HitCache", key = "'transport-forms'")
	@RequestMapping(value = "/forms/{domain}", method = RequestMethod.GET)
	public List<TransportForms> findDomainForms(@PathVariable("domain") String domain) {
		logger.info("Fetching  all transports form");
		List<TransportForms> forms = transportFormsRepository.findAllFormsByDomain(domain);
		return forms;
	}

}
