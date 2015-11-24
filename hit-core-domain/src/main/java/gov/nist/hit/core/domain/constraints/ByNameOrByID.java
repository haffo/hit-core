package gov.nist.hit.core.domain.constraints;

import java.util.ArrayList;
import java.util.List;

 
//@Entity
//@Table(name = "BYNAME_OR_BYID")
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ByNameOrByID implements java.io.Serializable {

	/**
	 *  
	 */
	private static final long serialVersionUID = -5212340093784881862L;

 	// @Column(name = "ID")
	// @GeneratedValue(strategy = GenerationType.TABLE)
	protected String id;

	// @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	// @JoinTable(name = "BYNAME_OR_BYID_PREDICATE", joinColumns =
	// //@JoinColumn(name = "BYNAME_OR_BYID"), inverseJoinColumns =
	// //@JoinColumn(name = "PREDICATE"))
	protected List<Predicate> predicates = new ArrayList<Predicate>();

	// @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	// @JoinTable(name = "BYNAME_OR_BYID_CONFSTATEMENT", joinColumns =
	// //@JoinColumn(name = "BYNAME_OR_BYID"), inverseJoinColumns =
	// //@JoinColumn(name = "CONFSTATEMENT"))
	protected List<ConformanceStatement> conformanceStatements = new ArrayList<ConformanceStatement>();

	public List<Predicate> getPredicates() {
		return predicates;
	}

	public void setPredicates(List<Predicate> predicates) {
		this.predicates = predicates;
	}

	public List<ConformanceStatement> getConformanceStatements() {
		return conformanceStatements;
	}

	public void setConformanceStatements(
			List<ConformanceStatement> conformanceStatements) {
		this.conformanceStatements = conformanceStatements;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void addPredicate(Predicate e) {
		predicates.add(e);
	}

	public void addConformanceStatement(ConformanceStatement e) {
		conformanceStatements.add(e);
	}

}
