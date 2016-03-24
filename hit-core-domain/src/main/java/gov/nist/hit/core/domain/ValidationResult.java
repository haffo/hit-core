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

import io.swagger.annotations.ApiModel;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * Validation Result by category.
 * 
 * @author Harold Affo (NIST)
 */
@ApiModel(value="ValidationResult", description="Data Model representing the result of a message validation")
public class ValidationResult {

  protected List<ValidationResultItem> errors;
  protected List<ValidationResultItem> alerts;
  protected List<ValidationResultItem> warnings;
  protected List<ValidationResultItem> affirmatives;
  protected List<ValidationResultItem> ignores;

  protected String xml;

  protected String html;

  public ValidationResult() {}

  public void setXml(String xml) {
    this.xml = xml;
  }

  public String getXml() {
    return xml;
  }

  public List<ValidationResultItem> getErrors() {
    return errors;
  }

  public void setErrors(List<ValidationResultItem> errors) {
    this.errors = errors;
  }

  public List<ValidationResultItem> getAlerts() {
    return alerts;
  }

  public void setAlerts(List<ValidationResultItem> alerts) {
    this.alerts = alerts;
  }

  public List<ValidationResultItem> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<ValidationResultItem> warnings) {
    this.warnings = warnings;
  }

  public List<ValidationResultItem> getAffirmatives() {
    return affirmatives;
  }

  public void setAffirmatives(List<ValidationResultItem> affirmatives) {
    this.affirmatives = affirmatives;
  }

  public List<ValidationResultItem> getIgnores() {
    return ignores;
  }

  public void setIgnores(List<ValidationResultItem> ignores) {
    this.ignores = ignores;
  }

  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
  }

}
