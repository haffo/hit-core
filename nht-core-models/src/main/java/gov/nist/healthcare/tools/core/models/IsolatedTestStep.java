package gov.nist.healthcare.tools.core.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class IsolatedTestStep extends NewTestStep implements Cloneable,
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

	@ManyToOne
	@JoinColumn(name = "testcontext_id")
	private TestContext testContext;

	public IsolatedTestStep(long id, String name, String description,
			Integer version) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.setVersion(version);
	}

	public IsolatedTestStep() {
		super();
		this.testContext = new TestContext();
		type = TestType.IsolatedTestStep;
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
