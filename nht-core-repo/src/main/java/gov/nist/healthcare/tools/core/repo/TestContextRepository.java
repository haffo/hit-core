package gov.nist.healthcare.tools.core.repo;

import gov.nist.healthcare.tools.core.models.Message;
import gov.nist.healthcare.tools.core.models.Profile;
import gov.nist.healthcare.tools.core.models.TestCase;
import gov.nist.healthcare.tools.core.models.TestContext;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TestContextRepository extends 
		JpaRepository<TestContext, Long> {
 
}
