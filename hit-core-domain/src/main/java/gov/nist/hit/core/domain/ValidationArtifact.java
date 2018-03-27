package gov.nist.hit.core.domain;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ValidationArtifact extends DomainBased {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  protected boolean preloaded = true;

  public boolean isPreloaded() {
    return preloaded;
  }

  public void setPreloaded(boolean preloaded) {
    this.preloaded = preloaded;
  }



}
