package gov.nist.healthcare.tools.core.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Embeddable
public class ValueSetLibrary {

	@JsonIgnore
	@Column(columnDefinition = "LONGTEXT")
	protected String valueSetXml;

	@JsonProperty("json")
	@Column(columnDefinition = "LONGTEXT")
	protected String valueSetJson;

	public String getValueSetXml() {
		return valueSetXml;
	}

	public void setValueSetXml(String valueSetXml) {
		this.valueSetXml = valueSetXml;
	}

	public String getValueSetJson() {
		return valueSetJson;
	}

	public void setValueSetJson(String valueSetJson) {
		this.valueSetJson = valueSetJson;
	}

	public ValueSetLibrary(String valueSetXml) {
		super();
		this.valueSetXml = valueSetXml;
	}

	public ValueSetLibrary() {
		super();
		// TODO Auto-generated constructor stub
	}

}
