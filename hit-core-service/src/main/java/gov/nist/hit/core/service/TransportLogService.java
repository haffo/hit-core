package gov.nist.hit.core.service;

import java.util.List;

import gov.nist.auth.hit.core.domain.TransportLog;

public interface TransportLogService {

	public TransportLog findOne(Long id);

	public List<TransportLog> findAll(String domain);

	public TransportLog save(TransportLog log);

	public List<TransportLog> findByUserId(Long userId, String domain);

	public List<TransportLog> findByTestStepId(Long testStepId, String domain);

	public long countAll(String domain);

	public void delete(Long id);

}
