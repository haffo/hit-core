package gov.nist.hit.core.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import gov.nist.auth.hit.core.domain.TransportLog;
import gov.nist.auth.hit.core.repo.TransportLogRepository;
import gov.nist.hit.core.service.TransportLogService;

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
  public List<TransportLog> findByUserId(Long userId) {
    // TODO Auto-generated method stub
    return transportLogRepository.findByUserId(userId);
  }


  @Override
  public List<TransportLog> findByTestStepId(Long testStepId) {
    // TODO Auto-generated method stub
    return transportLogRepository.findByTestStepId(testStepId);
  }


  @Override
  public List<TransportLog> findAll() {
    // TODO Auto-generated method stub
    return transportLogRepository.findAll(sortByDateDsc());
  }

  @Override
  public long countAll() {
    // TODO Auto-generated method stub
    return transportLogRepository.count();
  }

  @Override
  public void delete(Long id) {
    transportLogRepository.delete(id);
  }



  private Sort sortByDateDsc() {
    return new Sort(Sort.Direction.DESC, "date");
  }

}
