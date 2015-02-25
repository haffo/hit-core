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
package gov.nist.healthcare.tools.core.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="Code" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="DisplayName" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="Source" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ConceptId" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="Status" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="Synonym" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="Codesys" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "TableElement", namespace = "http://www.nist.gov/healthcare/data")
public class TableElement {

	@XmlAttribute(name = "Code", required = true)
	@XmlSchemaType(name = "anySimpleType")
	protected String code;
	@XmlAttribute(name = "DisplayName")
	@XmlSchemaType(name = "anySimpleType")
	protected String displayName;
	@XmlAttribute(name = "Source")
	protected String source;
	@XmlAttribute(name = "ConceptId")
	@XmlSchemaType(name = "anySimpleType")
	protected String conceptId;
	@XmlAttribute(name = "Status")
	@XmlSchemaType(name = "anySimpleType")
	protected String status;
	@XmlAttribute(name = "Synonym")
	@XmlSchemaType(name = "anySimpleType")
	protected String synonym;
	@XmlAttribute(name = "Codesys")
	protected String codesys;

	/**
	 * Gets the value of the code property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the value of the code property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setCode(String value) {
		this.code = value;
	}

	/**
	 * Gets the value of the displayName property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the value of the displayName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setDisplayName(String value) {
		this.displayName = value;
	}

	/**
	 * Gets the value of the source property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Sets the value of the source property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setSource(String value) {
		this.source = value;
	}

	/**
	 * Gets the value of the conceptId property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getConceptId() {
		return conceptId;
	}

	/**
	 * Sets the value of the conceptId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setConceptId(String value) {
		this.conceptId = value;
	}

	/**
	 * Gets the value of the status property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the value of the status property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setStatus(String value) {
		this.status = value;
	}

	/**
	 * Gets the value of the synonym property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getSynonym() {
		return synonym;
	}

	/**
	 * Sets the value of the synonym property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setSynonym(String value) {
		this.synonym = value;
	}

	/**
	 * Gets the value of the codesys property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getCodesys() {
		return codesys;
	}

	/**
	 * Sets the value of the codesys property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setCodesys(String value) {
		this.codesys = value;
	}

}
