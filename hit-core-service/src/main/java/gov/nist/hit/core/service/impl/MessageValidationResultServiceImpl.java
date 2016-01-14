package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.MessageValidationResult;
import gov.nist.hit.core.repo.MessageValidationResultRepository;
import gov.nist.hit.core.service.MessageValidationResultService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageValidationResultServiceImpl implements MessageValidationResultService {

  @Autowired
  private MessageValidationResultRepository validationReportRepository;


  @Override
  public void delete(MessageValidationResult report) {
    validationReportRepository.delete(report);
  }

  @Override
  public MessageValidationResult save(MessageValidationResult report) {
    return validationReportRepository.saveAndFlush(report);
  }


  @Override
  public void delete(Long id) {
    validationReportRepository.delete(id);
  }

  @Override
  public MessageValidationResult findOne(Long id) {
    return validationReportRepository.findOne(id);
  }

  @Override
  public void delete(List<MessageValidationResult> reports) {
    validationReportRepository.delete(reports);
  }

  @Override
  public void save(List<MessageValidationResult> reports) {
    validationReportRepository.save(reports);
  }

  @Override
  public MessageValidationResult findOneByTestStepAndUser(Long testStepId, Long userId) {
    return validationReportRepository.findOneByTestStepAndUser(userId, testStepId);
  }



}
