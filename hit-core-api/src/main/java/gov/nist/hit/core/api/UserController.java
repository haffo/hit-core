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

import gov.nist.hit.core.domain.User;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.exception.UserNotFoundException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Transactional()
  @RequestMapping(value = "/current", method = RequestMethod.POST)
  public User create(HttpSession session) throws UserNotFoundException {
    User user = null;
    Long id = SessionContext.getCurrentUserId(session);
    if (id != null) {
      user = userService.findOne(id);
    }
    if (user == null) {
      user = new User();
      userService.save(user);
      SessionContext.setCurrentUserId(session, user.getId());
    }
    return user;
  }

  @Transactional()
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public boolean delete(HttpSession session) throws UserNotFoundException {
    Long id = SessionContext.getCurrentUserId(session);
    if (id != null) {
      userService.delete(id);
    }
    SessionContext.setCurrentUserId(session, null);
    return true;
  }



}
