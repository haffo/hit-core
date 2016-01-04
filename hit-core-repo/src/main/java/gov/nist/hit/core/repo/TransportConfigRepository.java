package gov.nist.hit.core.repo;

import gov.nist.hit.core.domain.KeyValuePair;
import gov.nist.hit.core.domain.TestStepTestingType;
import gov.nist.hit.core.domain.TransportConfig;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransportConfigRepository extends JpaRepository<TransportConfig, Long>,
    JpaSpecificationExecutor {

  @Query("select config from TransportConfig config where config.user.id = :userId and config.protocol = :protocol and config.domain = :domain")
  TransportConfig findOneByUserAndProtocolAndDomain(@Param("userId") Long userId,
      @Param("protocol") String protocol, @Param("domain") String domain);

  @Query("select config from TransportConfig config where config.matches(:criteria,:type) == true")
  TransportConfig findOneByCriteria(@Param("criteria") KeyValuePair criteria,
      @Param("type") TestStepTestingType type);

  @Query("select config from TransportConfig config where config.matches(:criteria,:type) == true")
  TransportConfig findOneByOneMultipleCriteria(@Param("criteria") List<KeyValuePair> criteria,
      @Param("type") TestStepTestingType type);

}
