package gov.nist.hit.core.repo;

import gov.nist.hit.core.domain.ConformanceProfile;
import gov.nist.hit.core.domain.TestContext;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TestContextRepository extends JpaRepository<TestContext, Long> {

  @Query("select tc.conformanceProfile from TestContext tc where tc.id = :id")
  public ConformanceProfile findConformanceProfileByTestContextId(@Param("id") Long id);
}
