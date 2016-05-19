package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.TestCaseExecution;
import gov.nist.hit.core.repo.TestCaseExecutionRepository;
import gov.nist.hit.core.service.TestCaseExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
 * Created by Maxence Lefort on 2/10/16.
 */
@Service
public class TestCaseExecutionServiceImpl implements TestCaseExecutionService {

    private Logger logger = LoggerFactory.getLogger(TestCaseExecutionService.class);

    @Autowired
    TestCaseExecutionRepository testCaseExecutionRepository;

    @Override
    public TestCaseExecution save(TestCaseExecution testCaseExecution) {
        TestCaseExecution testCaseExecutionSaved = testCaseExecutionRepository.saveAndFlush(testCaseExecution);
        logger.info("Test case execution saved : "+testCaseExecutionSaved.getId());

        return testCaseExecutionSaved;
    }

    @Override
    public void delete(TestCaseExecution testCaseExecution) {
        testCaseExecutionRepository.delete(testCaseExecution);
    }

    @Override
    public void delete(Long testCaseExecutionId) {
        testCaseExecutionRepository.delete(testCaseExecutionId);
    }

    @Override
    public TestCaseExecution findOneByUserId(Long userId) {
        return testCaseExecutionRepository.getTestCaseExecutionFromUserId(userId);
    }

}
