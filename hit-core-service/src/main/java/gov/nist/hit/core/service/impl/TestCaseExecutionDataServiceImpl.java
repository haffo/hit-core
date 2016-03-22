package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.TestCaseExecutionData;
import gov.nist.hit.core.repo.TestCaseExecutionDataRepository;
import gov.nist.hit.core.service.TestCaseExecutionDataService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Created by Maxence Lefort on 2/10/16.
 */
@Service
public class TestCaseExecutionDataServiceImpl implements TestCaseExecutionDataService {

    @Autowired
    TestCaseExecutionDataRepository testCaseExecutionDataRepository;

    @Override
    public TestCaseExecutionData getTestCaseExecutionData(Long testStepFieldPairId, Long testCaseExecutionId) {
        return testCaseExecutionDataRepository.getTestCaseExecutionDataFromTestStepFieldPairIdAndTestCaseExecutionId(testStepFieldPairId,testCaseExecutionId);
    }

    @Override
    public TestCaseExecutionData save(TestCaseExecutionData testCaseExecutionData) {
        TestCaseExecutionData existingTestCaseExecutionData;
        if((existingTestCaseExecutionData = testCaseExecutionDataRepository.getTestCaseExecutionDataFromTestStepFieldPairIdAndTestCaseExecutionId(testCaseExecutionData.getTestStepFieldPair().getId(),testCaseExecutionData.getTestCaseExecution().getId()))!=null){
            testCaseExecutionData.setId(existingTestCaseExecutionData.getId());
        }
        return testCaseExecutionDataRepository.saveAndFlush(testCaseExecutionData);
    }

    @Override
    public void delete(TestCaseExecutionData testCaseExecutionData) {
        testCaseExecutionDataRepository.delete(testCaseExecutionData);
    }

    @Override
    public void delete(Long testCaseExecutionDataId) {
        testCaseExecutionDataRepository.delete(testCaseExecutionDataId);
    }

}
