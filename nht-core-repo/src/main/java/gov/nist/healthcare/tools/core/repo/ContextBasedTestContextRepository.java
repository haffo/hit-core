package gov.nist.healthcare.tools.core.repo;

import gov.nist.healthcare.tools.core.models.ContextBasedTestContext;
import gov.nist.healthcare.tools.core.models.Message;
import gov.nist.healthcare.tools.core.models.Profile;
import gov.nist.healthcare.tools.core.models.TestPlan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContextBasedTestContextRepository extends
		JpaRepository<ContextBasedTestContext, Long> {

	@Query("select testContext from ContextBasedTestContext testContext where testContext.testCase.id = :testCaseId")
	ContextBasedTestContext findOneByTestCaseId(
			@Param("testCaseId") Long testCaseId);

	@Query("select testContext.exampleMessages from ContextBasedTestContext testContext where testContext.testCase.id = :testCaseId")
	List<Message> findExampleMessagesByTestCaseId(
			@Param("testCaseId") Long testCaseId);

	@Query("select distinct testContext.testCase.testPlan from ContextBasedTestContext testContext")
	List<TestPlan> findAllTestPlans();

	@Query("select profile from Profile profile where profile.testContext.testCase.id = :testCaseId")
	Profile findProfileByTestCaseId(@Param("testCaseId") Long testCaseId);

	@Query("select profile.content from Profile profile where profile.testContext.testCase.id = :testCaseId")
	String getProfileContentByTestCaseId(@Param("testCaseId") Long testCaseId);

}
