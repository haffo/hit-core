package gov.nist.hit.core.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nist.auth.hit.core.domain.ValidationLog;
import gov.nist.auth.hit.core.repo.ValidationLogRepository;
import gov.nist.healthcare.unified.exceptions.ConversionException;
import gov.nist.healthcare.unified.exceptions.NotFoundException;
import gov.nist.healthcare.unified.model.Classification;
import gov.nist.healthcare.unified.model.Collection;
import gov.nist.healthcare.unified.model.Detections;
import gov.nist.healthcare.unified.model.EnhancedReport;
import gov.nist.healthcare.unified.model.Section;
import gov.nist.hit.core.domain.TestContext;
import gov.nist.hit.core.service.ValidationLogService;
import gov.nist.hit.core.service.util.ValidationLogUtil;

@Service
public class ValidationLogServiceImpl implements ValidationLogService {

	static final Logger logger = LoggerFactory.getLogger(ValidationLogServiceImpl.class);

	@Autowired
	private ValidationLogRepository validationLogRepository;

	@Override
	public ValidationLog findOne(Long id) {
		// TODO Auto-generated method stub
		return validationLogRepository.findOne(id);
	}

	@Override
	public ValidationLog save(ValidationLog log) {
		// TODO Auto-generated method stub
		return validationLogRepository.saveAndFlush(log);
	}

	@Override
	public List<ValidationLog> findByUserId(Long userId) {
		// TODO Auto-generated method stub
		return validationLogRepository.findByUserId(userId);
	}

	@Override
	public List<ValidationLog> findByUserIdAndStage(Long userId, String testingStage) {
		// TODO Auto-generated method stub
		return validationLogRepository.findByUserIdAndStage(userId, testingStage);
	}

	@Override
	public List<ValidationLog> findByTestStepId(Long testStepId) {
		// TODO Auto-generated method stub
		return validationLogRepository.findByTestStepId(testStepId);
	}

	@Override
	public ValidationLog generateAndSave(Long userId, TestContext testContext, EnhancedReport report) {
		ValidationLog log = new ValidationLog();
		log.setUserId(userId);
		log.setTestStepId(testContext.getTestStep().getPersistentId());
		log.setDate(new Date());
		log.setErrorCountInSegment(new HashMap<>());
		log.setMessage(report.getMessage());
		Detections detections = report.getDetections();
		// Loop on the classifications (Affirmative, Warning or Error)
		for (Classification classification : detections.classes()) {
			if (classification.getName().equals("Affirmative")) {
				// No need to display any log here
			} else if (classification.getName().equals("Warning")) {
				// Get the warning count
				log.setWarningCount(classification.keys().size());
			} else if (classification.getName().equals("Error")) {
				// Get the error count
				log.setErrorCount(classification.keys().size());
				if (log.getErrorCount() > 0) {
					// If there is more than 0 errors, then the validation
					// failed
					log.setValidationResult(false);
					// Loop on the errors
					for (String key : classification.keys()) {
						Collection collection = null;
						try {
							collection = classification.getArray(key);
							if (collection != null && collection.size() > 0) {
								for (int i = 0; i < collection.size(); i++) {
									Section section = collection.getObject(i);
									// Identify the path of the error
									String path = section.getString("path");
									if (path != null && !"".equals(path)) {
										path = path.split("\\[")[0];
										int segmentErrorCount = 1;
										// If there was already at least 1 error
										// for this segment, then increment its
										// error count.
										if (log.getErrorCountInSegment().containsKey(path)) {
											segmentErrorCount = log.getErrorCountInSegment().get(path) + 1;
										}
										// Add or update the segment's error
										// count
										log.getErrorCountInSegment().put(path, segmentErrorCount);
									}
								}
							}
						} catch (NotFoundException e) {
							e.printStackTrace();
						} catch (ConversionException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		// Parse the test context
		if (testContext != null) {
			log.setFormat((testContext.getFormat() != null ? testContext.getFormat() : ""));
			log.setMessageId((testContext.getType() != null ? testContext.getType() : ""));
			log.setTestingStage((testContext.getStage().name() != null ? testContext.getStage().name() : ""));
		}
		String validationLog = ValidationLogUtil.toString(log);
		logger.info(validationLog.toString());
		return save(log);
	}

	@Override
	public List<ValidationLog> findAll() {
		// TODO Auto-generated method stub
		return validationLogRepository.findAll();
	}

	@Override
	public long countAll() {
		// TODO Auto-generated method stub
		return validationLogRepository.count();
	}

}
