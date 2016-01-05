package gov.nist.hit.core.repo;

import gov.nist.hit.core.domain.KeyValuePair;
import gov.nist.hit.core.domain.TestStepTestingType;
import gov.nist.hit.core.domain.TransportConfig;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransportConfigRepository extends JpaRepository<TransportConfig, Long> {

  @Query("select tconfig from TransportConfig tconfig where tconfig.user.id = :userId and tconfig.protocol = :protocol")
  TransportConfig findOneByUserAndProtocol(@Param("userId") Long userId,
      @Param("protocol") String protocol);

  @Query("select tconfig from TransportConfig tconfig where tconfig.matches(:criteria,:type) = true")
  TransportConfig findOneByCriteria(@Param("criteria") KeyValuePair criteria,
      @Param("type") TestStepTestingType type);

  @Query("select tconfig from TransportConfig tconfig where tconfig.matches(:criteria,:type) = true")
  TransportConfig findOneByOneMultipleCriteria(@Param("criteria") List<KeyValuePair> criteria,
      @Param("type") TestStepTestingType type);

  @Query("select tconfig from TransportConfig tconfig where tconfig.user.id = :userId")
  List<TransportConfig> findAllByUser(@Param("userId") Long userId);

  @Query("delete from TransportConfig tconfig where tconfig.user.id = :userId")
  void deleteAllByUser(@Param("userId") Long userId);

}
