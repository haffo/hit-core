package gov.nist.hit.core.domain.constraints;

 
import gov.nist.hit.core.domain.Usage;

import java.io.Serializable;

 
//@Entity
//@Table(name = "PREDICATE")

 public class Predicate extends Constraint implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5723342171557075960L;

	public Predicate() {
		super();
	}

	// @Column(name = "TRUEUSAGE")
	private  Usage trueUsage;

	// @Column(name = "FALSEUSAGE")
	private Usage falseUsage;

	public Usage getTrueUsage() {
		return trueUsage;
	}

	public void setTrueUsage(Usage trueUsage) {
		this.trueUsage = trueUsage;
	}

	public Usage getFalseUsage() {
		return falseUsage;
	}

	public void setFalseUsage(Usage falseUsage) {
		this.falseUsage = falseUsage;
	}

	@Override
	public String toString() {
		return "Constraint [id=" + id + ", constraintId=" + constraintId
				+ ", constraintTarget=" + constraintTarget + ", description=" + description + ", assertion="
				+ assertion + "]";
	}

	@Override
	public Predicate clone() throws CloneNotSupportedException {
		return (Predicate) super.clone();
	}

}
