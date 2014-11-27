package gov.nist.healthcare.tools.core.repo;

   
import gov.nist.healthcare.tools.core.models.TransactionStatus;
import gov.nist.healthcare.tools.core.models.UserTransaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserTransactionRepository extends
		JpaRepository<UserTransaction, Long> {

	@Query("select incoming from UserTransaction transaction where transaction.user.tokenId = :tokenId")
	String getIncomingMessageByTokenId(@Param("tokenId") String tokenId);
	
	@Query("select outgoing from UserTransaction transaction where transaction.user.tokenId = :tokenId")
	String getOutgoingMessageByTokenId(@Param("tokenId") String tokenId);
	
	@Query("select transaction from UserTransaction transaction where transaction.user.tokenId = :tokenId")
	UserTransaction findOneByTokenId(@Param("tokenId") String tokenId);
 
	@Query("select transaction.user.tokenId from UserTransaction transaction where transaction.user.username = :username")
	String findTokenIdByUsername(@Param("username") String username);
	
	
	@Query("select transaction.status from UserTransaction transaction where transaction.user.tokenId = :tokenId")
	TransactionStatus getStatusByTokenId(@Param("tokenId") String tokenId);
	
	
	
}
