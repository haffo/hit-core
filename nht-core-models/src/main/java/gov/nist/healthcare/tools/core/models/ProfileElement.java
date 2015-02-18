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
package gov.nist.healthcare.tools.core.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProfileElement {

	protected String id;

	protected String name;

	// protected String cardinality;

	protected String longName;

	protected String path;

	protected String usage;

	protected String maxLength;

	protected String minLength;

	protected String table;

	protected String type;

	protected List<ProfileElement> children;

	protected Set<String> usages;

	protected SegmentRef reference;

	protected Set<Constraint> conformanceStatements = new HashSet<Constraint>();
	protected Set<Constraint> predicates = new HashSet<Constraint>();

	protected String dataType;

	protected Object minOccurs;

	protected Object maxOccurs;

	// protected String dataTypeDescription;
	//
	// protected String dataTypeUsage;

	protected String icon;

	protected String position;

	protected boolean relevent;

	protected int order;
	protected String lenght;

	protected String constraintPath;

	@JsonIgnore
	protected ProfileElement parent;

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

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
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

	// public String getDataTypeDescription() {
	// return dataTypeDescription;
	// }
	//
	// public void setDataTypeDescription(String dataTypeDescription) {
	// this.dataTypeDescription = dataTypeDescription;
	// }
	//
	// public String getDataTypeUsage() {
	// return dataTypeUsage;
	// }

	// public void setDataTypeUsage(String dataTypeUsage) {
	// this.dataTypeUsage = dataTypeUsage;
	// }

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

	// public String getCardinality() {
	// return cardinality;
	// }
	//
	// public void setCardinality(String cardinality) {
	// this.cardinality = cardinality;
	// }

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

	public Set<Constraint> getConformanceStatements() {
		return conformanceStatements;
	}

	public void setConformanceStatements(Set<Constraint> conformanceStatements) {
		this.conformanceStatements = conformanceStatements;
	}

	public Set<Constraint> getPredicates() {
		return predicates;
	}

	public void setPredicates(Set<Constraint> predicates) {
		this.predicates = predicates;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SegmentRef getReference() {
		return reference;
	}

	public void setReference(SegmentRef reference) {
		this.reference = reference;
	}

	public String getConstraintPath() {
		return constraintPath;
	}

	public void setConstraintPath(String constraintPath) {
		this.constraintPath = constraintPath;
	}

	public ProfileElement getParent() {
		return parent;
	}

	public void setParent(ProfileElement parent) {
		this.parent = parent;
	}

	public Set<String> getUsages() {
		if (usages == null)
			usages = new HashSet<String>();
		return usages;
	}

	public void setUsages(Set<String> usages) {
		this.usages = usages;
	}

	public boolean isRelevent() {
		return relevent;
	}

	public void setRelevent(boolean relevent) {
		this.relevent = relevent;
	}

}
