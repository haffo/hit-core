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

/**
 * Represents the data associated to a message validation.
 * 
 * @author Harold Affo (NIST)
 */
@ApiModel(value="ValidationResultItem", description="Data Model representing the failure/success of a message validation")
public class ValidationResultItem {

  protected String description;
  protected int column;
  protected int line;
  protected String path;
  protected String failureSeverity;
  protected String elementContent;
  protected String assertionDeclaration;
  protected String userComment;
  protected String assertionResult;
  protected String failureType;

  public ValidationResultItem(String description, int column, int line, String path,
      String failureSeverity, String elementContent, String assertionDeclaration,
      String userComment, String assertionResult, String failureType) {
    super();
    this.description = description;
    this.column = column;
    this.line = line;
    this.path = path;
    this.failureSeverity = failureSeverity;
    this.elementContent = elementContent;
    this.assertionDeclaration = assertionDeclaration;
    this.userComment = userComment;
    this.assertionResult = assertionResult;
    this.failureType = failureType;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getColumn() {
    return column;
  }

  public void setColumn(int column) {
    this.column = column;
  }

  public int getLine() {
    return line;
  }

  public void setLine(int line) {
    this.line = line;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getFailureSeverity() {
    return failureSeverity;
  }

  public void setFailureSeverity(String failureSeverity) {
    this.failureSeverity = failureSeverity;
  }

  public String getElementContent() {
    return elementContent;
  }

  public void setElementContent(String elementContent) {
    this.elementContent = elementContent;
  }

  public String getAssertionDeclaration() {
    return assertionDeclaration;
  }

  public void setAssertionDeclaration(String assertionDeclaration) {
    this.assertionDeclaration = assertionDeclaration;
  }

  public String getUserComment() {
    return userComment;
  }

  public void setUserComment(String userComment) {
    this.userComment = userComment;
  }

  public String getAssertionResult() {
    return assertionResult;
  }

  public void setAssertionResult(String assertionResult) {
    this.assertionResult = assertionResult;
  }

  public String getFailureType() {
    return failureType;
  }

  public void setFailureType(String failureType) {
    this.failureType = failureType;
  }

}
