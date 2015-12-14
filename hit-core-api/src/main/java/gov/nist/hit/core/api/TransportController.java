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

import gov.nist.hit.core.domain.Document;
import gov.nist.hit.core.domain.DocumentType;
import gov.nist.hit.core.domain.Json;
import gov.nist.hit.core.domain.KeyValuePair;
import gov.nist.hit.core.domain.MessageModel;
import gov.nist.hit.core.domain.MessageParserCommand;
import gov.nist.hit.core.domain.MessageValidationCommand;
import gov.nist.hit.core.domain.MessageValidationResult;
import gov.nist.hit.core.domain.TestStepTestingType;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.domain.Transaction;
import gov.nist.hit.core.domain.TransactionStatus;
import gov.nist.hit.core.domain.TransportConfig;
import gov.nist.hit.core.domain.SendRequest;
import gov.nist.hit.core.domain.User;
import gov.nist.hit.core.repo.TransactionRepository;
import gov.nist.hit.core.repo.TransportFormsRepository;
import gov.nist.hit.core.repo.UserRepository;
import gov.nist.hit.core.repo.TransportConfigRepository;
import gov.nist.hit.core.service.TestStepService;
import gov.nist.hit.core.service.TransportConfigService;
import gov.nist.hit.core.service.exception.MessageParserException;
import gov.nist.hit.core.service.exception.MessageValidationException;
import gov.nist.hit.core.service.exception.TestCaseException;
import gov.nist.hit.core.service.exception.UserNotFoundException;
import gov.nist.hit.core.service.exception.UserTokenIdNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RequestMapping("/transport")
@RestController
public  class TransportController {

  static final Logger logger = LoggerFactory.getLogger(DocumentationController.class);

  @Autowired
  private TransportFormsRepository transportFormsRepository;

  @Cacheable(value = "transportCache", key = "#type.name() + '-' + #protocol +  '-' + #domain + '-form'")
  @RequestMapping(value = "/form", method = RequestMethod.GET)
  public Json form(@RequestParam("type") TestStepTestingType type,@RequestParam("protocol") String protocol,@RequestParam("domain") String domain ) {
    String content = null;
    if(TestStepTestingType.SUT_INITIATOR.equals(type)){
      content = transportFormsRepository.getSutInitiatorFormByProtocolAndDomain(protocol, domain);
    }else if(TestStepTestingType.TA_INITIATOR.equals(type)){
      content = transportFormsRepository.getTaInitiatorFormByProtocolAndDomain(protocol, domain);
    }   
    logger.info("Fetching "+  type + " form of domain=" + domain + " and protocol=" + protocol);
    return new Json(content);
  }
  
  


}
