package gov.nist.hit.core.repo;

import gov.nist.hit.core.domain.Message;
import gov.nist.hit.core.domain.Profile;
import gov.nist.hit.core.domain.SoapTestCase;
import gov.nist.hit.core.domain.TestContext;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TestContextRepository extends 
		JpaRepository<TestContext, Long> {
 
}
