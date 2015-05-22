package gov.nist.healthcare.tools.core.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class IsolatedTestStep extends TestObject implements Cloneable,
		Serializable {

	private static final long serialVersionUID = -5928301465572845004L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@ManyToOne
	@JoinColumn(name = "s_actor_id")
	private Actor sActor;

	@ManyToOne
	@JoinColumn(name = "r_actor_id")
	private Actor rActor;

	@OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER, orphanRemoval = true)
	private TestContext testContext;

	public IsolatedTestStep(String name) {
		super();
		this.name = name;
	}

	public IsolatedTestStep() {
		super();
		category = TestCategory.Isolated;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Actor getsActor() {
		return sActor;
	}

	public void setsActor(Actor sActor) {
		this.sActor = sActor;
	}

	public Actor getrActor() {
		return rActor;
	}

	public void setrActor(Actor rActor) {
		this.rActor = rActor;
	}

	public TestContext getTestContext() {
		return testContext;
	}

	public void setTestContext(TestContext testContext) {
		this.testContext = testContext;
	}

}
