package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.service.PasswordService;
import gov.nist.hit.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

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
 * <p/>
 * Created by Maxence Lefort on 5/11/16.
 */

@Service(value = "passwordService")
public class PasswordServiceImpl implements PasswordService {

    @Autowired
    private UserService userService;

    @Override
    public boolean compare(String username,String encryptedPassword) {
        if(userService.userExists(username)){
            User user = userService.retrieveUserByUsername(username);
            return user.getPassword().equals(encryptedPassword);
        }
        return false;
    }

    @Override
    public String getEncryptedPassword(String username) {
        if(userService.userExists(username)){
            User user = userService.retrieveUserByUsername(username);
            return user.getPassword();
        }
        return null;
    }


}

