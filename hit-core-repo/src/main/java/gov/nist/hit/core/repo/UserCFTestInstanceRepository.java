package gov.nist.hit.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gov.nist.hit.core.domain.CFTestStep;

@Repository
public interface UserCFTestInstanceRepository extends JpaRepository<CFTestStep, Long> {

	@Modifying
	@Query("delete CFTestStep  where id = :id")
	public int deleteCFTestInstance(@Param("id") Long id);
	
}
