package gov.nist.healthcare.tools.core.models;

import java.util.HashSet;
import java.util.Set;

public class TableDefinition implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	protected Set<TableElement> tableElements = new HashSet<TableElement>();

	protected String tdId;
	protected String name;
	protected TableType type;
	protected String version;
	protected String codesys;
	protected String oid;
	protected String alternateId;

	protected StabilityType stability;
	protected ExtensibilityType extensibility;

	public Set<TableElement> getTableElements() {
		return this.tableElements;
	}

	/**
	 * Gets the value of the id property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTdId() {
		return tdId;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTdId(String tdId) {
		this.tdId = tdId;
	}

	/**
	 * Gets the value of the name property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the name property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link TableType }
	 * 
	 */
	public TableType getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value
	 *            allowed object is {@link TableType }
	 * 
	 */
	public void setType(TableType value) {
		this.type = value;
	}

	/**
	 * Gets the value of the version property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the value of the version property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setVersion(String value) {
		this.version = value;
	}

	/**
	 * Gets the value of the codesys property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodesys() {
		return codesys;
	}

	/**
	 * Sets the value of the codesys property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCodesys(String value) {
		this.codesys = value;
	}

	/**
	 * Gets the value of the oid property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOid() {
		return oid;
	}

	/**
	 * Sets the value of the oid property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOid(String value) {
		this.oid = value;
	}

	/**
	 * Gets the value of the alternateId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAlternateId() {
		return alternateId;
	}

	/**
	 * Sets the value of the alternateId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAlternateId(String value) {
		this.alternateId = value;
	}

	/**
	 * Gets the value of the stability property.
	 * 
	 * @return possible object is {@link StabilityType }
	 * 
	 */
	public StabilityType getStability() {
		return stability;
	}

	/**
	 * Sets the value of the stability property.
	 * 
	 * @param value
	 *            allowed object is {@link StabilityType }
	 * 
	 */
	public void setStability(StabilityType value) {
		this.stability = value;
	}

	/**
	 * Gets the value of the extensibility property.
	 * 
	 * @return possible object is {@link ExtensibilityType }
	 * 
	 */
	public ExtensibilityType getExtensibility() {
		return extensibility;
	}

	/**
	 * Sets the value of the extensibility property.
	 * 
	 * @param value
	 *            allowed object is {@link ExtensibilityType }
	 * 
	 */
	public void setExtensibility(ExtensibilityType value) {
		this.extensibility = value;
	}

	public void addTableElement(TableElement te) {
		getTableElements().add(te);
	}

}
