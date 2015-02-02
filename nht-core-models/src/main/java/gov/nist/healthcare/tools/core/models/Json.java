package gov.nist.healthcare.tools.core.models;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 
 * @author Harold Affo
 * 
 */
public class Json {

	private final String value;

	public Json(String value) {
		this.value = value;
	}

	@JsonValue
	@JsonRawValue
	public String value() {
		return value;
	}
}