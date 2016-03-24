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

import java.io.Serializable;

/**
 * @author Harold Affo (NIST)
 * 
 */
public class Command implements Serializable {

  private static final long serialVersionUID = 1L;
  protected String content;
  protected String endpoint;
  protected Long testCaseId;
  protected Long userId;
  protected String type;
  protected String requestMessage;

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Command() {
    super();
    // TODO Auto-generated constructor stub
  }

  public Command(String content) {
    super();
    this.content = content;
  }

  public Command(String content, String endpoint) {
    super();
    this.content = content;
    this.endpoint = endpoint;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public Long getTestCaseId() {
    return testCaseId;
  }

  public void setTestCaseId(Long testCaseId) {
    this.testCaseId = testCaseId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getRequestMessage() {
    return requestMessage;
  }

  public void setRequestMessage(String requestMessage) {
    this.requestMessage = requestMessage;
  }

}
