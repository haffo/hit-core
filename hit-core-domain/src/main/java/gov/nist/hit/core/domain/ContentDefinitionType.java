package gov.nist.hit.core.domain;

public enum ContentDefinitionType {

  Extensional("Extensional"), Intensional("Intensional");
  private final String value;

  ContentDefinitionType(String v) {
    value = v;
  }

  public String value() {
    return value;
  }

  public static ContentDefinitionType fromValue(String v) {
    for (ContentDefinitionType c : ContentDefinitionType.values()) {
      if (c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }

}
