package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.MessageValidationResult;

import java.util.List;

public interface MessageValidationResultService {


  public void delete(MessageValidationResult report);

  public MessageValidationResult save(MessageValidationResult report);

  public void delete(Long id);

  public MessageValidationResult findOne(Long id);

  public void delete(List<MessageValidationResult> reports);

  public void save(List<MessageValidationResult> report);

  public MessageValidationResult findOneByTestStepAndUser(Long testStepId, Long userId);

}
