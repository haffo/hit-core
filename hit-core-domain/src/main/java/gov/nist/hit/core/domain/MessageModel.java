package gov.nist.hit.core.domain;

import io.swagger.annotations.ApiModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
 
@ApiModel(value="MessageModel", description="Data Model representing the parsed message")
public class MessageModel {

  private Map<String, String> delimeters;
  protected List<MessageElement> elements;

  public List<MessageElement> getElements() {
    return elements;
  }

  public void setElements(List<MessageElement> elements) {
    this.elements = elements;
  }

  /**
   * @param type
   * @param content
   */
  public MessageModel() {
    elements = new ArrayList<MessageElement>();
  }

  public MessageModel(MessageElement root) {
    elements = root.getChildren();
  }

  public MessageModel(List<MessageElement> elements, Map<String, String> delimeters) {
    this.elements = elements;
    this.delimeters = delimeters;
  }


  public Map<String, String> getDelimeters() {
    return delimeters;
  }

  public void setDelimeters(Map<String, String> delimeters) {
    this.delimeters = delimeters;
  }



}
