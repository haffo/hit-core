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

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gov.nist.auth.hit.core.domain.ValidationLog;

public interface ValidationLogRepository extends JpaRepository<ValidationLog, Long> {


  @Query("select log from ValidationLog log where log.userId = :userId")
  public List<ValidationLog> findByUserId(@Param("userId") Long userId);

  @Query("select log from ValidationLog log where log.testStepId = :testStepId")
  public List<ValidationLog> findByTestStepId(@Param("testStepId") Long testStepId);

  @Query("select log from ValidationLog log where log.userId = :userId and log.testingStage = :testingStage")
  public List<ValidationLog> findByUserIdAndStage(@Param("userId") Long userId,
      @Param("testingStage") String testingStage);


}
