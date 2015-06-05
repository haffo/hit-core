package gov.nist.healthcare.tools.core.repo;

   
import gov.nist.healthcare.tools.core.models.User;
import gov.nist.healthcare.tools.core.models.SoapConnectivityTransaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends
		JpaRepository<User, Long> {
	
	@Query("select user from User user where user.username = :username and user.password = :password")
	User findOneByUsernameAndPassword(@Param("username") String username,@Param("password") String password);

 
	
}
