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

import gov.nist.hit.core.domain.ProfileModel;
import gov.nist.hit.core.service.exception.ProfileParserException;



public abstract class ProfileParser {

  protected final static String TYPE_GROUP = "GROUP";
  protected final static String TYPE_DATATYPE = "DATATYPE";
  protected final static String TYPE_SEGMENT = "SEGMENT";
  protected final static String TYPE_SEGMENT_REF = "SEGMENT_REF";
  protected final static String TYPE_FIELD = "FIELD";
  protected final static String TYPE_COMPONENT = "COMPONENT";
  protected final static String NODE_SEGMENT = "Segment";
  protected final static String NODE_DATATYPE = "Datatype";
  protected final static String NODE_GROUP = "Group";
  protected final static String TYPE_SUBCOMPONENT = "SUBCOMPONENT";
  protected final static String TYPE_MESSAGE = "MESSAGE";

  // protected final static String ICON_GROUP = "group.png";
  // protected final static String ICON_SEGMENT = "segment.png";
  // protected final static String ICON_FIELD = "field.png";
  // protected final static String ICON_DATATYPE = ICON_FIELD;
  // protected final static String ICON_COMPONENT = "component.png";
  // protected final static String ICON_SUBCOMPONENT = "subcomponent.png";


  // protected final static String ICON_GROUP = "G";
  // protected final static String ICON_SEGMENT = "S";
  // protected final static String ICON_FIELD = "F";
  // protected final static String ICON_DATATYPE = "D";
  // protected final static String ICON_COMPONENT = "C";
  // protected final static String ICON_SUBCOMPONENT = "S";

  protected ProfileModel model;


  public abstract ProfileModel parse(String integrationProfileXml, String conformanceProfileId,
      String... constraints) throws ProfileParserException;

  public abstract ProfileModel parse(Object conformanceProfile, String... constraints)
      throws ProfileParserException;
}
