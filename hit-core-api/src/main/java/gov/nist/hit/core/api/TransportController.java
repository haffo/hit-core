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
import gov.nist.hit.core.domain.SaveConfigRequest;
import gov.nist.hit.core.domain.TestStepTestingType;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.domain.Transaction;
import gov.nist.hit.core.domain.TransactionStatus;
import gov.nist.hit.core.domain.TransportConfig;
import gov.nist.hit.core.domain.SendRequest;
import gov.nist.hit.core.domain.TransportFormContent;
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
  
  @Autowired
  private TransportConfigRepository transportConfigRepository;
  
  @Autowired
  private TransactionRepository transactionRepository;
  

  //@Cacheable(value = "transportCache", key = "#type.name() + '-' + #protocol +  '-' + #domain + '-form'")
  @RequestMapping(value = "/config/form", method = RequestMethod.GET)
  public TransportFormContent form(@RequestParam("type") TestStepTestingType type,@RequestParam("protocol") String protocol ) {
    String content = null;
    if(TestStepTestingType.SUT_INITIATOR.equals(type)){
      content = transportFormsRepository.getSutInitiatorFormByProtocol(protocol);
    }else if(TestStepTestingType.TA_INITIATOR.equals(type)){
      content = transportFormsRepository.getTaInitiatorFormByProtocol(protocol);
    }   
    logger.info("Fetching  form of type="+  type + " and protocol=" + protocol);
    return new TransportFormContent(content);
  }
  
  @RequestMapping(value = "/config/save", method = RequestMethod.POST)
  public boolean saveConfig(@RequestBody SaveConfigRequest request) {
    TransportConfig config = transportConfigRepository.findOneByUserAndProtocol(request.getUserId(), request.getProtocol());
    if(config != null){
      if(TestStepTestingType.SUT_INITIATOR.equals(request.getType())){
        config.setSutInitiator(request.getConfig());
      }else if(TestStepTestingType.TA_INITIATOR.equals(request.getType())){
        config.setTaInitiator(request.getConfig());
      }
      transportConfigRepository.save(config);
    }
    return true;
  }
  
  @RequestMapping(value = "/transaction/{transactionId}/delete", method = RequestMethod.POST)
  public boolean deleteTransaction(@PathVariable Long transactionId) {
    transactionRepository.delete(transactionId);
    return true;
  }
  
  


}
