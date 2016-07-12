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

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RestController
@RequestMapping("/session")
public class GuestSessionController {

  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public boolean destroy(HttpServletRequest request) {
    // HttpSession session = request.getSession(false);
    // if (session != null) {
    // session.invalidate();
    // }
    return true;
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public boolean create(HttpServletRequest request) {
    // HttpSession session = request.getSession(true);
    return true;
  }


  @RequestMapping(method = RequestMethod.GET, value = "/keepAlive")
  public boolean keepAlive(HttpServletRequest request) {
    return true;
  }


}
