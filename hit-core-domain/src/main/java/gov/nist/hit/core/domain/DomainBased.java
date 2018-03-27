package gov.nist.hit.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public class DomainBased implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @NotNull
  @Column(nullable = false)
  protected String domain;


  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }


}
