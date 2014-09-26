/**
 * This software was developed at the National Institute of Standards and
 * Technology by employees of the Federal Government in the course of their
 * official duties. Pursuant to title 17 Section 105 of the United States Code
 * this software is not subject to copyright protection and is in the public
 * domain. This is an experimental system. NIST assumes no responsibility
 * whatsoever for its use by other parties, and makes no guarantees, expressed
 * or implied, about its quality, reliability, or any other characteristic. We
 * would appreciate acknowledgement if the software is used. This software can
 * be redistributed and/or modified freely provided that any derivative works
 * bear some notice that they are derived from it, and any modified versions
 * bear some notice that they have been modified.
 */
package gov.nist.healthcare.tools.core.models.message;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author Harold Affo (NIST)
 * 
 */

public class MessageElementData implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String path;

	protected String name;

	protected String usage;

	protected Integer minOccurs;

	protected Integer maxOccurs;

	protected int lineNumber = 0;

	protected int startIndex;

	protected int endIndex;

	protected int position;

	protected int instanceNumber;

	protected String description;

	protected String value;

	protected String type;

	protected String stringRepresentation;

	@JsonProperty("data_can_contain_anything")
	protected final boolean dataCanContainAnything = true;

	public String getPath() {

		return this.path;
	}

	public void setPath(String path) {

		this.path = path;
	}

	public String getName() {

		return this.name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getUsage() {

		return this.usage;
	}

	public void setUsage(String usage) {

		this.usage = usage;
	}

	public Integer getMinOccurs() {

		return this.minOccurs;
	}

	public void setMinOccurs(Integer minOccurs) {

		this.minOccurs = minOccurs;
	}

	public Integer getMaxOccurs() {

		return this.maxOccurs;
	}

	public void setMaxOccurs(Integer maxOccurs) {

		this.maxOccurs = maxOccurs;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * public Element getElement() { return element; } public void
	 * setElement(Element element) { this.element = element; }
	 */
	@Override
	public String toString() {

		if (this.value != null) {
			return this.value;
		} else {
			StringBuffer buffer1 = new StringBuffer();
			buffer1.append(this.path).append(":".charAt(0)).append(this.name)
					.append(" ".charAt(0)).append(this.usage).append("")
					.append("[".charAt(0)).append(printMinOccurs())
					.append(",".charAt(0)).append(printMaxOccurs())
					.append("]".charAt(0));
			return buffer1.toString();
		}
	}

	protected String printMaxOccurs() {

		if (this.maxOccurs == 65536) {
			return "*";
		}
		return this.maxOccurs + "";
	}

	protected String printMinOccurs() {

		if (this.minOccurs == -1) {
			return "";
		}
		return this.minOccurs + "";
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ (this.maxOccurs == null ? 0 : this.maxOccurs.hashCode());
		result = prime * result
				+ (this.minOccurs == null ? 0 : this.minOccurs.hashCode());
		result = prime * result
				+ (this.name == null ? 0 : this.name.hashCode());
		result = prime * result
				+ (this.path == null ? 0 : this.path.hashCode());
		result = prime * result
				+ (this.usage == null ? 0 : this.usage.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MessageElementData other = (MessageElementData) obj;
		if (this.maxOccurs == null) {
			if (other.maxOccurs != null) {
				return false;
			}
		} else if (!this.maxOccurs.equals(other.maxOccurs)) {
			return false;
		}
		if (this.minOccurs == null) {
			if (other.minOccurs != null) {
				return false;
			}
		} else if (!this.minOccurs.equals(other.minOccurs)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.path == null) {
			if (other.path != null) {
				return false;
			}
		} else if (!this.path.equals(other.path)) {
			return false;
		}
		if (this.usage == null) {
			if (other.usage != null) {
				return false;
			}
		} else if (!this.usage.equals(other.usage)) {
			return false;
		}
		return true;
	}

	public int getLineNumber() {

		return this.lineNumber;
	}

	public void setLineNumber(int lineNumber) {

		this.lineNumber = lineNumber;
	}

	public int getPosition() {

		return this.position;
	}

	public void setPosition(int position) {

		this.position = position;
	}

	public int getInstanceNumber() {

		return this.instanceNumber;
	}

	public void setInstanceNumber(int instanceNumber) {
		this.instanceNumber = instanceNumber;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStringRepresentation() {
		return stringRepresentation;
	}

	public void setStringRepresentation(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public boolean isDataCanContainAnything() {
		return dataCanContainAnything;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
