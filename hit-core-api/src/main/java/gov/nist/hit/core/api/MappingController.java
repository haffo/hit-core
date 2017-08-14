package gov.nist.hit.core.api;

import gov.nist.hit.core.service.TestCaseExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
 * <p>
 * Created by Maxence Lefort on 6/20/17.
 */
@RequestMapping("/mapping")
@RestController
public class MappingController {
    @Autowired
    TestCaseExecutionService testCaseExecutionService;

    Logger logger = LoggerFactory.getLogger(MappingController.class);


    @RequestMapping(value = "/{testCaseId}", method = RequestMethod.GET, produces = "application/json")
    public void cleanRecords(HttpServletRequest request) throws
        IOException {
        Long userId = SessionContext.getCurrentUserId(request.getSession(false));
        if (userId != null) {
            logger.info("Cleaning mapping records for userId " +userId);
            testCaseExecutionService.clearRecords(userId);
        }
    }
}
