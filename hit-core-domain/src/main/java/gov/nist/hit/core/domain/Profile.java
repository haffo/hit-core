/**
 * This software was developed at the National Institute of Standards and Technology by employees
 * of the Federal Government in the course of their official duties. Pursuant to title 17 Section 105 of the
 * United States Code this software is not subject to copyright protection and is in the public domain.
 * This is an experimental system. NIST assumes no responsibility whatsoever for its use by other parties,
 * and makes no guarantees, expressed or implied, about its quality, reliability, or any other characteristic.
 * We would appreciate acknowledgement if the software is used. This software can be redistributed and/or
 * modified freely provided that any derivative works bear some notice that they are derived from it, and any
 * modified versions bear some notice that they have been modified.
 */

package gov.nist.hit.core.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Harold Affo (NIST)
 * 
 */

@Embeddable
public class Profile implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@JsonIgnore
	@Column(columnDefinition = "LONGTEXT")
	protected String profileXml;

	@NotNull
	@JsonProperty("json")
	@Column(columnDefinition = "LONGTEXT")
	protected String profileJson;

	@Column(nullable = true)
	protected String name;

	@Column(nullable = true)
	protected String description;

	public Profile() {
		super();
	}

	public Profile(String name, String description, String xml) {
		super();
		this.name = name;
		this.description = description;
		this.profileXml = xml;
	}

	public Profile(String profileXml, String profileJson) {
		super();
		this.profileXml = profileXml;
		this.profileJson = profileJson;
	}

	public Profile(String xml) {
		super();
		this.name = null;
		this.description = null;
		this.profileXml = xml;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProfileXml() {
		return profileXml;
	}

	public void setProfileXml(String profileXml) {
		this.profileXml = profileXml;
	}

	public String getProfileJson() {
		return profileJson;
	}

	public void setProfileJson(String profileJson) {
		this.profileJson = profileJson;
	}

}
