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
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;


/**
 * @author Harold Affo (NIST)
 * 
 */
@ApiModel(value = "TransportResponse",
    description = "Data Model containing the response of a transaction")
public class TransportResponse {

  @ApiModelProperty(required = true, value = "id of the test step")
  private Long testStepId;
  @ApiModelProperty(required = true, value = "message sent")
  private String outgoingMessage;
  @ApiModelProperty(required = true, value = "message received")
  private String incomingMessage;



  public TransportResponse() {
    super();
  }



  public TransportResponse(Long testStepId, String outgoingMessage, String incomingMessage) {
    super();
    this.testStepId = testStepId;
    this.outgoingMessage = outgoingMessage;
    this.incomingMessage = incomingMessage;
  }



  public Long getTestStepId() {
    return testStepId;
  }



  public void setTestStepId(Long testStepId) {
    this.testStepId = testStepId;
  }



  public String getOutgoingMessage() {
    return outgoingMessage;
  }



  public void setOutgoingMessage(String outgoingMessage) {
    this.outgoingMessage = outgoingMessage;
  }



  public String getIncomingMessage() {
    return incomingMessage;
  }



  public void setIncomingMessage(String incomingMessage) {
    this.incomingMessage = incomingMessage;
  }



}
