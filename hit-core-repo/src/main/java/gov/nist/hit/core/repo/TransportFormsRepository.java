package gov.nist.hit.core.repo;

import gov.nist.hit.core.domain.TransportForms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransportFormsRepository extends JpaRepository<TransportForms, Long> {

  @Query("select tf from TransportForms tf where tf.protocol = :protocol and tf.domain = :domain")
  public TransportForms findOneByProtocolAndDomain(@Param("protocol") String protocol,
      @Param("domain") String domain);

  @Query("select tf.taInitiatorForm from TransportForms tf where tf.protocol = :protocol and tf.domain = :domain")
  public String getTaInitiatorFormByProtocolAndDomain(@Param("protocol") String protocol,
      @Param("domain") String domain);

  @Query("select tf.sutInitiatorForm from TransportForms tf where tf.protocol = :protocol and tf.domain = :domain")
  public String getSutInitiatorFormByProtocolAndDomain(@Param("protocol") String protocol,
      @Param("domain") String domain);

}
