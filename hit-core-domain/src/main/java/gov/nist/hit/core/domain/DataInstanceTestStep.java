package gov.nist.hit.core.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class DataInstanceTestStep extends TestObject implements Serializable {

	private static final long serialVersionUID = 8805967508478985159L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public DataInstanceTestStep() {
		super();
		this.type = TestType.TestStep;
		this.testContext = new TestContext();
		category = TestCategory.DataInstance;
	}

	@OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER, orphanRemoval = true)
	private TestContext testContext;

	public DataInstanceTestStep(String name) {
		super();
		this.name = name;
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

	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = super.hashCode();
	// result = prime * result
	// + ((testContext == null) ? 0 : testContext.hashCode());
	// return result;
	// }
	//
	// @Override
	// public boolean equals(Object obj) {
	// if (this == obj)
	// return true;
	// if (!super.equals(obj))
	// return false;
	// if (getClass() != obj.getClass())
	// return false;
	// DataInstanceTestStep other = (DataInstanceTestStep) obj;
	// if (testContext == null) {
	// if (other.testContext != null)
	// return false;
	// } else if (!testContext.equals(other.testContext))
	// return false;
	// return true;
	// }

}
