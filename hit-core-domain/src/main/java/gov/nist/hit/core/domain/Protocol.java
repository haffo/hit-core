package gov.nist.hit.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Protocol implements Serializable {
 
  private static final long serialVersionUID = 1L;

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


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Protocol other = (Protocol) obj;
    if (value == null) {
      if (other.value != null)
        return false;
    } else if (!value.equals(other.value))
      return false;
    return true;
  }
  
  
  
  
}
