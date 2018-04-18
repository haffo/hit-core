package gov.nist.hit.core.service;

import java.util.List;

import gov.nist.healthcare.unified.model.EnhancedReport;
import gov.nist.hit.core.domain.TestContext;
import gov.nist.hit.core.domain.log.ValidationLog;

public interface ValidationLogService {

  public ValidationLog findOne(Long id);

  public ValidationLog save(ValidationLog log);

  public List<ValidationLog> findByUserId(Long userId);

  public List<ValidationLog> findByTestStepId(Long testStepId);

  public ValidationLog generateAndSave(Long userId, TestContext testContext, EnhancedReport report);


}
