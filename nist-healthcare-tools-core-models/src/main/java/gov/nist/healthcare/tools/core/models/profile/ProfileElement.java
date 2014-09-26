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
package gov.nist.healthcare.tools.core.models.profile;

import java.util.ArrayList;
import java.util.List;

public class ProfileElement {

	protected String name;

	protected String cardinality;

	protected String longName;

	protected String path;

	protected String title;

	protected String usage;

	protected String length;

	protected String maxLength;

	protected String minLength;

	protected String table;

	protected String type;

	protected List<ProfileElement> children;

	protected String predicate;
	protected String implementationNote;
	protected String reference;

	protected Object conformanceStatement;

	protected String dataType;

	protected Object minOccurs;

	protected Object maxOccurs;

	protected String predicateTrueUsage;

	protected String predicateFalseUsage;

	protected String dataTypeDescription;

	protected String dataTypeUsage;

	protected String icon;

	protected String position;

	protected int order;
	protected String lenght;

	public ProfileElement(String name, String cardinality, String longName,
			String path, String title, String usage, String length,
			String maxLength, String minLength, String table, String type,
			List<ProfileElement> children, String predicate,
			String implementationNote, String reference,
			Object conformanceStatement, String dataType, Object minOccurs,
			Object maxOccurs, String predicateTrueUsage,
			String predicateFalseUsage, String dataTypeDescription,
			String dataTypeUsage, String icon, String position, int order,
			String lenght) {
		super();
		this.name = name;
		this.cardinality = cardinality;
		this.longName = longName;
		this.path = path;
		this.title = title;
		this.usage = usage;
		this.length = length;
		this.maxLength = maxLength;
		this.minLength = minLength;
		this.table = table;
		this.type = type;
		this.children = children;
		this.predicate = predicate;
		this.implementationNote = implementationNote;
		this.reference = reference;
		this.conformanceStatement = conformanceStatement;
		this.dataType = dataType;
		this.minOccurs = minOccurs;
		this.maxOccurs = maxOccurs;
		this.predicateTrueUsage = predicateTrueUsage;
		this.predicateFalseUsage = predicateFalseUsage;
		this.dataTypeDescription = dataTypeDescription;
		this.dataTypeUsage = dataTypeUsage;
		this.icon = icon;
		this.position = position;
		this.order = order;
		this.lenght = lenght;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public String getMinLength() {
		return minLength;
	}

	public void setMinLength(String minLength) {
		this.minLength = minLength;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public String getImplementationNote() {
		return implementationNote;
	}

	public void setImplementationNote(String implementationNote) {
		this.implementationNote = implementationNote;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Object getConformanceStatement() {
		return conformanceStatement;
	}

	public void setConformanceStatement(Object conformanceStatement) {
		this.conformanceStatement = conformanceStatement;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Object getMinOccurs() {
		return minOccurs;
	}

	public void setMinOccurs(Object minOccurs) {
		this.minOccurs = minOccurs;
	}

	public Object getMaxOccurs() {
		return maxOccurs;
	}

	public void setMaxOccurs(Object maxOccurs) {
		this.maxOccurs = maxOccurs;
	}

	public String getPredicateTrueUsage() {
		return predicateTrueUsage;
	}

	public void setPredicateTrueUsage(String predicateTrueUsage) {
		this.predicateTrueUsage = predicateTrueUsage;
	}

	public String getPredicateFalseUsage() {
		return predicateFalseUsage;
	}

	public void setPredicateFalseUsage(String predicateFalseUsage) {
		this.predicateFalseUsage = predicateFalseUsage;
	}

	public String getDataTypeDescription() {
		return dataTypeDescription;
	}

	public void setDataTypeDescription(String dataTypeDescription) {
		this.dataTypeDescription = dataTypeDescription;
	}

	public String getDataTypeUsage() {
		return dataTypeUsage;
	}

	public void setDataTypeUsage(String dataTypeUsage) {
		this.dataTypeUsage = dataTypeUsage;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	public String getLenght() {
		return lenght;
	}

	public void setLenght(String lenght) {
		this.lenght = lenght;
	}

	public ProfileElement(String name) {
		this();
		this.name = name;
	}

	public ProfileElement() {

		children = new ArrayList<ProfileElement>();
	}

	public String getCardinality() {
		return cardinality;
	}

	public void setCardinality(String cardinality) {
		this.cardinality = cardinality;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<ProfileElement> getChildren() {
		return children;
	}

	public void setChildren(List<ProfileElement> children) {
		this.children = children;
	}

}
