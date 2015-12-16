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
  

  @ElementCollection
  @CollectionTable(name = "SUT_INITIATOR_CONFIG")
  @MapKeyColumn(name = "PROPERTY_KEY")
  @Column(name = "PROPERTY_VALUE")
  protected Map<String, String> sutInitiator;

  @ElementCollection
  @CollectionTable(name = "TA_INITIATOR_CONFIG")
  @MapKeyColumn(name = "PROPERTY_KEY")
  @Column(name = "PROPERTY_VALUE")
  protected Map<String, String> taInitiator;

  @JsonIgnore
  @ManyToOne(cascade=CascadeType.ALL)  
  protected User user;

  public TransportConfig() {
    super();
    sutInitiator = new HashMap<String, String>();
    taInitiator = new HashMap<String, String>();
  }


  public TransportConfig(String protocol) {
    this();
    this.protocol = protocol;
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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public boolean matches(List<KeyValuePair> pairs, TestStepTestingType type) {
    if (!pairs.isEmpty()) {
      for (KeyValuePair pair : pairs) {
        boolean val = matches(pair, type);
        if (!val) {
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
        : type == TestStepTestingType.TA_INITIATOR ? taInitiator
            : new HashMap<String, String>();
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


  public String getDomain() {
    return domain;
  }


  public void setDomain(String domain) {
    this.domain = domain;
  }


  



}
