package gov.nist.hit.core.domain;

public enum ManualValidationResultType {

  PASSED("Passed"), FAILED("Failed"),PASSED_NOTABLE_EXCEPTION("Passed - Notable Exception"),
  FAILED_NOT_SUPPORTED("Failed - Not Supported"),INCOMPLETE("Incomplete"),INCONCLUSIVE("Inconclusive");
  
  private final String value;

  ManualValidationResultType(String v) {
    value = v;
  }

  public String value() {
    return value;
  }

  public static ManualValidationResultType fromValue(String v) {
    for (ManualValidationResultType c : ManualValidationResultType.values()) {
      if (c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }

}
