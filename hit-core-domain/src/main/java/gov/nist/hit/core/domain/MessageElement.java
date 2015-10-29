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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * 
 * @author Harold Affo (NIST)
 * 
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class MessageElement implements Serializable {

  private static final long serialVersionUID = 1L;

  public static String DEFAULT_TYPE = "default";

  protected String type;

  protected MessageElementData data;

  protected String label;
  protected List<MessageElement> children;

  public MessageElement() {
    this(DEFAULT_TYPE, null, null, true);
  }

  public MessageElement(String type, MessageElementData data, MessageElement parent,
      boolean selectable) {
    this.type = type;
    this.data = data;
    this.children = new ArrayList<MessageElement>();
    if (parent != null) {
      parent.getChildren().add(this);
    }
    label = data != null ? data.getLabel() : "Unknown";
   }

  public MessageElement(String type, MessageElementData data, MessageElement parent) {

    this(DEFAULT_TYPE, data, parent, true);
  }

  public MessageElement(MessageElementData data, MessageElement parent) {

    this(DEFAULT_TYPE, data, parent, true);
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public static String getDefaultType() {
    return DEFAULT_TYPE;
  }

  public static void setDefaultType(String defaultType) {

    DEFAULT_TYPE = defaultType;
  }

  public MessageElementData getData() {
    return this.data;
  }

  public void setData(MessageElementData data) {
    this.data = data;
  }

  public List<MessageElement> getChildren() {
    return this.children == null ? this.children = new ArrayList<MessageElement>() : this.children;
  }

  public void setChildren(List<MessageElement> children) {
    this.children = children;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public int getChildCount() {
    return this.children.size();
  }

  public boolean isLeaf() {
    if (this.children == null) {
      return true;
    }
    return this.children.size() == 0;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (this.data == null ? 0 : this.data.hashCode());
    return result;
  }

  @Override
  public String toString() {
    if (this.data != null) {
      return this.data.toString();
    }
    return "";
  }

  public MessageElement getChild(int line) {
    List<? extends MessageElement> children = getChildren();
    for (MessageElement node : children) {
      MessageElementData data = node.getData();
      if (data.getLineNumber() == line) {
        return node;
      }
    }
    return null;
  }
}
