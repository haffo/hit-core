package gov.nist.hit.core.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Protocol {
  
  @NotNull  
  @Column(nullable = false) 
  private String value;
  
  @NotNull 
  @Column(nullable = false) 
  private Integer position;
  
  private boolean defaut;
  
  public Protocol() {
    super();
  }
  
  
  public Protocol(String value, Integer position) {
    super();
    this.value = value;
    this.position = position;
  }

  public Protocol(String value, Integer position,boolean defaut) {
    super();
    this.value = value;
    this.position = position;
    this.defaut = defaut;
  }
  

  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }
  public Integer getPosition() {
    return position;
  }
  public void setPosition(Integer position) {
    this.position = position;
  }


  public boolean isDefaut() {
    return defaut;
  }


  public void setDefaut(boolean defaut) {
    this.defaut = defaut;
  }
  
  
  
  
}
