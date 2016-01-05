package gov.nist.hit.core.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;

@Entity
public class User implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;
  

//  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
//  @JoinTable(name = "user_configs", joinColumns = {@JoinColumn(name = "user_id")},
//      inverseJoinColumns = {@JoinColumn(name = "confi_id")})
//  
//  @OneToMany(mappedBy = "user")
//  private Set<TransportConfig>  configs = new HashSet<TransportConfig>();
//  

  public User() {
    super();
  }

  
  public void addConfig(TransportConfig config){
    if(config.getUser() != null)
      throw new RuntimeException("The configuration provided belongs already to another user");
//    this.getConfigs().add(config);
    config.setUser(this);
  }
 
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


//  public Set<TransportConfig> getConfigs() {
//    return configs;
//  }
//
//
//  public void setConfigs(Set<TransportConfig> configs) {
//    this.configs = configs;
//  }

  
  
   

}
