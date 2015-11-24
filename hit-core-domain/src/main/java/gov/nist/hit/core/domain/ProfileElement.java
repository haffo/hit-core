/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */
package gov.nist.hit.core.domain;

import gov.nist.hit.core.domain.constraints.ConformanceStatement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProfileElement {

  protected String id;

  protected String name;

  protected String description;

  protected String path;

  protected String usage;

  protected String maxLength;

  protected String minLength;

  protected String table;

  protected String type;

  protected List<ProfileElement> children;
  protected List<gov.nist.hit.core.domain.constraints.Predicate> predicates = new ArrayList<gov.nist.hit.core.domain.constraints.Predicate>();
  protected List<ConformanceStatement> conformanceStatements = new ArrayList<ConformanceStatement>();


  protected String ref;
 
  protected Map<Integer, Set<String>> dynamicMaps;

  protected String datatype;

  protected String min;

  protected String max;

//  protected String icon;

  protected String position;

  protected boolean relevent;

   protected String lenght;
  
  protected boolean hide;
  
  @JsonIgnore
  protected ProfileElement parent;
  
 
  public String getName() {
    return name;
  }

  
  public Map<Integer, Set<String>> getDynamicMaps() {
    return dynamicMaps;
  }

  public void setDynamicMaps(Map<Integer, Set<String>> dynamicMaps) {
    this.dynamicMaps = dynamicMaps;
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

 
  public String getDatatype() {
    return datatype;
  }


  public void setDatatype(String datatype) {
    this.datatype = datatype;
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

//  public String getIcon() {
//    return icon;
//  }
//
//  public void setIcon(String icon) {
//    this.icon = icon;
//  }

  public String getMin() {
    return min;
  }


  public void setMin(String min) {
    this.min = min;
  }


  public String getMax() {
    return max;
  }


  public void setMax(String max) {
    this.max = max;
  }


  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
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

 
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
 
  

//  public String getConstraintPath() {
//    return constraintPath;
//  }
//
//  public void setConstraintPath(String constraintPath) {
//    this.constraintPath = constraintPath;
//  }

  public String getRef() {
    return ref;
  }


  public void setRef(String ref) {
    this.ref = ref;
  }


  public ProfileElement getParent() {
    return parent;
  }

  public void setParent(ProfileElement parent) {
    this.parent = parent;
  }

  
  public boolean isRelevent() {
    return relevent;
  }

  public void setRelevent(boolean relevent) {
    this.relevent = relevent;
  }
  
  
  
 
  public boolean isHide() {
    return hide;
  }

  public void setHide(boolean hide) {
    this.hide = hide;
  }
  
  
  

  public List<gov.nist.hit.core.domain.constraints.Predicate> getPredicates() {
    return predicates;
  }


  public void setPredicates(List<gov.nist.hit.core.domain.constraints.Predicate> predicates) {
    this.predicates = predicates;
  }


  public List<ConformanceStatement> getConformanceStatements() {
    return conformanceStatements;
  }


  public void setConformanceStatements(List<ConformanceStatement> conformanceStatements) {
    this.conformanceStatements = conformanceStatements;
  }


  
  
  @Override
  public ProfileElement clone() throws CloneNotSupportedException {
    ProfileElement element = new ProfileElement();
    element.setChildren(clone(children));
//    element.setConformanceStatements(cloneConstraints(conformanceStatements));
    element.setDatatype(datatype);
//    element.setIcon(icon);
    element.setId(id);
     element.setMaxLength(maxLength);
    element.setMinLength(minLength);
    element.setMin(min);
    element.setName(name);
    element.setMax(max);
    element.setLenght(lenght);
     element.setParent(parent);
     element.setPath(path);
    element.setPosition(position);
//    element.setPredicates(clonePredicates(predicates));
    element.setRef(ref);
    element.setRelevent(relevent);
    element.setTable(table);
    element.setType(type);
    element.setUsage(usage);
     element.setHide(hide);
     
    return element;
  }
  
  public static List<ProfileElement> clone(List<ProfileElement> children) throws CloneNotSupportedException {
    if( children== null) return null;
    if( children.size() == 0) return new ArrayList<ProfileElement>();
    ArrayList<ProfileElement> clones = new ArrayList<ProfileElement>();
    for (ProfileElement child : children) {
      ProfileElement clone = child.clone();
      clones.add(clone);
    }
    return clones;
  }
  
  public static Set<Constraint> cloneConstraints(Set<Constraint> constraints) throws CloneNotSupportedException {
    if( constraints== null) return null;
    if( constraints.size() == 0) return new HashSet<Constraint>();
    Set<Constraint> clones = new HashSet<Constraint>();
    for (Constraint child : constraints) {
      Constraint clone = child.clone();
      clones.add(clone);
    }
     return clones;
  }
  
  public static Set<Predicate> clonePredicates(Set<Predicate> predicates) throws CloneNotSupportedException {
    if( predicates== null) return null;
    if( predicates.size() == 0) return new HashSet<Predicate>();
    HashSet<Predicate> clones = new HashSet<Predicate>();
    for (Predicate child : predicates) {
      Predicate clone = child.clone();
      clones.add(clone);
    }
     return clones;
  }
  
  

 
  
}
