package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.TransportForms;

import java.util.List;

public interface TransportFormsService {

  public TransportForms findOneByProtocolAndDomain(String protocol, String domain);

  public String getTaInitiatorFormByProtocolAndDomain(String protocol, String domain);

  public String getSutInitiatorFormByProtocolAndDomain(String protocol, String domain);

  public List<TransportForms> findAll();

}
