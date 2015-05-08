package gov.nist.healthcare.tools.core.repo;

import gov.nist.healthcare.tools.core.models.ContextBasedTestStepContext;
import gov.nist.healthcare.tools.core.models.Message;
import gov.nist.healthcare.tools.core.models.Profile;
import gov.nist.healthcare.tools.core.models.TestCase;
import gov.nist.healthcare.tools.core.models.TestPlan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContextBasedTestStepContextRepository extends
		JpaRepository<ContextBasedTestStepContext, Long> {

	@Query("select testStepContext from ContextBasedTestStepContext testStepContext where testStepContext.testStep.id = :testStepId")
	ContextBasedTestStepContext findOneByTestStepId(
			@Param("testStepId") Long testStepId);

//	@Query("select testContext.message from ContextBasedTestStepContext testContext where testContext.testStep.id = :testStepId")
//	Message findExampleMessageByTestStepId(
//			@Param("testStepId") Long testStepId);

	@Query("select distinct testContext.testStep.testCase.testPlan from ContextBasedTestStepContext testContext order by testContext.testStep.testCase.testPlan.name ASC")
	List<TestPlan> findAllTestPlans();

//	@Query("select profile from Profile profile where profile.testStepContext.testStep.id = :testStepId")
//	Profile findProfileByTestStepId(@Param("testStepId") Long testStepId);
//
//	@Query("select profile.xml from Profile profile where profile.testStepContext.testStep.id = :testStepId")
//	String getProfileXmlByTestStepId(@Param("testStepId") Long testStepId);

}
