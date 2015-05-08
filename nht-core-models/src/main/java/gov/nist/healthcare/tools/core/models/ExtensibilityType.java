package gov.nist.healthcare.tools.core.models;

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
