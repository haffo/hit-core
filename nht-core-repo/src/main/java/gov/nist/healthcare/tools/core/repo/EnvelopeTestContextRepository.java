package gov.nist.healthcare.tools.core.repo;

import gov.nist.healthcare.tools.core.models.EnvelopeTestContext;
import gov.nist.healthcare.tools.core.models.SutType;
import gov.nist.healthcare.tools.core.models.TestCase;
import gov.nist.healthcare.tools.core.models.TestPlan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnvelopeTestContextRepository extends
		JpaRepository<EnvelopeTestContext, Long> {

	@Query("select testContext from EnvelopeTestContext testContext where testContext.testCase.id = :testCaseId")
	EnvelopeTestContext findOneByTestCaseId(@Param("testCaseId") Long testCaseId);

	@Query("select distinct testContext.testCase.testPlan from EnvelopeTestContext testContext")
	List<TestPlan> findAllTestPlans();

	@Query("select testContext.testCase from EnvelopeTestContext testContext where testContext.testCase.sutType = :sutType")
	List<TestCase> findAllBySutType(@Param("sutType") SutType sutType);

}
