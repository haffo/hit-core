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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Harold Affo(NIST)
 * 
 */
@ApiModel(value="MessageValidationCommand", description="Data Model representing a request to validate a message")
public class MessageValidationCommand extends TestCaseCommand {

  @ApiModelProperty(required=true, value="content to parse")
  private String content;

   private Map<String, String> nav;;

   private String facilityId; 
  
   private String contextType; 
  
   private String name;
   
   @ApiModelProperty(required=false, value="data quality codes to validate")
   private ArrayList<String> dqaCodes; 

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

 
  public String getFacilityId() {
    return facilityId;
  }

  public void setFacilityId(String facilityId) {
    this.facilityId = facilityId;
  }

  public String getContextType() {
    return contextType;
  }

  public void setContextType(String contextType) {
    this.contextType = contextType;
  }

  public ArrayList<String> getDqaCodes() {
    return dqaCodes;
  }

  public void setDqaCodes(ArrayList<String> dqaCodes) {
    this.dqaCodes = dqaCodes;
  }

  
  public Map<String, String> getNav() {
    return nav;
  }

  public void setNav(Map<String, String> nav) {
    this.nav = nav;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  
 
 
}
