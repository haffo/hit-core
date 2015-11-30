package gov.nist.hit.core.domain.constraints;

import java.io.Serializable;
import java.util.UUID;

  
//@Entity
//@Table(name = "IGCONSTRAINT")
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Constraint implements Serializable, Cloneable {

	private static final long serialVersionUID = 5723342171557075960L;

	public Constraint() {
		super();
		this.id = UUID.randomUUID().toString();
	}

	// @Id
	// @Column(name = "ID")
	// @GeneratedValue(strategy = GenerationType.TABLE)
 	protected String id;

	// @NotNull
	// @Column(nullable = false, name = "CONSTRAINT_ID")
	protected String constraintId;

	// @Column(name = "CONSTRAINT_TARGET")
	protected String constraintTarget;
 

	// @NotNull
	// @Column(nullable = false, name = "CONSTRAINT_DEC")
	// ?? Should this be removed since there is already description in reference
	protected String description;

	// @NotNull
	// @Column(nullable = false, columnDefinition = "LONGTEXT", name =
	// "CONSTRAINT_ASSERTION")
	protected String assertion;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getConstraintId() {
		return constraintId;
	}

	public void setConstraintId(String constraintId) {
		this.constraintId = constraintId;
	}

	public String getConstraintTarget() {
		return constraintTarget;
	}

	public void setConstraintTarget(String constraintTarget) {
		this.constraintTarget = constraintTarget;
	}

 

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAssertion() {
		return assertion;
	}

	public void setAssertion(String assertion) {
		this.assertion = assertion;
	}

	@Override
	public String toString() {
		return "Constraint [id=" + id + ", constraintId=" + constraintId
				+ ", constraintTarget=" + constraintTarget +  ", description=" + description + ", assertion="
				+ assertion + "]";
	}

	@Override
	protected Constraint clone() throws CloneNotSupportedException {
		Constraint c = (Constraint) super.clone();
		c.setId(this.id);
		return c;
	}

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((constraintId == null) ? 0 : constraintId.hashCode());
    result = prime * result + ((constraintTarget == null) ? 0 : constraintTarget.hashCode());
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
    if (constraintId == null) {
      if (other.constraintId != null)
        return false;
    } else if (!constraintId.equals(other.constraintId))
      return false;
    if (constraintTarget == null) {
      if (other.constraintTarget != null)
        return false;
    } else if (!constraintTarget.equals(other.constraintTarget))
      return false;
    return true;
  }
	
	
	
	
}
