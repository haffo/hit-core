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

import gov.nist.hit.core.domain.AppInfo;
import gov.nist.hit.core.domain.KeyValuePair;
import gov.nist.hit.core.domain.Transaction;
import gov.nist.hit.core.domain.TransportConfig;
import gov.nist.hit.core.domain.User;
import gov.nist.hit.core.repo.AppInfoRepository;
import gov.nist.hit.core.repo.TransactionRepository;
import gov.nist.hit.core.repo.TransactionSpecs;
import gov.nist.hit.core.repo.TransportConfigRepository;
import gov.nist.hit.core.repo.UserRepository;
import gov.nist.hit.core.service.TransactionService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TransportConfigRepository transportConfigRepository;
  
  @Autowired
  private TransactionService transactionService;
  
  
  
  
  
//  /**
//   * TODO:REMOVE
//   * @return
//   */
//  @Transactional()
//  @RequestMapping(value = "/test", method = RequestMethod.GET)
//  public boolean checkProperties() {
//    Map<String,String> properties = new HashMap<String,String>();
//    properties.put("username", "dev-1");
//    properties.put("password", "dev-1");
//    properties.put("facilityID", "dev-1");
//    Transaction transaction = new Transaction();
//    transaction.setIncoming("ddsddsds");
//    transaction.setOutgoing("dddd");
//    transaction.setProperties(properties);
//    transactionRepository.saveAndFlush(transaction);
//    transaction = transactionRepository.findOne((where(TransactionSpecs.matches(properties))));
////    transaction = transactionRepository.findOneByProperties(properties);
//
//    return transaction != null ;   
//  }
  
  public UserRepository getUserRepository() {
    return userRepository;
  }

  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public TransportConfigRepository getTransportConfigRepository() {
    return transportConfigRepository;
  }

  public void setTransportConfigRepository(TransportConfigRepository transportConfigRepository) {
    this.transportConfigRepository = transportConfigRepository;
  }

  public TransactionService getTransactionService() {
    return transactionService;
  }

  public void setTransactionService(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @Transactional()
  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public User create() {
    User user = new User();
    userRepository.saveAndFlush(user); 
    return user;
  }

  @Modifying  
  @Transactional
  @RequestMapping(value = "/{userId}/delete", method = RequestMethod.POST)
  public boolean delete(@PathVariable Long userId) {
    List<TransportConfig> configs = transportConfigRepository.findAllByUser(userId);
    if(configs != null){
      transportConfigRepository.delete(configs);
    }
    
    List<Transaction> transactions = transactionService.findAllByUser(userId);
    if(transactions != null){
      transactionService.delete(transactions);
    }

    userRepository.delete(userId);
    return true;
  }

}
