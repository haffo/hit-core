package gov.nist.hit.core.repo;

import gov.nist.hit.core.domain.TransportForms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransportFormsRepository extends JpaRepository<TransportForms, Long> {

  @Query("select tf from TransportForms tf where tf.protocol = :protocol")
  public TransportForms findOneByProtocol(@Param("protocol") String protocol);

  @Query("select tf.taInitiatorForm from TransportForms tf where tf.protocol = :protocol")
  public String getTaInitiatorFormByProtocol(@Param("protocol") String protocol);

  @Query("select tf.sutInitiatorForm from TransportForms tf where tf.protocol = :protocol")
  public String getSutInitiatorFormByProtocol(@Param("protocol") String protocol);


}
