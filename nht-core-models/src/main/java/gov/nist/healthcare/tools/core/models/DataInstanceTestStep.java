package gov.nist.healthcare.tools.core.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DataInstanceTestStep extends NewTestStep implements Serializable {

	private static final long serialVersionUID = 8805967508478985159L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public DataInstanceTestStep() {
		super();
		this.testContext = new TestContext();
		type = TestType.DataInstanceTestCase;
	}

	@ManyToOne
	@JoinColumn(name = "testcontext_id")
	private TestContext testContext;

	public DataInstanceTestStep(long id, String name, String description,
			Integer version) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.setVersion(version);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TestContext getTestContext() {
		return testContext;
	}

	public void setTestContext(TestContext testContext) {
		this.testContext = testContext;
	}

}
