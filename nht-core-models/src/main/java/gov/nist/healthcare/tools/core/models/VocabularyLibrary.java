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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "tableDefinition" })
@XmlRootElement(name = "TableLibrary")
@Entity
public class VocabularyLibrary {

	@XmlTransient
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@XmlTransient
	protected VocabularyCollection vocabularyCollection;

	@XmlTransient
	protected String displayName;

	@XmlTransient
	protected String displayDescription;

	@XmlTransient
	@JsonIgnore
	@NotNull
	@Column(columnDefinition = "LONGTEXT")
	protected String content;

	/** list of tables */
	@XmlTransient
	@Transient
	protected List<TableDefinition> tableList;

	@JsonIgnore
	@XmlElement(name = "TableDefinition", required = true)
	@Transient
	protected List<TableDefinition> tableDefinition;

	@XmlAttribute(name = "Description")
	@XmlSchemaType(name = "anySimpleType")
	@JsonIgnore
	@Transient
	protected String description;
	@XmlAttribute(name = "HL7Version")
	@JsonIgnore
	@Transient
	protected String hl7Version;
	@XmlAttribute(name = "Name", required = true)
	@JsonIgnore
	@Transient
	protected String name;
	@XmlAttribute(name = "OrganizationName")
	@XmlSchemaType(name = "anySimpleType")
	@JsonIgnore
	@Transient
	protected String organizationName;
	@XmlAttribute(name = "Status")
	@JsonIgnore
	@Transient
	protected String status;
	@XmlAttribute(name = "TableLibraryIdentifier")
	@XmlSchemaType(name = "anySimpleType")
	@JsonIgnore
	@Transient
	protected String tableLibraryIdentifier;
	@XmlAttribute(name = "TableLibraryVersion")
	@XmlSchemaType(name = "anySimpleType")
	@JsonIgnore
	@Transient
	protected String tableLibraryVersion;

	public VocabularyLibrary() {
		super();
	}

	public VocabularyLibrary(String displayName, String displayDescription,
			String content) {
		super();
		this.displayName = displayName;
		this.displayDescription = displayDescription;
		this.content = content;
	}

	public void initTableList() {
		if (tableList == null) {

			HashMap<Integer, TableDefinition> tables = new HashMap<Integer, TableDefinition>();
			int id = 0;
			for (Iterator<TableDefinition> iterator = tableDefinition
					.iterator(); iterator.hasNext();) {
				TableDefinition tableDefinition = iterator.next();
				tables.put(id, tableDefinition);
				id++;
			}
			tableList = new ArrayList<TableDefinition>(tables.values());
			Collections.sort(tableList, new Comparator<TableDefinition>() {

				@Override
				public int compare(TableDefinition one, TableDefinition other) {
					if (one.getIdNumber() != null
							&& other.getIdNumber() != null)
						return one.getIdNumber().compareTo(other.getIdNumber());
					else if (one.getId() != null && other.getId() != null)
						return one.getId().compareTo(other.getId());
					return 0;

				}
			});
		}
	}

	public void load(List<TableDefinition> tableDefinition, String description,
			String hl7Version, String name, String organizationName,
			String status, String tableLibraryIdentifier,
			String tableLibraryVersion) {
		this.tableDefinition = tableDefinition;
		this.description = description;
		this.hl7Version = hl7Version;
		this.name = name;
		this.organizationName = organizationName;
		this.status = status;
		this.tableLibraryIdentifier = tableLibraryIdentifier;
		this.tableLibraryVersion = tableLibraryVersion;
		initTableList();
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayDescription() {
		return displayDescription;
	}

	public void setDisplayDescription(String displayDescription) {
		this.displayDescription = displayDescription;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Gets the value of the description property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the value of the description property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setDescription(String value) {
		this.description = value;
	}

	/**
	 * Gets the value of the hl7Version property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getHL7Version() {
		return hl7Version;
	}

	/**
	 * Sets the value of the hl7Version property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setHL7Version(String value) {
		this.hl7Version = value;
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
	 * Gets the value of the organizationName property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getOrganizationName() {
		return organizationName;
	}

	/**
	 * Sets the value of the organizationName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setOrganizationName(String value) {
		this.organizationName = value;
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
	 * Gets the value of the tableLibraryIdentifier property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getTableLibraryIdentifier() {
		return tableLibraryIdentifier;
	}

	/**
	 * Sets the value of the tableLibraryIdentifier property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setTableLibraryIdentifier(String value) {
		this.tableLibraryIdentifier = value;
	}

	/**
	 * Gets the value of the tableLibraryVersion property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getTableLibraryVersion() {
		return tableLibraryVersion;
	}

	/**
	 * Sets the value of the tableLibraryVersion property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setTableLibraryVersion(String value) {
		this.tableLibraryVersion = value;
	}

	public List<TableDefinition> getTableList() {
		return tableList;
	}

	public void setTableList(List<TableDefinition> tableList) {
		this.tableList = tableList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTableDefinition(List<TableDefinition> tableDefinition) {
		this.tableDefinition = tableDefinition;
	}

	/**
	 * Gets the value of the tableDefinition property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the tableDefinition property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getTableDefinition().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link TableDefinition }
	 */
	public List<TableDefinition> getTableDefinition() {
		if (tableDefinition == null) {
			tableDefinition = new ArrayList<TableDefinition>();
		}
		return this.tableDefinition;
	}

	public VocabularyCollection getVocabularyCollection() {
		return vocabularyCollection;
	}

	public void setVocabularyCollection(
			VocabularyCollection vocabularyCollection) {
		this.vocabularyCollection = vocabularyCollection;
	}

}
