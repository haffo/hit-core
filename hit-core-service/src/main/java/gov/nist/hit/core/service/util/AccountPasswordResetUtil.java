/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgment if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */
package gov.nist.hit.core.service.util;


import gov.nist.auth.hit.core.domain.AccountPasswordReset;
import gov.nist.auth.hit.core.domain.util.UserUtil;

import java.io.Serializable;
import java.util.Date;

import org.springframework.security.crypto.codec.Base64;



/**
 * @author Harold Affo
 * 
 */
public class AccountPasswordResetUtil implements Serializable {

  private static final long serialVersionUID = 20130625L;
  public static final Long tokenValidityTimeInMilis = 172800000L;



  public static boolean isTokenExpired(AccountPasswordReset resetRequest) {
    boolean result = false;

    Long currentTimeInMilis = (new Date()).getTime();
    result =
        (currentTimeInMilis - resetRequest.getTimestamp().getTime()) > tokenValidityTimeInMilis;

    return result;
  }

  public static String getNewToken(AccountPasswordReset resetRequest) throws Exception {
    if (resetRequest.getUsername() == null) {
      throw new Exception("usernameIsNull");
    }

    String result = resetRequest.getUsername() + UserUtil.generateRandom();
    // base 64 encoding
    byte[] bs = Base64.encode(result.getBytes());

    result = new String(bs, "UTF-8");

    return result;
  }

}
