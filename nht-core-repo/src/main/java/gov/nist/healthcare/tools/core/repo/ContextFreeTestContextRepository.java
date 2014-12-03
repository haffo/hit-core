package gov.nist.healthcare.tools.core.repo;

import gov.nist.healthcare.tools.core.models.ContextFreeTestContext;
import gov.nist.healthcare.tools.core.models.Message;
import gov.nist.healthcare.tools.core.models.Profile;
import gov.nist.healthcare.tools.core.models.TestCase;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContextFreeTestContextRepository extends
		JpaRepository<ContextFreeTestContext, Long> {

	@Query("select testContext from ContextFreeTestContext testContext where testContext.testCase.id = :testCaseId")
	ContextFreeTestContext findOneByTestCaseId(
			@Param("testCaseId") Long testCaseId);

	@Query("select testContext.exampleMessages from ContextFreeTestContext testContext where testContext.testCase.id = :testCaseId")
	List<Message> findExampleMessagesByTestCaseId(
			@Param("testCaseId") Long testCaseId);

	@Query("select distinct testContext.testCase from ContextFreeTestContext testContext")
	List<TestCase> findAllTestCases();

	@Query("select profile from Profile profile where profile.testContext.testCase.id = :testCaseId")
	Profile findProfileByTestCaseId(@Param("testCaseId") Long testCaseId);

	@Query("select profile.content from Profile profile where profile.testContext.testCase.id = :testCaseId")
	String getProfileContentByTestCaseId(@Param("testCaseId") Long testCaseId);

}
