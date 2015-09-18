package gov.nist.hit.core.domain;

public enum TestDomain {

  HL7V2("hl7v2"), EDI("edi"),XML("xml");
  private final String value;

  TestDomain(String v) {
    value = v;
  }

  public String value() {
    return value;
  }

 

}
