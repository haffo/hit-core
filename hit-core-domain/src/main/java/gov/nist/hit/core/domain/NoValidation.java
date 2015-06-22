package gov.nist.hit.core.domain;

import java.util.HashSet;
import java.util.Set;

public class NoValidation implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  protected Set<String> ids = new HashSet<String>();

  /**
   * Gets the value of the id property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the id property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getId().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list {@link String }
   * 
   * 
   */
  public Set<String> getIds() {
    return ids;
  }

}
