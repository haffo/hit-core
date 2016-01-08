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

import java.util.Map;


/**
 * @author Harold Affo (NIST)
 * 
 */
public class TransportRequest {

	private static final long serialVersionUID = 1L; 
	
	private Map<String, String> config;
	private Long testStepId; 
	private String message;
	private Long userId;
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
