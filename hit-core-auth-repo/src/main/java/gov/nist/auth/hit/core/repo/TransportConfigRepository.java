package gov.nist.auth.hit.core.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gov.nist.auth.hit.core.domain.TransportConfig;

public interface TransportConfigRepository extends JpaRepository<TransportConfig, Long> {

  @Query("select tconfig from TransportConfig tconfig where tconfig.userId = :userId and tconfig.protocol = :protocol and tconfig.domain = :domain")
  TransportConfig findOneByUserAndProtocolAndDomain(@Param("userId") Long userId,
      @Param("protocol") String protocol, @Param("domain") String domain);

  @Query("select tconfig from TransportConfig tconfig where tconfig.userId = :userId and tconfig.protocol = :protocol and tconfig.domain = :domain")
  List<TransportConfig> findAllByUserAndProtocolAndDomain(@Param("userId") Long userId,
      @Param("protocol") String protocol, @Param("domain") String domain);

  @Query("select tconfig from TransportConfig tconfig where tconfig.userId = :userId")
  List<TransportConfig> findAllByUser(@Param("userId") Long userId);

}
