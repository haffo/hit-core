package gov.nist.hit.core.domain;

public enum ExtensibilityType {

  OPEN("Open"), CLOSED("Closed");
  private final String value;

  ExtensibilityType(String v) {
    value = v;
  }

  public String value() {
    return value;
  }

  public static ExtensibilityType fromValue(String v) {
    for (ExtensibilityType c : ExtensibilityType.values()) {
      if (c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }

}
