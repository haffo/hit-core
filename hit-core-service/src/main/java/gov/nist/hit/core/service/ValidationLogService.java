package gov.nist.hit.core.service;

import java.util.List;

import gov.nist.auth.hit.core.domain.ValidationLog;
import gov.nist.healthcare.unified.model.EnhancedReport;
import gov.nist.hit.core.domain.TestContext;

public interface ValidationLogService {

  public ValidationLog findOne(Long id);

  public List<ValidationLog> findAll();

  public ValidationLog save(ValidationLog log);

  public List<ValidationLog> findByUserId(Long userId);

  public List<ValidationLog> findByTestStepId(Long testStepId);

  public ValidationLog generateAndSave(Long userId, TestContext testContext, EnhancedReport report);

  public List<ValidationLog> findByUserIdAndStage(Long userId, String testingStage);

  public long countAll();

  public void delete(Long id);



}
