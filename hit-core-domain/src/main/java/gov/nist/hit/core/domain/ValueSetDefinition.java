package gov.nist.hit.core.domain;

import java.util.HashSet;
import java.util.Set;

public class ValueSetDefinition implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  protected Set<ValueSetElement> valueSetElements = new HashSet<ValueSetElement>();

  protected String bindingIdentifier;
  protected String name;
  protected String description;
  private String version;
  protected String oid;
  protected StabilityType stability;
  protected ExtensibilityType extensibility;
  protected ContentDefinitionType contentDefinition;
  protected String noCodeDisplayText;

  public Set<ValueSetElement> getValueSetElements() {
    return this.valueSetElements;
  }


  /**
   * Gets the value of the name property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the value of the name property.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setName(String value) {
    this.name = value;
  }



  /**
   * Gets the value of the version property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getVersion() {
    return version;
  }

  /**
   * Sets the value of the version property.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setVersion(String value) {
    this.version = value;
  }


  /**
   * Gets the value of the oid property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getOid() {
    return oid;
  }

  /**
   * Sets the value of the oid property.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setOid(String value) {
    this.oid = value;
  }



  /**
   * Gets the value of the stability property.
   * 
   * @return possible object is {@link StabilityType }
   * 
   */
  public StabilityType getStability() {
    return stability;
  }

  /**
   * Sets the value of the stability property.
   * 
   * @param value allowed object is {@link StabilityType }
   * 
   */
  public void setStability(StabilityType value) {
    this.stability = value;
  }

  /**
   * Gets the value of the extensibility property.
   * 
   * @return possible object is {@link ExtensibilityType }
   * 
   */
  public ExtensibilityType getExtensibility() {
    return extensibility;
  }

  /**
   * Sets the value of the extensibility property.
   * 
   * @param value allowed object is {@link ExtensibilityType }
   * 
   */
  public void setExtensibility(ExtensibilityType value) {
    this.extensibility = value;
  }

  public void addValueSetElement(ValueSetElement te) {
    getValueSetElements().add(te);
  }



  public void setValueSetElements(Set<ValueSetElement> valueSetElements) {
    this.valueSetElements = valueSetElements;
  }


  public String getBindingIdentifier() {
    return bindingIdentifier;
  }


  public void setBindingIdentifier(String bindingIdentifier) {
    this.bindingIdentifier = bindingIdentifier;
  }


  public String getDescription() {
    return description;
  }


  public void setDescription(String desscription) {
    this.description = desscription;
  }

  public ContentDefinitionType getContentDefinition() {
    return contentDefinition;
  }


  public void setContentDefinition(ContentDefinitionType contentDefinition) {
    this.contentDefinition = contentDefinition;
  }


  public String getNoCodeDisplayText() {
    return noCodeDisplayText;
  }


  public void setNoCodeDisplayText(String noCodeDisplayText) {
    this.noCodeDisplayText = noCodeDisplayText;
  }



}
