package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.TransportMessage;

import java.util.List;
import java.util.Map;


public interface TransportMessageService {

  Long findMessageIdByProperties(Map<String, String> criteria);

  // String getOutgoingMessageByUserIdAndTestStepId(Long userId, Long testStepId);

  // List<TransportMessage> findAllByUser(Long userId);
  //
  // List<TransportMessage> findAllByProperties(Map<String, String> criteria);

  TransportMessage findOneByProperties(Map<String, String> criteria);

  // void delete(List<TransportMessage> messages);

  void delete(TransportMessage message);

  void delete(Long id);

  TransportMessage save(TransportMessage message);

  // List<TransportMessage> save(List<TransportMessage> messages);

  TransportMessage findOne(Long id);

  List<TransportMessage> findAllByProperties(Map<String, String> criteria);

  void delete(List<TransportMessage> confs);


}
