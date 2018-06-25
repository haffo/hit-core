package gov.nist.hit.core.domain;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ValidationArtifact extends TestResource {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public ValidationArtifact() {
    this.preloaded = true;
  }



}
