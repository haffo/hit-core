package gov.nist.hit.core.domain;

import java.io.Serializable;

public class Constraint implements Serializable {

  /**
	 * 
	 */
  private static final long serialVersionUID = 5723342171557075960L;

  protected String id;

  protected String description;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Constraint(String id, String description) {
    super();
    this.id = id;
    this.description = description;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Constraint other = (Constraint) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  protected Constraint clone() throws CloneNotSupportedException {
    Constraint clone = new Constraint(id, description);
    return clone;
  }
  
  

}
