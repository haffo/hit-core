package gov.nist.hit.core.service;

import java.util.List;

import gov.nist.auth.hit.core.domain.TransportLog;

public interface TransportLogService {

  public TransportLog findOne(Long id);

  public List<TransportLog> findAll();

  public TransportLog save(TransportLog log);

  public List<TransportLog> findByUserId(Long userId);

  public List<TransportLog> findByTestStepId(Long testStepId);

  public long countAll();

  public void delete(Long id);


}
