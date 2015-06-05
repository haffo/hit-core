package gov.nist.healthcare.tools.core.repo;

import gov.nist.healthcare.tools.core.models.SoapEnvelopeTestContext;
import gov.nist.healthcare.tools.core.models.SutType;
import gov.nist.healthcare.tools.core.models.SoapTestCase;
import gov.nist.healthcare.tools.core.models.SoapTestPlan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SoapEnvelopeTestContextRepository extends
		JpaRepository<SoapEnvelopeTestContext, Long> {
//
//	@Query("select testContext from SoapEnvelopeTestContext testContext where testContext.testCase.id = :testCaseId")
//	SoapEnvelopeTestContext findOneByTestCaseId(@Param("testCaseId") Long testCaseId);

//	@Query("select distinct testContext.testCase.testPlan from SoapEnvelopeTestContext testContext order by testContext.testCase.testPlan.name ASC")
//	List<SoapTestPlan> findAllTestPlans();

//	@Query("select testContext.testCase from EnvelopeTestContext testContext where testContext.testCase.sutType = :sutType")
//	List<SoapTestCase> findAllBySutType(@Param("sutType") SutType sutType);

}
