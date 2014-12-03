package gov.nist.healthcare.tools.core.repo;

import gov.nist.healthcare.tools.core.models.ConnectivityTestContext;
import gov.nist.healthcare.tools.core.models.TestPlan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConnectivityTestContextRepository extends
		JpaRepository<ConnectivityTestContext, Long> {

	@Query("select testContext.message from ConnectivityTestContext testContext where testContext.testCase.id = :testCaseId")
	String getMessageByTestCaseId(@Param("testCaseId") Long testCaseId);

	@Query("select testContext.message from ConnectivityTestContext testContext where testContext.id = :testContextId")
	String getMessageByTestContextId(@Param("testContextId") Long testContextId);

	@Query("select testContext from ConnectivityTestContext testContext where testContext.testCase.id = :testCaseId")
	ConnectivityTestContext findOneByTestCaseId(
			@Param("testCaseId") Long testCaseId);

	@Query("select distinct testContext.testCase.testPlan from ConnectivityTestContext testContext")
	List<TestPlan> findAllTestPlans();
	//
	// @Query("select distinct testContext.testCase.sutType from ConnectivityTestContext testContext")
	// List<SutType> findAllSutTypes();

	// @Query("select testContext.testCase from ConnectivityTestContext testContext where testContext.testCase.sutType = :sutType")
	// List<TestCase> findAllBySutType(@Param("sutType") SutType sutType);

}
