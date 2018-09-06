package gov.nist.hit.core.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nist.auth.hit.core.domain.TransportLog;
import gov.nist.auth.hit.core.repo.TransportLogRepository;
import gov.nist.hit.core.service.TransportLogService;
import gov.nist.hit.core.service.util.DateUtil;

@Service
public class TransportLogServiceImpl implements TransportLogService {

	static final Logger logger = LoggerFactory.getLogger(TransportLogServiceImpl.class);

	@Autowired
	private TransportLogRepository transportLogRepository;

	@Override
	public TransportLog findOne(Long id) {
		// TODO Auto-generated method stub
		return transportLogRepository.findOne(id);
	}

	@Override
	public TransportLog save(TransportLog log) {
		// TODO Auto-generated method stub
		return transportLogRepository.saveAndFlush(log);
	}

	@Override
	public List<TransportLog> findByUserId(Long userId, String domain) {
		// TODO Auto-generated method stub
		return transportLogRepository.findByUserId(userId, domain);
	}

	@Override
	public List<TransportLog> findByTestStepId(Long testStepId, String domain) {
		// TODO Auto-generated method stub
		return transportLogRepository.findByTestStepId(testStepId, domain);
	}

	@Override
	public List<TransportLog> findAll(String domain) {
		// TODO Auto-generated method stub
		Date current = new Date();
		Date startDate = DateUtil.getFirstDateOfMonth(current);
		Date endDate = DateUtil.getLastDateOfMonth(current);
		return transportLogRepository.findAllBetweenDate(startDate, endDate, domain);
	}

	@Override
	public long countAll(String domain) {
		Date current = new Date();
		Date startDate = DateUtil.getFirstDateOfMonth(current);
		Date endDate = DateUtil.getLastDateOfMonth(current);
		// TODO Auto-generated method stub
		return transportLogRepository.countBetweenDate(startDate, endDate, domain);

	}

	@Override
	public void delete(Long id) {
		transportLogRepository.delete(id);
	}

}
