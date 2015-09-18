package gov.nist.hit.core.repo;

import gov.nist.hit.core.domain.TestContext;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestContextRepository extends JpaRepository<TestContext, Long> {

}
