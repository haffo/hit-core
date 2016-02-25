package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.TransportForms;
import gov.nist.hit.core.repo.TransportFormsRepository;
import gov.nist.hit.core.service.TransportFormsService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransportFormsServiceImpl implements TransportFormsService {

  @Autowired
  private TransportFormsRepository transportFormsRepository;

  @Override
  public TransportForms findOneByProtocolAndDomain(String protocol, String domain) {
    return transportFormsRepository.findOneByProtocolAndDomain(protocol, domain);
  }

  @Override
  public String getTaInitiatorFormByProtocolAndDomain(String protocol, String domain) {
    return transportFormsRepository.getTaInitiatorFormByProtocolAndDomain(protocol, domain);
  }

  @Override
  public String getSutInitiatorFormByProtocolAndDomain(String protocol, String domain) {
    return transportFormsRepository.getSutInitiatorFormByProtocolAndDomain(protocol, domain);
  }

  @Override
  public List<TransportForms> findAll() {
    return transportFormsRepository.findAll();
  }

}
