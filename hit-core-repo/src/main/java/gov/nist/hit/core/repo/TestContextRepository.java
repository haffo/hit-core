package gov.nist.hit.core.repo;

import gov.nist.hit.core.domain.Message;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.domain.TestContext;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TestContextRepository extends JpaRepository<TestContext, Long> {

  @Query("select tc.message from TestContext tc where tc.stage = :stage")
  public List<Message> findAllExampleMessages(@Param("stage") TestingStage stage);

  @Query("select tc from TestContext tc where tc.message.id = :messageId")
  public TestContext findOneByMessageId(@Param("messageId") Long messageId);
  
}