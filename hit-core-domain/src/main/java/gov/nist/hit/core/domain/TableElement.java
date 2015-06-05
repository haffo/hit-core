package gov.nist.hit.core.domain;

public class TableElement implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	protected String code;
	protected String displayName;
	protected SourceType source;
	protected String codesys;
	protected UsageType usageType;

	/**
	 * Gets the value of the code property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the value of the code property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCode(String value) {
		this.code = value;
	}

	/**
	 * Gets the value of the displayName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the value of the displayName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDisplayName(String value) {
		this.displayName = value;
	}

	/**
	 * Gets the value of the source property.
	 * 
	 * @return possible object is {@link SourceType }
	 * 
	 */
	public SourceType getSource() {
		return source;
	}

	/**
	 * Sets the value of the source property.
	 * 
	 * @param value
	 *            allowed object is {@link SourceType }
	 * 
	 */
	public void setSource(SourceType value) {
		this.source = value;
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
	 * Gets the value of the usage property.
	 * 
	 * @return possible object is {@link UsageType }
	 * 
	 */
	public UsageType getUsageType() {
		if (usageType == null) {
			return UsageType.R;
		} else {
			return usageType;
		}
	}

	/**
	 * Sets the value of the usage property.
	 * 
	 * @param value
	 *            allowed object is {@link UsageType }
	 * 
	 */
	public void setUsageType(UsageType usageType) {
		this.usageType = usageType;
	}

}
