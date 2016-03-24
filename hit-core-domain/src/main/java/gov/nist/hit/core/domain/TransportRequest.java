/**
 * This software was developed at the National Institute of Standards and Technology by employees
 * of the Federal Government in the course of their official duties. Pursuant to title 17 Section 105 of the
 * United States Code this software is not subject to copyright protection and is in the public domain.
 * This is an experimental system. NIST assumes no responsibility whatsoever for its use by other parties,
 * and makes no guarantees, expressed or implied, about its quality, reliability, or any other characteristic.
 * We would appreciate acknowledgement if the software is used. This software can be redistributed and/or
 * modified freely provided that any derivative works bear some notice that they are derived from it, and any
 * modified versions bear some notice that they have been modified.
 */

package gov.nist.hit.core.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;


/**
 * @author Harold Affo (NIST)
 * 
 */
@ApiModel(value = "TransportRequest", description = "Data Model representing the request information of a transaction")
public class TransportRequest {

	private static final long serialVersionUID = 1L; 
	
	@ApiModelProperty(required = true, value = "configuration information of the sut")
	private Map<String, String> config; // required
	
	@ApiModelProperty(required = true, value = "id of the test step")
	private Long testStepId;
	
	@ApiModelProperty(required = true, value = "content of the message to be sent")
	private String message; 
	
	@ApiModelProperty(required = true, value = "id of the user executing the transaction")
	private Long userId;  
    
	@ApiModelProperty(required = true, value = "id of the message to respond with")
	private Long responseMessageId;  

	public TransportRequest() {
		super();
	}


  
  public Map<String, String> getConfig() {
    return config;
  }



  public void setConfig(Map<String, String> config) {
    this.config = config;
  }



  public Long getTestStepId() {
    return testStepId;
  }


  public void setTestStepId(Long testStepId) {
    this.testStepId = testStepId;
  }


  public String getMessage() {
    return message;
  }


  public void setMessage(String message) {
    this.message = message;
  }



  public Long getUserId() {
    return userId;
  }



  public void setUserId(Long userId) {
    this.userId = userId;
  }



  public Long getResponseMessageId() {
    return responseMessageId;
  }



  public void setResponseMessageId(Long responseMessageId) {
    this.responseMessageId = responseMessageId;
  }

  
	 

}
