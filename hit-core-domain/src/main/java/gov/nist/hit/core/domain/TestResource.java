package gov.nist.hit.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public class TestResource implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @NotNull
  @Column(nullable = false)
  protected String domain;

  @JsonIgnore
  @Column(nullable = true)
  protected String authorUsername;

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }


  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  protected TestScope scope;

  protected boolean preloaded = false;

  public String getAuthorUsername() {
    return authorUsername;
  }

  public void setAuthorUsername(String authorUsername) {
    this.authorUsername = authorUsername;
  }

  public TestScope getScope() {
    return scope;
  }

  public void setScope(TestScope scope) {
    this.scope = scope;
  }

  public boolean isPreloaded() {
    return preloaded;
  }

  public void setPreloaded(boolean preloaded) {
    this.preloaded = preloaded;
  }



}
