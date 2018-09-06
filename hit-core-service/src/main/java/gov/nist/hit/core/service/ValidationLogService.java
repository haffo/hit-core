package gov.nist.hit.core.service;

import java.util.List;

import gov.nist.auth.hit.core.domain.ValidationLog;
import gov.nist.healthcare.unified.model.EnhancedReport;
import gov.nist.hit.core.domain.TestContext;

public interface ValidationLogService {

	public ValidationLog findOne(Long id);

	public List<ValidationLog> findAll(String domain);

	public ValidationLog save(ValidationLog log);

	public List<ValidationLog> findByUserId(Long userId, String domain);

	public List<ValidationLog> findByTestStepId(Long testStepId, String domain);

	public ValidationLog generateAndSave(Long userId, TestContext testContext, EnhancedReport report);

	public List<ValidationLog> findByUserIdAndStage(Long userId, String testingStage, String domain);

	public long countAll(String domain);

	public void delete(Long id);

}
