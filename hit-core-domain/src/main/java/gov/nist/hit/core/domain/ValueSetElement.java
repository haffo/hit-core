package gov.nist.hit.core.domain;

public class ValueSetElement implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  protected String value; //code
  protected String displayName;
   protected String codeSystem;
  protected String codeSystemVersion;
  protected String comments;  
  private UsageType usage;
  
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }
  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
 
  public String getCodeSystem() {
    return codeSystem;
  }
  public void setCodeSystem(String codeSystem) {
    this.codeSystem = codeSystem;
  }
  public String getCodeSystemVersion() {
    return codeSystemVersion;
  }
  public void setCodeSystemVersion(String codeSystemVersion) {
    this.codeSystemVersion = codeSystemVersion;
  }
  public String getComments() {
    return comments;
  }
  public void setComments(String comments) {
    this.comments = comments;
  }
  public UsageType getUsage() {
    return usage;
  }
  public void setUsage(UsageType usage) {
    this.usage = usage;
  }

  
  
  
}
