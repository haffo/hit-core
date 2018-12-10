package gov.nist.hit.core.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.repo.TestPlanRepository;
import gov.nist.hit.core.service.TestPlanService;

@Service
public class TestPlanServiceImpl implements TestPlanService {

	@Autowired
	private TestPlanRepository testPlanRepository;

	@Override
	@Transactional(value = "transactionManager")
	public List<TestPlan> findShortAllByStageAndDomain(TestingStage stage, String domain) {
		return testPlanRepository.findShortAllByStageAndDomain(stage, domain);
	}

	@Override
	@Transactional(value = "transactionManager")
	public List<TestPlan> findShortAllByStageAndScopeAndDomain(TestingStage stage, TestScope scope, String domain) {
		return testPlanRepository.findShortAllByStageAndScopeAndDomain(stage, scope, domain);
	}

	@Override
	@Transactional(value = "transactionManager")
	public List<TestPlan> findAllShortByStageAndUsernameAndScopeAndDomain(TestingStage stage, String authorUsername,
			TestScope scope, String domain) {
		return testPlanRepository.findAllShortByStageAndUsernameAndScopeAndDomain(stage, authorUsername, scope, domain);
	}

	@Override
	@Transactional(value = "transactionManager")
	public List<TestArtifact> findAllTestPackagesByDomain(TestingStage stage, String domain) {
		return testPlanRepository.findAllTestPackagesByDomain(stage, domain);
	}

	@Override
	public TestPlan findOne(Long testPlanId) {
		// TODO Auto-generated method stub
		return testPlanRepository.findOne(testPlanId);
	}

	@Override
	@Transactional(value = "transactionManager")
	public List<TestPlan> findShortAllByStageAndAuthorAndDomain(TestingStage stage, String authorUsername,
			String domain) {
		return testPlanRepository.findShortAllByStageAndAuthorAndDomain(stage, authorUsername, domain);
	}

	@Override
	public List<TestPlan> findShortAllByStageAndScopeAndAuthorAndDomain(TestingStage stage, TestScope scope,
			String authorUsername, String domain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(value = "transactionManager")
	public void delete(TestPlan testPlan) {
		testPlanRepository.delete(testPlan);
	}

	@Override
	public TestPlan save(TestPlan testPlan) {
		// TODO Auto-generated method stub
		return testPlanRepository.save(testPlan);
	}

	@Override
	public TestPlan findByPersistentId(Long persistentId) {
		return testPlanRepository.getByPersistentId(persistentId);
	}

	@Override
	public boolean updateScope(TestPlan testPlan, TestScope scope) {
		testPlan.setScope(scope);
		Set<TestCase> testCases = testPlan.getTestCases();
		if (testCases != null) {
			for (TestCase testCase : testCases) {
				updateTestScope(testCase, scope);
			}
		}
		Set<TestCaseGroup> testCaseGroups = testPlan.getTestCaseGroups();
		if (testCaseGroups != null) {
			for (TestCaseGroup testCaseGroup : testCaseGroups) {
				updateTestScope(testCaseGroup, scope);
			}
		}
		save(testPlan);
		return true;
	}

	private void updateTestScope(TestCase testCase, TestScope scope) {
		testCase.setScope(scope);
		Set<TestStep> testSteps = testCase.getTestSteps();
		if (testSteps != null) {
			for (TestStep testStep : testSteps) {
				testStep.setScope(scope);
			}
		}
	}

	private void updateTestScope(TestCaseGroup testCaseGroup, TestScope scope) {
		testCaseGroup.setScope(scope);
		Set<TestCase> testCases = testCaseGroup.getTestCases();
		if (testCases != null) {
			for (TestCase testCase : testCases) {
				updateTestScope(testCase, scope);
			}
		}
	}

}
