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

package gov.nist.hit.core.repo;

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestingStage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TestPlanRepository extends JpaRepository<TestPlan, Long> {

  @Query("select tp from TestPlan tp where tp.stage = :stage")
  public List<TestPlan> findAllByStage(@Param("stage") TestingStage stage);

  @Query("select tp.testPackage from TestPlan tp where tp.stage = :stage")
  public List<TestArtifact> findAllTestPackages(@Param("stage") TestingStage stage);

  @Query("select tp.testPlanSummary from TestPlan tp where tp.stage = :stage")
  public List<TestArtifact> findAllTestPlanSummary(@Param("stage") TestingStage stage);

  @Query("select tp.testPlanSummary from TestPlan tp where tp.id = :id")
  public TestArtifact testPlanSummary(@Param("id") Long id);

  @Query("select tp.testPackage from TestPlan tp where tp.id = :id")
  public TestArtifact testPackage(@Param("id") Long id);



}
