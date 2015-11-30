package gov.nist.hit.core.domain.constraints;

import java.io.Serializable;

 
//@Entity
//@Table(name = "CONF_STATEMENT")
 public class ConformanceStatement extends Constraint implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5723342171557075960L;

	public ConformanceStatement() {
		super();
	}

	@Override
	public String toString() {
		return "Constraint [id=" + id + ", constraintId=" + constraintId
				+ ", constraintTarget=" + constraintTarget +  ", description=" + description + ", assertion="
				+ assertion + "]";
	}

	@Override
	public ConformanceStatement clone() throws CloneNotSupportedException {
		return (ConformanceStatement) super.clone();
	}
	
	
	
	

}
