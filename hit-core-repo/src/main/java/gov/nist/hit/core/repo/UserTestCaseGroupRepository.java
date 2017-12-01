package gov.nist.hit.core.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gov.nist.hit.core.domain.CFTestPlan;

@Repository
public interface UserTestCaseGroupRepository extends JpaRepository<CFTestPlan, Long> {

	public List<CFTestPlan> findByPreloaded(boolean preloaded);
	
	@Query("select tcg from CFTestPlan tcg where tcg.authorUsername = :authorUsername and preloaded = false and scope = 'USER'")
	public List<CFTestPlan> userExclusive(@Param("authorUsername") String authorUsername);
	
	@Modifying
	@Query("delete CFTestPlan  where id = :id")
	public int deleteUserTestCaseGroup(@Param("id") String id);
	
}
