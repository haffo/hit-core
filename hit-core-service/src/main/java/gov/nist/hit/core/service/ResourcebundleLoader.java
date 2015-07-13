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

package gov.nist.hit.core.service;

import gov.nist.hit.core.service.exception.ProfileParserException;

import java.io.IOException;

import org.codehaus.jackson.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Harold Affo
 * 
 */

public interface ResourcebundleLoader {

  static final Logger logger = LoggerFactory.getLogger(ResourcebundleLoader.class);

  public void appInfo() throws JsonProcessingException, IOException;

  public void constraints() throws IOException;

  public void integrationProfiles() throws IOException;

  public void vocabularyLibraries() throws IOException;

  public void cb() throws IOException;

  public void isolated() throws IOException;

  public void cf() throws IOException, ProfileParserException;


}
