/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */

package gov.nist.auth.hit.core.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gov.nist.auth.hit.core.domain.TransportLog;

public interface TransportLogRepository extends JpaRepository<TransportLog, Long> {

	@Query("select log from TransportLog log where log.userId = :userId")
	public List<TransportLog> findByUserId(@Param("userId") Long userId);

	@Query("select log from TransportLog log where log.testStepId = :testStepId")
	public List<TransportLog> findByTestStepId(@Param("testStepId") Long testStepId);

	@Query("select log from TransportLog log where log.date >= :startDate and log.date <= :endDate order by log.date DESC")
	public List<TransportLog> findAllBetweenDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	@Query("select count(log) from TransportLog log where log.date >= :startDate and log.date <= :endDate order by log.date DESC")
	public int countBetweenDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
