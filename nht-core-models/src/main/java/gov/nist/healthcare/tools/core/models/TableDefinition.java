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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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
 *       &lt;sequence>
 *         &lt;element ref="{http://www.nist.gov/healthcare/data}TableElement" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Codesys" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="Name" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="TestCategory" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="IdNumber" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="AlternateId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Oid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;sequence>
 *         &lt;element name="Comment" type="xs:string" maxOccurs="1" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "comment", "tableElement" })
@XmlRootElement(name = "TableDefinition", namespace = "http://www.nist.gov/healthcare/data")
public class TableDefinition {

	@XmlElement(name = "Comment", namespace = "http://www.nist.gov/healthcare/data")
	protected String comment;
	@XmlElement(name = "TableElement", namespace = "http://www.nist.gov/healthcare/data")
	protected List<TableElement> tableElement;
	@XmlAttribute(name = "Codesys", required = true)
	protected String codesys;
	@XmlAttribute(name = "Id")
	@XmlSchemaType(name = "anySimpleType")
	protected String id;
	@XmlAttribute(name = "Name")
	@XmlSchemaType(name = "anySimpleType")
	protected String name;
	@XmlAttribute(name = "Type")
	@XmlSchemaType(name = "anySimpleType")
	protected String type;
	@XmlAttribute(name = "TestCategory")
	@XmlSchemaType(name = "anySimpleType")
	protected String testCategory;
	@XmlAttribute(name = "IdNumber")
	@XmlSchemaType(name = "anySimpleType")
	protected String idNumber;
	@XmlAttribute(name = "AlternateId")
	protected String alternateId;
	@XmlAttribute(name = "Oid")
	protected String oid;

	/**
	 * Gets the value of the tableElement property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the tableElement property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getTableElement().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link TableElement }
	 */
	public List<TableElement> getTableElement() {
		if (tableElement == null) {
			tableElement = new ArrayList<TableElement>();
		}
		return this.tableElement;
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

	/**
	 * Gets the value of the id property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setId(String value) {
		this.id = value;
	}

	/**
	 * Gets the value of the name property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the name property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setType(String value) {
		this.type = value;
	}

	/**
	 * Gets the value of the testCategory property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getTestCategory() {
		return testCategory;
	}

	/**
	 * Sets the value of the testCategory property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setTestCategory(String value) {
		this.testCategory = value;
	}

	/**
	 * Gets the value of the idNumber property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getIdNumber() {
		return idNumber;
	}

	/**
	 * Sets the value of the idNumber property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setIdNumber(String value) {
		this.idNumber = value;
	}

	/**
	 * Gets the value of the alternateId property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getAlternateId() {
		return alternateId;
	}

	/**
	 * Sets the value of the alternateId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setAlternateId(String value) {
		this.alternateId = value;
	}

	/**
	 * Gets the value of the oid property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getOid() {
		return oid;
	}

	/**
	 * Sets the value of the oid property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setOid(String value) {
		this.oid = value;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
