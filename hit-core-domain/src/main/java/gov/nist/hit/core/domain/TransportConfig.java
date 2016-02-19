package gov.nist.hit.core.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class TransportConfig {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;

  @Column(name = "PROTOCOL")
  private String protocol; 
  
  @Column(name = "DOMAIN")
  private String domain;
  
  @Column(name = "USERID")
  private Long userId;
 
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "sut_initiator_config", joinColumns=@JoinColumn(name="transport_config_id"))
  @MapKeyColumn(name = "property_key")
  @Column(name = "property_value")
  protected Map<String, String> sutInitiator = new HashMap<String, String>();;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "ta_initiator_config", joinColumns=@JoinColumn(name="transport_config_id"))
  @MapKeyColumn(name = "property_key")
  @Column(name = "property_value")
  protected Map<String, String> taInitiator = new HashMap<String, String>();;

  public TransportConfig() {
    super();
  }


  public TransportConfig(String protocol,String domain) {
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

 

  public boolean matches(List<KeyValuePair> pairs, TestStepTestingType type) {
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

  public boolean matches(KeyValuePair pair, TestStepTestingType type) {
    Map<String, String> info = getConfigInfo(type);
    return pair.getKey() != null && info.containsKey(pair.getKey())
        && info.get(pair.getKey()).equals(pair.getValue());
  }

  public Map<String, String> getConfigInfo(TestStepTestingType type) {
    return type == TestStepTestingType.SUT_INITIATOR ? sutInitiator
        : type == TestStepTestingType.TA_INITIATOR ? taInitiator : new HashMap<String, String>();
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

 


}
