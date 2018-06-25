package gov.nist.hit.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import gov.nist.hit.core.domain.TransportForms;

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


  @Modifying
  @Transactional(value = "transactionManager")
  @Query("delete from TransportForms to where to.domain = :domain")
  public void deleteByDomain(@Param("domain") String domain);



}
