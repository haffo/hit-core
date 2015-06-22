/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */
package gov.nist.hit.core.domain;

import java.util.List;

/**
 * @author Harold Affo (NIST)
 */
public class ValidationFailure {

  protected String title;
  protected List<? extends Object> failures;

  public ValidationFailure(String title, List<? extends Object> failures) {
    super();
    this.title = title;
    this.failures = failures;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<? extends Object> getFailures() {
    return failures;
  }

  public void setFailures(List<? extends Object> failures) {
    this.failures = failures;
  }

}
