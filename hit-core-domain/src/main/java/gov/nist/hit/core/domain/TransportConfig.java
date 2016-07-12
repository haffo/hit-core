package gov.nist.hit.core.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;


@Entity
@ApiModel(value = "TransportConfig",
    description = "Data Model representing the configuration information of a transaction")
public class TransportConfig {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;

  @ApiModelProperty(required = true, value = "protocol of the transaction",
      example = "soap, rest, etc...")
  @Column(name = "PROTOCOL")
  private String protocol;

  @ApiModelProperty(required = true, value = "domain of the transaction",
      example = "iz, erx, etc...")
  @Column(name = "DOMAIN")
  private String domain;

  @ApiModelProperty(required = true, value = "id of the user executing the transaction",
      example = "soap, rest, etc...")
  @Column(name = "USERID")
  private Long userId;

  @ApiModelProperty(required = true, value = "configuration information  provided by the sut")
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "sut_initiator_config", joinColumns = @JoinColumn(
      name = "transport_config_id"))
  @MapKeyColumn(name = "property_key")
  @Column(name = "property_value")
  protected Map<String, String> sutInitiator = new HashMap<String, String>();;

  @ApiModelProperty(required = true, value = "configuration information provided to the sut")
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "ta_initiator_config", joinColumns = @JoinColumn(
      name = "transport_config_id"))
  @MapKeyColumn(name = "property_key")
  @Column(name = "property_value")
  protected Map<String, String> taInitiator = new HashMap<String, String>();;

  public TransportConfig() {
    super();
  }


  public TransportConfig(String protocol, String domain) {
    this();
    this.protocol = protocol;
    this.domain = domain;
  }

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }



  public boolean matches(List<KeyValuePair> pairs, TestingType type) {
    if (!pairs.isEmpty()) {
      for (KeyValuePair pair : pairs) {
        if (!matches(pair, type)) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  public boolean matches(KeyValuePair pair, TestingType type) {
    Map<String, String> info = getConfigInfo(type);
    return pair.getKey() != null && info.containsKey(pair.getKey())
        && info.get(pair.getKey()).equals(pair.getValue());
  }

  public Map<String, String> getConfigInfo(TestingType type) {
    return type == TestingType.SUT_INITIATOR ? sutInitiator
        : type == TestingType.TA_INITIATOR ? taInitiator : new HashMap<String, String>();
  }


  public Map<String, String> getSutInitiator() {
    return sutInitiator;
  }


  public void setSutInitiator(Map<String, String> sutInitiator) {
    this.sutInitiator = sutInitiator;
  }


  public Map<String, String> getTaInitiator() {
    return taInitiator;
  }


  public void setTaInitiator(Map<String, String> taInitiator) {
    this.taInitiator = taInitiator;
  }


  public Long getUserId() {
    return userId;
  }


  public void setUserId(Long userId) {
    this.userId = userId;
  }


  public String getDomain() {
    return domain;
  }


  public void setDomain(String domain) {
    this.domain = domain;
  }

}
