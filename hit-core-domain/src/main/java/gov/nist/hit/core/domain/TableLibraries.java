/**
 * This software was developed at the National Institute of Simport java.util.HashSet; import
 * java.util.List; import java.util.Set; e course of their official duties. Pursuant to title 17
 * Section 105 of the United States Code this software is not subject to copyright protection and is
 * in the public domain. This is an experimental system. NIST assumes no responsibility whatsoever
 * for its use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */
package gov.nist.hit.core.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * @author Harold Affo
 * 
 */

public class TableLibraries {

  protected String name;

  protected int position;

  protected Set<ValueSetLibrary> children = new HashSet<ValueSetLibrary>();

  public String getName() {
    return name;
  }

  public TableLibraries() {
    super();
  }

  public void setName(String name) {
    this.name = name;
  }

  public TableLibraries(String name) {
    super();
    this.name = name;
  }

  public TableLibraries(String name, Set<ValueSetLibrary> libraries) {
    super();
    this.name = name;
    if (libraries != null && !libraries.isEmpty()) {
      Iterator<ValueSetLibrary> it = libraries.iterator();
      while (it.hasNext()) {
        addTableLibrary(it.next());
      }
    }
  }

  public void addTableLibrary(ValueSetLibrary library) {
    children.add(library);
  }

  public Set<ValueSetLibrary> getChildren() {
    return children;
  }

  public void setChildren(Set<ValueSetLibrary> children) {
    this.children = children;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

}
