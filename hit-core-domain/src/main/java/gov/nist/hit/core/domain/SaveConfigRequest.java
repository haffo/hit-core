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


 public class SaveConfigRequest {
   private Long userId;
   private Map<String, String> config;
   private TestingType type;
   private String protocol;
   private String domain;
  public Long getUserId() {
    return userId;
  }
  public void setUserId(Long userId) {
    this.userId = userId;
  }
  public Map<String, String> getConfig() {
    return config;
  }
  public void setConfig(Map<String, String> config) {
    this.config = config;
  }
  public TestingType getType() {
    return type;
  }
  public void setType(TestingType type) {
    this.type = type;
  }
  public String getProtocol() {
    return protocol;
  }
  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }
  public String getDomain() {
    return domain;
  }
  public void setDomain(String domain) {
    this.domain = domain;
  }
  



}
